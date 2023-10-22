package com.sun.javafx.fxml.expression;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.AbstractList;
import java.util.ArrayList;

public class KeyPath extends AbstractList {
   private ArrayList elements;

   public KeyPath(ArrayList var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.elements = var1;
      }
   }

   public String get(int var1) {
      return (String)this.elements.get(var1);
   }

   public int size() {
      return this.elements.size();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      int var2 = 0;

      for(int var3 = this.elements.size(); var2 < var3; ++var2) {
         String var4 = (String)this.elements.get(var2);
         if (Character.isDigit(var4.charAt(0))) {
            var1.append("[");
            var1.append(var4);
            var1.append("]");
         } else {
            if (var2 > 0) {
               var1.append(".");
            }

            var1.append(var4);
         }
      }

      return var1.toString();
   }

   public static KeyPath parse(String var0) {
      try {
         PushbackReader var2 = new PushbackReader(new StringReader(var0));

         KeyPath var1;
         try {
            var1 = parse(var2);
         } finally {
            var2.close();
         }

         return var1;
      } catch (IOException var7) {
         throw new RuntimeException(var7);
      }
   }

   protected static KeyPath parse(PushbackReader var0) throws IOException {
      ArrayList var1 = new ArrayList();
      int var2 = var0.read();

      while(var2 != -1 && (Character.isJavaIdentifierStart(var2) || var2 == 91)) {
         StringBuilder var3 = new StringBuilder();
         boolean var4 = var2 == 91;
         if (var4) {
            var2 = var0.read();
            boolean var5 = var2 == 34 || var2 == 39;
            char var6;
            if (var5) {
               var6 = (char)var2;
               var2 = var0.read();
            } else {
               var6 = 0;
            }

            for(; var2 != -1 && var4; var4 = var2 != 93) {
               if (Character.isISOControl(var2)) {
                  throw new IllegalArgumentException("Illegal identifier character.");
               }

               if (!var5 && !Character.isDigit(var2)) {
                  throw new IllegalArgumentException("Illegal character in index value.");
               }

               var3.append((char)var2);
               var2 = var0.read();
               if (var5) {
                  var5 = var2 != var6;
                  if (!var5) {
                     var2 = var0.read();
                  }
               }
            }

            if (var5) {
               throw new IllegalArgumentException("Unterminated quoted identifier.");
            }

            if (var4) {
               throw new IllegalArgumentException("Unterminated bracketed identifier.");
            }

            var2 = var0.read();
         } else {
            while(var2 != -1 && var2 != 46 && var2 != 91 && Character.isJavaIdentifierPart(var2)) {
               var3.append((char)var2);
               var2 = var0.read();
            }
         }

         if (var2 == 46) {
            var2 = var0.read();
            if (var2 == -1) {
               throw new IllegalArgumentException("Illegal terminator character.");
            }
         }

         if (var3.length() == 0) {
            throw new IllegalArgumentException("Missing identifier.");
         }

         var1.add(var3.toString());
      }

      if (var1.size() == 0) {
         throw new IllegalArgumentException("Invalid path.");
      } else {
         if (var2 != -1) {
            var0.unread(var2);
         }

         return new KeyPath(var1);
      }
   }
}
