package ru.javawebinar.topjava.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javawebinar.topjava.util.NumberUtil.sumWithoutOverflow;

class NumberUtilTest {

    @Test
    void sumWithoutOverflowTest() {
        assertEquals(Integer.MAX_VALUE, sumWithoutOverflow(Integer.MAX_VALUE, 1), "positive overflow test");
        assertEquals(sumWithoutOverflow(Integer.MIN_VALUE, 1), Integer.MIN_VALUE + 1, "sum test");
        assertEquals(sumWithoutOverflow(Integer.MIN_VALUE, -1), Integer.MIN_VALUE, "negative overflow test");
    }

}