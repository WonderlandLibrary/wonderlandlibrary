package com.ibm.icu.text;

import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.util.ULocale;
import java.lang.ref.SoftReference;
import java.text.CharacterIterator;
import java.util.Locale;
import java.util.MissingResourceException;

public abstract class BreakIterator implements Cloneable {
   private static final boolean DEBUG = ICUDebug.enabled("breakiterator");
   public static final int DONE = -1;
   public static final int KIND_CHARACTER = 0;
   public static final int KIND_WORD = 1;
   public static final int KIND_LINE = 2;
   public static final int KIND_SENTENCE = 3;
   public static final int KIND_TITLE = 4;
   private static final int KIND_COUNT = 5;
   private static final SoftReference[] iterCache = new SoftReference[5];
   private static BreakIterator.BreakIteratorServiceShim shim;
   private ULocale validLocale;
   private ULocale actualLocale;

   protected BreakIterator() {
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException();
      }
   }

   public abstract int first();

   public abstract int last();

   public abstract int next(int var1);

   public abstract int next();

   public abstract int previous();

   public abstract int following(int var1);

   public int preceding(int var1) {
      int var2;
      for(var2 = this.following(var1); var2 >= var1 && var2 != -1; var2 = this.previous()) {
      }

      return var2;
   }

   public boolean isBoundary(int var1) {
      if (var1 == 0) {
         return true;
      } else {
         return this.following(var1 - 1) == var1;
      }
   }

   public abstract int current();

   public abstract CharacterIterator getText();

   public void setText(String var1) {
      this.setText((CharacterIterator)(new java.text.StringCharacterIterator(var1)));
   }

   public abstract void setText(CharacterIterator var1);

   public static BreakIterator getWordInstance() {
      return getWordInstance(ULocale.getDefault());
   }

   public static BreakIterator getWordInstance(Locale var0) {
      return getBreakInstance(ULocale.forLocale(var0), 1);
   }

   public static BreakIterator getWordInstance(ULocale var0) {
      return getBreakInstance(var0, 1);
   }

   public static BreakIterator getLineInstance() {
      return getLineInstance(ULocale.getDefault());
   }

   public static BreakIterator getLineInstance(Locale var0) {
      return getBreakInstance(ULocale.forLocale(var0), 2);
   }

   public static BreakIterator getLineInstance(ULocale var0) {
      return getBreakInstance(var0, 2);
   }

   public static BreakIterator getCharacterInstance() {
      return getCharacterInstance(ULocale.getDefault());
   }

   public static BreakIterator getCharacterInstance(Locale var0) {
      return getBreakInstance(ULocale.forLocale(var0), 0);
   }

   public static BreakIterator getCharacterInstance(ULocale var0) {
      return getBreakInstance(var0, 0);
   }

   public static BreakIterator getSentenceInstance() {
      return getSentenceInstance(ULocale.getDefault());
   }

   public static BreakIterator getSentenceInstance(Locale var0) {
      return getBreakInstance(ULocale.forLocale(var0), 3);
   }

   public static BreakIterator getSentenceInstance(ULocale var0) {
      return getBreakInstance(var0, 3);
   }

   public static BreakIterator getTitleInstance() {
      return getTitleInstance(ULocale.getDefault());
   }

   public static BreakIterator getTitleInstance(Locale var0) {
      return getBreakInstance(ULocale.forLocale(var0), 4);
   }

   public static BreakIterator getTitleInstance(ULocale var0) {
      return getBreakInstance(var0, 4);
   }

   public static Object registerInstance(BreakIterator var0, Locale var1, int var2) {
      return registerInstance(var0, ULocale.forLocale(var1), var2);
   }

   public static Object registerInstance(BreakIterator var0, ULocale var1, int var2) {
      if (iterCache[var2] != null) {
         BreakIterator.BreakIteratorCache var3 = (BreakIterator.BreakIteratorCache)iterCache[var2].get();
         if (var3 != null && var3.getLocale().equals(var1)) {
            iterCache[var2] = null;
         }
      }

      return getShim().registerInstance(var0, var1, var2);
   }

   public static boolean unregister(Object var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("registry key must not be null");
      } else if (shim == null) {
         return false;
      } else {
         for(int var1 = 0; var1 < 5; ++var1) {
            iterCache[var1] = null;
         }

         return shim.unregister(var0);
      }
   }

   /** @deprecated */
   public static BreakIterator getBreakInstance(ULocale var0, int var1) {
      if (iterCache[var1] != null) {
         BreakIterator.BreakIteratorCache var2 = (BreakIterator.BreakIteratorCache)iterCache[var1].get();
         if (var2 != null && var2.getLocale().equals(var0)) {
            return var2.createBreakInstance();
         }
      }

      BreakIterator var5 = getShim().createBreakIterator(var0, var1);
      BreakIterator.BreakIteratorCache var3 = new BreakIterator.BreakIteratorCache(var0, var5);
      iterCache[var1] = new SoftReference(var3);
      if (var5 instanceof RuleBasedBreakIterator) {
         RuleBasedBreakIterator var4 = (RuleBasedBreakIterator)var5;
         var4.setBreakType(var1);
      }

      return var5;
   }

   public static synchronized Locale[] getAvailableLocales() {
      return getShim().getAvailableLocales();
   }

   public static synchronized ULocale[] getAvailableULocales() {
      return getShim().getAvailableULocales();
   }

   private static BreakIterator.BreakIteratorServiceShim getShim() {
      if (shim == null) {
         try {
            Class var0 = Class.forName("com.ibm.icu.text.BreakIteratorFactory");
            shim = (BreakIterator.BreakIteratorServiceShim)var0.newInstance();
         } catch (MissingResourceException var1) {
            throw var1;
         } catch (Exception var2) {
            if (DEBUG) {
               var2.printStackTrace();
            }

            throw new RuntimeException(var2.getMessage());
         }
      }

      return shim;
   }

   public final ULocale getLocale(ULocale.Type var1) {
      return var1 == ULocale.ACTUAL_LOCALE ? this.actualLocale : this.validLocale;
   }

   final void setLocale(ULocale var1, ULocale var2) {
      if (var1 == null != (var2 == null)) {
         throw new IllegalArgumentException();
      } else {
         this.validLocale = var1;
         this.actualLocale = var2;
      }
   }

   abstract static class BreakIteratorServiceShim {
      public abstract Object registerInstance(BreakIterator var1, ULocale var2, int var3);

      public abstract boolean unregister(Object var1);

      public abstract Locale[] getAvailableLocales();

      public abstract ULocale[] getAvailableULocales();

      public abstract BreakIterator createBreakIterator(ULocale var1, int var2);
   }

   private static final class BreakIteratorCache {
      private BreakIterator iter;
      private ULocale where;

      BreakIteratorCache(ULocale var1, BreakIterator var2) {
         this.where = var1;
         this.iter = (BreakIterator)var2.clone();
      }

      ULocale getLocale() {
         return this.where;
      }

      BreakIterator createBreakInstance() {
         return (BreakIterator)this.iter.clone();
      }
   }
}
