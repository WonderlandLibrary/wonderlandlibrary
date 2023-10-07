/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.PrototypeObject;
import jdk.nashorn.internal.runtime.ScriptFunction;

final class NativeRegExp$Prototype
extends PrototypeObject {
    private Object compile = ScriptFunction.createBuiltin("compile", /* method handle: compile(java.lang.Object java.lang.Object java.lang.Object ) */ null);
    private Object exec = ScriptFunction.createBuiltin("exec", /* method handle: exec(java.lang.Object java.lang.Object ) */ null);
    private Object test = ScriptFunction.createBuiltin("test", /* method handle: test(java.lang.Object java.lang.Object ) */ null);
    private Object toString = ScriptFunction.createBuiltin("toString", /* method handle: toString(java.lang.Object ) */ null);
    private static final PropertyMap $nasgenmap$;

    public Object G$compile() {
        return this.compile;
    }

    public void S$compile(Object object) {
        this.compile = object;
    }

    public Object G$exec() {
        return this.exec;
    }

    public void S$exec(Object object) {
        this.exec = object;
    }

    public Object G$test() {
        return this.test;
    }

    public void S$test(Object object) {
        this.test = object;
    }

    public Object G$toString() {
        return this.toString;
    }

    public void S$toString(Object object) {
        this.toString = object;
    }

    static {
        ArrayList<Property> arrayList = new ArrayList<Property>(4);
        arrayList.add(AccessorProperty.create("compile", 2, cfr_ldc_0(), cfr_ldc_1()));
        arrayList.add(AccessorProperty.create("exec", 2, cfr_ldc_2(), cfr_ldc_3()));
        arrayList.add(AccessorProperty.create("test", 2, cfr_ldc_4(), cfr_ldc_5()));
        arrayList.add(AccessorProperty.create("toString", 2, cfr_ldc_6(), cfr_ldc_7()));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    NativeRegExp$Prototype() {
        super($nasgenmap$);
    }

    @Override
    public String getClassName() {
        return "RegExp";
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(NativeRegExp$Prototype.class, "G$compile", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_1() {
        try {
            return MethodHandles.lookup().findVirtual(NativeRegExp$Prototype.class, "S$compile", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_2() {
        try {
            return MethodHandles.lookup().findVirtual(NativeRegExp$Prototype.class, "G$exec", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_3() {
        try {
            return MethodHandles.lookup().findVirtual(NativeRegExp$Prototype.class, "S$exec", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_4() {
        try {
            return MethodHandles.lookup().findVirtual(NativeRegExp$Prototype.class, "G$test", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_5() {
        try {
            return MethodHandles.lookup().findVirtual(NativeRegExp$Prototype.class, "S$test", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_6() {
        try {
            return MethodHandles.lookup().findVirtual(NativeRegExp$Prototype.class, "G$toString", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_7() {
        try {
            return MethodHandles.lookup().findVirtual(NativeRegExp$Prototype.class, "S$toString", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

