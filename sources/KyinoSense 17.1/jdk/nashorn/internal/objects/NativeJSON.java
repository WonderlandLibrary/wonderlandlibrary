/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.objects.NativeBoolean;
import jdk.nashorn.internal.objects.NativeNumber;
import jdk.nashorn.internal.objects.NativeString;
import jdk.nashorn.internal.runtime.ConsString;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.JSONFunctions;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.linker.InvokeByName;

public final class NativeJSON
extends ScriptObject {
    private static final Object TO_JSON = new Object();
    private static final Object JSOBJECT_INVOKER = new Object();
    private static final Object REPLACER_INVOKER = new Object();
    private static PropertyMap $nasgenmap$;

    private static InvokeByName getTO_JSON() {
        return Global.instance().getInvokeByName(TO_JSON, new Callable<InvokeByName>(){

            @Override
            public InvokeByName call() {
                return new InvokeByName("toJSON", ScriptObject.class, Object.class, Object.class);
            }
        });
    }

    private static MethodHandle getJSOBJECT_INVOKER() {
        return Global.instance().getDynamicInvoker(JSOBJECT_INVOKER, new Callable<MethodHandle>(){

            @Override
            public MethodHandle call() {
                return Bootstrap.createDynamicInvoker("dyn:call", Object.class, Object.class, Object.class);
            }
        });
    }

    private static MethodHandle getREPLACER_INVOKER() {
        return Global.instance().getDynamicInvoker(REPLACER_INVOKER, new Callable<MethodHandle>(){

            @Override
            public MethodHandle call() {
                return Bootstrap.createDynamicInvoker("dyn:call", Object.class, Object.class, Object.class, Object.class, Object.class);
            }
        });
    }

    private NativeJSON() {
        throw new UnsupportedOperationException();
    }

    public static Object parse(Object self, Object text, Object reviver) {
        return JSONFunctions.parse(text, reviver);
    }

    public static Object stringify(Object self, Object value, Object replacer, Object space) {
        String gap;
        StringifyState state = new StringifyState();
        if (Bootstrap.isCallable(replacer)) {
            state.replacerFunction = replacer;
        } else if (NativeJSON.isArray(replacer) || NativeJSON.isJSObjectArray(replacer) || replacer instanceof Iterable || replacer != null && replacer.getClass().isArray()) {
            state.propertyList = new ArrayList<String>();
            ArrayLikeIterator<Object> iter = ArrayLikeIterator.arrayLikeIterator(replacer);
            while (iter.hasNext()) {
                String item = null;
                Object v = iter.next();
                if (v instanceof String) {
                    item = (String)v;
                } else if (v instanceof ConsString) {
                    item = v.toString();
                } else if (v instanceof Number || v instanceof NativeNumber || v instanceof NativeString) {
                    item = JSType.toString(v);
                }
                if (item == null) continue;
                state.propertyList.add(item);
            }
        }
        Object modSpace = space;
        if (modSpace instanceof NativeNumber) {
            modSpace = JSType.toNumber(JSType.toPrimitive(modSpace, Number.class));
        } else if (modSpace instanceof NativeString) {
            modSpace = JSType.toString(JSType.toPrimitive(modSpace, String.class));
        }
        if (modSpace instanceof Number) {
            int indent = Math.min(10, JSType.toInteger(modSpace));
            if (indent < 1) {
                gap = "";
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < indent; ++i) {
                    sb.append(' ');
                }
                gap = sb.toString();
            }
        } else if (JSType.isString(modSpace)) {
            String str = modSpace.toString();
            gap = str.substring(0, Math.min(10, str.length()));
        } else {
            gap = "";
        }
        state.gap = gap;
        ScriptObject wrapper = Global.newEmptyInstance();
        wrapper.set((Object)"", value, 0);
        return NativeJSON.str("", wrapper, state);
    }

    private static Object str(Object key, Object holder, StringifyState state) {
        assert (holder instanceof ScriptObject || holder instanceof JSObject);
        Object value = NativeJSON.getProperty(holder, key);
        try {
            JSObject jsObj;
            Object toJSON;
            if (value instanceof ScriptObject) {
                InvokeByName toJSONInvoker = NativeJSON.getTO_JSON();
                ScriptObject svalue = (ScriptObject)value;
                Object toJSON2 = toJSONInvoker.getGetter().invokeExact(svalue);
                if (Bootstrap.isCallable(toJSON2)) {
                    value = toJSONInvoker.getInvoker().invokeExact(toJSON2, svalue, key);
                }
            } else if (value instanceof JSObject && Bootstrap.isCallable(toJSON = (jsObj = (JSObject)value).getMember("toJSON"))) {
                value = NativeJSON.getJSOBJECT_INVOKER().invokeExact(toJSON, value);
            }
            if (state.replacerFunction != null) {
                value = NativeJSON.getREPLACER_INVOKER().invokeExact(state.replacerFunction, holder, key, value);
            }
        }
        catch (Error | RuntimeException t) {
            throw t;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
        boolean isObj = value instanceof ScriptObject;
        if (isObj) {
            if (value instanceof NativeNumber) {
                value = JSType.toNumber(value);
            } else if (value instanceof NativeString) {
                value = JSType.toString(value);
            } else if (value instanceof NativeBoolean) {
                value = ((NativeBoolean)value).booleanValue();
            }
        }
        if (value == null) {
            return "null";
        }
        if (Boolean.TRUE.equals(value)) {
            return "true";
        }
        if (Boolean.FALSE.equals(value)) {
            return "false";
        }
        if (value instanceof String) {
            return JSONFunctions.quote((String)value);
        }
        if (value instanceof ConsString) {
            return JSONFunctions.quote(value.toString());
        }
        if (value instanceof Number) {
            return JSType.isFinite(((Number)value).doubleValue()) ? JSType.toString(value) : "null";
        }
        JSType type = JSType.of(value);
        if (type == JSType.OBJECT) {
            if (NativeJSON.isArray(value) || NativeJSON.isJSObjectArray(value)) {
                return NativeJSON.JA(value, state);
            }
            if (value instanceof ScriptObject || value instanceof JSObject) {
                return NativeJSON.JO(value, state);
            }
        }
        return ScriptRuntime.UNDEFINED;
    }

    private static String JO(Object value, StringifyState state) {
        assert (value instanceof ScriptObject || value instanceof JSObject);
        if (state.stack.containsKey(value)) {
            throw ECMAErrors.typeError("JSON.stringify.cyclic", new String[0]);
        }
        state.stack.put(value, value);
        StringBuilder stepback = new StringBuilder(state.indent.toString());
        state.indent.append(state.gap);
        StringBuilder finalStr = new StringBuilder();
        ArrayList<StringBuilder> partial = new ArrayList<StringBuilder>();
        List<String> k = state.propertyList == null ? Arrays.asList(NativeJSON.getOwnKeys(value)) : state.propertyList;
        for (String p : k) {
            Object strP = NativeJSON.str(p, value, state);
            if (strP == ScriptRuntime.UNDEFINED) continue;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(JSONFunctions.quote(p.toString())).append(':');
            if (!state.gap.isEmpty()) {
                stringBuilder.append(' ');
            }
            stringBuilder.append(strP);
            partial.add(stringBuilder);
        }
        if (partial.isEmpty()) {
            finalStr.append("{}");
        } else if (state.gap.isEmpty()) {
            int size = partial.size();
            int index = 0;
            finalStr.append('{');
            for (Object e : partial) {
                finalStr.append(e);
                if (index < size - 1) {
                    finalStr.append(',');
                }
                ++index;
            }
            finalStr.append('}');
        } else {
            int size = partial.size();
            int index = 0;
            finalStr.append("{\n");
            finalStr.append((CharSequence)state.indent);
            for (Object e : partial) {
                finalStr.append(e);
                if (index < size - 1) {
                    finalStr.append(",\n");
                    finalStr.append((CharSequence)state.indent);
                }
                ++index;
            }
            finalStr.append('\n');
            finalStr.append((CharSequence)stepback);
            finalStr.append('}');
        }
        state.stack.remove(value);
        state.indent = stepback;
        return finalStr.toString();
    }

    private static Object JA(Object value, StringifyState state) {
        int index;
        assert (value instanceof ScriptObject || value instanceof JSObject);
        if (state.stack.containsKey(value)) {
            throw ECMAErrors.typeError("JSON.stringify.cyclic", new String[0]);
        }
        state.stack.put(value, value);
        StringBuilder stepback = new StringBuilder(state.indent.toString());
        state.indent.append(state.gap);
        ArrayList<Object> partial = new ArrayList<Object>();
        int length = JSType.toInteger(NativeJSON.getLength(value));
        for (index = 0; index < length; ++index) {
            Object strP = NativeJSON.str(index, value, state);
            if (strP == ScriptRuntime.UNDEFINED) {
                strP = "null";
            }
            partial.add(strP);
        }
        StringBuilder finalStr = new StringBuilder();
        if (partial.isEmpty()) {
            finalStr.append("[]");
        } else if (state.gap.isEmpty()) {
            int size = partial.size();
            index = 0;
            finalStr.append('[');
            for (Object e : partial) {
                finalStr.append(e);
                if (index < size - 1) {
                    finalStr.append(',');
                }
                ++index;
            }
            finalStr.append(']');
        } else {
            int size = partial.size();
            index = 0;
            finalStr.append("[\n");
            finalStr.append((CharSequence)state.indent);
            for (Object e : partial) {
                finalStr.append(e);
                if (index < size - 1) {
                    finalStr.append(",\n");
                    finalStr.append((CharSequence)state.indent);
                }
                ++index;
            }
            finalStr.append('\n');
            finalStr.append((CharSequence)stepback);
            finalStr.append(']');
        }
        state.stack.remove(value);
        state.indent = stepback;
        return finalStr.toString();
    }

    private static String[] getOwnKeys(Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).getOwnKeys(false);
        }
        if (obj instanceof ScriptObjectMirror) {
            return ((ScriptObjectMirror)obj).getOwnKeys(false);
        }
        if (obj instanceof JSObject) {
            return ((JSObject)obj).keySet().toArray(new String[0]);
        }
        throw new AssertionError((Object)"should not reach here");
    }

    private static Object getLength(Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).getLength();
        }
        if (obj instanceof JSObject) {
            return ((JSObject)obj).getMember("length");
        }
        throw new AssertionError((Object)"should not reach here");
    }

    private static boolean isJSObjectArray(Object obj) {
        return obj instanceof JSObject && ((JSObject)obj).isArray();
    }

    private static Object getProperty(Object holder, Object key) {
        if (holder instanceof ScriptObject) {
            return ((ScriptObject)holder).get(key);
        }
        if (holder instanceof JSObject) {
            JSObject jsObj = (JSObject)holder;
            if (key instanceof Integer) {
                return jsObj.getSlot((Integer)key);
            }
            return jsObj.getMember(Objects.toString(key));
        }
        return new AssertionError((Object)"should not reach here");
    }

    static {
        NativeJSON.$clinit$();
    }

    public static void $clinit$() {
        $nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }

    private static class StringifyState {
        final Map<Object, Object> stack = new IdentityHashMap<Object, Object>();
        StringBuilder indent = new StringBuilder();
        String gap = "";
        List<String> propertyList = null;
        Object replacerFunction = null;

        private StringifyState() {
        }
    }
}

