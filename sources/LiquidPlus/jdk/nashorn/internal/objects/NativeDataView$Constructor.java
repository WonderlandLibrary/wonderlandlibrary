/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import jdk.nashorn.internal.objects.NativeDataView;
import jdk.nashorn.internal.objects.NativeDataView$Prototype;
import jdk.nashorn.internal.runtime.PrototypeObject;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.Specialization;

final class NativeDataView$Constructor
extends ScriptFunction {
    NativeDataView$Constructor() {
        super("DataView", cfr_ldc_0(), new Specialization[]{new Specialization(cfr_ldc_1(), false), new Specialization(cfr_ldc_2(), false)});
        NativeDataView$Prototype nativeDataView$Prototype = new NativeDataView$Prototype();
        PrototypeObject.setConstructor(nativeDataView$Prototype, this);
        this.setPrototype(nativeDataView$Prototype);
        this.setArity(1);
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "constructor", MethodType.fromMethodDescriptorString("(ZLjava/lang/Object;[Ljava/lang/Object;)Ljdk/nashorn/internal/objects/NativeDataView;", null));
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
            return MethodHandles.lookup().findStatic(NativeDataView.class, "constructor", MethodType.fromMethodDescriptorString("(ZLjava/lang/Object;Ljava/lang/Object;I)Ljdk/nashorn/internal/objects/NativeDataView;", null));
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
            return MethodHandles.lookup().findStatic(NativeDataView.class, "constructor", MethodType.fromMethodDescriptorString("(ZLjava/lang/Object;Ljava/lang/Object;II)Ljdk/nashorn/internal/objects/NativeDataView;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

