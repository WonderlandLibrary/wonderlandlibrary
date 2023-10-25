package org.apache.logging.log4j.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class TypeUtil {
  public static List<Field> getAllDeclaredFields(Class<?> cls) {
    List<Field> fields = new ArrayList<>();
    while (cls != null) {
      Collections.addAll(fields, cls.getDeclaredFields());
      cls = cls.getSuperclass();
    } 
    return fields;
  }
  
  public static boolean isAssignable(Type lhs, Type rhs) {
    Objects.requireNonNull(lhs, "No left hand side type provided");
    Objects.requireNonNull(rhs, "No right hand side type provided");
    if (lhs.equals(rhs))
      return true; 
    if (Object.class.equals(lhs))
      return true; 
    if (lhs instanceof Class) {
      Class<?> lhsClass = (Class)lhs;
      if (rhs instanceof Class) {
        Class<?> rhsClass = (Class)rhs;
        return lhsClass.isAssignableFrom(rhsClass);
      } 
      if (rhs instanceof ParameterizedType) {
        Type rhsRawType = ((ParameterizedType)rhs).getRawType();
        if (rhsRawType instanceof Class)
          return lhsClass.isAssignableFrom((Class)rhsRawType); 
      } 
      if (lhsClass.isArray() && rhs instanceof GenericArrayType)
        return isAssignable(lhsClass.getComponentType(), ((GenericArrayType)rhs).getGenericComponentType()); 
    } 
    if (lhs instanceof ParameterizedType) {
      ParameterizedType lhsType = (ParameterizedType)lhs;
      if (rhs instanceof Class) {
        Type lhsRawType = lhsType.getRawType();
        if (lhsRawType instanceof Class)
          return ((Class)lhsRawType).isAssignableFrom((Class)rhs); 
      } else if (rhs instanceof ParameterizedType) {
        ParameterizedType rhsType = (ParameterizedType)rhs;
        return isParameterizedAssignable(lhsType, rhsType);
      } 
    } 
    if (lhs instanceof GenericArrayType) {
      Type lhsComponentType = ((GenericArrayType)lhs).getGenericComponentType();
      if (rhs instanceof Class) {
        Class<?> rhsClass = (Class)rhs;
        if (rhsClass.isArray())
          return isAssignable(lhsComponentType, rhsClass.getComponentType()); 
      } else if (rhs instanceof GenericArrayType) {
        return isAssignable(lhsComponentType, ((GenericArrayType)rhs).getGenericComponentType());
      } 
    } 
    if (lhs instanceof WildcardType)
      return isWildcardAssignable((WildcardType)lhs, rhs); 
    return false;
  }
  
  private static boolean isParameterizedAssignable(ParameterizedType lhs, ParameterizedType rhs) {
    if (lhs.equals(rhs))
      return true; 
    Type[] lhsTypeArguments = lhs.getActualTypeArguments();
    Type[] rhsTypeArguments = rhs.getActualTypeArguments();
    int size = lhsTypeArguments.length;
    if (rhsTypeArguments.length != size)
      return false; 
    for (int i = 0; i < size; i++) {
      Type lhsArgument = lhsTypeArguments[i];
      Type rhsArgument = rhsTypeArguments[i];
      if (!lhsArgument.equals(rhsArgument) && (!(lhsArgument instanceof WildcardType) || 
        
        !isWildcardAssignable((WildcardType)lhsArgument, rhsArgument)))
        return false; 
    } 
    return true;
  }
  
  private static boolean isWildcardAssignable(WildcardType lhs, Type rhs) {
    Type[] lhsUpperBounds = getEffectiveUpperBounds(lhs);
    Type[] lhsLowerBounds = getEffectiveLowerBounds(lhs);
    if (rhs instanceof WildcardType) {
      WildcardType rhsType = (WildcardType)rhs;
      Type[] rhsUpperBounds = getEffectiveUpperBounds(rhsType);
      Type[] rhsLowerBounds = getEffectiveLowerBounds(rhsType);
      for (Type lhsUpperBound : lhsUpperBounds) {
        for (Type rhsUpperBound : rhsUpperBounds) {
          if (!isBoundAssignable(lhsUpperBound, rhsUpperBound))
            return false; 
        } 
        for (Type rhsLowerBound : rhsLowerBounds) {
          if (!isBoundAssignable(lhsUpperBound, rhsLowerBound))
            return false; 
        } 
      } 
      for (Type lhsLowerBound : lhsLowerBounds) {
        for (Type rhsUpperBound : rhsUpperBounds) {
          if (!isBoundAssignable(rhsUpperBound, lhsLowerBound))
            return false; 
        } 
        for (Type rhsLowerBound : rhsLowerBounds) {
          if (!isBoundAssignable(rhsLowerBound, lhsLowerBound))
            return false; 
        } 
      } 
    } else {
      for (Type lhsUpperBound : lhsUpperBounds) {
        if (!isBoundAssignable(lhsUpperBound, rhs))
          return false; 
      } 
      for (Type lhsLowerBound : lhsLowerBounds) {
        if (!isBoundAssignable(lhsLowerBound, rhs))
          return false; 
      } 
    } 
    return true;
  }
  
  private static Type[] getEffectiveUpperBounds(WildcardType type) {
    Type[] upperBounds = type.getUpperBounds();
    (new Type[1])[0] = Object.class;
    return (upperBounds.length == 0) ? new Type[1] : upperBounds;
  }
  
  private static Type[] getEffectiveLowerBounds(WildcardType type) {
    Type[] lowerBounds = type.getLowerBounds();
    (new Type[1])[0] = null;
    return (lowerBounds.length == 0) ? new Type[1] : lowerBounds;
  }
  
  private static boolean isBoundAssignable(Type lhs, Type rhs) {
    return (rhs == null || (lhs != null && isAssignable(lhs, rhs)));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\TypeUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */