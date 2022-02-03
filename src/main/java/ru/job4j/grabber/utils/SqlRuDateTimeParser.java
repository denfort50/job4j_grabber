package ru.job4j.grabber.utils;

import java.time.*;
import java.util.Map;
import static java.util.Map.entry;

public class SqlRuDateTimeParser implements DateTimeParser {

    private static final Map<String, Integer> MONTHS = Map.ofEntries(
            entry("янв", 1), entry("фев", 2), entry("мар", 3),
            entry("апр", 4), entry("май", 5), entry("июн", 6),
            entry("июл", 7), entry("авг", 8), entry("сен", 9),
            entry("окт", 10), entry("ноя", 11), entry("дек", 12));

    @Override
    public LocalDateTime parse(String parse) {
        String[] dateAndTime = parse.split(",");
        String[] time = dateAndTime[1].substring(1).split(":");
        int hours = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);
        LocalTime localTime = LocalTime.of(hours, minutes);
        boolean today = "сегодня".equals(dateAndTime[0]);
        boolean yesterday = "вчера".equals(dateAndTime[0]);
        LocalDate localDate;
        if (today) {
            localDate = LocalDate.now();
        } else if (yesterday) {
            localDate = LocalDate.now().minus(Period.ofDays(1));
        } else {
            String[] date = dateAndTime[0].split(" ");
            int day = Integer.parseInt(date[0]);
            Month month = Month.of(MONTHS.get(date[1]));
            int year = 2000 + Integer.parseInt(date[2]);
            localDate = LocalDate.of(year, month, day);
        }
        return LocalDateTime.of(localDate, localTime);
    }
}
