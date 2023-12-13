package org.apache.logging.log4j.core.appender.db.jpa.converter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.apache.logging.log4j.core.helpers.Strings;

@Converter(
   autoApply = false
)
public class ThrowableAttributeConverter implements AttributeConverter {
   private static final int CAUSED_BY_STRING_LENGTH = 10;
   private static final Field THROWABLE_CAUSE;
   private static final Field THROWABLE_MESSAGE;

   public String convertToDatabaseColumn(Throwable var1) {
      if (var1 == null) {
         return null;
      } else {
         StringBuilder var2 = new StringBuilder();
         this.convertThrowable(var2, var1);
         return var2.toString();
      }
   }

   private void convertThrowable(StringBuilder var1, Throwable var2) {
      var1.append(var2.toString()).append('\n');
      StackTraceElement[] var3 = var2.getStackTrace();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         StackTraceElement var6 = var3[var5];
         var1.append("\tat ").append(var6).append('\n');
      }

      if (var2.getCause() != null) {
         var1.append("Caused by ");
         this.convertThrowable(var1, var2.getCause());
      }

   }

   public Throwable convertToEntityAttribute(String var1) {
      if (Strings.isEmpty(var1)) {
         return null;
      } else {
         List var2 = Arrays.asList(var1.split("(\n|\r\n)"));
         return this.convertString(var2.listIterator(), false);
      }
   }

   private Throwable convertString(ListIterator var1, boolean var2) {
      String var3 = (String)var1.next();
      if (var2) {
         var3 = var3.substring(10);
      }

      int var4 = var3.indexOf(":");
      String var6 = null;
      String var5;
      if (var4 > 1) {
         var5 = var3.substring(0, var4);
         if (var3.length() > var4 + 1) {
            var6 = var3.substring(var4 + 1).trim();
         }
      } else {
         var5 = var3;
      }

      ArrayList var7 = new ArrayList();
      Throwable var8 = null;

      while(var1.hasNext()) {
         String var9 = (String)var1.next();
         if (var9.startsWith("Caused by ")) {
            var1.previous();
            var8 = this.convertString(var1, true);
            break;
         }

         var7.add(StackTraceElementAttributeConverter.convertString(var9.trim().substring(3).trim()));
      }

      return this.getThrowable(var5, var6, var8, (StackTraceElement[])var7.toArray(new StackTraceElement[var7.size()]));
   }

   private Throwable getThrowable(String var1, String var2, Throwable var3, StackTraceElement[] var4) {
      try {
         Class var5 = Class.forName(var1);
         if (!Throwable.class.isAssignableFrom(var5)) {
            return null;
         } else {
            Throwable var6;
            if (var2 != null && var3 != null) {
               var6 = this.getThrowable(var5, var2, var3);
               if (var6 == null) {
                  var6 = this.getThrowable(var5, var3);
                  if (var6 == null) {
                     var6 = this.getThrowable(var5, var2);
                     if (var6 == null) {
                        var6 = this.getThrowable(var5);
                        if (var6 != null) {
                           THROWABLE_MESSAGE.set(var6, var2);
                           THROWABLE_CAUSE.set(var6, var3);
                        }
                     } else {
                        THROWABLE_CAUSE.set(var6, var3);
                     }
                  } else {
                     THROWABLE_MESSAGE.set(var6, var2);
                  }
               }
            } else if (var3 != null) {
               var6 = this.getThrowable(var5, var3);
               if (var6 == null) {
                  var6 = this.getThrowable(var5);
                  if (var6 != null) {
                     THROWABLE_CAUSE.set(var6, var3);
                  }
               }
            } else if (var2 != null) {
               var6 = this.getThrowable(var5, var2);
               if (var6 == null) {
                  var6 = this.getThrowable(var5);
                  if (var6 != null) {
                     THROWABLE_MESSAGE.set(var6, var3);
                  }
               }
            } else {
               var6 = this.getThrowable(var5);
            }

            if (var6 == null) {
               return null;
            } else {
               var6.setStackTrace(var4);
               return var6;
            }
         }
      } catch (Exception var7) {
         return null;
      }
   }

   private Throwable getThrowable(Class var1, String var2, Throwable var3) {
      try {
         Constructor[] var4 = (Constructor[])var1.getConstructors();
         Constructor[] var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Constructor var8 = var5[var7];
            Class[] var9 = var8.getParameterTypes();
            if (var9.length == 2) {
               if (String.class == var9[0] && Throwable.class.isAssignableFrom(var9[1])) {
                  return (Throwable)var8.newInstance(var2, var3);
               }

               if (String.class == var9[1] && Throwable.class.isAssignableFrom(var9[0])) {
                  return (Throwable)var8.newInstance(var3, var2);
               }
            }
         }

         return null;
      } catch (Exception var10) {
         return null;
      }
   }

   private Throwable getThrowable(Class var1, Throwable var2) {
      try {
         Constructor[] var3 = (Constructor[])var1.getConstructors();
         Constructor[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Constructor var7 = var4[var6];
            Class[] var8 = var7.getParameterTypes();
            if (var8.length == 1 && Throwable.class.isAssignableFrom(var8[0])) {
               return (Throwable)var7.newInstance(var2);
            }
         }

         return null;
      } catch (Exception var9) {
         return null;
      }
   }

   private Throwable getThrowable(Class var1, String var2) {
      try {
         return (Throwable)var1.getConstructor(String.class).newInstance(var2);
      } catch (Exception var4) {
         return null;
      }
   }

   private Throwable getThrowable(Class var1) {
      try {
         return (Throwable)var1.newInstance();
      } catch (Exception var3) {
         return null;
      }
   }

   public Object convertToEntityAttribute(Object var1) {
      return this.convertToEntityAttribute((String)var1);
   }

   public Object convertToDatabaseColumn(Object var1) {
      return this.convertToDatabaseColumn((Throwable)var1);
   }

   static {
      try {
         THROWABLE_CAUSE = Throwable.class.getDeclaredField("cause");
         THROWABLE_CAUSE.setAccessible(true);
         THROWABLE_MESSAGE = Throwable.class.getDeclaredField("detailMessage");
         THROWABLE_MESSAGE.setAccessible(true);
      } catch (NoSuchFieldException var1) {
         throw new IllegalStateException("Something is wrong with java.lang.Throwable.", var1);
      }
   }
}
