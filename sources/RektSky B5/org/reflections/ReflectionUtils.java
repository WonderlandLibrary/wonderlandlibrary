/*
 * Decompiled with CFR 0.152.
 */
package org.reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.Utils;

public abstract class ReflectionUtils {
    public static boolean includeObject = false;
    private static List<String> primitiveNames;
    private static List<Class> primitiveTypes;
    private static List<String> primitiveDescriptors;

    public static Set<Class<?>> getAllSuperTypes(Class<?> type, Predicate<? super Class<?>> ... predicates) {
        LinkedHashSet result = new LinkedHashSet();
        if (type != null && (includeObject || !type.equals(Object.class))) {
            result.add(type);
            for (Class<?> supertype : ReflectionUtils.getSuperTypes(type)) {
                result.addAll(ReflectionUtils.getAllSuperTypes(supertype, new Predicate[0]));
            }
        }
        return Utils.filter(result, predicates);
    }

    public static Set<Class<?>> getSuperTypes(Class<?> type) {
        LinkedHashSet result = new LinkedHashSet();
        Class<?> superclass = type.getSuperclass();
        Class<?>[] interfaces = type.getInterfaces();
        if (superclass != null && (includeObject || !superclass.equals(Object.class))) {
            result.add(superclass);
        }
        if (interfaces != null && interfaces.length > 0) {
            result.addAll(Arrays.asList(interfaces));
        }
        return result;
    }

    public static Set<Method> getAllMethods(Class<?> type, Predicate<? super Method> ... predicates) {
        HashSet<Method> result = new HashSet<Method>();
        for (Class<?> t2 : ReflectionUtils.getAllSuperTypes(type, new Predicate[0])) {
            result.addAll(ReflectionUtils.getMethods(t2, predicates));
        }
        return result;
    }

    public static Set<Method> getMethods(Class<?> t2, Predicate<? super Method> ... predicates) {
        return Utils.filter(t2.isInterface() ? t2.getMethods() : t2.getDeclaredMethods(), predicates);
    }

    public static Set<Constructor> getAllConstructors(Class<?> type, Predicate<? super Constructor> ... predicates) {
        HashSet<Constructor> result = new HashSet<Constructor>();
        for (Class<?> t2 : ReflectionUtils.getAllSuperTypes(type, new Predicate[0])) {
            result.addAll(ReflectionUtils.getConstructors(t2, predicates));
        }
        return result;
    }

    public static Set<Constructor> getConstructors(Class<?> t2, Predicate<? super Constructor> ... predicates) {
        return Utils.filter(t2.getDeclaredConstructors(), predicates);
    }

    public static Set<Field> getAllFields(Class<?> type, Predicate<? super Field> ... predicates) {
        HashSet<Field> result = new HashSet<Field>();
        for (Class<?> t2 : ReflectionUtils.getAllSuperTypes(type, new Predicate[0])) {
            result.addAll(ReflectionUtils.getFields(t2, predicates));
        }
        return result;
    }

    public static Set<Field> getFields(Class<?> type, Predicate<? super Field> ... predicates) {
        return Utils.filter(type.getDeclaredFields(), predicates);
    }

    public static <T extends AnnotatedElement> Set<Annotation> getAllAnnotations(T type, Predicate<Annotation> ... predicates) {
        HashSet<Annotation> result = new HashSet<Annotation>();
        if (type instanceof Class) {
            for (Class<?> t2 : ReflectionUtils.getAllSuperTypes((Class)type, new Predicate[0])) {
                result.addAll(ReflectionUtils.getAnnotations(t2, predicates));
            }
        } else {
            result.addAll(ReflectionUtils.getAnnotations(type, predicates));
        }
        return result;
    }

    public static <T extends AnnotatedElement> Set<Annotation> getAnnotations(T type, Predicate<Annotation> ... predicates) {
        return Utils.filter(type.getDeclaredAnnotations(), predicates);
    }

    public static <T extends AnnotatedElement> Set<T> getAll(Set<T> elements, Predicate<? super T> ... predicates) {
        return Utils.filter(elements, predicates);
    }

    public static <T extends Member> Predicate<T> withName(String name) {
        return input -> input != null && input.getName().equals(name);
    }

    public static <T extends Member> Predicate<T> withPrefix(String prefix) {
        return input -> input != null && input.getName().startsWith(prefix);
    }

    public static <T extends AnnotatedElement> Predicate<T> withPattern(String regex) {
        return input -> Pattern.matches(regex, input.toString());
    }

    public static <T extends AnnotatedElement> Predicate<T> withAnnotation(Class<? extends Annotation> annotation) {
        return input -> input != null && input.isAnnotationPresent(annotation);
    }

    public static <T extends AnnotatedElement> Predicate<T> withAnnotations(Class<? extends Annotation> ... annotations) {
        return input -> input != null && Arrays.equals(annotations, ReflectionUtils.annotationTypes(input.getAnnotations()));
    }

    public static <T extends AnnotatedElement> Predicate<T> withAnnotation(Annotation annotation) {
        return input -> input != null && input.isAnnotationPresent(annotation.annotationType()) && ReflectionUtils.areAnnotationMembersMatching(input.getAnnotation(annotation.annotationType()), annotation);
    }

    public static <T extends AnnotatedElement> Predicate<T> withAnnotations(Annotation ... annotations) {
        return input -> {
            Annotation[] inputAnnotations;
            if (input != null && (inputAnnotations = input.getAnnotations()).length == annotations.length) {
                return IntStream.range(0, inputAnnotations.length).allMatch(i2 -> ReflectionUtils.areAnnotationMembersMatching(inputAnnotations[i2], annotations[i2]));
            }
            return true;
        };
    }

    public static Predicate<Member> withParameters(Class<?> ... types) {
        return input -> Arrays.equals(ReflectionUtils.parameterTypes(input), types);
    }

    public static Predicate<Member> withParametersAssignableTo(Class ... types) {
        return input -> ReflectionUtils.isAssignable(types, ReflectionUtils.parameterTypes(input));
    }

    public static Predicate<Member> withParametersAssignableFrom(Class ... types) {
        return input -> ReflectionUtils.isAssignable(ReflectionUtils.parameterTypes(input), types);
    }

    public static Predicate<Member> withParametersCount(int count) {
        return input -> input != null && ReflectionUtils.parameterTypes(input).length == count;
    }

    public static Predicate<Member> withAnyParameterAnnotation(Class<? extends Annotation> annotationClass) {
        return input -> input != null && ReflectionUtils.annotationTypes(ReflectionUtils.parameterAnnotations(input)).stream().anyMatch(input1 -> input1.equals(annotationClass));
    }

    public static Predicate<Member> withAnyParameterAnnotation(Annotation annotation) {
        return input -> input != null && ReflectionUtils.parameterAnnotations(input).stream().anyMatch(input1 -> ReflectionUtils.areAnnotationMembersMatching(annotation, input1));
    }

    public static <T> Predicate<Field> withType(Class<T> type) {
        return input -> input != null && input.getType().equals(type);
    }

    public static <T> Predicate<Field> withTypeAssignableTo(Class<T> type) {
        return input -> input != null && type.isAssignableFrom(input.getType());
    }

    public static <T> Predicate<Method> withReturnType(Class<T> type) {
        return input -> input != null && input.getReturnType().equals(type);
    }

    public static <T> Predicate<Method> withReturnTypeAssignableTo(Class<T> type) {
        return input -> input != null && type.isAssignableFrom(input.getReturnType());
    }

    public static <T extends Member> Predicate<T> withModifier(int mod) {
        return input -> input != null && (input.getModifiers() & mod) != 0;
    }

    public static Predicate<Class<?>> withClassModifier(int mod) {
        return input -> input != null && (input.getModifiers() & mod) != 0;
    }

    public static Class<?> forName(String typeName, ClassLoader ... classLoaders) {
        String type;
        if (ReflectionUtils.getPrimitiveNames().contains(typeName)) {
            return ReflectionUtils.getPrimitiveTypes().get(ReflectionUtils.getPrimitiveNames().indexOf(typeName));
        }
        if (typeName.contains("[")) {
            int i2 = typeName.indexOf("[");
            type = typeName.substring(0, i2);
            String array = typeName.substring(i2).replace("]", "");
            type = ReflectionUtils.getPrimitiveNames().contains(type) ? ReflectionUtils.getPrimitiveDescriptors().get(ReflectionUtils.getPrimitiveNames().indexOf(type)) : "L" + type + ";";
            type = array + type;
        } else {
            type = typeName;
        }
        ArrayList<ReflectionsException> reflectionsExceptions = new ArrayList<ReflectionsException>();
        for (ClassLoader classLoader : ClasspathHelper.classLoaders(classLoaders)) {
            if (type.contains("[")) {
                try {
                    return Class.forName(type, false, classLoader);
                }
                catch (Throwable e2) {
                    reflectionsExceptions.add(new ReflectionsException("could not get type for name " + typeName, e2));
                }
            }
            try {
                return classLoader.loadClass(type);
            }
            catch (Throwable e3) {
                reflectionsExceptions.add(new ReflectionsException("could not get type for name " + typeName, e3));
            }
        }
        if (Reflections.log != null) {
            for (ReflectionsException reflectionsException : reflectionsExceptions) {
                Reflections.log.warn("could not get type for name " + typeName + " from any class loader", (Throwable)reflectionsException);
            }
        }
        return null;
    }

    public static <T> Set<Class<? extends T>> forNames(Collection<String> classes, ClassLoader ... classLoaders) {
        return classes.stream().map(className -> ReflectionUtils.forName(className, classLoaders)).filter(Objects::nonNull).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private static Class[] parameterTypes(Member member) {
        return member != null ? (member.getClass() == Method.class ? ((Method)member).getParameterTypes() : (member.getClass() == Constructor.class ? ((Constructor)member).getParameterTypes() : null)) : null;
    }

    private static Set<Annotation> parameterAnnotations(Member member) {
        Annotation[][] annotations = member instanceof Method ? ((Method)member).getParameterAnnotations() : (member instanceof Constructor ? ((Constructor)member).getParameterAnnotations() : (Annotation[][])null);
        return Arrays.stream(annotations).flatMap(Arrays::stream).collect(Collectors.toSet());
    }

    private static Set<Class<? extends Annotation>> annotationTypes(Collection<Annotation> annotations) {
        return annotations.stream().map(Annotation::annotationType).collect(Collectors.toSet());
    }

    private static Class<? extends Annotation>[] annotationTypes(Annotation[] annotations) {
        return (Class[])Arrays.stream(annotations).map(Annotation::annotationType).toArray(Class[]::new);
    }

    private static void initPrimitives() {
        if (primitiveNames == null) {
            primitiveNames = Arrays.asList("boolean", "char", "byte", "short", "int", "long", "float", "double", "void");
            primitiveTypes = Arrays.asList(Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE);
            primitiveDescriptors = Arrays.asList("Z", "C", "B", "S", "I", "J", "F", "D", "V");
        }
    }

    private static List<String> getPrimitiveNames() {
        ReflectionUtils.initPrimitives();
        return primitiveNames;
    }

    private static List<Class> getPrimitiveTypes() {
        ReflectionUtils.initPrimitives();
        return primitiveTypes;
    }

    private static List<String> getPrimitiveDescriptors() {
        ReflectionUtils.initPrimitives();
        return primitiveDescriptors;
    }

    private static boolean areAnnotationMembersMatching(Annotation annotation1, Annotation annotation2) {
        if (annotation2 != null && annotation1.annotationType() == annotation2.annotationType()) {
            for (Method method : annotation1.annotationType().getDeclaredMethods()) {
                try {
                    if (method.invoke(annotation1, new Object[0]).equals(method.invoke(annotation2, new Object[0]))) continue;
                    return false;
                }
                catch (Exception e2) {
                    throw new ReflectionsException(String.format("could not invoke method %s on annotation %s", method.getName(), annotation1.annotationType()), e2);
                }
            }
            return true;
        }
        return false;
    }

    private static boolean isAssignable(Class[] childClasses, Class[] parentClasses) {
        if (childClasses == null) {
            return parentClasses == null || parentClasses.length == 0;
        }
        if (childClasses.length != parentClasses.length) {
            return false;
        }
        return IntStream.range(0, childClasses.length).noneMatch(i2 -> !parentClasses[i2].isAssignableFrom(childClasses[i2]) || parentClasses[i2] == Object.class && childClasses[i2] != Object.class);
    }
}

