package ru.javawebinar.topjava.util.formatters;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class DateTimeCustomFormatter implements Formatter<DateTime> {

    @Override
    public DateTime parse(String text, Locale locale) throws ParseException {
        return DateTime.of(text);
    }

    @Override
    public String print(DateTime dateTime, Locale locale) {
        return dateTime.toString();
    }

}
