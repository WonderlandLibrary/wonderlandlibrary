/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import io.netty.util.internal.NoOpTypeParameterMatcher;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.TypeParameterMatcher;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.reflect.Method;
import javassist.ClassClassPath;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public final class JavassistTypeParameterMatcherGenerator {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(JavassistTypeParameterMatcherGenerator.class);
    private static final ClassPool classPool = new ClassPool(true);

    public static void appendClassPath(ClassPath classpath) {
        classPool.appendClassPath(classpath);
    }

    public static void appendClassPath(String pathname) throws NotFoundException {
        classPool.appendClassPath(pathname);
    }

    public static TypeParameterMatcher generate(Class<?> type) {
        ClassLoader classLoader = PlatformDependent.getContextClassLoader();
        if (classLoader == null) {
            classLoader = PlatformDependent.getSystemClassLoader();
        }
        return JavassistTypeParameterMatcherGenerator.generate(type, classLoader);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static TypeParameterMatcher generate(Class<?> type, ClassLoader classLoader) {
        String typeName = JavassistTypeParameterMatcherGenerator.typeName(type);
        String className = "io.netty.util.internal.__matchers__." + typeName + "Matcher";
        try {
            return (TypeParameterMatcher)Class.forName(className, true, classLoader).newInstance();
        }
        catch (Exception e2) {
            try {
                CtClass c2 = classPool.getAndRename(NoOpTypeParameterMatcher.class.getName(), className);
                c2.setModifiers(c2.getModifiers() | 0x10);
                c2.getDeclaredMethod("match").setBody("{ return $1 instanceof " + typeName + "; }");
                byte[] byteCode = c2.toBytecode();
                c2.detach();
                Method method = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
                method.setAccessible(true);
                Class generated = (Class)method.invoke(classLoader, className, byteCode, 0, byteCode.length);
                if (type != Object.class) {
                    logger.debug("Generated: {}", (Object)generated.getName());
                }
                return (TypeParameterMatcher)generated.newInstance();
            }
            catch (Exception e3) {
                throw new RuntimeException(e3);
            }
        }
    }

    private static String typeName(Class<?> type) {
        if (type.isArray()) {
            return JavassistTypeParameterMatcherGenerator.typeName(type.getComponentType()) + "[]";
        }
        return type.getName();
    }

    private JavassistTypeParameterMatcherGenerator() {
    }

    static {
        classPool.appendClassPath(new ClassClassPath(NoOpTypeParameterMatcher.class));
    }
}

