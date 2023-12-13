package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@Beta
@GwtCompatible
public enum RemovalCause {
   EXPLICIT {
      boolean wasEvicted() {
         return false;
      }
   },
   REPLACED {
      boolean wasEvicted() {
         return false;
      }
   },
   COLLECTED {
      boolean wasEvicted() {
         return true;
      }
   },
   EXPIRED {
      boolean wasEvicted() {
         return true;
      }
   },
   SIZE {
      boolean wasEvicted() {
         return true;
      }
   };

   private static final RemovalCause[] $VALUES = new RemovalCause[]{EXPLICIT, REPLACED, COLLECTED, EXPIRED, SIZE};

   private RemovalCause() {
   }

   abstract boolean wasEvicted();

   RemovalCause(Object var3) {
      this();
   }
}
