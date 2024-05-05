/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import jdk.nashorn.internal.objects.NativeJSAdapter;
import jdk.nashorn.internal.objects.NativeJSAdapter$Prototype;
import jdk.nashorn.internal.runtime.PrototypeObject;
import jdk.nashorn.internal.runtime.ScriptFunction;

final class NativeJSAdapter$Constructor
extends ScriptFunction {
    NativeJSAdapter$Constructor() {
        super("JSAdapter", cfr_ldc_0(), null);
        NativeJSAdapter$Prototype nativeJSAdapter$Prototype = new NativeJSAdapter$Prototype();
        PrototypeObject.setConstructor(nativeJSAdapter$Prototype, this);
        this.setPrototype(nativeJSAdapter$Prototype);
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findStatic(NativeJSAdapter.class, "construct", MethodType.fromMethodDescriptorString("(ZLjava/lang/Object;[Ljava/lang/Object;)Ljdk/nashorn/internal/objects/NativeJSAdapter;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

