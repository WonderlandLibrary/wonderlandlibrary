package com.ibm.icu.impl;

import com.ibm.icu.text.StringTransform;
import com.ibm.icu.text.SymbolTable;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.Freezable;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class UnicodeRegex implements Cloneable, Freezable, StringTransform {
   private SymbolTable symbolTable;
   private static UnicodeRegex STANDARD = new UnicodeRegex();
   private String bnfCommentString = "#";
   private String bnfVariableInfix = "=";
   private String bnfLineSeparator = "\n";
   private Appendable log = null;
   private Comparator LongestFirst = new Comparator(this) {
      final UnicodeRegex this$0;

      {
         this.this$0 = var1;
      }

      public int compare(Object var1, Object var2) {
         String var3 = var1.toString();
         String var4 = var2.toString();
         int var5 = var3.length();
         int var6 = var4.length();
         return var5 != var6 ? var6 - var5 : var3.compareTo(var4);
      }
   };

   public SymbolTable getSymbolTable() {
      return this.symbolTable;
   }

   public UnicodeRegex setSymbolTable(SymbolTable var1) {
      this.symbolTable = var1;
      return this;
   }

   public String transform(String var1) {
      StringBuilder var2 = new StringBuilder();
      UnicodeSet var3 = new UnicodeSet();
      ParsePosition var4 = new ParsePosition(0);
      byte var5 = 0;

      for(int var6 = 0; var6 < var1.length(); ++var6) {
         char var7 = var1.charAt(var6);
         switch(var5) {
         case 0:
            if (var7 == '\\') {
               if (UnicodeSet.resemblesPattern(var1, var6)) {
                  var6 = this.processSet(var1, var6, var2, var3, var4);
                  continue;
               }

               var5 = 1;
            } else if (var7 == '[' && UnicodeSet.resemblesPattern(var1, var6)) {
               var6 = this.processSet(var1, var6, var2, var3, var4);
               continue;
            }
            break;
         case 1:
            if (var7 == 'Q') {
               var5 = 1;
            } else {
               var5 = 0;
            }
            break;
         case 2:
            if (var7 == '\\') {
               var5 = 3;
            }
            break;
         case 3:
            if (var7 == 'E') {
               boolean var8 = false;
            }

            var5 = 2;
         }

         var2.append(var7);
      }

      return var2.toString();
   }

   public static String fix(String var0) {
      return STANDARD.transform(var0);
   }

   public static Pattern compile(String var0) {
      return Pattern.compile(STANDARD.transform(var0));
   }

   public static Pattern compile(String var0, int var1) {
      return Pattern.compile(STANDARD.transform(var0), var1);
   }

   public String compileBnf(String var1) {
      return this.compileBnf(Arrays.asList(var1.split("\\r\\n?|\\n")));
   }

   public String compileBnf(List var1) {
      Map var2 = this.getVariables(var1);
      LinkedHashSet var3 = new LinkedHashSet(var2.keySet());

      for(int var4 = 0; var4 < 2; ++var4) {
         Iterator var5 = var2.entrySet().iterator();

         while(var5.hasNext()) {
            Entry var6 = (Entry)var5.next();
            String var7 = (String)var6.getKey();
            String var8 = (String)var6.getValue();
            Iterator var9 = var2.entrySet().iterator();

            while(var9.hasNext()) {
               Entry var10 = (Entry)var9.next();
               String var11 = (String)var10.getKey();
               String var12 = (String)var10.getValue();
               if (!var7.equals(var11)) {
                  String var13 = var12.replace(var7, var8);
                  if (!var13.equals(var12)) {
                     var3.remove(var7);
                     var2.put(var11, var13);
                     if (this.log != null) {
                        try {
                           this.log.append(var11 + "=" + var13 + ";");
                        } catch (IOException var15) {
                           throw (IllegalArgumentException)(new IllegalArgumentException()).initCause(var15);
                        }
                     }
                  }
               }
            }
         }
      }

      if (var3.size() != 1) {
         throw new IllegalArgumentException("Not a single root: " + var3);
      } else {
         return (String)var2.get(var3.iterator().next());
      }
   }

   public String getBnfCommentString() {
      return this.bnfCommentString;
   }

   public void setBnfCommentString(String var1) {
      this.bnfCommentString = var1;
   }

   public String getBnfVariableInfix() {
      return this.bnfVariableInfix;
   }

   public void setBnfVariableInfix(String var1) {
      this.bnfVariableInfix = var1;
   }

   public String getBnfLineSeparator() {
      return this.bnfLineSeparator;
   }

   public void setBnfLineSeparator(String var1) {
      this.bnfLineSeparator = var1;
   }

   public static List appendLines(List var0, String var1, String var2) throws IOException {
      return appendLines(var0, (InputStream)(new FileInputStream(var1)), var2);
   }

   public static List appendLines(List var0, InputStream var1, String var2) throws UnsupportedEncodingException, IOException {
      BufferedReader var3 = new BufferedReader(new InputStreamReader(var1, var2 == null ? "UTF-8" : var2));

      while(true) {
         String var4 = var3.readLine();
         if (var4 == null) {
            return var0;
         }

         var0.add(var4);
      }
   }

   public UnicodeRegex cloneAsThawed() {
      try {
         return (UnicodeRegex)this.clone();
      } catch (CloneNotSupportedException var2) {
         throw new IllegalArgumentException();
      }
   }

   public UnicodeRegex freeze() {
      return this;
   }

   public boolean isFrozen() {
      return true;
   }

   private int processSet(String var1, int var2, StringBuilder var3, UnicodeSet var4, ParsePosition var5) {
      try {
         var5.setIndex(var2);
         UnicodeSet var6 = var4.clear().applyPattern((String)var1, (ParsePosition)var5, (SymbolTable)this.symbolTable, 0);
         var6.complement().complement();
         var3.append(var6.toPattern(false));
         var2 = var5.getIndex() - 1;
         return var2;
      } catch (Exception var7) {
         throw (IllegalArgumentException)(new IllegalArgumentException("Error in " + var1)).initCause(var7);
      }
   }

   private Map getVariables(List var1) {
      TreeMap var2 = new TreeMap(this.LongestFirst);
      String var3 = null;
      StringBuffer var4 = new StringBuffer();
      int var5 = 0;
      Iterator var6 = var1.iterator();

      while(var6.hasNext()) {
         String var7 = (String)var6.next();
         ++var5;
         if (var7.length() != 0) {
            if (var7.charAt(0) == '\ufeff') {
               var7 = var7.substring(1);
            }

            if (this.bnfCommentString != null) {
               int var8 = var7.indexOf(this.bnfCommentString);
               if (var8 >= 0) {
                  var7 = var7.substring(0, var8);
               }
            }

            String var12 = var7.trim();
            if (var12.length() != 0) {
               String var9 = var7;
               if (var7.trim().length() != 0) {
                  boolean var10 = var12.endsWith(";");
                  if (var10) {
                     var9 = var7.substring(0, var7.lastIndexOf(59));
                  }

                  int var11 = var9.indexOf(this.bnfVariableInfix);
                  if (var11 >= 0) {
                     if (var3 != null) {
                        throw new IllegalArgumentException("Missing ';' before " + var5 + ") " + var7);
                     }

                     var3 = var9.substring(0, var11).trim();
                     if (var2.containsKey(var3)) {
                        throw new IllegalArgumentException("Duplicate variable definition in " + var7);
                     }

                     var4.append(var9.substring(var11 + 1).trim());
                  } else {
                     if (var3 == null) {
                        throw new IllegalArgumentException("Missing '=' at " + var5 + ") " + var7);
                     }

                     var4.append(this.bnfLineSeparator).append(var9);
                  }

                  if (var10) {
                     var2.put(var3, var4.toString());
                     var3 = null;
                     var4.setLength(0);
                  }
               }
            }
         }
      }

      if (var3 != null) {
         throw new IllegalArgumentException("Missing ';' at end");
      } else {
         return var2;
      }
   }

   public Object cloneAsThawed() {
      return this.cloneAsThawed();
   }

   public Object freeze() {
      return this.freeze();
   }

   public Object transform(Object var1) {
      return this.transform((String)var1);
   }
}
