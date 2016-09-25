package impl;

import api.IMathGroup;
import api.IMathHandler;
import impl.function.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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

        if (exp.contains("()")) {
            throw new api.MathExpressionParseException("Invalid Expression: contains \"()\"");
        }

        if (!(exp.contains("+") || exp.contains("-") || exp.contains("*") || exp.contains("/") || exp.contains("^") || exp.contains("(") || exp.contains(")"))) {
            return parseBasicExp(exp);
        }

        if (!exp.contains("(") && !exp.contains(")")) {
            return parseNoParens(exp);
        } else {
            if (countCharInString(exp, '(') != countCharInString(exp, ')')) {
                throw new api.MathExpressionParseException("Mismatched parens");
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
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }
        throw new api.MathExpressionParseException("Error with function parse");
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
        String parsedString = parseResult.toPlainString();
        if (parsedString.matches("-\\d+(\\.\\d+)?")) {
            parsedString = parsedString.replace('-', '$'); // Prevents jams
        }
        exp = exp.replace(subBase, parsedString);
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
                throw new api.MathExpressionParseException("Too many close parens for num of openParens");
            }

            if (openFound && openGroups == 0) {
                return exp.substring(firstGroupOpenIndex, firstGroupCloseIndex + 1);
            }
        }
        throw new api.MathExpressionParseException("Unknown Error in getFirstParenGroup");
    }

    private IMathGroup parseNoParens(String exp) {
        if (isSpecialExponent(exp)) {
            return parseSpecialExponent(exp);
        }

        try {

            javafx.util.Pair<String[], Integer> mPair = expToStringArr(exp);

            String[] topLevelComponents = mPair.getKey();

            int mode = mPair.getValue();

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

            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            throw new api.MathExpressionParseException(e);
        }
    }

    private javafx.util.Pair<String[], Integer> expToStringArr(String exp) {
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
            mode = 4;
        } else {
            topLevelComponents = new String[] { exp };
            mode = -1;
        }

        return new javafx.util.Pair<>(topLevelComponents, mode);
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

    private IMathGroup parseSpecialExponent(String exp) {
        String[] parts = exp.split("\\^");

        try {
            if (parts.length > 2) {
                String[] finalExps = new String[2];
                finalExps[0] = parts[0];
                finalExps[1] = parts[1];
                for (int i = 2; i < parts.length; i++) {
                    finalExps[1] = finalExps[1] + "^" + parts[i];
                }
                if (finalExps[0].startsWith("$")) {
                    return new MathGroupExponentiation(new IMathGroup[] { parse(finalExps[0]), parseSpecialExponent(finalExps[1]) });
                } else if (finalExps[0].startsWith("#")) {
                    return new MathGroupNegation(new MathGroupExponentiation(new api.IMathGroup[] { parse(finalExps[0].substring(1, finalExps[0].length())), parse(finalExps[1]) }));
                }
            }

            if (parts[0].startsWith("$")) {
                return new MathGroupExponentiation(new IMathGroup[] { new impl.MathGroupNegation(parse(parts[0].replace("$", ""))), parse(parts[1].replace("$", "-").replace("#", "-")) });
            } else if (parts[0].startsWith("#")) {
                return new MathGroupNegation(new MathGroupExponentiation(new IMathGroup[] { parse(parts[0].substring(1)), parse(parts[1]) }));
            } else {
                return new MathGroupExponentiation(parseAll(parts));
            }
        } catch (Exception e) {
            throw new api.MathExpressionParseException(e);
        }
    }

    private String join(Object[] arr) {
        String ret = "";
        for (Object o : arr) {
            ret += o.toString();
        }
        return ret;
    }

    /**
     *
     * @param arr The array to base conversions off of (non-destructive)
     * @param startIndex The index to start joining (inclusive)
     * @param endIndex The index to end joining (exclusive)
     * @return
     */
    private String[] joinArrayMembers(String[] arr, int startIndex, int endIndex) {
        String[] ret = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            if (i < startIndex) {
                ret[i] = arr[i];
            } else if (i < endIndex) /* startIndex <= i < endIndex*/ {
                ret[startIndex] = (ret[startIndex] == null ? "" : ret[startIndex]) + ret[i];
            } else /* i >= endIndex */ {
                ret[i - (endIndex - startIndex) + 1] = arr[i];
            }
        }
        return ret;
    }

    private boolean isSpecialExponent(String exp) {
        return exp.matches("[$#]?\\d+(\\.\\d*)?(\\^[$#]?\\d+(\\.\\d*)?)+");
    }

    private IMathGroup parseBasicExp(String exp) {
        exp = exp.toUpperCase();
        if (constants.containsKey(exp)) {
            return constants.get(exp);
        } else return new MathGroupBasic(new BigDecimal(exp.equals("") ? "0" : exp.replace("#", "-").replace("$", "-")));
    }

    private String cleanExp(String exp) {
        String ret = exp
                .trim()
                .replaceAll("\\s+", "") // Remove whitespace
                .replaceAll("\\++", "+") //Remove duplicate '+'s`
                .replace("--", "+") // -- -> +
                .replaceAll("/+", "/") // Removes duplicate division signs
                .replaceAll("\\*+", "*") // Removes duplicate multiplication signs
                .replace("+-", "-") // + a negative number is like minus that number
                .replace("-+", "-") // adding + to a number has no effect
                .replace(")(", ")*(") // Allows implicit multiplication
                .replaceAll("/\\+", "/") // adding + to a number has no effect
                .replace("*+", "*")
                .replace("^+", "^")
                .replace("*-", "*#") // Prevents jams
                .replace("/-", "/#") // Prevents jams
                .replace("^-", "^#") // Prevents jams
        ;
        return ret.equals(exp) ? ret : cleanExp(ret);
    }

    private IMathGroup[] parseAll(String[] exps) {
        return java.util.Arrays.stream(exps).map(exp -> parse(exp)).toArray(IMathGroup[]::new);
    }

}
