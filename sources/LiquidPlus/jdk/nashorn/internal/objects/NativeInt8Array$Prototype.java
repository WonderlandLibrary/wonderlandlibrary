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

final class NativeInt8Array$Prototype
extends PrototypeObject {
    private Object set = ScriptFunction.createBuiltin("set", /* method handle: set(java.lang.Object java.lang.Object java.lang.Object ) */ null);
    private Object subarray = ScriptFunction.createBuiltin("subarray", /* method handle: subarray(java.lang.Object java.lang.Object java.lang.Object ) */ null);
    private static final PropertyMap $nasgenmap$;

    public Object G$set() {
        return this.set;
    }

    public void S$set(Object object) {
        this.set = object;
    }

    public Object G$subarray() {
        return this.subarray;
    }

    public void S$subarray(Object object) {
        this.subarray = object;
    }

    static {
        ArrayList<Property> arrayList = new ArrayList<Property>(2);
        arrayList.add(AccessorProperty.create("set", 2, cfr_ldc_0(), cfr_ldc_1()));
        arrayList.add(AccessorProperty.create("subarray", 2, cfr_ldc_2(), cfr_ldc_3()));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    NativeInt8Array$Prototype() {
        super($nasgenmap$);
    }

    @Override
    public String getClassName() {
        return "Int8Array";
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(NativeInt8Array$Prototype.class, "G$set", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeInt8Array$Prototype.class, "S$set", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeInt8Array$Prototype.class, "G$subarray", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeInt8Array$Prototype.class, "S$subarray", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

