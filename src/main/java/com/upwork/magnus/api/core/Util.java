package com.upwork.magnus.api.core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
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

    public static OffsetDateTime sqlTimestampToOffsetDateTime(Timestamp ts, String timeZone) {
        if (ts == null) {
            return null;
        }

        ZoneId zoneId = ZoneId.of(timeZone);

        Calendar cal = Calendar.getInstance();
        cal.setTime(ts);
        ZonedDateTime zdt = ZonedDateTime.of(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND),
                cal.get(Calendar.MILLISECOND) * 1000000,
                zoneId);
        return zdt.toOffsetDateTime();
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
