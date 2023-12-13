package gnu.trove.impl;

public final class HashFunctions {
   static final boolean $assertionsDisabled = !HashFunctions.class.desiredAssertionStatus();

   public static int hash(double var0) {
      if (!$assertionsDisabled && Double.isNaN(var0)) {
         throw new AssertionError("Values of NaN are not supported.");
      } else {
         long var2 = Double.doubleToLongBits(var0);
         return (int)(var2 ^ var2 >>> 32);
      }
   }

   public static int hash(float var0) {
      if (!$assertionsDisabled && Float.isNaN(var0)) {
         throw new AssertionError("Values of NaN are not supported.");
      } else {
         return Float.floatToIntBits(var0 * 6.6360896E8F);
      }
   }

   public static int hash(int var0) {
      return var0;
   }

   public static int hash(long var0) {
      return (int)(var0 ^ var0 >>> 32);
   }

   public static int hash(Object var0) {
      return var0 == null ? 0 : var0.hashCode();
   }

   public static int fastCeil(float var0) {
      int var1 = (int)var0;
      if (var0 - (float)var1 > 0.0F) {
         ++var1;
      }

      return var1;
   }
}
