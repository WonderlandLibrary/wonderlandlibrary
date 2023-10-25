package io.netty.util;

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
  private static final String PROP_LEVEL = "io.netty.leakDetectionLevel";
  
  static {
    boolean disabled;
  }
  
  private static final Level DEFAULT_LEVEL = Level.SIMPLE;
  
  private static Level level;
  
  public enum Level {
    DISABLED, SIMPLE, ADVANCED, PARANOID;
  }
  
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(ResourceLeakDetector.class);
  
  private static final int DEFAULT_SAMPLING_INTERVAL = 113;
  
  static {
    if (SystemPropertyUtil.get("io.netty.noResourceLeakDetection") != null) {
      disabled = SystemPropertyUtil.getBoolean("io.netty.noResourceLeakDetection", false);
      logger.debug("-Dio.netty.noResourceLeakDetection: {}", Boolean.valueOf(disabled));
      logger.warn("-Dio.netty.noResourceLeakDetection is deprecated. Use '-D{}={}' instead.", "io.netty.leakDetectionLevel", DEFAULT_LEVEL.name().toLowerCase());
    } else {
      disabled = false;
    } 
    Level defaultLevel = disabled ? Level.DISABLED : DEFAULT_LEVEL;
    String levelStr = SystemPropertyUtil.get("io.netty.leakDetectionLevel", defaultLevel.name()).trim().toUpperCase();
    Level level = DEFAULT_LEVEL;
    for (Level l : EnumSet.<Level>allOf(Level.class)) {
      if (levelStr.equals(l.name()) || levelStr.equals(String.valueOf(l.ordinal())))
        level = l; 
    } 
    ResourceLeakDetector.level = level;
    if (logger.isDebugEnabled())
      logger.debug("-D{}: {}", "io.netty.leakDetectionLevel", level.name().toLowerCase()); 
    STACK_TRACE_ELEMENT_EXCLUSIONS = new String[] { "io.netty.buffer.AbstractByteBufAllocator.toLeakAwareBuffer(" };
  }
  
  @Deprecated
  public static void setEnabled(boolean enabled) {
    setLevel(enabled ? Level.SIMPLE : Level.DISABLED);
  }
  
  public static boolean isEnabled() {
    return (getLevel().ordinal() > Level.DISABLED.ordinal());
  }
  
  public static void setLevel(Level level) {
    if (level == null)
      throw new NullPointerException("level"); 
    ResourceLeakDetector.level = level;
  }
  
  public static Level getLevel() {
    return level;
  }
  
  private final DefaultResourceLeak head = new DefaultResourceLeak(null);
  
  private final DefaultResourceLeak tail = new DefaultResourceLeak(null);
  
  private final ReferenceQueue<Object> refQueue = new ReferenceQueue();
  
  private final ConcurrentMap<String, Boolean> reportedLeaks = PlatformDependent.newConcurrentHashMap();
  
  private final String resourceType;
  
  private final int samplingInterval;
  
  private final long maxActive;
  
  private long active;
  
  private final AtomicBoolean loggedTooManyActive = new AtomicBoolean();
  
  private long leakCheckCnt;
  
  private static final String[] STACK_TRACE_ELEMENT_EXCLUSIONS;
  
  public ResourceLeakDetector(Class<?> resourceType) {
    this(StringUtil.simpleClassName(resourceType));
  }
  
  public ResourceLeakDetector(String resourceType) {
    this(resourceType, 113, Long.MAX_VALUE);
  }
  
  public ResourceLeakDetector(Class<?> resourceType, int samplingInterval, long maxActive) {
    this(StringUtil.simpleClassName(resourceType), samplingInterval, maxActive);
  }
  
  public ResourceLeakDetector(String resourceType, int samplingInterval, long maxActive) {
    if (resourceType == null)
      throw new NullPointerException("resourceType"); 
    if (samplingInterval <= 0)
      throw new IllegalArgumentException("samplingInterval: " + samplingInterval + " (expected: 1+)"); 
    if (maxActive <= 0L)
      throw new IllegalArgumentException("maxActive: " + maxActive + " (expected: 1+)"); 
    this.resourceType = resourceType;
    this.samplingInterval = samplingInterval;
    this.maxActive = maxActive;
    this.head.next = this.tail;
    this.tail.prev = this.head;
  }
  
  public ResourceLeak open(T obj) {
    Level level = ResourceLeakDetector.level;
    if (level == Level.DISABLED)
      return null; 
    if (level.ordinal() < Level.PARANOID.ordinal()) {
      if (this.leakCheckCnt++ % this.samplingInterval == 0L) {
        reportLeak(level);
        return new DefaultResourceLeak(obj);
      } 
      return null;
    } 
    reportLeak(level);
    return new DefaultResourceLeak(obj);
  }
  
  private void reportLeak(Level level) {
    if (!logger.isErrorEnabled()) {
      while (true) {
        DefaultResourceLeak ref = (DefaultResourceLeak)this.refQueue.poll();
        if (ref == null)
          break; 
        ref.close();
      } 
      return;
    } 
    int samplingInterval = (level == Level.PARANOID) ? 1 : this.samplingInterval;
    if (this.active * samplingInterval > this.maxActive && this.loggedTooManyActive.compareAndSet(false, true))
      logger.error("LEAK: You are creating too many " + this.resourceType + " instances.  " + this.resourceType + " is a shared resource that must be reused across the JVM," + "so that only a few instances are created."); 
    while (true) {
      DefaultResourceLeak ref = (DefaultResourceLeak)this.refQueue.poll();
      if (ref == null)
        break; 
      ref.clear();
      if (!ref.close())
        continue; 
      String records = ref.toString();
      if (this.reportedLeaks.putIfAbsent(records, Boolean.TRUE) == null) {
        if (records.isEmpty()) {
          logger.error("LEAK: {}.release() was not called before it's garbage-collected. Enable advanced leak reporting to find out where the leak occurred. To enable advanced leak reporting, specify the JVM option '-D{}={}' or call {}.setLevel()", new Object[] { this.resourceType, "io.netty.leakDetectionLevel", Level.ADVANCED.name().toLowerCase(), StringUtil.simpleClassName(this) });
          continue;
        } 
        logger.error("LEAK: {}.release() was not called before it's garbage-collected.{}", this.resourceType, records);
      } 
    } 
  }
  
  private final class DefaultResourceLeak extends PhantomReference<Object> implements ResourceLeak {
    private static final int MAX_RECORDS = 4;
    
    private final String creationRecord;
    
    private final Deque<String> lastRecords = new ArrayDeque<String>();
    
    private final AtomicBoolean freed;
    
    private DefaultResourceLeak prev;
    
    private DefaultResourceLeak next;
    
    DefaultResourceLeak(Object referent) {
      super(referent, (referent != null) ? ResourceLeakDetector.this.refQueue : null);
      if (referent != null) {
        ResourceLeakDetector.Level level = ResourceLeakDetector.getLevel();
        if (level.ordinal() >= ResourceLeakDetector.Level.ADVANCED.ordinal()) {
          this.creationRecord = ResourceLeakDetector.newRecord(3);
        } else {
          this.creationRecord = null;
        } 
        synchronized (ResourceLeakDetector.this.head) {
          this.prev = ResourceLeakDetector.this.head;
          this.next = ResourceLeakDetector.this.head.next;
          ResourceLeakDetector.this.head.next.prev = this;
          ResourceLeakDetector.this.head.next = this;
          ResourceLeakDetector.this.active++;
        } 
        this.freed = new AtomicBoolean();
      } else {
        this.creationRecord = null;
        this.freed = new AtomicBoolean(true);
      } 
    }
    
    public void record() {
      if (this.creationRecord != null) {
        String value = ResourceLeakDetector.newRecord(2);
        synchronized (this.lastRecords) {
          int size = this.lastRecords.size();
          if (size == 0 || !((String)this.lastRecords.getLast()).equals(value))
            this.lastRecords.add(value); 
          if (size > 4)
            this.lastRecords.removeFirst(); 
        } 
      } 
    }
    
    public boolean close() {
      if (this.freed.compareAndSet(false, true)) {
        synchronized (ResourceLeakDetector.this.head) {
          ResourceLeakDetector.this.active--;
          this.prev.next = this.next;
          this.next.prev = this.prev;
          this.prev = null;
          this.next = null;
        } 
        return true;
      } 
      return false;
    }
    
    public String toString() {
      Object[] array;
      if (this.creationRecord == null)
        return ""; 
      synchronized (this.lastRecords) {
        array = this.lastRecords.toArray();
      } 
      StringBuilder buf = new StringBuilder(16384);
      buf.append(StringUtil.NEWLINE);
      buf.append("Recent access records: ");
      buf.append(array.length);
      buf.append(StringUtil.NEWLINE);
      if (array.length > 0)
        for (int i = array.length - 1; i >= 0; i--) {
          buf.append('#');
          buf.append(i + 1);
          buf.append(':');
          buf.append(StringUtil.NEWLINE);
          buf.append(array[i]);
        }  
      buf.append("Created at:");
      buf.append(StringUtil.NEWLINE);
      buf.append(this.creationRecord);
      buf.setLength(buf.length() - StringUtil.NEWLINE.length());
      return buf.toString();
    }
  }
  
  static String newRecord(int recordsToSkip) {
    StringBuilder buf = new StringBuilder(4096);
    StackTraceElement[] array = (new Throwable()).getStackTrace();
    for (StackTraceElement e : array) {
      if (recordsToSkip > 0) {
        recordsToSkip--;
      } else {
        String estr = e.toString();
        boolean excluded = false;
        for (String exclusion : STACK_TRACE_ELEMENT_EXCLUSIONS) {
          if (estr.startsWith(exclusion)) {
            excluded = true;
            break;
          } 
        } 
        if (!excluded) {
          buf.append('\t');
          buf.append(estr);
          buf.append(StringUtil.NEWLINE);
        } 
      } 
    } 
    return buf.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\ResourceLeakDetector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */