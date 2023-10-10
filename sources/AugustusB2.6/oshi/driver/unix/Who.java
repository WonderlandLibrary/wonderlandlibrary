// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix;

import java.util.Locale;
import java.time.temporal.TemporalField;
import java.time.Year;
import java.time.temporal.ChronoField;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ChronoUnit;
import java.time.chrono.ChronoLocalDateTime;
import java.util.regex.Matcher;
import java.time.format.DateTimeParseException;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.util.Iterator;
import oshi.util.ExecutingCommand;
import java.util.ArrayList;
import oshi.software.os.OSSession;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Who
{
    private static final Pattern WHO_FORMAT_LINUX;
    private static final DateTimeFormatter WHO_DATE_FORMAT_LINUX;
    private static final Pattern WHO_FORMAT_UNIX;
    private static final DateTimeFormatter WHO_DATE_FORMAT_UNIX;
    
    private Who() {
    }
    
    public static synchronized List<OSSession> queryWho() {
        final List<OSSession> whoList = new ArrayList<OSSession>();
        boolean useUnix = false;
        for (final String s : ExecutingCommand.runNative("who")) {
            if (useUnix || !matchLinux(whoList, s)) {
                useUnix = matchUnix(whoList, s);
            }
        }
        return whoList;
    }
    
    private static boolean matchLinux(final List<OSSession> whoList, final String s) {
        final Matcher m = Who.WHO_FORMAT_LINUX.matcher(s);
        if (m.matches()) {
            try {
                whoList.add(new OSSession(m.group(1), m.group(2), LocalDateTime.parse(m.group(3) + " " + m.group(4), Who.WHO_DATE_FORMAT_LINUX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), (m.group(5) == null) ? "unknown" : m.group(5)));
                return true;
            }
            catch (DateTimeParseException ex) {}
            catch (NullPointerException ex2) {}
        }
        return false;
    }
    
    private static boolean matchUnix(final List<OSSession> whoList, final String s) {
        final Matcher m = Who.WHO_FORMAT_UNIX.matcher(s);
        if (m.matches()) {
            try {
                LocalDateTime login = LocalDateTime.parse(m.group(3) + " " + m.group(4) + " " + m.group(5), Who.WHO_DATE_FORMAT_UNIX);
                if (login.isAfter(LocalDateTime.now())) {
                    login = login.minus(1L, (TemporalUnit)ChronoUnit.YEARS);
                }
                final long millis = login.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                whoList.add(new OSSession(m.group(1), m.group(2), millis, (m.group(6) == null) ? "" : m.group(6)));
                return true;
            }
            catch (DateTimeParseException ex) {}
            catch (NullPointerException ex2) {}
        }
        return false;
    }
    
    static {
        WHO_FORMAT_LINUX = Pattern.compile("(\\S+)\\s+(\\S+)\\s+(\\d{4}-\\d{2}-\\d{2})\\s+(\\d{2}:\\d{2})\\s*(?:\\((.+)\\))?");
        WHO_DATE_FORMAT_LINUX = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        WHO_FORMAT_UNIX = Pattern.compile("(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\d+)\\s+(\\d{2}:\\d{2})\\s*(?:\\((.+)\\))?");
        WHO_DATE_FORMAT_UNIX = new DateTimeFormatterBuilder().appendPattern("MMM d HH:mm").parseDefaulting(ChronoField.YEAR, Year.now().getValue()).toFormatter(Locale.US);
    }
}
