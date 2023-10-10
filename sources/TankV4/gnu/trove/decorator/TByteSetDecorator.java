package gnu.trove.decorator;

import gnu.trove.iterator.TByteIterator;
import gnu.trove.set.TByteSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

public class TByteSetDecorator extends AbstractSet implements Set, Externalizable {
   static final long serialVersionUID = 1L;
   protected TByteSet _set;

   public TByteSetDecorator() {
   }

   public TByteSetDecorator(TByteSet var1) {
      this._set = var1;
   }

   public TByteSet getSet() {
      return this._set;
   }

   public boolean add(Byte var1) {
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

            byte var6;
            do {
               if (var4-- <= 0) {
                  return true;
               }

               Object var5 = var3.next();
               if (!(var5 instanceof Byte)) {
                  return false;
               }

               var6 = (Byte)var5;
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
      return var1 instanceof Byte && this._set.remove((Byte)var1);
   }

   public Iterator iterator() {
      return new Iterator(this) {
         private final TByteIterator it;
         final TByteSetDecorator this$0;

         {
            this.this$0 = var1;
            this.it = this.this$0._set.iterator();
         }

         public Byte next() {
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
      return !(var1 instanceof Byte) ? false : this._set.contains((Byte)var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      this._set = (TByteSet)var1.readObject();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(0);
      var1.writeObject(this._set);
   }

   public boolean add(Object var1) {
      return this.add((Byte)var1);
   }
}
