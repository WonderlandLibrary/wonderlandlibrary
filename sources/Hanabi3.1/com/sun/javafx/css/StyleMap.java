package com.sun.javafx.css;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StyleMap {
   public static final StyleMap EMPTY_MAP = new StyleMap(-1, Collections.emptyList());
   private static final Comparator cascadingStyleComparator = (var0, var1) -> {
      String var2 = var0.getProperty();
      String var3 = var1.getProperty();
      int var4 = var2.compareTo(var3);
      return var4 != 0 ? var4 : var0.compareTo(var1);
   };
   private final int id;
   private List selectors;
   private Map cascadingStyles;

   StyleMap(int var1, List var2) {
      this.id = var1;
      this.selectors = var2;
   }

   public int getId() {
      return this.id;
   }

   public boolean isEmpty() {
      if (this.selectors != null) {
         return this.selectors.isEmpty();
      } else {
         return this.cascadingStyles != null ? this.cascadingStyles.isEmpty() : true;
      }
   }

   public Map getCascadingStyles() {
      if (this.cascadingStyles == null) {
         if (this.selectors == null || this.selectors.isEmpty()) {
            this.cascadingStyles = Collections.emptyMap();
            return this.cascadingStyles;
         }

         ArrayList var1 = new ArrayList();
         int var2 = 0;
         int var3 = 0;

         int var8;
         for(int var4 = this.selectors.size(); var3 < var4; ++var3) {
            Selector var5 = (Selector)this.selectors.get(var3);
            Match var6 = var5.createMatch();
            Rule var7 = var5.getRule();
            var8 = 0;

            for(int var9 = var7.getDeclarations().size(); var8 < var9; ++var8) {
               Declaration var10 = (Declaration)var7.getDeclarations().get(var8);
               CascadingStyle var11 = new CascadingStyle(new Style(var6.selector, var10), var6.pseudoClasses, var6.specificity, var2++);
               var1.add(var11);
            }
         }

         if (var1.isEmpty()) {
            this.cascadingStyles = Collections.emptyMap();
            return this.cascadingStyles;
         }

         Collections.sort(var1, cascadingStyleComparator);
         var3 = var1.size();
         this.cascadingStyles = new HashMap(var3);
         CascadingStyle var12 = (CascadingStyle)var1.get(0);
         String var13 = var12.getProperty();
         int var14 = 0;

         while(true) {
            while(var14 < var3) {
               List var15 = (List)this.cascadingStyles.get(var13);
               if (var15 == null) {
                  var8 = var14;
                  String var16 = var13;

                  do {
                     ++var8;
                     if (var8 >= var3) {
                        break;
                     }

                     var12 = (CascadingStyle)var1.get(var8);
                     var13 = var12.getProperty();
                  } while(var13.equals(var16));

                  this.cascadingStyles.put(var16, var1.subList(var14, var8));
                  var14 = var8;
               } else {
                  assert false;
               }
            }

            this.selectors.clear();
            this.selectors = null;
            break;
         }
      }

      return this.cascadingStyles;
   }
}
