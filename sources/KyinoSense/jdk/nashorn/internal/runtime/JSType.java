/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.api.scripting.AbstractJSObject;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.Lexer;
import jdk.nashorn.internal.runtime.ConsString;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ECMAException;
import jdk.nashorn.internal.runtime.NumberToString;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.Undefined;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import jdk.nashorn.internal.runtime.linker.Bootstrap;

public final class JSType
extends Enum<JSType> {
    public static final /* enum */ JSType UNDEFINED = new JSType("undefined");
    public static final /* enum */ JSType NULL = new JSType("object");
    public static final /* enum */ JSType BOOLEAN = new JSType("boolean");
    public static final /* enum */ JSType NUMBER = new JSType("number");
    public static final /* enum */ JSType STRING = new JSType("string");
    public static final /* enum */ JSType OBJECT = new JSType("object");
    public static final /* enum */ JSType FUNCTION = new JSType("function");
    private final String typeName;
    public static final long MAX_UINT = 0xFFFFFFFFL;
    private static final MethodHandles.Lookup JSTYPE_LOOKUP;
    public static final CompilerConstants.Call TO_BOOLEAN;
    public static final CompilerConstants.Call TO_BOOLEAN_D;
    public static final CompilerConstants.Call TO_INTEGER;
    public static final CompilerConstants.Call TO_LONG;
    public static final CompilerConstants.Call TO_LONG_D;
    public static final CompilerConstants.Call TO_NUMBER;
    public static final CompilerConstants.Call TO_NUMBER_OPTIMISTIC;
    public static final CompilerConstants.Call TO_STRING;
    public static final CompilerConstants.Call TO_INT32;
    public static final CompilerConstants.Call TO_INT32_L;
    public static final CompilerConstants.Call TO_INT32_OPTIMISTIC;
    public static final CompilerConstants.Call TO_INT32_D;
    public static final CompilerConstants.Call TO_UINT32_OPTIMISTIC;
    public static final CompilerConstants.Call TO_UINT32_DOUBLE;
    public static final CompilerConstants.Call TO_UINT32;
    public static final CompilerConstants.Call TO_UINT32_D;
    public static final CompilerConstants.Call TO_STRING_D;
    public static final CompilerConstants.Call TO_PRIMITIVE_TO_STRING;
    public static final CompilerConstants.Call TO_PRIMITIVE_TO_CHARSEQUENCE;
    public static final CompilerConstants.Call THROW_UNWARRANTED;
    public static final CompilerConstants.Call ADD_EXACT;
    public static final CompilerConstants.Call SUB_EXACT;
    public static final CompilerConstants.Call MUL_EXACT;
    public static final CompilerConstants.Call DIV_EXACT;
    public static final CompilerConstants.Call DIV_ZERO;
    public static final CompilerConstants.Call REM_ZERO;
    public static final CompilerConstants.Call REM_EXACT;
    public static final CompilerConstants.Call DECREMENT_EXACT;
    public static final CompilerConstants.Call INCREMENT_EXACT;
    public static final CompilerConstants.Call NEGATE_EXACT;
    public static final CompilerConstants.Call TO_JAVA_ARRAY;
    public static final CompilerConstants.Call VOID_RETURN;
    public static final CompilerConstants.Call IS_STRING;
    public static final CompilerConstants.Call IS_NUMBER;
    private static final List<Type> ACCESSOR_TYPES;
    public static final int TYPE_UNDEFINED_INDEX = -1;
    public static final int TYPE_INT_INDEX = 0;
    public static final int TYPE_DOUBLE_INDEX = 1;
    public static final int TYPE_OBJECT_INDEX = 2;
    public static final List<MethodHandle> CONVERT_OBJECT;
    public static final List<MethodHandle> CONVERT_OBJECT_OPTIMISTIC;
    public static final int UNDEFINED_INT = 0;
    public static final long UNDEFINED_LONG = 0L;
    public static final double UNDEFINED_DOUBLE = Double.NaN;
    private static final long MAX_PRECISE_DOUBLE = 0x20000000000000L;
    private static final long MIN_PRECISE_DOUBLE = -9007199254740992L;
    public static final List<MethodHandle> GET_UNDEFINED;
    private static final double INT32_LIMIT = 4.294967296E9;
    private static final /* synthetic */ JSType[] $VALUES;

    public static JSType[] values() {
        return (JSType[])$VALUES.clone();
    }

    public static JSType valueOf(String name) {
        return Enum.valueOf(JSType.class, name);
    }

    private JSType(String typeName) {
        this.typeName = typeName;
    }

    public final String typeName() {
        return this.typeName;
    }

    public static JSType of(Object obj) {
        if (obj == null) {
            return NULL;
        }
        if (obj instanceof ScriptObject) {
            return obj instanceof ScriptFunction ? FUNCTION : OBJECT;
        }
        if (obj instanceof Boolean) {
            return BOOLEAN;
        }
        if (JSType.isString(obj)) {
            return STRING;
        }
        if (JSType.isNumber(obj)) {
            return NUMBER;
        }
        if (obj == ScriptRuntime.UNDEFINED) {
            return UNDEFINED;
        }
        return Bootstrap.isCallable(obj) ? FUNCTION : OBJECT;
    }

    public static JSType ofNoFunction(Object obj) {
        if (obj == null) {
            return NULL;
        }
        if (obj instanceof ScriptObject) {
            return OBJECT;
        }
        if (obj instanceof Boolean) {
            return BOOLEAN;
        }
        if (JSType.isString(obj)) {
            return STRING;
        }
        if (JSType.isNumber(obj)) {
            return NUMBER;
        }
        if (obj == ScriptRuntime.UNDEFINED) {
            return UNDEFINED;
        }
        return OBJECT;
    }

    public static void voidReturn() {
    }

    public static boolean isRepresentableAsInt(long number) {
        return (long)((int)number) == number;
    }

    public static boolean isRepresentableAsInt(double number) {
        return (double)((int)number) == number;
    }

    public static boolean isStrictlyRepresentableAsInt(double number) {
        return JSType.isRepresentableAsInt(number) && JSType.isNotNegativeZero(number);
    }

    public static boolean isRepresentableAsInt(Object obj) {
        if (obj instanceof Number) {
            return JSType.isRepresentableAsInt(((Number)obj).doubleValue());
        }
        return false;
    }

    public static boolean isRepresentableAsLong(double number) {
        return (double)((long)number) == number;
    }

    public static boolean isRepresentableAsDouble(long number) {
        return 0x20000000000000L >= number && number >= -9007199254740992L;
    }

    private static boolean isNotNegativeZero(double number) {
        return Double.doubleToRawLongBits(number) != Long.MIN_VALUE;
    }

    public static boolean isPrimitive(Object obj) {
        return obj == null || obj == ScriptRuntime.UNDEFINED || JSType.isString(obj) || JSType.isNumber(obj) || obj instanceof Boolean;
    }

    public static Object toPrimitive(Object obj) {
        return JSType.toPrimitive(obj, null);
    }

    public static Object toPrimitive(Object obj, Class<?> hint) {
        if (obj instanceof ScriptObject) {
            return JSType.toPrimitive((ScriptObject)obj, hint);
        }
        if (JSType.isPrimitive(obj)) {
            return obj;
        }
        if (hint == Number.class && obj instanceof Number) {
            return ((Number)obj).doubleValue();
        }
        if (obj instanceof JSObject) {
            return JSType.toPrimitive((JSObject)obj, hint);
        }
        if (obj instanceof StaticClass) {
            String name = ((StaticClass)obj).getRepresentedClass().getName();
            return new StringBuilder(12 + name.length()).append("[JavaClass ").append(name).append(']').toString();
        }
        return obj.toString();
    }

    private static Object toPrimitive(ScriptObject sobj, Class<?> hint) {
        return JSType.requirePrimitive(sobj.getDefaultValue(hint));
    }

    private static Object requirePrimitive(Object result) {
        if (!JSType.isPrimitive(result)) {
            throw ECMAErrors.typeError("bad.default.value", result.toString());
        }
        return result;
    }

    public static Object toPrimitive(JSObject jsobj, Class<?> hint) {
        try {
            return JSType.requirePrimitive(AbstractJSObject.getDefaultValue(jsobj, hint));
        }
        catch (UnsupportedOperationException e) {
            throw new ECMAException(Context.getGlobal().newTypeError(e.getMessage()), (Throwable)e);
        }
    }

    public static String toPrimitiveToString(Object obj) {
        return JSType.toString(JSType.toPrimitive(obj));
    }

    public static CharSequence toPrimitiveToCharSequence(Object obj) {
        return JSType.toCharSequence(JSType.toPrimitive(obj));
    }

    public static boolean toBoolean(double num) {
        return num != 0.0 && !Double.isNaN(num);
    }

    public static boolean toBoolean(Object obj) {
        if (obj instanceof Boolean) {
            return (Boolean)obj;
        }
        if (JSType.nullOrUndefined(obj)) {
            return false;
        }
        if (obj instanceof Number) {
            double num = ((Number)obj).doubleValue();
            return num != 0.0 && !Double.isNaN(num);
        }
        if (JSType.isString(obj)) {
            return ((CharSequence)obj).length() > 0;
        }
        return true;
    }

    public static String toString(Object obj) {
        return JSType.toStringImpl(obj, false);
    }

    public static CharSequence toCharSequence(Object obj) {
        if (obj instanceof ConsString) {
            return (CharSequence)obj;
        }
        return JSType.toString(obj);
    }

    public static boolean isString(Object obj) {
        return obj instanceof String || obj instanceof ConsString;
    }

    public static boolean isNumber(Object obj) {
        if (obj != null) {
            Class<?> c = obj.getClass();
            return c == Integer.class || c == Double.class || c == Float.class || c == Short.class || c == Byte.class;
        }
        return false;
    }

    public static String toString(int num) {
        return Integer.toString(num);
    }

    public static String toString(double num) {
        if (JSType.isRepresentableAsInt(num)) {
            return Integer.toString((int)num);
        }
        if (num == Double.POSITIVE_INFINITY) {
            return "Infinity";
        }
        if (num == Double.NEGATIVE_INFINITY) {
            return "-Infinity";
        }
        if (Double.isNaN(num)) {
            return "NaN";
        }
        return NumberToString.stringFor(num);
    }

    public static String toString(double num, int radix) {
        assert (radix >= 2 && radix <= 36) : "invalid radix";
        if (JSType.isRepresentableAsInt(num)) {
            return Integer.toString((int)num, radix);
        }
        if (num == Double.POSITIVE_INFINITY) {
            return "Infinity";
        }
        if (num == Double.NEGATIVE_INFINITY) {
            return "-Infinity";
        }
        if (Double.isNaN(num)) {
            return "NaN";
        }
        if (num == 0.0) {
            return "0";
        }
        String chars = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        boolean negative = num < 0.0;
        double signedNum = negative ? -num : num;
        double intPart = Math.floor(signedNum);
        double decPart = signedNum - intPart;
        do {
            double remainder = intPart % (double)radix;
            sb.append("0123456789abcdefghijklmnopqrstuvwxyz".charAt((int)remainder));
            intPart -= remainder;
        } while ((intPart /= (double)radix) >= 1.0);
        if (negative) {
            sb.append('-');
        }
        sb.reverse();
        if (decPart > 0.0) {
            double d;
            int dot = sb.length();
            sb.append('.');
            do {
                d = Math.floor(decPart *= (double)radix);
                sb.append("0123456789abcdefghijklmnopqrstuvwxyz".charAt((int)d));
            } while ((decPart -= d) > 0.0 && sb.length() - dot < 1100);
        }
        return sb.toString();
    }

    public static double toNumber(Object obj) {
        if (obj instanceof Double) {
            return (Double)obj;
        }
        if (obj instanceof Number) {
            return ((Number)obj).doubleValue();
        }
        return JSType.toNumberGeneric(obj);
    }

    public static double toNumberForEq(Object obj) {
        return obj == null ? Double.NaN : JSType.toNumber(obj);
    }

    public static double toNumberForStrictEq(Object obj) {
        if (obj instanceof Double) {
            return (Double)obj;
        }
        if (JSType.isNumber(obj)) {
            return ((Number)obj).doubleValue();
        }
        return Double.NaN;
    }

    public static Number toNarrowestNumber(long l) {
        return JSType.isRepresentableAsInt(l) ? (double)Integer.valueOf((int)l).intValue() : Double.valueOf(l);
    }

    public static double toNumber(Boolean b) {
        return b != false ? 1.0 : 0.0;
    }

    public static double toNumber(ScriptObject obj) {
        return JSType.toNumber(JSType.toPrimitive(obj, Number.class));
    }

    public static double toNumberOptimistic(Object obj, int programPoint) {
        Class<?> clz;
        if (obj != null && ((clz = obj.getClass()) == Double.class || clz == Integer.class || clz == Long.class)) {
            return ((Number)obj).doubleValue();
        }
        throw new UnwarrantedOptimismException(obj, programPoint);
    }

    public static double toNumberMaybeOptimistic(Object obj, int programPoint) {
        return UnwarrantedOptimismException.isValid(programPoint) ? JSType.toNumberOptimistic(obj, programPoint) : JSType.toNumber(obj);
    }

    public static int digit(char ch, int radix) {
        return JSType.digit(ch, radix, false);
    }

    public static int digit(char ch, int radix, boolean onlyIsoLatin1) {
        char maxInRadix = (char)(97 + (radix - 1) - 10);
        char c = Character.toLowerCase(ch);
        if (c >= 'a' && c <= maxInRadix) {
            return Character.digit(ch, radix);
        }
        if (Character.isDigit(ch) && (!onlyIsoLatin1 || ch >= '0' && ch <= '9')) {
            return Character.digit(ch, radix);
        }
        return -1;
    }

    public static double toNumber(String str) {
        double value;
        boolean negative;
        int end = str.length();
        if (end == 0) {
            return 0.0;
        }
        int start = 0;
        char f = str.charAt(0);
        while (Lexer.isJSWhitespace(f)) {
            if (++start == end) {
                return 0.0;
            }
            f = str.charAt(start);
        }
        while (Lexer.isJSWhitespace(str.charAt(end - 1))) {
            --end;
        }
        if (f == '-') {
            if (++start == end) {
                return Double.NaN;
            }
            f = str.charAt(start);
            negative = true;
        } else {
            if (f == '+') {
                if (++start == end) {
                    return Double.NaN;
                }
                f = str.charAt(start);
            }
            negative = false;
        }
        if (start + 1 < end && f == '0' && Character.toLowerCase(str.charAt(start + 1)) == 'x') {
            value = JSType.parseRadix(str.toCharArray(), start + 2, end, 16);
        } else {
            if (f == 'I' && end - start == 8 && str.regionMatches(start, "Infinity", 0, 8)) {
                return negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
            }
            for (int i = start; i < end; ++i) {
                f = str.charAt(i);
                if (f >= '0' && f <= '9' || f == '.' || f == 'e' || f == 'E' || f == '+' || f == '-') continue;
                return Double.NaN;
            }
            try {
                value = Double.parseDouble(str.substring(start, end));
            }
            catch (NumberFormatException e) {
                return Double.NaN;
            }
        }
        return negative ? -value : value;
    }

    public static int toInteger(Object obj) {
        return (int)JSType.toNumber(obj);
    }

    public static long toLong(Object obj) {
        return obj instanceof Long ? (Long)obj : JSType.toLong(JSType.toNumber(obj));
    }

    public static long toLong(double num) {
        return (long)num;
    }

    public static int toInt32(Object obj) {
        return JSType.toInt32(JSType.toNumber(obj));
    }

    public static int toInt32Optimistic(Object obj, int programPoint) {
        if (obj != null && obj.getClass() == Integer.class) {
            return (Integer)obj;
        }
        throw new UnwarrantedOptimismException(obj, programPoint);
    }

    public static int toInt32MaybeOptimistic(Object obj, int programPoint) {
        return UnwarrantedOptimismException.isValid(programPoint) ? JSType.toInt32Optimistic(obj, programPoint) : JSType.toInt32(obj);
    }

    public static int toInt32(long num) {
        return (int)(num >= -9007199254740992L && num <= 0x20000000000000L ? num : (long)((double)num % 4.294967296E9));
    }

    public static int toInt32(double num) {
        return (int)JSType.doubleToInt32(num);
    }

    public static long toUint32(Object obj) {
        return JSType.toUint32(JSType.toNumber(obj));
    }

    public static long toUint32(double num) {
        return JSType.doubleToInt32(num) & 0xFFFFFFFFL;
    }

    public static long toUint32(int num) {
        return (long)num & 0xFFFFFFFFL;
    }

    public static int toUint32Optimistic(int num, int pp) {
        if (num >= 0) {
            return num;
        }
        throw new UnwarrantedOptimismException(JSType.toUint32Double(num), pp, Type.NUMBER);
    }

    public static double toUint32Double(int num) {
        return JSType.toUint32(num);
    }

    public static int toUint16(Object obj) {
        return JSType.toUint16(JSType.toNumber(obj));
    }

    public static int toUint16(int num) {
        return num & 0xFFFF;
    }

    public static int toUint16(long num) {
        return (int)num & 0xFFFF;
    }

    public static int toUint16(double num) {
        return (int)JSType.doubleToInt32(num) & 0xFFFF;
    }

    private static long doubleToInt32(double num) {
        int exponent = Math.getExponent(num);
        if (exponent < 31) {
            return (long)num;
        }
        if (exponent >= 84) {
            return 0L;
        }
        double d = num >= 0.0 ? Math.floor(num) : Math.ceil(num);
        return (long)(d % 4.294967296E9);
    }

    public static boolean isFinite(double num) {
        return !Double.isInfinite(num) && !Double.isNaN(num);
    }

    public static Double toDouble(double num) {
        return num;
    }

    public static Double toDouble(long num) {
        return num;
    }

    public static Double toDouble(int num) {
        return num;
    }

    public static Object toObject(boolean bool) {
        return bool;
    }

    public static Object toObject(int num) {
        return num;
    }

    public static Object toObject(long num) {
        return num;
    }

    public static Object toObject(double num) {
        return num;
    }

    public static Object toObject(Object obj) {
        return obj;
    }

    public static Object toScriptObject(Object obj) {
        return JSType.toScriptObject(Context.getGlobal(), obj);
    }

    public static Object toScriptObject(Global global, Object obj) {
        if (JSType.nullOrUndefined(obj)) {
            throw ECMAErrors.typeError(global, "not.an.object", ScriptRuntime.safeToString(obj));
        }
        if (obj instanceof ScriptObject) {
            return obj;
        }
        return global.wrapAsObject(obj);
    }

    public static Object toJavaArray(Object obj, Class<?> componentType) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).getArray().asArrayOfType(componentType);
        }
        if (obj instanceof JSObject) {
            ArrayLikeIterator<Object> itr = ArrayLikeIterator.arrayLikeIterator(obj);
            int len = (int)itr.getLength();
            Object[] res = new Object[len];
            int idx = 0;
            while (itr.hasNext()) {
                res[idx++] = itr.next();
            }
            return JSType.convertArray(res, componentType);
        }
        if (obj == null) {
            return null;
        }
        throw new IllegalArgumentException("not a script object");
    }

    public static Object convertArray(Object[] src, Class<?> componentType) {
        if (componentType == Object.class) {
            for (int i = 0; i < src.length; ++i) {
                Object e = src[i];
                if (!(e instanceof ConsString)) continue;
                src[i] = e.toString();
            }
        }
        int l = src.length;
        Object dst = Array.newInstance(componentType, l);
        MethodHandle converter = Bootstrap.getLinkerServices().getTypeConverter(Object.class, componentType);
        try {
            for (int i = 0; i < src.length; ++i) {
                Array.set(dst, i, JSType.invoke(converter, src[i]));
            }
        }
        catch (Error | RuntimeException e) {
            throw e;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return dst;
    }

    public static boolean nullOrUndefined(Object obj) {
        return obj == null || obj == ScriptRuntime.UNDEFINED;
    }

    static String toStringImpl(Object obj, boolean safe) {
        if (obj instanceof String) {
            return (String)obj;
        }
        if (obj instanceof ConsString) {
            return obj.toString();
        }
        if (JSType.isNumber(obj)) {
            return JSType.toString(((Number)obj).doubleValue());
        }
        if (obj == ScriptRuntime.UNDEFINED) {
            return "undefined";
        }
        if (obj == null) {
            return "null";
        }
        if (obj instanceof Boolean) {
            return obj.toString();
        }
        if (safe && obj instanceof ScriptObject) {
            ScriptObject sobj = (ScriptObject)obj;
            Global gobj = Context.getGlobal();
            return gobj.isError(sobj) ? ECMAException.safeToString(sobj) : sobj.safeToString();
        }
        return JSType.toString(JSType.toPrimitive(obj, String.class));
    }

    static String trimLeft(String str) {
        int start;
        for (start = 0; start < str.length() && Lexer.isJSWhitespace(str.charAt(start)); ++start) {
        }
        return str.substring(start);
    }

    private static Object throwUnwarrantedOptimismException(Object value, int programPoint) {
        throw new UnwarrantedOptimismException(value, programPoint);
    }

    public static int addExact(int x, int y, int programPoint) throws UnwarrantedOptimismException {
        try {
            return Math.addExact(x, y);
        }
        catch (ArithmeticException e) {
            throw new UnwarrantedOptimismException((double)x + (double)y, programPoint);
        }
    }

    public static int subExact(int x, int y, int programPoint) throws UnwarrantedOptimismException {
        try {
            return Math.subtractExact(x, y);
        }
        catch (ArithmeticException e) {
            throw new UnwarrantedOptimismException((double)x - (double)y, programPoint);
        }
    }

    public static int mulExact(int x, int y, int programPoint) throws UnwarrantedOptimismException {
        try {
            return Math.multiplyExact(x, y);
        }
        catch (ArithmeticException e) {
            throw new UnwarrantedOptimismException((double)x * (double)y, programPoint);
        }
    }

    public static int divExact(int x, int y, int programPoint) throws UnwarrantedOptimismException {
        int res;
        try {
            res = x / y;
        }
        catch (ArithmeticException e) {
            assert (y == 0);
            throw new UnwarrantedOptimismException(x > 0 ? Double.POSITIVE_INFINITY : (x < 0 ? Double.NEGATIVE_INFINITY : Double.NaN), programPoint);
        }
        int rem = x % y;
        if (rem == 0) {
            return res;
        }
        throw new UnwarrantedOptimismException((double)x / (double)y, programPoint);
    }

    public static int divZero(int x, int y) {
        return y == 0 ? 0 : x / y;
    }

    public static int remZero(int x, int y) {
        return y == 0 ? 0 : x % y;
    }

    public static int remExact(int x, int y, int programPoint) throws UnwarrantedOptimismException {
        try {
            return x % y;
        }
        catch (ArithmeticException e) {
            assert (y == 0);
            throw new UnwarrantedOptimismException(Double.NaN, programPoint);
        }
    }

    public static int decrementExact(int x, int programPoint) throws UnwarrantedOptimismException {
        try {
            return Math.decrementExact(x);
        }
        catch (ArithmeticException e) {
            throw new UnwarrantedOptimismException((double)x - 1.0, programPoint);
        }
    }

    public static int incrementExact(int x, int programPoint) throws UnwarrantedOptimismException {
        try {
            return Math.incrementExact(x);
        }
        catch (ArithmeticException e) {
            throw new UnwarrantedOptimismException((double)x + 1.0, programPoint);
        }
    }

    public static int negateExact(int x, int programPoint) throws UnwarrantedOptimismException {
        try {
            if (x == 0) {
                throw new UnwarrantedOptimismException(-0.0, programPoint);
            }
            return Math.negateExact(x);
        }
        catch (ArithmeticException e) {
            throw new UnwarrantedOptimismException(-((double)x), programPoint);
        }
    }

    public static int getAccessorTypeIndex(Type type) {
        return JSType.getAccessorTypeIndex(type.getTypeClass());
    }

    public static int getAccessorTypeIndex(Class<?> type) {
        if (type == null) {
            return -1;
        }
        if (type == Integer.TYPE) {
            return 0;
        }
        if (type == Double.TYPE) {
            return 1;
        }
        if (!type.isPrimitive()) {
            return 2;
        }
        return -1;
    }

    public static Type getAccessorType(int index) {
        return ACCESSOR_TYPES.get(index);
    }

    public static int getNumberOfAccessorTypes() {
        return ACCESSOR_TYPES.size();
    }

    private static double parseRadix(char[] chars, int start, int length, int radix) {
        int pos = 0;
        for (int i = start; i < length; ++i) {
            if (JSType.digit(chars[i], radix) == -1) {
                return Double.NaN;
            }
            ++pos;
        }
        if (pos == 0) {
            return Double.NaN;
        }
        double value = 0.0;
        for (int i = start; i < start + pos; ++i) {
            value *= (double)radix;
            value += (double)JSType.digit(chars[i], radix);
        }
        return value;
    }

    private static double toNumberGeneric(Object obj) {
        if (obj == null) {
            return 0.0;
        }
        if (obj instanceof String) {
            return JSType.toNumber((String)obj);
        }
        if (obj instanceof ConsString) {
            return JSType.toNumber(obj.toString());
        }
        if (obj instanceof Boolean) {
            return JSType.toNumber((Boolean)obj);
        }
        if (obj instanceof ScriptObject) {
            return JSType.toNumber((ScriptObject)obj);
        }
        if (obj instanceof Undefined) {
            return Double.NaN;
        }
        return JSType.toNumber(JSType.toPrimitive(obj, Number.class));
    }

    private static Object invoke(MethodHandle mh, Object arg) {
        try {
            return mh.invoke(arg);
        }
        catch (Error | RuntimeException e) {
            throw e;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static MethodHandle unboxConstant(Object o) {
        if (o != null) {
            if (o.getClass() == Integer.class) {
                return Lookup.MH.constant(Integer.TYPE, (int)((Integer)o));
            }
            if (o.getClass() == Double.class) {
                return Lookup.MH.constant(Double.TYPE, (double)((Double)o));
            }
        }
        return Lookup.MH.constant(Object.class, o);
    }

    public static Class<?> unboxedFieldType(Object o) {
        if (o == null) {
            return Object.class;
        }
        if (o.getClass() == Integer.class) {
            return Integer.TYPE;
        }
        if (o.getClass() == Double.class) {
            return Double.TYPE;
        }
        return Object.class;
    }

    private static final List<MethodHandle> toUnmodifiableList(MethodHandle ... methodHandles) {
        return Collections.unmodifiableList(Arrays.asList(methodHandles));
    }

    static {
        $VALUES = new JSType[]{UNDEFINED, NULL, BOOLEAN, NUMBER, STRING, OBJECT, FUNCTION};
        JSTYPE_LOOKUP = MethodHandles.lookup();
        TO_BOOLEAN = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toBoolean", Boolean.TYPE, Object.class);
        TO_BOOLEAN_D = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toBoolean", Boolean.TYPE, Double.TYPE);
        TO_INTEGER = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toInteger", Integer.TYPE, Object.class);
        TO_LONG = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toLong", Long.TYPE, Object.class);
        TO_LONG_D = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toLong", Long.TYPE, Double.TYPE);
        TO_NUMBER = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toNumber", Double.TYPE, Object.class);
        TO_NUMBER_OPTIMISTIC = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toNumberOptimistic", Double.TYPE, Object.class, Integer.TYPE);
        TO_STRING = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toString", String.class, Object.class);
        TO_INT32 = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toInt32", Integer.TYPE, Object.class);
        TO_INT32_L = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toInt32", Integer.TYPE, Long.TYPE);
        TO_INT32_OPTIMISTIC = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toInt32Optimistic", Integer.TYPE, Object.class, Integer.TYPE);
        TO_INT32_D = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toInt32", Integer.TYPE, Double.TYPE);
        TO_UINT32_OPTIMISTIC = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toUint32Optimistic", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        TO_UINT32_DOUBLE = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toUint32Double", Double.TYPE, Integer.TYPE);
        TO_UINT32 = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toUint32", Long.TYPE, Object.class);
        TO_UINT32_D = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toUint32", Long.TYPE, Double.TYPE);
        TO_STRING_D = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toString", String.class, Double.TYPE);
        TO_PRIMITIVE_TO_STRING = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toPrimitiveToString", String.class, Object.class);
        TO_PRIMITIVE_TO_CHARSEQUENCE = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toPrimitiveToCharSequence", CharSequence.class, Object.class);
        THROW_UNWARRANTED = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "throwUnwarrantedOptimismException", Object.class, Object.class, Integer.TYPE);
        ADD_EXACT = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "addExact", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        SUB_EXACT = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "subExact", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        MUL_EXACT = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "mulExact", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        DIV_EXACT = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "divExact", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        DIV_ZERO = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "divZero", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        REM_ZERO = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "remZero", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        REM_EXACT = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "remExact", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        DECREMENT_EXACT = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "decrementExact", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        INCREMENT_EXACT = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "incrementExact", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        NEGATE_EXACT = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "negateExact", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        TO_JAVA_ARRAY = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "toJavaArray", Object.class, Object.class, Class.class);
        VOID_RETURN = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "voidReturn", Void.TYPE, new Class[0]);
        IS_STRING = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "isString", Boolean.TYPE, Object.class);
        IS_NUMBER = CompilerConstants.staticCall(JSTYPE_LOOKUP, JSType.class, "isNumber", Boolean.TYPE, Object.class);
        ACCESSOR_TYPES = Collections.unmodifiableList(Arrays.asList(Type.INT, Type.NUMBER, Type.OBJECT));
        CONVERT_OBJECT = JSType.toUnmodifiableList(TO_INT32.methodHandle(), TO_NUMBER.methodHandle(), null);
        CONVERT_OBJECT_OPTIMISTIC = JSType.toUnmodifiableList(TO_INT32_OPTIMISTIC.methodHandle(), TO_NUMBER_OPTIMISTIC.methodHandle(), null);
        GET_UNDEFINED = JSType.toUnmodifiableList(Lookup.MH.constant(Integer.TYPE, 0), Lookup.MH.constant(Double.TYPE, Double.NaN), Lookup.MH.constant(Object.class, Undefined.getUndefined()));
    }
}

