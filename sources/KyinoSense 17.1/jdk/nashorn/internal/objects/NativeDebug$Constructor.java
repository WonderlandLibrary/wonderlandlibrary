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

final class NativeDebug$Constructor
extends ScriptObject {
    private Object getArrayDataClass = ScriptFunction.createBuiltin("getArrayDataClass", /* method handle: getArrayDataClass(java.lang.Object java.lang.Object ) */ null);
    private Object getArrayData = ScriptFunction.createBuiltin("getArrayData", /* method handle: getArrayData(java.lang.Object java.lang.Object ) */ null);
    private Object getContext = ScriptFunction.createBuiltin("getContext", /* method handle: getContext(java.lang.Object ) */ null);
    private Object map = ScriptFunction.createBuiltin("map", /* method handle: map(java.lang.Object java.lang.Object ) */ null);
    private Object identical = ScriptFunction.createBuiltin("identical", /* method handle: identical(java.lang.Object java.lang.Object java.lang.Object ) */ null);
    private Object equalWithoutType = ScriptFunction.createBuiltin("equalWithoutType", /* method handle: equalWithoutType(java.lang.Object java.lang.Object java.lang.Object ) */ null);
    private Object diffPropertyMaps = ScriptFunction.createBuiltin("diffPropertyMaps", /* method handle: diffPropertyMaps(java.lang.Object java.lang.Object java.lang.Object ) */ null);
    private Object getClass = ScriptFunction.createBuiltin("getClass", /* method handle: getClass(java.lang.Object java.lang.Object ) */ null);
    private Object equals = ScriptFunction.createBuiltin("equals", /* method handle: equals(java.lang.Object java.lang.Object java.lang.Object ) */ null);
    private Object toJavaString = ScriptFunction.createBuiltin("toJavaString", /* method handle: toJavaString(java.lang.Object java.lang.Object ) */ null);
    private Object toIdentString = ScriptFunction.createBuiltin("toIdentString", /* method handle: toIdentString(java.lang.Object java.lang.Object ) */ null);
    private Object getListenerCount = ScriptFunction.createBuiltin("getListenerCount", /* method handle: getListenerCount(java.lang.Object java.lang.Object ) */ null);
    private Object dumpCounters = ScriptFunction.createBuiltin("dumpCounters", /* method handle: dumpCounters(java.lang.Object ) */ null);
    private Object getEventQueueCapacity = ScriptFunction.createBuiltin("getEventQueueCapacity", /* method handle: getEventQueueCapacity(java.lang.Object ) */ null);
    private Object setEventQueueCapacity = ScriptFunction.createBuiltin("setEventQueueCapacity", /* method handle: setEventQueueCapacity(java.lang.Object java.lang.Object ) */ null);
    private Object addRuntimeEvent = ScriptFunction.createBuiltin("addRuntimeEvent", /* method handle: addRuntimeEvent(java.lang.Object java.lang.Object ) */ null);
    private Object expandEventQueueCapacity = ScriptFunction.createBuiltin("expandEventQueueCapacity", /* method handle: expandEventQueueCapacity(java.lang.Object java.lang.Object ) */ null);
    private Object clearRuntimeEvents = ScriptFunction.createBuiltin("clearRuntimeEvents", /* method handle: clearRuntimeEvents(java.lang.Object ) */ null);
    private Object removeRuntimeEvent = ScriptFunction.createBuiltin("removeRuntimeEvent", /* method handle: removeRuntimeEvent(java.lang.Object java.lang.Object ) */ null);
    private Object getRuntimeEvents = ScriptFunction.createBuiltin("getRuntimeEvents", /* method handle: getRuntimeEvents(java.lang.Object ) */ null);
    private Object getLastRuntimeEvent = ScriptFunction.createBuiltin("getLastRuntimeEvent", /* method handle: getLastRuntimeEvent(java.lang.Object ) */ null);
    private static final PropertyMap $nasgenmap$;

    public Object G$getArrayDataClass() {
        return this.getArrayDataClass;
    }

    public void S$getArrayDataClass(Object object) {
        this.getArrayDataClass = object;
    }

    public Object G$getArrayData() {
        return this.getArrayData;
    }

    public void S$getArrayData(Object object) {
        this.getArrayData = object;
    }

    public Object G$getContext() {
        return this.getContext;
    }

    public void S$getContext(Object object) {
        this.getContext = object;
    }

    public Object G$map() {
        return this.map;
    }

    public void S$map(Object object) {
        this.map = object;
    }

    public Object G$identical() {
        return this.identical;
    }

    public void S$identical(Object object) {
        this.identical = object;
    }

    public Object G$equalWithoutType() {
        return this.equalWithoutType;
    }

    public void S$equalWithoutType(Object object) {
        this.equalWithoutType = object;
    }

    public Object G$diffPropertyMaps() {
        return this.diffPropertyMaps;
    }

    public void S$diffPropertyMaps(Object object) {
        this.diffPropertyMaps = object;
    }

    public Object G$getClass() {
        return this.getClass;
    }

    public void S$getClass(Object object) {
        this.getClass = object;
    }

    public Object G$equals() {
        return this.equals;
    }

    public void S$equals(Object object) {
        this.equals = object;
    }

    public Object G$toJavaString() {
        return this.toJavaString;
    }

    public void S$toJavaString(Object object) {
        this.toJavaString = object;
    }

    public Object G$toIdentString() {
        return this.toIdentString;
    }

    public void S$toIdentString(Object object) {
        this.toIdentString = object;
    }

    public Object G$getListenerCount() {
        return this.getListenerCount;
    }

    public void S$getListenerCount(Object object) {
        this.getListenerCount = object;
    }

    public Object G$dumpCounters() {
        return this.dumpCounters;
    }

    public void S$dumpCounters(Object object) {
        this.dumpCounters = object;
    }

    public Object G$getEventQueueCapacity() {
        return this.getEventQueueCapacity;
    }

    public void S$getEventQueueCapacity(Object object) {
        this.getEventQueueCapacity = object;
    }

    public Object G$setEventQueueCapacity() {
        return this.setEventQueueCapacity;
    }

    public void S$setEventQueueCapacity(Object object) {
        this.setEventQueueCapacity = object;
    }

    public Object G$addRuntimeEvent() {
        return this.addRuntimeEvent;
    }

    public void S$addRuntimeEvent(Object object) {
        this.addRuntimeEvent = object;
    }

    public Object G$expandEventQueueCapacity() {
        return this.expandEventQueueCapacity;
    }

    public void S$expandEventQueueCapacity(Object object) {
        this.expandEventQueueCapacity = object;
    }

    public Object G$clearRuntimeEvents() {
        return this.clearRuntimeEvents;
    }

    public void S$clearRuntimeEvents(Object object) {
        this.clearRuntimeEvents = object;
    }

    public Object G$removeRuntimeEvent() {
        return this.removeRuntimeEvent;
    }

    public void S$removeRuntimeEvent(Object object) {
        this.removeRuntimeEvent = object;
    }

    public Object G$getRuntimeEvents() {
        return this.getRuntimeEvents;
    }

    public void S$getRuntimeEvents(Object object) {
        this.getRuntimeEvents = object;
    }

    public Object G$getLastRuntimeEvent() {
        return this.getLastRuntimeEvent;
    }

    public void S$getLastRuntimeEvent(Object object) {
        this.getLastRuntimeEvent = object;
    }

    static {
        ArrayList<Property> arrayList = new ArrayList<Property>(21);
        arrayList.add(AccessorProperty.create("getArrayDataClass", 2, cfr_ldc_0(), cfr_ldc_1()));
        arrayList.add(AccessorProperty.create("getArrayData", 2, cfr_ldc_2(), cfr_ldc_3()));
        arrayList.add(AccessorProperty.create("getContext", 2, cfr_ldc_4(), cfr_ldc_5()));
        arrayList.add(AccessorProperty.create("map", 2, cfr_ldc_6(), cfr_ldc_7()));
        arrayList.add(AccessorProperty.create("identical", 2, cfr_ldc_8(), cfr_ldc_9()));
        arrayList.add(AccessorProperty.create("equalWithoutType", 2, cfr_ldc_10(), cfr_ldc_11()));
        arrayList.add(AccessorProperty.create("diffPropertyMaps", 2, cfr_ldc_12(), cfr_ldc_13()));
        arrayList.add(AccessorProperty.create("getClass", 2, cfr_ldc_14(), cfr_ldc_15()));
        arrayList.add(AccessorProperty.create("equals", 2, cfr_ldc_16(), cfr_ldc_17()));
        arrayList.add(AccessorProperty.create("toJavaString", 2, cfr_ldc_18(), cfr_ldc_19()));
        arrayList.add(AccessorProperty.create("toIdentString", 2, cfr_ldc_20(), cfr_ldc_21()));
        arrayList.add(AccessorProperty.create("getListenerCount", 2, cfr_ldc_22(), cfr_ldc_23()));
        arrayList.add(AccessorProperty.create("dumpCounters", 2, cfr_ldc_24(), cfr_ldc_25()));
        arrayList.add(AccessorProperty.create("getEventQueueCapacity", 2, cfr_ldc_26(), cfr_ldc_27()));
        arrayList.add(AccessorProperty.create("setEventQueueCapacity", 2, cfr_ldc_28(), cfr_ldc_29()));
        arrayList.add(AccessorProperty.create("addRuntimeEvent", 2, cfr_ldc_30(), cfr_ldc_31()));
        arrayList.add(AccessorProperty.create("expandEventQueueCapacity", 2, cfr_ldc_32(), cfr_ldc_33()));
        arrayList.add(AccessorProperty.create("clearRuntimeEvents", 2, cfr_ldc_34(), cfr_ldc_35()));
        arrayList.add(AccessorProperty.create("removeRuntimeEvent", 2, cfr_ldc_36(), cfr_ldc_37()));
        arrayList.add(AccessorProperty.create("getRuntimeEvents", 2, cfr_ldc_38(), cfr_ldc_39()));
        arrayList.add(AccessorProperty.create("getLastRuntimeEvent", 2, cfr_ldc_40(), cfr_ldc_41()));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    NativeDebug$Constructor() {
        super($nasgenmap$);
    }

    @Override
    public String getClassName() {
        return "Debug";
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$getArrayDataClass", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$getArrayDataClass", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$getArrayData", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$getArrayData", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$getContext", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$getContext", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$map", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$map", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$identical", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$identical", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$equalWithoutType", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$equalWithoutType", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$diffPropertyMaps", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$diffPropertyMaps", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$getClass", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$getClass", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$equals", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$equals", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$toJavaString", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$toJavaString", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$toIdentString", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$toIdentString", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$getListenerCount", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$getListenerCount", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$dumpCounters", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$dumpCounters", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$getEventQueueCapacity", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$getEventQueueCapacity", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$setEventQueueCapacity", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$setEventQueueCapacity", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$addRuntimeEvent", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$addRuntimeEvent", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$expandEventQueueCapacity", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$expandEventQueueCapacity", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$clearRuntimeEvents", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$clearRuntimeEvents", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$removeRuntimeEvent", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$removeRuntimeEvent", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$getRuntimeEvents", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$getRuntimeEvents", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "G$getLastRuntimeEvent", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeDebug$Constructor.class, "S$getLastRuntimeEvent", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

