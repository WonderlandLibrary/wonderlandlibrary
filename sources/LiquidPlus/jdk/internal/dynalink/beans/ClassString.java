/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.LinkedList;
import java.util.List;
import jdk.internal.dynalink.beans.MaximallySpecific;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.support.Guards;
import jdk.internal.dynalink.support.TypeUtilities;

final class ClassString {
    static final Class<?> NULL_CLASS = new Object(){}.getClass();
    private final Class<?>[] classes;
    private int hashCode;

    ClassString(Class<?>[] classes) {
        this.classes = classes;
    }

    ClassString(MethodType type) {
        this(type.parameterArray());
    }

    public boolean equals(Object other) {
        if (!(other instanceof ClassString)) {
            return false;
        }
        Class<?>[] otherClasses = ((ClassString)other).classes;
        if (otherClasses.length != this.classes.length) {
            return false;
        }
        for (int i = 0; i < otherClasses.length; ++i) {
            if (otherClasses[i] == this.classes[i]) continue;
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            int h = 0;
            for (int i = 0; i < this.classes.length; ++i) {
                h ^= this.classes[i].hashCode();
            }
            this.hashCode = h;
        }
        return this.hashCode;
    }

    boolean isVisibleFrom(ClassLoader classLoader) {
        for (int i = 0; i < this.classes.length; ++i) {
            if (Guards.canReferenceDirectly(classLoader, this.classes[i].getClassLoader())) continue;
            return false;
        }
        return true;
    }

    List<MethodHandle> getMaximallySpecifics(List<MethodHandle> methods, LinkerServices linkerServices, boolean varArg) {
        return MaximallySpecific.getMaximallySpecificMethodHandles(this.getApplicables(methods, linkerServices, varArg), varArg, this.classes, linkerServices);
    }

    LinkedList<MethodHandle> getApplicables(List<MethodHandle> methods, LinkerServices linkerServices, boolean varArg) {
        LinkedList<MethodHandle> list = new LinkedList<MethodHandle>();
        for (MethodHandle member : methods) {
            if (!this.isApplicable(member, linkerServices, varArg)) continue;
            list.add(member);
        }
        return list;
    }

    private boolean isApplicable(MethodHandle method, LinkerServices linkerServices, boolean varArg) {
        Class<?>[] formalTypes = method.type().parameterArray();
        int cl = this.classes.length;
        int fl = formalTypes.length - (varArg ? 1 : 0);
        if (varArg ? cl < fl : cl != fl) {
            return false;
        }
        for (int i = 1; i < fl; ++i) {
            if (ClassString.canConvert(linkerServices, this.classes[i], formalTypes[i])) continue;
            return false;
        }
        if (varArg) {
            Class<?> varArgType = formalTypes[fl].getComponentType();
            for (int i = fl; i < cl; ++i) {
                if (ClassString.canConvert(linkerServices, this.classes[i], varArgType)) continue;
                return false;
            }
        }
        return true;
    }

    private static boolean canConvert(LinkerServices ls, Class<?> from, Class<?> to) {
        if (from == NULL_CLASS) {
            return !to.isPrimitive();
        }
        return ls == null ? TypeUtilities.isMethodInvocationConvertible(from, to) : ls.canConvert(from, to);
    }
}

