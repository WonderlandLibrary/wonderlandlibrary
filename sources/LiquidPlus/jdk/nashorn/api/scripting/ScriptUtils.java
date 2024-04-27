/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.api.scripting;

import java.lang.invoke.MethodHandle;
import jdk.Exported;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.nashorn.api.scripting.Formatter;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.linker.Bootstrap;

@Exported
public final class ScriptUtils {
    private ScriptUtils() {
    }

    public static String parse(String code, String name, boolean includeLoc) {
        return ScriptRuntime.parse(code, name, includeLoc);
    }

    public static String format(String format, Object[] args2) {
        return Formatter.format(format, args2);
    }

    public static Object makeSynchronizedFunction(Object func, Object sync) {
        Object unwrapped = ScriptUtils.unwrap(func);
        if (unwrapped instanceof ScriptFunction) {
            return ((ScriptFunction)unwrapped).createSynchronized(ScriptUtils.unwrap(sync));
        }
        throw new IllegalArgumentException();
    }

    public static ScriptObjectMirror wrap(Object obj) {
        if (obj instanceof ScriptObjectMirror) {
            return (ScriptObjectMirror)obj;
        }
        if (obj instanceof ScriptObject) {
            ScriptObject sobj = (ScriptObject)obj;
            return (ScriptObjectMirror)ScriptObjectMirror.wrap(sobj, Context.getGlobal());
        }
        throw new IllegalArgumentException();
    }

    public static Object unwrap(Object obj) {
        if (obj instanceof ScriptObjectMirror) {
            return ScriptObjectMirror.unwrap(obj, Context.getGlobal());
        }
        return obj;
    }

    public static Object[] wrapArray(Object[] args2) {
        if (args2 == null || args2.length == 0) {
            return args2;
        }
        return ScriptObjectMirror.wrapArray(args2, Context.getGlobal());
    }

    public static Object[] unwrapArray(Object[] args2) {
        if (args2 == null || args2.length == 0) {
            return args2;
        }
        return ScriptObjectMirror.unwrapArray(args2, Context.getGlobal());
    }

    public static Object convert(Object obj, Object type) {
        Class<?> clazz;
        if (obj == null) {
            return null;
        }
        if (type instanceof Class) {
            clazz = (Class<?>)type;
        } else if (type instanceof StaticClass) {
            clazz = ((StaticClass)type).getRepresentedClass();
        } else {
            throw new IllegalArgumentException("type expected");
        }
        LinkerServices linker = Bootstrap.getLinkerServices();
        Object objToConvert = ScriptUtils.unwrap(obj);
        MethodHandle converter = linker.getTypeConverter(objToConvert.getClass(), clazz);
        if (converter == null) {
            throw new UnsupportedOperationException("conversion not supported");
        }
        try {
            return converter.invoke(objToConvert);
        }
        catch (Error | RuntimeException e) {
            throw e;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}

