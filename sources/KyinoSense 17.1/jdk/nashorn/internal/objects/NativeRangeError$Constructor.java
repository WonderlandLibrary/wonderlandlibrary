/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import jdk.nashorn.internal.objects.NativeRangeError;
import jdk.nashorn.internal.objects.NativeRangeError$Prototype;
import jdk.nashorn.internal.runtime.PrototypeObject;
import jdk.nashorn.internal.runtime.ScriptFunction;

final class NativeRangeError$Constructor
extends ScriptFunction {
    NativeRangeError$Constructor() {
        super("RangeError", cfr_ldc_0(), null);
        NativeRangeError$Prototype nativeRangeError$Prototype = new NativeRangeError$Prototype();
        PrototypeObject.setConstructor(nativeRangeError$Prototype, this);
        this.setPrototype(nativeRangeError$Prototype);
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findStatic(NativeRangeError.class, "constructor", MethodType.fromMethodDescriptorString("(ZLjava/lang/Object;Ljava/lang/Object;)Ljdk/nashorn/internal/objects/NativeRangeError;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

