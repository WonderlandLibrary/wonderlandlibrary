/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.serializers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.serializers.Serializer;
import org.reflections.util.Utils;

public class JavaCodeSerializer
implements Serializer {
    private static final String pathSeparator = "_";
    private static final String doubleSeparator = "__";
    private static final String dotSeparator = ".";
    private static final String arrayDescriptor = "$$";
    private static final String tokenSeparator = "_";

    @Override
    public Reflections read(InputStream inputStream) {
        throw new UnsupportedOperationException("read is not implemented on JavaCodeSerializer");
    }

    @Override
    public File save(Reflections reflections, String name) {
        String className;
        String packageName;
        if (name.endsWith("/")) {
            name = name.substring(0, name.length() - 1);
        }
        String filename = name.replace('.', '/').concat(".java");
        File file = Utils.prepareFile(filename);
        int lastDot = name.lastIndexOf(46);
        if (lastDot == -1) {
            packageName = "";
            className = name.substring(name.lastIndexOf(47) + 1);
        } else {
            packageName = name.substring(name.lastIndexOf(47) + 1, lastDot);
            className = name.substring(lastDot + 1);
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("//generated using Reflections JavaCodeSerializer").append(" [").append(new Date()).append("]").append("\n");
            if (packageName.length() != 0) {
                sb.append("package ").append(packageName).append(";\n");
                sb.append("\n");
            }
            sb.append("public interface ").append(className).append(" {\n\n");
            sb.append(this.toString(reflections));
            sb.append("}\n");
            Files.write(new File(filename).toPath(), sb.toString().getBytes(Charset.defaultCharset()), new OpenOption[0]);
        }
        catch (IOException e2) {
            throw new RuntimeException();
        }
        return file;
    }

    @Override
    public String toString(Reflections reflections) {
        if (reflections.getStore().keys(Utils.index(TypeElementsScanner.class)).isEmpty() && Reflections.log != null) {
            Reflections.log.warn("JavaCodeSerializer needs TypeElementsScanner configured");
        }
        StringBuilder sb = new StringBuilder();
        List<Object> prevPaths = new ArrayList();
        int indent = 1;
        ArrayList<String> keys = new ArrayList<String>(reflections.getStore().keys(Utils.index(TypeElementsScanner.class)));
        Collections.sort(keys);
        for (String fqn : keys) {
            int j2;
            int i2;
            List<String> typePaths = Arrays.asList(fqn.split("\\."));
            for (i2 = 0; i2 < Math.min(typePaths.size(), prevPaths.size()) && typePaths.get(i2).equals(prevPaths.get(i2)); ++i2) {
            }
            for (j2 = prevPaths.size(); j2 > i2; --j2) {
                sb.append(Utils.repeat("\t", --indent)).append("}\n");
            }
            for (j2 = i2; j2 < typePaths.size() - 1; ++j2) {
                sb.append(Utils.repeat("\t", indent++)).append("public interface ").append(this.getNonDuplicateName(typePaths.get(j2), typePaths, j2)).append(" {\n");
            }
            String className = typePaths.get(typePaths.size() - 1);
            ArrayList<String> annotations = new ArrayList<String>();
            ArrayList<String> fields = new ArrayList<String>();
            ArrayList<String> methods = new ArrayList<String>();
            Set<String> members = reflections.getStore().get(Utils.index(TypeElementsScanner.class), fqn);
            List sorted = StreamSupport.stream(members.spliterator(), false).sorted().collect(Collectors.toList());
            for (String element : sorted) {
                if (element.startsWith("@")) {
                    annotations.add(element.substring(1));
                    continue;
                }
                if (element.contains("(")) {
                    if (element.startsWith("<")) continue;
                    int i1 = element.indexOf(40);
                    String name = element.substring(0, i1);
                    String params = element.substring(i1 + 1, element.indexOf(")"));
                    String paramsDescriptor = "";
                    if (params.length() != 0) {
                        paramsDescriptor = "_" + params.replace(dotSeparator, "_").replace(", ", doubleSeparator).replace("[]", arrayDescriptor);
                    }
                    String normalized = name + paramsDescriptor;
                    if (!methods.contains(name)) {
                        methods.add(name);
                        continue;
                    }
                    methods.add(normalized);
                    continue;
                }
                if (Utils.isEmpty(element)) continue;
                fields.add(element);
            }
            sb.append(Utils.repeat("\t", indent++)).append("public interface ").append(this.getNonDuplicateName(className, typePaths, typePaths.size() - 1)).append(" {\n");
            if (!fields.isEmpty()) {
                sb.append(Utils.repeat("\t", indent++)).append("public interface fields {\n");
                for (String field : fields) {
                    sb.append(Utils.repeat("\t", indent)).append("public interface ").append(this.getNonDuplicateName(field, typePaths)).append(" {}\n");
                }
                sb.append(Utils.repeat("\t", --indent)).append("}\n");
            }
            if (!methods.isEmpty()) {
                sb.append(Utils.repeat("\t", indent++)).append("public interface methods {\n");
                for (String method : methods) {
                    String methodName = this.getNonDuplicateName(method, fields);
                    sb.append(Utils.repeat("\t", indent)).append("public interface ").append(this.getNonDuplicateName(methodName, typePaths)).append(" {}\n");
                }
                sb.append(Utils.repeat("\t", --indent)).append("}\n");
            }
            if (!annotations.isEmpty()) {
                sb.append(Utils.repeat("\t", indent++)).append("public interface annotations {\n");
                Iterator<Object> iterator = annotations.iterator();
                while (iterator.hasNext()) {
                    String annotation;
                    String nonDuplicateName = annotation = (String)iterator.next();
                    nonDuplicateName = this.getNonDuplicateName(nonDuplicateName, typePaths);
                    sb.append(Utils.repeat("\t", indent)).append("public interface ").append(nonDuplicateName).append(" {}\n");
                }
                sb.append(Utils.repeat("\t", --indent)).append("}\n");
            }
            prevPaths = typePaths;
        }
        for (int j3 = prevPaths.size(); j3 >= 1; --j3) {
            sb.append(Utils.repeat("\t", j3)).append("}\n");
        }
        return sb.toString();
    }

    private String getNonDuplicateName(String candidate, List<String> prev, int offset) {
        String normalized = this.normalize(candidate);
        for (int i2 = 0; i2 < offset; ++i2) {
            if (!normalized.equals(prev.get(i2))) continue;
            return this.getNonDuplicateName(normalized + "_", prev, offset);
        }
        return normalized;
    }

    private String normalize(String candidate) {
        return candidate.replace(dotSeparator, "_");
    }

    private String getNonDuplicateName(String candidate, List<String> prev) {
        return this.getNonDuplicateName(candidate, prev, prev.size());
    }

    public static Class<?> resolveClassOf(Class element) throws ClassNotFoundException {
        LinkedList<String> ognl = new LinkedList<String>();
        for (Class<?> cursor = element; cursor != null; cursor = cursor.getDeclaringClass()) {
            ognl.addFirst(cursor.getSimpleName());
        }
        String classOgnl = Utils.join(ognl.subList(1, ognl.size()), dotSeparator).replace(".$", "$");
        return Class.forName(classOgnl);
    }

    public static Class<?> resolveClass(Class aClass) {
        try {
            return JavaCodeSerializer.resolveClassOf(aClass);
        }
        catch (Exception e2) {
            throw new ReflectionsException("could not resolve to class " + aClass.getName(), e2);
        }
    }

    public static Field resolveField(Class aField) {
        try {
            String name = aField.getSimpleName();
            Class<?> declaringClass = aField.getDeclaringClass().getDeclaringClass();
            return JavaCodeSerializer.resolveClassOf(declaringClass).getDeclaredField(name);
        }
        catch (Exception e2) {
            throw new ReflectionsException("could not resolve to field " + aField.getName(), e2);
        }
    }

    public static Annotation resolveAnnotation(Class annotation) {
        try {
            String name = annotation.getSimpleName().replace("_", dotSeparator);
            Class<?> declaringClass = annotation.getDeclaringClass().getDeclaringClass();
            Class<?> aClass = JavaCodeSerializer.resolveClassOf(declaringClass);
            Class<?> aClass1 = ReflectionUtils.forName(name, new ClassLoader[0]);
            Object annotation1 = aClass.getAnnotation(aClass1);
            return annotation1;
        }
        catch (Exception e2) {
            throw new ReflectionsException("could not resolve to annotation " + annotation.getName(), e2);
        }
    }

    public static Method resolveMethod(Class aMethod) {
        String methodOgnl = aMethod.getSimpleName();
        try {
            Class[] paramTypes;
            String methodName;
            if (methodOgnl.contains("_")) {
                methodName = methodOgnl.substring(0, methodOgnl.indexOf("_"));
                String[] params = methodOgnl.substring(methodOgnl.indexOf("_") + 1).split(doubleSeparator);
                paramTypes = new Class[params.length];
                for (int i2 = 0; i2 < params.length; ++i2) {
                    String typeName = params[i2].replace(arrayDescriptor, "[]").replace("_", dotSeparator);
                    paramTypes[i2] = ReflectionUtils.forName(typeName, new ClassLoader[0]);
                }
            } else {
                methodName = methodOgnl;
                paramTypes = null;
            }
            Class<?> declaringClass = aMethod.getDeclaringClass().getDeclaringClass();
            return JavaCodeSerializer.resolveClassOf(declaringClass).getDeclaredMethod(methodName, paramTypes);
        }
        catch (Exception e2) {
            throw new ReflectionsException("could not resolve to method " + aMethod.getName(), e2);
        }
    }
}

