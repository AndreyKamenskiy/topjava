package ru.javawebinar.topjava.util;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(@NonNull LocalTime lt, @Nullable LocalTime startTime,
                                            @Nullable LocalTime endTime) {
        return (startTime == null || lt.compareTo(startTime) >= 0) &&
                (endTime == null || lt.compareTo(endTime) < 0);
    }

    public static boolean isBetweenClosed(@NonNull LocalDate ld, @Nullable LocalDate from, @Nullable LocalDate to) {
        return (from == null || ld.compareTo(from) >= 0) &&
                (to == null || ld.compareTo(to) <= 0);
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

