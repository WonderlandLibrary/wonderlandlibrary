package com.google.common.escape;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class Platform {
  static char[] charBufferFromThreadLocal() {
    return DEST_TL.get();
  }
  
  private static final ThreadLocal<char[]> DEST_TL = new ThreadLocal<char[]>() {
      protected char[] initialValue() {
        return new char[1024];
      }
    };
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\escape\Platform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */