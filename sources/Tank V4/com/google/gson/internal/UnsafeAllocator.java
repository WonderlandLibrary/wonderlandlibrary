package com.google.gson.internal;

import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class UnsafeAllocator {
   public abstract Object newInstance(Class var1) throws Exception;

   public static UnsafeAllocator create() {
      try {
         Class var7 = Class.forName("sun.misc.Unsafe");
         Field var8 = var7.getDeclaredField("theUnsafe");
         var8.setAccessible(true);
         Object var9 = var8.get((Object)null);
         Method var3 = var7.getMethod("allocateInstance", Class.class);
         return new UnsafeAllocator(var3, var9) {
            final Method val$allocateInstance;
            final Object val$unsafe;

            {
               this.val$allocateInstance = var1;
               this.val$unsafe = var2;
            }

            public Object newInstance(Class var1) throws Exception {
               return this.val$allocateInstance.invoke(this.val$unsafe, var1);
            }
         };
      } catch (Exception var6) {
         Method var0;
         try {
            var0 = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
            var0.setAccessible(true);
            return new UnsafeAllocator(var0) {
               final Method val$newInstance;

               {
                  this.val$newInstance = var1;
               }

               public Object newInstance(Class var1) throws Exception {
                  return this.val$newInstance.invoke((Object)null, var1, Object.class);
               }
            };
         } catch (Exception var5) {
            try {
               var0 = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
               var0.setAccessible(true);
               int var1 = (Integer)var0.invoke((Object)null, Object.class);
               Method var2 = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, Integer.TYPE);
               var2.setAccessible(true);
               return new UnsafeAllocator(var2, var1) {
                  final Method val$newInstance;
                  final int val$constructorId;

                  {
                     this.val$newInstance = var1;
                     this.val$constructorId = var2;
                  }

                  public Object newInstance(Class var1) throws Exception {
                     return this.val$newInstance.invoke((Object)null, var1, this.val$constructorId);
                  }
               };
            } catch (Exception var4) {
               return new UnsafeAllocator() {
                  public Object newInstance(Class var1) {
                     throw new UnsupportedOperationException("Cannot allocate " + var1);
                  }
               };
            }
         }
      }
   }
}
