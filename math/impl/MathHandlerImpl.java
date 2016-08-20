package com.meowingtwurtle.math.impl;

import com.meowingtwurtle.math.api.IMathGroup;
import com.meowingtwurtle.math.api.IMathHandler;
import com.meowingtwurtle.math.impl.function.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MathHandlerImpl implements IMathHandler {

    INSTANCE;
    
    private final Map<String, Class<?>> functions = new HashMap<String, Class<?>>() {
        /**
        * 
        */
        private static final long serialVersionUID = -8782756002049336410L;

        {
            put("sin", MathFunctionSin.class);
            put("cos", MathFunctionCos.class);
            put("tan", MathFunctionTan.class);
            put("abs", MathFunctionAbs.class);
            put("sqrt", MathFunctionSqrt.class);
        }
    };

    public IMathGroup parse(String exp) {
        exp = cleanExp(exp);

        System.out.println("exp = [" + exp + "]");

        if (exp.contains("()")) {
            return null;
        }

        if (!(exp.contains("+") || exp.contains("-") || exp.contains("*") || exp.contains("/") || exp.contains("^") || exp.contains("(") || exp.contains(")"))) {
            return parseBasicExp(exp);
        }

        if (!exp.contains("(") && !exp.contains(")")) {
            return parseNoParens(exp);
        } else {
            if (countCharInString(exp, '(') != countCharInString(exp, ')')) {
//                System.out.println("Mismatched parens");
                return null;
            }
            for (String s : functions.keySet()) {
                if (exp.startsWith(s)) {
                    return parseWithFunction(exp, s);
                }
            }
            return parseWithParensNoFunction(exp);
        }
    }
    
    private IMathGroup parseWithFunction(String exp, String functionName) {
        String firstParenGroup = getFirstParenGroup(exp);
        IMathGroup functionParam = parse(firstParenGroup);
        Class<?> functionClass = functions.get(functionName);
        if (functionClass != null) {
            try {
                Object function = functionClass.getConstructor(IMathGroup.class).newInstance(functionParam);
                return parse(exp.replace(functionName + firstParenGroup, ((IMathFunction) function).eval().toPlainString()));
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }
//            System.out.println("Error with function parse");
        return null;
    }

    private int countCharInString(String s, char target) {
        int ret = 0;
        
        for (char c : s.toCharArray()) {
            if (c == target) {
                ret++;
            }
        }
        
        return ret;
    }

    private IMathGroup parseWithParensNoFunction(String exp) {        
        String subBase = getFirstParenGroup(exp);
        String subNoParens = subBase.substring(1, subBase.length() - 1);
        BigDecimal parseResult = parse(subNoParens).eval();
//        String quoteToReplace = cleanReplacementString(Matcher.quoteReplacement(subBase));
        exp = exp.replace(subBase, parseResult.toPlainString());
        return parse(exp);
    }
    
    private String getFirstParenGroup(String exp) {
        int firstGroupOpenIndex = -1;
        int firstGroupCloseIndex = -1;
        
        int openGroups = 0;
        boolean openFound = false;
        
        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);
            
            if (c == '(') {
                if (openGroups == 0) {
                    firstGroupOpenIndex = i;
                }
                
                openGroups++;
                openFound = true;
            } else if (c == ')') {
                openGroups--;
                if (openGroups == 0 && openFound) {
                    firstGroupCloseIndex = i;
                }
            }
            
            if (openGroups < 0) {
//                System.out.println("Too many close parens for num of openParens");
                return null;
            }
            
            if (openFound && openGroups == 0) {
                return exp.substring(firstGroupOpenIndex, firstGroupCloseIndex + 1);
            }
        }
//        System.out.println("Error with no-paren parse");
        return null;
    }

    private IMathGroup parseNoParens(String exp) {
        if (containsSpecialUnaryMinus(exp)) {
            System.out.println("Contains unary minus exp");
            return parseWithUnaryMinus(exp);
        }
        
        try {

            String[] topLevelComponents;

            int mode;

            if (exp.contains("+")) {
                topLevelComponents = exp.split("\\+");
                mode = 0;
            } else if (exp.contains("-")) {
                topLevelComponents = exp.split("-");
                mode = 1;
            } else if (exp.contains("*")) {
                topLevelComponents = exp.split("\\*");
                mode = 2;
            } else if (exp.contains("/")) {
                topLevelComponents = exp.split("/");
                mode = 3;
            } else if (exp.contains("^")) {
                topLevelComponents = exp.split("\\^");
//                System.out.println("exponents detected");
                mode = 4;
            } else {
                topLevelComponents = new String[] { exp };
                mode = -1;
            }
//            System.out.println("Mode: " + mode);
            
//            System.out.println("topLevelComponents: " + Arrays.toString(topLevelComponents));

            IMathGroup[] retParams = new IMathGroup[topLevelComponents.length];

            for (int i = 0; i < topLevelComponents.length; i++) {
                retParams[i] = parse(topLevelComponents[i]);
            }

            IMathGroup ret;

            if (mode == 0) {
                ret = new MathGroupAddition(retParams);
            } else if (mode == 1) {
                ret = new MathGroupSubtraction(retParams);
            } else if (mode == 2) {
                ret = new MathGroupMultiplication(retParams);
            } else if (mode == 3) {
                ret = new MathGroupDivision(retParams);
            } else if (mode == 4) {
                ret = new MathGroupExponentiation(retParams);
            } else {
                ret = new MathGroupBasic(retParams[0]);
            }

            System.out.println("ret = " + ret);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    final Map<String, IMathGroup> constants = new HashMap<String, IMathGroup>() {
        /**
         * 
         */
        private static final long serialVersionUID = 4416580880537999100L;

        {
            put("PI", new MathGroupBasic(BigDecimal.valueOf(Math.PI)));
        }
    };

    private String cleanReplacementString(String s) {
        return s.replace("*", "\\*").replace("+", "\\+").replace("(", "\\(").replace(")", "\\)");
    }

    private IMathGroup parseWithUnaryMinus(String exp) {
        Pattern patternExponent = Pattern.compile("-?\\d+(\\.\\d*)?\\^-\\d+(\\.\\d*)?");
        Matcher matcherExponent = patternExponent.matcher(exp);

        if (matcherExponent.find()) {
            System.out.println("Found exp");
            String sub = matcherExponent.group();
            String[] parts = sub.split("\\^");
            return parse(exp.replaceFirst(cleanReplacementString(sub), new MathGroupExponentiation(parseAll(parts)).eval().toPlainString()));
        }

        Pattern patternMultiplication = Pattern.compile("-?\\d+(\\.\\d*)?\\*-\\d+(\\.\\d*)?");
        Matcher matcherMultiplication = patternMultiplication.matcher(exp);

        if (matcherMultiplication.find()) {
            System.out.println("Found multip");
            String sub = matcherMultiplication.group();
            String[] parts = sub.split("\\*");
            return parse(exp.replaceFirst(cleanReplacementString(sub), new MathGroupMultiplication(parseAll(parts)).eval().toPlainString()));
        }

        Pattern patternDivision = Pattern.compile("-?\\d+(\\.\\d*)?/-\\d+(\\.\\d*)?");
        Matcher matcherDivision = patternDivision.matcher(exp);

        if (matcherDivision.find()) {
            System.out.println("Found div");
            String sub = matcherDivision.group();
            String[] parts = sub.split("/");
            return parse(exp.replaceFirst(cleanReplacementString(sub), new MathGroupDivision(parseAll(parts)).eval().toPlainString()));
        }
        System.out.println("Non standard unary minus exp: Returning 0");
        return new MathGroupBasic(BigDecimal.ZERO);
    }

    private boolean containsSpecialUnaryMinus(String exp) {
        return exp.contains("^-") || exp.contains("*-") || exp.contains("/-");
    }

    private IMathGroup parseBasicExp(String exp) {
        exp = exp.toUpperCase();
        if (constants.containsKey(exp)) {
            return constants.get(exp);
        } else
            return new MathGroupBasic(new BigDecimal(exp.equals("") ? "0" : exp));
    }

    private String cleanExp(String exp) {
        String ret = exp.trim().replaceAll("\\s+", "").replaceAll("\\++", "+").replace("--", "+").replaceAll("/+", "/").replaceAll("\\*+", "*").replace("+-", "-").replace("-+", "-").replace(")(", ")*(").replaceAll("/\\+", "/");
        return ret.equals(exp) ? ret : cleanExp(ret);
    }

    private IMathGroup[] parseAll(String[] exps) {
        IMathGroup[] ret = new IMathGroup[exps.length];
        for (int i = 0; i < exps.length; i++) {
            ret[i] = parse(exps[i]);
        }
        return ret;
    }

}
