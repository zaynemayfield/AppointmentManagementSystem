package model;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Handles dealing with dates and times and timezone
 */
public class Time {
    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static DateTimeFormatter getDate = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static DateTimeFormatter getMinutes = DateTimeFormatter.ofPattern("mm");
    private static DateTimeFormatter getHours = DateTimeFormatter.ofPattern("HH");
    public static Integer offSet  = ZonedDateTime.now().getOffset().getTotalSeconds()/60/60;
    public static Integer estOffSet = ZonedDateTime.now(ZoneId.of("America/New_York")).getOffset().getTotalSeconds()/60/60;

    /**
     * Takes the time from db and converts it to the user's timezone
     * @param dateTime
     * @return
     */
    public static String toLocal(String dateTime) {
        LocalDateTime temp = LocalDateTime.parse(dateTime,format);
        ZonedDateTime zoneTemp = ZonedDateTime.of(temp, ZoneId.of("America/New_York"));
        ZonedDateTime zoneUserDateTime = zoneTemp.withZoneSameInstant(ZoneId.systemDefault());
        String userDateTime = zoneUserDateTime.format(format);
        return userDateTime;
    }

    /**
     * converts time to Eastern Standard Time
     * @param dateTime
     * @return
     */
    public static String toEST(String dateTime) {
        LocalDateTime temp = LocalDateTime.parse(dateTime,format);
        ZonedDateTime zoneTemp = ZonedDateTime.of(temp, ZoneId.systemDefault());
        ZonedDateTime zoneUserDateTime = zoneTemp.withZoneSameInstant(ZoneId.of("America/New_York"));
        String userDateTime = zoneUserDateTime.format(format);
        return userDateTime;
    }

    /**
     * gets current time in Eastern Standard Time
     * @return
     */
    public static String currentEST() {
        ZonedDateTime zoneCurrentEST = ZonedDateTime.now(ZoneId.of("America/New_York"));
        String currentEST = zoneCurrentEST.format(format);
        return currentEST;
    }

    /**
     * returns Eastern standard time in zonedDateTime form
     * @param dateTime
     * @return
     */
    public static ZonedDateTime toESTZoned(String dateTime) {
        LocalDateTime temp = LocalDateTime.parse(dateTime,format);
        ZonedDateTime zoneTemp = ZonedDateTime.of(temp, ZoneId.systemDefault());
        ZonedDateTime zoneUserDateTime = zoneTemp.withZoneSameInstant(ZoneId.of("America/New_York"));
        return zoneUserDateTime;
    }

    /**
     * gets current eastern standard time in zonedDateTime
     * @return
     */
    public static ZonedDateTime currentESTZoned() {
        ZonedDateTime zoneCurrentEST = ZonedDateTime.now(ZoneId.of("America/New_York"));
        return zoneCurrentEST;
    }

    /**
     * gets minutes from date
     * @param start
     * @return
     */
    public static String getMinute(String start) {
        LocalDateTime temp = LocalDateTime.parse(start,format);
        ZonedDateTime zoneTemp = ZonedDateTime.of(temp, ZoneId.systemDefault());
        return zoneTemp.format(getMinutes);

    }

    /**
     * gets hours from date
     * @param start
     * @return
     */
    public static String getHour(String start) {
        LocalDateTime temp = LocalDateTime.parse(start,format);
        ZonedDateTime zoneTemp = ZonedDateTime.of(temp, ZoneId.systemDefault());
        String startDate = zoneTemp.format(getHours);
        if (Integer.parseInt(startDate) <10) {
            startDate = startDate.substring(1);
            return startDate;
        } else {
            return startDate;
        }
    }

    /**
     * gets duration from time difference between start and end
     * @param start
     * @param end
     * @return
     */
    public static long getDuration(String start, String end) {
        LocalDateTime s = LocalDateTime.parse(start,format);
        LocalDateTime e = LocalDateTime.parse(end,format);
        Duration duration = Duration.between(s, e);
        long d = Math.abs(duration.toMinutes());
        return d;
    }

    /**
     * gets date for datepicker
     * @param start
     * @return
     */
    public static LocalDate getDate(String start) {
        LocalDateTime temp = LocalDateTime.parse(start,format);
        ZonedDateTime zoneTemp = ZonedDateTime.of(temp, ZoneId.systemDefault());
        String dateTemp = zoneTemp.format(getDate);
        LocalDate startDate = LocalDate.parse(dateTemp, getDate);
        return startDate;
    }

    /**
     *
     * @param closing converts closing time to compare with selected date to see if it is outside of business hours
     * @return
     */
    public static ZonedDateTime closingDate(String closing) {
        LocalDateTime temp = LocalDateTime.parse(closing,format);
        ZonedDateTime closingDate = ZonedDateTime.of(temp, ZoneId.of("America/New_York"));
        return closingDate;
    }

    /**
     * Gets the offset value from Buisness EST time and client Local time
     * @return
     */
    public static int getOffSet() {
        return (Time.estOffSet-Time.offSet)*-1;
    }
}
