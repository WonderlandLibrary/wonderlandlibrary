package javassist.bytecode.annotation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationDefaultAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;

public class AnnotationImpl implements InvocationHandler {
   private static final String JDK_ANNOTATION_CLASS_NAME = "java.lang.annotation.Annotation";
   private static Method JDK_ANNOTATION_TYPE_METHOD = null;
   private Annotation annotation;
   private ClassPool pool;
   private ClassLoader classLoader;
   private transient Class annotationType;
   private transient int cachedHashCode = Integer.MIN_VALUE;

   public static Object make(ClassLoader var0, Class var1, ClassPool var2, Annotation var3) {
      AnnotationImpl var4 = new AnnotationImpl(var3, var2, var0);
      return Proxy.newProxyInstance(var0, new Class[]{var1}, var4);
   }

   private AnnotationImpl(Annotation var1, ClassPool var2, ClassLoader var3) {
      this.annotation = var1;
      this.pool = var2;
      this.classLoader = var3;
   }

   public String getTypeName() {
      return this.annotation.getTypeName();
   }

   private Class getAnnotationType() {
      if (this.annotationType == null) {
         String var1 = this.annotation.getTypeName();

         try {
            this.annotationType = this.classLoader.loadClass(var1);
         } catch (ClassNotFoundException var4) {
            NoClassDefFoundError var3 = new NoClassDefFoundError("Error loading annotation class: " + var1);
            var3.setStackTrace(var4.getStackTrace());
            throw var3;
         }
      }

      return this.annotationType;
   }

   public Annotation getAnnotation() {
      return this.annotation;
   }

   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      String var4 = var2.getName();
      if (Object.class == var2.getDeclaringClass()) {
         if ("equals".equals(var4)) {
            Object var5 = var3[0];
            return new Boolean(this.checkEquals(var5));
         }

         if ("toString".equals(var4)) {
            return this.annotation.toString();
         }

         if ("hashCode".equals(var4)) {
            return new Integer(this.hashCode());
         }
      } else if ("annotationType".equals(var4) && var2.getParameterTypes().length == 0) {
         return this.getAnnotationType();
      }

      MemberValue var6 = this.annotation.getMemberValue(var4);
      return var6 == null ? this.getDefault(var4, var2) : var6.getValue(this.classLoader, this.pool, var2);
   }

   private Object getDefault(String var1, Method var2) throws ClassNotFoundException, RuntimeException {
      String var3 = this.annotation.getTypeName();
      if (this.pool != null) {
         try {
            CtClass var4 = this.pool.get(var3);
            ClassFile var5 = var4.getClassFile2();
            MethodInfo var6 = var5.getMethod(var1);
            if (var6 != null) {
               AnnotationDefaultAttribute var7 = (AnnotationDefaultAttribute)var6.getAttribute("AnnotationDefault");
               if (var7 != null) {
                  MemberValue var8 = var7.getDefaultValue();
                  return var8.getValue(this.classLoader, this.pool, var2);
               }
            }
         } catch (NotFoundException var9) {
            throw new RuntimeException("cannot find a class file: " + var3);
         }
      }

      throw new RuntimeException("no default value: " + var3 + "." + var1 + "()");
   }

   public int hashCode() {
      if (this.cachedHashCode == Integer.MIN_VALUE) {
         int var1 = 0;
         this.getAnnotationType();
         Method[] var2 = this.annotationType.getDeclaredMethods();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            String var4 = var2[var3].getName();
            int var5 = 0;
            MemberValue var6 = this.annotation.getMemberValue(var4);
            Object var7 = null;

            try {
               if (var6 != null) {
                  var7 = var6.getValue(this.classLoader, this.pool, var2[var3]);
               }

               if (var7 == null) {
                  var7 = this.getDefault(var4, var2[var3]);
               }
            } catch (RuntimeException var9) {
               throw var9;
            } catch (Exception var10) {
               throw new RuntimeException("Error retrieving value " + var4 + " for annotation " + this.annotation.getTypeName(), var10);
            }

            if (var7 != null) {
               if (var7.getClass().isArray()) {
                  var5 = arrayHashCode(var7);
               } else {
                  var5 = var7.hashCode();
               }
            }

            var1 += 127 * var4.hashCode() ^ var5;
         }

         this.cachedHashCode = var1;
      }

      return this.cachedHashCode;
   }

   private boolean checkEquals(Object var1) throws Exception {
      if (var1 == null) {
         return false;
      } else {
         if (var1 instanceof Proxy) {
            InvocationHandler var2 = Proxy.getInvocationHandler(var1);
            if (var2 instanceof AnnotationImpl) {
               AnnotationImpl var13 = (AnnotationImpl)var2;
               return this.annotation.equals(var13.annotation);
            }
         }

         Class var12 = (Class)JDK_ANNOTATION_TYPE_METHOD.invoke(var1, (Object[])null);
         if (!this.getAnnotationType().equals(var12)) {
            return false;
         } else {
            Method[] var3 = this.annotationType.getDeclaredMethods();

            for(int var4 = 0; var4 < var3.length; ++var4) {
               String var5 = var3[var4].getName();
               MemberValue var6 = this.annotation.getMemberValue(var5);
               Object var7 = null;
               Object var8 = null;

               try {
                  if (var6 != null) {
                     var7 = var6.getValue(this.classLoader, this.pool, var3[var4]);
                  }

                  if (var7 == null) {
                     var7 = this.getDefault(var5, var3[var4]);
                  }

                  var8 = var3[var4].invoke(var1, (Object[])null);
               } catch (RuntimeException var10) {
                  throw var10;
               } catch (Exception var11) {
                  throw new RuntimeException("Error retrieving value " + var5 + " for annotation " + this.annotation.getTypeName(), var11);
               }

               if (var7 == null && var8 != null) {
                  return false;
               }

               if (var7 != null && !var7.equals(var8)) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   private static int arrayHashCode(Object var0) {
      if (var0 == null) {
         return 0;
      } else {
         int var1 = 1;
         Object[] var2 = (Object[])((Object[])var0);

         for(int var3 = 0; var3 < var2.length; ++var3) {
            int var4 = 0;
            if (var2[var3] != null) {
               var4 = var2[var3].hashCode();
            }

            var1 = 31 * var1 + var4;
         }

         return var1;
      }
   }

   static {
      try {
         Class var0 = Class.forName("java.lang.annotation.Annotation");
         JDK_ANNOTATION_TYPE_METHOD = var0.getMethod("annotationType", (Class[])null);
      } catch (Exception var1) {
      }

   }
}
