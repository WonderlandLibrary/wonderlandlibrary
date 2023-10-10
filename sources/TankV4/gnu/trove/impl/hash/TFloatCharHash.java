package gnu.trove.impl.hash;

import gnu.trove.impl.HashFunctions;
import gnu.trove.procedure.TFloatProcedure;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public abstract class TFloatCharHash extends TPrimitiveHash {
   static final long serialVersionUID = 1L;
   public transient float[] _set;
   protected float no_entry_key;
   protected char no_entry_value;
   protected boolean consumeFreeSlot;

   public TFloatCharHash() {
      this.no_entry_key = 0.0F;
      this.no_entry_value = 0;
   }

   public TFloatCharHash(int var1) {
      super(var1);
      this.no_entry_key = 0.0F;
      this.no_entry_value = 0;
   }

   public TFloatCharHash(int var1, float var2) {
      super(var1, var2);
      this.no_entry_key = 0.0F;
      this.no_entry_value = 0;
   }

   public TFloatCharHash(int var1, float var2, float var3, char var4) {
      super(var1, var2);
      this.no_entry_key = var3;
      this.no_entry_value = var4;
   }

   public float getNoEntryKey() {
      return this.no_entry_key;
   }

   public char getNoEntryValue() {
      return this.no_entry_value;
   }

   protected int setUp(int var1) {
      int var2 = super.setUp(var1);
      this._set = new float[var2];
      return var2;
   }

   public boolean contains(float var1) {
      return this >= var1;
   }

   public boolean forEach(TFloatProcedure var1) {
      byte[] var2 = this._states;
      float[] var3 = this._set;
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

   int indexRehashed(float var1, int var2, int var3, byte var4) {
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

   protected int insertKey(float var1) {
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

   int insertKeyRehash(float var1, int var2, int var3, byte var4) {
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

   void insertKeyAt(int var1, float var2) {
      this._set[var1] = var2;
      this._states[var1] = 1;
   }

   protected int XinsertKey(float var1) {
      byte[] var6 = this._states;
      float[] var7 = this._set;
      int var5 = var6.length;
      int var2 = HashFunctions.hash(var1) & Integer.MAX_VALUE;
      int var4 = var2 % var5;
      byte var8 = var6[var4];
      this.consumeFreeSlot = false;
      if (var8 == 0) {
         this.consumeFreeSlot = true;
         var7[var4] = var1;
         var6[var4] = 1;
         return var4;
      } else if (var8 == 1 && var7[var4] == var1) {
         return -var4 - 1;
      } else {
         int var3 = 1 + var2 % (var5 - 2);
         if (var8 != 2) {
            do {
               var4 -= var3;
               if (var4 < 0) {
                  var4 += var5;
               }

               var8 = var6[var4];
            } while(var8 == 1 && var7[var4] != var1);
         }

         if (var8 != 2) {
            if (var8 == 1) {
               return -var4 - 1;
            } else {
               this.consumeFreeSlot = true;
               var7[var4] = var1;
               var6[var4] = 1;
               return var4;
            }
         } else {
            int var9;
            for(var9 = var4; var8 != 0 && (var8 == 2 || var7[var4] != var1); var8 = var6[var4]) {
               var4 -= var3;
               if (var4 < 0) {
                  var4 += var5;
               }
            }

            if (var8 == 1) {
               return -var4 - 1;
            } else {
               var7[var4] = var1;
               var6[var4] = 1;
               return var9;
            }
         }
      }
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(0);
      super.writeExternal(var1);
      var1.writeFloat(this.no_entry_key);
      var1.writeChar(this.no_entry_value);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      super.readExternal(var1);
      this.no_entry_key = var1.readFloat();
      this.no_entry_value = var1.readChar();
   }
}
