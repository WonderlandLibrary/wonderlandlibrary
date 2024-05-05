/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Objects;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyDescriptor;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;

public final class DataPropertyDescriptor
extends ScriptObject
implements PropertyDescriptor {
    public Object configurable;
    public Object enumerable;
    public Object writable;
    public Object value;
    private static PropertyMap $nasgenmap$;

    DataPropertyDescriptor(boolean configurable, boolean enumerable, boolean writable, Object value, Global global) {
        super(global.getObjectPrototype(), $nasgenmap$);
        this.configurable = configurable;
        this.enumerable = enumerable;
        this.writable = writable;
        this.value = value;
    }

    @Override
    public boolean isConfigurable() {
        return JSType.toBoolean(this.configurable);
    }

    @Override
    public boolean isEnumerable() {
        return JSType.toBoolean(this.enumerable);
    }

    @Override
    public boolean isWritable() {
        return JSType.toBoolean(this.writable);
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public ScriptFunction getGetter() {
        throw new UnsupportedOperationException("getter");
    }

    @Override
    public ScriptFunction getSetter() {
        throw new UnsupportedOperationException("setter");
    }

    @Override
    public void setConfigurable(boolean flag) {
        this.configurable = flag;
    }

    @Override
    public void setEnumerable(boolean flag) {
        this.enumerable = flag;
    }

    @Override
    public void setWritable(boolean flag) {
        this.writable = flag;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public void setGetter(Object getter) {
        throw new UnsupportedOperationException("set getter");
    }

    @Override
    public void setSetter(Object setter) {
        throw new UnsupportedOperationException("set setter");
    }

    @Override
    public PropertyDescriptor fillFrom(ScriptObject sobj) {
        if (sobj.has("configurable")) {
            this.configurable = JSType.toBoolean(sobj.get("configurable"));
        } else {
            this.delete("configurable", false);
        }
        if (sobj.has("enumerable")) {
            this.enumerable = JSType.toBoolean(sobj.get("enumerable"));
        } else {
            this.delete("enumerable", false);
        }
        if (sobj.has("writable")) {
            this.writable = JSType.toBoolean(sobj.get("writable"));
        } else {
            this.delete("writable", false);
        }
        if (sobj.has("value")) {
            this.value = sobj.get("value");
        } else {
            this.delete("value", false);
        }
        return this;
    }

    @Override
    public int type() {
        return 1;
    }

    @Override
    public boolean hasAndEquals(PropertyDescriptor otherDesc) {
        if (!(otherDesc instanceof DataPropertyDescriptor)) {
            return false;
        }
        DataPropertyDescriptor other = (DataPropertyDescriptor)otherDesc;
        return !(this.has("configurable") && !ScriptRuntime.sameValue(this.configurable, other.configurable) || this.has("enumerable") && !ScriptRuntime.sameValue(this.enumerable, other.enumerable) || this.has("writable") && !ScriptRuntime.sameValue(this.writable, other.writable) || this.has("value") && !ScriptRuntime.sameValue(this.value, other.value));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DataPropertyDescriptor)) {
            return false;
        }
        DataPropertyDescriptor other = (DataPropertyDescriptor)obj;
        return ScriptRuntime.sameValue(this.configurable, other.configurable) && ScriptRuntime.sameValue(this.enumerable, other.enumerable) && ScriptRuntime.sameValue(this.writable, other.writable) && ScriptRuntime.sameValue(this.value, other.value);
    }

    public String toString() {
        return '[' + this.getClass().getSimpleName() + " {configurable=" + this.configurable + " enumerable=" + this.enumerable + " writable=" + this.writable + " value=" + this.value + "}]";
    }

    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.configurable);
        hash = 43 * hash + Objects.hashCode(this.enumerable);
        hash = 43 * hash + Objects.hashCode(this.writable);
        hash = 43 * hash + Objects.hashCode(this.value);
        return hash;
    }

    static {
        DataPropertyDescriptor.$clinit$();
    }

    public static void $clinit$() {
        ArrayList<Property> arrayList = new ArrayList<Property>(4);
        arrayList.add(AccessorProperty.create("configurable", 0, cfr_ldc_0(), cfr_ldc_1()));
        arrayList.add(AccessorProperty.create("enumerable", 0, cfr_ldc_2(), cfr_ldc_3()));
        arrayList.add(AccessorProperty.create("writable", 0, cfr_ldc_4(), cfr_ldc_5()));
        arrayList.add(AccessorProperty.create("value", 0, cfr_ldc_6(), cfr_ldc_7()));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    public Object G$configurable() {
        return this.configurable;
    }

    public void S$configurable(Object object) {
        this.configurable = object;
    }

    public Object G$enumerable() {
        return this.enumerable;
    }

    public void S$enumerable(Object object) {
        this.enumerable = object;
    }

    public Object G$writable() {
        return this.writable;
    }

    public void S$writable(Object object) {
        this.writable = object;
    }

    public Object G$value() {
        return this.value;
    }

    public void S$value(Object object) {
        this.value = object;
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(DataPropertyDescriptor.class, "G$configurable", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(DataPropertyDescriptor.class, "S$configurable", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(DataPropertyDescriptor.class, "G$enumerable", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(DataPropertyDescriptor.class, "S$enumerable", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(DataPropertyDescriptor.class, "G$writable", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(DataPropertyDescriptor.class, "S$writable", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(DataPropertyDescriptor.class, "G$value", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(DataPropertyDescriptor.class, "S$value", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

