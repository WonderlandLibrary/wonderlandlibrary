/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import jdk.nashorn.internal.objects.NativeDataView;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.PrototypeObject;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.Specialization;

final class NativeDataView$Prototype
extends PrototypeObject {
    private Object getInt8 = ScriptFunction.createBuiltin("getInt8", /* method handle: getInt8(java.lang.Object java.lang.Object ) */ null, new Specialization[]{new Specialization(/* method handle: getInt8(java.lang.Object int ) */ null, false)});
    private Object getUint8 = ScriptFunction.createBuiltin("getUint8", /* method handle: getUint8(java.lang.Object java.lang.Object ) */ null, new Specialization[]{new Specialization(/* method handle: getUint8(java.lang.Object int ) */ null, false)});
    private Object getInt16;
    private Object getUint16;
    private Object getInt32;
    private Object getUint32;
    private Object getFloat32;
    private Object getFloat64;
    private Object setInt8;
    private Object setUint8;
    private Object setInt16;
    private Object setUint16;
    private Object setInt32;
    private Object setUint32;
    private Object setFloat32;
    private Object setFloat64;
    private static final PropertyMap $nasgenmap$;

    public Object G$getInt8() {
        return this.getInt8;
    }

    public void S$getInt8(Object object) {
        this.getInt8 = object;
    }

    public Object G$getUint8() {
        return this.getUint8;
    }

    public void S$getUint8(Object object) {
        this.getUint8 = object;
    }

    public Object G$getInt16() {
        return this.getInt16;
    }

    public void S$getInt16(Object object) {
        this.getInt16 = object;
    }

    public Object G$getUint16() {
        return this.getUint16;
    }

    public void S$getUint16(Object object) {
        this.getUint16 = object;
    }

    public Object G$getInt32() {
        return this.getInt32;
    }

    public void S$getInt32(Object object) {
        this.getInt32 = object;
    }

    public Object G$getUint32() {
        return this.getUint32;
    }

    public void S$getUint32(Object object) {
        this.getUint32 = object;
    }

    public Object G$getFloat32() {
        return this.getFloat32;
    }

    public void S$getFloat32(Object object) {
        this.getFloat32 = object;
    }

    public Object G$getFloat64() {
        return this.getFloat64;
    }

    public void S$getFloat64(Object object) {
        this.getFloat64 = object;
    }

    public Object G$setInt8() {
        return this.setInt8;
    }

    public void S$setInt8(Object object) {
        this.setInt8 = object;
    }

    public Object G$setUint8() {
        return this.setUint8;
    }

    public void S$setUint8(Object object) {
        this.setUint8 = object;
    }

    public Object G$setInt16() {
        return this.setInt16;
    }

    public void S$setInt16(Object object) {
        this.setInt16 = object;
    }

    public Object G$setUint16() {
        return this.setUint16;
    }

    public void S$setUint16(Object object) {
        this.setUint16 = object;
    }

    public Object G$setInt32() {
        return this.setInt32;
    }

    public void S$setInt32(Object object) {
        this.setInt32 = object;
    }

    public Object G$setUint32() {
        return this.setUint32;
    }

    public void S$setUint32(Object object) {
        this.setUint32 = object;
    }

    public Object G$setFloat32() {
        return this.setFloat32;
    }

    public void S$setFloat32(Object object) {
        this.setFloat32 = object;
    }

    public Object G$setFloat64() {
        return this.setFloat64;
    }

    public void S$setFloat64(Object object) {
        this.setFloat64 = object;
    }

    static {
        ArrayList<Property> arrayList = new ArrayList<Property>(16);
        arrayList.add(AccessorProperty.create("getInt8", 2, cfr_ldc_0(), cfr_ldc_1()));
        arrayList.add(AccessorProperty.create("getUint8", 2, cfr_ldc_2(), cfr_ldc_3()));
        arrayList.add(AccessorProperty.create("getInt16", 2, cfr_ldc_4(), cfr_ldc_5()));
        arrayList.add(AccessorProperty.create("getUint16", 2, cfr_ldc_6(), cfr_ldc_7()));
        arrayList.add(AccessorProperty.create("getInt32", 2, cfr_ldc_8(), cfr_ldc_9()));
        arrayList.add(AccessorProperty.create("getUint32", 2, cfr_ldc_10(), cfr_ldc_11()));
        arrayList.add(AccessorProperty.create("getFloat32", 2, cfr_ldc_12(), cfr_ldc_13()));
        arrayList.add(AccessorProperty.create("getFloat64", 2, cfr_ldc_14(), cfr_ldc_15()));
        arrayList.add(AccessorProperty.create("setInt8", 2, cfr_ldc_16(), cfr_ldc_17()));
        arrayList.add(AccessorProperty.create("setUint8", 2, cfr_ldc_18(), cfr_ldc_19()));
        arrayList.add(AccessorProperty.create("setInt16", 2, cfr_ldc_20(), cfr_ldc_21()));
        arrayList.add(AccessorProperty.create("setUint16", 2, cfr_ldc_22(), cfr_ldc_23()));
        arrayList.add(AccessorProperty.create("setInt32", 2, cfr_ldc_24(), cfr_ldc_25()));
        arrayList.add(AccessorProperty.create("setUint32", 2, cfr_ldc_26(), cfr_ldc_27()));
        arrayList.add(AccessorProperty.create("setFloat32", 2, cfr_ldc_28(), cfr_ldc_29()));
        arrayList.add(AccessorProperty.create("setFloat64", 2, cfr_ldc_30(), cfr_ldc_31()));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    NativeDataView$Prototype() {
        super($nasgenmap$);
        ScriptFunction scriptFunction = ScriptFunction.createBuiltin("getInt16", cfr_ldc_32(), new Specialization[]{new Specialization(cfr_ldc_33(), false), new Specialization(cfr_ldc_34(), false)});
        scriptFunction.setArity(1);
        this.getInt16 = scriptFunction;
        ScriptFunction scriptFunction2 = ScriptFunction.createBuiltin("getUint16", cfr_ldc_35(), new Specialization[]{new Specialization(cfr_ldc_36(), false), new Specialization(cfr_ldc_37(), false)});
        scriptFunction2.setArity(1);
        this.getUint16 = scriptFunction2;
        ScriptFunction scriptFunction3 = ScriptFunction.createBuiltin("getInt32", cfr_ldc_38(), new Specialization[]{new Specialization(cfr_ldc_39(), false), new Specialization(cfr_ldc_40(), false)});
        scriptFunction3.setArity(1);
        this.getInt32 = scriptFunction3;
        ScriptFunction scriptFunction4 = ScriptFunction.createBuiltin("getUint32", cfr_ldc_41(), new Specialization[]{new Specialization(cfr_ldc_42(), false), new Specialization(cfr_ldc_43(), false)});
        scriptFunction4.setArity(1);
        this.getUint32 = scriptFunction4;
        ScriptFunction scriptFunction5 = ScriptFunction.createBuiltin("getFloat32", cfr_ldc_44(), new Specialization[]{new Specialization(cfr_ldc_45(), false), new Specialization(cfr_ldc_46(), false)});
        scriptFunction5.setArity(1);
        this.getFloat32 = scriptFunction5;
        ScriptFunction scriptFunction6 = ScriptFunction.createBuiltin("getFloat64", cfr_ldc_47(), new Specialization[]{new Specialization(cfr_ldc_48(), false), new Specialization(cfr_ldc_49(), false)});
        scriptFunction6.setArity(1);
        this.getFloat64 = scriptFunction6;
        ScriptFunction scriptFunction7 = ScriptFunction.createBuiltin("setInt8", cfr_ldc_50(), new Specialization[]{new Specialization(cfr_ldc_51(), false)});
        scriptFunction7.setArity(2);
        this.setInt8 = scriptFunction7;
        ScriptFunction scriptFunction8 = ScriptFunction.createBuiltin("setUint8", cfr_ldc_52(), new Specialization[]{new Specialization(cfr_ldc_53(), false)});
        scriptFunction8.setArity(2);
        this.setUint8 = scriptFunction8;
        ScriptFunction scriptFunction9 = ScriptFunction.createBuiltin("setInt16", cfr_ldc_54(), new Specialization[]{new Specialization(cfr_ldc_55(), false), new Specialization(cfr_ldc_56(), false)});
        scriptFunction9.setArity(2);
        this.setInt16 = scriptFunction9;
        ScriptFunction scriptFunction10 = ScriptFunction.createBuiltin("setUint16", cfr_ldc_57(), new Specialization[]{new Specialization(cfr_ldc_58(), false), new Specialization(cfr_ldc_59(), false)});
        scriptFunction10.setArity(2);
        this.setUint16 = scriptFunction10;
        ScriptFunction scriptFunction11 = ScriptFunction.createBuiltin("setInt32", cfr_ldc_60(), new Specialization[]{new Specialization(cfr_ldc_61(), false), new Specialization(cfr_ldc_62(), false)});
        scriptFunction11.setArity(2);
        this.setInt32 = scriptFunction11;
        ScriptFunction scriptFunction12 = ScriptFunction.createBuiltin("setUint32", cfr_ldc_63(), new Specialization[]{new Specialization(cfr_ldc_64(), false), new Specialization(cfr_ldc_65(), false)});
        scriptFunction12.setArity(2);
        this.setUint32 = scriptFunction12;
        ScriptFunction scriptFunction13 = ScriptFunction.createBuiltin("setFloat32", cfr_ldc_66(), new Specialization[]{new Specialization(cfr_ldc_67(), false), new Specialization(cfr_ldc_68(), false)});
        scriptFunction13.setArity(2);
        this.setFloat32 = scriptFunction13;
        ScriptFunction scriptFunction14 = ScriptFunction.createBuiltin("setFloat64", cfr_ldc_69(), new Specialization[]{new Specialization(cfr_ldc_70(), false), new Specialization(cfr_ldc_71(), false)});
        scriptFunction14.setArity(2);
        this.setFloat64 = scriptFunction14;
    }

    @Override
    public String getClassName() {
        return "DataView";
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "G$getInt8", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "S$getInt8", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "G$getUint8", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "S$getUint8", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "G$getInt16", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "S$getInt16", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "G$getUint16", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "S$getUint16", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "G$getInt32", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "S$getInt32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "G$getUint32", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "S$getUint32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "G$getFloat32", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "S$getFloat32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "G$getFloat64", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "S$getFloat64", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "G$setInt8", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "S$setInt8", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "G$setUint8", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "S$setUint8", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "G$setInt16", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "S$setInt16", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "G$setUint16", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "S$setUint16", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "G$setInt32", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "S$setInt32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "G$setUint32", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "S$setUint32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "G$setFloat32", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "S$setFloat32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "G$setFloat64", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDataView$Prototype.class, "S$setFloat64", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findStatic(NativeDataView.class, "getInt16", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)I", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_33() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "getInt16", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;I)I", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_34() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "getInt16", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;IZ)I", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_35() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "getUint16", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)I", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_36() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "getUint16", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;I)I", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_37() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "getUint16", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;IZ)I", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_38() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "getInt32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)I", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_39() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "getInt32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;I)I", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_40() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "getInt32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;IZ)I", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_41() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "getUint32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_42() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "getUint32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;I)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_43() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "getUint32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;IZ)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_44() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "getFloat32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_45() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "getFloat32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;I)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_46() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "getFloat32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;IZ)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_47() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "getFloat64", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_48() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "getFloat64", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;I)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_49() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "getFloat64", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;IZ)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_50() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setInt8", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_51() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setInt8", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;II)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_52() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setUint8", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_53() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setUint8", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;II)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_54() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setInt16", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_55() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setInt16", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;II)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_56() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setInt16", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;IIZ)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_57() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setUint16", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_58() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setUint16", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;II)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_59() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setUint16", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;IIZ)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_60() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setInt32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_61() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setInt32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;II)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_62() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setInt32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;IIZ)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_63() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setUint32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_64() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setUint32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;ID)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_65() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setUint32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;IDZ)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_66() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setFloat32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_67() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setFloat32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;ID)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_68() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setFloat32", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;IDZ)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_69() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setFloat64", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_70() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setFloat64", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;ID)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_71() {
        try {
            return MethodHandles.lookup().findStatic(NativeDataView.class, "setFloat64", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;IDZ)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

