/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import jdk.nashorn.internal.objects.NativeDate;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.PrototypeObject;
import jdk.nashorn.internal.runtime.ScriptFunction;

final class NativeDate$Prototype
extends PrototypeObject {
    private Object toString = ScriptFunction.createBuiltin("toString", /* method handle: toString(java.lang.Object ) */ null);
    private Object toDateString = ScriptFunction.createBuiltin("toDateString", /* method handle: toDateString(java.lang.Object ) */ null);
    private Object toTimeString = ScriptFunction.createBuiltin("toTimeString", /* method handle: toTimeString(java.lang.Object ) */ null);
    private Object toLocaleString = ScriptFunction.createBuiltin("toLocaleString", /* method handle: toLocaleString(java.lang.Object ) */ null);
    private Object toLocaleDateString = ScriptFunction.createBuiltin("toLocaleDateString", /* method handle: toLocaleDateString(java.lang.Object ) */ null);
    private Object toLocaleTimeString = ScriptFunction.createBuiltin("toLocaleTimeString", /* method handle: toLocaleTimeString(java.lang.Object ) */ null);
    private Object valueOf = ScriptFunction.createBuiltin("valueOf", /* method handle: valueOf(java.lang.Object ) */ null);
    private Object getTime = ScriptFunction.createBuiltin("getTime", /* method handle: getTime(java.lang.Object ) */ null);
    private Object getFullYear = ScriptFunction.createBuiltin("getFullYear", /* method handle: getFullYear(java.lang.Object ) */ null);
    private Object getUTCFullYear = ScriptFunction.createBuiltin("getUTCFullYear", /* method handle: getUTCFullYear(java.lang.Object ) */ null);
    private Object getYear = ScriptFunction.createBuiltin("getYear", /* method handle: getYear(java.lang.Object ) */ null);
    private Object getMonth = ScriptFunction.createBuiltin("getMonth", /* method handle: getMonth(java.lang.Object ) */ null);
    private Object getUTCMonth = ScriptFunction.createBuiltin("getUTCMonth", /* method handle: getUTCMonth(java.lang.Object ) */ null);
    private Object getDate = ScriptFunction.createBuiltin("getDate", /* method handle: getDate(java.lang.Object ) */ null);
    private Object getUTCDate = ScriptFunction.createBuiltin("getUTCDate", /* method handle: getUTCDate(java.lang.Object ) */ null);
    private Object getDay = ScriptFunction.createBuiltin("getDay", /* method handle: getDay(java.lang.Object ) */ null);
    private Object getUTCDay = ScriptFunction.createBuiltin("getUTCDay", /* method handle: getUTCDay(java.lang.Object ) */ null);
    private Object getHours = ScriptFunction.createBuiltin("getHours", /* method handle: getHours(java.lang.Object ) */ null);
    private Object getUTCHours = ScriptFunction.createBuiltin("getUTCHours", /* method handle: getUTCHours(java.lang.Object ) */ null);
    private Object getMinutes = ScriptFunction.createBuiltin("getMinutes", /* method handle: getMinutes(java.lang.Object ) */ null);
    private Object getUTCMinutes = ScriptFunction.createBuiltin("getUTCMinutes", /* method handle: getUTCMinutes(java.lang.Object ) */ null);
    private Object getSeconds = ScriptFunction.createBuiltin("getSeconds", /* method handle: getSeconds(java.lang.Object ) */ null);
    private Object getUTCSeconds = ScriptFunction.createBuiltin("getUTCSeconds", /* method handle: getUTCSeconds(java.lang.Object ) */ null);
    private Object getMilliseconds = ScriptFunction.createBuiltin("getMilliseconds", /* method handle: getMilliseconds(java.lang.Object ) */ null);
    private Object getUTCMilliseconds = ScriptFunction.createBuiltin("getUTCMilliseconds", /* method handle: getUTCMilliseconds(java.lang.Object ) */ null);
    private Object getTimezoneOffset = ScriptFunction.createBuiltin("getTimezoneOffset", /* method handle: getTimezoneOffset(java.lang.Object ) */ null);
    private Object setTime = ScriptFunction.createBuiltin("setTime", /* method handle: setTime(java.lang.Object java.lang.Object ) */ null);
    private Object setMilliseconds;
    private Object setUTCMilliseconds;
    private Object setSeconds;
    private Object setUTCSeconds;
    private Object setMinutes;
    private Object setUTCMinutes;
    private Object setHours;
    private Object setUTCHours;
    private Object setDate;
    private Object setUTCDate;
    private Object setMonth;
    private Object setUTCMonth;
    private Object setFullYear;
    private Object setUTCFullYear;
    private Object setYear;
    private Object toUTCString;
    private Object toGMTString;
    private Object toISOString;
    private Object toJSON;
    private static final PropertyMap $nasgenmap$;

    public Object G$toString() {
        return this.toString;
    }

    public void S$toString(Object object) {
        this.toString = object;
    }

    public Object G$toDateString() {
        return this.toDateString;
    }

    public void S$toDateString(Object object) {
        this.toDateString = object;
    }

    public Object G$toTimeString() {
        return this.toTimeString;
    }

    public void S$toTimeString(Object object) {
        this.toTimeString = object;
    }

    public Object G$toLocaleString() {
        return this.toLocaleString;
    }

    public void S$toLocaleString(Object object) {
        this.toLocaleString = object;
    }

    public Object G$toLocaleDateString() {
        return this.toLocaleDateString;
    }

    public void S$toLocaleDateString(Object object) {
        this.toLocaleDateString = object;
    }

    public Object G$toLocaleTimeString() {
        return this.toLocaleTimeString;
    }

    public void S$toLocaleTimeString(Object object) {
        this.toLocaleTimeString = object;
    }

    public Object G$valueOf() {
        return this.valueOf;
    }

    public void S$valueOf(Object object) {
        this.valueOf = object;
    }

    public Object G$getTime() {
        return this.getTime;
    }

    public void S$getTime(Object object) {
        this.getTime = object;
    }

    public Object G$getFullYear() {
        return this.getFullYear;
    }

    public void S$getFullYear(Object object) {
        this.getFullYear = object;
    }

    public Object G$getUTCFullYear() {
        return this.getUTCFullYear;
    }

    public void S$getUTCFullYear(Object object) {
        this.getUTCFullYear = object;
    }

    public Object G$getYear() {
        return this.getYear;
    }

    public void S$getYear(Object object) {
        this.getYear = object;
    }

    public Object G$getMonth() {
        return this.getMonth;
    }

    public void S$getMonth(Object object) {
        this.getMonth = object;
    }

    public Object G$getUTCMonth() {
        return this.getUTCMonth;
    }

    public void S$getUTCMonth(Object object) {
        this.getUTCMonth = object;
    }

    public Object G$getDate() {
        return this.getDate;
    }

    public void S$getDate(Object object) {
        this.getDate = object;
    }

    public Object G$getUTCDate() {
        return this.getUTCDate;
    }

    public void S$getUTCDate(Object object) {
        this.getUTCDate = object;
    }

    public Object G$getDay() {
        return this.getDay;
    }

    public void S$getDay(Object object) {
        this.getDay = object;
    }

    public Object G$getUTCDay() {
        return this.getUTCDay;
    }

    public void S$getUTCDay(Object object) {
        this.getUTCDay = object;
    }

    public Object G$getHours() {
        return this.getHours;
    }

    public void S$getHours(Object object) {
        this.getHours = object;
    }

    public Object G$getUTCHours() {
        return this.getUTCHours;
    }

    public void S$getUTCHours(Object object) {
        this.getUTCHours = object;
    }

    public Object G$getMinutes() {
        return this.getMinutes;
    }

    public void S$getMinutes(Object object) {
        this.getMinutes = object;
    }

    public Object G$getUTCMinutes() {
        return this.getUTCMinutes;
    }

    public void S$getUTCMinutes(Object object) {
        this.getUTCMinutes = object;
    }

    public Object G$getSeconds() {
        return this.getSeconds;
    }

    public void S$getSeconds(Object object) {
        this.getSeconds = object;
    }

    public Object G$getUTCSeconds() {
        return this.getUTCSeconds;
    }

    public void S$getUTCSeconds(Object object) {
        this.getUTCSeconds = object;
    }

    public Object G$getMilliseconds() {
        return this.getMilliseconds;
    }

    public void S$getMilliseconds(Object object) {
        this.getMilliseconds = object;
    }

    public Object G$getUTCMilliseconds() {
        return this.getUTCMilliseconds;
    }

    public void S$getUTCMilliseconds(Object object) {
        this.getUTCMilliseconds = object;
    }

    public Object G$getTimezoneOffset() {
        return this.getTimezoneOffset;
    }

    public void S$getTimezoneOffset(Object object) {
        this.getTimezoneOffset = object;
    }

    public Object G$setTime() {
        return this.setTime;
    }

    public void S$setTime(Object object) {
        this.setTime = object;
    }

    public Object G$setMilliseconds() {
        return this.setMilliseconds;
    }

    public void S$setMilliseconds(Object object) {
        this.setMilliseconds = object;
    }

    public Object G$setUTCMilliseconds() {
        return this.setUTCMilliseconds;
    }

    public void S$setUTCMilliseconds(Object object) {
        this.setUTCMilliseconds = object;
    }

    public Object G$setSeconds() {
        return this.setSeconds;
    }

    public void S$setSeconds(Object object) {
        this.setSeconds = object;
    }

    public Object G$setUTCSeconds() {
        return this.setUTCSeconds;
    }

    public void S$setUTCSeconds(Object object) {
        this.setUTCSeconds = object;
    }

    public Object G$setMinutes() {
        return this.setMinutes;
    }

    public void S$setMinutes(Object object) {
        this.setMinutes = object;
    }

    public Object G$setUTCMinutes() {
        return this.setUTCMinutes;
    }

    public void S$setUTCMinutes(Object object) {
        this.setUTCMinutes = object;
    }

    public Object G$setHours() {
        return this.setHours;
    }

    public void S$setHours(Object object) {
        this.setHours = object;
    }

    public Object G$setUTCHours() {
        return this.setUTCHours;
    }

    public void S$setUTCHours(Object object) {
        this.setUTCHours = object;
    }

    public Object G$setDate() {
        return this.setDate;
    }

    public void S$setDate(Object object) {
        this.setDate = object;
    }

    public Object G$setUTCDate() {
        return this.setUTCDate;
    }

    public void S$setUTCDate(Object object) {
        this.setUTCDate = object;
    }

    public Object G$setMonth() {
        return this.setMonth;
    }

    public void S$setMonth(Object object) {
        this.setMonth = object;
    }

    public Object G$setUTCMonth() {
        return this.setUTCMonth;
    }

    public void S$setUTCMonth(Object object) {
        this.setUTCMonth = object;
    }

    public Object G$setFullYear() {
        return this.setFullYear;
    }

    public void S$setFullYear(Object object) {
        this.setFullYear = object;
    }

    public Object G$setUTCFullYear() {
        return this.setUTCFullYear;
    }

    public void S$setUTCFullYear(Object object) {
        this.setUTCFullYear = object;
    }

    public Object G$setYear() {
        return this.setYear;
    }

    public void S$setYear(Object object) {
        this.setYear = object;
    }

    public Object G$toUTCString() {
        return this.toUTCString;
    }

    public void S$toUTCString(Object object) {
        this.toUTCString = object;
    }

    public Object G$toGMTString() {
        return this.toGMTString;
    }

    public void S$toGMTString(Object object) {
        this.toGMTString = object;
    }

    public Object G$toISOString() {
        return this.toISOString;
    }

    public void S$toISOString(Object object) {
        this.toISOString = object;
    }

    public Object G$toJSON() {
        return this.toJSON;
    }

    public void S$toJSON(Object object) {
        this.toJSON = object;
    }

    static {
        ArrayList<Property> arrayList = new ArrayList<Property>(46);
        arrayList.add(AccessorProperty.create("toString", 2, cfr_ldc_0(), cfr_ldc_1()));
        arrayList.add(AccessorProperty.create("toDateString", 2, cfr_ldc_2(), cfr_ldc_3()));
        arrayList.add(AccessorProperty.create("toTimeString", 2, cfr_ldc_4(), cfr_ldc_5()));
        arrayList.add(AccessorProperty.create("toLocaleString", 2, cfr_ldc_6(), cfr_ldc_7()));
        arrayList.add(AccessorProperty.create("toLocaleDateString", 2, cfr_ldc_8(), cfr_ldc_9()));
        arrayList.add(AccessorProperty.create("toLocaleTimeString", 2, cfr_ldc_10(), cfr_ldc_11()));
        arrayList.add(AccessorProperty.create("valueOf", 2, cfr_ldc_12(), cfr_ldc_13()));
        arrayList.add(AccessorProperty.create("getTime", 2, cfr_ldc_14(), cfr_ldc_15()));
        arrayList.add(AccessorProperty.create("getFullYear", 2, cfr_ldc_16(), cfr_ldc_17()));
        arrayList.add(AccessorProperty.create("getUTCFullYear", 2, cfr_ldc_18(), cfr_ldc_19()));
        arrayList.add(AccessorProperty.create("getYear", 2, cfr_ldc_20(), cfr_ldc_21()));
        arrayList.add(AccessorProperty.create("getMonth", 2, cfr_ldc_22(), cfr_ldc_23()));
        arrayList.add(AccessorProperty.create("getUTCMonth", 2, cfr_ldc_24(), cfr_ldc_25()));
        arrayList.add(AccessorProperty.create("getDate", 2, cfr_ldc_26(), cfr_ldc_27()));
        arrayList.add(AccessorProperty.create("getUTCDate", 2, cfr_ldc_28(), cfr_ldc_29()));
        arrayList.add(AccessorProperty.create("getDay", 2, cfr_ldc_30(), cfr_ldc_31()));
        arrayList.add(AccessorProperty.create("getUTCDay", 2, cfr_ldc_32(), cfr_ldc_33()));
        arrayList.add(AccessorProperty.create("getHours", 2, cfr_ldc_34(), cfr_ldc_35()));
        arrayList.add(AccessorProperty.create("getUTCHours", 2, cfr_ldc_36(), cfr_ldc_37()));
        arrayList.add(AccessorProperty.create("getMinutes", 2, cfr_ldc_38(), cfr_ldc_39()));
        arrayList.add(AccessorProperty.create("getUTCMinutes", 2, cfr_ldc_40(), cfr_ldc_41()));
        arrayList.add(AccessorProperty.create("getSeconds", 2, cfr_ldc_42(), cfr_ldc_43()));
        arrayList.add(AccessorProperty.create("getUTCSeconds", 2, cfr_ldc_44(), cfr_ldc_45()));
        arrayList.add(AccessorProperty.create("getMilliseconds", 2, cfr_ldc_46(), cfr_ldc_47()));
        arrayList.add(AccessorProperty.create("getUTCMilliseconds", 2, cfr_ldc_48(), cfr_ldc_49()));
        arrayList.add(AccessorProperty.create("getTimezoneOffset", 2, cfr_ldc_50(), cfr_ldc_51()));
        arrayList.add(AccessorProperty.create("setTime", 2, cfr_ldc_52(), cfr_ldc_53()));
        arrayList.add(AccessorProperty.create("setMilliseconds", 2, cfr_ldc_54(), cfr_ldc_55()));
        arrayList.add(AccessorProperty.create("setUTCMilliseconds", 2, cfr_ldc_56(), cfr_ldc_57()));
        arrayList.add(AccessorProperty.create("setSeconds", 2, cfr_ldc_58(), cfr_ldc_59()));
        arrayList.add(AccessorProperty.create("setUTCSeconds", 2, cfr_ldc_60(), cfr_ldc_61()));
        arrayList.add(AccessorProperty.create("setMinutes", 2, cfr_ldc_62(), cfr_ldc_63()));
        arrayList.add(AccessorProperty.create("setUTCMinutes", 2, cfr_ldc_64(), cfr_ldc_65()));
        arrayList.add(AccessorProperty.create("setHours", 2, cfr_ldc_66(), cfr_ldc_67()));
        arrayList.add(AccessorProperty.create("setUTCHours", 2, cfr_ldc_68(), cfr_ldc_69()));
        arrayList.add(AccessorProperty.create("setDate", 2, cfr_ldc_70(), cfr_ldc_71()));
        arrayList.add(AccessorProperty.create("setUTCDate", 2, cfr_ldc_72(), cfr_ldc_73()));
        arrayList.add(AccessorProperty.create("setMonth", 2, cfr_ldc_74(), cfr_ldc_75()));
        arrayList.add(AccessorProperty.create("setUTCMonth", 2, cfr_ldc_76(), cfr_ldc_77()));
        arrayList.add(AccessorProperty.create("setFullYear", 2, cfr_ldc_78(), cfr_ldc_79()));
        arrayList.add(AccessorProperty.create("setUTCFullYear", 2, cfr_ldc_80(), cfr_ldc_81()));
        arrayList.add(AccessorProperty.create("setYear", 2, cfr_ldc_82(), cfr_ldc_83()));
        arrayList.add(AccessorProperty.create("toUTCString", 2, cfr_ldc_84(), cfr_ldc_85()));
        arrayList.add(AccessorProperty.create("toGMTString", 2, cfr_ldc_86(), cfr_ldc_87()));
        arrayList.add(AccessorProperty.create("toISOString", 2, cfr_ldc_88(), cfr_ldc_89()));
        arrayList.add(AccessorProperty.create("toJSON", 2, cfr_ldc_90(), cfr_ldc_91()));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    NativeDate$Prototype() {
        super($nasgenmap$);
        ScriptFunction scriptFunction = ScriptFunction.createBuiltin("setMilliseconds", cfr_ldc_92());
        scriptFunction.setArity(1);
        this.setMilliseconds = scriptFunction;
        ScriptFunction scriptFunction2 = ScriptFunction.createBuiltin("setUTCMilliseconds", cfr_ldc_93());
        scriptFunction2.setArity(1);
        this.setUTCMilliseconds = scriptFunction2;
        ScriptFunction scriptFunction3 = ScriptFunction.createBuiltin("setSeconds", cfr_ldc_94());
        scriptFunction3.setArity(2);
        this.setSeconds = scriptFunction3;
        ScriptFunction scriptFunction4 = ScriptFunction.createBuiltin("setUTCSeconds", cfr_ldc_95());
        scriptFunction4.setArity(2);
        this.setUTCSeconds = scriptFunction4;
        ScriptFunction scriptFunction5 = ScriptFunction.createBuiltin("setMinutes", cfr_ldc_96());
        scriptFunction5.setArity(3);
        this.setMinutes = scriptFunction5;
        ScriptFunction scriptFunction6 = ScriptFunction.createBuiltin("setUTCMinutes", cfr_ldc_97());
        scriptFunction6.setArity(3);
        this.setUTCMinutes = scriptFunction6;
        ScriptFunction scriptFunction7 = ScriptFunction.createBuiltin("setHours", cfr_ldc_98());
        scriptFunction7.setArity(4);
        this.setHours = scriptFunction7;
        ScriptFunction scriptFunction8 = ScriptFunction.createBuiltin("setUTCHours", cfr_ldc_99());
        scriptFunction8.setArity(4);
        this.setUTCHours = scriptFunction8;
        ScriptFunction scriptFunction9 = ScriptFunction.createBuiltin("setDate", cfr_ldc_100());
        scriptFunction9.setArity(1);
        this.setDate = scriptFunction9;
        ScriptFunction scriptFunction10 = ScriptFunction.createBuiltin("setUTCDate", cfr_ldc_101());
        scriptFunction10.setArity(1);
        this.setUTCDate = scriptFunction10;
        ScriptFunction scriptFunction11 = ScriptFunction.createBuiltin("setMonth", cfr_ldc_102());
        scriptFunction11.setArity(2);
        this.setMonth = scriptFunction11;
        ScriptFunction scriptFunction12 = ScriptFunction.createBuiltin("setUTCMonth", cfr_ldc_103());
        scriptFunction12.setArity(2);
        this.setUTCMonth = scriptFunction12;
        ScriptFunction scriptFunction13 = ScriptFunction.createBuiltin("setFullYear", cfr_ldc_104());
        scriptFunction13.setArity(3);
        this.setFullYear = scriptFunction13;
        ScriptFunction scriptFunction14 = ScriptFunction.createBuiltin("setUTCFullYear", cfr_ldc_105());
        scriptFunction14.setArity(3);
        this.setUTCFullYear = scriptFunction14;
        this.setYear = ScriptFunction.createBuiltin("setYear", cfr_ldc_106());
        this.toUTCString = ScriptFunction.createBuiltin("toUTCString", cfr_ldc_107());
        this.toGMTString = ScriptFunction.createBuiltin("toGMTString", cfr_ldc_108());
        this.toISOString = ScriptFunction.createBuiltin("toISOString", cfr_ldc_109());
        this.toJSON = ScriptFunction.createBuiltin("toJSON", cfr_ldc_110());
    }

    @Override
    public String getClassName() {
        return "Date";
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$toString", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$toString", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$toDateString", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$toDateString", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$toTimeString", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$toTimeString", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$toLocaleString", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$toLocaleString", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$toLocaleDateString", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$toLocaleDateString", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$toLocaleTimeString", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$toLocaleTimeString", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$valueOf", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$valueOf", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$getTime", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$getTime", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$getFullYear", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$getFullYear", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$getUTCFullYear", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$getUTCFullYear", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$getYear", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$getYear", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$getMonth", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$getMonth", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$getUTCMonth", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$getUTCMonth", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$getDate", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$getDate", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$getUTCDate", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$getUTCDate", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$getDay", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$getDay", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$getUTCDay", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$getUTCDay", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$getHours", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$getHours", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$getUTCHours", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$getUTCHours", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$getMinutes", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$getMinutes", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$getUTCMinutes", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$getUTCMinutes", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$getSeconds", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$getSeconds", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$getUTCSeconds", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$getUTCSeconds", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$getMilliseconds", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$getMilliseconds", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$getUTCMilliseconds", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$getUTCMilliseconds", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$getTimezoneOffset", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$getTimezoneOffset", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$setTime", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$setTime", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$setMilliseconds", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$setMilliseconds", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$setUTCMilliseconds", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$setUTCMilliseconds", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$setSeconds", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$setSeconds", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$setUTCSeconds", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$setUTCSeconds", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$setMinutes", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$setMinutes", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$setUTCMinutes", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$setUTCMinutes", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$setHours", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$setHours", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$setUTCHours", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$setUTCHours", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$setDate", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$setDate", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_72() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$setUTCDate", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_73() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$setUTCDate", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_74() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$setMonth", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_75() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$setMonth", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_76() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$setUTCMonth", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_77() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$setUTCMonth", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_78() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$setFullYear", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_79() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$setFullYear", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_80() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$setUTCFullYear", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_81() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$setUTCFullYear", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_82() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$setYear", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_83() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$setYear", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_84() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$toUTCString", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_85() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$toUTCString", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_86() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$toGMTString", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_87() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$toGMTString", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_88() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$toISOString", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_89() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$toISOString", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_90() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "G$toJSON", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_91() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDate$Prototype.class, "S$toJSON", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_92() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "setMilliseconds", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;[Ljava/lang/Object;)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_93() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "setUTCMilliseconds", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;[Ljava/lang/Object;)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_94() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "setSeconds", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;[Ljava/lang/Object;)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_95() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "setUTCSeconds", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;[Ljava/lang/Object;)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_96() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "setMinutes", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;[Ljava/lang/Object;)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_97() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "setUTCMinutes", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;[Ljava/lang/Object;)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_98() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "setHours", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;[Ljava/lang/Object;)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_99() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "setUTCHours", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;[Ljava/lang/Object;)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_100() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "setDate", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;[Ljava/lang/Object;)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_101() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "setUTCDate", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;[Ljava/lang/Object;)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_102() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "setMonth", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;[Ljava/lang/Object;)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_103() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "setUTCMonth", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;[Ljava/lang/Object;)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_104() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "setFullYear", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;[Ljava/lang/Object;)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_105() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "setUTCFullYear", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;[Ljava/lang/Object;)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_106() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "setYear", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_107() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "toUTCString", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/String;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_108() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "toGMTString", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/String;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_109() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "toISOString", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/String;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_110() {
        try {
            return MethodHandles.lookup().findStatic(NativeDate.class, "toJSON", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

