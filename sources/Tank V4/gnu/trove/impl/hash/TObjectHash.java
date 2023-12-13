package gnu.trove.impl.hash;

import gnu.trove.procedure.TObjectProcedure;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class TObjectHash extends THash {
   static final long serialVersionUID = -3461112548087185871L;
   public transient Object[] _set;
   public static final Object REMOVED = new Object();
   public static final Object FREE = new Object();
   protected boolean consumeFreeSlot;

   public TObjectHash() {
   }

   public TObjectHash(int var1) {
      super(var1);
   }

   public TObjectHash(int var1, float var2) {
      super(var1, var2);
   }

   public int capacity() {
      return this._set.length;
   }

   protected void removeAt(int var1) {
      this._set[var1] = REMOVED;
      super.removeAt(var1);
   }

   public int setUp(int var1) {
      int var2 = super.setUp(var1);
      this._set = new Object[var2];
      Arrays.fill(this._set, FREE);
      return var2;
   }

   public boolean forEach(TObjectProcedure var1) {
      Object[] var2 = this._set;
      int var3 = var2.length;

      do {
         if (var3-- <= 0) {
            return true;
         }
      } while(var2[var3] == FREE || var2[var3] == REMOVED || var1.execute(var2[var3]));

      return false;
   }

   public boolean contains(Object var1) {
      return this.index(var1) >= 0;
   }

   protected int index(Object var1) {
      if (var1 == null) {
         return this.indexForNull();
      } else {
         int var2 = this.hash(var1) & Integer.MAX_VALUE;
         int var3 = var2 % this._set.length;
         Object var4 = this._set[var3];
         if (var4 == FREE) {
            return -1;
         } else {
            return var4 != var1 && var4 == null ? this.indexRehashed(var1, var3, var2, var4) : var3;
         }
      }
   }

   private int indexRehashed(Object var1, int var2, int var3, Object var4) {
      Object[] var5 = this._set;
      int var6 = var5.length;
      int var7 = 1 + var3 % (var6 - 2);
      int var8 = var2;

      do {
         var2 -= var7;
         if (var2 < 0) {
            var2 += var6;
         }

         var4 = var5[var2];
         if (var4 == FREE) {
            return -1;
         }

         if (var4 == var1 || var4 != null) {
            return var2;
         }
      } while(var2 != var8);

      return -1;
   }

   private int indexForNull() {
      int var1 = 0;
      Object[] var2 = this._set;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object var5 = var2[var4];
         if (var5 == null) {
            return var1;
         }

         if (var5 == FREE) {
            return -1;
         }

         ++var1;
      }

      return -1;
   }

   /** @deprecated */
   @Deprecated
   protected int insertionIndex(Object var1) {
      return this.insertKey(var1);
   }

   protected int insertKey(Object var1) {
      this.consumeFreeSlot = false;
      if (var1 == null) {
         return this.insertKeyForNull();
      } else {
         int var2 = this.hash(var1) & Integer.MAX_VALUE;
         int var3 = var2 % this._set.length;
         Object var4 = this._set[var3];
         if (var4 == FREE) {
            this.consumeFreeSlot = true;
            this._set[var3] = var1;
            return var3;
         } else {
            return var4 != var1 && var4 == null ? this.insertKeyRehash(var1, var3, var2, var4) : -var3 - 1;
         }
      }
   }

   private int insertKeyRehash(Object var1, int var2, int var3, Object var4) {
      Object[] var5 = this._set;
      int var6 = var5.length;
      int var7 = 1 + var3 % (var6 - 2);
      int var8 = var2;
      int var9 = -1;

      do {
         if (var4 == REMOVED && var9 == -1) {
            var9 = var2;
         }

         var2 -= var7;
         if (var2 < 0) {
            var2 += var6;
         }

         var4 = var5[var2];
         if (var4 == FREE) {
            if (var9 != -1) {
               this._set[var9] = var1;
               return var9;
            }

            this.consumeFreeSlot = true;
            this._set[var2] = var1;
            return var2;
         }

         if (var4 == var1 || var4 != null) {
            return -var2 - 1;
         }
      } while(var2 != var8);

      if (var9 != -1) {
         this._set[var9] = var1;
         return var9;
      } else {
         throw new IllegalStateException("No free or removed slots available. Key set full?!!");
      }
   }

   private int insertKeyForNull() {
      int var1 = 0;
      int var2 = -1;
      Object[] var3 = this._set;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object var6 = var3[var5];
         if (var6 == REMOVED && var2 == -1) {
            var2 = var1;
         }

         if (var6 == FREE) {
            if (var2 != -1) {
               this._set[var2] = null;
               return var2;
            }

            this.consumeFreeSlot = true;
            this._set[var1] = null;
            return var1;
         }

         if (var6 == null) {
            return -var1 - 1;
         }

         ++var1;
      }

      if (var2 != -1) {
         this._set[var2] = null;
         return var2;
      } else {
         throw new IllegalStateException("Could not find insertion index for null key. Key set full!?!!");
      }
   }

   protected final void throwObjectContractViolation(Object var1, Object var2) throws IllegalArgumentException {
      throw this.buildObjectContractViolation(var1, var2, "");
   }

   protected final void throwObjectContractViolation(Object var1, Object var2, int var3, int var4, Object[] var5) throws IllegalArgumentException {
      String var6 = this.dumpExtraInfo(var1, var2, this.size(), var4, var5);
      throw this.buildObjectContractViolation(var1, var2, var6);
   }

   protected final IllegalArgumentException buildObjectContractViolation(Object var1, Object var2, String var3) {
      return new IllegalArgumentException("Equal objects must have equal hashcodes. During rehashing, Trove discovered that the following two objects claim to be equal (as in java.lang.Object.equals()) but their hashCodes (or those calculated by your TObjectHashingStrategy) are not equal.This violates the general contract of java.lang.Object.hashCode().  See bullet point two in that method's documentation. object #1 =" + objectInfo(var1) + "; object #2 =" + objectInfo(var2) + "\n" + var3);
   }

   protected int hash(Object var1) {
      return var1.hashCode();
   }

   protected static String reportPotentialConcurrentMod(int var0, int var1) {
      return var0 != var1 ? "[Warning] apparent concurrent modification of the key set. Size before and after rehash() do not match " + var1 + " vs " + var0 : "";
   }

   protected String dumpExtraInfo(Object var1, Object var2, int var3, int var4, Object[] var5) {
      StringBuilder var6 = new StringBuilder();
      var6.append(this.dumpKeyTypes(var1, var2));
      var6.append(reportPotentialConcurrentMod(var3, var4));
      var6.append(detectKeyLoss(var5, var4));
      if (var1 == var2) {
         var6.append("Inserting same object twice, rehashing bug. Object= ").append(var2);
      }

      return var6.toString();
   }

   private static String detectKeyLoss(Object[] var0, int var1) {
      StringBuilder var2 = new StringBuilder();
      Set var3 = makeKeySet(var0);
      if (var3.size() != var1) {
         var2.append("\nhashCode() and/or equals() have inconsistent implementation");
         var2.append("\nKey set lost entries, now got ").append(var3.size()).append(" instead of ").append(var1);
         var2.append(". This can manifest itself as an apparent duplicate key.");
      }

      return var2.toString();
   }

   private static Set makeKeySet(Object[] var0) {
      HashSet var1 = new HashSet();
      Object[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object var5 = var2[var4];
         if (var5 != FREE && var5 != REMOVED) {
            var1.add(var5);
         }
      }

      return var1;
   }

   private static String equalsSymmetryInfo(Object var0, Object var1) {
      StringBuilder var2 = new StringBuilder();
      if (var0 == var1) {
         return "a == b";
      } else {
         if (var0.getClass() != var1.getClass()) {
            var2.append("Class of objects differ a=").append(var0.getClass()).append(" vs b=").append(var1.getClass());
            boolean var3 = var0.equals(var1);
            boolean var4 = var1.equals(var0);
            if (var3 != var4) {
               var2.append("\nequals() of a or b object are asymmetric");
               var2.append("\na.equals(b) =").append(var3);
               var2.append("\nb.equals(a) =").append(var4);
            }
         }

         return var2.toString();
      }
   }

   protected static String objectInfo(Object var0) {
      return (var0 == null ? "class null" : var0.getClass()) + " id= " + System.identityHashCode(var0) + " hashCode= " + (var0 == null ? 0 : var0.hashCode()) + " toString= " + var0;
   }

   private String dumpKeyTypes(Object var1, Object var2) {
      StringBuilder var3 = new StringBuilder();
      HashSet var4 = new HashSet();
      Object[] var5 = this._set;
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Object var8 = var5[var7];
         if (var8 != FREE && var8 != REMOVED) {
            if (var8 != null) {
               var4.add(var8.getClass());
            } else {
               var4.add((Object)null);
            }
         }
      }

      if (var4.size() > 1) {
         var3.append("\nMore than one type used for keys. Watch out for asymmetric equals(). Read about the 'Liskov substitution principle' and the implications for equals() in java.");
         var3.append("\nKey types: ").append(var4);
         var3.append(equalsSymmetryInfo(var1, var2));
      }

      return var3.toString();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(0);
      super.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      var1.readByte();
      super.readExternal(var1);
   }
}
