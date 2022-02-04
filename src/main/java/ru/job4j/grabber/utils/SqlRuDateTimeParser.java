package ru.job4j.grabber.utils;

import java.time.*;
import java.util.Map;
import static java.util.Map.entry;

public class SqlRuDateTimeParser implements DateTimeParser {

    public static final LocalDate TODAY = LocalDate.now();
    public static final LocalDate YESTERDAY = LocalDate.now().minus(Period.ofDays(1));
    private static final Map<String, Integer> MONTHS = Map.ofEntries(
            entry("янв", 1), entry("фев", 2), entry("мар", 3),
            entry("апр", 4), entry("май", 5), entry("июн", 6),
            entry("июл", 7), entry("авг", 8), entry("сен", 9),
            entry("окт", 10), entry("ноя", 11), entry("дек", 12));

    @Override
    public LocalDateTime parse(String parse) {
        String[] dateAndTime = parse.split(",");
        String[] time = dateAndTime[1].substring(1).split(":");
        LocalTime localTime = LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]));
        LocalDateTime result;
        if ("сегодня".equals(dateAndTime[0])) {
            result = LocalDateTime.of(TODAY, localTime);
        } else if ("вчера".equals(dateAndTime[0])) {
            result = LocalDateTime.of(YESTERDAY, localTime);
        } else {
            String[] date = dateAndTime[0].split(" ");
            int day = Integer.parseInt(date[0]);
            Month month = Month.of(MONTHS.get(date[1]));
            int year = 2000 + Integer.parseInt(date[2]);
            result = LocalDateTime.of(LocalDate.of(year, month, day), localTime);
        }
        return result;
    }
}
