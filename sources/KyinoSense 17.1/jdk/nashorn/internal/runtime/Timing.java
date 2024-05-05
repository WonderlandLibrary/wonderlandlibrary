/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
import java.util.function.Supplier;
import jdk.nashorn.internal.codegen.CompileUnit;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.runtime.logging.Logger;

@Logger(name="time")
public final class Timing
implements Loggable {
    private DebugLogger log;
    private TimeSupplier timeSupplier;
    private final boolean isEnabled;
    private final long startTime;
    private static final String LOGGER_NAME = Timing.class.getAnnotation(Logger.class).name();

    public Timing(boolean isEnabled) {
        this.isEnabled = isEnabled;
        this.startTime = System.nanoTime();
    }

    public String getLogInfo() {
        assert (this.isEnabled());
        return this.timeSupplier.get();
    }

    public String[] getLogInfoLines() {
        assert (this.isEnabled());
        return this.timeSupplier.getStrings();
    }

    boolean isEnabled() {
        return this.isEnabled;
    }

    public void accumulateTime(String module, long durationNano) {
        if (this.isEnabled()) {
            this.ensureInitialized(Context.getContextTrusted());
            this.timeSupplier.accumulateTime(module, durationNano);
        }
    }

    private DebugLogger ensureInitialized(Context context) {
        if (this.isEnabled() && this.log == null) {
            this.log = this.initLogger(context);
            if (this.log.isEnabled()) {
                this.timeSupplier = new TimeSupplier();
                Runtime.getRuntime().addShutdownHook(new Thread(){

                    @Override
                    public void run() {
                        StringBuilder sb = new StringBuilder();
                        for (String str : Timing.this.timeSupplier.getStrings()) {
                            sb.append('[').append(Timing.getLoggerName()).append("] ").append(str).append('\n');
                        }
                        System.err.print(sb);
                    }
                });
            }
        }
        return this.log;
    }

    static String getLoggerName() {
        return LOGGER_NAME;
    }

    @Override
    public DebugLogger initLogger(Context context) {
        return context.getLogger(this.getClass());
    }

    @Override
    public DebugLogger getLogger() {
        return this.log;
    }

    public static String toMillisPrint(long durationNano) {
        return Long.toString(TimeUnit.NANOSECONDS.toMillis(durationNano));
    }

    final class TimeSupplier
    implements Supplier<String> {
        private final Map<String, LongAdder> timings = new ConcurrentHashMap<String, LongAdder>();
        private final LinkedBlockingQueue<String> orderedTimingNames = new LinkedBlockingQueue();
        private final Function<String, LongAdder> newTimingCreator = new Function<String, LongAdder>(){

            @Override
            public LongAdder apply(String s) {
                TimeSupplier.this.orderedTimingNames.add(s);
                return new LongAdder();
            }
        };

        TimeSupplier() {
        }

        String[] getStrings() {
            ArrayList<String> strs = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new StringReader(this.get()));
            try {
                String line;
                while ((line = br.readLine()) != null) {
                    strs.add(line);
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            return strs.toArray(new String[strs.size()]);
        }

        @Override
        public String get() {
            long t = System.nanoTime();
            long knownTime = 0L;
            int maxKeyLength = 0;
            int maxValueLength = 0;
            for (Map.Entry<String, LongAdder> entry : this.timings.entrySet()) {
                maxKeyLength = Math.max(maxKeyLength, entry.getKey().length());
                maxValueLength = Math.max(maxValueLength, Timing.toMillisPrint(entry.getValue().longValue()).length());
            }
            ++maxKeyLength;
            StringBuilder sb = new StringBuilder();
            sb.append("Accumulated compilation phase timings:\n\n");
            for (String timingName : this.orderedTimingNames) {
                int len = sb.length();
                sb.append(timingName);
                len = sb.length() - len;
                while (len++ < maxKeyLength) {
                    sb.append(' ');
                }
                long duration = this.timings.get(timingName).longValue();
                String strDuration = Timing.toMillisPrint(duration);
                len = strDuration.length();
                for (int i = 0; i < maxValueLength - len; ++i) {
                    sb.append(' ');
                }
                sb.append(strDuration).append(" ms\n");
                knownTime += duration;
            }
            long l = t - Timing.this.startTime;
            sb.append('\n');
            sb.append("Total runtime: ").append(Timing.toMillisPrint(l)).append(" ms (Non-runtime: ").append(Timing.toMillisPrint(knownTime)).append(" ms [").append((int)((double)knownTime * 100.0 / (double)l)).append("%])");
            sb.append("\n\nEmitted compile units: ").append(CompileUnit.getEmittedUnitCount());
            return sb.toString();
        }

        private void accumulateTime(String module, long duration) {
            this.timings.computeIfAbsent(module, this.newTimingCreator).add(duration);
        }
    }
}

