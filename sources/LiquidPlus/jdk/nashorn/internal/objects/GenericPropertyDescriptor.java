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

public final class GenericPropertyDescriptor
extends ScriptObject
implements PropertyDescriptor {
    public Object configurable;
    public Object enumerable;
    private static PropertyMap $nasgenmap$;

    GenericPropertyDescriptor(boolean configurable, boolean enumerable, Global global) {
        super(global.getObjectPrototype(), $nasgenmap$);
        this.configurable = configurable;
        this.enumerable = enumerable;
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
        return false;
    }

    @Override
    public Object getValue() {
        throw new UnsupportedOperationException("value");
    }

    @Override
    public ScriptFunction getGetter() {
        throw new UnsupportedOperationException("get");
    }

    @Override
    public ScriptFunction getSetter() {
        throw new UnsupportedOperationException("set");
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
        return this;
    }

    @Override
    public int type() {
        return 0;
    }

    @Override
    public boolean hasAndEquals(PropertyDescriptor other) {
        if (this.has("configurable") && other.has("configurable") && this.isConfigurable() != other.isConfigurable()) {
            return false;
        }
        return !this.has("enumerable") || !other.has("enumerable") || this.isEnumerable() == other.isEnumerable();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GenericPropertyDescriptor)) {
            return false;
        }
        GenericPropertyDescriptor other = (GenericPropertyDescriptor)obj;
        return ScriptRuntime.sameValue(this.configurable, other.configurable) && ScriptRuntime.sameValue(this.enumerable, other.enumerable);
    }

    public String toString() {
        return '[' + this.getClass().getSimpleName() + " {configurable=" + this.configurable + " enumerable=" + this.enumerable + "}]";
    }

    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.configurable);
        hash = 97 * hash + Objects.hashCode(this.enumerable);
        return hash;
    }

    static {
        GenericPropertyDescriptor.$clinit$();
    }

    public static void $clinit$() {
        ArrayList<Property> arrayList = new ArrayList<Property>(2);
        arrayList.add(AccessorProperty.create("configurable", 0, cfr_ldc_0(), cfr_ldc_1()));
        arrayList.add(AccessorProperty.create("enumerable", 0, cfr_ldc_2(), cfr_ldc_3()));
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

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(GenericPropertyDescriptor.class, "G$configurable", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(GenericPropertyDescriptor.class, "S$configurable", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(GenericPropertyDescriptor.class, "G$enumerable", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(GenericPropertyDescriptor.class, "S$enumerable", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

