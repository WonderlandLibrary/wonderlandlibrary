package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@Beta
@GwtCompatible
public abstract class Ticker {
  public abstract long read();
  
  public static Ticker systemTicker() {
    return SYSTEM_TICKER;
  }
  
  private static final Ticker SYSTEM_TICKER = new Ticker() {
      public long read() {
        return Platform.systemNanoTime();
      }
    };
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\base\Ticker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */