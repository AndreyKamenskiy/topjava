package ru.javawebinar.topjava.util.formatters;

import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public record DateTime(@Nullable LocalDate date, @Nullable LocalTime time) {

    private static final String DATE_TIME_DELIMITER = "T";
    private static final String TIME_DELIMITER = ":";

    public static DateTime of(String dateTime) throws DateTimeParseException {
        if (dateTime == null || dateTime.isEmpty()) return new DateTime(null, null);
        if (dateTime.contains(DATE_TIME_DELIMITER)) {
            LocalDateTime localDateTime = LocalDateTime.parse(dateTime);
            return new DateTime(localDateTime.toLocalDate(), localDateTime.toLocalTime());
        } else if (dateTime.contains(TIME_DELIMITER)) {
            LocalTime localTime = LocalTime.parse(dateTime);
            return new DateTime(null, localTime);
        }
        LocalDate localDate = LocalDate.parse(dateTime);
        return new DateTime(localDate, null);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        if (date != null && time != null) {
            s.append(date).append("T").append(time);
        } else if (date != null) {
            s.append(date);
        } else if (time != null) {
            s.append(time);
        }
        return s.toString();
    }

}
