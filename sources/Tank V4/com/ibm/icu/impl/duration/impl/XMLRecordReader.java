package com.ibm.icu.impl.duration.impl;

import com.ibm.icu.lang.UCharacter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class XMLRecordReader implements RecordReader {
   private Reader r;
   private List nameStack;
   private boolean atTag;
   private String tag;

   public XMLRecordReader(Reader var1) {
      this.r = var1;
      this.nameStack = new ArrayList();
      if (this.getTag().startsWith("?xml")) {
         this.advance();
      }

      if (this.getTag().startsWith("!--")) {
         this.advance();
      }

   }

   public boolean open(String var1) {
      if (this.getTag().equals(var1)) {
         this.nameStack.add(var1);
         this.advance();
         return true;
      } else {
         return false;
      }
   }

   public boolean close() {
      int var1 = this.nameStack.size() - 1;
      String var2 = (String)this.nameStack.get(var1);
      if (this.getTag().equals("/" + var2)) {
         this.nameStack.remove(var1);
         this.advance();
         return true;
      } else {
         return false;
      }
   }

   public boolean bool(String var1) {
      String var2 = this.string(var1);
      return var2 != null ? "true".equals(var2) : false;
   }

   public boolean[] boolArray(String var1) {
      String[] var2 = this.stringArray(var1);
      if (var2 == null) {
         return null;
      } else {
         boolean[] var3 = new boolean[var2.length];

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3[var4] = "true".equals(var2[var4]);
         }

         return var3;
      }
   }

   public char character(String var1) {
      String var2 = this.string(var1);
      return var2 != null ? var2.charAt(0) : '\uffff';
   }

   public char[] characterArray(String var1) {
      String[] var2 = this.stringArray(var1);
      if (var2 == null) {
         return null;
      } else {
         char[] var3 = new char[var2.length];

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3[var4] = var2[var4].charAt(0);
         }

         return var3;
      }
   }

   public byte namedIndex(String var1, String[] var2) {
      String var3 = this.string(var1);
      if (var3 != null) {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var3.equals(var2[var4])) {
               return (byte)var4;
            }
         }
      }

      return -1;
   }

   public byte[] namedIndexArray(String var1, String[] var2) {
      String[] var3 = this.stringArray(var1);
      if (var3 == null) {
         return null;
      } else {
         byte[] var4 = new byte[var3.length];

         label28:
         for(int var5 = 0; var5 < var3.length; ++var5) {
            String var6 = var3[var5];

            for(int var7 = 0; var7 < var2.length; ++var7) {
               if (var2[var7].equals(var6)) {
                  var4[var5] = (byte)var7;
                  continue label28;
               }
            }

            var4[var5] = -1;
         }

         return var4;
      }
   }

   public String string(String var1) {
      if (var1 != false) {
         String var2 = this.readData();
         if ("/" + var1 != false) {
            return var2;
         }
      }

      return null;
   }

   public String[] stringArray(String var1) {
      if (var1 + "List" != false) {
         ArrayList var2;
         String var3;
         for(var2 = new ArrayList(); null != (var3 = this.string(var1)); var2.add(var3)) {
            if ("Null".equals(var3)) {
               var3 = null;
            }
         }

         if ("/" + var1 + "List" != false) {
            return (String[])var2.toArray(new String[var2.size()]);
         }
      }

      return null;
   }

   public String[][] stringTable(String var1) {
      if (var1 + "Table" != false) {
         ArrayList var2 = new ArrayList();

         String[] var3;
         while(null != (var3 = this.stringArray(var1))) {
            var2.add(var3);
         }

         if ("/" + var1 + "Table" != false) {
            return (String[][])var2.toArray(new String[var2.size()][]);
         }
      }

      return (String[][])null;
   }

   private String getTag() {
      if (this.tag == null) {
         this.tag = this.readNextTag();
      }

      return this.tag;
   }

   private void advance() {
      this.tag = null;
   }

   private String readData() {
      StringBuilder var1 = new StringBuilder();
      boolean var2 = false;

      while(true) {
         int var3 = this.readChar();
         if (var3 == -1 || var3 == 60) {
            this.atTag = var3 == 60;
            return var1.toString();
         }

         if (var3 == 38) {
            var3 = this.readChar();
            StringBuilder var4;
            if (var3 == 35) {
               var4 = new StringBuilder();
               byte var5 = 10;
               var3 = this.readChar();
               if (var3 == 120) {
                  var5 = 16;
                  var3 = this.readChar();
               }

               while(var3 != 59 && var3 != -1) {
                  var4.append((char)var3);
                  var3 = this.readChar();
               }

               try {
                  int var6 = Integer.parseInt(var4.toString(), var5);
                  var3 = (char)var6;
               } catch (NumberFormatException var7) {
                  System.err.println("numbuf: " + var4.toString() + " radix: " + var5);
                  throw var7;
               }
            } else {
               for(var4 = new StringBuilder(); var3 != 59 && var3 != -1; var3 = this.readChar()) {
                  var4.append((char)var3);
               }

               String var8 = var4.toString();
               if (var8.equals("lt")) {
                  var3 = 60;
               } else if (var8.equals("gt")) {
                  var3 = 62;
               } else if (var8.equals("quot")) {
                  var3 = 34;
               } else if (var8.equals("apos")) {
                  var3 = 39;
               } else {
                  if (!var8.equals("amp")) {
                     System.err.println("unrecognized character entity: '" + var8 + "'");
                     continue;
                  }

                  var3 = 38;
               }
            }
         }

         if (UCharacter.isWhitespace(var3)) {
            if (var2) {
               continue;
            }

            var3 = 32;
            var2 = true;
         } else {
            var2 = false;
         }

         var1.append((char)var3);
      }
   }

   private String readNextTag() {
      boolean var1 = false;

      int var3;
      while(!this.atTag) {
         var3 = this.readChar();
         if (var3 != 60 && var3 != -1) {
            if (UCharacter.isWhitespace(var3)) {
               continue;
            }

            System.err.println("Unexpected non-whitespace character " + Integer.toHexString(var3));
            break;
         }

         if (var3 == 60) {
            this.atTag = true;
         }
         break;
      }

      if (!this.atTag) {
         return null;
      } else {
         this.atTag = false;
         StringBuilder var2 = new StringBuilder();

         while(true) {
            var3 = this.readChar();
            if (var3 == 62 || var3 == -1) {
               return var2.toString();
            }

            var2.append((char)var3);
         }
      }
   }

   int readChar() {
      try {
         return this.r.read();
      } catch (IOException var2) {
         return -1;
      }
   }
}
