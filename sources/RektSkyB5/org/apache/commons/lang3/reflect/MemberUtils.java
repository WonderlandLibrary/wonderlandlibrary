/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.lang3.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.commons.lang3.ClassUtils;

abstract class MemberUtils {
    private static final int ACCESS_TEST = 7;
    private static final Class<?>[] ORDERED_PRIMITIVE_TYPES = new Class[]{Byte.TYPE, Short.TYPE, Character.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE};

    MemberUtils() {
    }

    static boolean setAccessibleWorkaround(AccessibleObject o2) {
        if (o2 == null || o2.isAccessible()) {
            return false;
        }
        Member m2 = (Member)((Object)o2);
        if (!o2.isAccessible() && Modifier.isPublic(m2.getModifiers()) && MemberUtils.isPackageAccess(m2.getDeclaringClass().getModifiers())) {
            try {
                o2.setAccessible(true);
                return true;
            }
            catch (SecurityException securityException) {
                // empty catch block
            }
        }
        return false;
    }

    static boolean isPackageAccess(int modifiers) {
        return (modifiers & 7) == 0;
    }

    static boolean isAccessible(Member m2) {
        return m2 != null && Modifier.isPublic(m2.getModifiers()) && !m2.isSynthetic();
    }

    static int compareConstructorFit(Constructor<?> left, Constructor<?> right, Class<?>[] actual) {
        return MemberUtils.compareParameterTypes(Executable.of(left), Executable.of(right), actual);
    }

    static int compareMethodFit(Method left, Method right, Class<?>[] actual) {
        return MemberUtils.compareParameterTypes(Executable.of(left), Executable.of(right), actual);
    }

    private static int compareParameterTypes(Executable left, Executable right, Class<?>[] actual) {
        float rightCost;
        float leftCost = MemberUtils.getTotalTransformationCost(actual, left);
        return leftCost < (rightCost = MemberUtils.getTotalTransformationCost(actual, right)) ? -1 : (rightCost < leftCost ? 1 : 0);
    }

    private static float getTotalTransformationCost(Class<?>[] srcArgs, Executable executable) {
        long normalArgsLen;
        Class<?>[] destArgs = executable.getParameterTypes();
        boolean isVarArgs = executable.isVarArgs();
        float totalCost = 0.0f;
        long l2 = normalArgsLen = isVarArgs ? (long)(destArgs.length - 1) : (long)destArgs.length;
        if ((long)srcArgs.length < normalArgsLen) {
            return Float.MAX_VALUE;
        }
        int i2 = 0;
        while ((long)i2 < normalArgsLen) {
            totalCost += MemberUtils.getObjectTransformationCost(srcArgs[i2], destArgs[i2]);
            ++i2;
        }
        if (isVarArgs) {
            boolean noVarArgsPassed = srcArgs.length < destArgs.length;
            boolean explicitArrayForVarags = srcArgs.length == destArgs.length && srcArgs[srcArgs.length - 1].isArray();
            float varArgsCost = 0.001f;
            Class<?> destClass = destArgs[destArgs.length - 1].getComponentType();
            if (noVarArgsPassed) {
                totalCost += MemberUtils.getObjectTransformationCost(destClass, Object.class) + 0.001f;
            } else if (explicitArrayForVarags) {
                Class<?> sourceClass = srcArgs[srcArgs.length - 1].getComponentType();
                totalCost += MemberUtils.getObjectTransformationCost(sourceClass, destClass) + 0.001f;
            } else {
                for (int i3 = destArgs.length - 1; i3 < srcArgs.length; ++i3) {
                    Class<?> srcClass = srcArgs[i3];
                    totalCost += MemberUtils.getObjectTransformationCost(srcClass, destClass) + 0.001f;
                }
            }
        }
        return totalCost;
    }

    private static float getObjectTransformationCost(Class<?> srcClass, Class<?> destClass) {
        if (destClass.isPrimitive()) {
            return MemberUtils.getPrimitivePromotionCost(srcClass, destClass);
        }
        float cost = 0.0f;
        while (srcClass != null && !destClass.equals(srcClass)) {
            if (destClass.isInterface() && ClassUtils.isAssignable(srcClass, destClass)) {
                cost += 0.25f;
                break;
            }
            cost += 1.0f;
            srcClass = srcClass.getSuperclass();
        }
        if (srcClass == null) {
            cost += 1.5f;
        }
        return cost;
    }

    private static float getPrimitivePromotionCost(Class<?> srcClass, Class<?> destClass) {
        float cost = 0.0f;
        Class<?> cls = srcClass;
        if (!cls.isPrimitive()) {
            cost += 0.1f;
            cls = ClassUtils.wrapperToPrimitive(cls);
        }
        for (int i2 = 0; cls != destClass && i2 < ORDERED_PRIMITIVE_TYPES.length; ++i2) {
            if (cls != ORDERED_PRIMITIVE_TYPES[i2]) continue;
            cost += 0.1f;
            if (i2 >= ORDERED_PRIMITIVE_TYPES.length - 1) continue;
            cls = ORDERED_PRIMITIVE_TYPES[i2 + 1];
        }
        return cost;
    }

    static boolean isMatchingMethod(Method method, Class<?>[] parameterTypes) {
        return MemberUtils.isMatchingExecutable(Executable.of(method), parameterTypes);
    }

    static boolean isMatchingConstructor(Constructor<?> method, Class<?>[] parameterTypes) {
        return MemberUtils.isMatchingExecutable(Executable.of(method), parameterTypes);
    }

    private static boolean isMatchingExecutable(Executable method, Class<?>[] parameterTypes) {
        Class<?>[] methodParameterTypes = method.getParameterTypes();
        if (ClassUtils.isAssignable(parameterTypes, methodParameterTypes, true)) {
            return true;
        }
        if (method.isVarArgs()) {
            int i2;
            for (i2 = 0; i2 < methodParameterTypes.length - 1 && i2 < parameterTypes.length; ++i2) {
                if (ClassUtils.isAssignable(parameterTypes[i2], methodParameterTypes[i2], true)) continue;
                return false;
            }
            Class<?> varArgParameterType = methodParameterTypes[methodParameterTypes.length - 1].getComponentType();
            while (i2 < parameterTypes.length) {
                if (!ClassUtils.isAssignable(parameterTypes[i2], varArgParameterType, true)) {
                    return false;
                }
                ++i2;
            }
            return true;
        }
        return false;
    }

    private static final class Executable {
        private final Class<?>[] parameterTypes;
        private final boolean isVarArgs;

        private static Executable of(Method method) {
            return new Executable(method);
        }

        private static Executable of(Constructor<?> constructor) {
            return new Executable(constructor);
        }

        private Executable(Method method) {
            this.parameterTypes = method.getParameterTypes();
            this.isVarArgs = method.isVarArgs();
        }

        private Executable(Constructor<?> constructor) {
            this.parameterTypes = constructor.getParameterTypes();
            this.isVarArgs = constructor.isVarArgs();
        }

        public Class<?>[] getParameterTypes() {
            return this.parameterTypes;
        }

        public boolean isVarArgs() {
            return this.isVarArgs;
        }
    }
}

