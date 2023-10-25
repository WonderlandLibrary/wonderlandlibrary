package org.apache.logging.log4j.core.appender.rolling;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.util.Integers;

@Plugin(name = "TimeBasedTriggeringPolicy", category = "Core", printObject = true)
public final class TimeBasedTriggeringPolicy extends AbstractTriggeringPolicy {
  private long nextRolloverMillis;
  
  private final int interval;
  
  private final boolean modulate;
  
  private final long maxRandomDelayMillis;
  
  private RollingFileManager manager;
  
  public static class Builder implements org.apache.logging.log4j.core.util.Builder<TimeBasedTriggeringPolicy> {
    @PluginBuilderAttribute
    private int interval = 1;
    
    @PluginBuilderAttribute
    private boolean modulate = false;
    
    @PluginBuilderAttribute
    private int maxRandomDelay = 0;
    
    public TimeBasedTriggeringPolicy build() {
      long maxRandomDelayMillis = TimeUnit.SECONDS.toMillis(this.maxRandomDelay);
      return new TimeBasedTriggeringPolicy(this.interval, this.modulate, maxRandomDelayMillis);
    }
    
    public int getInterval() {
      return this.interval;
    }
    
    public boolean isModulate() {
      return this.modulate;
    }
    
    public int getMaxRandomDelay() {
      return this.maxRandomDelay;
    }
    
    public Builder withInterval(int interval) {
      this.interval = interval;
      return this;
    }
    
    public Builder withModulate(boolean modulate) {
      this.modulate = modulate;
      return this;
    }
    
    public Builder withMaxRandomDelay(int maxRandomDelay) {
      this.maxRandomDelay = maxRandomDelay;
      return this;
    }
  }
  
  private TimeBasedTriggeringPolicy(int interval, boolean modulate, long maxRandomDelayMillis) {
    this.interval = interval;
    this.modulate = modulate;
    this.maxRandomDelayMillis = maxRandomDelayMillis;
  }
  
  public int getInterval() {
    return this.interval;
  }
  
  public long getNextRolloverMillis() {
    return this.nextRolloverMillis;
  }
  
  public void initialize(RollingFileManager aManager) {
    this.manager = aManager;
    long current = aManager.getFileTime();
    if (current == 0L)
      current = System.currentTimeMillis(); 
    aManager.getPatternProcessor().getNextTime(current, this.interval, this.modulate);
    aManager.getPatternProcessor().setTimeBased(true);
    this
      .nextRolloverMillis = ThreadLocalRandom.current().nextLong(0L, 1L + this.maxRandomDelayMillis) + aManager.getPatternProcessor().getNextTime(current, this.interval, this.modulate);
  }
  
  public boolean isTriggeringEvent(LogEvent event) {
    long nowMillis = event.getTimeMillis();
    if (nowMillis >= this.nextRolloverMillis) {
      this
        .nextRolloverMillis = ThreadLocalRandom.current().nextLong(0L, 1L + this.maxRandomDelayMillis) + this.manager.getPatternProcessor().getNextTime(nowMillis, this.interval, this.modulate);
      this.manager.getPatternProcessor().setCurrentFileTime(System.currentTimeMillis());
      return true;
    } 
    return false;
  }
  
  @Deprecated
  public static TimeBasedTriggeringPolicy createPolicy(@PluginAttribute("interval") String interval, @PluginAttribute("modulate") String modulate) {
    return newBuilder()
      .withInterval(Integers.parseInt(interval, 1))
      .withModulate(Boolean.parseBoolean(modulate))
      .build();
  }
  
  @PluginBuilderFactory
  public static Builder newBuilder() {
    return new Builder();
  }
  
  public String toString() {
    return "TimeBasedTriggeringPolicy(nextRolloverMillis=" + this.nextRolloverMillis + ", interval=" + this.interval + ", modulate=" + this.modulate + ")";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\TimeBasedTriggeringPolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */