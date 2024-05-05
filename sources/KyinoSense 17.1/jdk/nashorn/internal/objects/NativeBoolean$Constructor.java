/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import jdk.nashorn.internal.objects.NativeBoolean;
import jdk.nashorn.internal.objects.NativeBoolean$Prototype;
import jdk.nashorn.internal.runtime.PrototypeObject;
import jdk.nashorn.internal.runtime.ScriptFunction;

final class NativeBoolean$Constructor
extends ScriptFunction {
    NativeBoolean$Constructor() {
        super("Boolean", cfr_ldc_0(), null);
        NativeBoolean$Prototype nativeBoolean$Prototype = new NativeBoolean$Prototype();
        PrototypeObject.setConstructor(nativeBoolean$Prototype, this);
        this.setPrototype(nativeBoolean$Prototype);
        this.setArity(1);
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findStatic(NativeBoolean.class, "constructor", MethodType.fromMethodDescriptorString("(ZLjava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

