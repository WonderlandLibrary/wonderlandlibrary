package com.google.common.reflect;

import com.google.common.collect.Sets;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Set;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
abstract class TypeVisitor {
   private final Set visited = Sets.newHashSet();

   public final void visit(Type... var1) {
      Type[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Type var5 = var2[var4];
         if (var5 != null && this.visited.add(var5)) {
            boolean var6 = false;
            if (var5 instanceof TypeVariable) {
               this.visitTypeVariable((TypeVariable)var5);
            } else if (var5 instanceof WildcardType) {
               this.visitWildcardType((WildcardType)var5);
            } else if (var5 instanceof ParameterizedType) {
               this.visitParameterizedType((ParameterizedType)var5);
            } else if (var5 instanceof Class) {
               this.visitClass((Class)var5);
            } else {
               if (!(var5 instanceof GenericArrayType)) {
                  throw new AssertionError("Unknown type: " + var5);
               }

               this.visitGenericArrayType((GenericArrayType)var5);
            }

            var6 = true;
            if (!var6) {
               this.visited.remove(var5);
            }
         }
      }

   }

   void visitClass(Class var1) {
   }

   void visitGenericArrayType(GenericArrayType var1) {
   }

   void visitParameterizedType(ParameterizedType var1) {
   }

   void visitTypeVariable(TypeVariable var1) {
   }

   void visitWildcardType(WildcardType var1) {
   }
}
