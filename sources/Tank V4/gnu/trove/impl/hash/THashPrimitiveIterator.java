package gnu.trove.impl.hash;

import gnu.trove.iterator.TPrimitiveIterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public abstract class THashPrimitiveIterator implements TPrimitiveIterator {
   protected final TPrimitiveHash _hash;
   protected int _expectedSize;
   protected int _index;

   public THashPrimitiveIterator(TPrimitiveHash var1) {
      this._hash = var1;
      this._expectedSize = this._hash.size();
      this._index = this._hash.capacity();
   }

   protected final int nextIndex() {
      if (this._expectedSize != this._hash.size()) {
         throw new ConcurrentModificationException();
      } else {
         byte[] var1 = this._hash._states;
         int var2 = this._index;

         while(var2-- > 0 && var1[var2] != 1) {
         }

         return var2;
      }
   }

   public boolean hasNext() {
      return this.nextIndex() >= 0;
   }

   public void remove() {
      if (this._expectedSize != this._hash.size()) {
         throw new ConcurrentModificationException();
      } else {
         this._hash.tempDisableAutoCompaction();
         this._hash.removeAt(this._index);
         this._hash.reenableAutoCompaction(false);
         --this._expectedSize;
      }
   }

   protected final void moveToNextIndex() {
      if ((this._index = this.nextIndex()) < 0) {
         throw new NoSuchElementException();
      }
   }
}
