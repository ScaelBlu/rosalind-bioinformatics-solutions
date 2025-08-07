package io.github.ScaelBlu;

import java.math.BigInteger;

public class MathUtils {

    /**
     * Calculates the permutations of a non-negative number.
     * @param n a non-negative integer.
     * @return the number os permutations.
     */
    public static BigInteger factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Selection could not be less than zero.");
        }
        BigInteger factorial = BigInteger.ONE;
        for (int i=n; i>1; i--) {
            factorial = factorial.multiply(BigInteger.valueOf(i));
        }
        return factorial;
    }

    /**
     * Calculates the number of ordered k-sized selections from a set of n elements. The k must be greater than n.
     * @param n the size of the set.
     * @param k the number of elements to select.
     * @return the number of possible ordered selections.
     */
    public static BigInteger variation(long n, long k) {
        if (k > n) {
            throw new IllegalArgumentException("Could not select more element (k) than the base set (n).");
        }
        if (k < 0) {
            throw new IllegalArgumentException("Selection could not be less than zero.");
        }
        BigInteger variations = BigInteger.ONE;
        for (int i=0; i<k; i++) {
            variations = variations.multiply(BigInteger.valueOf(n-i));
        }
        return variations;
    }

    /**
     * Calculates the number of the k-sized subsets of a base set with n elements. The k must be greater than n.
     * @param n the size of the base set.
     * @param k the size of the subsets.
     * @return the number of possible subsets
     */
    public static BigInteger combination(long n, long k) {
        if (k > n) {
            throw new IllegalArgumentException("Could not select more element (k) than the base set (n).");
        }
        if (k < 0) {
            throw new IllegalArgumentException("Selection could not be less than zero.");
        }
        BigInteger combinations = BigInteger.ONE;
        for (int i=1; i<=k; i++) {
            combinations = combinations.multiply(BigInteger.valueOf(n - i + 1)).divide(BigInteger.valueOf(i));
        }
        return combinations;
    }

    /**
     * Calculates the number of ordered, k-sized selections from a multiset of n elements that may repeat infinitely.
     * @param n the size of the set.
     * @param k the number of elements to select.
     * @return the number of the possible selections with repetitions.
     */
    public static BigInteger repeatedVariation(int n, int k) {
        return BigInteger.valueOf(n).pow(k);
    }

    /**
     * Calculates the number of possible unordered k-sized subsets from a multiset containing n infinitely repeated elements.
     * @param n the number of distinct elements.
     * @param k the number of elements to select.
     * @return the number of k-sized combinations with repetition.
     */
    public static BigInteger repeatedCombination(long n, long k) {
        if (n == 0 && k == 0) return BigInteger.ONE;
        return combination(n+k-1, k);
    }
}
