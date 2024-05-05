/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class ArgumentSetter {
    public static final CompilerConstants.Call SET_ARGUMENT = CompilerConstants.staticCallNoLookup(ArgumentSetter.class, "setArgument", Void.TYPE, Object.class, ScriptObject.class, Integer.TYPE);
    public static final CompilerConstants.Call SET_ARRAY_ELEMENT = CompilerConstants.staticCallNoLookup(ArgumentSetter.class, "setArrayElement", Void.TYPE, Object.class, Object[].class, Integer.TYPE);

    private ArgumentSetter() {
    }

    public static void setArgument(Object value, ScriptObject arguments, int key) {
        arguments.setArgument(key, value);
    }

    public static void setArrayElement(Object value, Object[] arguments, int key) {
        arguments[key] = value;
    }
}

