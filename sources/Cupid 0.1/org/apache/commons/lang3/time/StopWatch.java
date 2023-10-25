package org.apache.commons.lang3.time;

public class StopWatch {
  private static final long NANO_2_MILLIS = 1000000L;
  
  private enum State {
    UNSTARTED {
      boolean isStarted() {
        return false;
      }
      
      boolean isStopped() {
        return true;
      }
      
      boolean isSuspended() {
        return false;
      }
    },
    RUNNING {
      boolean isStarted() {
        return true;
      }
      
      boolean isStopped() {
        return false;
      }
      
      boolean isSuspended() {
        return false;
      }
    },
    STOPPED {
      boolean isStarted() {
        return false;
      }
      
      boolean isStopped() {
        return true;
      }
      
      boolean isSuspended() {
        return false;
      }
    },
    SUSPENDED {
      boolean isStarted() {
        return true;
      }
      
      boolean isStopped() {
        return false;
      }
      
      boolean isSuspended() {
        return true;
      }
    };
    
    abstract boolean isStarted();
    
    abstract boolean isStopped();
    
    abstract boolean isSuspended();
  }
  
  private enum SplitState {
    SPLIT, UNSPLIT;
  }
  
  private State runningState = State.UNSTARTED;
  
  private SplitState splitState = SplitState.UNSPLIT;
  
  private long startTime;
  
  private long startTimeMillis;
  
  private long stopTime;
  
  public void start() {
    if (this.runningState == State.STOPPED)
      throw new IllegalStateException("Stopwatch must be reset before being restarted. "); 
    if (this.runningState != State.UNSTARTED)
      throw new IllegalStateException("Stopwatch already started. "); 
    this.startTime = System.nanoTime();
    this.startTimeMillis = System.currentTimeMillis();
    this.runningState = State.RUNNING;
  }
  
  public void stop() {
    if (this.runningState != State.RUNNING && this.runningState != State.SUSPENDED)
      throw new IllegalStateException("Stopwatch is not running. "); 
    if (this.runningState == State.RUNNING)
      this.stopTime = System.nanoTime(); 
    this.runningState = State.STOPPED;
  }
  
  public void reset() {
    this.runningState = State.UNSTARTED;
    this.splitState = SplitState.UNSPLIT;
  }
  
  public void split() {
    if (this.runningState != State.RUNNING)
      throw new IllegalStateException("Stopwatch is not running. "); 
    this.stopTime = System.nanoTime();
    this.splitState = SplitState.SPLIT;
  }
  
  public void unsplit() {
    if (this.splitState != SplitState.SPLIT)
      throw new IllegalStateException("Stopwatch has not been split. "); 
    this.splitState = SplitState.UNSPLIT;
  }
  
  public void suspend() {
    if (this.runningState != State.RUNNING)
      throw new IllegalStateException("Stopwatch must be running to suspend. "); 
    this.stopTime = System.nanoTime();
    this.runningState = State.SUSPENDED;
  }
  
  public void resume() {
    if (this.runningState != State.SUSPENDED)
      throw new IllegalStateException("Stopwatch must be suspended to resume. "); 
    this.startTime += System.nanoTime() - this.stopTime;
    this.runningState = State.RUNNING;
  }
  
  public long getTime() {
    return getNanoTime() / 1000000L;
  }
  
  public long getNanoTime() {
    if (this.runningState == State.STOPPED || this.runningState == State.SUSPENDED)
      return this.stopTime - this.startTime; 
    if (this.runningState == State.UNSTARTED)
      return 0L; 
    if (this.runningState == State.RUNNING)
      return System.nanoTime() - this.startTime; 
    throw new RuntimeException("Illegal running state has occurred.");
  }
  
  public long getSplitTime() {
    return getSplitNanoTime() / 1000000L;
  }
  
  public long getSplitNanoTime() {
    if (this.splitState != SplitState.SPLIT)
      throw new IllegalStateException("Stopwatch must be split to get the split time. "); 
    return this.stopTime - this.startTime;
  }
  
  public long getStartTime() {
    if (this.runningState == State.UNSTARTED)
      throw new IllegalStateException("Stopwatch has not been started"); 
    return this.startTimeMillis;
  }
  
  public String toString() {
    return DurationFormatUtils.formatDurationHMS(getTime());
  }
  
  public String toSplitString() {
    return DurationFormatUtils.formatDurationHMS(getSplitTime());
  }
  
  public boolean isStarted() {
    return this.runningState.isStarted();
  }
  
  public boolean isSuspended() {
    return this.runningState.isSuspended();
  }
  
  public boolean isStopped() {
    return this.runningState.isStopped();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\time\StopWatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */