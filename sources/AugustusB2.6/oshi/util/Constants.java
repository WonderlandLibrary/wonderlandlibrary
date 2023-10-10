// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.Instant;
import java.util.regex.Pattern;
import java.time.OffsetDateTime;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Constants
{
    public static final String UNKNOWN = "unknown";
    public static final String SYSFS_SERIAL_PATH = "/sys/devices/virtual/dmi/id/";
    public static final OffsetDateTime UNIX_EPOCH;
    public static final Pattern DIGITS;
    
    private Constants() {
        throw new AssertionError();
    }
    
    static {
        UNIX_EPOCH = OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
        DIGITS = Pattern.compile("\\d+");
    }
}
