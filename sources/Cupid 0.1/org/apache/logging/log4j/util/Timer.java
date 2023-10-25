package org.apache.logging.log4j.util;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Timer implements Serializable, StringBuilderFormattable {
  private static final long serialVersionUID = 9175191792439630013L;
  
  private final String name;
  
  private Status status;
  
  private long elapsedTime;
  
  private final int iterations;
  
  public enum Status {
    Started, Stopped, Paused;
  }
  
  private static long NANO_PER_SECOND = 1000000000L;
  
  private static long NANO_PER_MINUTE = NANO_PER_SECOND * 60L;
  
  private static long NANO_PER_HOUR = NANO_PER_MINUTE * 60L;
  
  private ThreadLocal<Long> startTime = new ThreadLocal<Long>() {
      protected Long initialValue() {
        return Long.valueOf(0L);
      }
    };
  
  public Timer(String name) {
    this(name, 0);
  }
  
  public Timer(String name, int iterations) {
    this.name = name;
    this.status = Status.Stopped;
    this.iterations = (iterations > 0) ? iterations : 0;
  }
  
  public synchronized void start() {
    this.startTime.set(Long.valueOf(System.nanoTime()));
    this.elapsedTime = 0L;
    this.status = Status.Started;
  }
  
  public synchronized void startOrResume() {
    if (this.status == Status.Stopped) {
      start();
    } else {
      resume();
    } 
  }
  
  public synchronized String stop() {
    this.elapsedTime += System.nanoTime() - ((Long)this.startTime.get()).longValue();
    this.startTime.set(Long.valueOf(0L));
    this.status = Status.Stopped;
    return toString();
  }
  
  public synchronized void pause() {
    this.elapsedTime += System.nanoTime() - ((Long)this.startTime.get()).longValue();
    this.startTime.set(Long.valueOf(0L));
    this.status = Status.Paused;
  }
  
  public synchronized void resume() {
    this.startTime.set(Long.valueOf(System.nanoTime()));
    this.status = Status.Started;
  }
  
  public String getName() {
    return this.name;
  }
  
  public long getElapsedTime() {
    return this.elapsedTime / 1000000L;
  }
  
  public long getElapsedNanoTime() {
    return this.elapsedTime;
  }
  
  public Status getStatus() {
    return this.status;
  }
  
  public String toString() {
    StringBuilder result = new StringBuilder();
    formatTo(result);
    return result.toString();
  }
  
  public void formatTo(StringBuilder buffer) {
    long nanoseconds, hours, minutes, seconds;
    String elapsed;
    DecimalFormat numFormat;
    buffer.append("Timer ").append(this.name);
    switch (this.status) {
      case Started:
        buffer.append(" started");
        return;
      case Paused:
        buffer.append(" paused");
        return;
      case Stopped:
        nanoseconds = this.elapsedTime;
        hours = nanoseconds / NANO_PER_HOUR;
        nanoseconds %= NANO_PER_HOUR;
        minutes = nanoseconds / NANO_PER_MINUTE;
        nanoseconds %= NANO_PER_MINUTE;
        seconds = nanoseconds / NANO_PER_SECOND;
        nanoseconds %= NANO_PER_SECOND;
        elapsed = "";
        if (hours > 0L)
          elapsed = elapsed + hours + " hours "; 
        if (minutes > 0L || hours > 0L)
          elapsed = elapsed + minutes + " minutes "; 
        numFormat = new DecimalFormat("#0");
        elapsed = elapsed + numFormat.format(seconds) + '.';
        numFormat = new DecimalFormat("000000000");
        elapsed = elapsed + numFormat.format(nanoseconds) + " seconds";
        buffer.append(" stopped. Elapsed time: ").append(elapsed);
        if (this.iterations > 0) {
          nanoseconds = this.elapsedTime / this.iterations;
          hours = nanoseconds / NANO_PER_HOUR;
          nanoseconds %= NANO_PER_HOUR;
          minutes = nanoseconds / NANO_PER_MINUTE;
          nanoseconds %= NANO_PER_MINUTE;
          seconds = nanoseconds / NANO_PER_SECOND;
          nanoseconds %= NANO_PER_SECOND;
          elapsed = "";
          if (hours > 0L)
            elapsed = elapsed + hours + " hours "; 
          if (minutes > 0L || hours > 0L)
            elapsed = elapsed + minutes + " minutes "; 
          numFormat = new DecimalFormat("#0");
          elapsed = elapsed + numFormat.format(seconds) + '.';
          numFormat = new DecimalFormat("000000000");
          elapsed = elapsed + numFormat.format(nanoseconds) + " seconds";
          buffer.append(" Average per iteration: ").append(elapsed);
        } 
        return;
    } 
    buffer.append(' ').append(this.status);
  }
  
  public boolean equals(Object o) {
    if (this == o)
      return true; 
    if (!(o instanceof Timer))
      return false; 
    Timer timer = (Timer)o;
    if (this.elapsedTime != timer.elapsedTime)
      return false; 
    if (this.startTime != timer.startTime)
      return false; 
    if ((this.name != null) ? !this.name.equals(timer.name) : (timer.name != null))
      return false; 
    if ((this.status != null) ? !this.status.equals(timer.status) : (timer.status != null))
      return false; 
    return true;
  }
  
  public int hashCode() {
    int result = (this.name != null) ? this.name.hashCode() : 0;
    result = 29 * result + ((this.status != null) ? this.status.hashCode() : 0);
    long time = ((Long)this.startTime.get()).longValue();
    result = 29 * result + (int)(time ^ time >>> 32L);
    result = 29 * result + (int)(this.elapsedTime ^ this.elapsedTime >>> 32L);
    return result;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4\\util\Timer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */