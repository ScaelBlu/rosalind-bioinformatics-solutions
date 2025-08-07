package io.github.ScaelBlu;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class MathUtilsTest {

    @ParameterizedTest
    @CsvSource({
            "0, 1",
            "3, 6",
            "4, 24",
            "10, 3628800"
    })
    void testFactorial(int n, BigInteger expected) {
        assertEquals(expected, MathUtils.factorial(n));
    }

    @Test
    void testFactorialWithBadArguments() {
        final IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> MathUtils.factorial(-5));
        assertEquals("Selection could not be less than zero.", iae.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, 1",
            "5, 3, 60",
            "10, 10, 3628800"
    })
    void testVariation(int n, int k, BigInteger expected) {
        assertEquals(expected, MathUtils.variation(n, k));
    }

    @Test
    void testVariationWithBadArguments() {
        final IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> MathUtils.variation(6, 7));
        assertEquals("Could not select more element (k) than the base set (n).", iae.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, 1",
            "5, 3, 10"
    })
    void testCombination(int n, int k, BigInteger expected) {
        assertEquals(expected, MathUtils.combination(n, k));
    }

    @Test
    void testCombinationWithBadArguments() {
        final IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> MathUtils.variation(6, 7));
        assertEquals("Could not select more element (k) than the base set (n).", iae.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, 1",
            "5, 3, 125",
            "2, 7, 128",
            "4, 4, 256",
    })
    void testRepeatedVariation(int n, int k, BigInteger expected) {
        assertEquals(expected, MathUtils.repeatedVariation(n, k));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, 1",
            "5, 3, 35",
            "3, 4, 15"
    })
    void testRepeatedCombination(int n, int k, BigInteger expected) {
        assertEquals(expected, MathUtils.repeatedCombination(n, k));
    }
}