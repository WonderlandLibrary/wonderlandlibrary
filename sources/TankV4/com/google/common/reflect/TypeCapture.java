package com.google.common.reflect;

import com.google.common.base.Preconditions;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

abstract class TypeCapture {
   final Type capture() {
      Type var1 = this.getClass().getGenericSuperclass();
      Preconditions.checkArgument(var1 instanceof ParameterizedType, "%s isn't parameterized", var1);
      return ((ParameterizedType)var1).getActualTypeArguments()[0];
   }
}
