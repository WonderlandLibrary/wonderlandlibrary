package com.ibm.icu.impl;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class SortedSetRelation {
   public static final int A_NOT_B = 4;
   public static final int A_AND_B = 2;
   public static final int B_NOT_A = 1;
   public static final int ANY = 7;
   public static final int CONTAINS = 6;
   public static final int DISJOINT = 5;
   public static final int ISCONTAINED = 3;
   public static final int NO_B = 4;
   public static final int EQUALS = 2;
   public static final int NO_A = 1;
   public static final int NONE = 0;
   public static final int ADDALL = 7;
   public static final int A = 6;
   public static final int COMPLEMENTALL = 5;
   public static final int B = 3;
   public static final int REMOVEALL = 4;
   public static final int RETAINALL = 2;
   public static final int B_REMOVEALL = 1;

   public static boolean hasRelation(SortedSet var0, int var1, SortedSet var2) {
      if (var1 >= 0 && var1 <= 7) {
         boolean var3 = (var1 & 4) != 0;
         boolean var4 = (var1 & 2) != 0;
         boolean var5 = (var1 & 1) != 0;
         switch(var1) {
         case 2:
            if (var0.size() != var2.size()) {
               return false;
            }
            break;
         case 3:
            if (var0.size() > var2.size()) {
               return false;
            }
         case 4:
         case 5:
         default:
            break;
         case 6:
            if (var0.size() < var2.size()) {
               return false;
            }
         }

         if (var0.size() == 0) {
            return var2.size() == 0 ? true : var5;
         } else if (var2.size() == 0) {
            return var3;
         } else {
            Iterator var6 = var0.iterator();
            Iterator var7 = var2.iterator();
            Object var8 = var6.next();
            Object var9 = var7.next();

            while(true) {
               while(true) {
                  int var10 = ((Comparable)var8).compareTo(var9);
                  if (var10 == 0) {
                     if (!var4) {
                        return false;
                     }

                     if (!var6.hasNext()) {
                        if (!var7.hasNext()) {
                           return true;
                        }

                        return var5;
                     }

                     if (!var7.hasNext()) {
                        return var3;
                     }

                     var8 = var6.next();
                     var9 = var7.next();
                  } else if (var10 < 0) {
                     if (!var3) {
                        return false;
                     }

                     if (!var6.hasNext()) {
                        return var5;
                     }

                     var8 = var6.next();
                  } else {
                     if (!var5) {
                        return false;
                     }

                     if (!var7.hasNext()) {
                        return var3;
                     }

                     var9 = var7.next();
                  }
               }
            }
         }
      } else {
         throw new IllegalArgumentException("Relation " + var1 + " out of range");
      }
   }

   public static SortedSet doOperation(SortedSet var0, int var1, SortedSet var2) {
      TreeSet var3;
      switch(var1) {
      case 0:
         var0.clear();
         return var0;
      case 1:
         var3 = new TreeSet(var2);
         var3.removeAll(var0);
         var0.clear();
         var0.addAll(var3);
         return var0;
      case 2:
         var0.retainAll(var2);
         return var0;
      case 3:
         var0.clear();
         var0.addAll(var2);
         return var0;
      case 4:
         var0.removeAll(var2);
         return var0;
      case 5:
         var3 = new TreeSet(var2);
         var3.removeAll(var0);
         var0.removeAll(var2);
         var0.addAll(var3);
         return var0;
      case 6:
         return var0;
      case 7:
         var0.addAll(var2);
         return var0;
      default:
         throw new IllegalArgumentException("Relation " + var1 + " out of range");
      }
   }
}
