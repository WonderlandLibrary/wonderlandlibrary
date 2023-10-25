package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public enum BoundType {
  OPEN {
    BoundType flip() {
      return CLOSED;
    }
  },
  CLOSED {
    BoundType flip() {
      return OPEN;
    }
  };
  
  static BoundType forBoolean(boolean inclusive) {
    return inclusive ? CLOSED : OPEN;
  }
  
  abstract BoundType flip();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\BoundType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */