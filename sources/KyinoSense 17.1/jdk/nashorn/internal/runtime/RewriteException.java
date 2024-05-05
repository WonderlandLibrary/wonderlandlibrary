/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.util.Arrays;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.lookup.MethodHandleFactory;
import jdk.nashorn.internal.lookup.MethodHandleFunctionality;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;

public final class RewriteException
extends Exception {
    private static final MethodHandleFunctionality MH = MethodHandleFactory.getFunctionality();
    private ScriptObject runtimeScope;
    private Object[] byteCodeSlots;
    private final int[] previousContinuationEntryPoints;
    public static final CompilerConstants.Call GET_BYTECODE_SLOTS = CompilerConstants.virtualCallNoLookup(RewriteException.class, "getByteCodeSlots", Object[].class, new Class[0]);
    public static final CompilerConstants.Call GET_PROGRAM_POINT = CompilerConstants.virtualCallNoLookup(RewriteException.class, "getProgramPoint", Integer.TYPE, new Class[0]);
    public static final CompilerConstants.Call GET_RETURN_VALUE = CompilerConstants.virtualCallNoLookup(RewriteException.class, "getReturnValueDestructive", Object.class, new Class[0]);
    public static final CompilerConstants.Call BOOTSTRAP = CompilerConstants.staticCallNoLookup(RewriteException.class, "populateArrayBootstrap", CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, Integer.TYPE);
    private static final CompilerConstants.Call POPULATE_ARRAY = CompilerConstants.staticCall(MethodHandles.lookup(), RewriteException.class, "populateArray", Object[].class, Object[].class, Integer.TYPE, Object[].class);
    public static final CompilerConstants.Call TO_LONG_ARRAY = CompilerConstants.staticCallNoLookup(RewriteException.class, "toLongArray", long[].class, Object.class, RewriteException.class);
    public static final CompilerConstants.Call TO_DOUBLE_ARRAY = CompilerConstants.staticCallNoLookup(RewriteException.class, "toDoubleArray", double[].class, Object.class, RewriteException.class);
    public static final CompilerConstants.Call TO_OBJECT_ARRAY = CompilerConstants.staticCallNoLookup(RewriteException.class, "toObjectArray", Object[].class, Object.class, RewriteException.class);
    public static final CompilerConstants.Call INSTANCE_OR_NULL = CompilerConstants.staticCallNoLookup(RewriteException.class, "instanceOrNull", Object.class, Object.class, Class.class);
    public static final CompilerConstants.Call ASSERT_ARRAY_LENGTH = CompilerConstants.staticCallNoLookup(RewriteException.class, "assertArrayLength", Void.TYPE, Object[].class, Integer.TYPE);

    private RewriteException(UnwarrantedOptimismException e, Object[] byteCodeSlots, String[] byteCodeSymbolNames, int[] previousContinuationEntryPoints) {
        super("", e, false, Context.DEBUG);
        this.byteCodeSlots = byteCodeSlots;
        this.runtimeScope = RewriteException.mergeSlotsWithScope(byteCodeSlots, byteCodeSymbolNames);
        this.previousContinuationEntryPoints = previousContinuationEntryPoints;
    }

    public static RewriteException create(UnwarrantedOptimismException e, Object[] byteCodeSlots, String[] byteCodeSymbolNames) {
        return RewriteException.create(e, byteCodeSlots, byteCodeSymbolNames, null);
    }

    public static RewriteException create(UnwarrantedOptimismException e, Object[] byteCodeSlots, String[] byteCodeSymbolNames, int[] previousContinuationEntryPoints) {
        return new RewriteException(e, byteCodeSlots, byteCodeSymbolNames, previousContinuationEntryPoints);
    }

    public static CallSite populateArrayBootstrap(MethodHandles.Lookup lookup, String name, MethodType type, int startIndex) {
        MethodHandle mh = POPULATE_ARRAY.methodHandle();
        mh = MH.insertArguments(mh, 1, startIndex);
        mh = MH.asCollector(mh, Object[].class, type.parameterCount() - 1);
        mh = MH.asType(mh, type);
        return new ConstantCallSite(mh);
    }

    private static ScriptObject mergeSlotsWithScope(Object[] byteCodeSlots, String[] byteCodeSymbolNames) {
        ScriptObject locals = Global.newEmptyInstance();
        int l = Math.min(byteCodeSlots.length, byteCodeSymbolNames.length);
        ScriptObject runtimeScope = null;
        String scopeName = CompilerConstants.SCOPE.symbolName();
        for (int i = 0; i < l; ++i) {
            String name = byteCodeSymbolNames[i];
            Object value = byteCodeSlots[i];
            if (scopeName.equals(name)) {
                assert (runtimeScope == null);
                runtimeScope = (ScriptObject)value;
                continue;
            }
            if (name == null) continue;
            locals.set((Object)name, value, 2);
        }
        locals.setProto(runtimeScope);
        return locals;
    }

    public static Object[] populateArray(Object[] arrayToBePopluated, int startIndex, Object[] items) {
        System.arraycopy(items, 0, arrayToBePopluated, startIndex, items.length);
        return arrayToBePopluated;
    }

    public static long[] toLongArray(Object obj, RewriteException e) {
        if (obj instanceof long[]) {
            return (long[])obj;
        }
        assert (obj instanceof int[]);
        int[] in = (int[])obj;
        long[] out = new long[in.length];
        for (int i = 0; i < in.length; ++i) {
            out[i] = in[i];
        }
        return e.replaceByteCodeValue(in, out);
    }

    public static double[] toDoubleArray(Object obj, RewriteException e) {
        if (obj instanceof double[]) {
            return (double[])obj;
        }
        assert (obj instanceof int[] || obj instanceof long[]);
        int l = Array.getLength(obj);
        double[] out = new double[l];
        for (int i = 0; i < l; ++i) {
            out[i] = Array.getDouble(obj, i);
        }
        return e.replaceByteCodeValue(obj, out);
    }

    public static Object[] toObjectArray(Object obj, RewriteException e) {
        if (obj instanceof Object[]) {
            return (Object[])obj;
        }
        assert (obj instanceof int[] || obj instanceof long[] || obj instanceof double[]) : obj + " is " + obj.getClass().getName();
        int l = Array.getLength(obj);
        Object[] out = new Object[l];
        for (int i = 0; i < l; ++i) {
            out[i] = Array.get(obj, i);
        }
        return e.replaceByteCodeValue(obj, out);
    }

    public static Object instanceOrNull(Object obj, Class<?> clazz) {
        return clazz.isInstance(obj) ? obj : null;
    }

    public static void assertArrayLength(Object[] arr, int length) {
        int i = arr.length;
        while (i-- > length) {
            if (arr[i] != ScriptRuntime.UNDEFINED) {
                throw new AssertionError((Object)String.format("Expected array length %d, but it is %d", length, i + 1));
            }
        }
    }

    private <T> T replaceByteCodeValue(Object in, T out) {
        for (int i = 0; i < this.byteCodeSlots.length; ++i) {
            if (this.byteCodeSlots[i] != in) continue;
            this.byteCodeSlots[i] = out;
        }
        return out;
    }

    private UnwarrantedOptimismException getUOE() {
        return (UnwarrantedOptimismException)this.getCause();
    }

    public Object getReturnValueDestructive() {
        assert (this.byteCodeSlots != null);
        this.byteCodeSlots = null;
        this.runtimeScope = null;
        return this.getUOE().getReturnValueDestructive();
    }

    Object getReturnValueNonDestructive() {
        return this.getUOE().getReturnValueNonDestructive();
    }

    public Type getReturnType() {
        return this.getUOE().getReturnType();
    }

    public int getProgramPoint() {
        return this.getUOE().getProgramPoint();
    }

    public Object[] getByteCodeSlots() {
        return this.byteCodeSlots == null ? null : (Object[])this.byteCodeSlots.clone();
    }

    public int[] getPreviousContinuationEntryPoints() {
        return this.previousContinuationEntryPoints == null ? null : (int[])this.previousContinuationEntryPoints.clone();
    }

    public ScriptObject getRuntimeScope() {
        return this.runtimeScope;
    }

    private static String stringify(Object returnValue) {
        if (returnValue == null) {
            return "null";
        }
        String str = returnValue.toString();
        if (returnValue instanceof String) {
            str = '\'' + str + '\'';
        } else if (returnValue instanceof Double) {
            str = str + 'd';
        } else if (returnValue instanceof Long) {
            str = str + 'l';
        }
        return str;
    }

    @Override
    public String getMessage() {
        return this.getMessage(false);
    }

    public String getMessageShort() {
        return this.getMessage(true);
    }

    private String getMessage(boolean isShort) {
        Object[] slots;
        StringBuilder sb = new StringBuilder();
        sb.append("[pp=").append(this.getProgramPoint()).append(", ");
        if (!isShort && (slots = this.byteCodeSlots) != null) {
            sb.append("slots=").append(Arrays.asList(slots)).append(", ");
        }
        sb.append("type=").append(this.getReturnType()).append(", ");
        sb.append("value=").append(RewriteException.stringify(this.getReturnValueNonDestructive())).append(")]");
        return sb.toString();
    }

    private void writeObject(ObjectOutputStream out) throws NotSerializableException {
        throw new NotSerializableException(this.getClass().getName());
    }

    private void readObject(ObjectInputStream in) throws NotSerializableException {
        throw new NotSerializableException(this.getClass().getName());
    }
}

