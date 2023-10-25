/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public final class SystemPropertyUtil {
    private static final Pattern INTEGER_PATTERN;
    private static final InternalLogger logger;
    private static boolean loggedException;
    private static boolean initializedLogger;

    public static boolean contains(String string) {
        return SystemPropertyUtil.get(string) != null;
    }

    public static int getInt(String string, int n) {
        String string2 = SystemPropertyUtil.get(string);
        if (string2 == null) {
            return n;
        }
        if (INTEGER_PATTERN.matcher(string2 = string2.trim().toLowerCase()).matches()) {
            try {
                return Integer.parseInt(string2);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        SystemPropertyUtil.log("Unable to parse the integer system property '" + string + "':" + string2 + " - " + "using the default value: " + n);
        return n;
    }

    public static String get(String string) {
        return SystemPropertyUtil.get(string, null);
    }

    private static void log(String string) {
        if (initializedLogger) {
            logger.warn(string);
        } else {
            Logger.getLogger(SystemPropertyUtil.class.getName()).log(Level.WARNING, string);
        }
    }

    public static boolean getBoolean(String string, boolean bl) {
        String string2 = SystemPropertyUtil.get(string);
        if (string2 == null) {
            return bl;
        }
        if ((string2 = string2.trim().toLowerCase()).isEmpty()) {
            return true;
        }
        if ("true".equals(string2) || "yes".equals(string2) || "1".equals(string2)) {
            return true;
        }
        if ("false".equals(string2) || "no".equals(string2) || "0".equals(string2)) {
            return false;
        }
        SystemPropertyUtil.log("Unable to parse the boolean system property '" + string + "':" + string2 + " - " + "using the default value: " + bl);
        return bl;
    }

    public static String get(final String string, String string2) {
        String string3;
        block5: {
            if (string == null) {
                throw new NullPointerException("key");
            }
            if (string.isEmpty()) {
                throw new IllegalArgumentException("key must not be empty.");
            }
            string3 = null;
            try {
                string3 = System.getSecurityManager() == null ? System.getProperty(string) : AccessController.doPrivileged(new PrivilegedAction<String>(){

                    @Override
                    public String run() {
                        return System.getProperty(string);
                    }
                });
            }
            catch (Exception exception) {
                if (loggedException) break block5;
                SystemPropertyUtil.log("Unable to retrieve a system property '" + string + "'; default values will be used.", exception);
                loggedException = true;
            }
        }
        if (string3 == null) {
            return string2;
        }
        return string3;
    }

    private static void log(String string, Exception exception) {
        if (initializedLogger) {
            logger.warn(string, exception);
        } else {
            Logger.getLogger(SystemPropertyUtil.class.getName()).log(Level.WARNING, string, exception);
        }
    }

    static {
        initializedLogger = false;
        logger = InternalLoggerFactory.getInstance(SystemPropertyUtil.class);
        initializedLogger = true;
        INTEGER_PATTERN = Pattern.compile("-?[0-9]+");
    }

    public static long getLong(String string, long l) {
        String string2 = SystemPropertyUtil.get(string);
        if (string2 == null) {
            return l;
        }
        if (INTEGER_PATTERN.matcher(string2 = string2.trim().toLowerCase()).matches()) {
            try {
                return Long.parseLong(string2);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        SystemPropertyUtil.log("Unable to parse the long integer system property '" + string + "':" + string2 + " - " + "using the default value: " + l);
        return l;
    }

    private SystemPropertyUtil() {
    }
}

