package com.sun.javafx.css;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class StyleClassSet extends BitSet {
   static final Map styleClassMap = new HashMap(64);
   static final List styleClasses = new ArrayList();

   public StyleClassSet() {
   }

   StyleClassSet(List var1) {
      int var2 = var1 != null ? var1.size() : 0;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = (String)var1.get(var3);
         if (var4 != null && !var4.isEmpty()) {
            StyleClass var5 = getStyleClass(var4);
            this.add(var5);
         }
      }

   }

   public Object[] toArray() {
      return this.toArray(new StyleClass[this.size()]);
   }

   public Object[] toArray(Object[] var1) {
      if (var1.length < this.size()) {
         var1 = (Object[])(new StyleClass[this.size()]);
      }

      int var2 = 0;

      while(var2 < this.getBits().length) {
         long var3 = this.getBits()[var2];

         for(int var5 = 0; var5 < 64; ++var5) {
            long var6 = 1L << var5;
            if ((var3 & var6) == var6) {
               int var8 = var2 * 64 + var5;
               StyleClass var9 = getStyleClass(var8);
               var1[var2++] = var9;
            }
         }
      }

      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("style-classes: [");
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         var1.append(((StyleClass)var2.next()).getStyleClassName());
         if (var2.hasNext()) {
            var1.append(", ");
         }
      }

      var1.append(']');
      return var1.toString();
   }

   protected StyleClass cast(Object var1) {
      if (var1 == null) {
         throw new NullPointerException("null arg");
      } else {
         StyleClass var2 = (StyleClass)var1;
         return var2;
      }
   }

   protected StyleClass getT(int var1) {
      return getStyleClass(var1);
   }

   protected int getIndex(StyleClass var1) {
      return var1.getIndex();
   }

   static StyleClass getStyleClass(String var0) {
      if (var0 != null && !var0.trim().isEmpty()) {
         StyleClass var1 = null;
         Integer var2 = (Integer)styleClassMap.get(var0);
         int var3 = var2 != null ? var2 : -1;
         int var4 = styleClasses.size();

         assert var3 < var4;

         if (var3 != -1 && var3 < var4) {
            var1 = (StyleClass)styleClasses.get(var3);
         }

         if (var1 == null) {
            var1 = new StyleClass(var0, var4);
            styleClasses.add(var1);
            styleClassMap.put(var0, var4);
         }

         return var1;
      } else {
         throw new IllegalArgumentException("styleClass cannot be null or empty String");
      }
   }

   static StyleClass getStyleClass(int var0) {
      return 0 <= var0 && var0 < styleClasses.size() ? (StyleClass)styleClasses.get(var0) : null;
   }
}
