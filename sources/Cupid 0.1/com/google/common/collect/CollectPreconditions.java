package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

@GwtCompatible
final class CollectPreconditions {
  static void checkEntryNotNull(Object key, Object value) {
    if (key == null)
      throw new NullPointerException("null key in entry: null=" + value); 
    if (value == null)
      throw new NullPointerException("null value in entry: " + key + "=null"); 
  }
  
  static int checkNonnegative(int value, String name) {
    if (value < 0)
      throw new IllegalArgumentException(name + " cannot be negative but was: " + value); 
    return value;
  }
  
  static void checkRemove(boolean canRemove) {
    Preconditions.checkState(canRemove, "no calls to next() since the last call to remove()");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\CollectPreconditions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */