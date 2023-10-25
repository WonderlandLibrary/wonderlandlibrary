/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util;

import io.netty.util.ResourceLeak;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ResourceLeakDetector<T> {
    private final AtomicBoolean loggedTooManyActive;
    private final DefaultResourceLeak tail;
    private static final String PROP_LEVEL = "io.netty.leakDetectionLevel";
    private static final String[] STACK_TRACE_ELEMENT_EXCLUSIONS;
    private static final Level DEFAULT_LEVEL;
    private final ConcurrentMap<String, Boolean> reportedLeaks;
    private final DefaultResourceLeak head = new DefaultResourceLeak((Object)null);
    private final int samplingInterval;
    private final ReferenceQueue<Object> refQueue;
    private static final int DEFAULT_SAMPLING_INTERVAL = 113;
    private static final InternalLogger logger;
    private long active;
    private final String resourceType;
    private final long maxActive;
    private static Level level;
    private long leakCheckCnt;

    public ResourceLeakDetector(String string) {
        this(string, 113, Long.MAX_VALUE);
    }

    static {
        boolean bl;
        DEFAULT_LEVEL = Level.SIMPLE;
        logger = InternalLoggerFactory.getInstance(ResourceLeakDetector.class);
        if (SystemPropertyUtil.get("io.netty.noResourceLeakDetection") != null) {
            bl = SystemPropertyUtil.getBoolean("io.netty.noResourceLeakDetection", false);
            logger.debug("-Dio.netty.noResourceLeakDetection: {}", (Object)bl);
            logger.warn("-Dio.netty.noResourceLeakDetection is deprecated. Use '-D{}={}' instead.", (Object)PROP_LEVEL, (Object)DEFAULT_LEVEL.name().toLowerCase());
        } else {
            bl = false;
        }
        Level level = bl ? Level.DISABLED : DEFAULT_LEVEL;
        String string = SystemPropertyUtil.get(PROP_LEVEL, level.name()).trim().toUpperCase();
        Level level2 = DEFAULT_LEVEL;
        for (Level level3 : EnumSet.allOf(Level.class)) {
            if (!string.equals(level3.name()) && !string.equals(String.valueOf(level3.ordinal()))) continue;
            level2 = level3;
        }
        ResourceLeakDetector.level = level2;
        if (logger.isDebugEnabled()) {
            logger.debug("-D{}: {}", (Object)PROP_LEVEL, (Object)level2.name().toLowerCase());
        }
        STACK_TRACE_ELEMENT_EXCLUSIONS = new String[]{"io.netty.buffer.AbstractByteBufAllocator.toLeakAwareBuffer("};
    }

    static String newRecord(int n) {
        StackTraceElement[] stackTraceElementArray;
        StringBuilder stringBuilder = new StringBuilder(4096);
        for (StackTraceElement stackTraceElement : stackTraceElementArray = new Throwable().getStackTrace()) {
            if (n > 0) {
                --n;
                continue;
            }
            String string = stackTraceElement.toString();
            boolean bl = false;
            for (String string2 : STACK_TRACE_ELEMENT_EXCLUSIONS) {
                if (!string.startsWith(string2)) continue;
                bl = true;
                break;
            }
            if (bl) continue;
            stringBuilder.append('\t');
            stringBuilder.append(string);
            stringBuilder.append(StringUtil.NEWLINE);
        }
        return stringBuilder.toString();
    }

    public static boolean isEnabled() {
        return ResourceLeakDetector.getLevel().ordinal() > Level.DISABLED.ordinal();
    }

    private void reportLeak(Level level) {
        DefaultResourceLeak defaultResourceLeak;
        int n;
        if (!logger.isErrorEnabled()) {
            DefaultResourceLeak defaultResourceLeak2;
            while ((defaultResourceLeak2 = (DefaultResourceLeak)this.refQueue.poll()) != null) {
                defaultResourceLeak2.close();
            }
            return;
        }
        int n2 = n = level == Level.PARANOID ? 1 : this.samplingInterval;
        if (this.active * (long)n > this.maxActive && this.loggedTooManyActive.compareAndSet(false, true)) {
            logger.error("LEAK: You are creating too many " + this.resourceType + " instances.  " + this.resourceType + " is a shared resource that must be reused across the JVM," + "so that only a few instances are created.");
        }
        while ((defaultResourceLeak = (DefaultResourceLeak)this.refQueue.poll()) != null) {
            String string;
            defaultResourceLeak.clear();
            if (!defaultResourceLeak.close() || this.reportedLeaks.putIfAbsent(string = defaultResourceLeak.toString(), Boolean.TRUE) != null) continue;
            if (string.isEmpty()) {
                logger.error("LEAK: {}.release() was not called before it's garbage-collected. Enable advanced leak reporting to find out where the leak occurred. To enable advanced leak reporting, specify the JVM option '-D{}={}' or call {}.setLevel()", this.resourceType, PROP_LEVEL, Level.ADVANCED.name().toLowerCase(), StringUtil.simpleClassName(this));
                continue;
            }
            logger.error("LEAK: {}.release() was not called before it's garbage-collected.{}", (Object)this.resourceType, (Object)string);
        }
    }

    public ResourceLeakDetector(String string, int n, long l) {
        this.tail = new DefaultResourceLeak((Object)null);
        this.refQueue = new ReferenceQueue();
        this.reportedLeaks = PlatformDependent.newConcurrentHashMap();
        this.loggedTooManyActive = new AtomicBoolean();
        if (string == null) {
            throw new NullPointerException("resourceType");
        }
        if (n <= 0) {
            throw new IllegalArgumentException("samplingInterval: " + n + " (expected: 1+)");
        }
        if (l <= 0L) {
            throw new IllegalArgumentException("maxActive: " + l + " (expected: 1+)");
        }
        this.resourceType = string;
        this.samplingInterval = n;
        this.maxActive = l;
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    public ResourceLeakDetector(Class<?> clazz, int n, long l) {
        this(StringUtil.simpleClassName(clazz), n, l);
    }

    public static Level getLevel() {
        return level;
    }

    public ResourceLeakDetector(Class<?> clazz) {
        this(StringUtil.simpleClassName(clazz));
    }

    public static void setLevel(Level level) {
        if (level == null) {
            throw new NullPointerException("level");
        }
        ResourceLeakDetector.level = level;
    }

    @Deprecated
    public static void setEnabled(boolean bl) {
        ResourceLeakDetector.setLevel(bl ? Level.SIMPLE : Level.DISABLED);
    }

    public ResourceLeak open(T t) {
        Level level = ResourceLeakDetector.level;
        if (level == Level.DISABLED) {
            return null;
        }
        if (level.ordinal() < Level.PARANOID.ordinal()) {
            if (this.leakCheckCnt++ % (long)this.samplingInterval == 0L) {
                this.reportLeak(level);
                return new DefaultResourceLeak(t);
            }
            return null;
        }
        this.reportLeak(level);
        return new DefaultResourceLeak(t);
    }

    private final class DefaultResourceLeak
    extends PhantomReference<Object>
    implements ResourceLeak {
        private final AtomicBoolean freed;
        private final Deque<String> lastRecords;
        private DefaultResourceLeak next;
        private final String creationRecord;
        private DefaultResourceLeak prev;
        private static final int MAX_RECORDS = 4;

        DefaultResourceLeak(Object object) {
            super(object, object != null ? ResourceLeakDetector.this.refQueue : null);
            this.lastRecords = new ArrayDeque<String>();
            if (object != null) {
                Level level = ResourceLeakDetector.getLevel();
                this.creationRecord = level.ordinal() >= Level.ADVANCED.ordinal() ? ResourceLeakDetector.newRecord(3) : null;
                DefaultResourceLeak defaultResourceLeak = ResourceLeakDetector.this.head;
                synchronized (defaultResourceLeak) {
                    this.prev = ResourceLeakDetector.this.head;
                    this.next = ((ResourceLeakDetector)ResourceLeakDetector.this).head.next;
                    ((ResourceLeakDetector)ResourceLeakDetector.this).head.next.prev = this;
                    ((ResourceLeakDetector)ResourceLeakDetector.this).head.next = this;
                    ResourceLeakDetector.this.active++;
                }
                this.freed = new AtomicBoolean();
            } else {
                this.creationRecord = null;
                this.freed = new AtomicBoolean(true);
            }
        }

        @Override
        public boolean close() {
            if (this.freed.compareAndSet(false, true)) {
                DefaultResourceLeak defaultResourceLeak = ResourceLeakDetector.this.head;
                synchronized (defaultResourceLeak) {
                    ResourceLeakDetector.this.active--;
                    this.prev.next = this.next;
                    this.next.prev = this.prev;
                    this.prev = null;
                    this.next = null;
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            Object[] objectArray;
            if (this.creationRecord == null) {
                return "";
            }
            Object object = this.lastRecords;
            synchronized (object) {
                objectArray = this.lastRecords.toArray();
            }
            object = new StringBuilder(16384);
            ((StringBuilder)object).append(StringUtil.NEWLINE);
            ((StringBuilder)object).append("Recent access records: ");
            ((StringBuilder)object).append(objectArray.length);
            ((StringBuilder)object).append(StringUtil.NEWLINE);
            if (objectArray.length > 0) {
                for (int i = objectArray.length - 1; i >= 0; --i) {
                    ((StringBuilder)object).append('#');
                    ((StringBuilder)object).append(i + 1);
                    ((StringBuilder)object).append(':');
                    ((StringBuilder)object).append(StringUtil.NEWLINE);
                    ((StringBuilder)object).append(objectArray[i]);
                }
            }
            ((StringBuilder)object).append("Created at:");
            ((StringBuilder)object).append(StringUtil.NEWLINE);
            ((StringBuilder)object).append(this.creationRecord);
            ((StringBuilder)object).setLength(((StringBuilder)object).length() - StringUtil.NEWLINE.length());
            return ((StringBuilder)object).toString();
        }

        @Override
        public void record() {
            block3: {
                if (this.creationRecord == null) break block3;
                String string = ResourceLeakDetector.newRecord(2);
                Deque<String> deque = this.lastRecords;
                synchronized (deque) {
                    int n = this.lastRecords.size();
                    if (n == 0 || !this.lastRecords.getLast().equals(string)) {
                        this.lastRecords.add(string);
                    }
                    if (n > 4) {
                        this.lastRecords.removeFirst();
                    }
                }
            }
        }
    }

    public static enum Level {
        DISABLED,
        SIMPLE,
        ADVANCED,
        PARANOID;

    }
}

