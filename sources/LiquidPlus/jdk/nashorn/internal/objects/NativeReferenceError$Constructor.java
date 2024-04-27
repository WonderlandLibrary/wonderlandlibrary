/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import jdk.nashorn.internal.objects.NativeReferenceError;
import jdk.nashorn.internal.objects.NativeReferenceError$Prototype;
import jdk.nashorn.internal.runtime.PrototypeObject;
import jdk.nashorn.internal.runtime.ScriptFunction;

final class NativeReferenceError$Constructor
extends ScriptFunction {
    NativeReferenceError$Constructor() {
        super("ReferenceError", cfr_ldc_0(), null);
        NativeReferenceError$Prototype nativeReferenceError$Prototype = new NativeReferenceError$Prototype();
        PrototypeObject.setConstructor(nativeReferenceError$Prototype, this);
        this.setPrototype(nativeReferenceError$Prototype);
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findStatic(NativeReferenceError.class, "constructor", MethodType.fromMethodDescriptorString("(ZLjava/lang/Object;Ljava/lang/Object;)Ljdk/nashorn/internal/objects/NativeReferenceError;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

