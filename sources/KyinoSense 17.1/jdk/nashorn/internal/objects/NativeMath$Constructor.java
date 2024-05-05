/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import jdk.nashorn.internal.objects.NativeMath;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.Specialization;

final class NativeMath$Constructor
extends ScriptObject {
    private Object abs = ScriptFunction.createBuiltin("abs", /* method handle: abs(java.lang.Object java.lang.Object ) */ null, new Specialization[]{new Specialization(/* method handle: abs(java.lang.Object int ) */ null, false), new Specialization(/* method handle: abs(java.lang.Object long ) */ null, false), new Specialization(/* method handle: abs(java.lang.Object double ) */ null, false)});
    private Object acos = ScriptFunction.createBuiltin("acos", /* method handle: acos(java.lang.Object java.lang.Object ) */ null, new Specialization[]{new Specialization(/* method handle: acos(java.lang.Object double ) */ null, false)});
    private Object asin = ScriptFunction.createBuiltin("asin", /* method handle: asin(java.lang.Object java.lang.Object ) */ null, new Specialization[]{new Specialization(/* method handle: asin(java.lang.Object double ) */ null, false)});
    private Object atan = ScriptFunction.createBuiltin("atan", /* method handle: atan(java.lang.Object java.lang.Object ) */ null, new Specialization[]{new Specialization(/* method handle: atan(java.lang.Object double ) */ null, false)});
    private Object atan2 = ScriptFunction.createBuiltin("atan2", /* method handle: atan2(java.lang.Object java.lang.Object java.lang.Object ) */ null, new Specialization[]{new Specialization(/* method handle: atan2(java.lang.Object double double ) */ null, false)});
    private Object ceil = ScriptFunction.createBuiltin("ceil", /* method handle: ceil(java.lang.Object java.lang.Object ) */ null, new Specialization[]{new Specialization(/* method handle: ceil(java.lang.Object int ) */ null, false), new Specialization(/* method handle: ceil(java.lang.Object long ) */ null, false), new Specialization(/* method handle: ceil(java.lang.Object double ) */ null, false)});
    private Object cos = ScriptFunction.createBuiltin("cos", /* method handle: cos(java.lang.Object java.lang.Object ) */ null, new Specialization[]{new Specialization(/* method handle: cos(java.lang.Object double ) */ null, false)});
    private Object exp = ScriptFunction.createBuiltin("exp", /* method handle: exp(java.lang.Object java.lang.Object ) */ null);
    private Object floor = ScriptFunction.createBuiltin("floor", /* method handle: floor(java.lang.Object java.lang.Object ) */ null, new Specialization[]{new Specialization(/* method handle: floor(java.lang.Object int ) */ null, false), new Specialization(/* method handle: floor(java.lang.Object long ) */ null, false), new Specialization(/* method handle: floor(java.lang.Object double ) */ null, false)});
    private Object log = ScriptFunction.createBuiltin("log", /* method handle: log(java.lang.Object java.lang.Object ) */ null, new Specialization[]{new Specialization(/* method handle: log(java.lang.Object double ) */ null, false)});
    private Object max;
    private Object min;
    private Object pow;
    private Object random;
    private Object round;
    private Object sin;
    private Object sqrt;
    private Object tan;
    private static final PropertyMap $nasgenmap$;

    public double G$E() {
        return NativeMath.E;
    }

    public double G$LN10() {
        return NativeMath.LN10;
    }

    public double G$LN2() {
        return NativeMath.LN2;
    }

    public double G$LOG2E() {
        return NativeMath.LOG2E;
    }

    public double G$LOG10E() {
        return NativeMath.LOG10E;
    }

    public double G$PI() {
        return NativeMath.PI;
    }

    public double G$SQRT1_2() {
        return NativeMath.SQRT1_2;
    }

    public double G$SQRT2() {
        return NativeMath.SQRT2;
    }

    public Object G$abs() {
        return this.abs;
    }

    public void S$abs(Object object) {
        this.abs = object;
    }

    public Object G$acos() {
        return this.acos;
    }

    public void S$acos(Object object) {
        this.acos = object;
    }

    public Object G$asin() {
        return this.asin;
    }

    public void S$asin(Object object) {
        this.asin = object;
    }

    public Object G$atan() {
        return this.atan;
    }

    public void S$atan(Object object) {
        this.atan = object;
    }

    public Object G$atan2() {
        return this.atan2;
    }

    public void S$atan2(Object object) {
        this.atan2 = object;
    }

    public Object G$ceil() {
        return this.ceil;
    }

    public void S$ceil(Object object) {
        this.ceil = object;
    }

    public Object G$cos() {
        return this.cos;
    }

    public void S$cos(Object object) {
        this.cos = object;
    }

    public Object G$exp() {
        return this.exp;
    }

    public void S$exp(Object object) {
        this.exp = object;
    }

    public Object G$floor() {
        return this.floor;
    }

    public void S$floor(Object object) {
        this.floor = object;
    }

    public Object G$log() {
        return this.log;
    }

    public void S$log(Object object) {
        this.log = object;
    }

    public Object G$max() {
        return this.max;
    }

    public void S$max(Object object) {
        this.max = object;
    }

    public Object G$min() {
        return this.min;
    }

    public void S$min(Object object) {
        this.min = object;
    }

    public Object G$pow() {
        return this.pow;
    }

    public void S$pow(Object object) {
        this.pow = object;
    }

    public Object G$random() {
        return this.random;
    }

    public void S$random(Object object) {
        this.random = object;
    }

    public Object G$round() {
        return this.round;
    }

    public void S$round(Object object) {
        this.round = object;
    }

    public Object G$sin() {
        return this.sin;
    }

    public void S$sin(Object object) {
        this.sin = object;
    }

    public Object G$sqrt() {
        return this.sqrt;
    }

    public void S$sqrt(Object object) {
        this.sqrt = object;
    }

    public Object G$tan() {
        return this.tan;
    }

    public void S$tan(Object object) {
        this.tan = object;
    }

    static {
        ArrayList<Property> arrayList = new ArrayList<Property>(26);
        arrayList.add(AccessorProperty.create("E", 7, cfr_ldc_0(), null));
        arrayList.add(AccessorProperty.create("LN10", 7, cfr_ldc_1(), null));
        arrayList.add(AccessorProperty.create("LN2", 7, cfr_ldc_2(), null));
        arrayList.add(AccessorProperty.create("LOG2E", 7, cfr_ldc_3(), null));
        arrayList.add(AccessorProperty.create("LOG10E", 7, cfr_ldc_4(), null));
        arrayList.add(AccessorProperty.create("PI", 7, cfr_ldc_5(), null));
        arrayList.add(AccessorProperty.create("SQRT1_2", 7, cfr_ldc_6(), null));
        arrayList.add(AccessorProperty.create("SQRT2", 7, cfr_ldc_7(), null));
        arrayList.add(AccessorProperty.create("abs", 2, cfr_ldc_8(), cfr_ldc_9()));
        arrayList.add(AccessorProperty.create("acos", 2, cfr_ldc_10(), cfr_ldc_11()));
        arrayList.add(AccessorProperty.create("asin", 2, cfr_ldc_12(), cfr_ldc_13()));
        arrayList.add(AccessorProperty.create("atan", 2, cfr_ldc_14(), cfr_ldc_15()));
        arrayList.add(AccessorProperty.create("atan2", 2, cfr_ldc_16(), cfr_ldc_17()));
        arrayList.add(AccessorProperty.create("ceil", 2, cfr_ldc_18(), cfr_ldc_19()));
        arrayList.add(AccessorProperty.create("cos", 2, cfr_ldc_20(), cfr_ldc_21()));
        arrayList.add(AccessorProperty.create("exp", 2, cfr_ldc_22(), cfr_ldc_23()));
        arrayList.add(AccessorProperty.create("floor", 2, cfr_ldc_24(), cfr_ldc_25()));
        arrayList.add(AccessorProperty.create("log", 2, cfr_ldc_26(), cfr_ldc_27()));
        arrayList.add(AccessorProperty.create("max", 2, cfr_ldc_28(), cfr_ldc_29()));
        arrayList.add(AccessorProperty.create("min", 2, cfr_ldc_30(), cfr_ldc_31()));
        arrayList.add(AccessorProperty.create("pow", 2, cfr_ldc_32(), cfr_ldc_33()));
        arrayList.add(AccessorProperty.create("random", 2, cfr_ldc_34(), cfr_ldc_35()));
        arrayList.add(AccessorProperty.create("round", 2, cfr_ldc_36(), cfr_ldc_37()));
        arrayList.add(AccessorProperty.create("sin", 2, cfr_ldc_38(), cfr_ldc_39()));
        arrayList.add(AccessorProperty.create("sqrt", 2, cfr_ldc_40(), cfr_ldc_41()));
        arrayList.add(AccessorProperty.create("tan", 2, cfr_ldc_42(), cfr_ldc_43()));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    NativeMath$Constructor() {
        super($nasgenmap$);
        ScriptFunction scriptFunction = ScriptFunction.createBuiltin("max", cfr_ldc_44(), new Specialization[]{new Specialization(cfr_ldc_45(), false), new Specialization(cfr_ldc_46(), false), new Specialization(cfr_ldc_47(), false), new Specialization(cfr_ldc_48(), false), new Specialization(cfr_ldc_49(), false)});
        scriptFunction.setArity(2);
        this.max = scriptFunction;
        ScriptFunction scriptFunction2 = ScriptFunction.createBuiltin("min", cfr_ldc_50(), new Specialization[]{new Specialization(cfr_ldc_51(), false), new Specialization(cfr_ldc_52(), false), new Specialization(cfr_ldc_53(), false), new Specialization(cfr_ldc_54(), false), new Specialization(cfr_ldc_55(), false)});
        scriptFunction2.setArity(2);
        this.min = scriptFunction2;
        this.pow = ScriptFunction.createBuiltin("pow", cfr_ldc_56(), new Specialization[]{new Specialization(cfr_ldc_57(), false)});
        this.random = ScriptFunction.createBuiltin("random", cfr_ldc_58());
        this.round = ScriptFunction.createBuiltin("round", cfr_ldc_59());
        this.sin = ScriptFunction.createBuiltin("sin", cfr_ldc_60(), new Specialization[]{new Specialization(cfr_ldc_61(), false)});
        this.sqrt = ScriptFunction.createBuiltin("sqrt", cfr_ldc_62(), new Specialization[]{new Specialization(cfr_ldc_63(), false)});
        this.tan = ScriptFunction.createBuiltin("tan", cfr_ldc_64(), new Specialization[]{new Specialization(cfr_ldc_65(), false)});
    }

    @Override
    public String getClassName() {
        return "Math";
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$E", MethodType.fromMethodDescriptorString("()D", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$LN10", MethodType.fromMethodDescriptorString("()D", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$LN2", MethodType.fromMethodDescriptorString("()D", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$LOG2E", MethodType.fromMethodDescriptorString("()D", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$LOG10E", MethodType.fromMethodDescriptorString("()D", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$PI", MethodType.fromMethodDescriptorString("()D", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$SQRT1_2", MethodType.fromMethodDescriptorString("()D", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$SQRT2", MethodType.fromMethodDescriptorString("()D", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$abs", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "S$abs", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$acos", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "S$acos", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$asin", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "S$asin", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$atan", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "S$atan", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$atan2", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "S$atan2", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$ceil", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "S$ceil", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$cos", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "S$cos", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$exp", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "S$exp", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$floor", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "S$floor", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$log", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "S$log", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$max", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "S$max", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$min", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "S$min", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$pow", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "S$pow", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$random", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "S$random", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$round", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "S$round", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$sin", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "S$sin", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$sqrt", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "S$sqrt", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "G$tan", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeMath$Constructor.class, "S$tan", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "max", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;[Ljava/lang/Object;)D", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "max", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)D", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "max", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;II)I", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "max", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;JJ)J", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "max", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;DD)D", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "max", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)D", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "min", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;[Ljava/lang/Object;)D", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "min", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)D", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "min", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;II)I", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "min", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;JJ)J", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "min", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;DD)D", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "min", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)D", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "pow", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)D", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "pow", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;DD)D", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "random", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)D", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "round", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)D", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "sin", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)D", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "sin", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;D)D", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "sqrt", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)D", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "sqrt", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;D)D", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "tan", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)D", null));
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
            return MethodHandles.lookup().findStatic(NativeMath.class, "tan", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;D)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

