package ru.javawebinar.topjava.util;

public class NumberUtil {

    public static int sumWithoutOverflow(int a, int b) {
        long sum = (long) a + b;
        if (sum > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (sum < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int) sum;
    }

}
