/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import jdk.nashorn.internal.objects.NativeFunction;
import jdk.nashorn.internal.objects.NativeFunction$Prototype;
import jdk.nashorn.internal.runtime.PrototypeObject;
import jdk.nashorn.internal.runtime.ScriptFunction;

final class NativeFunction$Constructor
extends ScriptFunction {
    NativeFunction$Constructor() {
        super("Function", cfr_ldc_0(), null);
        NativeFunction$Prototype nativeFunction$Prototype = new NativeFunction$Prototype();
        PrototypeObject.setConstructor(nativeFunction$Prototype, this);
        this.setPrototype(nativeFunction$Prototype);
        this.setArity(1);
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findStatic(NativeFunction.class, "function", MethodType.fromMethodDescriptorString("(ZLjava/lang/Object;[Ljava/lang/Object;)Ljdk/nashorn/internal/runtime/ScriptFunction;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

