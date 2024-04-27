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
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;

final class NativeJSON$Constructor
extends ScriptObject {
    private Object parse = ScriptFunction.createBuiltin("parse", /* method handle: parse(java.lang.Object java.lang.Object java.lang.Object ) */ null);
    private Object stringify = ScriptFunction.createBuiltin("stringify", /* method handle: stringify(java.lang.Object java.lang.Object java.lang.Object java.lang.Object ) */ null);
    private static final PropertyMap $nasgenmap$;

    public Object G$parse() {
        return this.parse;
    }

    public void S$parse(Object object) {
        this.parse = object;
    }

    public Object G$stringify() {
        return this.stringify;
    }

    public void S$stringify(Object object) {
        this.stringify = object;
    }

    static {
        ArrayList<Property> arrayList = new ArrayList<Property>(2);
        arrayList.add(AccessorProperty.create("parse", 2, cfr_ldc_0(), cfr_ldc_1()));
        arrayList.add(AccessorProperty.create("stringify", 2, cfr_ldc_2(), cfr_ldc_3()));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    NativeJSON$Constructor() {
        super($nasgenmap$);
    }

    @Override
    public String getClassName() {
        return "JSON";
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJSON$Constructor.class, "G$parse", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeJSON$Constructor.class, "S$parse", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeJSON$Constructor.class, "G$stringify", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeJSON$Constructor.class, "S$stringify", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

