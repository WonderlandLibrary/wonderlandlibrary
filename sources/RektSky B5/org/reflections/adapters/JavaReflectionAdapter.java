/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.adapters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.reflections.ReflectionUtils;
import org.reflections.adapters.MetadataAdapter;
import org.reflections.util.Utils;
import org.reflections.vfs.Vfs;

public class JavaReflectionAdapter
implements MetadataAdapter<Class, Field, Member> {
    @Override
    public List<Field> getFields(Class cls) {
        return Arrays.asList(cls.getDeclaredFields());
    }

    @Override
    public List<Member> getMethods(Class cls) {
        ArrayList<Member> methods = new ArrayList<Member>();
        methods.addAll(Arrays.asList(cls.getDeclaredMethods()));
        methods.addAll(Arrays.asList(cls.getDeclaredConstructors()));
        return methods;
    }

    @Override
    public String getMethodName(Member method) {
        return method instanceof Method ? method.getName() : (method instanceof Constructor ? "<init>" : null);
    }

    @Override
    public List<String> getParameterNames(Member member) {
        Class<?>[] parameterTypes = member instanceof Method ? ((Method)member).getParameterTypes() : (member instanceof Constructor ? ((Constructor)member).getParameterTypes() : null);
        return parameterTypes != null ? Arrays.stream(parameterTypes).map(JavaReflectionAdapter::getName).collect(Collectors.toList()) : Collections.emptyList();
    }

    @Override
    public List<String> getClassAnnotationNames(Class aClass) {
        return this.getAnnotationNames(aClass.getDeclaredAnnotations());
    }

    @Override
    public List<String> getFieldAnnotationNames(Field field) {
        return this.getAnnotationNames(field.getDeclaredAnnotations());
    }

    @Override
    public List<String> getMethodAnnotationNames(Member method) {
        Annotation[] annotations = method instanceof Method ? ((Method)method).getDeclaredAnnotations() : (method instanceof Constructor ? ((Constructor)method).getDeclaredAnnotations() : null);
        return this.getAnnotationNames(annotations);
    }

    @Override
    public List<String> getParameterAnnotationNames(Member method, int parameterIndex) {
        Annotation[][] annotations = method instanceof Method ? ((Method)method).getParameterAnnotations() : (method instanceof Constructor ? ((Constructor)method).getParameterAnnotations() : (Annotation[][])null);
        return this.getAnnotationNames(annotations != null ? annotations[parameterIndex] : null);
    }

    @Override
    public String getReturnTypeName(Member method) {
        return ((Method)method).getReturnType().getName();
    }

    @Override
    public String getFieldName(Field field) {
        return field.getName();
    }

    @Override
    public Class getOrCreateClassObject(Vfs.File file) throws Exception {
        return this.getOrCreateClassObject(file, null);
    }

    public Class getOrCreateClassObject(Vfs.File file, ClassLoader ... loaders) throws Exception {
        String name = file.getRelativePath().replace("/", ".").replace(".class", "");
        return ReflectionUtils.forName(name, loaders);
    }

    @Override
    public String getMethodModifier(Member method) {
        return Modifier.toString(method.getModifiers());
    }

    @Override
    public String getMethodKey(Class cls, Member method) {
        return this.getMethodName(method) + "(" + Utils.join(this.getParameterNames(method), ", ") + ")";
    }

    @Override
    public String getMethodFullKey(Class cls, Member method) {
        return this.getClassName(cls) + "." + this.getMethodKey(cls, method);
    }

    @Override
    public boolean isPublic(Object o2) {
        Integer mod = o2 instanceof Class ? ((Class)o2).getModifiers() : (o2 instanceof Member ? Integer.valueOf(((Member)o2).getModifiers()) : null).intValue();
        return mod != null && Modifier.isPublic(mod);
    }

    @Override
    public String getClassName(Class cls) {
        return cls.getName();
    }

    @Override
    public String getSuperclassName(Class cls) {
        Class superclass = cls.getSuperclass();
        return superclass != null ? superclass.getName() : "";
    }

    @Override
    public List<String> getInterfacesNames(Class cls) {
        Class<?>[] classes = cls.getInterfaces();
        return classes != null ? Arrays.stream(classes).map(Class::getName).collect(Collectors.toList()) : Collections.emptyList();
    }

    @Override
    public boolean acceptsInput(String file) {
        return file.endsWith(".class");
    }

    private List<String> getAnnotationNames(Annotation[] annotations) {
        return Arrays.stream(annotations).map(annotation -> annotation.annotationType().getName()).collect(Collectors.toList());
    }

    public static String getName(Class type) {
        if (type.isArray()) {
            try {
                Class<?> cl = type;
                int dim = 0;
                while (cl.isArray()) {
                    ++dim;
                    cl = cl.getComponentType();
                }
                return cl.getName() + Utils.repeat("[]", dim);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        return type.getName();
    }
}

