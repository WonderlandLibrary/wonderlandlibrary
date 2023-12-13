package javassist.bytecode.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class Type {
   private final CtClass clazz;
   private final boolean special;
   private static final Map prims = new IdentityHashMap();
   public static final Type DOUBLE;
   public static final Type BOOLEAN;
   public static final Type LONG;
   public static final Type CHAR;
   public static final Type BYTE;
   public static final Type SHORT;
   public static final Type INTEGER;
   public static final Type FLOAT;
   public static final Type VOID;
   public static final Type UNINIT;
   public static final Type RETURN_ADDRESS;
   public static final Type TOP;
   public static final Type BOGUS;
   public static final Type OBJECT;
   public static final Type SERIALIZABLE;
   public static final Type CLONEABLE;
   public static final Type THROWABLE;

   public static Type get(CtClass var0) {
      Type var1 = (Type)prims.get(var0);
      return var1 != null ? var1 : new Type(var0);
   }

   private static Type lookupType(String var0) {
      try {
         return new Type(ClassPool.getDefault().get(var0));
      } catch (NotFoundException var2) {
         throw new RuntimeException(var2);
      }
   }

   Type(CtClass var1) {
      this(var1, false);
   }

   private Type(CtClass var1, boolean var2) {
      this.clazz = var1;
      this.special = var2;
   }

   boolean popChanged() {
      return false;
   }

   public int getSize() {
      return this.clazz != CtClass.doubleType && this.clazz != CtClass.longType && this != TOP ? 1 : 2;
   }

   public CtClass getCtClass() {
      return this.clazz;
   }

   public boolean isSpecial() {
      return this.special;
   }

   public int getDimensions() {
      if (this != null) {
         return 0;
      } else {
         String var1 = this.clazz.getName();
         int var2 = var1.length() - 1;

         int var3;
         for(var3 = 0; var1.charAt(var2) == ']'; ++var3) {
            var2 -= 2;
         }

         return var3;
      }
   }

   public Type getComponent() {
      if (this.clazz != null && this.clazz.isArray()) {
         CtClass var1;
         try {
            var1 = this.clazz.getComponentType();
         } catch (NotFoundException var3) {
            throw new RuntimeException(var3);
         }

         Type var2 = (Type)prims.get(var1);
         return var2 != null ? var2 : new Type(var1);
      } else {
         return null;
      }
   }

   public boolean isAssignableFrom(Type var1) {
      if (this == var1) {
         return true;
      } else if ((var1 != UNINIT || this == false) && (this != UNINIT || var1 != false)) {
         if (var1 instanceof MultiType) {
            return ((MultiType)var1).isAssignableTo(this);
         } else if (var1 instanceof MultiArrayType) {
            return ((MultiArrayType)var1).isAssignableTo(this);
         } else if (this.clazz != null && !this.clazz.isPrimitive()) {
            try {
               return var1.clazz.subtypeOf(this.clazz);
            } catch (Exception var3) {
               throw new RuntimeException(var3);
            }
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   public Type merge(Type var1) {
      if (var1 == this) {
         return this;
      } else if (var1 == null) {
         return this;
      } else if (var1 == UNINIT) {
         return this;
      } else if (this == UNINIT) {
         return var1;
      } else if (var1 == false && this != false) {
         if (var1 instanceof MultiType) {
            return var1.merge(this);
         } else if (var1 != null && this != null) {
            return this.mergeArray(var1);
         } else {
            try {
               return this.mergeClasses(var1);
            } catch (NotFoundException var3) {
               throw new RuntimeException(var3);
            }
         }
      } else {
         return BOGUS;
      }
   }

   Type getRootComponent(Type var1) {
      while(var1 != null) {
         var1 = var1.getComponent();
      }

      return var1;
   }

   private Type createArray(Type var1, int var2) {
      if (var1 instanceof MultiType) {
         return new MultiArrayType((MultiType)var1, var2);
      } else {
         String var3 = this.arrayName(var1.clazz.getName(), var2);

         try {
            Type var4 = get(this.getClassPool(var1).get(var3));
            return var4;
         } catch (NotFoundException var6) {
            throw new RuntimeException(var6);
         }
      }
   }

   String arrayName(String var1, int var2) {
      int var3 = var1.length();
      int var4 = var3 + var2 * 2;
      char[] var5 = new char[var4];
      var1.getChars(0, var3, var5, 0);

      while(var3 < var4) {
         var5[var3++] = '[';
         var5[var3++] = ']';
      }

      var1 = new String(var5);
      return var1;
   }

   private ClassPool getClassPool(Type var1) {
      ClassPool var2 = var1.clazz.getClassPool();
      return var2 != null ? var2 : ClassPool.getDefault();
   }

   private Type mergeArray(Type var1) {
      Type var2 = this.getRootComponent(var1);
      Type var3 = this.getRootComponent(this);
      int var4 = var1.getDimensions();
      int var5 = this.getDimensions();
      Type var6;
      if (var4 == var5) {
         var6 = var3.merge(var2);
         return var6 == BOGUS ? OBJECT : this.createArray(var6, var5);
      } else {
         int var7;
         if (var4 < var5) {
            var6 = var2;
            var7 = var4;
         } else {
            var6 = var3;
            var7 = var5;
         }

         return CLONEABLE.clazz != var6.clazz && SERIALIZABLE.clazz == var6.clazz ? this.createArray(OBJECT, var7) : this.createArray(var6, var7);
      }
   }

   private static CtClass findCommonSuperClass(CtClass var0, CtClass var1) throws NotFoundException {
      CtClass var2 = var0;
      CtClass var3 = var1;
      CtClass var5 = var0;

      while(true) {
         if (var2 != var3 && var2.getSuperclass() != null) {
            return var2;
         }

         CtClass var6 = var2.getSuperclass();
         CtClass var7 = var3.getSuperclass();
         if (var7 == null) {
            var3 = var1;
            break;
         }

         if (var6 == null) {
            var5 = var1;
            var2 = var3;
            var3 = var0;
            break;
         }

         var2 = var6;
         var3 = var7;
      }

      while(true) {
         var2 = var2.getSuperclass();
         if (var2 == null) {
            for(var2 = var5; var2 != var3; var3 = var3.getSuperclass()) {
               var2 = var2.getSuperclass();
            }

            return var2;
         }

         var5 = var5.getSuperclass();
      }
   }

   private Type mergeClasses(Type var1) throws NotFoundException {
      CtClass var2 = findCommonSuperClass(this.clazz, var1.clazz);
      Map var3;
      if (var2.getSuperclass() == null) {
         var3 = this.findCommonInterfaces(var1);
         if (var3.size() == 1) {
            return new Type((CtClass)var3.values().iterator().next());
         } else {
            return (Type)(var3.size() > 1 ? new MultiType(var3) : new Type(var2));
         }
      } else {
         var3 = this.findExclusiveDeclaredInterfaces(var1, var2);
         return (Type)(var3.size() > 0 ? new MultiType(var3, new Type(var2)) : new Type(var2));
      }
   }

   private Map findCommonInterfaces(Type var1) {
      Map var2 = this.getAllInterfaces(var1.clazz, (Map)null);
      Map var3 = this.getAllInterfaces(this.clazz, (Map)null);
      return this.findCommonInterfaces(var2, var3);
   }

   private Map findExclusiveDeclaredInterfaces(Type var1, CtClass var2) {
      Map var3 = this.getDeclaredInterfaces(var1.clazz, (Map)null);
      Map var4 = this.getDeclaredInterfaces(this.clazz, (Map)null);
      Map var5 = this.getAllInterfaces(var2, (Map)null);
      Iterator var6 = var5.keySet().iterator();

      while(var6.hasNext()) {
         Object var7 = var6.next();
         var3.remove(var7);
         var4.remove(var7);
      }

      return this.findCommonInterfaces(var3, var4);
   }

   Map findCommonInterfaces(Map var1, Map var2) {
      Iterator var3 = var2.keySet().iterator();

      while(var3.hasNext()) {
         if (!var1.containsKey(var3.next())) {
            var3.remove();
         }
      }

      var3 = (new ArrayList(var2.values())).iterator();

      while(var3.hasNext()) {
         CtClass var4 = (CtClass)var3.next();

         CtClass[] var5;
         try {
            var5 = var4.getInterfaces();
         } catch (NotFoundException var7) {
            throw new RuntimeException(var7);
         }

         for(int var6 = 0; var6 < var5.length; ++var6) {
            var2.remove(var5[var6].getName());
         }
      }

      return var2;
   }

   Map getAllInterfaces(CtClass var1, Map var2) {
      if (var2 == null) {
         var2 = new HashMap();
      }

      if (var1.isInterface()) {
         ((Map)var2).put(var1.getName(), var1);
      }

      do {
         try {
            CtClass[] var3 = var1.getInterfaces();

            for(int var4 = 0; var4 < var3.length; ++var4) {
               CtClass var5 = var3[var4];
               ((Map)var2).put(var5.getName(), var5);
               this.getAllInterfaces(var5, (Map)var2);
            }

            var1 = var1.getSuperclass();
         } catch (NotFoundException var6) {
            throw new RuntimeException(var6);
         }
      } while(var1 != null);

      return (Map)var2;
   }

   Map getDeclaredInterfaces(CtClass var1, Map var2) {
      if (var2 == null) {
         var2 = new HashMap();
      }

      if (var1.isInterface()) {
         ((Map)var2).put(var1.getName(), var1);
      }

      CtClass[] var3;
      try {
         var3 = var1.getInterfaces();
      } catch (NotFoundException var6) {
         throw new RuntimeException(var6);
      }

      for(int var4 = 0; var4 < var3.length; ++var4) {
         CtClass var5 = var3[var4];
         ((Map)var2).put(var5.getName(), var5);
         this.getDeclaredInterfaces(var5, (Map)var2);
      }

      return (Map)var2;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Type)) {
         return false;
      } else {
         return var1.getClass() == this.getClass() && this.clazz != ((Type)var1).clazz;
      }
   }

   public String toString() {
      if (this == BOGUS) {
         return "BOGUS";
      } else if (this == UNINIT) {
         return "UNINIT";
      } else if (this == RETURN_ADDRESS) {
         return "RETURN ADDRESS";
      } else if (this == TOP) {
         return "TOP";
      } else {
         return this.clazz == null ? "null" : this.clazz.getName();
      }
   }

   static {
      DOUBLE = new Type(CtClass.doubleType);
      BOOLEAN = new Type(CtClass.booleanType);
      LONG = new Type(CtClass.longType);
      CHAR = new Type(CtClass.charType);
      BYTE = new Type(CtClass.byteType);
      SHORT = new Type(CtClass.shortType);
      INTEGER = new Type(CtClass.intType);
      FLOAT = new Type(CtClass.floatType);
      VOID = new Type(CtClass.voidType);
      UNINIT = new Type((CtClass)null);
      RETURN_ADDRESS = new Type((CtClass)null, true);
      TOP = new Type((CtClass)null, true);
      BOGUS = new Type((CtClass)null, true);
      OBJECT = lookupType("java.lang.Object");
      SERIALIZABLE = lookupType("java.io.Serializable");
      CLONEABLE = lookupType("java.lang.Cloneable");
      THROWABLE = lookupType("java.lang.Throwable");
      prims.put(CtClass.doubleType, DOUBLE);
      prims.put(CtClass.longType, LONG);
      prims.put(CtClass.charType, CHAR);
      prims.put(CtClass.shortType, SHORT);
      prims.put(CtClass.intType, INTEGER);
      prims.put(CtClass.floatType, FLOAT);
      prims.put(CtClass.byteType, BYTE);
      prims.put(CtClass.booleanType, BOOLEAN);
      prims.put(CtClass.voidType, VOID);
   }
}
