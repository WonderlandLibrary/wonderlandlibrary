package gnu.trove.impl.hash;

import gnu.trove.impl.HashFunctions;
import gnu.trove.procedure.TDoubleProcedure;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public abstract class TDoubleByteHash extends TPrimitiveHash {
   static final long serialVersionUID = 1L;
   public transient double[] _set;
   protected double no_entry_key;
   protected byte no_entry_value;
   protected boolean consumeFreeSlot;

   public TDoubleByteHash() {
      this.no_entry_key = 0.0D;
      this.no_entry_value = 0;
   }

   public TDoubleByteHash(int var1) {
      super(var1);
      this.no_entry_key = 0.0D;
      this.no_entry_value = 0;
   }

   public TDoubleByteHash(int var1, float var2) {
      super(var1, var2);
      this.no_entry_key = 0.0D;
      this.no_entry_value = 0;
   }

   public TDoubleByteHash(int var1, float var2, double var3, byte var5) {
      super(var1, var2);
      this.no_entry_key = var3;
      this.no_entry_value = var5;
   }

   public double getNoEntryKey() {
      return this.no_entry_key;
   }

   public byte getNoEntryValue() {
      return this.no_entry_value;
   }

   protected int setUp(int var1) {
      int var2 = super.setUp(var1);
      this._set = new double[var2];
      return var2;
   }

   public boolean contains(double param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean forEach(TDoubleProcedure var1) {
      byte[] var2 = this._states;
      double[] var3 = this._set;
      int var4 = var3.length;

      do {
         if (var4-- <= 0) {
            return true;
         }
      } while(var2[var4] != 1 || var1.execute(var3[var4]));

      return false;
   }

   protected void removeAt(int var1) {
      this._set[var1] = this.no_entry_key;
      super.removeAt(var1);
   }

   int indexRehashed(double var1, int var3, int var4, byte var5) {
      int var6 = this._set.length;
      int var7 = 1 + var4 % (var6 - 2);
      int var8 = var3;

      do {
         var3 -= var7;
         if (var3 < 0) {
            var3 += var6;
         }

         var5 = this._states[var3];
         if (var5 == 0) {
            return -1;
         }

         if (var1 == this._set[var3] && var5 != 2) {
            return var3;
         }
      } while(var3 != var8);

      return -1;
   }

   protected int insertKey(double var1) {
      int var3 = HashFunctions.hash(var1) & Integer.MAX_VALUE;
      int var4 = var3 % this._states.length;
      byte var5 = this._states[var4];
      this.consumeFreeSlot = false;
      if (var5 == 0) {
         this.consumeFreeSlot = true;
         this.insertKeyAt(var4, var1);
         return var4;
      } else {
         return var5 == 1 && this._set[var4] == var1 ? -var4 - 1 : this.insertKeyRehash(var1, var4, var3, var5);
      }
   }

   int insertKeyRehash(double var1, int var3, int var4, byte var5) {
      int var6 = this._set.length;
      int var7 = 1 + var4 % (var6 - 2);
      int var8 = var3;
      int var9 = -1;

      do {
         if (var5 == 2 && var9 == -1) {
            var9 = var3;
         }

         var3 -= var7;
         if (var3 < 0) {
            var3 += var6;
         }

         var5 = this._states[var3];
         if (var5 == 0) {
            if (var9 != -1) {
               this.insertKeyAt(var9, var1);
               return var9;
            }

            this.consumeFreeSlot = true;
            this.insertKeyAt(var3, var1);
            return var3;
         }

         if (var5 == 1 && this._set[var3] == var1) {
            return -var3 - 1;
         }
      } while(var3 != var8);

      if (var9 != -1) {
         this.insertKeyAt(var9, var1);
         return var9;
      } else {
         throw new IllegalStateException("No free or removed slots available. Key set full?!!");
      }
   }

   void insertKeyAt(int var1, double var2) {
      this._set[var1] = var2;
      this._states[var1] = 1;
   }

   protected int XinsertKey(double var1) {
      byte[] var7 = this._states;
      double[] var8 = this._set;
      int var6 = var7.length;
      int var3 = HashFunctions.hash(var1) & Integer.MAX_VALUE;
      int var5 = var3 % var6;
      byte var9 = var7[var5];
      this.consumeFreeSlot = false;
      if (var9 == 0) {
         this.consumeFreeSlot = true;
         var8[var5] = var1;
         var7[var5] = 1;
         return var5;
      } else if (var9 == 1 && var8[var5] == var1) {
         return -var5 - 1;
      } else {
         int var4 = 1 + var3 % (var6 - 2);
         if (var9 != 2) {
            do {
               var5 -= var4;
               if (var5 < 0) {
                  var5 += var6;
               }

               var9 = var7[var5];
            } while(var9 == 1 && var8[var5] != var1);
         }

         if (var9 != 2) {
            if (var9 == 1) {
               return -var5 - 1;
            } else {
               this.consumeFreeSlot = true;
               var8[var5] = var1;
               var7[var5] = 1;
               return var5;
            }
         } else {
            int var10;
            for(var10 = var5; var9 != 0 && (var9 == 2 || var8[var5] != var1); var9 = var7[var5]) {
               var5 -= var4;
               if (var5 < 0) {
                  var5 += var6;
               }
            }

            if (var9 == 1) {
               return -var5 - 1;
            } else {
               var8[var5] = var1;
               var7[var5] = 1;
               return var10;
            }
         }
      }
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(0);
      super.writeExternal(var1);
      var1.writeDouble(this.no_entry_key);
      var1.writeByte(this.no_entry_value);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      super.readExternal(var1);
      this.no_entry_key = var1.readDouble();
      this.no_entry_value = var1.readByte();
   }
}
