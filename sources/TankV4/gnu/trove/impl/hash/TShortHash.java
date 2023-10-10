package gnu.trove.impl.hash;

import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.procedure.TShortProcedure;
import java.util.Arrays;

public abstract class TShortHash extends TPrimitiveHash {
   static final long serialVersionUID = 1L;
   public transient short[] _set;
   protected short no_entry_value;
   protected boolean consumeFreeSlot;

   public TShortHash() {
      this.no_entry_value = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
      if (this.no_entry_value != 0) {
         Arrays.fill(this._set, this.no_entry_value);
      }

   }

   public TShortHash(int var1) {
      super(var1);
      this.no_entry_value = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
      if (this.no_entry_value != 0) {
         Arrays.fill(this._set, this.no_entry_value);
      }

   }

   public TShortHash(int var1, float var2) {
      super(var1, var2);
      this.no_entry_value = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
      if (this.no_entry_value != 0) {
         Arrays.fill(this._set, this.no_entry_value);
      }

   }

   public TShortHash(int var1, float var2, short var3) {
      super(var1, var2);
      this.no_entry_value = var3;
      if (var3 != 0) {
         Arrays.fill(this._set, var3);
      }

   }

   public short getNoEntryValue() {
      return this.no_entry_value;
   }

   protected int setUp(int var1) {
      int var2 = super.setUp(var1);
      this._set = new short[var2];
      return var2;
   }

   public boolean contains(short var1) {
      return this.index(var1) >= 0;
   }

   public boolean forEach(TShortProcedure var1) {
      byte[] var2 = this._states;
      short[] var3 = this._set;
      int var4 = var3.length;

      do {
         if (var4-- <= 0) {
            return true;
         }
      } while(var2[var4] != 1 || var1.execute(var3[var4]));

      return false;
   }

   protected void removeAt(int var1) {
      this._set[var1] = this.no_entry_value;
      super.removeAt(var1);
   }

   protected int index(short var1) {
      byte[] var6 = this._states;
      short[] var7 = this._set;
      int var5 = var6.length;
      int var2 = HashFunctions.hash(var1) & Integer.MAX_VALUE;
      int var4 = var2 % var5;
      byte var8 = var6[var4];
      if (var8 == 0) {
         return -1;
      } else {
         return var8 == 1 && var7[var4] == var1 ? var4 : this.indexRehashed(var1, var4, var2, var8);
      }
   }

   int indexRehashed(short var1, int var2, int var3, byte var4) {
      int var5 = this._set.length;
      int var6 = 1 + var3 % (var5 - 2);
      int var7 = var2;

      do {
         var2 -= var6;
         if (var2 < 0) {
            var2 += var5;
         }

         var4 = this._states[var2];
         if (var4 == 0) {
            return -1;
         }

         if (var1 == this._set[var2] && var4 != 2) {
            return var2;
         }
      } while(var2 != var7);

      return -1;
   }

   protected int insertKey(short var1) {
      int var2 = HashFunctions.hash(var1) & Integer.MAX_VALUE;
      int var3 = var2 % this._states.length;
      byte var4 = this._states[var3];
      this.consumeFreeSlot = false;
      if (var4 == 0) {
         this.consumeFreeSlot = true;
         this.insertKeyAt(var3, var1);
         return var3;
      } else {
         return var4 == 1 && this._set[var3] == var1 ? -var3 - 1 : this.insertKeyRehash(var1, var3, var2, var4);
      }
   }

   int insertKeyRehash(short var1, int var2, int var3, byte var4) {
      int var5 = this._set.length;
      int var6 = 1 + var3 % (var5 - 2);
      int var7 = var2;
      int var8 = -1;

      do {
         if (var4 == 2 && var8 == -1) {
            var8 = var2;
         }

         var2 -= var6;
         if (var2 < 0) {
            var2 += var5;
         }

         var4 = this._states[var2];
         if (var4 == 0) {
            if (var8 != -1) {
               this.insertKeyAt(var8, var1);
               return var8;
            }

            this.consumeFreeSlot = true;
            this.insertKeyAt(var2, var1);
            return var2;
         }

         if (var4 == 1 && this._set[var2] == var1) {
            return -var2 - 1;
         }
      } while(var2 != var7);

      if (var8 != -1) {
         this.insertKeyAt(var8, var1);
         return var8;
      } else {
         throw new IllegalStateException("No free or removed slots available. Key set full?!!");
      }
   }

   void insertKeyAt(int var1, short var2) {
      this._set[var1] = var2;
      this._states[var1] = 1;
   }
}
