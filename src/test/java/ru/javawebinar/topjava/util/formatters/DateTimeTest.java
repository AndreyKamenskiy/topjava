package ru.javawebinar.topjava.util.formatters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.provider.*;
import org.junit.jupiter.params.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

class DateTimeTest {

    @ParameterizedTest
    @CsvSource({
            "2024-01-01, , 2024-01-01",
            "2024-01-01, 15:01, 2024-01-01T15:01",
            ", 21:59, 21:59"
    })
    void testToString(String date, String time, String expected) {
        LocalDate localDate = date != null ? LocalDate.parse(date) : null;
        LocalTime localTime = time != null ? LocalTime.parse(time) : null;
        DateTime dateTime = new DateTime(localDate, localTime);
        assertEquals(expected, dateTime.toString());
    }

    @Test
    void testEmptyToString() {
        DateTime dateTime = new DateTime(null, null);
        assertEquals("", dateTime.toString());
    }

    @ParameterizedTest
    @CsvSource({
            "2024-01-01T15:01, 2024-01-01, 15:01",
            "15:01, , 15:01",
            "2024-01-01, 2024-01-01, "
    })
    void testOf(String text, LocalDate date, LocalTime time) {
        DateTime dateTime = DateTime.of(text);
        assertEquals(text, dateTime.toString());
        assertEquals(date, dateTime.date());
        assertEquals(time, dateTime.time());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2024-1-01T15:01", "2024-13-01", "2024-1-01T", "T15:01"})
    void parseError(String text) {
        assertThrows(DateTimeParseException.class, () -> DateTime.of(text));
    }

}