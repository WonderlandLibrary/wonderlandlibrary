package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import javax.annotation.Nullable;

@GwtIncompatible("unnecessary")
abstract class ImmutableMapEntry extends ImmutableEntry {
   ImmutableMapEntry(Object var1, Object var2) {
      super(var1, var2);
      CollectPreconditions.checkEntryNotNull(var1, var2);
   }

   ImmutableMapEntry(ImmutableMapEntry var1) {
      super(var1.getKey(), var1.getValue());
   }

   @Nullable
   abstract ImmutableMapEntry getNextInKeyBucket();

   @Nullable
   abstract ImmutableMapEntry getNextInValueBucket();

   static final class TerminalEntry extends ImmutableMapEntry {
      TerminalEntry(ImmutableMapEntry var1) {
         super(var1);
      }

      TerminalEntry(Object var1, Object var2) {
         super(var1, var2);
      }

      @Nullable
      ImmutableMapEntry getNextInKeyBucket() {
         return null;
      }

      @Nullable
      ImmutableMapEntry getNextInValueBucket() {
         return null;
      }
   }
}
