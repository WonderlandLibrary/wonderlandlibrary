/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Queue;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.internal.dynalink.support.TypeUtilities;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ListAdapter;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.linker.JavaAdapterFactory;

public final class NativeJava {
    private static PropertyMap $nasgenmap$;

    private NativeJava() {
        throw new UnsupportedOperationException();
    }

    public static boolean isType(Object self, Object type) {
        return type instanceof StaticClass;
    }

    public static Object synchronizedFunc(Object self, Object func, Object obj) {
        if (func instanceof ScriptFunction) {
            return ((ScriptFunction)func).createSynchronized(obj);
        }
        throw ECMAErrors.typeError("not.a.function", ScriptRuntime.safeToString(func));
    }

    public static boolean isJavaMethod(Object self, Object obj) {
        return Bootstrap.isDynamicMethod(obj);
    }

    public static boolean isJavaFunction(Object self, Object obj) {
        return Bootstrap.isCallable(obj) && !(obj instanceof ScriptFunction);
    }

    public static boolean isJavaObject(Object self, Object obj) {
        return obj != null && !(obj instanceof ScriptObject);
    }

    public static boolean isScriptObject(Object self, Object obj) {
        return obj instanceof ScriptObject;
    }

    public static boolean isScriptFunction(Object self, Object obj) {
        return obj instanceof ScriptFunction;
    }

    public static Object type(Object self, Object objTypeName) throws ClassNotFoundException {
        return NativeJava.type(objTypeName);
    }

    private static StaticClass type(Object objTypeName) throws ClassNotFoundException {
        return StaticClass.forClass(NativeJava.type(JSType.toString(objTypeName)));
    }

    private static Class<?> type(String typeName) throws ClassNotFoundException {
        if (typeName.endsWith("[]")) {
            return NativeJava.arrayType(typeName);
        }
        return NativeJava.simpleType(typeName);
    }

    public static Object typeName(Object self, Object type) {
        if (type instanceof StaticClass) {
            return ((StaticClass)type).getRepresentedClass().getName();
        }
        if (type instanceof Class) {
            return ((Class)type).getName();
        }
        return ScriptRuntime.UNDEFINED;
    }

    public static Object to(Object self, Object obj, Object objType) throws ClassNotFoundException {
        Class targetClass;
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof ScriptObject) && !(obj instanceof JSObject)) {
            throw ECMAErrors.typeError("not.an.object", ScriptRuntime.safeToString(obj));
        }
        if (objType == ScriptRuntime.UNDEFINED) {
            targetClass = Object[].class;
        } else {
            StaticClass targetType = objType instanceof StaticClass ? (StaticClass)objType : NativeJava.type(objType);
            targetClass = targetType.getRepresentedClass();
        }
        if (targetClass.isArray()) {
            try {
                return JSType.toJavaArray(obj, targetClass.getComponentType());
            }
            catch (Exception exp) {
                throw ECMAErrors.typeError(exp, "java.array.conversion.failed", targetClass.getName());
            }
        }
        if (targetClass == List.class || targetClass == Deque.class || targetClass == Queue.class || targetClass == Collection.class) {
            return ListAdapter.create(obj);
        }
        throw ECMAErrors.typeError("unsupported.java.to.type", targetClass.getName());
    }

    public static NativeArray from(Object self, Object objArray) {
        if (objArray == null) {
            return null;
        }
        if (objArray instanceof Collection) {
            return new NativeArray(((Collection)objArray).toArray());
        }
        if (objArray instanceof Object[]) {
            return new NativeArray((Object[])((Object[])objArray).clone());
        }
        if (objArray instanceof int[]) {
            return new NativeArray((int[])((int[])objArray).clone());
        }
        if (objArray instanceof double[]) {
            return new NativeArray((double[])((double[])objArray).clone());
        }
        if (objArray instanceof long[]) {
            return new NativeArray((long[])((long[])objArray).clone());
        }
        if (objArray instanceof byte[]) {
            return new NativeArray(NativeJava.copyArray((byte[])objArray));
        }
        if (objArray instanceof short[]) {
            return new NativeArray(NativeJava.copyArray((short[])objArray));
        }
        if (objArray instanceof char[]) {
            return new NativeArray(NativeJava.copyArray((char[])objArray));
        }
        if (objArray instanceof float[]) {
            return new NativeArray(NativeJava.copyArray((float[])objArray));
        }
        if (objArray instanceof boolean[]) {
            return new NativeArray(NativeJava.copyArray((boolean[])objArray));
        }
        throw ECMAErrors.typeError("cant.convert.to.javascript.array", objArray.getClass().getName());
    }

    private static int[] copyArray(byte[] in) {
        int[] out = new int[in.length];
        for (int i = 0; i < in.length; ++i) {
            out[i] = in[i];
        }
        return out;
    }

    private static int[] copyArray(short[] in) {
        int[] out = new int[in.length];
        for (int i = 0; i < in.length; ++i) {
            out[i] = in[i];
        }
        return out;
    }

    private static int[] copyArray(char[] in) {
        int[] out = new int[in.length];
        for (int i = 0; i < in.length; ++i) {
            out[i] = in[i];
        }
        return out;
    }

    private static double[] copyArray(float[] in) {
        double[] out = new double[in.length];
        for (int i = 0; i < in.length; ++i) {
            out[i] = in[i];
        }
        return out;
    }

    private static Object[] copyArray(boolean[] in) {
        Object[] out = new Object[in.length];
        for (int i = 0; i < in.length; ++i) {
            out[i] = in[i];
        }
        return out;
    }

    private static Class<?> simpleType(String typeName) throws ClassNotFoundException {
        Class<?> primClass = TypeUtilities.getPrimitiveTypeByName(typeName);
        if (primClass != null) {
            return primClass;
        }
        Context ctx = Global.getThisContext();
        try {
            return ctx.findClass(typeName);
        }
        catch (ClassNotFoundException e) {
            StringBuilder nextName = new StringBuilder(typeName);
            int lastDot = nextName.length();
            while (true) {
                if ((lastDot = nextName.lastIndexOf(".", lastDot - 1)) == -1) {
                    throw e;
                }
                nextName.setCharAt(lastDot, '$');
                try {
                    return ctx.findClass(nextName.toString());
                }
                catch (ClassNotFoundException classNotFoundException) {
                    continue;
                }
                break;
            }
        }
    }

    private static Class<?> arrayType(String typeName) throws ClassNotFoundException {
        return Array.newInstance(NativeJava.type(typeName.substring(0, typeName.length() - 2)), 0).getClass();
    }

    public static Object extend(Object self, Object ... types) {
        int typesLen;
        ScriptObject classOverrides;
        if (types == null || types.length == 0) {
            throw ECMAErrors.typeError("extend.expects.at.least.one.argument", new String[0]);
        }
        int l = types.length;
        if (types[l - 1] instanceof ScriptObject) {
            classOverrides = (ScriptObject)types[l - 1];
            typesLen = l - 1;
            if (typesLen == 0) {
                throw ECMAErrors.typeError("extend.expects.at.least.one.type.argument", new String[0]);
            }
        } else {
            classOverrides = null;
            typesLen = l;
        }
        Class[] stypes = new Class[typesLen];
        try {
            for (int i = 0; i < typesLen; ++i) {
                stypes[i] = ((StaticClass)types[i]).getRepresentedClass();
            }
        }
        catch (ClassCastException e) {
            throw ECMAErrors.typeError("extend.expects.java.types", new String[0]);
        }
        MethodHandles.Lookup lookup = self instanceof MethodHandles.Lookup ? (MethodHandles.Lookup)self : MethodHandles.publicLookup();
        return JavaAdapterFactory.getAdapterClassFor(stypes, classOverrides, lookup);
    }

    public static Object _super(Object self, Object adapter) {
        return Bootstrap.createSuperAdapter(adapter);
    }

    public static Object asJSONCompatible(Object self, Object obj) {
        return ScriptObjectMirror.wrapAsJSONCompatible(obj, Context.getGlobal());
    }

    static {
        NativeJava.$clinit$();
    }

    public static void $clinit$() {
        $nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
}

