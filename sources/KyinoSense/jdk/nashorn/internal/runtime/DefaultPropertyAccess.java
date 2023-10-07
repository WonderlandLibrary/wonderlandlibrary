/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyAccess;

public abstract class DefaultPropertyAccess
implements PropertyAccess {
    @Override
    public int getInt(Object key, int programPoint) {
        return JSType.toInt32(this.get(key));
    }

    @Override
    public int getInt(double key, int programPoint) {
        return this.getInt(JSType.toObject(key), programPoint);
    }

    @Override
    public int getInt(int key, int programPoint) {
        return this.getInt(JSType.toObject(key), programPoint);
    }

    @Override
    public double getDouble(Object key, int programPoint) {
        return JSType.toNumber(this.get(key));
    }

    @Override
    public double getDouble(double key, int programPoint) {
        return this.getDouble(JSType.toObject(key), programPoint);
    }

    @Override
    public double getDouble(int key, int programPoint) {
        return this.getDouble(JSType.toObject(key), programPoint);
    }

    @Override
    public abstract Object get(Object var1);

    @Override
    public Object get(double key) {
        return this.get(JSType.toObject(key));
    }

    @Override
    public Object get(int key) {
        return this.get(JSType.toObject(key));
    }

    @Override
    public void set(double key, int value, int flags) {
        this.set(JSType.toObject(key), JSType.toObject(value), flags);
    }

    @Override
    public void set(double key, double value, int flags) {
        this.set(JSType.toObject(key), JSType.toObject(value), flags);
    }

    @Override
    public void set(double key, Object value, int flags) {
        this.set(JSType.toObject(key), JSType.toObject(value), flags);
    }

    @Override
    public void set(int key, int value, int flags) {
        this.set(JSType.toObject(key), JSType.toObject(value), flags);
    }

    @Override
    public void set(int key, double value, int flags) {
        this.set(JSType.toObject(key), JSType.toObject(value), flags);
    }

    @Override
    public void set(int key, Object value, int flags) {
        this.set(JSType.toObject(key), value, flags);
    }

    @Override
    public void set(Object key, int value, int flags) {
        this.set(key, JSType.toObject(value), flags);
    }

    @Override
    public void set(Object key, double value, int flags) {
        this.set(key, JSType.toObject(value), flags);
    }

    @Override
    public abstract void set(Object var1, Object var2, int var3);

    @Override
    public abstract boolean has(Object var1);

    @Override
    public boolean has(int key) {
        return this.has(JSType.toObject(key));
    }

    @Override
    public boolean has(double key) {
        return this.has(JSType.toObject(key));
    }

    @Override
    public boolean hasOwnProperty(int key) {
        return this.hasOwnProperty(JSType.toObject(key));
    }

    @Override
    public boolean hasOwnProperty(double key) {
        return this.hasOwnProperty(JSType.toObject(key));
    }

    @Override
    public abstract boolean hasOwnProperty(Object var1);

    @Override
    public boolean delete(int key, boolean strict) {
        return this.delete(JSType.toObject(key), strict);
    }

    @Override
    public boolean delete(double key, boolean strict) {
        return this.delete(JSType.toObject(key), strict);
    }
}

