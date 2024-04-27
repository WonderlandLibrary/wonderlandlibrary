/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.regexp.RegExpResult;

public final class NativeRegExpExecResult
extends ScriptObject {
    public Object index;
    public Object input;
    private static PropertyMap $nasgenmap$;

    NativeRegExpExecResult(RegExpResult result, Global global) {
        super(global.getArrayPrototype(), $nasgenmap$);
        this.setIsArray();
        this.setArray(ArrayData.allocate((Object[])result.getGroups().clone()));
        this.index = result.getIndex();
        this.input = result.getInput();
    }

    @Override
    public String getClassName() {
        return "Array";
    }

    public static Object length(Object self) {
        if (self instanceof ScriptObject) {
            return (double)JSType.toUint32(((ScriptObject)self).getArray().length());
        }
        return 0;
    }

    public static void length(Object self, Object length) {
        if (self instanceof ScriptObject) {
            ((ScriptObject)self).setLength(NativeArray.validLength(length));
        }
    }

    static {
        NativeRegExpExecResult.$clinit$();
    }

    public static void $clinit$() {
        ArrayList<Property> arrayList = new ArrayList<Property>(3);
        arrayList.add(AccessorProperty.create("index", 0, cfr_ldc_0(), cfr_ldc_1()));
        arrayList.add(AccessorProperty.create("input", 0, cfr_ldc_2(), cfr_ldc_3()));
        arrayList.add(AccessorProperty.create("length", 6, cfr_ldc_4(), cfr_ldc_5()));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    public Object G$index() {
        return this.index;
    }

    public void S$index(Object object) {
        this.index = object;
    }

    public Object G$input() {
        return this.input;
    }

    public void S$input(Object object) {
        this.input = object;
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(NativeRegExpExecResult.class, "G$index", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeRegExpExecResult.class, "S$index", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeRegExpExecResult.class, "G$input", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeRegExpExecResult.class, "S$input", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_4() {
        try {
            return MethodHandles.lookup().findStatic(NativeRegExpExecResult.class, "length", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_5() {
        try {
            return MethodHandles.lookup().findStatic(NativeRegExpExecResult.class, "length", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

