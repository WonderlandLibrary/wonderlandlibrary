/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import jdk.nashorn.internal.objects.NativeArrayBuffer;
import jdk.nashorn.internal.objects.NativeArrayBuffer$Prototype;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.PrototypeObject;
import jdk.nashorn.internal.runtime.ScriptFunction;

final class NativeArrayBuffer$Constructor
extends ScriptFunction {
    private Object isView = ScriptFunction.createBuiltin("isView", /* method handle: isView(java.lang.Object java.lang.Object ) */ null);
    private static final PropertyMap $nasgenmap$;

    public Object G$isView() {
        return this.isView;
    }

    public void S$isView(Object object) {
        this.isView = object;
    }

    static {
        ArrayList<Property> arrayList = new ArrayList<Property>(1);
        arrayList.add(AccessorProperty.create("isView", 2, cfr_ldc_0(), cfr_ldc_1()));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    NativeArrayBuffer$Constructor() {
        super("ArrayBuffer", cfr_ldc_2(), $nasgenmap$, null);
        NativeArrayBuffer$Prototype nativeArrayBuffer$Prototype = new NativeArrayBuffer$Prototype();
        PrototypeObject.setConstructor(nativeArrayBuffer$Prototype, this);
        this.setPrototype(nativeArrayBuffer$Prototype);
        this.setArity(1);
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(NativeArrayBuffer$Constructor.class, "G$isView", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeArrayBuffer$Constructor.class, "S$isView", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findStatic(NativeArrayBuffer.class, "constructor", MethodType.fromMethodDescriptorString("(ZLjava/lang/Object;[Ljava/lang/Object;)Ljdk/nashorn/internal/objects/NativeArrayBuffer;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

