package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import javax.annotation.Nullable;

@Beta
public abstract class TypeParameter extends TypeCapture {
   final TypeVariable typeVariable;

   protected TypeParameter() {
      Type var1 = this.capture();
      Preconditions.checkArgument(var1 instanceof TypeVariable, "%s should be a type variable.", var1);
      this.typeVariable = (TypeVariable)var1;
   }

   public final int hashCode() {
      return this.typeVariable.hashCode();
   }

   public final boolean equals(@Nullable Object var1) {
      if (var1 instanceof TypeParameter) {
         TypeParameter var2 = (TypeParameter)var1;
         return this.typeVariable.equals(var2.typeVariable);
      } else {
         return false;
      }
   }

   public String toString() {
      return this.typeVariable.toString();
   }
}
