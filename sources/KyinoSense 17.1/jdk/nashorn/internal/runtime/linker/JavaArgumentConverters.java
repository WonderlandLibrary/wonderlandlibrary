/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import jdk.internal.dynalink.support.TypeUtilities;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.ConsString;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;

final class JavaArgumentConverters {
    private static final MethodHandle TO_BOOLEAN = JavaArgumentConverters.findOwnMH("toBoolean", Boolean.class, Object.class);
    private static final MethodHandle TO_STRING = JavaArgumentConverters.findOwnMH("toString", String.class, Object.class);
    private static final MethodHandle TO_DOUBLE = JavaArgumentConverters.findOwnMH("toDouble", Double.class, Object.class);
    private static final MethodHandle TO_NUMBER = JavaArgumentConverters.findOwnMH("toNumber", Number.class, Object.class);
    private static final MethodHandle TO_LONG = JavaArgumentConverters.findOwnMH("toLong", Long.class, Object.class);
    private static final MethodHandle TO_LONG_PRIMITIVE = JavaArgumentConverters.findOwnMH("toLongPrimitive", Long.TYPE, Object.class);
    private static final MethodHandle TO_CHAR = JavaArgumentConverters.findOwnMH("toChar", Character.class, Object.class);
    private static final MethodHandle TO_CHAR_PRIMITIVE = JavaArgumentConverters.findOwnMH("toCharPrimitive", Character.TYPE, Object.class);
    private static final Map<Class<?>, MethodHandle> CONVERTERS = new HashMap();

    private JavaArgumentConverters() {
    }

    static MethodHandle getConverter(Class<?> targetType) {
        return CONVERTERS.get(targetType);
    }

    private static Boolean toBoolean(Object obj) {
        if (obj instanceof Boolean) {
            return (Boolean)obj;
        }
        if (obj == null) {
            return null;
        }
        if (obj == ScriptRuntime.UNDEFINED) {
            return null;
        }
        if (obj instanceof Number) {
            double num = ((Number)obj).doubleValue();
            return num != 0.0 && !Double.isNaN(num);
        }
        if (JSType.isString(obj)) {
            return ((CharSequence)obj).length() > 0;
        }
        if (obj instanceof ScriptObject) {
            return true;
        }
        throw JavaArgumentConverters.assertUnexpectedType(obj);
    }

    private static Character toChar(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            int ival = ((Number)o).intValue();
            if (ival >= 0 && ival <= 65535) {
                return Character.valueOf((char)ival);
            }
            throw ECMAErrors.typeError("cant.convert.number.to.char", new String[0]);
        }
        String s = JavaArgumentConverters.toString(o);
        if (s == null) {
            return null;
        }
        if (s.length() != 1) {
            throw ECMAErrors.typeError("cant.convert.string.to.char", new String[0]);
        }
        return Character.valueOf(s.charAt(0));
    }

    static char toCharPrimitive(Object obj0) {
        Character c = JavaArgumentConverters.toChar(obj0);
        return c == null ? (char)'\u0000' : c.charValue();
    }

    static String toString(Object obj) {
        return obj == null ? null : JSType.toString(obj);
    }

    private static Double toDouble(Object obj0) {
        Object obj = obj0;
        while (true) {
            if (obj == null) {
                return null;
            }
            if (obj instanceof Double) {
                return (Double)obj;
            }
            if (obj instanceof Number) {
                return ((Number)obj).doubleValue();
            }
            if (obj instanceof String) {
                return JSType.toNumber((String)obj);
            }
            if (obj instanceof ConsString) {
                return JSType.toNumber(obj.toString());
            }
            if (obj instanceof Boolean) {
                return (Boolean)obj != false ? 1.0 : 0.0;
            }
            if (!(obj instanceof ScriptObject)) break;
            obj = JSType.toPrimitive(obj, Number.class);
        }
        if (obj == ScriptRuntime.UNDEFINED) {
            return Double.NaN;
        }
        throw JavaArgumentConverters.assertUnexpectedType(obj);
    }

    private static Number toNumber(Object obj0) {
        Object obj = obj0;
        while (true) {
            if (obj == null) {
                return null;
            }
            if (obj instanceof Number) {
                return (Number)obj;
            }
            if (obj instanceof String) {
                return JSType.toNumber((String)obj);
            }
            if (obj instanceof ConsString) {
                return JSType.toNumber(obj.toString());
            }
            if (obj instanceof Boolean) {
                return (Boolean)obj != false ? 1.0 : 0.0;
            }
            if (!(obj instanceof ScriptObject)) break;
            obj = JSType.toPrimitive(obj, Number.class);
        }
        if (obj == ScriptRuntime.UNDEFINED) {
            return Double.NaN;
        }
        throw JavaArgumentConverters.assertUnexpectedType(obj);
    }

    private static Long toLong(Object obj0) {
        Object obj = obj0;
        while (true) {
            if (obj == null) {
                return null;
            }
            if (obj instanceof Long) {
                return (Long)obj;
            }
            if (obj instanceof Integer) {
                return ((Integer)obj).longValue();
            }
            if (obj instanceof Double) {
                Double d = (Double)obj;
                if (Double.isInfinite(d)) {
                    return 0L;
                }
                return d.longValue();
            }
            if (obj instanceof Float) {
                Float f = (Float)obj;
                if (Float.isInfinite(f.floatValue())) {
                    return 0L;
                }
                return f.longValue();
            }
            if (obj instanceof Number) {
                return ((Number)obj).longValue();
            }
            if (JSType.isString(obj)) {
                return JSType.toLong(obj);
            }
            if (obj instanceof Boolean) {
                return (Boolean)obj != false ? 1L : 0L;
            }
            if (!(obj instanceof ScriptObject)) break;
            obj = JSType.toPrimitive(obj, Number.class);
        }
        if (obj == ScriptRuntime.UNDEFINED) {
            return null;
        }
        throw JavaArgumentConverters.assertUnexpectedType(obj);
    }

    private static AssertionError assertUnexpectedType(Object obj) {
        return new AssertionError((Object)("Unexpected type" + obj.getClass().getName() + ". Guards should have prevented this"));
    }

    private static long toLongPrimitive(Object obj0) {
        Long l = JavaArgumentConverters.toLong(obj0);
        return l == null ? 0L : l;
    }

    private static MethodHandle findOwnMH(String name, Class<?> rtype, Class<?> ... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), JavaArgumentConverters.class, name, Lookup.MH.type(rtype, types));
    }

    private static void putDoubleConverter(Class<?> targetType) {
        Class<?> primitive = TypeUtilities.getPrimitiveType(targetType);
        CONVERTERS.put(primitive, Lookup.MH.explicitCastArguments(JSType.TO_NUMBER.methodHandle(), JSType.TO_NUMBER.methodHandle().type().changeReturnType(primitive)));
        CONVERTERS.put(targetType, Lookup.MH.filterReturnValue(TO_DOUBLE, JavaArgumentConverters.findOwnMH(primitive.getName() + "Value", targetType, Double.class)));
    }

    private static void putLongConverter(Class<?> targetType) {
        Class<?> primitive = TypeUtilities.getPrimitiveType(targetType);
        CONVERTERS.put(primitive, Lookup.MH.explicitCastArguments(TO_LONG_PRIMITIVE, TO_LONG_PRIMITIVE.type().changeReturnType(primitive)));
        CONVERTERS.put(targetType, Lookup.MH.filterReturnValue(TO_LONG, JavaArgumentConverters.findOwnMH(primitive.getName() + "Value", targetType, Long.class)));
    }

    private static Byte byteValue(Long l) {
        return l == null ? null : Byte.valueOf(l.byteValue());
    }

    private static Short shortValue(Long l) {
        return l == null ? null : Short.valueOf(l.shortValue());
    }

    private static Integer intValue(Long l) {
        return l == null ? null : Integer.valueOf(l.intValue());
    }

    private static Float floatValue(Double d) {
        return d == null ? null : Float.valueOf(d.floatValue());
    }

    static {
        CONVERTERS.put(Number.class, TO_NUMBER);
        CONVERTERS.put(String.class, TO_STRING);
        CONVERTERS.put(Boolean.TYPE, JSType.TO_BOOLEAN.methodHandle());
        CONVERTERS.put(Boolean.class, TO_BOOLEAN);
        CONVERTERS.put(Character.TYPE, TO_CHAR_PRIMITIVE);
        CONVERTERS.put(Character.class, TO_CHAR);
        CONVERTERS.put(Double.TYPE, JSType.TO_NUMBER.methodHandle());
        CONVERTERS.put(Double.class, TO_DOUBLE);
        CONVERTERS.put(Long.TYPE, TO_LONG_PRIMITIVE);
        CONVERTERS.put(Long.class, TO_LONG);
        JavaArgumentConverters.putLongConverter(Byte.class);
        JavaArgumentConverters.putLongConverter(Short.class);
        JavaArgumentConverters.putLongConverter(Integer.class);
        JavaArgumentConverters.putDoubleConverter(Float.class);
    }
}

