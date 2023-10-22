package com.sun.javafx.css;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.css.PseudoClass;

public final class PseudoClassState extends BitSet {
   static final Map pseudoClassMap = new HashMap(64);
   static final List pseudoClasses = new ArrayList();

   public PseudoClassState() {
   }

   PseudoClassState(List var1) {
      int var2 = var1 != null ? var1.size() : 0;

      for(int var3 = 0; var3 < var2; ++var3) {
         PseudoClass var4 = getPseudoClass((String)var1.get(var3));
         this.add(var4);
      }

   }

   public Object[] toArray() {
      return this.toArray(new PseudoClass[this.size()]);
   }

   public Object[] toArray(Object[] var1) {
      if (var1.length < this.size()) {
         var1 = (Object[])(new PseudoClass[this.size()]);
      }

      int var2 = 0;

      while(var2 < this.getBits().length) {
         long var3 = this.getBits()[var2];

         for(int var5 = 0; var5 < 64; ++var5) {
            long var6 = 1L << var5;
            if ((var3 & var6) == var6) {
               int var8 = var2 * 64 + var5;
               PseudoClass var9 = getPseudoClass(var8);
               var1[var2++] = var9;
            }
         }
      }

      return var1;
   }

   public String toString() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         var1.add(((PseudoClass)var2.next()).getPseudoClassName());
      }

      return var1.toString();
   }

   protected PseudoClass cast(Object var1) {
      if (var1 == null) {
         throw new NullPointerException("null arg");
      } else {
         PseudoClass var2 = (PseudoClass)var1;
         return var2;
      }
   }

   protected PseudoClass getT(int var1) {
      return getPseudoClass(var1);
   }

   protected int getIndex(PseudoClass var1) {
      if (var1 instanceof PseudoClassImpl) {
         return ((PseudoClassImpl)var1).getIndex();
      } else {
         String var2 = var1.getPseudoClassName();
         Integer var3 = (Integer)pseudoClassMap.get(var2);
         if (var3 == null) {
            var3 = pseudoClasses.size();
            pseudoClasses.add(new PseudoClassImpl(var2, var3));
            pseudoClassMap.put(var2, var3);
         }

         return var3;
      }
   }

   public static PseudoClass getPseudoClass(String var0) {
      if (var0 != null && !var0.trim().isEmpty()) {
         Object var1 = null;
         Integer var2 = (Integer)pseudoClassMap.get(var0);
         int var3 = var2 != null ? var2 : -1;
         int var4 = pseudoClasses.size();

         assert var3 < var4;

         if (var3 != -1 && var3 < var4) {
            var1 = (PseudoClass)pseudoClasses.get(var3);
         }

         if (var1 == null) {
            var1 = new PseudoClassImpl(var0, var4);
            pseudoClasses.add(var1);
            pseudoClassMap.put(var0, var4);
         }

         return (PseudoClass)var1;
      } else {
         throw new IllegalArgumentException("pseudoClass cannot be null or empty String");
      }
   }

   static PseudoClass getPseudoClass(int var0) {
      return 0 <= var0 && var0 < pseudoClasses.size() ? (PseudoClass)pseudoClasses.get(var0) : null;
   }
}
