/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.logging.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;

public class SimpleLog
implements Log,
Serializable {
    private static final long serialVersionUID = 136942970684951178L;
    protected static final String systemPrefix = "org.apache.commons.logging.simplelog.";
    protected static final Properties simpleLogProps = new Properties();
    protected static final String DEFAULT_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss:SSS zzz";
    protected static volatile boolean showLogName = false;
    protected static volatile boolean showShortName = true;
    protected static volatile boolean showDateTime = false;
    protected static volatile String dateTimeFormat = "yyyy/MM/dd HH:mm:ss:SSS zzz";
    protected static DateFormat dateFormatter = null;
    public static final int LOG_LEVEL_TRACE = 1;
    public static final int LOG_LEVEL_DEBUG = 2;
    public static final int LOG_LEVEL_INFO = 3;
    public static final int LOG_LEVEL_WARN = 4;
    public static final int LOG_LEVEL_ERROR = 5;
    public static final int LOG_LEVEL_FATAL = 6;
    public static final int LOG_LEVEL_ALL = 0;
    public static final int LOG_LEVEL_OFF = 7;
    protected volatile String logName = null;
    protected volatile int currentLogLevel;
    private volatile String shortLogName = null;
    static /* synthetic */ Class class$java$lang$Thread;
    static /* synthetic */ Class class$org$apache$commons$logging$impl$SimpleLog;

    private static String getStringProperty(String name) {
        String prop = null;
        try {
            prop = System.getProperty(name);
        }
        catch (SecurityException securityException) {
            // empty catch block
        }
        return prop == null ? simpleLogProps.getProperty(name) : prop;
    }

    private static String getStringProperty(String name, String dephault) {
        String prop = SimpleLog.getStringProperty(name);
        return prop == null ? dephault : prop;
    }

    private static boolean getBooleanProperty(String name, boolean dephault) {
        String prop = SimpleLog.getStringProperty(name);
        return prop == null ? dephault : "true".equalsIgnoreCase(prop);
    }

    public SimpleLog(String name) {
        this.logName = name;
        this.setLevel(3);
        String lvl = SimpleLog.getStringProperty("org.apache.commons.logging.simplelog.log." + this.logName);
        int i = String.valueOf(name).lastIndexOf(".");
        while (null == lvl && i > -1) {
            name = name.substring(0, i);
            lvl = SimpleLog.getStringProperty("org.apache.commons.logging.simplelog.log." + name);
            i = String.valueOf(name).lastIndexOf(".");
        }
        if (null == lvl) {
            lvl = SimpleLog.getStringProperty("org.apache.commons.logging.simplelog.defaultlog");
        }
        if ("all".equalsIgnoreCase(lvl)) {
            this.setLevel(0);
        } else if ("trace".equalsIgnoreCase(lvl)) {
            this.setLevel(1);
        } else if ("debug".equalsIgnoreCase(lvl)) {
            this.setLevel(2);
        } else if ("info".equalsIgnoreCase(lvl)) {
            this.setLevel(3);
        } else if ("warn".equalsIgnoreCase(lvl)) {
            this.setLevel(4);
        } else if ("error".equalsIgnoreCase(lvl)) {
            this.setLevel(5);
        } else if ("fatal".equalsIgnoreCase(lvl)) {
            this.setLevel(6);
        } else if ("off".equalsIgnoreCase(lvl)) {
            this.setLevel(7);
        }
    }

    public void setLevel(int currentLogLevel) {
        this.currentLogLevel = currentLogLevel;
    }

    public int getLevel() {
        return this.currentLogLevel;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void log(int type, Object message, Throwable t) {
        StringBuffer buf = new StringBuffer();
        if (showDateTime) {
            String dateText;
            Date now = new Date();
            DateFormat dateFormat = dateFormatter;
            synchronized (dateFormat) {
                dateText = dateFormatter.format(now);
            }
            buf.append(dateText);
            buf.append(" ");
        }
        switch (type) {
            case 1: {
                buf.append("[TRACE] ");
                break;
            }
            case 2: {
                buf.append("[DEBUG] ");
                break;
            }
            case 3: {
                buf.append("[INFO] ");
                break;
            }
            case 4: {
                buf.append("[WARN] ");
                break;
            }
            case 5: {
                buf.append("[ERROR] ");
                break;
            }
            case 6: {
                buf.append("[FATAL] ");
            }
        }
        if (showShortName) {
            if (this.shortLogName == null) {
                String slName = this.logName.substring(this.logName.lastIndexOf(".") + 1);
                this.shortLogName = slName.substring(slName.lastIndexOf("/") + 1);
            }
            buf.append(String.valueOf(this.shortLogName)).append(" - ");
        } else if (showLogName) {
            buf.append(String.valueOf(this.logName)).append(" - ");
        }
        buf.append(String.valueOf(message));
        if (t != null) {
            buf.append(" <");
            buf.append(t.toString());
            buf.append(">");
            StringWriter sw = new StringWriter(1024);
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            buf.append(sw.toString());
        }
        this.write(buf);
    }

    protected void write(StringBuffer buffer) {
        System.err.println(buffer.toString());
    }

    protected boolean isLevelEnabled(int logLevel) {
        return logLevel >= this.currentLogLevel;
    }

    public final void debug(Object message) {
        if (this.isLevelEnabled(2)) {
            this.log(2, message, null);
        }
    }

    public final void debug(Object message, Throwable t) {
        if (this.isLevelEnabled(2)) {
            this.log(2, message, t);
        }
    }

    public final void trace(Object message) {
        if (this.isLevelEnabled(1)) {
            this.log(1, message, null);
        }
    }

    public final void trace(Object message, Throwable t) {
        if (this.isLevelEnabled(1)) {
            this.log(1, message, t);
        }
    }

    public final void info(Object message) {
        if (this.isLevelEnabled(3)) {
            this.log(3, message, null);
        }
    }

    public final void info(Object message, Throwable t) {
        if (this.isLevelEnabled(3)) {
            this.log(3, message, t);
        }
    }

    public final void warn(Object message) {
        if (this.isLevelEnabled(4)) {
            this.log(4, message, null);
        }
    }

    public final void warn(Object message, Throwable t) {
        if (this.isLevelEnabled(4)) {
            this.log(4, message, t);
        }
    }

    public final void error(Object message) {
        if (this.isLevelEnabled(5)) {
            this.log(5, message, null);
        }
    }

    public final void error(Object message, Throwable t) {
        if (this.isLevelEnabled(5)) {
            this.log(5, message, t);
        }
    }

    public final void fatal(Object message) {
        if (this.isLevelEnabled(6)) {
            this.log(6, message, null);
        }
    }

    public final void fatal(Object message, Throwable t) {
        if (this.isLevelEnabled(6)) {
            this.log(6, message, t);
        }
    }

    public final boolean isDebugEnabled() {
        return this.isLevelEnabled(2);
    }

    public final boolean isErrorEnabled() {
        return this.isLevelEnabled(5);
    }

    public final boolean isFatalEnabled() {
        return this.isLevelEnabled(6);
    }

    public final boolean isInfoEnabled() {
        return this.isLevelEnabled(3);
    }

    public final boolean isTraceEnabled() {
        return this.isLevelEnabled(1);
    }

    public final boolean isWarnEnabled() {
        return this.isLevelEnabled(4);
    }

    private static ClassLoader getContextClassLoader() {
        ClassLoader classLoader = null;
        try {
            Method method = (class$java$lang$Thread == null ? (class$java$lang$Thread = SimpleLog.class$("java.lang.Thread")) : class$java$lang$Thread).getMethod("getContextClassLoader", null);
            try {
                classLoader = (ClassLoader)method.invoke(Thread.currentThread(), null);
            }
            catch (IllegalAccessException e) {
            }
            catch (InvocationTargetException e) {
                if (!(e.getTargetException() instanceof SecurityException)) {
                    throw new LogConfigurationException("Unexpected InvocationTargetException", e.getTargetException());
                }
            }
        }
        catch (NoSuchMethodException noSuchMethodException) {
            // empty catch block
        }
        if (classLoader == null) {
            classLoader = (class$org$apache$commons$logging$impl$SimpleLog == null ? (class$org$apache$commons$logging$impl$SimpleLog = SimpleLog.class$("org.apache.commons.logging.impl.SimpleLog")) : class$org$apache$commons$logging$impl$SimpleLog).getClassLoader();
        }
        return classLoader;
    }

    private static InputStream getResourceAsStream(final String name) {
        return (InputStream)AccessController.doPrivileged(new PrivilegedAction(){

            public Object run() {
                ClassLoader threadCL = SimpleLog.getContextClassLoader();
                if (threadCL != null) {
                    return threadCL.getResourceAsStream(name);
                }
                return ClassLoader.getSystemResourceAsStream(name);
            }
        });
    }

    static /* synthetic */ Class class$(String x0) {
        try {
            return Class.forName(x0);
        }
        catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        InputStream in = SimpleLog.getResourceAsStream("simplelog.properties");
        if (null != in) {
            try {
                simpleLogProps.load(in);
                in.close();
            }
            catch (IOException e) {
                // empty catch block
            }
        }
        showLogName = SimpleLog.getBooleanProperty("org.apache.commons.logging.simplelog.showlogname", showLogName);
        showShortName = SimpleLog.getBooleanProperty("org.apache.commons.logging.simplelog.showShortLogname", showShortName);
        if (showDateTime = SimpleLog.getBooleanProperty("org.apache.commons.logging.simplelog.showdatetime", showDateTime)) {
            dateTimeFormat = SimpleLog.getStringProperty("org.apache.commons.logging.simplelog.dateTimeFormat", dateTimeFormat);
            try {
                dateFormatter = new SimpleDateFormat(dateTimeFormat);
            }
            catch (IllegalArgumentException e) {
                dateTimeFormat = DEFAULT_DATE_TIME_FORMAT;
                dateFormatter = new SimpleDateFormat(dateTimeFormat);
            }
        }
    }
}

