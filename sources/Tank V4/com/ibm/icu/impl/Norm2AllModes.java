package com.ibm.icu.impl;

import com.ibm.icu.text.Normalizer;
import com.ibm.icu.text.Normalizer2;
import java.io.IOException;
import java.io.InputStream;

public final class Norm2AllModes {
   public final Normalizer2Impl impl;
   public final Norm2AllModes.ComposeNormalizer2 comp;
   public final Norm2AllModes.DecomposeNormalizer2 decomp;
   public final Norm2AllModes.FCDNormalizer2 fcd;
   public final Norm2AllModes.ComposeNormalizer2 fcc;
   private static CacheBase cache = new SoftCache() {
      protected Norm2AllModes createInstance(String var1, InputStream var2) {
         Normalizer2Impl var3;
         if (var2 == null) {
            var3 = (new Normalizer2Impl()).load("data/icudt51b/" + var1 + ".nrm");
         } else {
            var3 = (new Normalizer2Impl()).load(var2);
         }

         return new Norm2AllModes(var3);
      }

      protected Object createInstance(Object var1, Object var2) {
         return this.createInstance((String)var1, (InputStream)var2);
      }
   };
   public static final Norm2AllModes.NoopNormalizer2 NOOP_NORMALIZER2 = new Norm2AllModes.NoopNormalizer2();

   private Norm2AllModes(Normalizer2Impl var1) {
      this.impl = var1;
      this.comp = new Norm2AllModes.ComposeNormalizer2(var1, false);
      this.decomp = new Norm2AllModes.DecomposeNormalizer2(var1);
      this.fcd = new Norm2AllModes.FCDNormalizer2(var1);
      this.fcc = new Norm2AllModes.ComposeNormalizer2(var1, true);
   }

   private static Norm2AllModes getInstanceFromSingleton(Norm2AllModes.Norm2AllModesSingleton var0) {
      if (Norm2AllModes.Norm2AllModesSingleton.access$000(var0) != null) {
         throw Norm2AllModes.Norm2AllModesSingleton.access$000(var0);
      } else {
         return Norm2AllModes.Norm2AllModesSingleton.access$100(var0);
      }
   }

   public static Norm2AllModes getNFCInstance() {
      return getInstanceFromSingleton(Norm2AllModes.NFCSingleton.access$200());
   }

   public static Norm2AllModes getNFKCInstance() {
      return getInstanceFromSingleton(Norm2AllModes.NFKCSingleton.access$300());
   }

   public static Norm2AllModes getNFKC_CFInstance() {
      return getInstanceFromSingleton(Norm2AllModes.NFKC_CFSingleton.access$400());
   }

   public static Norm2AllModes.Normalizer2WithImpl getN2WithImpl(int var0) {
      switch(var0) {
      case 0:
         return getNFCInstance().decomp;
      case 1:
         return getNFKCInstance().decomp;
      case 2:
         return getNFCInstance().comp;
      case 3:
         return getNFKCInstance().comp;
      default:
         return null;
      }
   }

   public static Norm2AllModes getInstance(InputStream var0, String var1) {
      if (var0 == null) {
         Norm2AllModes.Norm2AllModesSingleton var2;
         if (var1.equals("nfc")) {
            var2 = Norm2AllModes.NFCSingleton.access$200();
         } else if (var1.equals("nfkc")) {
            var2 = Norm2AllModes.NFKCSingleton.access$300();
         } else if (var1.equals("nfkc_cf")) {
            var2 = Norm2AllModes.NFKC_CFSingleton.access$400();
         } else {
            var2 = null;
         }

         if (var2 != null) {
            if (Norm2AllModes.Norm2AllModesSingleton.access$000(var2) != null) {
               throw Norm2AllModes.Norm2AllModesSingleton.access$000(var2);
            }

            return Norm2AllModes.Norm2AllModesSingleton.access$100(var2);
         }
      }

      return (Norm2AllModes)cache.getInstance(var1, var0);
   }

   public static Normalizer2 getFCDNormalizer2() {
      return getNFCInstance().fcd;
   }

   Norm2AllModes(Normalizer2Impl var1, Object var2) {
      this(var1);
   }

   private static final class NFKC_CFSingleton {
      private static final Norm2AllModes.Norm2AllModesSingleton INSTANCE = new Norm2AllModes.Norm2AllModesSingleton("nfkc_cf");

      static Norm2AllModes.Norm2AllModesSingleton access$400() {
         return INSTANCE;
      }
   }

   private static final class NFKCSingleton {
      private static final Norm2AllModes.Norm2AllModesSingleton INSTANCE = new Norm2AllModes.Norm2AllModesSingleton("nfkc");

      static Norm2AllModes.Norm2AllModesSingleton access$300() {
         return INSTANCE;
      }
   }

   private static final class NFCSingleton {
      private static final Norm2AllModes.Norm2AllModesSingleton INSTANCE = new Norm2AllModes.Norm2AllModesSingleton("nfc");

      static Norm2AllModes.Norm2AllModesSingleton access$200() {
         return INSTANCE;
      }
   }

   private static final class Norm2AllModesSingleton {
      private Norm2AllModes allModes;
      private RuntimeException exception;

      private Norm2AllModesSingleton(String var1) {
         try {
            Normalizer2Impl var2 = (new Normalizer2Impl()).load("data/icudt51b/" + var1 + ".nrm");
            this.allModes = new Norm2AllModes(var2);
         } catch (RuntimeException var3) {
            this.exception = var3;
         }

      }

      static RuntimeException access$000(Norm2AllModes.Norm2AllModesSingleton var0) {
         return var0.exception;
      }

      static Norm2AllModes access$100(Norm2AllModes.Norm2AllModesSingleton var0) {
         return var0.allModes;
      }

      Norm2AllModesSingleton(String var1, Object var2) {
         this(var1);
      }
   }

   public static final class FCDNormalizer2 extends Norm2AllModes.Normalizer2WithImpl {
      public FCDNormalizer2(Normalizer2Impl var1) {
         super(var1);
      }

      protected void normalize(CharSequence var1, Normalizer2Impl.ReorderingBuffer var2) {
         this.impl.makeFCD(var1, 0, var1.length(), var2);
      }

      protected void normalizeAndAppend(CharSequence var1, boolean var2, Normalizer2Impl.ReorderingBuffer var3) {
         this.impl.makeFCDAndAppend(var1, var2, var3);
      }

      public int spanQuickCheckYes(CharSequence var1) {
         return this.impl.makeFCD(var1, 0, var1.length(), (Normalizer2Impl.ReorderingBuffer)null);
      }

      public int getQuickCheck(int var1) {
         return this.impl.isDecompYes(this.impl.getNorm16(var1)) ? 1 : 0;
      }

      public boolean hasBoundaryBefore(int var1) {
         return this.impl.hasFCDBoundaryBefore(var1);
      }

      public boolean hasBoundaryAfter(int var1) {
         return this.impl.hasFCDBoundaryAfter(var1);
      }

      public boolean isInert(int var1) {
         return this.impl.isFCDInert(var1);
      }
   }

   public static final class ComposeNormalizer2 extends Norm2AllModes.Normalizer2WithImpl {
      private final boolean onlyContiguous;

      public ComposeNormalizer2(Normalizer2Impl var1, boolean var2) {
         super(var1);
         this.onlyContiguous = var2;
      }

      protected void normalize(CharSequence var1, Normalizer2Impl.ReorderingBuffer var2) {
         this.impl.compose(var1, 0, var1.length(), this.onlyContiguous, true, var2);
      }

      protected void normalizeAndAppend(CharSequence var1, boolean var2, Normalizer2Impl.ReorderingBuffer var3) {
         this.impl.composeAndAppend(var1, var2, this.onlyContiguous, var3);
      }

      public boolean isNormalized(CharSequence var1) {
         return this.impl.compose(var1, 0, var1.length(), this.onlyContiguous, false, new Normalizer2Impl.ReorderingBuffer(this.impl, new StringBuilder(), 5));
      }

      public Normalizer.QuickCheckResult quickCheck(CharSequence var1) {
         int var2 = this.impl.composeQuickCheck(var1, 0, var1.length(), this.onlyContiguous, false);
         if ((var2 & 1) != 0) {
            return Normalizer.MAYBE;
         } else {
            return var2 >>> 1 == var1.length() ? Normalizer.YES : Normalizer.NO;
         }
      }

      public int spanQuickCheckYes(CharSequence var1) {
         return this.impl.composeQuickCheck(var1, 0, var1.length(), this.onlyContiguous, true) >>> 1;
      }

      public int getQuickCheck(int var1) {
         return this.impl.getCompQuickCheck(this.impl.getNorm16(var1));
      }

      public boolean hasBoundaryBefore(int var1) {
         return this.impl.hasCompBoundaryBefore(var1);
      }

      public boolean hasBoundaryAfter(int var1) {
         return this.impl.hasCompBoundaryAfter(var1, this.onlyContiguous, false);
      }

      public boolean isInert(int var1) {
         return this.impl.hasCompBoundaryAfter(var1, this.onlyContiguous, true);
      }
   }

   public static final class DecomposeNormalizer2 extends Norm2AllModes.Normalizer2WithImpl {
      public DecomposeNormalizer2(Normalizer2Impl var1) {
         super(var1);
      }

      protected void normalize(CharSequence var1, Normalizer2Impl.ReorderingBuffer var2) {
         this.impl.decompose(var1, 0, var1.length(), var2);
      }

      protected void normalizeAndAppend(CharSequence var1, boolean var2, Normalizer2Impl.ReorderingBuffer var3) {
         this.impl.decomposeAndAppend(var1, var2, var3);
      }

      public int spanQuickCheckYes(CharSequence var1) {
         return this.impl.decompose(var1, 0, var1.length(), (Normalizer2Impl.ReorderingBuffer)null);
      }

      public int getQuickCheck(int var1) {
         return this.impl.isDecompYes(this.impl.getNorm16(var1)) ? 1 : 0;
      }

      public boolean hasBoundaryBefore(int var1) {
         return this.impl.hasDecompBoundary(var1, true);
      }

      public boolean hasBoundaryAfter(int var1) {
         return this.impl.hasDecompBoundary(var1, false);
      }

      public boolean isInert(int var1) {
         return this.impl.isDecompInert(var1);
      }
   }

   public abstract static class Normalizer2WithImpl extends Normalizer2 {
      public final Normalizer2Impl impl;

      public Normalizer2WithImpl(Normalizer2Impl var1) {
         this.impl = var1;
      }

      public StringBuilder normalize(CharSequence var1, StringBuilder var2) {
         if (var2 == var1) {
            throw new IllegalArgumentException();
         } else {
            var2.setLength(0);
            this.normalize(var1, new Normalizer2Impl.ReorderingBuffer(this.impl, var2, var1.length()));
            return var2;
         }
      }

      public Appendable normalize(CharSequence var1, Appendable var2) {
         if (var2 == var1) {
            throw new IllegalArgumentException();
         } else {
            Normalizer2Impl.ReorderingBuffer var3 = new Normalizer2Impl.ReorderingBuffer(this.impl, var2, var1.length());
            this.normalize(var1, var3);
            var3.flush();
            return var2;
         }
      }

      protected abstract void normalize(CharSequence var1, Normalizer2Impl.ReorderingBuffer var2);

      public StringBuilder normalizeSecondAndAppend(StringBuilder var1, CharSequence var2) {
         return this.normalizeSecondAndAppend(var1, var2, true);
      }

      public StringBuilder append(StringBuilder var1, CharSequence var2) {
         return this.normalizeSecondAndAppend(var1, var2, false);
      }

      public StringBuilder normalizeSecondAndAppend(StringBuilder var1, CharSequence var2, boolean var3) {
         if (var1 == var2) {
            throw new IllegalArgumentException();
         } else {
            this.normalizeAndAppend(var2, var3, new Normalizer2Impl.ReorderingBuffer(this.impl, var1, var1.length() + var2.length()));
            return var1;
         }
      }

      protected abstract void normalizeAndAppend(CharSequence var1, boolean var2, Normalizer2Impl.ReorderingBuffer var3);

      public String getDecomposition(int var1) {
         return this.impl.getDecomposition(var1);
      }

      public String getRawDecomposition(int var1) {
         return this.impl.getRawDecomposition(var1);
      }

      public int composePair(int var1, int var2) {
         return this.impl.composePair(var1, var2);
      }

      public int getCombiningClass(int var1) {
         return this.impl.getCC(this.impl.getNorm16(var1));
      }

      public Normalizer.QuickCheckResult quickCheck(CharSequence var1) {
         return this == var1 ? Normalizer.YES : Normalizer.NO;
      }

      public int getQuickCheck(int var1) {
         return 1;
      }
   }

   public static final class NoopNormalizer2 extends Normalizer2 {
      public StringBuilder normalize(CharSequence var1, StringBuilder var2) {
         if (var2 != var1) {
            var2.setLength(0);
            return var2.append(var1);
         } else {
            throw new IllegalArgumentException();
         }
      }

      public Appendable normalize(CharSequence var1, Appendable var2) {
         if (var2 != var1) {
            try {
               return var2.append(var1);
            } catch (IOException var4) {
               throw new RuntimeException(var4);
            }
         } else {
            throw new IllegalArgumentException();
         }
      }

      public StringBuilder normalizeSecondAndAppend(StringBuilder var1, CharSequence var2) {
         if (var1 != var2) {
            return var1.append(var2);
         } else {
            throw new IllegalArgumentException();
         }
      }

      public StringBuilder append(StringBuilder var1, CharSequence var2) {
         if (var1 != var2) {
            return var1.append(var2);
         } else {
            throw new IllegalArgumentException();
         }
      }

      public String getDecomposition(int var1) {
         return null;
      }

      public boolean isNormalized(CharSequence var1) {
         return true;
      }

      public Normalizer.QuickCheckResult quickCheck(CharSequence var1) {
         return Normalizer.YES;
      }

      public int spanQuickCheckYes(CharSequence var1) {
         return var1.length();
      }

      public boolean hasBoundaryBefore(int var1) {
         return true;
      }

      public boolean hasBoundaryAfter(int var1) {
         return true;
      }

      public boolean isInert(int var1) {
         return true;
      }
   }
}
