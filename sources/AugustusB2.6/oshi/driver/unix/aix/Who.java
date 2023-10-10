// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix.aix;

import java.util.regex.Matcher;
import java.time.format.DateTimeParseException;
import java.time.ZoneId;
import java.time.LocalDateTime;
import oshi.util.ExecutingCommand;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Who
{
    private static final Pattern BOOT_FORMAT_AIX;
    private static final DateTimeFormatter BOOT_DATE_FORMAT_AIX;
    
    private Who() {
    }
    
    public static long queryBootTime() {
        final String s = ExecutingCommand.getFirstAnswer("/usr/bin/who -b");
        final Matcher m = Who.BOOT_FORMAT_AIX.matcher(s);
        if (m.matches()) {
            try {
                return LocalDateTime.parse(m.group(1) + " " + m.group(2), Who.BOOT_DATE_FORMAT_AIX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }
            catch (DateTimeParseException ex) {}
            catch (NullPointerException ex2) {}
        }
        return 0L;
    }
    
    static {
        BOOT_FORMAT_AIX = Pattern.compile("\\D+(\\d{4}-\\d{2}-\\d{2})\\s+(\\d{2}:\\d{2}).*");
        BOOT_DATE_FORMAT_AIX = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }
}
