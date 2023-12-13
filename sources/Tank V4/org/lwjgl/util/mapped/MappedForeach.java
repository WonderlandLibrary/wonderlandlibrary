package org.lwjgl.util.mapped;

import java.util.Iterator;

final class MappedForeach implements Iterable {
   final MappedObject mapped;
   final int elementCount;

   MappedForeach(MappedObject var1, int var2) {
      this.mapped = var1;
      this.elementCount = var2;
   }

   public Iterator iterator() {
      return new Iterator(this) {
         private int index;
         final MappedForeach this$0;

         {
            this.this$0 = var1;
         }

         public boolean hasNext() {
            return this.index < this.this$0.elementCount;
         }

         public MappedObject next() {
            this.this$0.mapped.setViewAddress(this.this$0.mapped.getViewAddress(this.index++));
            return this.this$0.mapped;
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }

         public Object next() {
            return this.next();
         }
      };
   }
}
