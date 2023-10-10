// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import java.lang.reflect.WildcardType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.util.List;

public final class TypeUtil
{
    private TypeUtil() {
    }
    
    public static List<Field> getAllDeclaredFields(Class<?> cls) {
        final List<Field> fields = new ArrayList<Field>();
        while (cls != null) {
            Collections.addAll(fields, cls.getDeclaredFields());
            cls = cls.getSuperclass();
        }
        return fields;
    }
    
    public static boolean isAssignable(final Type lhs, final Type rhs) {
        Objects.requireNonNull(lhs, "No left hand side type provided");
        Objects.requireNonNull(rhs, "No right hand side type provided");
        if (lhs.equals(rhs)) {
            return true;
        }
        if (Object.class.equals(lhs)) {
            return true;
        }
        if (lhs instanceof Class) {
            final Class<?> lhsClass = (Class<?>)lhs;
            if (rhs instanceof Class) {
                final Class<?> rhsClass = (Class<?>)rhs;
                return lhsClass.isAssignableFrom(rhsClass);
            }
            if (rhs instanceof ParameterizedType) {
                final Type rhsRawType = ((ParameterizedType)rhs).getRawType();
                if (rhsRawType instanceof Class) {
                    return lhsClass.isAssignableFrom((Class<?>)rhsRawType);
                }
            }
            if (lhsClass.isArray() && rhs instanceof GenericArrayType) {
                return isAssignable(lhsClass.getComponentType(), ((GenericArrayType)rhs).getGenericComponentType());
            }
        }
        if (lhs instanceof ParameterizedType) {
            final ParameterizedType lhsType = (ParameterizedType)lhs;
            if (rhs instanceof Class) {
                final Type lhsRawType = lhsType.getRawType();
                if (lhsRawType instanceof Class) {
                    return ((Class)lhsRawType).isAssignableFrom((Class)rhs);
                }
            }
            else if (rhs instanceof ParameterizedType) {
                final ParameterizedType rhsType = (ParameterizedType)rhs;
                return isParameterizedAssignable(lhsType, rhsType);
            }
        }
        if (lhs instanceof GenericArrayType) {
            final Type lhsComponentType = ((GenericArrayType)lhs).getGenericComponentType();
            if (rhs instanceof Class) {
                final Class<?> rhsClass = (Class<?>)rhs;
                if (rhsClass.isArray()) {
                    return isAssignable(lhsComponentType, rhsClass.getComponentType());
                }
            }
            else if (rhs instanceof GenericArrayType) {
                return isAssignable(lhsComponentType, ((GenericArrayType)rhs).getGenericComponentType());
            }
        }
        return lhs instanceof WildcardType && isWildcardAssignable((WildcardType)lhs, rhs);
    }
    
    private static boolean isParameterizedAssignable(final ParameterizedType lhs, final ParameterizedType rhs) {
        if (lhs.equals(rhs)) {
            return true;
        }
        final Type[] lhsTypeArguments = lhs.getActualTypeArguments();
        final Type[] rhsTypeArguments = rhs.getActualTypeArguments();
        final int size = lhsTypeArguments.length;
        if (rhsTypeArguments.length != size) {
            return false;
        }
        for (int i = 0; i < size; ++i) {
            final Type lhsArgument = lhsTypeArguments[i];
            final Type rhsArgument = rhsTypeArguments[i];
            if (!lhsArgument.equals(rhsArgument) && (!(lhsArgument instanceof WildcardType) || !isWildcardAssignable((WildcardType)lhsArgument, rhsArgument))) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean isWildcardAssignable(final WildcardType lhs, final Type rhs) {
        final Type[] lhsUpperBounds = getEffectiveUpperBounds(lhs);
        final Type[] lhsLowerBounds = getEffectiveLowerBounds(lhs);
        if (rhs instanceof WildcardType) {
            final WildcardType rhsType = (WildcardType)rhs;
            final Type[] rhsUpperBounds = getEffectiveUpperBounds(rhsType);
            final Type[] rhsLowerBounds = getEffectiveLowerBounds(rhsType);
            for (final Type lhsUpperBound : lhsUpperBounds) {
                for (final Type rhsUpperBound : rhsUpperBounds) {
                    if (!isBoundAssignable(lhsUpperBound, rhsUpperBound)) {
                        return false;
                    }
                }
                for (final Type rhsLowerBound : rhsLowerBounds) {
                    if (!isBoundAssignable(lhsUpperBound, rhsLowerBound)) {
                        return false;
                    }
                }
            }
            for (final Type lhsLowerBound : lhsLowerBounds) {
                for (final Type rhsUpperBound : rhsUpperBounds) {
                    if (!isBoundAssignable(rhsUpperBound, lhsLowerBound)) {
                        return false;
                    }
                }
                for (final Type rhsLowerBound : rhsLowerBounds) {
                    if (!isBoundAssignable(rhsLowerBound, lhsLowerBound)) {
                        return false;
                    }
                }
            }
        }
        else {
            for (final Type lhsUpperBound2 : lhsUpperBounds) {
                if (!isBoundAssignable(lhsUpperBound2, rhs)) {
                    return false;
                }
            }
            for (final Type lhsLowerBound2 : lhsLowerBounds) {
                if (!isBoundAssignable(lhsLowerBound2, rhs)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private static Type[] getEffectiveUpperBounds(final WildcardType type) {
        final Type[] upperBounds = type.getUpperBounds();
        return (upperBounds.length == 0) ? new Type[] { Object.class } : upperBounds;
    }
    
    private static Type[] getEffectiveLowerBounds(final WildcardType type) {
        final Type[] lowerBounds = type.getLowerBounds();
        return (lowerBounds.length == 0) ? new Type[] { null } : lowerBounds;
    }
    
    private static boolean isBoundAssignable(final Type lhs, final Type rhs) {
        return rhs == null || (lhs != null && isAssignable(lhs, rhs));
    }
}
