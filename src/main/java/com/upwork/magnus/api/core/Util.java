package com.upwork.magnus.api.core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

/**
 * Created by ali on 2016-04-25.
 */
public class Util {

    public String readJson(String path) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(new FileInputStream(path)));

        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    public static OffsetDateTime sqlTimestampToOffsetDateTime(Date date, Time time, String timeZone) {
        return LocalDateTime.of(date.toLocalDate(), time.toLocalTime())
                .atZone(ZoneId.of(timeZone))
                .toOffsetDateTime();
    }

    public static Timestamp offsetDateTimeToSql(OffsetDateTime datetime) {
        if (datetime == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.set(datetime.getYear(), datetime.getMonth().ordinal(), datetime.getDayOfMonth(),
                datetime.getHour(), datetime.getMinute(), datetime.getSecond());
        cal.set(Calendar.MILLISECOND, datetime.getNano() / 1000000);
        return new Timestamp(cal.getTimeInMillis());
    }

}
