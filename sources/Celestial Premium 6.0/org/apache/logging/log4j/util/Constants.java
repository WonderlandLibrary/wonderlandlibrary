/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.util;

import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class Constants {
    public static final boolean IS_WEB_APP = PropertiesUtil.getProperties().getBooleanProperty("log4j2.is.webapp", Constants.isClassAvailable("javax.servlet.Servlet"));
    public static final boolean ENABLE_THREADLOCALS = !IS_WEB_APP && PropertiesUtil.getProperties().getBooleanProperty("log4j2.enable.threadlocals", true);
    public static final int JAVA_MAJOR_VERSION = Constants.getMajorVersion();
    public static final int MAX_REUSABLE_MESSAGE_SIZE = Constants.size("log4j.maxReusableMsgSize", 518);
    public static final String LOG4J2_DEBUG = "log4j2.debug";

    private static int size(String property, int defaultValue) {
        return PropertiesUtil.getProperties().getIntegerProperty(property, defaultValue);
    }

    private static boolean isClassAvailable(String className) {
        try {
            return LoaderUtil.loadClass(className) != null;
        }
        catch (Throwable e) {
            return false;
        }
    }

    private Constants() {
    }

    private static int getMajorVersion() {
        return Constants.getMajorVersion(System.getProperty("java.version"));
    }

    static int getMajorVersion(String version) {
        String[] parts = version.split("-|\\.");
        try {
            boolean isJEP223;
            int token = Integer.parseInt(parts[0]);
            boolean bl = isJEP223 = token != 1;
            if (isJEP223) {
                return token;
            }
            return Integer.parseInt(parts[1]);
        }
        catch (Exception ex) {
            return 0;
        }
    }
}

