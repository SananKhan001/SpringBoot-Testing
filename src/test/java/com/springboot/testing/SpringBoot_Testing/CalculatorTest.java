package com.springboot.testing.SpringBoot_Testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {
    private Calculator calculator;

//    @BeforeEach // Befor each test case
//    public void setup(){
//        calculator = new Calculator();
//    }

    @Test
    public void testMultiply(){
        calculator = new Calculator();
        assertEquals(20, calculator.multiply(5, 4));
        assertEquals(25, calculator.multiply(5, 5));
    }

//    @Test
//    public void testMultiplyDiffParm(){
//        calculator = new Calculator();
//        assertEquals(25, calculator.multiply(5, 5));
//    }

    @Test
    public void testDivide(){
        calculator = new Calculator();
        assertEquals(5, 20, 0);
    }
}
