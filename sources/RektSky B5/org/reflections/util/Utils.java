/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.reflections.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Utils {
    public static String repeat(String string, int times) {
        return IntStream.range(0, times).mapToObj(i2 -> string).collect(Collectors.joining());
    }

    public static boolean isEmpty(String s2) {
        return s2 == null || s2.length() == 0;
    }

    public static File prepareFile(String filename) {
        File file = new File(filename);
        File parent = file.getAbsoluteFile().getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        return file;
    }

    public static Member getMemberFromDescriptor(String descriptor, ClassLoader ... classLoaders) throws ReflectionsException {
        int p0 = descriptor.lastIndexOf(40);
        String memberKey = p0 != -1 ? descriptor.substring(0, p0) : descriptor;
        String methodParameters = p0 != -1 ? descriptor.substring(p0 + 1, descriptor.lastIndexOf(41)) : "";
        int p1 = Math.max(memberKey.lastIndexOf(46), memberKey.lastIndexOf("$"));
        String className = memberKey.substring(memberKey.lastIndexOf(32) + 1, p1);
        String memberName = memberKey.substring(p1 + 1);
        Class[] parameterTypes = null;
        if (!Utils.isEmpty(methodParameters)) {
            String[] parameterNames = methodParameters.split(",");
            parameterTypes = (Class[])Arrays.stream(parameterNames).map(name -> ReflectionUtils.forName(name.trim(), classLoaders)).toArray(Class[]::new);
        }
        for (Class<?> aClass = ReflectionUtils.forName(className, classLoaders); aClass != null; aClass = aClass.getSuperclass()) {
            try {
                if (!descriptor.contains("(")) {
                    return aClass.isInterface() ? aClass.getField(memberName) : aClass.getDeclaredField(memberName);
                }
                if (Utils.isConstructor(descriptor)) {
                    return aClass.isInterface() ? aClass.getConstructor(parameterTypes) : aClass.getDeclaredConstructor(parameterTypes);
                }
                return aClass.isInterface() ? aClass.getMethod(memberName, parameterTypes) : aClass.getDeclaredMethod(memberName, parameterTypes);
            }
            catch (Exception e2) {
                continue;
            }
        }
        throw new ReflectionsException("Can't resolve member named " + memberName + " for class " + className);
    }

    public static Set<Method> getMethodsFromDescriptors(Iterable<String> annotatedWith, ClassLoader ... classLoaders) {
        HashSet<Method> result = new HashSet<Method>();
        for (String annotated : annotatedWith) {
            Method member;
            if (Utils.isConstructor(annotated) || (member = (Method)Utils.getMemberFromDescriptor(annotated, classLoaders)) == null) continue;
            result.add(member);
        }
        return result;
    }

    public static Set<Constructor> getConstructorsFromDescriptors(Iterable<String> annotatedWith, ClassLoader ... classLoaders) {
        HashSet<Constructor> result = new HashSet<Constructor>();
        for (String annotated : annotatedWith) {
            Constructor member;
            if (!Utils.isConstructor(annotated) || (member = (Constructor)Utils.getMemberFromDescriptor(annotated, classLoaders)) == null) continue;
            result.add(member);
        }
        return result;
    }

    public static Set<Member> getMembersFromDescriptors(Iterable<String> values, ClassLoader ... classLoaders) {
        HashSet<Member> result = new HashSet<Member>();
        for (String value : values) {
            try {
                result.add(Utils.getMemberFromDescriptor(value, classLoaders));
            }
            catch (ReflectionsException e2) {
                throw new ReflectionsException("Can't resolve member named " + value, e2);
            }
        }
        return result;
    }

    public static Field getFieldFromString(String field, ClassLoader ... classLoaders) {
        String className = field.substring(0, field.lastIndexOf(46));
        String fieldName = field.substring(field.lastIndexOf(46) + 1);
        try {
            return ReflectionUtils.forName(className, classLoaders).getDeclaredField(fieldName);
        }
        catch (NoSuchFieldException e2) {
            throw new ReflectionsException("Can't resolve field named " + fieldName, e2);
        }
    }

    public static void close(InputStream closeable) {
        block3: {
            try {
                if (closeable != null) {
                    closeable.close();
                }
            }
            catch (IOException e2) {
                if (Reflections.log == null) break block3;
                Reflections.log.warn("Could not close InputStream", (Throwable)e2);
            }
        }
    }

    public static Logger findLogger(Class<?> aClass) {
        try {
            Class.forName("org.slf4j.impl.StaticLoggerBinder");
            return LoggerFactory.getLogger(aClass);
        }
        catch (Throwable e2) {
            return null;
        }
    }

    public static boolean isConstructor(String fqn) {
        return fqn.contains("init>");
    }

    public static String name(Class type) {
        if (!type.isArray()) {
            return type.getName();
        }
        int dim = 0;
        while (type.isArray()) {
            ++dim;
            type = type.getComponentType();
        }
        return type.getName() + Utils.repeat("[]", dim);
    }

    public static List<String> names(Collection<Class<?>> types) {
        return types.stream().map(Utils::name).collect(Collectors.toList());
    }

    public static List<String> names(Class<?> ... types) {
        return Utils.names(Arrays.asList(types));
    }

    public static String name(Constructor constructor) {
        return constructor.getName() + ".<init>(" + Utils.join(Utils.names(constructor.getParameterTypes()), ", ") + ")";
    }

    public static String name(Method method) {
        return method.getDeclaringClass().getName() + "." + method.getName() + "(" + Utils.join(Utils.names(method.getParameterTypes()), ", ") + ")";
    }

    public static String name(Field field) {
        return field.getDeclaringClass().getName() + "." + field.getName();
    }

    public static String index(Class<?> scannerClass) {
        return scannerClass.getSimpleName();
    }

    public static <T> Predicate<T> and(Predicate ... predicates) {
        return Arrays.stream(predicates).reduce(t2 -> true, Predicate::and);
    }

    public static String join(Collection<?> elements, String delimiter) {
        return elements.stream().map(Object::toString).collect(Collectors.joining(delimiter));
    }

    public static <T> Set<T> filter(Collection<T> result, Predicate<? super T> ... predicates) {
        return result.stream().filter(Utils.and(predicates)).collect(Collectors.toSet());
    }

    public static <T> Set<T> filter(Collection<T> result, Predicate<? super T> predicate) {
        return result.stream().filter(predicate).collect(Collectors.toSet());
    }

    public static <T> Set<T> filter(T[] result, Predicate<? super T> ... predicates) {
        return Arrays.stream(result).filter(Utils.and(predicates)).collect(Collectors.toSet());
    }
}

