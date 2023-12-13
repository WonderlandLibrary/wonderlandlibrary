package com.ibm.icu.impl.duration.impl;

import com.ibm.icu.lang.UCharacter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class XMLRecordWriter implements RecordWriter {
   private Writer w;
   private List nameStack;
   static final String NULL_NAME = "Null";
   private static final String INDENT = "    ";

   public XMLRecordWriter(Writer var1) {
      this.w = var1;
      this.nameStack = new ArrayList();
   }

   public boolean open(String var1) {
      this.newline();
      this.writeString("<" + var1 + ">");
      this.nameStack.add(var1);
      return true;
   }

   public boolean close() {
      int var1 = this.nameStack.size() - 1;
      if (var1 >= 0) {
         String var2 = (String)this.nameStack.remove(var1);
         this.newline();
         this.writeString("</" + var2 + ">");
         return true;
      } else {
         return false;
      }
   }

   public void flush() {
      try {
         this.w.flush();
      } catch (IOException var2) {
      }

   }

   public void bool(String var1, boolean var2) {
      this.internalString(var1, String.valueOf(var2));
   }

   public void boolArray(String var1, boolean[] var2) {
      if (var2 != null) {
         String[] var3 = new String[var2.length];

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3[var4] = String.valueOf(var2[var4]);
         }

         this.stringArray(var1, var3);
      }

   }

   private static String ctos(char var0) {
      if (var0 == '<') {
         return "&lt;";
      } else {
         return var0 == '&' ? "&amp;" : String.valueOf(var0);
      }
   }

   public void character(String var1, char var2) {
      if (var2 != '\uffff') {
         this.internalString(var1, ctos(var2));
      }

   }

   public void characterArray(String var1, char[] var2) {
      if (var2 != null) {
         String[] var3 = new String[var2.length];

         for(int var4 = 0; var4 < var2.length; ++var4) {
            char var5 = var2[var4];
            if (var5 == '\uffff') {
               var3[var4] = "Null";
            } else {
               var3[var4] = ctos(var5);
            }
         }

         this.internalStringArray(var1, var3);
      }

   }

   public void namedIndex(String var1, String[] var2, int var3) {
      if (var3 >= 0) {
         this.internalString(var1, var2[var3]);
      }

   }

   public void namedIndexArray(String var1, String[] var2, byte[] var3) {
      if (var3 != null) {
         String[] var4 = new String[var3.length];

         for(int var5 = 0; var5 < var3.length; ++var5) {
            byte var6 = var3[var5];
            if (var6 < 0) {
               var4[var5] = "Null";
            } else {
               var4[var5] = var2[var6];
            }
         }

         this.internalStringArray(var1, var4);
      }

   }

   public static String normalize(String var0) {
      if (var0 == null) {
         return null;
      } else {
         StringBuilder var1 = null;
         boolean var2 = false;
         boolean var3 = false;
         boolean var4 = false;

         for(int var5 = 0; var5 < var0.length(); ++var5) {
            char var6 = var0.charAt(var5);
            if (UCharacter.isWhitespace(var6)) {
               if (var1 == null && (var2 || var6 != ' ')) {
                  var1 = new StringBuilder(var0.substring(0, var5));
               }

               if (var2) {
                  continue;
               }

               var2 = true;
               var4 = false;
               var6 = ' ';
            } else {
               var2 = false;
               var4 = var6 == '<' || var6 == '&';
               if (var4 && var1 == null) {
                  var1 = new StringBuilder(var0.substring(0, var5));
               }
            }

            if (var1 != null) {
               if (var4) {
                  var1.append(var6 == '<' ? "&lt;" : "&amp;");
               } else {
                  var1.append(var6);
               }
            }
         }

         if (var1 != null) {
            return var1.toString();
         } else {
            return var0;
         }
      }
   }

   private void internalString(String var1, String var2) {
      if (var2 != null) {
         this.newline();
         this.writeString("<" + var1 + ">" + var2 + "</" + var1 + ">");
      }

   }

   private void internalStringArray(String var1, String[] var2) {
      if (var2 != null) {
         this.push(var1 + "List");

         for(int var3 = 0; var3 < var2.length; ++var3) {
            String var4 = var2[var3];
            if (var4 == null) {
               var4 = "Null";
            }

            this.string(var1, var4);
         }

         this.pop();
      }

   }

   public void string(String var1, String var2) {
      this.internalString(var1, normalize(var2));
   }

   public void stringArray(String var1, String[] var2) {
      if (var2 != null) {
         this.push(var1 + "List");

         for(int var3 = 0; var3 < var2.length; ++var3) {
            String var4 = normalize(var2[var3]);
            if (var4 == null) {
               var4 = "Null";
            }

            this.internalString(var1, var4);
         }

         this.pop();
      }

   }

   public void stringTable(String var1, String[][] var2) {
      if (var2 != null) {
         this.push(var1 + "Table");

         for(int var3 = 0; var3 < var2.length; ++var3) {
            String[] var4 = var2[var3];
            if (var4 == null) {
               this.internalString(var1 + "List", "Null");
            } else {
               this.stringArray(var1, var4);
            }
         }

         this.pop();
      }

   }

   private void push(String var1) {
      this.newline();
      this.writeString("<" + var1 + ">");
      this.nameStack.add(var1);
   }

   private void pop() {
      int var1 = this.nameStack.size() - 1;
      String var2 = (String)this.nameStack.remove(var1);
      this.newline();
      this.writeString("</" + var2 + ">");
   }

   private void newline() {
      this.writeString("\n");

      for(int var1 = 0; var1 < this.nameStack.size(); ++var1) {
         this.writeString("    ");
      }

   }

   private void writeString(String var1) {
      if (this.w != null) {
         try {
            this.w.write(var1);
         } catch (IOException var3) {
            System.err.println(var3.getMessage());
            this.w = null;
         }
      }

   }
}
