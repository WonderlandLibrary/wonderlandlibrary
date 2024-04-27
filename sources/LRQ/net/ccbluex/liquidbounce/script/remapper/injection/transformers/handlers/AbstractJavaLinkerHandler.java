/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmStatic
 *  org.objectweb.asm.Type
 */
package net.ccbluex.liquidbounce.script.remapper.injection.transformers.handlers;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import kotlin.jvm.JvmStatic;
import net.ccbluex.liquidbounce.script.remapper.Remapper;
import org.objectweb.asm.Type;

public final class AbstractJavaLinkerHandler {
    public static final AbstractJavaLinkerHandler INSTANCE;

    @JvmStatic
    public static final String addMember(Class<?> clazz, String name, AccessibleObject accessibleObject) {
        if (!(accessibleObject instanceof Method)) {
            return name;
        }
        Class<?> currentClass = clazz;
        while (currentClass.getName().equals("java.lang.Object") ^ true) {
            String remapped = Remapper.INSTANCE.remapMethod(currentClass, name, Type.getMethodDescriptor((Method)((Method)accessibleObject)));
            if (remapped.equals(name) ^ true) {
                return remapped;
            }
            if (currentClass.getSuperclass() == null) break;
            currentClass = currentClass.getSuperclass();
        }
        return name;
    }

    @JvmStatic
    public static final String addMember(Class<?> clazz, String name) {
        Class<?> currentClass = clazz;
        while (currentClass.getName().equals("java.lang.Object") ^ true) {
            String remapped = Remapper.INSTANCE.remapField(currentClass, name);
            if (remapped.equals(name) ^ true) {
                return remapped;
            }
            if (currentClass.getSuperclass() == null) break;
            currentClass = currentClass.getSuperclass();
        }
        return name;
    }

    @JvmStatic
    public static final String setPropertyGetter(Class<?> clazz, String name) {
        Class<?> currentClass = clazz;
        while (currentClass.getName().equals("java.lang.Object") ^ true) {
            String remapped = Remapper.INSTANCE.remapField(currentClass, name);
            if (remapped.equals(name) ^ true) {
                return remapped;
            }
            if (currentClass.getSuperclass() == null) break;
            currentClass = currentClass.getSuperclass();
        }
        return name;
    }

    private AbstractJavaLinkerHandler() {
    }

    static {
        AbstractJavaLinkerHandler abstractJavaLinkerHandler;
        INSTANCE = abstractJavaLinkerHandler = new AbstractJavaLinkerHandler();
    }
}

