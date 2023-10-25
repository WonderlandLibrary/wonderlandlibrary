/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Vector;

public class PerformanceLogger {
    private static final int START_INDEX = 0;
    private static final int LAST_RESERVED = 0;
    private static boolean perfLoggingOn = false;
    private static boolean useNanoTime = false;
    private static Vector<TimeData> times;
    private static String logFileName;
    private static Writer logWriter;
    private static long baseTime;

    public static boolean loggingEnabled() {
        return perfLoggingOn;
    }

    private static long getCurrentTime() {
        if (useNanoTime) {
            return System.nanoTime();
        }
        return System.currentTimeMillis();
    }

    public static void setStartTime(String string) {
        if (PerformanceLogger.loggingEnabled()) {
            long l = PerformanceLogger.getCurrentTime();
            PerformanceLogger.setStartTime(string, l);
        }
    }

    public static void setBaseTime(long l) {
        if (PerformanceLogger.loggingEnabled()) {
            baseTime = l;
        }
    }

    public static void setStartTime(String string, long l) {
        if (PerformanceLogger.loggingEnabled()) {
            times.set(0, new TimeData(string, l));
        }
    }

    public static long getStartTime() {
        if (PerformanceLogger.loggingEnabled()) {
            return times.get(0).getTime();
        }
        return 0L;
    }

    public static int setTime(String string) {
        if (PerformanceLogger.loggingEnabled()) {
            long l = PerformanceLogger.getCurrentTime();
            return PerformanceLogger.setTime(string, l);
        }
        return 0;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int setTime(String string, long l) {
        if (PerformanceLogger.loggingEnabled()) {
            Vector<TimeData> vector = times;
            synchronized (vector) {
                times.add(new TimeData(string, l));
                return times.size() - 1;
            }
        }
        return 0;
    }

    public static long getTimeAtIndex(int n) {
        if (PerformanceLogger.loggingEnabled()) {
            return times.get(n).getTime();
        }
        return 0L;
    }

    public static String getMessageAtIndex(int n) {
        if (PerformanceLogger.loggingEnabled()) {
            return times.get(n).getMessage();
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void outputLog(Writer writer) {
        if (PerformanceLogger.loggingEnabled()) {
            try {
                Vector<TimeData> vector = times;
                synchronized (vector) {
                    for (int i = 0; i < times.size(); ++i) {
                        TimeData timeData = times.get(i);
                        if (timeData == null) continue;
                        writer.write(i + " " + timeData.getMessage() + ": " + (timeData.getTime() - baseTime) + "\n");
                    }
                }
                writer.flush();
            }
            catch (Exception exception) {
                System.out.println(exception + ": Writing performance log to " + writer);
            }
        }
    }

    public static void outputLog() {
        PerformanceLogger.outputLog(logWriter);
    }

    static {
        logFileName = null;
        logWriter = null;
        String string = AccessController.doPrivileged(new PrivilegedAction<String>(){

            @Override
            public String run() {
                return System.getProperty("sun.perflog");
            }
        });
        if (string != null) {
            perfLoggingOn = true;
            String string2 = AccessController.doPrivileged(new PrivilegedAction<String>(){

                @Override
                public String run() {
                    return System.getProperty("sun.perflog.nano");
                }
            });
            if (string2 != null) {
                useNanoTime = true;
            }
            if (string.regionMatches(true, 0, "file:", 0, 5)) {
                logFileName = string.substring(5);
            }
            if (logFileName != null && logWriter == null) {
                Void void_ = AccessController.doPrivileged(new PrivilegedAction<Void>(){

                    @Override
                    public Void run() {
                        try {
                            File file = new File(logFileName);
                            file.createNewFile();
                            logWriter = new FileWriter(file);
                        }
                        catch (Exception exception) {
                            System.out.println(exception + ": Creating logfile " + logFileName + ".  Log to console");
                        }
                        return null;
                    }
                });
            }
            if (logWriter == null) {
                logWriter = new OutputStreamWriter(System.out);
            }
        }
        times = new Vector(10);
        for (int i = 0; i <= 0; ++i) {
            times.add(new TimeData("Time " + i + " not set", 0L));
        }
    }

    static class TimeData {
        String message;
        long time;

        TimeData(String string, long l) {
            this.message = string;
            this.time = l;
        }

        String getMessage() {
            return this.message;
        }

        long getTime() {
            return this.time;
        }
    }
}

