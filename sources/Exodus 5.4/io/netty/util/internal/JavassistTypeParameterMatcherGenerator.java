/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javassist.ClassClassPath
 *  javassist.ClassPath
 *  javassist.ClassPool
 *  javassist.CtClass
 *  javassist.NotFoundException
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

    private JavassistTypeParameterMatcherGenerator() {
    }

    public static void appendClassPath(ClassPath classPath) {
        classPool.appendClassPath(classPath);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static TypeParameterMatcher generate(Class<?> clazz, ClassLoader classLoader) {
        String string = JavassistTypeParameterMatcherGenerator.typeName(clazz);
        String string2 = "io.netty.util.internal.__matchers__." + string + "Matcher";
        try {
            return (TypeParameterMatcher)Class.forName(string2, true, classLoader).newInstance();
        }
        catch (Exception exception) {
            try {
                CtClass ctClass = classPool.getAndRename(NoOpTypeParameterMatcher.class.getName(), string2);
                ctClass.setModifiers(ctClass.getModifiers() | 0x10);
                ctClass.getDeclaredMethod("match").setBody("{ return $1 instanceof " + string + "; }");
                byte[] byArray = ctClass.toBytecode();
                ctClass.detach();
                Method method = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
                method.setAccessible(true);
                Class clazz2 = (Class)method.invoke(classLoader, string2, byArray, 0, byArray.length);
                if (clazz != Object.class) {
                    logger.debug("Generated: {}", (Object)clazz2.getName());
                }
                return (TypeParameterMatcher)clazz2.newInstance();
            }
            catch (Exception exception2) {
                throw new RuntimeException(exception2);
            }
        }
    }

    static {
        classPool.appendClassPath((ClassPath)new ClassClassPath(NoOpTypeParameterMatcher.class));
    }

    private static String typeName(Class<?> clazz) {
        if (clazz.isArray()) {
            return JavassistTypeParameterMatcherGenerator.typeName(clazz.getComponentType()) + "[]";
        }
        return clazz.getName();
    }

    public static void appendClassPath(String string) throws NotFoundException {
        classPool.appendClassPath(string);
    }

    public static TypeParameterMatcher generate(Class<?> clazz) {
        ClassLoader classLoader = PlatformDependent.getContextClassLoader();
        if (classLoader == null) {
            classLoader = PlatformDependent.getSystemClassLoader();
        }
        return JavassistTypeParameterMatcherGenerator.generate(clazz, classLoader);
    }
}

