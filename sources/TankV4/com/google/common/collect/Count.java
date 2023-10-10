package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.Nullable;

@GwtCompatible
final class Count implements Serializable {
   private int value;

   Count(int var1) {
      this.value = var1;
   }

   public int get() {
      return this.value;
   }

   public int getAndAdd(int var1) {
      int var2 = this.value;
      this.value = var2 + var1;
      return var2;
   }

   public int addAndGet(int var1) {
      return this.value += var1;
   }

   public void set(int var1) {
      this.value = var1;
   }

   public int getAndSet(int var1) {
      int var2 = this.value;
      this.value = var1;
      return var2;
   }

   public int hashCode() {
      return this.value;
   }

   public boolean equals(@Nullable Object var1) {
      return var1 instanceof Count && ((Count)var1).value == this.value;
   }

   public String toString() {
      return Integer.toString(this.value);
   }
}
