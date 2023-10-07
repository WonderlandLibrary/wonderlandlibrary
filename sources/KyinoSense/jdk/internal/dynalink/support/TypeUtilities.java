/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TypeUtilities {
    static final Class<Object> OBJECT_CLASS = Object.class;
    private static final Map<Class<?>, Class<?>> WRAPPER_TYPES = TypeUtilities.createWrapperTypes();
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPES = TypeUtilities.invertMap(WRAPPER_TYPES);
    private static final Map<String, Class<?>> PRIMITIVE_TYPES_BY_NAME = TypeUtilities.createClassNameMapping(WRAPPER_TYPES.keySet());
    private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPES = TypeUtilities.createWrapperToPrimitiveTypes();
    private static final Set<Class<?>> PRIMITIVE_WRAPPER_TYPES = TypeUtilities.createPrimitiveWrapperTypes();

    private TypeUtilities() {
    }

    public static Class<?> getCommonLosslessConversionType(Class<?> c1, Class<?> c2) {
        if (c1 == c2) {
            return c1;
        }
        if (c1 == Void.TYPE || c2 == Void.TYPE) {
            return Object.class;
        }
        if (TypeUtilities.isConvertibleWithoutLoss(c2, c1)) {
            return c1;
        }
        if (TypeUtilities.isConvertibleWithoutLoss(c1, c2)) {
            return c2;
        }
        if (c1.isPrimitive() && c2.isPrimitive()) {
            if (c1 == Byte.TYPE && c2 == Character.TYPE || c1 == Character.TYPE && c2 == Byte.TYPE) {
                return Integer.TYPE;
            }
            if (c1 == Short.TYPE && c2 == Character.TYPE || c1 == Character.TYPE && c2 == Short.TYPE) {
                return Integer.TYPE;
            }
            if (c1 == Integer.TYPE && c2 == Float.TYPE || c1 == Float.TYPE && c2 == Integer.TYPE) {
                return Double.TYPE;
            }
        }
        return TypeUtilities.getMostSpecificCommonTypeUnequalNonprimitives(c1, c2);
    }

    private static Class<?> getMostSpecificCommonTypeUnequalNonprimitives(Class<?> c1, Class<?> c2) {
        Class<?> npc1 = c1.isPrimitive() ? TypeUtilities.getWrapperType(c1) : c1;
        Class<?> npc2 = c2.isPrimitive() ? TypeUtilities.getWrapperType(c2) : c2;
        Set<Class<Class<?>>> a1 = TypeUtilities.getAssignables(npc1, npc2);
        Set<Class<?>> a2 = TypeUtilities.getAssignables(npc2, npc1);
        a1.retainAll(a2);
        if (a1.isEmpty()) {
            return Object.class;
        }
        ArrayList max = new ArrayList();
        block0: for (Class<?> clazz : a1) {
            Iterator maxiter = max.iterator();
            while (maxiter.hasNext()) {
                Class maxClazz = (Class)maxiter.next();
                if (TypeUtilities.isSubtype(maxClazz, clazz)) continue block0;
                if (!TypeUtilities.isSubtype(clazz, maxClazz)) continue;
                maxiter.remove();
            }
            max.add(clazz);
        }
        if (max.size() > 1) {
            return Object.class;
        }
        return (Class)max.get(0);
    }

    private static Set<Class<?>> getAssignables(Class<?> c1, Class<?> c2) {
        HashSet s = new HashSet();
        TypeUtilities.collectAssignables(c1, c2, s);
        return s;
    }

    private static void collectAssignables(Class<?> c1, Class<?> c2, Set<Class<?>> s) {
        Class<?> sc;
        if (c1.isAssignableFrom(c2)) {
            s.add(c1);
        }
        if ((sc = c1.getSuperclass()) != null) {
            TypeUtilities.collectAssignables(sc, c2, s);
        }
        Class<?>[] itf = c1.getInterfaces();
        for (int i = 0; i < itf.length; ++i) {
            TypeUtilities.collectAssignables(itf[i], c2, s);
        }
    }

    private static Map<Class<?>, Class<?>> createWrapperTypes() {
        IdentityHashMap<Class<Comparable<Boolean>>, Class> wrapperTypes = new IdentityHashMap<Class<Comparable<Boolean>>, Class>(8);
        wrapperTypes.put(Boolean.TYPE, Boolean.class);
        wrapperTypes.put(Byte.TYPE, Byte.class);
        wrapperTypes.put(Character.TYPE, Character.class);
        wrapperTypes.put(Short.TYPE, Short.class);
        wrapperTypes.put(Integer.TYPE, Integer.class);
        wrapperTypes.put(Long.TYPE, Long.class);
        wrapperTypes.put(Float.TYPE, Float.class);
        wrapperTypes.put(Double.TYPE, Double.class);
        return Collections.unmodifiableMap(wrapperTypes);
    }

    private static Map<String, Class<?>> createClassNameMapping(Collection<Class<?>> classes) {
        HashMap map = new HashMap();
        for (Class<?> clazz : classes) {
            map.put(clazz.getName(), clazz);
        }
        return map;
    }

    private static <K, V> Map<V, K> invertMap(Map<K, V> map) {
        IdentityHashMap<V, K> inverted = new IdentityHashMap<V, K>(map.size());
        for (Map.Entry<K, V> entry : map.entrySet()) {
            inverted.put(entry.getValue(), entry.getKey());
        }
        return Collections.unmodifiableMap(inverted);
    }

    public static boolean isMethodInvocationConvertible(Class<?> sourceType, Class<?> targetType) {
        if (targetType.isAssignableFrom(sourceType)) {
            return true;
        }
        if (sourceType.isPrimitive()) {
            if (targetType.isPrimitive()) {
                return TypeUtilities.isProperPrimitiveSubtype(sourceType, targetType);
            }
            assert (WRAPPER_TYPES.get(sourceType) != null) : sourceType.getName();
            return targetType.isAssignableFrom(WRAPPER_TYPES.get(sourceType));
        }
        if (targetType.isPrimitive()) {
            Class<?> unboxedCallSiteType = PRIMITIVE_TYPES.get(sourceType);
            return unboxedCallSiteType != null && (unboxedCallSiteType == targetType || TypeUtilities.isProperPrimitiveSubtype(unboxedCallSiteType, targetType));
        }
        return false;
    }

    public static boolean isConvertibleWithoutLoss(Class<?> sourceType, Class<?> targetType) {
        if (targetType.isAssignableFrom(sourceType) || targetType == Void.TYPE) {
            return true;
        }
        if (sourceType.isPrimitive()) {
            if (sourceType == Void.TYPE) {
                return targetType == Object.class;
            }
            if (targetType.isPrimitive()) {
                return TypeUtilities.isProperPrimitiveLosslessSubtype(sourceType, targetType);
            }
            assert (WRAPPER_TYPES.get(sourceType) != null) : sourceType.getName();
            return targetType.isAssignableFrom(WRAPPER_TYPES.get(sourceType));
        }
        return false;
    }

    public static boolean isPotentiallyConvertible(Class<?> callSiteType, Class<?> methodType) {
        if (TypeUtilities.areAssignable(callSiteType, methodType)) {
            return true;
        }
        if (callSiteType.isPrimitive()) {
            return methodType.isPrimitive() || TypeUtilities.isAssignableFromBoxedPrimitive(methodType);
        }
        if (methodType.isPrimitive()) {
            return TypeUtilities.isAssignableFromBoxedPrimitive(callSiteType);
        }
        return false;
    }

    public static boolean areAssignable(Class<?> c1, Class<?> c2) {
        return c1.isAssignableFrom(c2) || c2.isAssignableFrom(c1);
    }

    public static boolean isSubtype(Class<?> subType, Class<?> superType) {
        if (superType.isAssignableFrom(subType)) {
            return true;
        }
        if (superType.isPrimitive() && subType.isPrimitive()) {
            return TypeUtilities.isProperPrimitiveSubtype(subType, superType);
        }
        return false;
    }

    private static boolean isProperPrimitiveSubtype(Class<?> subType, Class<?> superType) {
        if (superType == Boolean.TYPE || subType == Boolean.TYPE) {
            return false;
        }
        if (subType == Byte.TYPE) {
            return superType != Character.TYPE;
        }
        if (subType == Character.TYPE) {
            return superType != Short.TYPE && superType != Byte.TYPE;
        }
        if (subType == Short.TYPE) {
            return superType != Character.TYPE && superType != Byte.TYPE;
        }
        if (subType == Integer.TYPE) {
            return superType == Long.TYPE || superType == Float.TYPE || superType == Double.TYPE;
        }
        if (subType == Long.TYPE) {
            return superType == Float.TYPE || superType == Double.TYPE;
        }
        if (subType == Float.TYPE) {
            return superType == Double.TYPE;
        }
        return false;
    }

    private static boolean isProperPrimitiveLosslessSubtype(Class<?> subType, Class<?> superType) {
        if (superType == Boolean.TYPE || subType == Boolean.TYPE) {
            return false;
        }
        if (superType == Character.TYPE || subType == Character.TYPE) {
            return false;
        }
        if (subType == Byte.TYPE) {
            return true;
        }
        if (subType == Short.TYPE) {
            return superType != Byte.TYPE;
        }
        if (subType == Integer.TYPE) {
            return superType == Long.TYPE || superType == Double.TYPE;
        }
        if (subType == Float.TYPE) {
            return superType == Double.TYPE;
        }
        return false;
    }

    private static Map<Class<?>, Class<?>> createWrapperToPrimitiveTypes() {
        IdentityHashMap classes = new IdentityHashMap();
        classes.put(Void.class, Void.TYPE);
        classes.put(Boolean.class, Boolean.TYPE);
        classes.put(Byte.class, Byte.TYPE);
        classes.put(Character.class, Character.TYPE);
        classes.put(Short.class, Short.TYPE);
        classes.put(Integer.class, Integer.TYPE);
        classes.put(Long.class, Long.TYPE);
        classes.put(Float.class, Float.TYPE);
        classes.put(Double.class, Double.TYPE);
        return classes;
    }

    private static Set<Class<?>> createPrimitiveWrapperTypes() {
        IdentityHashMap classes = new IdentityHashMap();
        TypeUtilities.addClassHierarchy(classes, Boolean.class);
        TypeUtilities.addClassHierarchy(classes, Byte.class);
        TypeUtilities.addClassHierarchy(classes, Character.class);
        TypeUtilities.addClassHierarchy(classes, Short.class);
        TypeUtilities.addClassHierarchy(classes, Integer.class);
        TypeUtilities.addClassHierarchy(classes, Long.class);
        TypeUtilities.addClassHierarchy(classes, Float.class);
        TypeUtilities.addClassHierarchy(classes, Double.class);
        return classes.keySet();
    }

    private static void addClassHierarchy(Map<Class<?>, Class<?>> map, Class<?> clazz) {
        if (clazz == null) {
            return;
        }
        map.put(clazz, clazz);
        TypeUtilities.addClassHierarchy(map, clazz.getSuperclass());
        for (Class<?> itf : clazz.getInterfaces()) {
            TypeUtilities.addClassHierarchy(map, itf);
        }
    }

    private static boolean isAssignableFromBoxedPrimitive(Class<?> clazz) {
        return PRIMITIVE_WRAPPER_TYPES.contains(clazz);
    }

    public static Class<?> getPrimitiveTypeByName(String name) {
        return PRIMITIVE_TYPES_BY_NAME.get(name);
    }

    public static Class<?> getPrimitiveType(Class<?> wrapperType) {
        return WRAPPER_TO_PRIMITIVE_TYPES.get(wrapperType);
    }

    public static Class<?> getWrapperType(Class<?> primitiveType) {
        return WRAPPER_TYPES.get(primitiveType);
    }

    public static boolean isWrapperType(Class<?> type) {
        return PRIMITIVE_TYPES.containsKey(type);
    }
}

