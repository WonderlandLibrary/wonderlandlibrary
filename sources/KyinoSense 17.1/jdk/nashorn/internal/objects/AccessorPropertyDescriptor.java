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
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyDescriptor;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;

public final class AccessorPropertyDescriptor
extends ScriptObject
implements PropertyDescriptor {
    public Object configurable;
    public Object enumerable;
    public Object get;
    public Object set;
    private static PropertyMap $nasgenmap$;

    AccessorPropertyDescriptor(boolean configurable, boolean enumerable, Object get, Object set, Global global) {
        super(global.getObjectPrototype(), $nasgenmap$);
        this.configurable = configurable;
        this.enumerable = enumerable;
        this.get = get;
        this.set = set;
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
        return true;
    }

    @Override
    public Object getValue() {
        throw new UnsupportedOperationException("value");
    }

    @Override
    public ScriptFunction getGetter() {
        return this.get instanceof ScriptFunction ? (ScriptFunction)this.get : null;
    }

    @Override
    public ScriptFunction getSetter() {
        return this.set instanceof ScriptFunction ? (ScriptFunction)this.set : null;
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
        throw new UnsupportedOperationException("set writable");
    }

    @Override
    public void setValue(Object value) {
        throw new UnsupportedOperationException("set value");
    }

    @Override
    public void setGetter(Object getter) {
        this.get = getter;
    }

    @Override
    public void setSetter(Object setter) {
        this.set = setter;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
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
        if (sobj.has("get")) {
            Object getter = sobj.get("get");
            if (getter != ScriptRuntime.UNDEFINED && !(getter instanceof ScriptFunction)) throw ECMAErrors.typeError("not.a.function", ScriptRuntime.safeToString(getter));
            this.get = getter;
        } else {
            this.delete("get", false);
        }
        if (sobj.has("set")) {
            Object setter = sobj.get("set");
            if (setter != ScriptRuntime.UNDEFINED && !(setter instanceof ScriptFunction)) throw ECMAErrors.typeError("not.a.function", ScriptRuntime.safeToString(setter));
            this.set = setter;
            return this;
        } else {
            this.delete("set", false);
        }
        return this;
    }

    @Override
    public int type() {
        return 2;
    }

    @Override
    public boolean hasAndEquals(PropertyDescriptor otherDesc) {
        if (!(otherDesc instanceof AccessorPropertyDescriptor)) {
            return false;
        }
        AccessorPropertyDescriptor other = (AccessorPropertyDescriptor)otherDesc;
        return !(this.has("configurable") && !ScriptRuntime.sameValue(this.configurable, other.configurable) || this.has("enumerable") && !ScriptRuntime.sameValue(this.enumerable, other.enumerable) || this.has("get") && !ScriptRuntime.sameValue(this.get, other.get) || this.has("set") && !ScriptRuntime.sameValue(this.set, other.set));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AccessorPropertyDescriptor)) {
            return false;
        }
        AccessorPropertyDescriptor other = (AccessorPropertyDescriptor)obj;
        return ScriptRuntime.sameValue(this.configurable, other.configurable) && ScriptRuntime.sameValue(this.enumerable, other.enumerable) && ScriptRuntime.sameValue(this.get, other.get) && ScriptRuntime.sameValue(this.set, other.set);
    }

    public String toString() {
        return '[' + this.getClass().getSimpleName() + " {configurable=" + this.configurable + " enumerable=" + this.enumerable + " getter=" + this.get + " setter=" + this.set + "}]";
    }

    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.configurable);
        hash = 41 * hash + Objects.hashCode(this.enumerable);
        hash = 41 * hash + Objects.hashCode(this.get);
        hash = 41 * hash + Objects.hashCode(this.set);
        return hash;
    }

    static {
        AccessorPropertyDescriptor.$clinit$();
    }

    public static void $clinit$() {
        ArrayList<Property> arrayList = new ArrayList<Property>(4);
        arrayList.add(AccessorProperty.create("configurable", 0, cfr_ldc_0(), cfr_ldc_1()));
        arrayList.add(AccessorProperty.create("enumerable", 0, cfr_ldc_2(), cfr_ldc_3()));
        arrayList.add(AccessorProperty.create("get", 0, cfr_ldc_4(), cfr_ldc_5()));
        arrayList.add(AccessorProperty.create("set", 0, cfr_ldc_6(), cfr_ldc_7()));
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

    public Object G$get() {
        return this.get;
    }

    public void S$get(Object object) {
        this.get = object;
    }

    public Object G$set() {
        return this.set;
    }

    public void S$set(Object object) {
        this.set = object;
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(AccessorPropertyDescriptor.class, "G$configurable", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(AccessorPropertyDescriptor.class, "S$configurable", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(AccessorPropertyDescriptor.class, "G$enumerable", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(AccessorPropertyDescriptor.class, "S$enumerable", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(AccessorPropertyDescriptor.class, "G$get", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(AccessorPropertyDescriptor.class, "S$get", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(AccessorPropertyDescriptor.class, "G$set", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(AccessorPropertyDescriptor.class, "S$set", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

