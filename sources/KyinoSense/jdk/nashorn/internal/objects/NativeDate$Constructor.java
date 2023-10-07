/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import jdk.nashorn.internal.objects.NativeDate;
import jdk.nashorn.internal.objects.NativeDate$Prototype;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.PrototypeObject;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.Specialization;

final class NativeDate$Constructor
extends ScriptFunction {
    private Object parse = ScriptFunction.createBuiltin("parse", /* method handle: parse(java.lang.Object java.lang.Object ) */ null);
    private Object UTC;
    private Object now;
    private static final PropertyMap $nasgenmap$;

    public Object G$parse() {
        return this.parse;
    }

    public void S$parse(Object object) {
        this.parse = object;
    }

    public Object G$UTC() {
        return this.UTC;
    }

    public void S$UTC(Object object) {
        this.UTC = object;
    }

    public Object G$now() {
        return this.now;
    }

    public void S$now(Object object) {
        this.now = object;
    }

    static {
        ArrayList<Property> arrayList = new ArrayList<Property>(3);
        arrayList.add(AccessorProperty.create("parse", 2, cfr_ldc_0(), cfr_ldc_1()));
        arrayList.add(AccessorProperty.create("UTC", 2, cfr_ldc_2(), cfr_ldc_3()));
        arrayList.add(AccessorProperty.create("now", 2, cfr_ldc_4(), cfr_ldc_5()));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    NativeDate$Constructor() {
        super("Date", cfr_ldc_6(), $nasgenmap$, new Specialization[]{new Specialization(cfr_ldc_7(), false)});
        ScriptFunction scriptFunction = ScriptFunction.createBuiltin("UTC", cfr_ldc_8());
        scriptFunction.setArity(7);
        this.UTC = scriptFunction;
        this.now = ScriptFunction.createBuiltin("now", cfr_ldc_9());
        NativeDate$Prototype nativeDate$Prototype = new NativeDate$Prototype();
        PrototypeObject.setConstructor(nativeDate$Prototype, this);
        this.setPrototype(nativeDate$Prototype);
        this.setArity(7);
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Constructor.class, "G$parse", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Constructor.class, "S$parse", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Constructor.class, "G$UTC", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Constructor.class, "S$UTC", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Constructor.class, "G$now", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Constructor.class, "S$now", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findStatic(NativeDate.class, "construct", MethodType.fromMethodDescriptorString("(ZLjava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findStatic(NativeDate.class, "construct", MethodType.fromMethodDescriptorString("(ZLjava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_8() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "UTC", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;[Ljava/lang/Object;)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_9() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "now", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

