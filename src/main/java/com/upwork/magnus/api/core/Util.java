package com.upwork.magnus.api.core;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by ali on 2016-04-25.
 */
public class Util {
    public static String javaSqlTimeStampToString(Timestamp date) {
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" );
        TimeZone tz = TimeZone.getTimeZone( "UTC" );
        df.setTimeZone( tz );
        String output = df.format( date );
        int inset0 = 9;
        int inset1 = 6;
        String s0 = output.substring( 0, output.length() - inset0 );
        String s1 = output.substring( output.length() - inset1, output.length() );
        String result = s0 + s1;
        result = result.replaceAll( "UTC", "+00:00" );
        return result;
    }
}
