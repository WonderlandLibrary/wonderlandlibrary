/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.scripts.JS;

final class DebuggerSupport {
    static boolean FORCELOAD = true;

    DebuggerSupport() {
    }

    static void notifyInvoke(MethodHandle mh) {
    }

    static SourceInfo getSourceInfo(Class<?> clazz) {
        if (JS.class.isAssignableFrom(clazz)) {
            try {
                Field sourceField = clazz.getDeclaredField(CompilerConstants.SOURCE.symbolName());
                sourceField.setAccessible(true);
                Source src = (Source)sourceField.get(null);
                return src.getSourceInfo();
            }
            catch (IllegalAccessException | NoSuchFieldException ignored) {
                return null;
            }
        }
        return null;
    }

    static Object getGlobal() {
        return Context.getGlobal();
    }

    static Object eval(ScriptObject scope, Object self, String string, boolean returnException) {
        Global global = Context.getGlobal();
        ScriptObject initialScope = scope != null ? scope : global;
        Object callThis = self != null ? self : global;
        Context context = ((ScriptObject)global).getContext();
        try {
            return context.eval(initialScope, string, callThis, ScriptRuntime.UNDEFINED);
        }
        catch (Throwable ex) {
            return returnException ? ex : null;
        }
    }

    static DebuggerValueDesc[] valueInfos(Object object, boolean all) {
        assert (object instanceof ScriptObject);
        return DebuggerSupport.getDebuggerValueDescs((ScriptObject)object, all, new HashSet<Object>());
    }

    static DebuggerValueDesc valueInfo(String name, Object value, boolean all) {
        return DebuggerSupport.valueInfo(name, value, all, new HashSet<Object>());
    }

    private static DebuggerValueDesc valueInfo(String name, Object value, boolean all, Set<Object> duplicates) {
        if (value instanceof ScriptObject && !(value instanceof ScriptFunction)) {
            ScriptObject object = (ScriptObject)value;
            return new DebuggerValueDesc(name, !object.isEmpty(), value, DebuggerSupport.objectAsString(object, all, duplicates));
        }
        return new DebuggerValueDesc(name, false, value, DebuggerSupport.valueAsString(value));
    }

    private static DebuggerValueDesc[] getDebuggerValueDescs(ScriptObject object, boolean all, Set<Object> duplicates) {
        if (duplicates.contains(object)) {
            return null;
        }
        duplicates.add(object);
        String[] keys2 = object.getOwnKeys(all);
        DebuggerValueDesc[] descs = new DebuggerValueDesc[keys2.length];
        for (int i = 0; i < keys2.length; ++i) {
            String key = keys2[i];
            descs[i] = DebuggerSupport.valueInfo(key, object.get(key), all, duplicates);
        }
        duplicates.remove(object);
        return descs;
    }

    private static String objectAsString(ScriptObject object, boolean all, Set<Object> duplicates) {
        StringBuilder sb = new StringBuilder();
        if (ScriptObject.isArray(object)) {
            sb.append('[');
            long length = (long)object.getDouble("length", -1);
            for (long i = 0L; i < length; ++i) {
                if (object.has(i)) {
                    boolean isUndefined;
                    Object valueAsObject = object.get(i);
                    boolean bl = isUndefined = valueAsObject == ScriptRuntime.UNDEFINED;
                    if (isUndefined) {
                        if (i == 0L) continue;
                        sb.append(",");
                        continue;
                    }
                    if (i != 0L) {
                        sb.append(", ");
                    }
                    if (valueAsObject instanceof ScriptObject && !(valueAsObject instanceof ScriptFunction)) {
                        String objectString = DebuggerSupport.objectAsString((ScriptObject)valueAsObject, all, duplicates);
                        sb.append(objectString != null ? objectString : "{...}");
                        continue;
                    }
                    sb.append(DebuggerSupport.valueAsString(valueAsObject));
                    continue;
                }
                if (i == 0L) continue;
                sb.append(',');
            }
            sb.append(']');
        } else {
            sb.append('{');
            DebuggerValueDesc[] descs = DebuggerSupport.getDebuggerValueDescs(object, all, duplicates);
            if (descs != null) {
                for (int i = 0; i < descs.length; ++i) {
                    if (i != 0) {
                        sb.append(", ");
                    }
                    String valueAsString = descs[i].valueAsString;
                    sb.append(descs[i].key);
                    sb.append(": ");
                    sb.append(valueAsString);
                }
            }
            sb.append('}');
        }
        return sb.toString();
    }

    static String valueAsString(Object value) {
        JSType type = JSType.of(value);
        switch (type) {
            case BOOLEAN: {
                return value.toString();
            }
            case STRING: {
                return DebuggerSupport.escape(value.toString());
            }
            case NUMBER: {
                return JSType.toString(((Number)value).doubleValue());
            }
            case NULL: {
                return "null";
            }
            case UNDEFINED: {
                return "undefined";
            }
            case OBJECT: {
                return ScriptRuntime.safeToString(value);
            }
            case FUNCTION: {
                if (value instanceof ScriptFunction) {
                    return ((ScriptFunction)value).toSource();
                }
                return value.toString();
            }
        }
        return value.toString();
    }

    private static String escape(String value) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        block10: for (char ch : value.toCharArray()) {
            switch (ch) {
                case '\\': {
                    sb.append("\\\\");
                    continue block10;
                }
                case '\"': {
                    sb.append("\\\"");
                    continue block10;
                }
                case '\'': {
                    sb.append("\\'");
                    continue block10;
                }
                case '\b': {
                    sb.append("\\b");
                    continue block10;
                }
                case '\f': {
                    sb.append("\\f");
                    continue block10;
                }
                case '\n': {
                    sb.append("\\n");
                    continue block10;
                }
                case '\r': {
                    sb.append("\\r");
                    continue block10;
                }
                case '\t': {
                    sb.append("\\t");
                    continue block10;
                }
                default: {
                    if (ch < ' ' || ch >= '\u00ff') {
                        sb.append("\\u");
                        String hex = Integer.toHexString(ch);
                        for (int i = hex.length(); i < 4; ++i) {
                            sb.append('0');
                        }
                        sb.append(hex);
                        continue block10;
                    }
                    sb.append(ch);
                }
            }
        }
        sb.append("\"");
        return sb.toString();
    }

    static {
        DebuggerValueDesc forceLoad = new DebuggerValueDesc(null, false, null, null);
        SourceInfo sourceInfo = new SourceInfo(null, 0, null, null);
    }

    static class SourceInfo {
        final String name;
        final URL url;
        final int hash;
        final char[] content;

        SourceInfo(String name, int hash, URL url, char[] content) {
            this.name = name;
            this.hash = hash;
            this.url = url;
            this.content = content;
        }
    }

    static class DebuggerValueDesc {
        final String key;
        final boolean expandable;
        final Object valueAsObject;
        final String valueAsString;

        DebuggerValueDesc(String key, boolean expandable, Object valueAsObject, String valueAsString) {
            this.key = key;
            this.expandable = expandable;
            this.valueAsObject = valueAsObject;
            this.valueAsString = valueAsString;
        }
    }
}

