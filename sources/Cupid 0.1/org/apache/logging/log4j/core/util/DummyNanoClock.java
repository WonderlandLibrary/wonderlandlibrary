package org.apache.logging.log4j.core.util;

public final class DummyNanoClock implements NanoClock {
  private final long fixedNanoTime;
  
  public DummyNanoClock() {
    this(0L);
  }
  
  public DummyNanoClock(long fixedNanoTime) {
    this.fixedNanoTime = fixedNanoTime;
  }
  
  public long nanoTime() {
    return this.fixedNanoTime;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\DummyNanoClock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */