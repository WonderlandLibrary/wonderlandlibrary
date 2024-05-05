/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import jdk.nashorn.internal.objects.NativeJavaImporter;
import jdk.nashorn.internal.objects.NativeJavaImporter$Prototype;
import jdk.nashorn.internal.runtime.PrototypeObject;
import jdk.nashorn.internal.runtime.ScriptFunction;

final class NativeJavaImporter$Constructor
extends ScriptFunction {
    NativeJavaImporter$Constructor() {
        super("JavaImporter", cfr_ldc_0(), null);
        NativeJavaImporter$Prototype nativeJavaImporter$Prototype = new NativeJavaImporter$Prototype();
        PrototypeObject.setConstructor(nativeJavaImporter$Prototype, this);
        this.setPrototype(nativeJavaImporter$Prototype);
        this.setArity(1);
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findStatic(NativeJavaImporter.class, "constructor", MethodType.fromMethodDescriptorString("(ZLjava/lang/Object;[Ljava/lang/Object;)Ljdk/nashorn/internal/objects/NativeJavaImporter;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

