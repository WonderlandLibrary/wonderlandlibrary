/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import jdk.nashorn.internal.objects.NativeObject;
import jdk.nashorn.internal.objects.NativeObject$Prototype;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.PrototypeObject;
import jdk.nashorn.internal.runtime.ScriptFunction;

final class NativeObject$Constructor
extends ScriptFunction {
    private Object setIndexedPropertiesToExternalArrayData = ScriptFunction.createBuiltin("setIndexedPropertiesToExternalArrayData", /* method handle: setIndexedPropertiesToExternalArrayData(java.lang.Object java.lang.Object java.lang.Object ) */ null);
    private Object getPrototypeOf = ScriptFunction.createBuiltin("getPrototypeOf", /* method handle: getPrototypeOf(java.lang.Object java.lang.Object ) */ null);
    private Object setPrototypeOf = ScriptFunction.createBuiltin("setPrototypeOf", /* method handle: setPrototypeOf(java.lang.Object java.lang.Object java.lang.Object ) */ null);
    private Object getOwnPropertyDescriptor = ScriptFunction.createBuiltin("getOwnPropertyDescriptor", /* method handle: getOwnPropertyDescriptor(java.lang.Object java.lang.Object java.lang.Object ) */ null);
    private Object getOwnPropertyNames = ScriptFunction.createBuiltin("getOwnPropertyNames", /* method handle: getOwnPropertyNames(java.lang.Object java.lang.Object ) */ null);
    private Object create = ScriptFunction.createBuiltin("create", /* method handle: create(java.lang.Object java.lang.Object java.lang.Object ) */ null);
    private Object defineProperty = ScriptFunction.createBuiltin("defineProperty", /* method handle: defineProperty(java.lang.Object java.lang.Object java.lang.Object java.lang.Object ) */ null);
    private Object defineProperties = ScriptFunction.createBuiltin("defineProperties", /* method handle: defineProperties(java.lang.Object java.lang.Object java.lang.Object ) */ null);
    private Object seal = ScriptFunction.createBuiltin("seal", /* method handle: seal(java.lang.Object java.lang.Object ) */ null);
    private Object freeze = ScriptFunction.createBuiltin("freeze", /* method handle: freeze(java.lang.Object java.lang.Object ) */ null);
    private Object preventExtensions = ScriptFunction.createBuiltin("preventExtensions", /* method handle: preventExtensions(java.lang.Object java.lang.Object ) */ null);
    private Object isSealed = ScriptFunction.createBuiltin("isSealed", /* method handle: isSealed(java.lang.Object java.lang.Object ) */ null);
    private Object isFrozen = ScriptFunction.createBuiltin("isFrozen", /* method handle: isFrozen(java.lang.Object java.lang.Object ) */ null);
    private Object isExtensible = ScriptFunction.createBuiltin("isExtensible", /* method handle: isExtensible(java.lang.Object java.lang.Object ) */ null);
    private Object keys = ScriptFunction.createBuiltin("keys", /* method handle: keys(java.lang.Object java.lang.Object ) */ null);
    private Object bindProperties = ScriptFunction.createBuiltin("bindProperties", /* method handle: bindProperties(java.lang.Object java.lang.Object java.lang.Object ) */ null);
    private static final PropertyMap $nasgenmap$;

    public Object G$setIndexedPropertiesToExternalArrayData() {
        return this.setIndexedPropertiesToExternalArrayData;
    }

    public void S$setIndexedPropertiesToExternalArrayData(Object object) {
        this.setIndexedPropertiesToExternalArrayData = object;
    }

    public Object G$getPrototypeOf() {
        return this.getPrototypeOf;
    }

    public void S$getPrototypeOf(Object object) {
        this.getPrototypeOf = object;
    }

    public Object G$setPrototypeOf() {
        return this.setPrototypeOf;
    }

    public void S$setPrototypeOf(Object object) {
        this.setPrototypeOf = object;
    }

    public Object G$getOwnPropertyDescriptor() {
        return this.getOwnPropertyDescriptor;
    }

    public void S$getOwnPropertyDescriptor(Object object) {
        this.getOwnPropertyDescriptor = object;
    }

    public Object G$getOwnPropertyNames() {
        return this.getOwnPropertyNames;
    }

    public void S$getOwnPropertyNames(Object object) {
        this.getOwnPropertyNames = object;
    }

    public Object G$create() {
        return this.create;
    }

    public void S$create(Object object) {
        this.create = object;
    }

    public Object G$defineProperty() {
        return this.defineProperty;
    }

    public void S$defineProperty(Object object) {
        this.defineProperty = object;
    }

    public Object G$defineProperties() {
        return this.defineProperties;
    }

    public void S$defineProperties(Object object) {
        this.defineProperties = object;
    }

    public Object G$seal() {
        return this.seal;
    }

    public void S$seal(Object object) {
        this.seal = object;
    }

    public Object G$freeze() {
        return this.freeze;
    }

    public void S$freeze(Object object) {
        this.freeze = object;
    }

    public Object G$preventExtensions() {
        return this.preventExtensions;
    }

    public void S$preventExtensions(Object object) {
        this.preventExtensions = object;
    }

    public Object G$isSealed() {
        return this.isSealed;
    }

    public void S$isSealed(Object object) {
        this.isSealed = object;
    }

    public Object G$isFrozen() {
        return this.isFrozen;
    }

    public void S$isFrozen(Object object) {
        this.isFrozen = object;
    }

    public Object G$isExtensible() {
        return this.isExtensible;
    }

    public void S$isExtensible(Object object) {
        this.isExtensible = object;
    }

    public Object G$keys() {
        return this.keys;
    }

    public void S$keys(Object object) {
        this.keys = object;
    }

    public Object G$bindProperties() {
        return this.bindProperties;
    }

    public void S$bindProperties(Object object) {
        this.bindProperties = object;
    }

    static {
        ArrayList<Property> arrayList = new ArrayList<Property>(16);
        arrayList.add(AccessorProperty.create("setIndexedPropertiesToExternalArrayData", 2, cfr_ldc_0(), cfr_ldc_1()));
        arrayList.add(AccessorProperty.create("getPrototypeOf", 2, cfr_ldc_2(), cfr_ldc_3()));
        arrayList.add(AccessorProperty.create("setPrototypeOf", 2, cfr_ldc_4(), cfr_ldc_5()));
        arrayList.add(AccessorProperty.create("getOwnPropertyDescriptor", 2, cfr_ldc_6(), cfr_ldc_7()));
        arrayList.add(AccessorProperty.create("getOwnPropertyNames", 2, cfr_ldc_8(), cfr_ldc_9()));
        arrayList.add(AccessorProperty.create("create", 2, cfr_ldc_10(), cfr_ldc_11()));
        arrayList.add(AccessorProperty.create("defineProperty", 2, cfr_ldc_12(), cfr_ldc_13()));
        arrayList.add(AccessorProperty.create("defineProperties", 2, cfr_ldc_14(), cfr_ldc_15()));
        arrayList.add(AccessorProperty.create("seal", 2, cfr_ldc_16(), cfr_ldc_17()));
        arrayList.add(AccessorProperty.create("freeze", 2, cfr_ldc_18(), cfr_ldc_19()));
        arrayList.add(AccessorProperty.create("preventExtensions", 2, cfr_ldc_20(), cfr_ldc_21()));
        arrayList.add(AccessorProperty.create("isSealed", 2, cfr_ldc_22(), cfr_ldc_23()));
        arrayList.add(AccessorProperty.create("isFrozen", 2, cfr_ldc_24(), cfr_ldc_25()));
        arrayList.add(AccessorProperty.create("isExtensible", 2, cfr_ldc_26(), cfr_ldc_27()));
        arrayList.add(AccessorProperty.create("keys", 2, cfr_ldc_28(), cfr_ldc_29()));
        arrayList.add(AccessorProperty.create("bindProperties", 2, cfr_ldc_30(), cfr_ldc_31()));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    NativeObject$Constructor() {
        super("Object", cfr_ldc_32(), $nasgenmap$, null);
        NativeObject$Prototype nativeObject$Prototype = new NativeObject$Prototype();
        PrototypeObject.setConstructor(nativeObject$Prototype, this);
        this.setPrototype(nativeObject$Prototype);
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "G$setIndexedPropertiesToExternalArrayData", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "S$setIndexedPropertiesToExternalArrayData", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "G$getPrototypeOf", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "S$getPrototypeOf", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "G$setPrototypeOf", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "S$setPrototypeOf", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "G$getOwnPropertyDescriptor", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "S$getOwnPropertyDescriptor", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "G$getOwnPropertyNames", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "S$getOwnPropertyNames", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "G$create", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "S$create", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "G$defineProperty", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "S$defineProperty", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "G$defineProperties", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "S$defineProperties", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "G$seal", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "S$seal", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "G$freeze", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "S$freeze", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "G$preventExtensions", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "S$preventExtensions", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "G$isSealed", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "S$isSealed", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "G$isFrozen", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "S$isFrozen", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "G$isExtensible", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "S$isExtensible", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_28() {
        try {
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "G$keys", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_29() {
        try {
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "S$keys", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_30() {
        try {
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "G$bindProperties", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_31() {
        try {
            return MethodHandles.lookup().findVirtual(NativeObject$Constructor.class, "S$bindProperties", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_32() {
        try {
            return MethodHandles.lookup().findStatic(NativeObject.class, "construct", MethodType.fromMethodDescriptorString("(ZLjava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

