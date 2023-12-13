package joptsimple.internal;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class AbbreviationMap {
   private String key;
   private Object value;
   private final Map children = new TreeMap();
   private int keysBeyond;

   public boolean contains(String var1) {
      return this.get(var1) != null;
   }

   public Object get(String var1) {
      char[] var2 = charsOf(var1);
      AbbreviationMap var3 = this;
      char[] var4 = var2;
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         char var7 = var4[var6];
         var3 = (AbbreviationMap)var3.children.get(var7);
         if (var3 == null) {
            return null;
         }
      }

      return var3.value;
   }

   public void put(String var1, Object var2) {
      if (var2 == null) {
         throw new NullPointerException();
      } else if (var1.length() == 0) {
         throw new IllegalArgumentException();
      } else {
         char[] var3 = charsOf(var1);
         this.add(var3, var2, 0, var3.length);
      }
   }

   public void putAll(Iterable var1, Object var2) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         this.put(var4, var2);
      }

   }

   private boolean add(char[] var1, Object var2, int var3, int var4) {
      if (var3 == var4) {
         this.value = var2;
         boolean var8 = this.key != null;
         this.key = new String(var1);
         return !var8;
      } else {
         char var5 = var1[var3];
         AbbreviationMap var6 = (AbbreviationMap)this.children.get(var5);
         if (var6 == null) {
            var6 = new AbbreviationMap();
            this.children.put(var5, var6);
         }

         boolean var7 = var6.add(var1, var2, var3 + 1, var4);
         if (var7) {
            ++this.keysBeyond;
         }

         if (this.key == null) {
            this.value = this.keysBeyond > 1 ? null : var2;
         }

         return var7;
      }
   }

   public void remove(String var1) {
      if (var1.length() == 0) {
         throw new IllegalArgumentException();
      } else {
         char[] var2 = charsOf(var1);
         this.remove(var2, 0, var2.length);
      }
   }

   private void setValueToThatOfOnlyChild() {
      Entry var1 = (Entry)this.children.entrySet().iterator().next();
      AbbreviationMap var2 = (AbbreviationMap)var1.getValue();
      this.value = var2.value;
   }

   private boolean removeAtEndOfKey() {
      if (this.key == null) {
         return false;
      } else {
         this.key = null;
         if (this.keysBeyond == 1) {
            this.setValueToThatOfOnlyChild();
         } else {
            this.value = null;
         }

         return true;
      }
   }

   public Map toJavaUtilMap() {
      TreeMap var1 = new TreeMap();
      this.addToMappings(var1);
      return var1;
   }

   private void addToMappings(Map var1) {
      if (this.key != null) {
         var1.put(this.key, this.value);
      }

      Iterator var2 = this.children.values().iterator();

      while(var2.hasNext()) {
         AbbreviationMap var3 = (AbbreviationMap)var2.next();
         var3.addToMappings(var1);
      }

   }

   private static char[] charsOf(String var0) {
      char[] var1 = new char[var0.length()];
      var0.getChars(0, var0.length(), var1, 0);
      return var1;
   }
}
