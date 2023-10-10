package my.NewSnake.event;

import java.util.Iterator;

public class FlexibleArray implements Iterable {
   private Object[] elements;

   public void add(Object var1) {
      if (var1 != null) {
         Object[] var2 = new Object[this.size() + 1];

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var3 < this.size()) {
               var2[var3] = this.get(var3);
            } else {
               var2[var3] = var1;
            }
         }

         this.set(var2);
      }

   }

   public Iterator iterator() {
      return new Iterator(this) {
         private int index;
         final FlexibleArray this$0;

         {
            this.this$0 = var1;
            this.index = 0;
         }

         public boolean hasNext() {
            return this.index < this.this$0.size() && this.this$0.get(this.index) != null;
         }

         public void remove() {
            this.this$0.remove(this.this$0.get(this.index));
         }

         public Object next() {
            return this.this$0.get(this.index++);
         }
      };
   }

   public Object get(int var1) {
      return this.array()[var1];
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public FlexibleArray() {
      this.elements = new Object[0];
   }

   public void clear() {
      this.elements = new Object[0];
   }

   public int size() {
      return this.array().length;
   }

   public Object[] array() {
      return this.elements;
   }

   public void remove(Object var1) {
      if (var1 <= 0) {
         Object[] var2 = new Object[this.size() - 1];
         boolean var3 = true;

         for(int var4 = 0; var4 < this.size(); ++var4) {
            if (var3 && this.get(var4).equals(var1)) {
               var3 = false;
            } else {
               var2[var3 ? var4 : var4 - 1] = this.get(var4);
            }
         }

         this.set(var2);
      }

   }

   private void set(Object[] var1) {
      this.elements = var1;
   }

   public FlexibleArray(Object[] var1) {
      this.elements = var1;
   }
}
