/*
 * Decompiled with CFR 0.150.
 */
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
    private TypeUtil() {
    }

    public static List<Field> getAllDeclaredFields(Class<?> cls) {
        ArrayList<Field> fields = new ArrayList<Field>();
        while (cls != null) {
            Collections.addAll(fields, cls.getDeclaredFields());
            cls = cls.getSuperclass();
        }
        return fields;
    }

    public static boolean isAssignable(Type lhs, Type rhs) {
        Objects.requireNonNull(lhs, "No left hand side type provided");
        Objects.requireNonNull(rhs, "No right hand side type provided");
        if (lhs.equals(rhs)) {
            return true;
        }
        if (Object.class.equals((Object)lhs)) {
            return true;
        }
        if (lhs instanceof Class) {
            Type rhsRawType;
            Class lhsClass = (Class)lhs;
            if (rhs instanceof Class) {
                Class rhsClass = (Class)rhs;
                return lhsClass.isAssignableFrom(rhsClass);
            }
            if (rhs instanceof ParameterizedType && (rhsRawType = ((ParameterizedType)rhs).getRawType()) instanceof Class) {
                return lhsClass.isAssignableFrom((Class)rhsRawType);
            }
            if (lhsClass.isArray() && rhs instanceof GenericArrayType) {
                return TypeUtil.isAssignable(lhsClass.getComponentType(), ((GenericArrayType)rhs).getGenericComponentType());
            }
        }
        if (lhs instanceof ParameterizedType) {
            ParameterizedType lhsType = (ParameterizedType)lhs;
            if (rhs instanceof Class) {
                Type lhsRawType = lhsType.getRawType();
                if (lhsRawType instanceof Class) {
                    return ((Class)lhsRawType).isAssignableFrom((Class)rhs);
                }
            } else if (rhs instanceof ParameterizedType) {
                ParameterizedType rhsType = (ParameterizedType)rhs;
                return TypeUtil.isParameterizedAssignable(lhsType, rhsType);
            }
        }
        if (lhs instanceof GenericArrayType) {
            Type lhsComponentType = ((GenericArrayType)lhs).getGenericComponentType();
            if (rhs instanceof Class) {
                Class rhsClass = (Class)rhs;
                if (rhsClass.isArray()) {
                    return TypeUtil.isAssignable(lhsComponentType, rhsClass.getComponentType());
                }
            } else if (rhs instanceof GenericArrayType) {
                return TypeUtil.isAssignable(lhsComponentType, ((GenericArrayType)rhs).getGenericComponentType());
            }
        }
        if (lhs instanceof WildcardType) {
            return TypeUtil.isWildcardAssignable((WildcardType)lhs, rhs);
        }
        return false;
    }

    private static boolean isParameterizedAssignable(ParameterizedType lhs, ParameterizedType rhs) {
        int size;
        if (lhs.equals(rhs)) {
            return true;
        }
        Type[] lhsTypeArguments = lhs.getActualTypeArguments();
        Type[] rhsTypeArguments = rhs.getActualTypeArguments();
        if (rhsTypeArguments.length != (size = lhsTypeArguments.length)) {
            return false;
        }
        for (int i = 0; i < size; ++i) {
            Type lhsArgument = lhsTypeArguments[i];
            Type rhsArgument = rhsTypeArguments[i];
            if (lhsArgument.equals(rhsArgument) || lhsArgument instanceof WildcardType && TypeUtil.isWildcardAssignable((WildcardType)lhsArgument, rhsArgument)) continue;
            return false;
        }
        return true;
    }

    private static boolean isWildcardAssignable(WildcardType lhs, Type rhs) {
        Type[] lhsUpperBounds = TypeUtil.getEffectiveUpperBounds(lhs);
        Type[] lhsLowerBounds = TypeUtil.getEffectiveLowerBounds(lhs);
        if (rhs instanceof WildcardType) {
            WildcardType rhsType = (WildcardType)rhs;
            Type[] rhsUpperBounds = TypeUtil.getEffectiveUpperBounds(rhsType);
            Type[] rhsLowerBounds = TypeUtil.getEffectiveLowerBounds(rhsType);
            for (Type lhsUpperBound : lhsUpperBounds) {
                for (Type rhsUpperBound : rhsUpperBounds) {
                    if (TypeUtil.isBoundAssignable(lhsUpperBound, rhsUpperBound)) continue;
                    return false;
                }
                for (Type rhsLowerBound : rhsLowerBounds) {
                    if (TypeUtil.isBoundAssignable(lhsUpperBound, rhsLowerBound)) continue;
                    return false;
                }
            }
            for (Type lhsLowerBound : lhsLowerBounds) {
                for (Type rhsUpperBound : rhsUpperBounds) {
                    if (TypeUtil.isBoundAssignable(rhsUpperBound, lhsLowerBound)) continue;
                    return false;
                }
                for (Type rhsLowerBound : rhsLowerBounds) {
                    if (TypeUtil.isBoundAssignable(rhsLowerBound, lhsLowerBound)) continue;
                    return false;
                }
            }
        } else {
            for (Type lhsUpperBound : lhsUpperBounds) {
                if (TypeUtil.isBoundAssignable(lhsUpperBound, rhs)) continue;
                return false;
            }
            for (Type lhsLowerBound : lhsLowerBounds) {
                if (TypeUtil.isBoundAssignable(lhsLowerBound, rhs)) continue;
                return false;
            }
        }
        return true;
    }

    private static Type[] getEffectiveUpperBounds(WildcardType type) {
        Type[] arrtype;
        Type[] upperBounds = type.getUpperBounds();
        if (upperBounds.length == 0) {
            Type[] arrtype2 = new Type[1];
            arrtype = arrtype2;
            arrtype2[0] = Object.class;
        } else {
            arrtype = upperBounds;
        }
        return arrtype;
    }

    private static Type[] getEffectiveLowerBounds(WildcardType type) {
        Type[] arrtype;
        Type[] lowerBounds = type.getLowerBounds();
        if (lowerBounds.length == 0) {
            Type[] arrtype2 = new Type[1];
            arrtype = arrtype2;
            arrtype2[0] = null;
        } else {
            arrtype = lowerBounds;
        }
        return arrtype;
    }

    private static boolean isBoundAssignable(Type lhs, Type rhs) {
        return rhs == null || lhs != null && TypeUtil.isAssignable(lhs, rhs);
    }
}

