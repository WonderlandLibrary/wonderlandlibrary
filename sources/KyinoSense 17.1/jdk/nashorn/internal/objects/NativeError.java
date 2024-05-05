/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import jdk.nashorn.api.scripting.NashornException;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.ECMAException;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;

public final class NativeError
extends ScriptObject {
    static final MethodHandle GET_COLUMNNUMBER = NativeError.findOwnMH("getColumnNumber", Object.class, Object.class);
    static final MethodHandle SET_COLUMNNUMBER = NativeError.findOwnMH("setColumnNumber", Object.class, Object.class, Object.class);
    static final MethodHandle GET_LINENUMBER = NativeError.findOwnMH("getLineNumber", Object.class, Object.class);
    static final MethodHandle SET_LINENUMBER = NativeError.findOwnMH("setLineNumber", Object.class, Object.class, Object.class);
    static final MethodHandle GET_FILENAME = NativeError.findOwnMH("getFileName", Object.class, Object.class);
    static final MethodHandle SET_FILENAME = NativeError.findOwnMH("setFileName", Object.class, Object.class, Object.class);
    static final MethodHandle GET_STACK = NativeError.findOwnMH("getStack", Object.class, Object.class);
    static final MethodHandle SET_STACK = NativeError.findOwnMH("setStack", Object.class, Object.class, Object.class);
    static final String MESSAGE = "message";
    static final String NAME = "name";
    static final String STACK = "__stack__";
    static final String LINENUMBER = "__lineNumber__";
    static final String COLUMNNUMBER = "__columnNumber__";
    static final String FILENAME = "__fileName__";
    public Object instMessage;
    public Object nashornException;
    private static PropertyMap $nasgenmap$;

    private NativeError(Object msg, ScriptObject proto, PropertyMap map) {
        super(proto, map);
        if (msg != ScriptRuntime.UNDEFINED) {
            this.instMessage = JSType.toString(msg);
        } else {
            this.delete(MESSAGE, false);
        }
        NativeError.initException(this);
    }

    NativeError(Object msg, Global global) {
        this(msg, global.getErrorPrototype(), $nasgenmap$);
    }

    private NativeError(Object msg) {
        this(msg, Global.instance());
    }

    @Override
    public String getClassName() {
        return "Error";
    }

    public static NativeError constructor(boolean newObj, Object self, Object msg) {
        return new NativeError(msg);
    }

    static void initException(ScriptObject self) {
        new ECMAException(self, null);
    }

    public static Object captureStackTrace(Object self, Object errorObj) {
        ScriptObject sobj = Global.checkObject(errorObj);
        NativeError.initException(sobj);
        sobj.delete(STACK, false);
        if (!sobj.has("stack")) {
            ScriptFunction getStack = ScriptFunction.createBuiltin("getStack", GET_STACK);
            ScriptFunction setStack = ScriptFunction.createBuiltin("setStack", SET_STACK);
            sobj.addOwnProperty("stack", 2, getStack, setStack);
        }
        return ScriptRuntime.UNDEFINED;
    }

    public static Object dumpStack(Object self) {
        Thread.dumpStack();
        return ScriptRuntime.UNDEFINED;
    }

    public static Object printStackTrace(Object self) {
        return ECMAException.printStackTrace(Global.checkObject(self));
    }

    public static Object getStackTrace(Object self) {
        ScriptObject sobj = Global.checkObject(self);
        Object exception = ECMAException.getException(sobj);
        Object[] res = exception instanceof Throwable ? NashornException.getScriptFrames((Throwable)exception) : ScriptRuntime.EMPTY_ARRAY;
        return new NativeArray(res);
    }

    public static Object getLineNumber(Object self) {
        ScriptObject sobj = Global.checkObject(self);
        return sobj.has(LINENUMBER) ? sobj.get(LINENUMBER) : ECMAException.getLineNumber(sobj);
    }

    public static Object setLineNumber(Object self, Object value) {
        ScriptObject sobj = Global.checkObject(self);
        if (sobj.hasOwnProperty(LINENUMBER)) {
            sobj.put(LINENUMBER, value, false);
        } else {
            sobj.addOwnProperty(LINENUMBER, 2, value);
        }
        return value;
    }

    public static Object getColumnNumber(Object self) {
        ScriptObject sobj = Global.checkObject(self);
        return sobj.has(COLUMNNUMBER) ? sobj.get(COLUMNNUMBER) : ECMAException.getColumnNumber((ScriptObject)self);
    }

    public static Object setColumnNumber(Object self, Object value) {
        ScriptObject sobj = Global.checkObject(self);
        if (sobj.hasOwnProperty(COLUMNNUMBER)) {
            sobj.put(COLUMNNUMBER, value, false);
        } else {
            sobj.addOwnProperty(COLUMNNUMBER, 2, value);
        }
        return value;
    }

    public static Object getFileName(Object self) {
        ScriptObject sobj = Global.checkObject(self);
        return sobj.has(FILENAME) ? sobj.get(FILENAME) : ECMAException.getFileName((ScriptObject)self);
    }

    public static Object setFileName(Object self, Object value) {
        ScriptObject sobj = Global.checkObject(self);
        if (sobj.hasOwnProperty(FILENAME)) {
            sobj.put(FILENAME, value, false);
        } else {
            sobj.addOwnProperty(FILENAME, 2, value);
        }
        return value;
    }

    public static Object getStack(Object self) {
        ScriptObject sobj = Global.checkObject(self);
        if (sobj.has(STACK)) {
            return sobj.get(STACK);
        }
        Object exception = ECMAException.getException(sobj);
        if (exception instanceof Throwable) {
            String value = NativeError.getScriptStackString(sobj, (Throwable)exception);
            if (sobj.hasOwnProperty(STACK)) {
                sobj.put(STACK, value, false);
            } else {
                sobj.addOwnProperty(STACK, 2, value);
            }
            return value;
        }
        return ScriptRuntime.UNDEFINED;
    }

    public static Object setStack(Object self, Object value) {
        ScriptObject sobj = Global.checkObject(self);
        if (sobj.hasOwnProperty(STACK)) {
            sobj.put(STACK, value, false);
        } else {
            sobj.addOwnProperty(STACK, 2, value);
        }
        return value;
    }

    public static Object toString(Object self) {
        ScriptObject sobj = Global.checkObject(self);
        Object name = sobj.get(NAME);
        name = name == ScriptRuntime.UNDEFINED ? "Error" : JSType.toString(name);
        Object msg = sobj.get(MESSAGE);
        msg = msg == ScriptRuntime.UNDEFINED ? "" : JSType.toString(msg);
        if (((String)name).isEmpty()) {
            return msg;
        }
        if (((String)msg).isEmpty()) {
            return name;
        }
        return name + ": " + msg;
    }

    private static MethodHandle findOwnMH(String name, Class<?> rtype, Class<?> ... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), NativeError.class, name, Lookup.MH.type(rtype, types));
    }

    private static String getScriptStackString(ScriptObject sobj, Throwable exp) {
        return JSType.toString(sobj) + "\n" + NashornException.getScriptStackString(exp);
    }

    static {
        NativeError.$clinit$();
    }

    public static void $clinit$() {
        ArrayList<Property> arrayList = new ArrayList<Property>(2);
        arrayList.add(AccessorProperty.create(MESSAGE, 2, cfr_ldc_0(), cfr_ldc_1()));
        arrayList.add(AccessorProperty.create("nashornException", 2, cfr_ldc_2(), cfr_ldc_3()));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    public Object G$instMessage() {
        return this.instMessage;
    }

    public void S$instMessage(Object object) {
        this.instMessage = object;
    }

    public Object G$nashornException() {
        return this.nashornException;
    }

    public void S$nashornException(Object object) {
        this.nashornException = object;
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(NativeError.class, "G$instMessage", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeError.class, "S$instMessage", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(NativeError.class, "G$nashornException", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeError.class, "S$nashornException", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

