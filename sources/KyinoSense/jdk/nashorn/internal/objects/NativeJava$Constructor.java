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
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;

final class NativeJava$Constructor
extends ScriptObject {
    private Object isType = ScriptFunction.createBuiltin("isType", /* method handle: isType(java.lang.Object java.lang.Object ) */ null);
    private Object synchronizedFunc = ScriptFunction.createBuiltin("synchronized", /* method handle: synchronizedFunc(java.lang.Object java.lang.Object java.lang.Object ) */ null);
    private Object isJavaMethod = ScriptFunction.createBuiltin("isJavaMethod", /* method handle: isJavaMethod(java.lang.Object java.lang.Object ) */ null);
    private Object isJavaFunction = ScriptFunction.createBuiltin("isJavaFunction", /* method handle: isJavaFunction(java.lang.Object java.lang.Object ) */ null);
    private Object isJavaObject = ScriptFunction.createBuiltin("isJavaObject", /* method handle: isJavaObject(java.lang.Object java.lang.Object ) */ null);
    private Object isScriptObject = ScriptFunction.createBuiltin("isScriptObject", /* method handle: isScriptObject(java.lang.Object java.lang.Object ) */ null);
    private Object isScriptFunction = ScriptFunction.createBuiltin("isScriptFunction", /* method handle: isScriptFunction(java.lang.Object java.lang.Object ) */ null);
    private Object type = ScriptFunction.createBuiltin("type", /* method handle: type(java.lang.Object java.lang.Object ) */ null);
    private Object typeName = ScriptFunction.createBuiltin("typeName", /* method handle: typeName(java.lang.Object java.lang.Object ) */ null);
    private Object to = ScriptFunction.createBuiltin("to", /* method handle: to(java.lang.Object java.lang.Object java.lang.Object ) */ null);
    private Object from = ScriptFunction.createBuiltin("from", /* method handle: from(java.lang.Object java.lang.Object ) */ null);
    private Object extend = ScriptFunction.createBuiltin("extend", /* method handle: extend(java.lang.Object java.lang.Object[] ) */ null);
    private Object _super = ScriptFunction.createBuiltin("super", /* method handle: _super(java.lang.Object java.lang.Object ) */ null);
    private Object asJSONCompatible = ScriptFunction.createBuiltin("asJSONCompatible", /* method handle: asJSONCompatible(java.lang.Object java.lang.Object ) */ null);
    private static final PropertyMap $nasgenmap$;

    public Object G$isType() {
        return this.isType;
    }

    public void S$isType(Object object) {
        this.isType = object;
    }

    public Object G$synchronizedFunc() {
        return this.synchronizedFunc;
    }

    public void S$synchronizedFunc(Object object) {
        this.synchronizedFunc = object;
    }

    public Object G$isJavaMethod() {
        return this.isJavaMethod;
    }

    public void S$isJavaMethod(Object object) {
        this.isJavaMethod = object;
    }

    public Object G$isJavaFunction() {
        return this.isJavaFunction;
    }

    public void S$isJavaFunction(Object object) {
        this.isJavaFunction = object;
    }

    public Object G$isJavaObject() {
        return this.isJavaObject;
    }

    public void S$isJavaObject(Object object) {
        this.isJavaObject = object;
    }

    public Object G$isScriptObject() {
        return this.isScriptObject;
    }

    public void S$isScriptObject(Object object) {
        this.isScriptObject = object;
    }

    public Object G$isScriptFunction() {
        return this.isScriptFunction;
    }

    public void S$isScriptFunction(Object object) {
        this.isScriptFunction = object;
    }

    public Object G$type() {
        return this.type;
    }

    public void S$type(Object object) {
        this.type = object;
    }

    public Object G$typeName() {
        return this.typeName;
    }

    public void S$typeName(Object object) {
        this.typeName = object;
    }

    public Object G$to() {
        return this.to;
    }

    public void S$to(Object object) {
        this.to = object;
    }

    public Object G$from() {
        return this.from;
    }

    public void S$from(Object object) {
        this.from = object;
    }

    public Object G$extend() {
        return this.extend;
    }

    public void S$extend(Object object) {
        this.extend = object;
    }

    public Object G$_super() {
        return this._super;
    }

    public void S$_super(Object object) {
        this._super = object;
    }

    public Object G$asJSONCompatible() {
        return this.asJSONCompatible;
    }

    public void S$asJSONCompatible(Object object) {
        this.asJSONCompatible = object;
    }

    static {
        ArrayList<Property> arrayList = new ArrayList<Property>(14);
        arrayList.add(AccessorProperty.create("isType", 2, cfr_ldc_0(), cfr_ldc_1()));
        arrayList.add(AccessorProperty.create("synchronized", 2, cfr_ldc_2(), cfr_ldc_3()));
        arrayList.add(AccessorProperty.create("isJavaMethod", 2, cfr_ldc_4(), cfr_ldc_5()));
        arrayList.add(AccessorProperty.create("isJavaFunction", 2, cfr_ldc_6(), cfr_ldc_7()));
        arrayList.add(AccessorProperty.create("isJavaObject", 2, cfr_ldc_8(), cfr_ldc_9()));
        arrayList.add(AccessorProperty.create("isScriptObject", 2, cfr_ldc_10(), cfr_ldc_11()));
        arrayList.add(AccessorProperty.create("isScriptFunction", 2, cfr_ldc_12(), cfr_ldc_13()));
        arrayList.add(AccessorProperty.create("type", 2, cfr_ldc_14(), cfr_ldc_15()));
        arrayList.add(AccessorProperty.create("typeName", 2, cfr_ldc_16(), cfr_ldc_17()));
        arrayList.add(AccessorProperty.create("to", 2, cfr_ldc_18(), cfr_ldc_19()));
        arrayList.add(AccessorProperty.create("from", 2, cfr_ldc_20(), cfr_ldc_21()));
        arrayList.add(AccessorProperty.create("extend", 2, cfr_ldc_22(), cfr_ldc_23()));
        arrayList.add(AccessorProperty.create("super", 2, cfr_ldc_24(), cfr_ldc_25()));
        arrayList.add(AccessorProperty.create("asJSONCompatible", 2, cfr_ldc_26(), cfr_ldc_27()));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    NativeJava$Constructor() {
        super($nasgenmap$);
    }

    @Override
    public String getClassName() {
        return "Java";
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "G$isType", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "S$isType", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "G$synchronizedFunc", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "S$synchronizedFunc", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "G$isJavaMethod", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "S$isJavaMethod", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_6() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "G$isJavaFunction", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_7() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "S$isJavaFunction", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_8() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "G$isJavaObject", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_9() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "S$isJavaObject", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_10() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "G$isScriptObject", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_11() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "S$isScriptObject", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_12() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "G$isScriptFunction", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_13() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "S$isScriptFunction", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_14() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "G$type", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_15() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "S$type", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_16() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "G$typeName", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_17() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "S$typeName", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_18() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "G$to", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_19() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "S$to", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_20() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "G$from", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_21() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "S$from", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_22() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "G$extend", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_23() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "S$extend", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_24() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "G$_super", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_25() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "S$_super", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_26() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "G$asJSONCompatible", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_27() {
        try {
            return MethodHandles.lookup().findVirtual(NativeJava$Constructor.class, "S$asJSONCompatible", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

