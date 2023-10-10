package com.ibm.icu.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalePriorityList implements Iterable {
   private static final double D0 = 0.0D;
   private static final Double D1 = 1.0D;
   private static final Pattern languageSplitter = Pattern.compile("\\s*,\\s*");
   private static final Pattern weightSplitter = Pattern.compile("\\s*(\\S*)\\s*;\\s*q\\s*=\\s*(\\S*)");
   private final Map languagesAndWeights;
   private static Comparator myDescendingDouble = new Comparator() {
      public int compare(Double var1, Double var2) {
         return -var1.compareTo(var2);
      }

      public int compare(Object var1, Object var2) {
         return this.compare((Double)var1, (Double)var2);
      }
   };

   public static LocalePriorityList.Builder add(ULocale var0) {
      return (new LocalePriorityList.Builder()).add(var0);
   }

   public static LocalePriorityList.Builder add(ULocale var0, double var1) {
      return (new LocalePriorityList.Builder()).add(var0, var1);
   }

   public static LocalePriorityList.Builder add(LocalePriorityList var0) {
      return (new LocalePriorityList.Builder()).add(var0);
   }

   public static LocalePriorityList.Builder add(String var0) {
      return (new LocalePriorityList.Builder()).add(var0);
   }

   public Double getWeight(ULocale var1) {
      return (Double)this.languagesAndWeights.get(var1);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      Iterator var2 = this.languagesAndWeights.keySet().iterator();

      while(var2.hasNext()) {
         ULocale var3 = (ULocale)var2.next();
         if (var1.length() != 0) {
            var1.append(", ");
         }

         var1.append(var3);
         double var4 = (Double)this.languagesAndWeights.get(var3);
         if (var4 != D1) {
            var1.append(";q=").append(var4);
         }
      }

      return var1.toString();
   }

   public Iterator iterator() {
      return this.languagesAndWeights.keySet().iterator();
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this == var1) {
         return true;
      } else {
         try {
            LocalePriorityList var2 = (LocalePriorityList)var1;
            return this.languagesAndWeights.equals(var2.languagesAndWeights);
         } catch (RuntimeException var3) {
            return false;
         }
      }
   }

   public int hashCode() {
      return this.languagesAndWeights.hashCode();
   }

   private LocalePriorityList(Map var1) {
      this.languagesAndWeights = var1;
   }

   static Comparator access$100() {
      return myDescendingDouble;
   }

   static Double access$200() {
      return D1;
   }

   LocalePriorityList(Map var1, Object var2) {
      this(var1);
   }

   static Map access$400(LocalePriorityList var0) {
      return var0.languagesAndWeights;
   }

   static Pattern access$500() {
      return languageSplitter;
   }

   static Pattern access$600() {
      return weightSplitter;
   }

   public static class Builder {
      private final Map languageToWeight;

      private Builder() {
         this.languageToWeight = new LinkedHashMap();
      }

      public LocalePriorityList build() {
         return this.build(false);
      }

      public LocalePriorityList build(boolean var1) {
         TreeMap var2 = new TreeMap(LocalePriorityList.access$100());

         ULocale var4;
         Object var6;
         for(Iterator var3 = this.languageToWeight.keySet().iterator(); var3.hasNext(); ((Set)var6).add(var4)) {
            var4 = (ULocale)var3.next();
            Double var5 = (Double)this.languageToWeight.get(var4);
            var6 = (Set)var2.get(var5);
            if (var6 == null) {
               var2.put(var5, var6 = new LinkedHashSet());
            }
         }

         LinkedHashMap var9 = new LinkedHashMap();
         Iterator var10 = var2.entrySet().iterator();

         while(var10.hasNext()) {
            Entry var11 = (Entry)var10.next();
            Double var12 = (Double)var11.getKey();
            Iterator var7 = ((Set)var11.getValue()).iterator();

            while(var7.hasNext()) {
               ULocale var8 = (ULocale)var7.next();
               var9.put(var8, var1 ? var12 : LocalePriorityList.access$200());
            }
         }

         return new LocalePriorityList(Collections.unmodifiableMap(var9));
      }

      public LocalePriorityList.Builder add(LocalePriorityList var1) {
         Iterator var2 = LocalePriorityList.access$400(var1).keySet().iterator();

         while(var2.hasNext()) {
            ULocale var3 = (ULocale)var2.next();
            this.add(var3, (Double)LocalePriorityList.access$400(var1).get(var3));
         }

         return this;
      }

      public LocalePriorityList.Builder add(ULocale var1) {
         return this.add(var1, LocalePriorityList.access$200());
      }

      public LocalePriorityList.Builder add(ULocale... var1) {
         ULocale[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ULocale var5 = var2[var4];
            this.add(var5, LocalePriorityList.access$200());
         }

         return this;
      }

      public LocalePriorityList.Builder add(ULocale var1, double var2) {
         if (this.languageToWeight.containsKey(var1)) {
            this.languageToWeight.remove(var1);
         }

         if (var2 <= 0.0D) {
            return this;
         } else {
            if (var2 > LocalePriorityList.access$200()) {
               var2 = LocalePriorityList.access$200();
            }

            this.languageToWeight.put(var1, var2);
            return this;
         }
      }

      public LocalePriorityList.Builder add(String var1) {
         String[] var2 = LocalePriorityList.access$500().split(var1.trim());
         Matcher var3 = LocalePriorityList.access$600().matcher("");
         String[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            if (var3.reset(var7).matches()) {
               ULocale var8 = new ULocale(var3.group(1));
               double var9 = Double.parseDouble(var3.group(2));
               if (!(var9 >= 0.0D) || !(var9 <= LocalePriorityList.access$200())) {
                  throw new IllegalArgumentException("Illegal weight, must be 0..1: " + var9);
               }

               this.add(var8, var9);
            } else if (var7.length() != 0) {
               this.add(new ULocale(var7));
            }
         }

         return this;
      }

      Builder(Object var1) {
         this();
      }
   }
}
