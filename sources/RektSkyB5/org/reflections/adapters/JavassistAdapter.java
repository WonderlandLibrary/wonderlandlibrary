/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.adapters;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.Descriptor;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import org.reflections.ReflectionsException;
import org.reflections.adapters.MetadataAdapter;
import org.reflections.util.Utils;
import org.reflections.vfs.Vfs;

public class JavassistAdapter
implements MetadataAdapter<ClassFile, FieldInfo, MethodInfo> {
    public static boolean includeInvisibleTag = true;

    @Override
    public List<FieldInfo> getFields(ClassFile cls) {
        return cls.getFields();
    }

    @Override
    public List<MethodInfo> getMethods(ClassFile cls) {
        return cls.getMethods();
    }

    @Override
    public String getMethodName(MethodInfo method) {
        return method.getName();
    }

    @Override
    public List<String> getParameterNames(MethodInfo method) {
        String descriptor = method.getDescriptor();
        descriptor = descriptor.substring(descriptor.indexOf("(") + 1, descriptor.lastIndexOf(")"));
        return this.splitDescriptorToTypeNames(descriptor);
    }

    @Override
    public List<String> getClassAnnotationNames(ClassFile aClass) {
        return this.getAnnotationNames((AnnotationsAttribute)aClass.getAttribute("RuntimeVisibleAnnotations"), includeInvisibleTag ? (AnnotationsAttribute)aClass.getAttribute("RuntimeInvisibleAnnotations") : null);
    }

    @Override
    public List<String> getFieldAnnotationNames(FieldInfo field) {
        return this.getAnnotationNames((AnnotationsAttribute)field.getAttribute("RuntimeVisibleAnnotations"), includeInvisibleTag ? (AnnotationsAttribute)field.getAttribute("RuntimeInvisibleAnnotations") : null);
    }

    @Override
    public List<String> getMethodAnnotationNames(MethodInfo method) {
        return this.getAnnotationNames((AnnotationsAttribute)method.getAttribute("RuntimeVisibleAnnotations"), includeInvisibleTag ? (AnnotationsAttribute)method.getAttribute("RuntimeInvisibleAnnotations") : null);
    }

    @Override
    public List<String> getParameterAnnotationNames(MethodInfo method, int parameterIndex) {
        ArrayList<String> result = new ArrayList<String>();
        List<ParameterAnnotationsAttribute> parameterAnnotationsAttributes = Arrays.asList((ParameterAnnotationsAttribute)method.getAttribute("RuntimeVisibleParameterAnnotations"), (ParameterAnnotationsAttribute)method.getAttribute("RuntimeInvisibleParameterAnnotations"));
        for (ParameterAnnotationsAttribute parameterAnnotationsAttribute : parameterAnnotationsAttributes) {
            Annotation[][] annotations;
            if (parameterAnnotationsAttribute == null || parameterIndex >= (annotations = parameterAnnotationsAttribute.getAnnotations()).length) continue;
            Annotation[] annotation = annotations[parameterIndex];
            result.addAll(this.getAnnotationNames(annotation));
        }
        return result;
    }

    @Override
    public String getReturnTypeName(MethodInfo method) {
        String descriptor = method.getDescriptor();
        descriptor = descriptor.substring(descriptor.lastIndexOf(")") + 1);
        return this.splitDescriptorToTypeNames(descriptor).get(0);
    }

    @Override
    public String getFieldName(FieldInfo field) {
        return field.getName();
    }

    @Override
    public ClassFile getOrCreateClassObject(Vfs.File file) {
        InputStream inputStream = null;
        try {
            inputStream = file.openInputStream();
            DataInputStream dis = new DataInputStream(new BufferedInputStream(inputStream));
            ClassFile classFile = new ClassFile(dis);
            return classFile;
        }
        catch (IOException e2) {
            throw new ReflectionsException("could not create class file from " + file.getName(), e2);
        }
        finally {
            Utils.close(inputStream);
        }
    }

    @Override
    public String getMethodModifier(MethodInfo method) {
        int accessFlags = method.getAccessFlags();
        return AccessFlag.isPrivate(accessFlags) ? "private" : (AccessFlag.isProtected(accessFlags) ? "protected" : (this.isPublic(accessFlags) ? "public" : ""));
    }

    @Override
    public String getMethodKey(ClassFile cls, MethodInfo method) {
        return this.getMethodName(method) + "(" + Utils.join(this.getParameterNames(method), ", ") + ")";
    }

    @Override
    public String getMethodFullKey(ClassFile cls, MethodInfo method) {
        return this.getClassName(cls) + "." + this.getMethodKey(cls, method);
    }

    @Override
    public boolean isPublic(Object o2) {
        Integer accessFlags = o2 instanceof ClassFile ? ((ClassFile)o2).getAccessFlags() : (o2 instanceof FieldInfo ? ((FieldInfo)o2).getAccessFlags() : (o2 instanceof MethodInfo ? Integer.valueOf(((MethodInfo)o2).getAccessFlags()) : null).intValue());
        return accessFlags != null && AccessFlag.isPublic(accessFlags);
    }

    @Override
    public String getClassName(ClassFile cls) {
        return cls.getName();
    }

    @Override
    public String getSuperclassName(ClassFile cls) {
        return cls.getSuperclass();
    }

    @Override
    public List<String> getInterfacesNames(ClassFile cls) {
        return Arrays.asList(cls.getInterfaces());
    }

    @Override
    public boolean acceptsInput(String file) {
        return file.endsWith(".class");
    }

    private List<String> getAnnotationNames(AnnotationsAttribute ... annotationsAttributes) {
        if (annotationsAttributes != null) {
            return Arrays.stream(annotationsAttributes).filter(Objects::nonNull).flatMap(annotationsAttribute -> Arrays.stream(annotationsAttribute.getAnnotations())).map(Annotation::getTypeName).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private List<String> getAnnotationNames(Annotation[] annotations) {
        return Arrays.stream(annotations).map(Annotation::getTypeName).collect(Collectors.toList());
    }

    private List<String> splitDescriptorToTypeNames(String descriptors) {
        List<String> result = new ArrayList<String>();
        if (descriptors != null && descriptors.length() != 0) {
            ArrayList<Integer> indices = new ArrayList<Integer>();
            Descriptor.Iterator iterator = new Descriptor.Iterator(descriptors);
            while (iterator.hasNext()) {
                indices.add(iterator.next());
            }
            indices.add(descriptors.length());
            result = IntStream.range(0, indices.size() - 1).mapToObj(i2 -> Descriptor.toString(descriptors.substring((Integer)indices.get(i2), (Integer)indices.get(i2 + 1)))).collect(Collectors.toList());
        }
        return result;
    }
}

