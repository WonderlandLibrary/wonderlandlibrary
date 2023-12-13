package org.apache.commons.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

public class FilenameUtils {
   public static final char EXTENSION_SEPARATOR = '.';
   public static final String EXTENSION_SEPARATOR_STR;
   private static final char UNIX_SEPARATOR = '/';
   private static final char WINDOWS_SEPARATOR = '\\';
   private static final char SYSTEM_SEPARATOR;
   private static final char OTHER_SEPARATOR;

   public static String normalize(String var0) {
      return doNormalize(var0, SYSTEM_SEPARATOR, true);
   }

   public static String normalize(String var0, boolean var1) {
      int var2 = var1 ? 47 : 92;
      return doNormalize(var0, (char)var2, true);
   }

   public static String normalizeNoEndSeparator(String var0) {
      return doNormalize(var0, SYSTEM_SEPARATOR, false);
   }

   public static String normalizeNoEndSeparator(String var0, boolean var1) {
      int var2 = var1 ? 47 : 92;
      return doNormalize(var0, (char)var2, false);
   }

   private static String doNormalize(String var0, char var1, boolean var2) {
      if (var0 == null) {
         return null;
      } else {
         int var3 = var0.length();
         if (var3 == 0) {
            return var0;
         } else {
            int var4 = getPrefixLength(var0);
            if (var4 < 0) {
               return null;
            } else {
               char[] var5 = new char[var3 + 2];
               var0.getChars(0, var0.length(), var5, 0);
               char var6 = var1 == SYSTEM_SEPARATOR ? OTHER_SEPARATOR : SYSTEM_SEPARATOR;

               for(int var7 = 0; var7 < var5.length; ++var7) {
                  if (var5[var7] == var6) {
                     var5[var7] = var1;
                  }
               }

               boolean var10 = true;
               if (var5[var3 - 1] != var1) {
                  var5[var3++] = var1;
                  var10 = false;
               }

               int var8;
               for(var8 = var4 + 1; var8 < var3; ++var8) {
                  if (var5[var8] == var1 && var5[var8 - 1] == var1) {
                     System.arraycopy(var5, var8, var5, var8 - 1, var3 - var8);
                     --var3;
                     --var8;
                  }
               }

               for(var8 = var4 + 1; var8 < var3; ++var8) {
                  if (var5[var8] == var1 && var5[var8 - 1] == '.' && (var8 == var4 + 1 || var5[var8 - 2] == var1)) {
                     if (var8 == var3 - 1) {
                        var10 = true;
                     }

                     System.arraycopy(var5, var8 + 1, var5, var8 - 1, var3 - var8);
                     var3 -= 2;
                     --var8;
                  }
               }

               label109:
               for(var8 = var4 + 2; var8 < var3; ++var8) {
                  if (var5[var8] == var1 && var5[var8 - 1] == '.' && var5[var8 - 2] == '.' && (var8 == var4 + 2 || var5[var8 - 3] == var1)) {
                     if (var8 == var4 + 2) {
                        return null;
                     }

                     if (var8 == var3 - 1) {
                        var10 = true;
                     }

                     for(int var9 = var8 - 4; var9 >= var4; --var9) {
                        if (var5[var9] == var1) {
                           System.arraycopy(var5, var8 + 1, var5, var9 + 1, var3 - var8);
                           var3 -= var8 - var9;
                           var8 = var9 + 1;
                           continue label109;
                        }
                     }

                     System.arraycopy(var5, var8 + 1, var5, var4, var3 - var8);
                     var3 -= var8 + 1 - var4;
                     var8 = var4 + 1;
                  }
               }

               if (var3 <= 0) {
                  return "";
               } else if (var3 <= var4) {
                  return new String(var5, 0, var3);
               } else if (var10 && var2) {
                  return new String(var5, 0, var3);
               } else {
                  return new String(var5, 0, var3 - 1);
               }
            }
         }
      }
   }

   public static String concat(String param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   public static boolean directoryContains(String var0, String var1) throws IOException {
      if (var0 == null) {
         throw new IllegalArgumentException("Directory must not be null");
      } else if (var1 == null) {
         return false;
      } else {
         return IOCase.SYSTEM.checkEquals(var0, var1) ? false : IOCase.SYSTEM.checkStartsWith(var1, var0);
      }
   }

   public static String separatorsToUnix(String var0) {
      return var0 != null && var0.indexOf(92) != -1 ? var0.replace('\\', '/') : var0;
   }

   public static String separatorsToWindows(String var0) {
      return var0 != null && var0.indexOf(47) != -1 ? var0.replace('/', '\\') : var0;
   }

   public static String separatorsToSystem(String param0) {
      // $FF: Couldn't be decompiled
   }

   public static int getPrefixLength(String param0) {
      // $FF: Couldn't be decompiled
   }

   public static int indexOfLastSeparator(String var0) {
      if (var0 == null) {
         return -1;
      } else {
         int var1 = var0.lastIndexOf(47);
         int var2 = var0.lastIndexOf(92);
         return Math.max(var1, var2);
      }
   }

   public static int indexOfExtension(String var0) {
      if (var0 == null) {
         return -1;
      } else {
         int var1 = var0.lastIndexOf(46);
         int var2 = indexOfLastSeparator(var0);
         return var2 > var1 ? -1 : var1;
      }
   }

   public static String getPrefix(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = getPrefixLength(var0);
         if (var1 < 0) {
            return null;
         } else {
            return var1 > var0.length() ? var0 + '/' : var0.substring(0, var1);
         }
      }
   }

   public static String getPath(String var0) {
      return doGetPath(var0, 1);
   }

   public static String getPathNoEndSeparator(String var0) {
      return doGetPath(var0, 0);
   }

   private static String doGetPath(String var0, int var1) {
      if (var0 == null) {
         return null;
      } else {
         int var2 = getPrefixLength(var0);
         if (var2 < 0) {
            return null;
         } else {
            int var3 = indexOfLastSeparator(var0);
            int var4 = var3 + var1;
            return var2 < var0.length() && var3 >= 0 && var2 < var4 ? var0.substring(var2, var4) : "";
         }
      }
   }

   public static String getFullPath(String var0) {
      return doGetFullPath(var0, true);
   }

   public static String getFullPathNoEndSeparator(String var0) {
      return doGetFullPath(var0, false);
   }

   private static String doGetFullPath(String var0, boolean var1) {
      if (var0 == null) {
         return null;
      } else {
         int var2 = getPrefixLength(var0);
         if (var2 < 0) {
            return null;
         } else if (var2 >= var0.length()) {
            return var1 ? getPrefix(var0) : var0;
         } else {
            int var3 = indexOfLastSeparator(var0);
            if (var3 < 0) {
               return var0.substring(0, var2);
            } else {
               int var4 = var3 + (var1 ? 1 : 0);
               if (var4 == 0) {
                  ++var4;
               }

               return var0.substring(0, var4);
            }
         }
      }
   }

   public static String getName(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = indexOfLastSeparator(var0);
         return var0.substring(var1 + 1);
      }
   }

   public static String getBaseName(String var0) {
      return removeExtension(getName(var0));
   }

   public static String getExtension(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = indexOfExtension(var0);
         return var1 == -1 ? "" : var0.substring(var1 + 1);
      }
   }

   public static String removeExtension(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = indexOfExtension(var0);
         return var1 == -1 ? var0 : var0.substring(0, var1);
      }
   }

   public static boolean equals(String var0, String var1) {
      return equals(var0, var1, false, IOCase.SENSITIVE);
   }

   public static boolean equalsOnSystem(String var0, String var1) {
      return equals(var0, var1, false, IOCase.SYSTEM);
   }

   public static boolean equalsNormalized(String var0, String var1) {
      return equals(var0, var1, true, IOCase.SENSITIVE);
   }

   public static boolean equalsNormalizedOnSystem(String var0, String var1) {
      return equals(var0, var1, true, IOCase.SYSTEM);
   }

   public static boolean equals(String var0, String var1, boolean var2, IOCase var3) {
      if (var0 != null && var1 != null) {
         if (var2) {
            var0 = normalize(var0);
            var1 = normalize(var1);
            if (var0 == null || var1 == null) {
               throw new NullPointerException("Error normalizing one or both of the file names");
            }
         }

         if (var3 == null) {
            var3 = IOCase.SENSITIVE;
         }

         return var3.checkEquals(var0, var1);
      } else {
         return var0 == null && var1 == null;
      }
   }

   public static boolean isExtension(String var0, String var1) {
      if (var0 == null) {
         return false;
      } else if (var1 != null && var1.length() != 0) {
         String var2 = getExtension(var0);
         return var2.equals(var1);
      } else {
         return indexOfExtension(var0) == -1;
      }
   }

   public static boolean isExtension(String var0, String[] var1) {
      if (var0 == null) {
         return false;
      } else if (var1 != null && var1.length != 0) {
         String var2 = getExtension(var0);
         String[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (var2.equals(var6)) {
               return true;
            }
         }

         return false;
      } else {
         return indexOfExtension(var0) == -1;
      }
   }

   public static boolean isExtension(String var0, Collection var1) {
      if (var0 == null) {
         return false;
      } else if (var1 != null && !var1.isEmpty()) {
         String var2 = getExtension(var0);
         Iterator var3 = var1.iterator();

         String var4;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            var4 = (String)var3.next();
         } while(!var2.equals(var4));

         return true;
      } else {
         return indexOfExtension(var0) == -1;
      }
   }

   public static boolean wildcardMatch(String var0, String var1) {
      return wildcardMatch(var0, var1, IOCase.SENSITIVE);
   }

   public static boolean wildcardMatchOnSystem(String var0, String var1) {
      return wildcardMatch(var0, var1, IOCase.SYSTEM);
   }

   public static boolean wildcardMatch(String var0, String var1, IOCase var2) {
      if (var0 == null && var1 == null) {
         return true;
      } else if (var0 != null && var1 != null) {
         if (var2 == null) {
            var2 = IOCase.SENSITIVE;
         }

         String[] var3 = splitOnTokens(var1);
         boolean var4 = false;
         int var5 = 0;
         int var6 = 0;
         Stack var7 = new Stack();

         do {
            if (var7.size() > 0) {
               int[] var8 = (int[])var7.pop();
               var6 = var8[0];
               var5 = var8[1];
               var4 = true;
            }

            for(; var6 < var3.length; ++var6) {
               if (var3[var6].equals("?")) {
                  ++var5;
                  if (var5 > var0.length()) {
                     break;
                  }

                  var4 = false;
               } else if (var3[var6].equals("*")) {
                  var4 = true;
                  if (var6 == var3.length - 1) {
                     var5 = var0.length();
                  }
               } else {
                  if (var4) {
                     var5 = var2.checkIndexOf(var0, var5, var3[var6]);
                     if (var5 == -1) {
                        break;
                     }

                     int var9 = var2.checkIndexOf(var0, var5 + 1, var3[var6]);
                     if (var9 >= 0) {
                        var7.push(new int[]{var6, var9});
                     }
                  } else if (!var2.checkRegionMatches(var0, var5, var3[var6])) {
                     break;
                  }

                  var5 += var3[var6].length();
                  var4 = false;
               }
            }

            if (var6 == var3.length && var5 == var0.length()) {
               return true;
            }
         } while(var7.size() > 0);

         return false;
      } else {
         return false;
      }
   }

   static String[] splitOnTokens(String var0) {
      if (var0.indexOf(63) == -1 && var0.indexOf(42) == -1) {
         return new String[]{var0};
      } else {
         char[] var1 = var0.toCharArray();
         ArrayList var2 = new ArrayList();
         StringBuilder var3 = new StringBuilder();

         for(int var4 = 0; var4 < var1.length; ++var4) {
            if (var1[var4] != '?' && var1[var4] != '*') {
               var3.append(var1[var4]);
            } else {
               if (var3.length() != 0) {
                  var2.add(var3.toString());
                  var3.setLength(0);
               }

               if (var1[var4] == '?') {
                  var2.add("?");
               } else if (var2.isEmpty() || var4 > 0 && !((String)var2.get(var2.size() - 1)).equals("*")) {
                  var2.add("*");
               }
            }
         }

         if (var3.length() != 0) {
            var2.add(var3.toString());
         }

         return (String[])var2.toArray(new String[var2.size()]);
      }
   }

   static {
      // $FF: Couldn't be decompiled
   }
}
