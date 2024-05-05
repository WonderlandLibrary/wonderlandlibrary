/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.PrototypeObject;
import jdk.nashorn.internal.runtime.ScriptFunction;

final class NativeJavaImporter$Prototype
extends PrototypeObject {
    private Object __noSuchProperty__ = ScriptFunction.createBuiltin("__noSuchProperty__", /* method handle: __noSuchProperty__(java.lang.Object java.lang.Object ) */ null);
    private Object __noSuchMethod__ = ScriptFunction.createBuiltin("__noSuchMethod__", /* method handle: __noSuchMethod__(java.lang.Object java.lang.Object[] ) */ null);
    private static final PropertyMap $nasgenmap$;

    public Object G$__noSuchProperty__() {
        return this.__noSuchProperty__;
    }

    public void S$__noSuchProperty__(Object object) {
        this.__noSuchProperty__ = object;
    }

    public Object G$__noSuchMethod__() {
        return this.__noSuchMethod__;
    }

    public void S$__noSuchMethod__(Object object) {
        this.__noSuchMethod__ = object;
    }

    static {
        ArrayList<Property> arrayList = new ArrayList<Property>(2);
        arrayList.add(AccessorProperty.create("__noSuchProperty__", 2, cfr_ldc_0(), cfr_ldc_1()));
        arrayList.add(AccessorProperty.create("__noSuchMethod__", 2, cfr_ldc_2(), cfr_ldc_3()));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    NativeJavaImporter$Prototype() {
        super($nasgenmap$);
    }

    @Override
    public String getClassName() {
        return "JavaImporter";
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJavaImporter$Prototype.class, "G$__noSuchProperty__", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeJavaImporter$Prototype.class, "S$__noSuchProperty__", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeJavaImporter$Prototype.class, "G$__noSuchMethod__", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeJavaImporter$Prototype.class, "S$__noSuchMethod__", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

