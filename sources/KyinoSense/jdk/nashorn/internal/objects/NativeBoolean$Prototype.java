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

final class NativeBoolean$Prototype
extends PrototypeObject {
    private Object toString = ScriptFunction.createBuiltin("toString", /* method handle: toString(java.lang.Object ) */ null);
    private Object valueOf = ScriptFunction.createBuiltin("valueOf", /* method handle: valueOf(java.lang.Object ) */ null);
    private static final PropertyMap $nasgenmap$;

    public Object G$toString() {
        return this.toString;
    }

    public void S$toString(Object object) {
        this.toString = object;
    }

    public Object G$valueOf() {
        return this.valueOf;
    }

    public void S$valueOf(Object object) {
        this.valueOf = object;
    }

    static {
        ArrayList<Property> arrayList = new ArrayList<Property>(2);
        arrayList.add(AccessorProperty.create("toString", 2, cfr_ldc_0(), cfr_ldc_1()));
        arrayList.add(AccessorProperty.create("valueOf", 2, cfr_ldc_2(), cfr_ldc_3()));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    NativeBoolean$Prototype() {
        super($nasgenmap$);
    }

    @Override
    public String getClassName() {
        return "Boolean";
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(NativeBoolean$Prototype.class, "G$toString", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeBoolean$Prototype.class, "S$toString", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeBoolean$Prototype.class, "G$valueOf", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeBoolean$Prototype.class, "S$valueOf", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

