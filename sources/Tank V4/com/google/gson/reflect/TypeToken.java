package com.google.gson.reflect;

import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

public class TypeToken {
   final Class rawType;
   final Type type;
   final int hashCode;

   protected TypeToken() {
      this.type = getSuperclassTypeParameter(this.getClass());
      this.rawType = $Gson$Types.getRawType(this.type);
      this.hashCode = this.type.hashCode();
   }

   TypeToken(Type var1) {
      this.type = $Gson$Types.canonicalize((Type)$Gson$Preconditions.checkNotNull(var1));
      this.rawType = $Gson$Types.getRawType(this.type);
      this.hashCode = this.type.hashCode();
   }

   static Type getSuperclassTypeParameter(Class var0) {
      Type var1 = var0.getGenericSuperclass();
      if (var1 instanceof Class) {
         throw new RuntimeException("Missing type parameter.");
      } else {
         ParameterizedType var2 = (ParameterizedType)var1;
         return $Gson$Types.canonicalize(var2.getActualTypeArguments()[0]);
      }
   }

   public final Class getRawType() {
      return this.rawType;
   }

   public final Type getType() {
      return this.type;
   }

   /** @deprecated */
   @Deprecated
   public boolean isAssignableFrom(Class var1) {
      return this.isAssignableFrom((Type)var1);
   }

   /** @deprecated */
   @Deprecated
   public boolean isAssignableFrom(Type var1) {
      if (var1 == null) {
         return false;
      } else if (this.type.equals(var1)) {
         return true;
      } else if (this.type instanceof Class) {
         return this.rawType.isAssignableFrom($Gson$Types.getRawType(var1));
      } else if (this.type instanceof ParameterizedType) {
         return isAssignableFrom(var1, (ParameterizedType)this.type, new HashMap());
      } else if (!(this.type instanceof GenericArrayType)) {
         throw buildUnexpectedTypeError(this.type, Class.class, ParameterizedType.class, GenericArrayType.class);
      } else {
         Type var10000;
         if (this.rawType.isAssignableFrom($Gson$Types.getRawType(var1))) {
            var10000 = var1;
            if ((GenericArrayType)this.type != false) {
               boolean var10001 = true;
               return (boolean)var10000;
            }
         }

         var10000 = false;
         return (boolean)var10000;
      }
   }

   /** @deprecated */
   @Deprecated
   public boolean isAssignableFrom(TypeToken var1) {
      return this.isAssignableFrom(var1.getType());
   }

   private static AssertionError buildUnexpectedTypeError(Type var0, Class... var1) {
      StringBuilder var2 = new StringBuilder("Unexpected type. Expected one of: ");
      Class[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Class var6 = var3[var5];
         var2.append(var6.getName()).append(", ");
      }

      var2.append("but got: ").append(var0.getClass().getName()).append(", for type token: ").append(var0.toString()).append('.');
      return new AssertionError(var2.toString());
   }

   public final int hashCode() {
      return this.hashCode;
   }

   public final boolean equals(Object var1) {
      return var1 instanceof TypeToken && $Gson$Types.equals(this.type, ((TypeToken)var1).type);
   }

   public final String toString() {
      return $Gson$Types.typeToString(this.type);
   }

   public static TypeToken get(Type var0) {
      return new TypeToken(var0);
   }

   public static TypeToken get(Class var0) {
      return new TypeToken(var0);
   }
}
