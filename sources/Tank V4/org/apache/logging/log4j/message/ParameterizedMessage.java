package org.apache.logging.log4j.message;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class ParameterizedMessage implements Message {
   public static final String RECURSION_PREFIX = "[...";
   public static final String RECURSION_SUFFIX = "...]";
   public static final String ERROR_PREFIX = "[!!!";
   public static final String ERROR_SEPARATOR = "=>";
   public static final String ERROR_MSG_SEPARATOR = ":";
   public static final String ERROR_SUFFIX = "!!!]";
   private static final long serialVersionUID = -665975803997290697L;
   private static final int HASHVAL = 31;
   private static final char DELIM_START = '{';
   private static final char DELIM_STOP = '}';
   private static final char ESCAPE_CHAR = '\\';
   private final String messagePattern;
   private final String[] stringArgs;
   private transient Object[] argArray;
   private transient String formattedMessage;
   private transient Throwable throwable;

   public ParameterizedMessage(String var1, String[] var2, Throwable var3) {
      this.messagePattern = var1;
      this.stringArgs = var2;
      this.throwable = var3;
   }

   public ParameterizedMessage(String var1, Object[] var2, Throwable var3) {
      this.messagePattern = var1;
      this.throwable = var3;
      this.stringArgs = this.parseArguments(var2);
   }

   public ParameterizedMessage(String var1, Object[] var2) {
      this.messagePattern = var1;
      this.stringArgs = this.parseArguments(var2);
   }

   public ParameterizedMessage(String var1, Object var2) {
      this(var1, new Object[]{var2});
   }

   public ParameterizedMessage(String var1, Object var2, Object var3) {
      this(var1, new Object[]{var2, var3});
   }

   private String[] parseArguments(Object[] var1) {
      if (var1 == null) {
         return null;
      } else {
         int var2 = countArgumentPlaceholders(this.messagePattern);
         int var3 = var1.length;
         if (var2 < var1.length && this.throwable == null && var1[var1.length - 1] instanceof Throwable) {
            this.throwable = (Throwable)var1[var1.length - 1];
            --var3;
         }

         this.argArray = new Object[var3];

         for(int var4 = 0; var4 < var3; ++var4) {
            this.argArray[var4] = var1[var4];
         }

         String[] var6;
         if (var2 == 1 && this.throwable == null && var1.length > 1) {
            var6 = new String[]{deepToString(var1)};
         } else {
            var6 = new String[var3];

            for(int var5 = 0; var5 < var6.length; ++var5) {
               var6[var5] = deepToString(var1[var5]);
            }
         }

         return var6;
      }
   }

   public String getFormattedMessage() {
      if (this.formattedMessage == null) {
         this.formattedMessage = this.formatMessage(this.messagePattern, this.stringArgs);
      }

      return this.formattedMessage;
   }

   public String getFormat() {
      return this.messagePattern;
   }

   public Object[] getParameters() {
      return (Object[])(this.argArray != null ? this.argArray : this.stringArgs);
   }

   public Throwable getThrowable() {
      return this.throwable;
   }

   protected String formatMessage(String var1, String[] var2) {
      return format(var1, var2);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         ParameterizedMessage var2 = (ParameterizedMessage)var1;
         if (this.messagePattern != null) {
            if (this.messagePattern.equals(var2.messagePattern)) {
               return Arrays.equals(this.stringArgs, var2.stringArgs);
            }
         } else if (var2.messagePattern == null) {
            return Arrays.equals(this.stringArgs, var2.stringArgs);
         }

         return false;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.messagePattern != null ? this.messagePattern.hashCode() : 0;
      var1 = 31 * var1 + (this.stringArgs != null ? Arrays.hashCode(this.stringArgs) : 0);
      return var1;
   }

   public static String format(String var0, Object[] var1) {
      if (var0 != null && var1 != null && var1.length != 0) {
         StringBuilder var2 = new StringBuilder();
         int var3 = 0;
         int var4 = 0;

         for(int var5 = 0; var5 < var0.length(); ++var5) {
            char var6 = var0.charAt(var5);
            if (var6 == '\\') {
               ++var3;
            } else {
               int var7;
               if (var6 == '{' && var5 < var0.length() - 1 && var0.charAt(var5 + 1) == '}') {
                  var7 = var3 / 2;

                  for(int var8 = 0; var8 < var7; ++var8) {
                     var2.append('\\');
                  }

                  if (var3 % 2 == 1) {
                     var2.append('{');
                     var2.append('}');
                  } else {
                     if (var4 < var1.length) {
                        var2.append(var1[var4]);
                     } else {
                        var2.append('{').append('}');
                     }

                     ++var4;
                  }

                  ++var5;
                  var3 = 0;
               } else {
                  if (var3 > 0) {
                     for(var7 = 0; var7 < var3; ++var7) {
                        var2.append('\\');
                     }

                     var3 = 0;
                  }

                  var2.append(var6);
               }
            }
         }

         return var2.toString();
      } else {
         return var0;
      }
   }

   public static int countArgumentPlaceholders(String var0) {
      if (var0 == null) {
         return 0;
      } else {
         int var1 = var0.indexOf(123);
         if (var1 == -1) {
            return 0;
         } else {
            int var2 = 0;
            boolean var3 = false;

            for(int var4 = 0; var4 < var0.length(); ++var4) {
               char var5 = var0.charAt(var4);
               if (var5 == '\\') {
                  var3 = !var3;
               } else if (var5 == '{') {
                  if (!var3 && var4 < var0.length() - 1 && var0.charAt(var4 + 1) == '}') {
                     ++var2;
                     ++var4;
                  }

                  var3 = false;
               } else {
                  var3 = false;
               }
            }

            return var2;
         }
      }
   }

   public static String deepToString(Object var0) {
      if (var0 == null) {
         return null;
      } else if (var0 instanceof String) {
         return (String)var0;
      } else {
         StringBuilder var1 = new StringBuilder();
         HashSet var2 = new HashSet();
         recursiveDeepToString(var0, var1, var2);
         return var1.toString();
      }
   }

   private static void recursiveDeepToString(Object var0, StringBuilder var1, Set var2) {
      if (var0 == null) {
         var1.append("null");
      } else if (var0 instanceof String) {
         var1.append(var0);
      } else {
         Class var3 = var0.getClass();
         String var4;
         boolean var6;
         Object var10;
         if (var3.isArray()) {
            if (var3 == byte[].class) {
               var1.append(Arrays.toString((byte[])((byte[])var0)));
            } else if (var3 == short[].class) {
               var1.append(Arrays.toString((short[])((short[])var0)));
            } else if (var3 == int[].class) {
               var1.append(Arrays.toString((int[])((int[])var0)));
            } else if (var3 == long[].class) {
               var1.append(Arrays.toString((long[])((long[])var0)));
            } else if (var3 == float[].class) {
               var1.append(Arrays.toString((float[])((float[])var0)));
            } else if (var3 == double[].class) {
               var1.append(Arrays.toString((double[])((double[])var0)));
            } else if (var3 == boolean[].class) {
               var1.append(Arrays.toString((boolean[])((boolean[])var0)));
            } else if (var3 == char[].class) {
               var1.append(Arrays.toString((char[])((char[])var0)));
            } else {
               var4 = identityToString(var0);
               if (var2.contains(var4)) {
                  var1.append("[...").append(var4).append("...]");
               } else {
                  var2.add(var4);
                  Object[] var5 = (Object[])((Object[])var0);
                  var1.append("[");
                  var6 = true;
                  Object[] var7 = var5;
                  int var8 = var5.length;

                  for(int var9 = 0; var9 < var8; ++var9) {
                     var10 = var7[var9];
                     if (var6) {
                        var6 = false;
                     } else {
                        var1.append(", ");
                     }

                     recursiveDeepToString(var10, var1, new HashSet(var2));
                  }

                  var1.append("]");
               }
            }
         } else {
            Iterator var18;
            if (var0 instanceof Map) {
               var4 = identityToString(var0);
               if (var2.contains(var4)) {
                  var1.append("[...").append(var4).append("...]");
               } else {
                  var2.add(var4);
                  Map var14 = (Map)var0;
                  var1.append("{");
                  var6 = true;
                  var18 = var14.entrySet().iterator();

                  while(var18.hasNext()) {
                     Entry var20 = (Entry)var18.next();
                     Entry var22 = (Entry)var20;
                     if (var6) {
                        var6 = false;
                     } else {
                        var1.append(", ");
                     }

                     var10 = var22.getKey();
                     Object var11 = var22.getValue();
                     recursiveDeepToString(var10, var1, new HashSet(var2));
                     var1.append("=");
                     recursiveDeepToString(var11, var1, new HashSet(var2));
                  }

                  var1.append("}");
               }
            } else if (var0 instanceof Collection) {
               var4 = identityToString(var0);
               if (var2.contains(var4)) {
                  var1.append("[...").append(var4).append("...]");
               } else {
                  var2.add(var4);
                  Collection var15 = (Collection)var0;
                  var1.append("[");
                  var6 = true;

                  Object var21;
                  for(var18 = var15.iterator(); var18.hasNext(); recursiveDeepToString(var21, var1, new HashSet(var2))) {
                     var21 = var18.next();
                     if (var6) {
                        var6 = false;
                     } else {
                        var1.append(", ");
                     }
                  }

                  var1.append("]");
               }
            } else if (var0 instanceof Date) {
               Date var13 = (Date)var0;
               SimpleDateFormat var16 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
               var1.append(var16.format(var13));
            } else {
               try {
                  var1.append(var0.toString());
               } catch (Throwable var12) {
                  var1.append("[!!!");
                  var1.append(identityToString(var0));
                  var1.append("=>");
                  String var17 = var12.getMessage();
                  String var19 = var12.getClass().getName();
                  var1.append(var19);
                  if (!var19.equals(var17)) {
                     var1.append(":");
                     var1.append(var17);
                  }

                  var1.append("!!!]");
               }
            }
         }

      }
   }

   public static String identityToString(Object var0) {
      return var0 == null ? null : var0.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(var0));
   }

   public String toString() {
      return "ParameterizedMessage[messagePattern=" + this.messagePattern + ", stringArgs=" + Arrays.toString(this.stringArgs) + ", throwable=" + this.throwable + "]";
   }
}
