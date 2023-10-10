package org.apache.commons.lang3.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

public class ExceptionUtils {
   static final String WRAPPED_MARKER = " [wrapped] ";
   private static final String[] CAUSE_METHOD_NAMES = new String[]{"getCause", "getNextException", "getTargetException", "getException", "getSourceException", "getRootCause", "getCausedByException", "getNested", "getLinkedException", "getNestedException", "getLinkedCause", "getThrowable"};

   /** @deprecated */
   @Deprecated
   public static String[] getDefaultCauseMethodNames() {
      return (String[])ArrayUtils.clone((Object[])CAUSE_METHOD_NAMES);
   }

   /** @deprecated */
   @Deprecated
   public static Throwable getCause(Throwable var0) {
      return getCause(var0, CAUSE_METHOD_NAMES);
   }

   /** @deprecated */
   @Deprecated
   public static Throwable getCause(Throwable var0, String[] var1) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 == null) {
            var1 = CAUSE_METHOD_NAMES;
         }

         String[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            if (var5 != null) {
               Throwable var6 = getCauseUsingMethodName(var0, var5);
               if (var6 != null) {
                  return var6;
               }
            }
         }

         return null;
      }
   }

   public static Throwable getRootCause(Throwable var0) {
      List var1 = getThrowableList(var0);
      return var1.size() < 2 ? null : (Throwable)var1.get(var1.size() - 1);
   }

   private static Throwable getCauseUsingMethodName(Throwable var0, String var1) {
      Method var2 = null;

      try {
         var2 = var0.getClass().getMethod(var1);
      } catch (NoSuchMethodException var7) {
      } catch (SecurityException var8) {
      }

      if (var2 != null && Throwable.class.isAssignableFrom(var2.getReturnType())) {
         try {
            return (Throwable)var2.invoke(var0);
         } catch (IllegalAccessException var4) {
         } catch (IllegalArgumentException var5) {
         } catch (InvocationTargetException var6) {
         }
      }

      return null;
   }

   public static int getThrowableCount(Throwable var0) {
      return getThrowableList(var0).size();
   }

   public static Throwable[] getThrowables(Throwable var0) {
      List var1 = getThrowableList(var0);
      return (Throwable[])var1.toArray(new Throwable[var1.size()]);
   }

   public static List getThrowableList(Throwable var0) {
      ArrayList var1;
      for(var1 = new ArrayList(); var0 != null && !var1.contains(var0); var0 = getCause(var0)) {
         var1.add(var0);
      }

      return var1;
   }

   public static int indexOfThrowable(Throwable var0, Class var1) {
      return indexOf(var0, var1, 0, false);
   }

   public static int indexOfThrowable(Throwable var0, Class var1, int var2) {
      return indexOf(var0, var1, var2, false);
   }

   public static int indexOfType(Throwable var0, Class var1) {
      return indexOf(var0, var1, 0, true);
   }

   public static int indexOfType(Throwable var0, Class var1, int var2) {
      return indexOf(var0, var1, var2, true);
   }

   private static int indexOf(Throwable var0, Class var1, int var2, boolean var3) {
      if (var0 != null && var1 != null) {
         if (var2 < 0) {
            var2 = 0;
         }

         Throwable[] var4 = getThrowables(var0);
         if (var2 >= var4.length) {
            return -1;
         } else {
            int var5;
            if (var3) {
               for(var5 = var2; var5 < var4.length; ++var5) {
                  if (var1.isAssignableFrom(var4[var5].getClass())) {
                     return var5;
                  }
               }
            } else {
               for(var5 = var2; var5 < var4.length; ++var5) {
                  if (var1.equals(var4[var5].getClass())) {
                     return var5;
                  }
               }
            }

            return -1;
         }
      } else {
         return -1;
      }
   }

   public static void printRootCauseStackTrace(Throwable var0) {
      printRootCauseStackTrace(var0, System.err);
   }

   public static void printRootCauseStackTrace(Throwable var0, PrintStream var1) {
      if (var0 != null) {
         if (var1 == null) {
            throw new IllegalArgumentException("The PrintStream must not be null");
         } else {
            String[] var2 = getRootCauseStackTrace(var0);
            String[] var3 = var2;
            int var4 = var2.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               String var6 = var3[var5];
               var1.println(var6);
            }

            var1.flush();
         }
      }
   }

   public static void printRootCauseStackTrace(Throwable var0, PrintWriter var1) {
      if (var0 != null) {
         if (var1 == null) {
            throw new IllegalArgumentException("The PrintWriter must not be null");
         } else {
            String[] var2 = getRootCauseStackTrace(var0);
            String[] var3 = var2;
            int var4 = var2.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               String var6 = var3[var5];
               var1.println(var6);
            }

            var1.flush();
         }
      }
   }

   public static String[] getRootCauseStackTrace(Throwable var0) {
      if (var0 == null) {
         return ArrayUtils.EMPTY_STRING_ARRAY;
      } else {
         Throwable[] var1 = getThrowables(var0);
         int var2 = var1.length;
         ArrayList var3 = new ArrayList();
         List var4 = getStackFrameList(var1[var2 - 1]);
         int var5 = var2;

         while(true) {
            --var5;
            if (var5 < 0) {
               return (String[])var3.toArray(new String[var3.size()]);
            }

            List var6 = var4;
            if (var5 != 0) {
               var4 = getStackFrameList(var1[var5 - 1]);
               removeCommonFrames(var6, var4);
            }

            if (var5 == var2 - 1) {
               var3.add(var1[var5].toString());
            } else {
               var3.add(" [wrapped] " + var1[var5].toString());
            }

            for(int var7 = 0; var7 < var6.size(); ++var7) {
               var3.add(var6.get(var7));
            }
         }
      }
   }

   public static void removeCommonFrames(List var0, List var1) {
      if (var0 != null && var1 != null) {
         int var2 = var0.size() - 1;

         for(int var3 = var1.size() - 1; var2 >= 0 && var3 >= 0; --var3) {
            String var4 = (String)var0.get(var2);
            String var5 = (String)var1.get(var3);
            if (var4.equals(var5)) {
               var0.remove(var2);
            }

            --var2;
         }

      } else {
         throw new IllegalArgumentException("The List must not be null");
      }
   }

   public static String getStackTrace(Throwable var0) {
      StringWriter var1 = new StringWriter();
      PrintWriter var2 = new PrintWriter(var1, true);
      var0.printStackTrace(var2);
      return var1.getBuffer().toString();
   }

   public static String[] getStackFrames(Throwable var0) {
      return var0 == null ? ArrayUtils.EMPTY_STRING_ARRAY : getStackFrames(getStackTrace(var0));
   }

   static String[] getStackFrames(String var0) {
      String var1 = SystemUtils.LINE_SEPARATOR;
      StringTokenizer var2 = new StringTokenizer(var0, var1);
      ArrayList var3 = new ArrayList();

      while(var2.hasMoreTokens()) {
         var3.add(var2.nextToken());
      }

      return (String[])var3.toArray(new String[var3.size()]);
   }

   static List getStackFrameList(Throwable var0) {
      String var1 = getStackTrace(var0);
      String var2 = SystemUtils.LINE_SEPARATOR;
      StringTokenizer var3 = new StringTokenizer(var1, var2);
      ArrayList var4 = new ArrayList();
      boolean var5 = false;

      while(var3.hasMoreTokens()) {
         String var6 = var3.nextToken();
         int var7 = var6.indexOf("at");
         if (var7 != -1 && var6.substring(0, var7).trim().isEmpty()) {
            var5 = true;
            var4.add(var6);
         } else if (var5) {
            break;
         }
      }

      return var4;
   }

   public static String getMessage(Throwable var0) {
      if (var0 == null) {
         return "";
      } else {
         String var1 = ClassUtils.getShortClassName(var0, (String)null);
         String var2 = var0.getMessage();
         return var1 + ": " + StringUtils.defaultString(var2);
      }
   }

   public static String getRootCauseMessage(Throwable var0) {
      Throwable var1 = getRootCause(var0);
      var1 = var1 == null ? var0 : var1;
      return getMessage(var1);
   }
}
