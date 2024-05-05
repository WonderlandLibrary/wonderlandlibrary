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
import jdk.nashorn.internal.runtime.Specialization;

final class NativeArrayBuffer$Prototype
extends PrototypeObject {
    private Object slice = ScriptFunction.createBuiltin("slice", /* method handle: slice(java.lang.Object java.lang.Object java.lang.Object ) */ null, new Specialization[]{new Specialization(/* method handle: slice(java.lang.Object int int ) */ null, false), new Specialization(/* method handle: slice(java.lang.Object int ) */ null, false)});
    private static final PropertyMap $nasgenmap$;

    public Object G$slice() {
        return this.slice;
    }

    public void S$slice(Object object) {
        this.slice = object;
    }

    static {
        ArrayList<Property> arrayList = new ArrayList<Property>(1);
        arrayList.add(AccessorProperty.create("slice", 2, cfr_ldc_0(), cfr_ldc_1()));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    NativeArrayBuffer$Prototype() {
        super($nasgenmap$);
    }

    @Override
    public String getClassName() {
        return "ArrayBuffer";
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(NativeArrayBuffer$Prototype.class, "G$slice", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeArrayBuffer$Prototype.class, "S$slice", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

