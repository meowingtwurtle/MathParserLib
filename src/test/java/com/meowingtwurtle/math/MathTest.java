package com.meowingtwurtle.math;

import com.meowingtwurtle.math.api.IMathHandler;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.math.BigDecimal;

/**
 * Unit test for simple App.
 */
public class MathTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MathTest(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( MathTest.class );
    }

    public void testApp() {
        assertEquals(BigDecimal.valueOf(0.0), IMathHandler.getMathHandler().parse("sin(0)").eval());
        assertEquals(BigDecimal.valueOf(0.0), IMathHandler.getMathHandler().parse("sin(1+sin(0)-1)").eval());
        assertEquals(BigDecimal.valueOf(0.0), IMathHandler.getMathHandler().parse("(1 + sin(1+sin(0)-1) - 1)").eval());
        assertEquals(BigDecimal.valueOf(0.0), IMathHandler.getMathHandler().parse("2*(1 + sin(1+sin(0)-1) - 1)").eval());
    }
}
