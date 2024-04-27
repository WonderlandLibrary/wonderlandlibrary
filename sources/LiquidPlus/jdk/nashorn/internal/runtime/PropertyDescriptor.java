/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;

public interface PropertyDescriptor {
    public static final int GENERIC = 0;
    public static final int DATA = 1;
    public static final int ACCESSOR = 2;
    public static final String CONFIGURABLE = "configurable";
    public static final String ENUMERABLE = "enumerable";
    public static final String WRITABLE = "writable";
    public static final String VALUE = "value";
    public static final String GET = "get";
    public static final String SET = "set";

    public boolean isConfigurable();

    public boolean isEnumerable();

    public boolean isWritable();

    public Object getValue();

    public ScriptFunction getGetter();

    public ScriptFunction getSetter();

    public void setConfigurable(boolean var1);

    public void setEnumerable(boolean var1);

    public void setWritable(boolean var1);

    public void setValue(Object var1);

    public void setGetter(Object var1);

    public void setSetter(Object var1);

    public PropertyDescriptor fillFrom(ScriptObject var1);

    public int type();

    public boolean has(Object var1);

    public boolean hasAndEquals(PropertyDescriptor var1);
}

