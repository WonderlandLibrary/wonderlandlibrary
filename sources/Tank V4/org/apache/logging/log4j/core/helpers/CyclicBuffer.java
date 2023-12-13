package org.apache.logging.log4j.core.helpers;

import java.lang.reflect.Array;

public class CyclicBuffer {
   private final Object[] ring;
   private int first = 0;
   private int last = 0;
   private int numElems = 0;
   private final Class clazz;

   public CyclicBuffer(Class var1, int var2) throws IllegalArgumentException {
      if (var2 < 1) {
         throw new IllegalArgumentException("The maxSize argument (" + var2 + ") is not a positive integer.");
      } else {
         this.ring = this.makeArray(var1, var2);
         this.clazz = var1;
      }
   }

   private Object[] makeArray(Class var1, int var2) {
      return (Object[])((Object[])Array.newInstance(var1, var2));
   }

   public synchronized void add(Object var1) {
      this.ring[this.last] = var1;
      if (++this.last == this.ring.length) {
         this.last = 0;
      }

      if (this.numElems < this.ring.length) {
         ++this.numElems;
      } else if (++this.first == this.ring.length) {
         this.first = 0;
      }

   }

   public synchronized Object[] removeAll() {
      Object[] var1 = this.makeArray(this.clazz, this.numElems);
      int var2 = 0;

      while(this.numElems > 0) {
         --this.numElems;
         var1[var2++] = this.ring[this.first];
         this.ring[this.first] = null;
         if (++this.first == this.ring.length) {
            this.first = 0;
         }
      }

      return var1;
   }

   public boolean isEmpty() {
      return 0 == this.numElems;
   }
}
