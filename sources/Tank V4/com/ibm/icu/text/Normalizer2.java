package com.ibm.icu.text;

import com.ibm.icu.impl.Norm2AllModes;
import java.io.InputStream;

public abstract class Normalizer2 {
   public static Normalizer2 getNFCInstance() {
      return Norm2AllModes.getNFCInstance().comp;
   }

   public static Normalizer2 getNFDInstance() {
      return Norm2AllModes.getNFCInstance().decomp;
   }

   public static Normalizer2 getNFKCInstance() {
      return Norm2AllModes.getNFKCInstance().comp;
   }

   public static Normalizer2 getNFKDInstance() {
      return Norm2AllModes.getNFKCInstance().decomp;
   }

   public static Normalizer2 getNFKCCasefoldInstance() {
      return Norm2AllModes.getNFKC_CFInstance().comp;
   }

   public static Normalizer2 getInstance(InputStream var0, String var1, Normalizer2.Mode var2) {
      Norm2AllModes var3 = Norm2AllModes.getInstance(var0, var1);
      switch(var2) {
      case COMPOSE:
         return var3.comp;
      case DECOMPOSE:
         return var3.decomp;
      case FCD:
         return var3.fcd;
      case COMPOSE_CONTIGUOUS:
         return var3.fcc;
      default:
         return null;
      }
   }

   public String normalize(CharSequence var1) {
      if (var1 instanceof String) {
         int var2 = this.spanQuickCheckYes(var1);
         if (var2 == var1.length()) {
            return (String)var1;
         } else {
            StringBuilder var3 = (new StringBuilder(var1.length())).append(var1, 0, var2);
            return this.normalizeSecondAndAppend(var3, var1.subSequence(var2, var1.length())).toString();
         }
      } else {
         return this.normalize(var1, new StringBuilder(var1.length())).toString();
      }
   }

   public abstract StringBuilder normalize(CharSequence var1, StringBuilder var2);

   public abstract Appendable normalize(CharSequence var1, Appendable var2);

   public abstract StringBuilder normalizeSecondAndAppend(StringBuilder var1, CharSequence var2);

   public abstract StringBuilder append(StringBuilder var1, CharSequence var2);

   public abstract String getDecomposition(int var1);

   public String getRawDecomposition(int var1) {
      return null;
   }

   public int composePair(int var1, int var2) {
      return -1;
   }

   public int getCombiningClass(int var1) {
      return 0;
   }

   public abstract boolean isNormalized(CharSequence var1);

   public abstract Normalizer.QuickCheckResult quickCheck(CharSequence var1);

   public abstract int spanQuickCheckYes(CharSequence var1);

   public abstract boolean hasBoundaryBefore(int var1);

   public abstract boolean hasBoundaryAfter(int var1);

   public abstract boolean isInert(int var1);

   /** @deprecated */
   protected Normalizer2() {
   }

   public static enum Mode {
      COMPOSE,
      DECOMPOSE,
      FCD,
      COMPOSE_CONTIGUOUS;

      private static final Normalizer2.Mode[] $VALUES = new Normalizer2.Mode[]{COMPOSE, DECOMPOSE, FCD, COMPOSE_CONTIGUOUS};
   }
}
