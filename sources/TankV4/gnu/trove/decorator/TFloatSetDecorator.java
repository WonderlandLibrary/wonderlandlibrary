package gnu.trove.decorator;

import gnu.trove.iterator.TFloatIterator;
import gnu.trove.set.TFloatSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

public class TFloatSetDecorator extends AbstractSet implements Set, Externalizable {
   static final long serialVersionUID = 1L;
   protected TFloatSet _set;

   public TFloatSetDecorator() {
   }

   public TFloatSetDecorator(TFloatSet var1) {
      this._set = var1;
   }

   public TFloatSet getSet() {
      return this._set;
   }

   public boolean add(Float var1) {
      return var1 != null && this._set.add(var1);
   }

   public boolean equals(Object var1) {
      if (this._set.equals(var1)) {
         return true;
      } else if (var1 instanceof Set) {
         Set var2 = (Set)var1;
         if (var2.size() != this._set.size()) {
            return false;
         } else {
            Iterator var3 = var2.iterator();
            int var4 = var2.size();

            float var6;
            do {
               if (var4-- <= 0) {
                  return true;
               }

               Object var5 = var3.next();
               if (!(var5 instanceof Float)) {
                  return false;
               }

               var6 = (Float)var5;
            } while(this._set.contains(var6));

            return false;
         }
      } else {
         return false;
      }
   }

   public void clear() {
      this._set.clear();
   }

   public boolean remove(Object var1) {
      return var1 instanceof Float && this._set.remove((Float)var1);
   }

   public Iterator iterator() {
      return new Iterator(this) {
         private final TFloatIterator it;
         final TFloatSetDecorator this$0;

         {
            this.this$0 = var1;
            this.it = this.this$0._set.iterator();
         }

         public Float next() {
            return this.it.next();
         }

         public boolean hasNext() {
            return this.it.hasNext();
         }

         public void remove() {
            this.it.remove();
         }

         public Object next() {
            return this.next();
         }
      };
   }

   public int size() {
      return this._set.size();
   }

   public boolean isEmpty() {
      return this._set.size() == 0;
   }

   public boolean contains(Object var1) {
      return !(var1 instanceof Float) ? false : this._set.contains((Float)var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      this._set = (TFloatSet)var1.readObject();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(0);
      var1.writeObject(this._set);
   }

   public boolean add(Object var1) {
      return this.add((Float)var1);
   }
}
