package gnu.trove.set.hash;

import gnu.trove.TFloatCollection;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TFloatHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.iterator.TFloatIterator;
import gnu.trove.set.TFloatSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class TFloatHashSet extends TFloatHash implements TFloatSet, Externalizable {
   static final long serialVersionUID = 1L;

   public TFloatHashSet() {
   }

   public TFloatHashSet(int var1) {
      super(var1);
   }

   public TFloatHashSet(int var1, float var2) {
      super(var1, var2);
   }

   public TFloatHashSet(int var1, float var2, float var3) {
      super(var1, var2, var3);
      if (var3 != 0.0F) {
         Arrays.fill(this._set, var3);
      }

   }

   public TFloatHashSet(Collection var1) {
      this(Math.max(var1.size(), 10));
      this.addAll(var1);
   }

   public TFloatHashSet(TFloatCollection var1) {
      this(Math.max(var1.size(), 10));
      if (var1 instanceof TFloatHashSet) {
         TFloatHashSet var2 = (TFloatHashSet)var1;
         this._loadFactor = var2._loadFactor;
         this.no_entry_value = var2.no_entry_value;
         if (this.no_entry_value != 0.0F) {
            Arrays.fill(this._set, this.no_entry_value);
         }

         this.setUp((int)Math.ceil((double)(10.0F / this._loadFactor)));
      }

      this.addAll(var1);
   }

   public TFloatHashSet(float[] var1) {
      this(Math.max(var1.length, 10));
      this.addAll(var1);
   }

   public TFloatIterator iterator() {
      return new TFloatHashSet.TFloatHashIterator(this, this);
   }

   public float[] toArray() {
      float[] var1 = new float[this.size()];
      float[] var2 = this._set;
      byte[] var3 = this._states;
      int var4 = var3.length;
      int var5 = 0;

      while(var4-- > 0) {
         if (var3[var4] == 1) {
            var1[var5++] = var2[var4];
         }
      }

      return var1;
   }

   public float[] toArray(float[] var1) {
      float[] var2 = this._set;
      byte[] var3 = this._states;
      int var4 = var3.length;
      int var5 = 0;

      while(var4-- > 0) {
         if (var3[var4] == 1) {
            var1[var5++] = var2[var4];
         }
      }

      if (var1.length > this._size) {
         var1[this._size] = this.no_entry_value;
      }

      return var1;
   }

   public boolean containsAll(Collection var1) {
      Iterator var2 = var1.iterator();

      float var4;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         Object var3 = var2.next();
         if (!(var3 instanceof Float)) {
            return false;
         }

         var4 = (Float)var3;
      } while(this.contains(var4));

      return false;
   }

   public boolean containsAll(TFloatCollection var1) {
      TFloatIterator var2 = var1.iterator();

      float var3;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         var3 = var2.next();
      } while(this.contains(var3));

      return false;
   }

   public boolean containsAll(float[] var1) {
      int var2 = var1.length;

      do {
         if (var2-- <= 0) {
            return true;
         }
      } while(this.contains(var1[var2]));

      return false;
   }

   public boolean addAll(Collection var1) {
      boolean var2 = false;
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Float var4 = (Float)var3.next();
         float var5 = var4;
         if (var5 < 0) {
            var2 = true;
         }
      }

      return var2;
   }

   public boolean addAll(TFloatCollection var1) {
      boolean var2 = false;
      TFloatIterator var3 = var1.iterator();

      while(var3.hasNext()) {
         float var4 = var3.next();
         if (var4 < 0) {
            var2 = true;
         }
      }

      return var2;
   }

   public boolean addAll(float[] var1) {
      boolean var2 = false;
      int var3 = var1.length;

      while(var3-- > 0) {
         if (var1[var3] < 0) {
            var2 = true;
         }
      }

      return var2;
   }

   public boolean retainAll(Collection var1) {
      boolean var2 = false;
      TFloatIterator var3 = this.iterator();

      while(var3.hasNext()) {
         if (!var1.contains(var3.next())) {
            var3.remove();
            var2 = true;
         }
      }

      return var2;
   }

   public boolean retainAll(TFloatCollection var1) {
      if (this == var1) {
         return false;
      } else {
         boolean var2 = false;
         TFloatIterator var3 = this.iterator();

         while(var3.hasNext()) {
            if (!var1.contains(var3.next())) {
               var3.remove();
               var2 = true;
            }
         }

         return var2;
      }
   }

   public boolean retainAll(float[] var1) {
      boolean var2 = false;
      Arrays.sort(var1);
      float[] var3 = this._set;
      byte[] var4 = this._states;
      this._autoCompactTemporaryDisable = true;
      int var5 = var3.length;

      while(var5-- > 0) {
         if (var4[var5] == 1 && Arrays.binarySearch(var1, var3[var5]) < 0) {
            this.removeAt(var5);
            var2 = true;
         }
      }

      this._autoCompactTemporaryDisable = false;
      return var2;
   }

   public boolean removeAll(Collection var1) {
      boolean var2 = false;
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         if (var4 instanceof Float) {
            float var5 = (Float)var4;
            if (var5 >= 0) {
               var2 = true;
            }
         }
      }

      return var2;
   }

   public boolean removeAll(TFloatCollection var1) {
      boolean var2 = false;
      TFloatIterator var3 = var1.iterator();

      while(var3.hasNext()) {
         float var4 = var3.next();
         if (var4 >= 0) {
            var2 = true;
         }
      }

      return var2;
   }

   public boolean removeAll(float[] var1) {
      boolean var2 = false;
      int var3 = var1.length;

      while(var3-- > 0) {
         if (var1[var3] >= 0) {
            var2 = true;
         }
      }

      return var2;
   }

   public void clear() {
      super.clear();
      float[] var1 = this._set;
      byte[] var2 = this._states;

      for(int var3 = var1.length; var3-- > 0; var2[var3] = 0) {
         var1[var3] = this.no_entry_value;
      }

   }

   protected void rehash(int var1) {
      int var2 = this._set.length;
      float[] var3 = this._set;
      byte[] var4 = this._states;
      this._set = new float[var1];
      this._states = new byte[var1];
      int var5 = var2;

      while(var5-- > 0) {
         if (var4[var5] == 1) {
            float var6 = var3[var5];
            this.insertKey(var6);
         }
      }

   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof TFloatSet)) {
         return false;
      } else {
         TFloatSet var2 = (TFloatSet)var1;
         if (var2.size() != this.size()) {
            return false;
         } else {
            int var3 = this._states.length;

            do {
               if (var3-- <= 0) {
                  return true;
               }
            } while(this._states[var3] != 1 || var2.contains(this._set[var3]));

            return false;
         }
      }
   }

   public int hashCode() {
      int var1 = 0;
      int var2 = this._states.length;

      while(var2-- > 0) {
         if (this._states[var2] == 1) {
            var1 += HashFunctions.hash(this._set[var2]);
         }
      }

      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(this._size * 2 + 2);
      var1.append("{");
      int var2 = this._states.length;
      int var3 = 1;

      while(var2-- > 0) {
         if (this._states[var2] == 1) {
            var1.append(this._set[var2]);
            if (var3++ < this._size) {
               var1.append(",");
            }
         }
      }

      var1.append("}");
      return var1.toString();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(1);
      super.writeExternal(var1);
      var1.writeInt(this._size);
      var1.writeFloat(this._loadFactor);
      var1.writeFloat(this.no_entry_value);
      int var2 = this._states.length;

      while(var2-- > 0) {
         if (this._states[var2] == 1) {
            var1.writeFloat(this._set[var2]);
         }
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      super.readExternal(var1);
      int var3 = var1.readInt();
      if (var2 >= 1) {
         this._loadFactor = var1.readFloat();
         this.no_entry_value = var1.readFloat();
         if (this.no_entry_value != 0.0F) {
            Arrays.fill(this._set, this.no_entry_value);
         }
      }

      this.setUp(var3);

      while(var3-- > 0) {
         float var4 = var1.readFloat();
         this.add(var4);
      }

   }

   class TFloatHashIterator extends THashPrimitiveIterator implements TFloatIterator {
      private final TFloatHash _hash;
      final TFloatHashSet this$0;

      public TFloatHashIterator(TFloatHashSet var1, TFloatHash var2) {
         super(var2);
         this.this$0 = var1;
         this._hash = var2;
      }

      public float next() {
         this.moveToNextIndex();
         return this._hash._set[this._index];
      }
   }
}
