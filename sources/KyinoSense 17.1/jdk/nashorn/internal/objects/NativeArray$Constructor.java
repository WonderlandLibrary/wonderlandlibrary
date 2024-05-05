/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.objects.NativeArray$Prototype;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.PrototypeObject;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.Specialization;

final class NativeArray$Constructor
extends ScriptFunction {
    private Object isArray = ScriptFunction.createBuiltin("isArray", /* method handle: isArray(java.lang.Object java.lang.Object ) */ null);
    private static final PropertyMap $nasgenmap$;

    public Object G$isArray() {
        return this.isArray;
    }

    public void S$isArray(Object object) {
        this.isArray = object;
    }

    static {
        ArrayList<Property> arrayList = new ArrayList<Property>(1);
        arrayList.add(AccessorProperty.create("isArray", 2, cfr_ldc_0(), cfr_ldc_1()));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    NativeArray$Constructor() {
        super("Array", cfr_ldc_2(), $nasgenmap$, new Specialization[]{new Specialization(cfr_ldc_3(), false), new Specialization(cfr_ldc_4(), false), new Specialization(cfr_ldc_5(), false), new Specialization(cfr_ldc_6(), false), new Specialization(cfr_ldc_7(), false)});
        NativeArray$Prototype nativeArray$Prototype = new NativeArray$Prototype();
        PrototypeObject.setConstructor(nativeArray$Prototype, this);
        this.setPrototype(nativeArray$Prototype);
        this.setArity(1);
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(NativeArray$Constructor.class, "G$isArray", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeArray$Constructor.class, "S$isArray", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findStatic(NativeArray.class, "construct", MethodType.fromMethodDescriptorString("(ZLjava/lang/Object;[Ljava/lang/Object;)Ljdk/nashorn/internal/objects/NativeArray;", null));
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
            return MethodHandles.lookup().findStatic(NativeArray.class, "construct", MethodType.fromMethodDescriptorString("(ZLjava/lang/Object;)Ljdk/nashorn/internal/objects/NativeArray;", null));
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
            return MethodHandles.lookup().findStatic(NativeArray.class, "construct", MethodType.fromMethodDescriptorString("(ZLjava/lang/Object;Z)Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findStatic(NativeArray.class, "construct", MethodType.fromMethodDescriptorString("(ZLjava/lang/Object;I)Ljdk/nashorn/internal/objects/NativeArray;", null));
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
            return MethodHandles.lookup().findStatic(NativeArray.class, "construct", MethodType.fromMethodDescriptorString("(ZLjava/lang/Object;J)Ljdk/nashorn/internal/objects/NativeArray;", null));
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
            return MethodHandles.lookup().findStatic(NativeArray.class, "construct", MethodType.fromMethodDescriptorString("(ZLjava/lang/Object;D)Ljdk/nashorn/internal/objects/NativeArray;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

