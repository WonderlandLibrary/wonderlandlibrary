package dev.zprestige.prestige.client.protection.check.impl;

import dev.zprestige.prestige.client.managers.ProtectionManager;
import dev.zprestige.prestige.client.protection.check.Category;
import dev.zprestige.prestige.client.protection.check.Check;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DumpCheck extends Check {

    public Method method;
    public static String[] classes = {"sun.instrument.InstrumentationImpl", "java.lang.instrument.Instrumentation", "java.lang.instrument.ClassDefinition", "java.lang.instrument.ClassFileTransformer", "java.lang.instrument.IllegalClassFormatException", "java.lang.instrument.UnmodifiableClassException"};

    public DumpCheck() {
        super(Category.Normal);
        try {
            method = ClassLoader.class.getDeclaredMethod("findLoadedClass0", String.class);
        }
        catch (NoSuchMethodException ignored) {
        }
    }

    @Override
    public void run() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        if (method != null) {
            for (String string : classes) {
                Object object;
                try {
                    object = method.invoke(classLoader, string);
                }
                catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
                    return;
                }
                if (object != null) {
                    ProtectionManager.exit("C");
                }
            }
        }
    }
}
