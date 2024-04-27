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

    private AbstractJavaLinkerHandler() {
    }

    @JvmStatic
    public static final String setPropertyGetter(Class clazz, String string) {
        Class clazz2 = clazz;
        while (clazz2.getName().equals("java.lang.Object") ^ true) {
            String string2 = Remapper.INSTANCE.remapField(clazz2, string);
            if (string2.equals(string) ^ true) {
                return string2;
            }
            if (clazz2.getSuperclass() == null) break;
            clazz2 = clazz2.getSuperclass();
        }
        return string;
    }

    @JvmStatic
    public static final String addMember(Class clazz, String string) {
        Class clazz2 = clazz;
        while (clazz2.getName().equals("java.lang.Object") ^ true) {
            String string2 = Remapper.INSTANCE.remapField(clazz2, string);
            if (string2.equals(string) ^ true) {
                return string2;
            }
            if (clazz2.getSuperclass() == null) break;
            clazz2 = clazz2.getSuperclass();
        }
        return string;
    }

    @JvmStatic
    public static final String addMember(Class clazz, String string, AccessibleObject accessibleObject) {
        if (!(accessibleObject instanceof Method)) {
            return string;
        }
        Class clazz2 = clazz;
        while (clazz2.getName().equals("java.lang.Object") ^ true) {
            String string2 = Remapper.INSTANCE.remapMethod(clazz2, string, Type.getMethodDescriptor((Method)((Method)accessibleObject)));
            if (string2.equals(string) ^ true) {
                return string2;
            }
            if (clazz2.getSuperclass() == null) break;
            clazz2 = clazz2.getSuperclass();
        }
        return string;
    }

    static {
        AbstractJavaLinkerHandler abstractJavaLinkerHandler;
        INSTANCE = abstractJavaLinkerHandler = new AbstractJavaLinkerHandler();
    }
}

