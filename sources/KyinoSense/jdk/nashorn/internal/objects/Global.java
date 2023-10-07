/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.SwitchPoint;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.nashorn.api.scripting.ClassFilter;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.objects.AccessorPropertyDescriptor;
import jdk.nashorn.internal.objects.DataPropertyDescriptor;
import jdk.nashorn.internal.objects.GenericPropertyDescriptor;
import jdk.nashorn.internal.objects.NativeArguments;
import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.objects.NativeBoolean;
import jdk.nashorn.internal.objects.NativeDate;
import jdk.nashorn.internal.objects.NativeError;
import jdk.nashorn.internal.objects.NativeEvalError;
import jdk.nashorn.internal.objects.NativeNumber;
import jdk.nashorn.internal.objects.NativeObject;
import jdk.nashorn.internal.objects.NativeRangeError;
import jdk.nashorn.internal.objects.NativeReferenceError;
import jdk.nashorn.internal.objects.NativeRegExp;
import jdk.nashorn.internal.objects.NativeString;
import jdk.nashorn.internal.objects.NativeSyntaxError;
import jdk.nashorn.internal.objects.NativeTypeError;
import jdk.nashorn.internal.objects.NativeURIError;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.FindProperty;
import jdk.nashorn.internal.runtime.GlobalConstants;
import jdk.nashorn.internal.runtime.GlobalFunctions;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.NativeJavaPackage;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyDescriptor;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.Scope;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.ScriptingFunctions;
import jdk.nashorn.internal.runtime.Specialization;
import jdk.nashorn.internal.runtime.Undefined;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.linker.InvokeByName;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;
import jdk.nashorn.internal.runtime.regexp.RegExpResult;
import jdk.nashorn.internal.scripts.JD;
import jdk.nashorn.internal.scripts.JO;
import jdk.nashorn.tools.ShellFunctions;

public final class Global
extends Scope {
    private static final Object LAZY_SENTINEL = new Object();
    private static final Object LOCATION_PLACEHOLDER = new Object();
    private final InvokeByName TO_STRING = new InvokeByName("toString", ScriptObject.class);
    private final InvokeByName VALUE_OF = new InvokeByName("valueOf", ScriptObject.class);
    public Object arguments;
    public Object parseInt;
    public Object parseFloat;
    public Object isNaN;
    public Object isFinite;
    public Object encodeURI;
    public Object encodeURIComponent;
    public Object decodeURI;
    public Object decodeURIComponent;
    public Object escape;
    public Object unescape;
    public Object print;
    public Object load;
    public Object loadWithNewGlobal;
    public Object exit;
    public Object quit;
    public static final double NaN = Double.NaN;
    public static final double Infinity = Double.POSITIVE_INFINITY;
    public static final Object undefined = ScriptRuntime.UNDEFINED;
    public Object eval;
    public volatile Object object;
    public volatile Object function;
    public volatile Object array;
    public volatile Object string;
    public volatile Object _boolean;
    public volatile Object number;
    private volatile Object date = LAZY_SENTINEL;
    private volatile Object regexp = LAZY_SENTINEL;
    private volatile Object json = LAZY_SENTINEL;
    private volatile Object jsadapter = LAZY_SENTINEL;
    public volatile Object math;
    public volatile Object error;
    private volatile Object evalError = LAZY_SENTINEL;
    private volatile Object rangeError = LAZY_SENTINEL;
    public volatile Object referenceError;
    public volatile Object syntaxError;
    public volatile Object typeError;
    private volatile Object uriError = LAZY_SENTINEL;
    private volatile Object arrayBuffer;
    private volatile Object dataView;
    private volatile Object int8Array;
    private volatile Object uint8Array;
    private volatile Object uint8ClampedArray;
    private volatile Object int16Array;
    private volatile Object uint16Array;
    private volatile Object int32Array;
    private volatile Object uint32Array;
    private volatile Object float32Array;
    private volatile Object float64Array;
    public volatile Object packages;
    public volatile Object com;
    public volatile Object edu;
    public volatile Object java;
    public volatile Object javafx;
    public volatile Object javax;
    public volatile Object org;
    private volatile Object javaImporter;
    private volatile Object javaApi;
    public static final Object __FILE__ = LOCATION_PLACEHOLDER;
    public static final Object __DIR__ = LOCATION_PLACEHOLDER;
    public static final Object __LINE__ = LOCATION_PLACEHOLDER;
    private volatile NativeDate DEFAULT_DATE;
    private volatile NativeRegExp DEFAULT_REGEXP;
    private ScriptFunction builtinFunction;
    private ScriptFunction builtinObject;
    private ScriptFunction builtinArray;
    private ScriptFunction builtinBoolean;
    private ScriptFunction builtinDate;
    private ScriptObject builtinJSON;
    private ScriptFunction builtinJSAdapter;
    private ScriptObject builtinMath;
    private ScriptFunction builtinNumber;
    private ScriptFunction builtinRegExp;
    private ScriptFunction builtinString;
    private ScriptFunction builtinError;
    private ScriptFunction builtinEval;
    private ScriptFunction builtinEvalError;
    private ScriptFunction builtinRangeError;
    private ScriptFunction builtinReferenceError;
    private ScriptFunction builtinSyntaxError;
    private ScriptFunction builtinTypeError;
    private ScriptFunction builtinURIError;
    private ScriptObject builtinPackages;
    private ScriptObject builtinCom;
    private ScriptObject builtinEdu;
    private ScriptObject builtinJava;
    private ScriptObject builtinJavafx;
    private ScriptObject builtinJavax;
    private ScriptObject builtinOrg;
    private ScriptFunction builtinJavaImporter;
    private ScriptObject builtinJavaApi;
    private ScriptFunction builtinArrayBuffer;
    private ScriptFunction builtinDataView;
    private ScriptFunction builtinInt8Array;
    private ScriptFunction builtinUint8Array;
    private ScriptFunction builtinUint8ClampedArray;
    private ScriptFunction builtinInt16Array;
    private ScriptFunction builtinUint16Array;
    private ScriptFunction builtinInt32Array;
    private ScriptFunction builtinUint32Array;
    private ScriptFunction builtinFloat32Array;
    private ScriptFunction builtinFloat64Array;
    private ScriptFunction typeErrorThrower;
    private RegExpResult lastRegExpResult;
    private static final MethodHandle EVAL = Global.findOwnMH_S("eval", Object.class, Object.class, Object.class);
    private static final MethodHandle NO_SUCH_PROPERTY = Global.findOwnMH_S("__noSuchProperty__", Object.class, Object.class, Object.class);
    private static final MethodHandle PRINT = Global.findOwnMH_S("print", Object.class, Object.class, Object[].class);
    private static final MethodHandle PRINTLN = Global.findOwnMH_S("println", Object.class, Object.class, Object[].class);
    private static final MethodHandle LOAD = Global.findOwnMH_S("load", Object.class, Object.class, Object.class);
    private static final MethodHandle LOAD_WITH_NEW_GLOBAL = Global.findOwnMH_S("loadWithNewGlobal", Object.class, Object.class, Object[].class);
    private static final MethodHandle EXIT = Global.findOwnMH_S("exit", Object.class, Object.class, Object.class);
    private static final MethodHandle LEXICAL_SCOPE_FILTER = Global.findOwnMH_S("lexicalScopeFilter", Object.class, Object.class);
    private static PropertyMap $nasgenmap$;
    private final Context context;
    private ThreadLocal<ScriptContext> scontext;
    private ScriptEngine engine;
    private volatile ScriptContext initscontext;
    private final LexicalScope lexicalScope;
    private SwitchPoint lexicalScopeSwitchPoint;
    private final Map<Object, InvokeByName> namedInvokers = new ConcurrentHashMap<Object, InvokeByName>();
    private final Map<Object, MethodHandle> dynamicInvokers = new ConcurrentHashMap<Object, MethodHandle>();

    public static Object getDate(Object self) {
        Global global = Global.instanceFrom(self);
        if (global.date == LAZY_SENTINEL) {
            global.date = global.getBuiltinDate();
        }
        return global.date;
    }

    public static void setDate(Object self, Object value) {
        Global global = Global.instanceFrom(self);
        global.date = value;
    }

    public static Object getRegExp(Object self) {
        Global global = Global.instanceFrom(self);
        if (global.regexp == LAZY_SENTINEL) {
            global.regexp = global.getBuiltinRegExp();
        }
        return global.regexp;
    }

    public static void setRegExp(Object self, Object value) {
        Global global = Global.instanceFrom(self);
        global.regexp = value;
    }

    public static Object getJSON(Object self) {
        Global global = Global.instanceFrom(self);
        if (global.json == LAZY_SENTINEL) {
            global.json = global.getBuiltinJSON();
        }
        return global.json;
    }

    public static void setJSON(Object self, Object value) {
        Global global = Global.instanceFrom(self);
        global.json = value;
    }

    public static Object getJSAdapter(Object self) {
        Global global = Global.instanceFrom(self);
        if (global.jsadapter == LAZY_SENTINEL) {
            global.jsadapter = global.getBuiltinJSAdapter();
        }
        return global.jsadapter;
    }

    public static void setJSAdapter(Object self, Object value) {
        Global global = Global.instanceFrom(self);
        global.jsadapter = value;
    }

    public static Object getEvalError(Object self) {
        Global global = Global.instanceFrom(self);
        if (global.evalError == LAZY_SENTINEL) {
            global.evalError = global.getBuiltinEvalError();
        }
        return global.evalError;
    }

    public static void setEvalError(Object self, Object value) {
        Global global = Global.instanceFrom(self);
        global.evalError = value;
    }

    public static Object getRangeError(Object self) {
        Global global = Global.instanceFrom(self);
        if (global.rangeError == LAZY_SENTINEL) {
            global.rangeError = global.getBuiltinRangeError();
        }
        return global.rangeError;
    }

    public static void setRangeError(Object self, Object value) {
        Global global = Global.instanceFrom(self);
        global.rangeError = value;
    }

    public static Object getURIError(Object self) {
        Global global = Global.instanceFrom(self);
        if (global.uriError == LAZY_SENTINEL) {
            global.uriError = global.getBuiltinURIError();
        }
        return global.uriError;
    }

    public static void setURIError(Object self, Object value) {
        Global global = Global.instanceFrom(self);
        global.uriError = value;
    }

    public static Object getArrayBuffer(Object self) {
        Global global = Global.instanceFrom(self);
        if (global.arrayBuffer == LAZY_SENTINEL) {
            global.arrayBuffer = global.getBuiltinArrayBuffer();
        }
        return global.arrayBuffer;
    }

    public static void setArrayBuffer(Object self, Object value) {
        Global global = Global.instanceFrom(self);
        global.arrayBuffer = value;
    }

    public static Object getDataView(Object self) {
        Global global = Global.instanceFrom(self);
        if (global.dataView == LAZY_SENTINEL) {
            global.dataView = global.getBuiltinDataView();
        }
        return global.dataView;
    }

    public static void setDataView(Object self, Object value) {
        Global global = Global.instanceFrom(self);
        global.dataView = value;
    }

    public static Object getInt8Array(Object self) {
        Global global = Global.instanceFrom(self);
        if (global.int8Array == LAZY_SENTINEL) {
            global.int8Array = global.getBuiltinInt8Array();
        }
        return global.int8Array;
    }

    public static void setInt8Array(Object self, Object value) {
        Global global = Global.instanceFrom(self);
        global.int8Array = value;
    }

    public static Object getUint8Array(Object self) {
        Global global = Global.instanceFrom(self);
        if (global.uint8Array == LAZY_SENTINEL) {
            global.uint8Array = global.getBuiltinUint8Array();
        }
        return global.uint8Array;
    }

    public static void setUint8Array(Object self, Object value) {
        Global global = Global.instanceFrom(self);
        global.uint8Array = value;
    }

    public static Object getUint8ClampedArray(Object self) {
        Global global = Global.instanceFrom(self);
        if (global.uint8ClampedArray == LAZY_SENTINEL) {
            global.uint8ClampedArray = global.getBuiltinUint8ClampedArray();
        }
        return global.uint8ClampedArray;
    }

    public static void setUint8ClampedArray(Object self, Object value) {
        Global global = Global.instanceFrom(self);
        global.uint8ClampedArray = value;
    }

    public static Object getInt16Array(Object self) {
        Global global = Global.instanceFrom(self);
        if (global.int16Array == LAZY_SENTINEL) {
            global.int16Array = global.getBuiltinInt16Array();
        }
        return global.int16Array;
    }

    public static void setInt16Array(Object self, Object value) {
        Global global = Global.instanceFrom(self);
        global.int16Array = value;
    }

    public static Object getUint16Array(Object self) {
        Global global = Global.instanceFrom(self);
        if (global.uint16Array == LAZY_SENTINEL) {
            global.uint16Array = global.getBuiltinUint16Array();
        }
        return global.uint16Array;
    }

    public static void setUint16Array(Object self, Object value) {
        Global global = Global.instanceFrom(self);
        global.uint16Array = value;
    }

    public static Object getInt32Array(Object self) {
        Global global = Global.instanceFrom(self);
        if (global.int32Array == LAZY_SENTINEL) {
            global.int32Array = global.getBuiltinInt32Array();
        }
        return global.int32Array;
    }

    public static void setInt32Array(Object self, Object value) {
        Global global = Global.instanceFrom(self);
        global.int32Array = value;
    }

    public static Object getUint32Array(Object self) {
        Global global = Global.instanceFrom(self);
        if (global.uint32Array == LAZY_SENTINEL) {
            global.uint32Array = global.getBuiltinUint32Array();
        }
        return global.uint32Array;
    }

    public static void setUint32Array(Object self, Object value) {
        Global global = Global.instanceFrom(self);
        global.uint32Array = value;
    }

    public static Object getFloat32Array(Object self) {
        Global global = Global.instanceFrom(self);
        if (global.float32Array == LAZY_SENTINEL) {
            global.float32Array = global.getBuiltinFloat32Array();
        }
        return global.float32Array;
    }

    public static void setFloat32Array(Object self, Object value) {
        Global global = Global.instanceFrom(self);
        global.float32Array = value;
    }

    public static Object getFloat64Array(Object self) {
        Global global = Global.instanceFrom(self);
        if (global.float64Array == LAZY_SENTINEL) {
            global.float64Array = global.getBuiltinFloat64Array();
        }
        return global.float64Array;
    }

    public static void setFloat64Array(Object self, Object value) {
        Global global = Global.instanceFrom(self);
        global.float64Array = value;
    }

    public static Object getJavaImporter(Object self) {
        Global global = Global.instanceFrom(self);
        if (global.javaImporter == LAZY_SENTINEL) {
            global.javaImporter = global.getBuiltinJavaImporter();
        }
        return global.javaImporter;
    }

    public static void setJavaImporter(Object self, Object value) {
        Global global = Global.instanceFrom(self);
        global.javaImporter = value;
    }

    public static Object getJavaApi(Object self) {
        Global global = Global.instanceFrom(self);
        if (global.javaApi == LAZY_SENTINEL) {
            global.javaApi = global.getBuiltinJavaApi();
        }
        return global.javaApi;
    }

    public static void setJavaApi(Object self, Object value) {
        Global global = Global.instanceFrom(self);
        global.javaApi = value;
    }

    NativeDate getDefaultDate() {
        return this.DEFAULT_DATE;
    }

    NativeRegExp getDefaultRegExp() {
        return this.DEFAULT_REGEXP;
    }

    public void setScriptContext(ScriptContext ctxt) {
        assert (this.scontext != null);
        this.scontext.set(ctxt);
    }

    public ScriptContext getScriptContext() {
        assert (this.scontext != null);
        return this.scontext.get();
    }

    public void setInitScriptContext(ScriptContext ctxt) {
        this.initscontext = ctxt;
    }

    private ScriptContext currentContext() {
        ScriptContext sc;
        ScriptContext scriptContext = sc = this.scontext != null ? this.scontext.get() : null;
        if (sc != null) {
            return sc;
        }
        if (this.initscontext != null) {
            return this.initscontext;
        }
        return this.engine != null ? this.engine.getContext() : null;
    }

    @Override
    protected Context getContext() {
        return this.context;
    }

    @Override
    protected boolean useDualFields() {
        return this.context.useDualFields();
    }

    private static PropertyMap checkAndGetMap(Context context) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new RuntimePermission("nashorn.createGlobal"));
        }
        Objects.requireNonNull(context);
        return $nasgenmap$;
    }

    public Global(Context context) {
        super(Global.checkAndGetMap(context));
        this.context = context;
        this.lexicalScope = context.getEnv()._es6 ? new LexicalScope(this) : null;
    }

    public static Global instance() {
        return Objects.requireNonNull(Context.getGlobal());
    }

    private static Global instanceFrom(Object self) {
        return self instanceof Global ? (Global)self : Global.instance();
    }

    public static boolean hasInstance() {
        return Context.getGlobal() != null;
    }

    static ScriptEnvironment getEnv() {
        return Global.instance().getContext().getEnv();
    }

    static Context getThisContext() {
        return Global.instance().getContext();
    }

    public ClassFilter getClassFilter() {
        return this.context.getClassFilter();
    }

    public boolean isOfContext(Context ctxt) {
        return this.context == ctxt;
    }

    public boolean isStrictContext() {
        return this.context.getEnv()._strict;
    }

    public void initBuiltinObjects(ScriptEngine eng) {
        if (this.builtinObject != null) {
            return;
        }
        this.engine = eng;
        if (this.engine != null) {
            this.scontext = new ThreadLocal();
        }
        this.init(eng);
    }

    public Object wrapAsObject(Object obj) {
        if (obj instanceof Boolean) {
            return new NativeBoolean((Boolean)obj, this);
        }
        if (obj instanceof Number) {
            return new NativeNumber(((Number)obj).doubleValue(), this);
        }
        if (JSType.isString(obj)) {
            return new NativeString((CharSequence)obj, this);
        }
        if (obj instanceof Object[]) {
            return new NativeArray(ArrayData.allocate((Object[])obj), this);
        }
        if (obj instanceof double[]) {
            return new NativeArray(ArrayData.allocate((double[])obj), this);
        }
        if (obj instanceof int[]) {
            return new NativeArray(ArrayData.allocate((int[])obj), this);
        }
        if (obj instanceof ArrayData) {
            return new NativeArray((ArrayData)obj, this);
        }
        return obj;
    }

    public static GuardedInvocation primitiveLookup(LinkRequest request, Object self) {
        if (JSType.isString(self)) {
            return NativeString.lookupPrimitive(request, self);
        }
        if (self instanceof Number) {
            return NativeNumber.lookupPrimitive(request, self);
        }
        if (self instanceof Boolean) {
            return NativeBoolean.lookupPrimitive(request, self);
        }
        throw new IllegalArgumentException("Unsupported primitive: " + self);
    }

    public static MethodHandle getPrimitiveWrapFilter(Object self) {
        if (JSType.isString(self)) {
            return NativeString.WRAPFILTER;
        }
        if (self instanceof Number) {
            return NativeNumber.WRAPFILTER;
        }
        if (self instanceof Boolean) {
            return NativeBoolean.WRAPFILTER;
        }
        throw new IllegalArgumentException("Unsupported primitive: " + self);
    }

    public ScriptObject newObject() {
        return this.useDualFields() ? new JD(this.getObjectPrototype()) : new JO(this.getObjectPrototype());
    }

    public Object getDefaultValue(ScriptObject sobj, Class<?> typeHint) {
        Class<Object> hint = typeHint;
        if (hint == null) {
            hint = Number.class;
        }
        try {
            if (hint == String.class) {
                Object value;
                Object value2;
                Object toString2 = this.TO_STRING.getGetter().invokeExact(sobj);
                if (Bootstrap.isCallable(toString2) && JSType.isPrimitive(value2 = this.TO_STRING.getInvoker().invokeExact(toString2, sobj))) {
                    return value2;
                }
                Object valueOf = this.VALUE_OF.getGetter().invokeExact(sobj);
                if (Bootstrap.isCallable(valueOf) && JSType.isPrimitive(value = this.VALUE_OF.getInvoker().invokeExact(valueOf, sobj))) {
                    return value;
                }
                throw ECMAErrors.typeError(this, "cannot.get.default.string", new String[0]);
            }
            if (hint == Number.class) {
                Object value;
                Object value3;
                Object valueOf = this.VALUE_OF.getGetter().invokeExact(sobj);
                if (Bootstrap.isCallable(valueOf) && JSType.isPrimitive(value3 = this.VALUE_OF.getInvoker().invokeExact(valueOf, sobj))) {
                    return value3;
                }
                Object toString3 = this.TO_STRING.getGetter().invokeExact(sobj);
                if (Bootstrap.isCallable(toString3) && JSType.isPrimitive(value = this.TO_STRING.getInvoker().invokeExact(toString3, sobj))) {
                    return value;
                }
                throw ECMAErrors.typeError(this, "cannot.get.default.number", new String[0]);
            }
        }
        catch (Error | RuntimeException e) {
            throw e;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return ScriptRuntime.UNDEFINED;
    }

    public boolean isError(ScriptObject sobj) {
        ScriptObject errorProto = this.getErrorPrototype();
        for (ScriptObject proto = sobj.getProto(); proto != null; proto = proto.getProto()) {
            if (proto != errorProto) continue;
            return true;
        }
        return false;
    }

    public ScriptObject newError(String msg) {
        return new NativeError(msg, this);
    }

    public ScriptObject newEvalError(String msg) {
        return new NativeEvalError(msg, this);
    }

    public ScriptObject newRangeError(String msg) {
        return new NativeRangeError(msg, this);
    }

    public ScriptObject newReferenceError(String msg) {
        return new NativeReferenceError(msg, this);
    }

    public ScriptObject newSyntaxError(String msg) {
        return new NativeSyntaxError(msg, this);
    }

    public ScriptObject newTypeError(String msg) {
        return new NativeTypeError(msg, this);
    }

    public ScriptObject newURIError(String msg) {
        return new NativeURIError(msg, this);
    }

    public PropertyDescriptor newGenericDescriptor(boolean configurable, boolean enumerable) {
        return new GenericPropertyDescriptor(configurable, enumerable, this);
    }

    public PropertyDescriptor newDataDescriptor(Object value, boolean configurable, boolean enumerable, boolean writable) {
        return new DataPropertyDescriptor(configurable, enumerable, writable, value, this);
    }

    public PropertyDescriptor newAccessorDescriptor(Object get, Object set, boolean configurable, boolean enumerable) {
        AccessorPropertyDescriptor desc = new AccessorPropertyDescriptor(configurable, enumerable, get == null ? ScriptRuntime.UNDEFINED : get, set == null ? ScriptRuntime.UNDEFINED : set, this);
        if (get == null) {
            desc.delete("get", false);
        }
        if (set == null) {
            desc.delete("set", false);
        }
        return desc;
    }

    private static <T> T getLazilyCreatedValue(Object key, Callable<T> creator, Map<Object, T> map) {
        T obj = map.get(key);
        if (obj != null) {
            return obj;
        }
        try {
            T newObj = creator.call();
            T existingObj = map.putIfAbsent(key, newObj);
            return existingObj != null ? existingObj : newObj;
        }
        catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    public InvokeByName getInvokeByName(Object key, Callable<InvokeByName> creator) {
        return Global.getLazilyCreatedValue(key, creator, this.namedInvokers);
    }

    public MethodHandle getDynamicInvoker(Object key, Callable<MethodHandle> creator) {
        return Global.getLazilyCreatedValue(key, creator, this.dynamicInvokers);
    }

    public static Object __noSuchProperty__(Object self, Object name) {
        int scope;
        Global global = Global.instance();
        ScriptContext sctxt = global.currentContext();
        String nameStr = name.toString();
        if (sctxt != null && (scope = sctxt.getAttributesScope(nameStr)) != -1) {
            return ScriptObjectMirror.unwrap(sctxt.getAttribute(nameStr, scope), global);
        }
        if ("context".equals(nameStr)) {
            return sctxt;
        }
        if ("engine".equals(nameStr) && (System.getSecurityManager() == null || global.getClassFilter() == null)) {
            return global.engine;
        }
        if (self == ScriptRuntime.UNDEFINED) {
            throw ECMAErrors.referenceError(global, "not.defined", nameStr);
        }
        return ScriptRuntime.UNDEFINED;
    }

    public static Object eval(Object self, Object str) {
        return Global.directEval(self, str, Global.instanceFrom(self), ScriptRuntime.UNDEFINED, false);
    }

    public static Object directEval(Object self, Object str, Object callThis, Object location, boolean strict) {
        if (!JSType.isString(str)) {
            return str;
        }
        Global global = Global.instanceFrom(self);
        Global scope = self instanceof ScriptObject && ((ScriptObject)self).isScope() ? (ScriptObject)self : global;
        return global.getContext().eval(scope, str.toString(), callThis, location, strict, true);
    }

    public static Object print(Object self, Object ... objects) {
        return Global.instanceFrom(self).printImpl(false, objects);
    }

    public static Object println(Object self, Object ... objects) {
        return Global.instanceFrom(self).printImpl(true, objects);
    }

    public static Object load(Object self, Object source) throws IOException {
        Global global = Global.instanceFrom(self);
        return global.getContext().load(self, source);
    }

    public static Object loadWithNewGlobal(Object self, Object ... args2) throws IOException {
        Global global = Global.instanceFrom(self);
        int length = args2.length;
        boolean hasArgs = 0 < length;
        Undefined from = hasArgs ? args2[0] : ScriptRuntime.UNDEFINED;
        Object[] arguments = hasArgs ? Arrays.copyOfRange(args2, 1, length) : args2;
        return global.getContext().loadWithNewGlobal(from, arguments);
    }

    public static Object exit(Object self, Object code) {
        System.exit(JSType.toInt32(code));
        return ScriptRuntime.UNDEFINED;
    }

    public ScriptObject getObjectPrototype() {
        return ScriptFunction.getPrototype(this.builtinObject);
    }

    public ScriptObject getFunctionPrototype() {
        return ScriptFunction.getPrototype(this.builtinFunction);
    }

    ScriptObject getArrayPrototype() {
        return ScriptFunction.getPrototype(this.builtinArray);
    }

    ScriptObject getBooleanPrototype() {
        return ScriptFunction.getPrototype(this.builtinBoolean);
    }

    ScriptObject getNumberPrototype() {
        return ScriptFunction.getPrototype(this.builtinNumber);
    }

    ScriptObject getDatePrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinDate());
    }

    ScriptObject getRegExpPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinRegExp());
    }

    ScriptObject getStringPrototype() {
        return ScriptFunction.getPrototype(this.builtinString);
    }

    ScriptObject getErrorPrototype() {
        return ScriptFunction.getPrototype(this.builtinError);
    }

    ScriptObject getEvalErrorPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinEvalError());
    }

    ScriptObject getRangeErrorPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinRangeError());
    }

    ScriptObject getReferenceErrorPrototype() {
        return ScriptFunction.getPrototype(this.builtinReferenceError);
    }

    ScriptObject getSyntaxErrorPrototype() {
        return ScriptFunction.getPrototype(this.builtinSyntaxError);
    }

    ScriptObject getTypeErrorPrototype() {
        return ScriptFunction.getPrototype(this.builtinTypeError);
    }

    ScriptObject getURIErrorPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinURIError());
    }

    ScriptObject getJavaImporterPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinJavaImporter());
    }

    ScriptObject getJSAdapterPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinJSAdapter());
    }

    private synchronized ScriptFunction getBuiltinArrayBuffer() {
        if (this.builtinArrayBuffer == null) {
            this.builtinArrayBuffer = this.initConstructorAndSwitchPoint("ArrayBuffer", ScriptFunction.class);
        }
        return this.builtinArrayBuffer;
    }

    ScriptObject getArrayBufferPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinArrayBuffer());
    }

    private synchronized ScriptFunction getBuiltinDataView() {
        if (this.builtinDataView == null) {
            this.builtinDataView = this.initConstructorAndSwitchPoint("DataView", ScriptFunction.class);
        }
        return this.builtinDataView;
    }

    ScriptObject getDataViewPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinDataView());
    }

    private synchronized ScriptFunction getBuiltinInt8Array() {
        if (this.builtinInt8Array == null) {
            this.builtinInt8Array = this.initConstructorAndSwitchPoint("Int8Array", ScriptFunction.class);
        }
        return this.builtinInt8Array;
    }

    ScriptObject getInt8ArrayPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinInt8Array());
    }

    private synchronized ScriptFunction getBuiltinUint8Array() {
        if (this.builtinUint8Array == null) {
            this.builtinUint8Array = this.initConstructorAndSwitchPoint("Uint8Array", ScriptFunction.class);
        }
        return this.builtinUint8Array;
    }

    ScriptObject getUint8ArrayPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinUint8Array());
    }

    private synchronized ScriptFunction getBuiltinUint8ClampedArray() {
        if (this.builtinUint8ClampedArray == null) {
            this.builtinUint8ClampedArray = this.initConstructorAndSwitchPoint("Uint8ClampedArray", ScriptFunction.class);
        }
        return this.builtinUint8ClampedArray;
    }

    ScriptObject getUint8ClampedArrayPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinUint8ClampedArray());
    }

    private synchronized ScriptFunction getBuiltinInt16Array() {
        if (this.builtinInt16Array == null) {
            this.builtinInt16Array = this.initConstructorAndSwitchPoint("Int16Array", ScriptFunction.class);
        }
        return this.builtinInt16Array;
    }

    ScriptObject getInt16ArrayPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinInt16Array());
    }

    private synchronized ScriptFunction getBuiltinUint16Array() {
        if (this.builtinUint16Array == null) {
            this.builtinUint16Array = this.initConstructorAndSwitchPoint("Uint16Array", ScriptFunction.class);
        }
        return this.builtinUint16Array;
    }

    ScriptObject getUint16ArrayPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinUint16Array());
    }

    private synchronized ScriptFunction getBuiltinInt32Array() {
        if (this.builtinInt32Array == null) {
            this.builtinInt32Array = this.initConstructorAndSwitchPoint("Int32Array", ScriptFunction.class);
        }
        return this.builtinInt32Array;
    }

    ScriptObject getInt32ArrayPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinInt32Array());
    }

    private synchronized ScriptFunction getBuiltinUint32Array() {
        if (this.builtinUint32Array == null) {
            this.builtinUint32Array = this.initConstructorAndSwitchPoint("Uint32Array", ScriptFunction.class);
        }
        return this.builtinUint32Array;
    }

    ScriptObject getUint32ArrayPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinUint32Array());
    }

    private synchronized ScriptFunction getBuiltinFloat32Array() {
        if (this.builtinFloat32Array == null) {
            this.builtinFloat32Array = this.initConstructorAndSwitchPoint("Float32Array", ScriptFunction.class);
        }
        return this.builtinFloat32Array;
    }

    ScriptObject getFloat32ArrayPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinFloat32Array());
    }

    private synchronized ScriptFunction getBuiltinFloat64Array() {
        if (this.builtinFloat64Array == null) {
            this.builtinFloat64Array = this.initConstructorAndSwitchPoint("Float64Array", ScriptFunction.class);
        }
        return this.builtinFloat64Array;
    }

    ScriptObject getFloat64ArrayPrototype() {
        return ScriptFunction.getPrototype(this.getBuiltinFloat64Array());
    }

    public ScriptFunction getTypeErrorThrower() {
        return this.typeErrorThrower;
    }

    private synchronized ScriptFunction getBuiltinDate() {
        if (this.builtinDate == null) {
            this.builtinDate = this.initConstructorAndSwitchPoint("Date", ScriptFunction.class);
            ScriptObject dateProto = ScriptFunction.getPrototype(this.builtinDate);
            this.DEFAULT_DATE = new NativeDate(Double.NaN, dateProto);
        }
        return this.builtinDate;
    }

    private synchronized ScriptFunction getBuiltinEvalError() {
        if (this.builtinEvalError == null) {
            this.builtinEvalError = this.initErrorSubtype("EvalError", this.getErrorPrototype());
        }
        return this.builtinEvalError;
    }

    private ScriptFunction getBuiltinFunction() {
        return this.builtinFunction;
    }

    public static SwitchPoint getBuiltinFunctionApplySwitchPoint() {
        return ScriptFunction.getPrototype(Global.instance().getBuiltinFunction()).getProperty("apply").getBuiltinSwitchPoint();
    }

    private static boolean isBuiltinFunctionProperty(String name) {
        Global instance = Global.instance();
        ScriptFunction builtinFunction = instance.getBuiltinFunction();
        if (builtinFunction == null) {
            return false;
        }
        boolean isBuiltinFunction = instance.function == builtinFunction;
        return isBuiltinFunction && ScriptFunction.getPrototype(builtinFunction).getProperty(name).isBuiltin();
    }

    public static boolean isBuiltinFunctionPrototypeApply() {
        return Global.isBuiltinFunctionProperty("apply");
    }

    public static boolean isBuiltinFunctionPrototypeCall() {
        return Global.isBuiltinFunctionProperty("call");
    }

    private synchronized ScriptFunction getBuiltinJSAdapter() {
        if (this.builtinJSAdapter == null) {
            this.builtinJSAdapter = this.initConstructorAndSwitchPoint("JSAdapter", ScriptFunction.class);
        }
        return this.builtinJSAdapter;
    }

    private synchronized ScriptObject getBuiltinJSON() {
        if (this.builtinJSON == null) {
            this.builtinJSON = this.initConstructorAndSwitchPoint("JSON", ScriptObject.class);
        }
        return this.builtinJSON;
    }

    private synchronized ScriptFunction getBuiltinJavaImporter() {
        if (this.getContext().getEnv()._no_java) {
            throw new IllegalStateException();
        }
        if (this.builtinJavaImporter == null) {
            this.builtinJavaImporter = this.initConstructor("JavaImporter", ScriptFunction.class);
        }
        return this.builtinJavaImporter;
    }

    private synchronized ScriptObject getBuiltinJavaApi() {
        if (this.getContext().getEnv()._no_java) {
            throw new IllegalStateException();
        }
        if (this.builtinJavaApi == null) {
            this.builtinJavaApi = this.initConstructor("Java", ScriptObject.class);
        }
        return this.builtinJavaApi;
    }

    private synchronized ScriptFunction getBuiltinRangeError() {
        if (this.builtinRangeError == null) {
            this.builtinRangeError = this.initErrorSubtype("RangeError", this.getErrorPrototype());
        }
        return this.builtinRangeError;
    }

    private synchronized ScriptFunction getBuiltinRegExp() {
        if (this.builtinRegExp == null) {
            this.builtinRegExp = this.initConstructorAndSwitchPoint("RegExp", ScriptFunction.class);
            ScriptObject regExpProto = ScriptFunction.getPrototype(this.builtinRegExp);
            this.DEFAULT_REGEXP = new NativeRegExp("(?:)", "", this, regExpProto);
            regExpProto.addBoundProperties(this.DEFAULT_REGEXP);
        }
        return this.builtinRegExp;
    }

    private synchronized ScriptFunction getBuiltinURIError() {
        if (this.builtinURIError == null) {
            this.builtinURIError = this.initErrorSubtype("URIError", this.getErrorPrototype());
        }
        return this.builtinURIError;
    }

    @Override
    public String getClassName() {
        return "global";
    }

    public static Object regExpCopy(Object regexp) {
        return new NativeRegExp((NativeRegExp)regexp);
    }

    public static NativeRegExp toRegExp(Object obj) {
        if (obj instanceof NativeRegExp) {
            return (NativeRegExp)obj;
        }
        return new NativeRegExp(JSType.toString(obj));
    }

    public static Object toObject(Object obj) {
        if (obj == null || obj == ScriptRuntime.UNDEFINED) {
            throw ECMAErrors.typeError("not.an.object", ScriptRuntime.safeToString(obj));
        }
        if (obj instanceof ScriptObject) {
            return obj;
        }
        return Global.instance().wrapAsObject(obj);
    }

    public static NativeArray allocate(Object[] initial) {
        ArrayData arrayData = ArrayData.allocate(initial);
        for (int index = 0; index < initial.length; ++index) {
            Object value = initial[index];
            if (value != ScriptRuntime.EMPTY) continue;
            arrayData = arrayData.delete(index);
        }
        return new NativeArray(arrayData);
    }

    public static NativeArray allocate(double[] initial) {
        return new NativeArray(ArrayData.allocate(initial));
    }

    public static NativeArray allocate(int[] initial) {
        return new NativeArray(ArrayData.allocate(initial));
    }

    public static ScriptObject allocateArguments(Object[] arguments, Object callee, int numParams) {
        return NativeArguments.allocate(arguments, (ScriptFunction)callee, numParams);
    }

    public static boolean isEval(Object fn) {
        return fn == Global.instance().builtinEval;
    }

    public static Object replaceLocationPropertyPlaceholder(Object placeholder, Object locationProperty) {
        return Global.isLocationPropertyPlaceholder(placeholder) ? locationProperty : placeholder;
    }

    public static boolean isLocationPropertyPlaceholder(Object placeholder) {
        return placeholder == LOCATION_PLACEHOLDER;
    }

    public static Object newRegExp(String expression, String options) {
        if (options == null) {
            return new NativeRegExp(expression);
        }
        return new NativeRegExp(expression, options);
    }

    public static ScriptObject objectPrototype() {
        return Global.instance().getObjectPrototype();
    }

    public static ScriptObject newEmptyInstance() {
        return Global.instance().newObject();
    }

    public static ScriptObject checkObject(Object obj) {
        if (!(obj instanceof ScriptObject)) {
            throw ECMAErrors.typeError("not.an.object", ScriptRuntime.safeToString(obj));
        }
        return (ScriptObject)obj;
    }

    public static void checkObjectCoercible(Object obj) {
        if (obj == null || obj == ScriptRuntime.UNDEFINED) {
            throw ECMAErrors.typeError("not.an.object", ScriptRuntime.safeToString(obj));
        }
    }

    public final ScriptObject getLexicalScope() {
        assert (this.context.getEnv()._es6);
        return this.lexicalScope;
    }

    @Override
    public void addBoundProperties(ScriptObject source, Property[] properties) {
        PropertyMap ownMap = this.getMap();
        LexicalScope lexScope = null;
        PropertyMap lexicalMap = null;
        boolean hasLexicalDefinitions = false;
        if (this.context.getEnv()._es6) {
            lexScope = (LexicalScope)this.getLexicalScope();
            lexicalMap = lexScope.getMap();
            for (Property property : properties) {
                Property globalProperty;
                if (property.isLexicalBinding()) {
                    hasLexicalDefinitions = true;
                }
                if ((globalProperty = ownMap.findProperty(property.getKey())) != null && !globalProperty.isConfigurable() && property.isLexicalBinding()) {
                    throw ECMAErrors.syntaxError("redeclare.variable", property.getKey());
                }
                Property lexicalProperty = lexicalMap.findProperty(property.getKey());
                if (lexicalProperty == null || property.isConfigurable()) continue;
                throw ECMAErrors.syntaxError("redeclare.variable", property.getKey());
            }
        }
        boolean extensible = this.isExtensible();
        for (Property property : properties) {
            if (property.isLexicalBinding()) {
                assert (lexScope != null);
                lexicalMap = lexScope.addBoundProperty(lexicalMap, source, property, true);
                if (ownMap.findProperty(property.getKey()) == null) continue;
                this.invalidateGlobalConstant(property.getKey());
                continue;
            }
            ownMap = this.addBoundProperty(ownMap, source, property, extensible);
        }
        this.setMap(ownMap);
        if (hasLexicalDefinitions) {
            assert (lexScope != null);
            lexScope.setMap(lexicalMap);
            this.invalidateLexicalSwitchPoint();
        }
    }

    @Override
    public GuardedInvocation findGetMethod(CallSiteDescriptor desc, LinkRequest request, String operator) {
        String name = desc.getNameToken(2);
        boolean isScope = NashornCallSiteDescriptor.isScope(desc);
        if (this.lexicalScope != null && isScope && !NashornCallSiteDescriptor.isApplyToCall(desc) && this.lexicalScope.hasOwnProperty(name)) {
            return this.lexicalScope.findGetMethod(desc, request, operator);
        }
        GuardedInvocation invocation = super.findGetMethod(desc, request, operator);
        if (isScope && this.context.getEnv()._es6 && (invocation.getSwitchPoints() == null || !this.hasOwnProperty(name))) {
            return invocation.addSwitchPoint(this.getLexicalScopeSwitchPoint());
        }
        return invocation;
    }

    @Override
    protected FindProperty findProperty(String key, boolean deep, ScriptObject start) {
        FindProperty find;
        if (this.lexicalScope != null && start != this && start.isScope() && (find = this.lexicalScope.findProperty(key, false)) != null) {
            return find;
        }
        return super.findProperty(key, deep, start);
    }

    @Override
    public GuardedInvocation findSetMethod(CallSiteDescriptor desc, LinkRequest request) {
        String name;
        boolean isScope = NashornCallSiteDescriptor.isScope(desc);
        if (this.lexicalScope != null && isScope && this.lexicalScope.hasOwnProperty(name = desc.getNameToken(2))) {
            return this.lexicalScope.findSetMethod(desc, request);
        }
        GuardedInvocation invocation = super.findSetMethod(desc, request);
        if (isScope && this.context.getEnv()._es6) {
            return invocation.addSwitchPoint(this.getLexicalScopeSwitchPoint());
        }
        return invocation;
    }

    public void addShellBuiltins() {
        ScriptFunction value = ScriptFunction.createBuiltin("input", ShellFunctions.INPUT);
        this.addOwnProperty("input", 2, value);
        value = ScriptFunction.createBuiltin("evalinput", ShellFunctions.EVALINPUT);
        this.addOwnProperty("evalinput", 2, value);
    }

    private synchronized SwitchPoint getLexicalScopeSwitchPoint() {
        SwitchPoint switchPoint = this.lexicalScopeSwitchPoint;
        if (switchPoint == null || switchPoint.hasBeenInvalidated()) {
            switchPoint = this.lexicalScopeSwitchPoint = new SwitchPoint();
        }
        return switchPoint;
    }

    private synchronized void invalidateLexicalSwitchPoint() {
        if (this.lexicalScopeSwitchPoint != null) {
            this.context.getLogger(GlobalConstants.class).info("Invalidating non-constant globals on lexical scope update");
            SwitchPoint.invalidateAll(new SwitchPoint[]{this.lexicalScopeSwitchPoint});
        }
    }

    private static Object lexicalScopeFilter(Object self) {
        if (self instanceof Global) {
            return ((Global)self).getLexicalScope();
        }
        return self;
    }

    private <T extends ScriptObject> T initConstructorAndSwitchPoint(String name, Class<T> clazz) {
        T func = this.initConstructor(name, clazz);
        this.tagBuiltinProperties(name, (ScriptObject)func);
        return func;
    }

    private void init(ScriptEngine eng) {
        assert (Context.getGlobal() == this) : "this global is not set as current";
        ScriptEnvironment env = this.getContext().getEnv();
        this.initFunctionAndObject();
        this.setInitialProto(this.getObjectPrototype());
        this.builtinEval = ScriptFunction.createBuiltin("eval", EVAL);
        this.eval = this.builtinEval;
        this.parseInt = ScriptFunction.createBuiltin("parseInt", GlobalFunctions.PARSEINT, new Specialization[]{new Specialization(GlobalFunctions.PARSEINT_Z), new Specialization(GlobalFunctions.PARSEINT_I), new Specialization(GlobalFunctions.PARSEINT_OI), new Specialization(GlobalFunctions.PARSEINT_O)});
        this.parseFloat = ScriptFunction.createBuiltin("parseFloat", GlobalFunctions.PARSEFLOAT);
        this.isNaN = ScriptFunction.createBuiltin("isNaN", GlobalFunctions.IS_NAN, new Specialization[]{new Specialization(GlobalFunctions.IS_NAN_I), new Specialization(GlobalFunctions.IS_NAN_J), new Specialization(GlobalFunctions.IS_NAN_D)});
        this.parseFloat = ScriptFunction.createBuiltin("parseFloat", GlobalFunctions.PARSEFLOAT);
        this.isNaN = ScriptFunction.createBuiltin("isNaN", GlobalFunctions.IS_NAN);
        this.isFinite = ScriptFunction.createBuiltin("isFinite", GlobalFunctions.IS_FINITE);
        this.encodeURI = ScriptFunction.createBuiltin("encodeURI", GlobalFunctions.ENCODE_URI);
        this.encodeURIComponent = ScriptFunction.createBuiltin("encodeURIComponent", GlobalFunctions.ENCODE_URICOMPONENT);
        this.decodeURI = ScriptFunction.createBuiltin("decodeURI", GlobalFunctions.DECODE_URI);
        this.decodeURIComponent = ScriptFunction.createBuiltin("decodeURIComponent", GlobalFunctions.DECODE_URICOMPONENT);
        this.escape = ScriptFunction.createBuiltin("escape", GlobalFunctions.ESCAPE);
        this.unescape = ScriptFunction.createBuiltin("unescape", GlobalFunctions.UNESCAPE);
        this.print = ScriptFunction.createBuiltin("print", env._print_no_newline ? PRINT : PRINTLN);
        this.load = ScriptFunction.createBuiltin("load", LOAD);
        this.loadWithNewGlobal = ScriptFunction.createBuiltin("loadWithNewGlobal", LOAD_WITH_NEW_GLOBAL);
        this.exit = ScriptFunction.createBuiltin("exit", EXIT);
        this.quit = ScriptFunction.createBuiltin("quit", EXIT);
        this.builtinArray = this.initConstructorAndSwitchPoint("Array", ScriptFunction.class);
        this.builtinBoolean = this.initConstructorAndSwitchPoint("Boolean", ScriptFunction.class);
        this.builtinNumber = this.initConstructorAndSwitchPoint("Number", ScriptFunction.class);
        this.builtinString = this.initConstructorAndSwitchPoint("String", ScriptFunction.class);
        this.builtinMath = this.initConstructorAndSwitchPoint("Math", ScriptObject.class);
        ScriptObject stringPrototype = this.getStringPrototype();
        stringPrototype.addOwnProperty("length", 7, 0.0);
        ScriptObject arrayPrototype = this.getArrayPrototype();
        arrayPrototype.setIsArray();
        this.initErrorObjects();
        if (!env._no_java) {
            this.javaApi = LAZY_SENTINEL;
            this.javaImporter = LAZY_SENTINEL;
            this.initJavaAccess();
        } else {
            this.delete("Java", false);
            this.delete("JavaImporter", false);
            this.delete("Packages", false);
            this.delete("com", false);
            this.delete("edu", false);
            this.delete("java", false);
            this.delete("javafx", false);
            this.delete("javax", false);
            this.delete("org", false);
        }
        if (!env._no_typed_arrays) {
            this.arrayBuffer = LAZY_SENTINEL;
            this.dataView = LAZY_SENTINEL;
            this.int8Array = LAZY_SENTINEL;
            this.uint8Array = LAZY_SENTINEL;
            this.uint8ClampedArray = LAZY_SENTINEL;
            this.int16Array = LAZY_SENTINEL;
            this.uint16Array = LAZY_SENTINEL;
            this.int32Array = LAZY_SENTINEL;
            this.uint32Array = LAZY_SENTINEL;
            this.float32Array = LAZY_SENTINEL;
            this.float64Array = LAZY_SENTINEL;
        }
        if (env._scripting) {
            this.initScripting(env);
        }
        if (Context.DEBUG) {
            boolean debugOkay;
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                try {
                    sm.checkPermission(new RuntimePermission("nashorn.debugMode"));
                    debugOkay = true;
                }
                catch (SecurityException ignored) {
                    debugOkay = false;
                }
            } else {
                debugOkay = true;
            }
            if (debugOkay) {
                this.initDebug();
            }
        }
        this.copyBuiltins();
        this.arguments = this.wrapAsObject(env.getArguments().toArray());
        if (env._scripting) {
            this.addOwnProperty("$ARG", 2, this.arguments);
        }
        if (eng != null) {
            this.addOwnProperty("javax.script.filename", 2, null);
            ScriptFunction noSuchProp = ScriptFunction.createStrictBuiltin("__noSuchProperty__", NO_SUCH_PROPERTY);
            this.addOwnProperty("__noSuchProperty__", 2, noSuchProp);
        }
    }

    private void initErrorObjects() {
        this.builtinError = this.initConstructor("Error", ScriptFunction.class);
        ScriptObject errorProto = this.getErrorPrototype();
        ScriptFunction getStack = ScriptFunction.createBuiltin("getStack", NativeError.GET_STACK);
        ScriptFunction setStack = ScriptFunction.createBuiltin("setStack", NativeError.SET_STACK);
        errorProto.addOwnProperty("stack", 2, getStack, setStack);
        ScriptFunction getLineNumber = ScriptFunction.createBuiltin("getLineNumber", NativeError.GET_LINENUMBER);
        ScriptFunction setLineNumber = ScriptFunction.createBuiltin("setLineNumber", NativeError.SET_LINENUMBER);
        errorProto.addOwnProperty("lineNumber", 2, getLineNumber, setLineNumber);
        ScriptFunction getColumnNumber = ScriptFunction.createBuiltin("getColumnNumber", NativeError.GET_COLUMNNUMBER);
        ScriptFunction setColumnNumber = ScriptFunction.createBuiltin("setColumnNumber", NativeError.SET_COLUMNNUMBER);
        errorProto.addOwnProperty("columnNumber", 2, getColumnNumber, setColumnNumber);
        ScriptFunction getFileName = ScriptFunction.createBuiltin("getFileName", NativeError.GET_FILENAME);
        ScriptFunction setFileName = ScriptFunction.createBuiltin("setFileName", NativeError.SET_FILENAME);
        errorProto.addOwnProperty("fileName", 2, getFileName, setFileName);
        errorProto.set((Object)"name", (Object)"Error", 0);
        errorProto.set((Object)"message", (Object)"", 0);
        this.tagBuiltinProperties("Error", this.builtinError);
        this.builtinReferenceError = this.initErrorSubtype("ReferenceError", errorProto);
        this.builtinSyntaxError = this.initErrorSubtype("SyntaxError", errorProto);
        this.builtinTypeError = this.initErrorSubtype("TypeError", errorProto);
    }

    private ScriptFunction initErrorSubtype(String name, ScriptObject errorProto) {
        ScriptFunction cons = this.initConstructor(name, ScriptFunction.class);
        ScriptObject prototype = ScriptFunction.getPrototype(cons);
        prototype.set((Object)"name", (Object)name, 0);
        prototype.set((Object)"message", (Object)"", 0);
        prototype.setInitialProto(errorProto);
        this.tagBuiltinProperties(name, cons);
        return cons;
    }

    private void initJavaAccess() {
        ScriptObject objectProto = this.getObjectPrototype();
        this.builtinPackages = new NativeJavaPackage("", objectProto);
        this.builtinCom = new NativeJavaPackage("com", objectProto);
        this.builtinEdu = new NativeJavaPackage("edu", objectProto);
        this.builtinJava = new NativeJavaPackage("java", objectProto);
        this.builtinJavafx = new NativeJavaPackage("javafx", objectProto);
        this.builtinJavax = new NativeJavaPackage("javax", objectProto);
        this.builtinOrg = new NativeJavaPackage("org", objectProto);
    }

    private void initScripting(ScriptEnvironment scriptEnv) {
        ScriptObject value = ScriptFunction.createBuiltin("readLine", ScriptingFunctions.READLINE);
        this.addOwnProperty("readLine", 2, value);
        value = ScriptFunction.createBuiltin("readFully", ScriptingFunctions.READFULLY);
        this.addOwnProperty("readFully", 2, value);
        String execName = "$EXEC";
        value = ScriptFunction.createBuiltin("$EXEC", ScriptingFunctions.EXEC);
        value.addOwnProperty("throwOnError", 2, false);
        this.addOwnProperty("$EXEC", 2, value);
        value = (ScriptObject)this.get("print");
        this.addOwnProperty("echo", 2, value);
        ScriptObject options = this.newObject();
        Global.copyOptions(options, scriptEnv);
        this.addOwnProperty("$OPTIONS", 2, options);
        if (System.getSecurityManager() == null) {
            ScriptObject env = this.newObject();
            env.putAll(System.getenv(), scriptEnv._strict);
            if (!env.containsKey("PWD")) {
                env.put("PWD", System.getProperty("user.dir"), scriptEnv._strict);
            }
            this.addOwnProperty("$ENV", 2, env);
        } else {
            this.addOwnProperty("$ENV", 2, ScriptRuntime.UNDEFINED);
        }
        this.addOwnProperty("$OUT", 2, ScriptRuntime.UNDEFINED);
        this.addOwnProperty("$ERR", 2, ScriptRuntime.UNDEFINED);
        this.addOwnProperty("$EXIT", 2, ScriptRuntime.UNDEFINED);
    }

    private static void copyOptions(ScriptObject options, ScriptEnvironment scriptEnv) {
        for (Field f : scriptEnv.getClass().getFields()) {
            try {
                options.set((Object)f.getName(), f.get(scriptEnv), 0);
            }
            catch (IllegalAccessException | IllegalArgumentException exp) {
                throw new RuntimeException(exp);
            }
        }
    }

    private void copyBuiltins() {
        this.array = this.builtinArray;
        this._boolean = this.builtinBoolean;
        this.error = this.builtinError;
        this.function = this.builtinFunction;
        this.com = this.builtinCom;
        this.edu = this.builtinEdu;
        this.java = this.builtinJava;
        this.javafx = this.builtinJavafx;
        this.javax = this.builtinJavax;
        this.org = this.builtinOrg;
        this.math = this.builtinMath;
        this.number = this.builtinNumber;
        this.object = this.builtinObject;
        this.packages = this.builtinPackages;
        this.referenceError = this.builtinReferenceError;
        this.string = this.builtinString;
        this.syntaxError = this.builtinSyntaxError;
        this.typeError = this.builtinTypeError;
    }

    private void initDebug() {
        this.addOwnProperty("Debug", 2, this.initConstructor("Debug", ScriptObject.class));
    }

    private Object printImpl(boolean newLine, Object ... objects) {
        ScriptContext sc = this.currentContext();
        PrintWriter out = sc != null ? new PrintWriter(sc.getWriter()) : this.getContext().getEnv().getOut();
        StringBuilder sb = new StringBuilder();
        for (Object obj : objects) {
            if (sb.length() != 0) {
                sb.append(' ');
            }
            sb.append(JSType.toString(obj));
        }
        if (newLine) {
            out.println(sb.toString());
        } else {
            out.print(sb.toString());
        }
        out.flush();
        return ScriptRuntime.UNDEFINED;
    }

    private <T extends ScriptObject> T initConstructor(String name, Class<T> clazz) {
        try {
            StringBuilder sb = new StringBuilder("jdk.nashorn.internal.objects.");
            sb.append("Native");
            sb.append(name);
            sb.append("$Constructor");
            Class<?> funcClass = Class.forName(sb.toString());
            ScriptObject res = (ScriptObject)clazz.cast(funcClass.newInstance());
            if (res instanceof ScriptFunction) {
                ScriptFunction func = (ScriptFunction)res;
                func.modifyOwnProperty(func.getProperty("prototype"), 7);
            }
            if (res.getProto() == null) {
                res.setInitialProto(this.getObjectPrototype());
            }
            res.setIsBuiltin();
            return (T)res;
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Property> extractBuiltinProperties(String name, ScriptObject func) {
        Property prop;
        ScriptObject proto;
        ArrayList<Property> list = new ArrayList<Property>();
        list.addAll(Arrays.asList(func.getMap().getProperties()));
        if (func instanceof ScriptFunction && (proto = ScriptFunction.getPrototype((ScriptFunction)func)) != null) {
            list.addAll(Arrays.asList(proto.getMap().getProperties()));
        }
        if ((prop = this.getProperty(name)) != null) {
            list.add(prop);
        }
        return list;
    }

    private void tagBuiltinProperties(String name, ScriptObject func) {
        SwitchPoint sp = this.context.getBuiltinSwitchPoint(name);
        if (sp == null) {
            sp = this.context.newBuiltinSwitchPoint(name);
        }
        for (Property prop : this.extractBuiltinProperties(name, func)) {
            prop.setBuiltinSwitchPoint(sp);
        }
    }

    private void initFunctionAndObject() {
        ScriptObject prototype;
        ScriptFunction func;
        Object value;
        String key;
        Property[] properties;
        this.builtinFunction = this.initConstructor("Function", ScriptFunction.class);
        ScriptFunction anon = ScriptFunction.createAnonymous();
        anon.addBoundProperties(this.getFunctionPrototype());
        this.builtinFunction.setInitialProto(anon);
        this.builtinFunction.setPrototype(anon);
        anon.set((Object)"constructor", (Object)this.builtinFunction, 0);
        anon.deleteOwnProperty(anon.getMap().findProperty("prototype"));
        this.typeErrorThrower = ScriptFunction.createBuiltin("TypeErrorThrower", Lookup.TYPE_ERROR_THROWER_GETTER);
        this.typeErrorThrower.preventExtensions();
        this.builtinObject = this.initConstructor("Object", ScriptFunction.class);
        ScriptObject ObjectPrototype = this.getObjectPrototype();
        anon.setInitialProto(ObjectPrototype);
        ScriptFunction getProto = ScriptFunction.createBuiltin("getProto", NativeObject.GET__PROTO__);
        ScriptFunction setProto = ScriptFunction.createBuiltin("setProto", NativeObject.SET__PROTO__);
        ObjectPrototype.addOwnProperty("__proto__", 2, getProto, setProto);
        for (Property property : properties = this.getFunctionPrototype().getMap().getProperties()) {
            key = property.getKey();
            value = this.builtinFunction.get(key);
            if (!(value instanceof ScriptFunction) || value == anon) continue;
            func = (ScriptFunction)value;
            func.setInitialProto(this.getFunctionPrototype());
            prototype = ScriptFunction.getPrototype(func);
            if (prototype == null) continue;
            prototype.setInitialProto(ObjectPrototype);
        }
        for (Property property : this.builtinObject.getMap().getProperties()) {
            key = property.getKey();
            value = this.builtinObject.get(key);
            if (!(value instanceof ScriptFunction) || (prototype = ScriptFunction.getPrototype(func = (ScriptFunction)value)) == null) continue;
            prototype.setInitialProto(ObjectPrototype);
        }
        for (Property property : properties = this.getObjectPrototype().getMap().getProperties()) {
            key = property.getKey();
            if (key.equals("constructor") || !((value = ObjectPrototype.get(key)) instanceof ScriptFunction) || (prototype = ScriptFunction.getPrototype(func = (ScriptFunction)value)) == null) continue;
            prototype.setInitialProto(ObjectPrototype);
        }
        this.tagBuiltinProperties("Object", this.builtinObject);
        this.tagBuiltinProperties("Function", this.builtinFunction);
        this.tagBuiltinProperties("Function", anon);
    }

    private static MethodHandle findOwnMH_S(String name, Class<?> rtype, Class<?> ... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), Global.class, name, Lookup.MH.type(rtype, types));
    }

    RegExpResult getLastRegExpResult() {
        return this.lastRegExpResult;
    }

    void setLastRegExpResult(RegExpResult regExpResult) {
        this.lastRegExpResult = regExpResult;
    }

    @Override
    protected boolean isGlobal() {
        return true;
    }

    static {
        Global.$clinit$();
    }

    public static void $clinit$() {
        ArrayList<Property> arrayList = new ArrayList<Property>(61);
        arrayList.add(AccessorProperty.create("arguments", 6, cfr_ldc_0(), cfr_ldc_1()));
        arrayList.add(AccessorProperty.create("parseInt", 2, cfr_ldc_2(), cfr_ldc_3()));
        arrayList.add(AccessorProperty.create("parseFloat", 2, cfr_ldc_4(), cfr_ldc_5()));
        arrayList.add(AccessorProperty.create("isNaN", 2, cfr_ldc_6(), cfr_ldc_7()));
        arrayList.add(AccessorProperty.create("isFinite", 2, cfr_ldc_8(), cfr_ldc_9()));
        arrayList.add(AccessorProperty.create("encodeURI", 2, cfr_ldc_10(), cfr_ldc_11()));
        arrayList.add(AccessorProperty.create("encodeURIComponent", 2, cfr_ldc_12(), cfr_ldc_13()));
        arrayList.add(AccessorProperty.create("decodeURI", 2, cfr_ldc_14(), cfr_ldc_15()));
        arrayList.add(AccessorProperty.create("decodeURIComponent", 2, cfr_ldc_16(), cfr_ldc_17()));
        arrayList.add(AccessorProperty.create("escape", 2, cfr_ldc_18(), cfr_ldc_19()));
        arrayList.add(AccessorProperty.create("unescape", 2, cfr_ldc_20(), cfr_ldc_21()));
        arrayList.add(AccessorProperty.create("print", 2, cfr_ldc_22(), cfr_ldc_23()));
        arrayList.add(AccessorProperty.create("load", 2, cfr_ldc_24(), cfr_ldc_25()));
        arrayList.add(AccessorProperty.create("loadWithNewGlobal", 2, cfr_ldc_26(), cfr_ldc_27()));
        arrayList.add(AccessorProperty.create("exit", 2, cfr_ldc_28(), cfr_ldc_29()));
        arrayList.add(AccessorProperty.create("quit", 2, cfr_ldc_30(), cfr_ldc_31()));
        arrayList.add(AccessorProperty.create("NaN", 7, cfr_ldc_32(), null));
        arrayList.add(AccessorProperty.create("Infinity", 7, cfr_ldc_33(), null));
        arrayList.add(AccessorProperty.create("undefined", 7, cfr_ldc_34(), null));
        arrayList.add(AccessorProperty.create("eval", 2, cfr_ldc_35(), cfr_ldc_36()));
        arrayList.add(AccessorProperty.create("Object", 2, cfr_ldc_37(), cfr_ldc_38()));
        arrayList.add(AccessorProperty.create("Function", 2, cfr_ldc_39(), cfr_ldc_40()));
        arrayList.add(AccessorProperty.create("Array", 2, cfr_ldc_41(), cfr_ldc_42()));
        arrayList.add(AccessorProperty.create("String", 2, cfr_ldc_43(), cfr_ldc_44()));
        arrayList.add(AccessorProperty.create("Boolean", 2, cfr_ldc_45(), cfr_ldc_46()));
        arrayList.add(AccessorProperty.create("Number", 2, cfr_ldc_47(), cfr_ldc_48()));
        arrayList.add(AccessorProperty.create("Math", 2, cfr_ldc_49(), cfr_ldc_50()));
        arrayList.add(AccessorProperty.create("Error", 2, cfr_ldc_51(), cfr_ldc_52()));
        arrayList.add(AccessorProperty.create("ReferenceError", 2, cfr_ldc_53(), cfr_ldc_54()));
        arrayList.add(AccessorProperty.create("SyntaxError", 2, cfr_ldc_55(), cfr_ldc_56()));
        arrayList.add(AccessorProperty.create("TypeError", 2, cfr_ldc_57(), cfr_ldc_58()));
        arrayList.add(AccessorProperty.create("Packages", 2, cfr_ldc_59(), cfr_ldc_60()));
        arrayList.add(AccessorProperty.create("com", 2, cfr_ldc_61(), cfr_ldc_62()));
        arrayList.add(AccessorProperty.create("edu", 2, cfr_ldc_63(), cfr_ldc_64()));
        arrayList.add(AccessorProperty.create("java", 2, cfr_ldc_65(), cfr_ldc_66()));
        arrayList.add(AccessorProperty.create("javafx", 2, cfr_ldc_67(), cfr_ldc_68()));
        arrayList.add(AccessorProperty.create("javax", 2, cfr_ldc_69(), cfr_ldc_70()));
        arrayList.add(AccessorProperty.create("org", 2, cfr_ldc_71(), cfr_ldc_72()));
        arrayList.add(AccessorProperty.create("__FILE__", 7, cfr_ldc_73(), null));
        arrayList.add(AccessorProperty.create("__DIR__", 7, cfr_ldc_74(), null));
        arrayList.add(AccessorProperty.create("__LINE__", 7, cfr_ldc_75(), null));
        arrayList.add(AccessorProperty.create("Date", 2, cfr_ldc_76(), cfr_ldc_77()));
        arrayList.add(AccessorProperty.create("RegExp", 2, cfr_ldc_78(), cfr_ldc_79()));
        arrayList.add(AccessorProperty.create("JSON", 2, cfr_ldc_80(), cfr_ldc_81()));
        arrayList.add(AccessorProperty.create("JSAdapter", 2, cfr_ldc_82(), cfr_ldc_83()));
        arrayList.add(AccessorProperty.create("EvalError", 2, cfr_ldc_84(), cfr_ldc_85()));
        arrayList.add(AccessorProperty.create("RangeError", 2, cfr_ldc_86(), cfr_ldc_87()));
        arrayList.add(AccessorProperty.create("URIError", 2, cfr_ldc_88(), cfr_ldc_89()));
        arrayList.add(AccessorProperty.create("ArrayBuffer", 2, cfr_ldc_90(), cfr_ldc_91()));
        arrayList.add(AccessorProperty.create("DataView", 2, cfr_ldc_92(), cfr_ldc_93()));
        arrayList.add(AccessorProperty.create("Int8Array", 2, cfr_ldc_94(), cfr_ldc_95()));
        arrayList.add(AccessorProperty.create("Uint8Array", 2, cfr_ldc_96(), cfr_ldc_97()));
        arrayList.add(AccessorProperty.create("Uint8ClampedArray", 2, cfr_ldc_98(), cfr_ldc_99()));
        arrayList.add(AccessorProperty.create("Int16Array", 2, cfr_ldc_100(), cfr_ldc_101()));
        arrayList.add(AccessorProperty.create("Uint16Array", 2, cfr_ldc_102(), cfr_ldc_103()));
        arrayList.add(AccessorProperty.create("Int32Array", 2, cfr_ldc_104(), cfr_ldc_105()));
        arrayList.add(AccessorProperty.create("Uint32Array", 2, cfr_ldc_106(), cfr_ldc_107()));
        arrayList.add(AccessorProperty.create("Float32Array", 2, cfr_ldc_108(), cfr_ldc_109()));
        arrayList.add(AccessorProperty.create("Float64Array", 2, cfr_ldc_110(), cfr_ldc_111()));
        arrayList.add(AccessorProperty.create("JavaImporter", 2, cfr_ldc_112(), cfr_ldc_113()));
        arrayList.add(AccessorProperty.create("Java", 2, cfr_ldc_114(), cfr_ldc_115()));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    public Object G$arguments() {
        return this.arguments;
    }

    public void S$arguments(Object object) {
        this.arguments = object;
    }

    public Object G$parseInt() {
        return this.parseInt;
    }

    public void S$parseInt(Object object) {
        this.parseInt = object;
    }

    public Object G$parseFloat() {
        return this.parseFloat;
    }

    public void S$parseFloat(Object object) {
        this.parseFloat = object;
    }

    public Object G$isNaN() {
        return this.isNaN;
    }

    public void S$isNaN(Object object) {
        this.isNaN = object;
    }

    public Object G$isFinite() {
        return this.isFinite;
    }

    public void S$isFinite(Object object) {
        this.isFinite = object;
    }

    public Object G$encodeURI() {
        return this.encodeURI;
    }

    public void S$encodeURI(Object object) {
        this.encodeURI = object;
    }

    public Object G$encodeURIComponent() {
        return this.encodeURIComponent;
    }

    public void S$encodeURIComponent(Object object) {
        this.encodeURIComponent = object;
    }

    public Object G$decodeURI() {
        return this.decodeURI;
    }

    public void S$decodeURI(Object object) {
        this.decodeURI = object;
    }

    public Object G$decodeURIComponent() {
        return this.decodeURIComponent;
    }

    public void S$decodeURIComponent(Object object) {
        this.decodeURIComponent = object;
    }

    public Object G$escape() {
        return this.escape;
    }

    public void S$escape(Object object) {
        this.escape = object;
    }

    public Object G$unescape() {
        return this.unescape;
    }

    public void S$unescape(Object object) {
        this.unescape = object;
    }

    public Object G$print() {
        return this.print;
    }

    public void S$print(Object object) {
        this.print = object;
    }

    public Object G$load() {
        return this.load;
    }

    public void S$load(Object object) {
        this.load = object;
    }

    public Object G$loadWithNewGlobal() {
        return this.loadWithNewGlobal;
    }

    public void S$loadWithNewGlobal(Object object) {
        this.loadWithNewGlobal = object;
    }

    public Object G$exit() {
        return this.exit;
    }

    public void S$exit(Object object) {
        this.exit = object;
    }

    public Object G$quit() {
        return this.quit;
    }

    public void S$quit(Object object) {
        this.quit = object;
    }

    public double G$NaN() {
        return NaN;
    }

    public double G$Infinity() {
        return Infinity;
    }

    public Object G$undefined() {
        return undefined;
    }

    public Object G$eval() {
        return this.eval;
    }

    public void S$eval(Object object) {
        this.eval = object;
    }

    public Object G$object() {
        return this.object;
    }

    public void S$object(Object object) {
        this.object = object;
    }

    public Object G$function() {
        return this.function;
    }

    public void S$function(Object object) {
        this.function = object;
    }

    public Object G$array() {
        return this.array;
    }

    public void S$array(Object object) {
        this.array = object;
    }

    public Object G$string() {
        return this.string;
    }

    public void S$string(Object object) {
        this.string = object;
    }

    public Object G$_boolean() {
        return this._boolean;
    }

    public void S$_boolean(Object object) {
        this._boolean = object;
    }

    public Object G$number() {
        return this.number;
    }

    public void S$number(Object object) {
        this.number = object;
    }

    public Object G$math() {
        return this.math;
    }

    public void S$math(Object object) {
        this.math = object;
    }

    public Object G$error() {
        return this.error;
    }

    public void S$error(Object object) {
        this.error = object;
    }

    public Object G$referenceError() {
        return this.referenceError;
    }

    public void S$referenceError(Object object) {
        this.referenceError = object;
    }

    public Object G$syntaxError() {
        return this.syntaxError;
    }

    public void S$syntaxError(Object object) {
        this.syntaxError = object;
    }

    public Object G$typeError() {
        return this.typeError;
    }

    public void S$typeError(Object object) {
        this.typeError = object;
    }

    public Object G$packages() {
        return this.packages;
    }

    public void S$packages(Object object) {
        this.packages = object;
    }

    public Object G$com() {
        return this.com;
    }

    public void S$com(Object object) {
        this.com = object;
    }

    public Object G$edu() {
        return this.edu;
    }

    public void S$edu(Object object) {
        this.edu = object;
    }

    public Object G$java() {
        return this.java;
    }

    public void S$java(Object object) {
        this.java = object;
    }

    public Object G$javafx() {
        return this.javafx;
    }

    public void S$javafx(Object object) {
        this.javafx = object;
    }

    public Object G$javax() {
        return this.javax;
    }

    public void S$javax(Object object) {
        this.javax = object;
    }

    public Object G$org() {
        return this.org;
    }

    public void S$org(Object object) {
        this.org = object;
    }

    public Object G$__FILE__() {
        return __FILE__;
    }

    public Object G$__DIR__() {
        return __DIR__;
    }

    public Object G$__LINE__() {
        return __LINE__;
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$arguments", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(Global.class, "S$arguments", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(Global.class, "G$parseInt", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(Global.class, "S$parseInt", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(Global.class, "G$parseFloat", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(Global.class, "S$parseFloat", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findVirtual(Global.class, "G$isNaN", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(Global.class, "S$isNaN", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_8() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$isFinite", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_9() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$isFinite", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_10() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$encodeURI", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_11() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$encodeURI", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_12() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$encodeURIComponent", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_13() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$encodeURIComponent", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_14() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$decodeURI", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_15() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$decodeURI", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_16() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$decodeURIComponent", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_17() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$decodeURIComponent", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_18() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$escape", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_19() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$escape", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_20() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$unescape", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_21() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$unescape", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_22() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$print", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_23() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$print", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_24() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$load", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_25() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$load", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_26() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$loadWithNewGlobal", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_27() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$loadWithNewGlobal", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_28() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$exit", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_29() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$exit", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_30() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$quit", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_31() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$quit", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_32() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$NaN", MethodType.fromMethodDescriptorString("()D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_33() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$Infinity", MethodType.fromMethodDescriptorString("()D", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_34() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$undefined", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_35() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$eval", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_36() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$eval", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_37() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$object", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_38() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$object", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_39() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$function", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_40() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$function", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_41() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$array", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_42() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$array", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_43() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$string", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_44() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$string", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_45() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$_boolean", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_46() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$_boolean", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_47() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$number", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_48() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$number", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_49() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$math", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_50() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$math", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_51() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$error", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_52() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$error", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_53() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$referenceError", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_54() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$referenceError", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_55() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$syntaxError", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_56() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$syntaxError", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_57() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$typeError", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_58() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$typeError", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_59() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$packages", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_60() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$packages", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_61() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$com", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_62() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$com", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_63() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$edu", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_64() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$edu", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_65() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$java", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_66() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$java", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_67() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$javafx", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_68() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$javafx", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_69() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$javax", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_70() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$javax", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_71() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$org", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_72() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "S$org", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_73() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$__FILE__", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_74() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$__DIR__", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_75() {
        try {
            return MethodHandles.lookup().findVirtual(Global.class, "G$__LINE__", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_76() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "getDate", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_77() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "setDate", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_78() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "getRegExp", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_79() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "setRegExp", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_80() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "getJSON", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_81() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "setJSON", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_82() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "getJSAdapter", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_83() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "setJSAdapter", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_84() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "getEvalError", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_85() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "setEvalError", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_86() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "getRangeError", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_87() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "setRangeError", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_88() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "getURIError", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_89() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "setURIError", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_90() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "getArrayBuffer", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_91() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "setArrayBuffer", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_92() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "getDataView", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_93() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "setDataView", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_94() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "getInt8Array", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_95() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "setInt8Array", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_96() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "getUint8Array", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_97() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "setUint8Array", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_98() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "getUint8ClampedArray", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_99() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "setUint8ClampedArray", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_100() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "getInt16Array", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_101() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "setInt16Array", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_102() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "getUint16Array", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_103() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "setUint16Array", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_104() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "getInt32Array", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_105() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "setInt32Array", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_106() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "getUint32Array", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_107() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "setUint32Array", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_108() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "getFloat32Array", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_109() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "setFloat32Array", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_110() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "getFloat64Array", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_111() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "setFloat64Array", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_112() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "getJavaImporter", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_113() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "setJavaImporter", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_114() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "getJavaApi", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_115() {
        try {
            return MethodHandles.lookup().findStatic(Global.class, "setJavaApi", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;Ljava/lang/Object;)V", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }

    private static class LexicalScope
    extends ScriptObject {
        LexicalScope(Global global) {
            super(global, PropertyMap.newMap());
        }

        @Override
        protected GuardedInvocation findGetMethod(CallSiteDescriptor desc, LinkRequest request, String operator) {
            return LexicalScope.filterInvocation(super.findGetMethod(desc, request, operator));
        }

        @Override
        protected GuardedInvocation findSetMethod(CallSiteDescriptor desc, LinkRequest request) {
            return LexicalScope.filterInvocation(super.findSetMethod(desc, request));
        }

        @Override
        protected PropertyMap addBoundProperty(PropertyMap propMap, ScriptObject source, Property property, boolean extensible) {
            return super.addBoundProperty(propMap, source, property, extensible);
        }

        private static GuardedInvocation filterInvocation(GuardedInvocation invocation) {
            MethodType type = invocation.getInvocation().type();
            return invocation.asType(type.changeParameterType(0, Object.class)).filterArguments(0, LEXICAL_SCOPE_FILTER);
        }
    }
}

