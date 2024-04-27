/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Locale;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.linker.NashornGuards;
import jdk.nashorn.internal.runtime.linker.PrimitiveLookup;

public final class NativeNumber
extends ScriptObject {
    static final MethodHandle WRAPFILTER = NativeNumber.findOwnMH("wrapFilter", Lookup.MH.type(NativeNumber.class, Object.class));
    private static final MethodHandle PROTOFILTER = NativeNumber.findOwnMH("protoFilter", Lookup.MH.type(Object.class, Object.class));
    public static final double MAX_VALUE = Double.MAX_VALUE;
    public static final double MIN_VALUE = Double.MIN_VALUE;
    public static final double NaN = Double.NaN;
    public static final double NEGATIVE_INFINITY = Double.NEGATIVE_INFINITY;
    public static final double POSITIVE_INFINITY = Double.POSITIVE_INFINITY;
    private final double value;
    private static PropertyMap $nasgenmap$;

    private NativeNumber(double value, ScriptObject proto, PropertyMap map) {
        super(proto, map);
        this.value = value;
    }

    NativeNumber(double value, Global global) {
        this(value, global.getNumberPrototype(), $nasgenmap$);
    }

    private NativeNumber(double value) {
        this(value, Global.instance());
    }

    @Override
    public String safeToString() {
        return "[Number " + this.toString() + "]";
    }

    public String toString() {
        return Double.toString(this.getValue());
    }

    public double getValue() {
        return this.doubleValue();
    }

    public double doubleValue() {
        return this.value;
    }

    @Override
    public String getClassName() {
        return "Number";
    }

    public static Object constructor(boolean newObj, Object self, Object ... args2) {
        double num = args2.length > 0 ? JSType.toNumber(args2[0]) : 0.0;
        return newObj ? new NativeNumber(num) : Double.valueOf(num);
    }

    public static String toFixed(Object self, Object fractionDigits) {
        return NativeNumber.toFixed(self, JSType.toInteger(fractionDigits));
    }

    public static String toFixed(Object self, int fractionDigits) {
        if (fractionDigits < 0 || fractionDigits > 20) {
            throw ECMAErrors.rangeError("invalid.fraction.digits", "toFixed");
        }
        double x = NativeNumber.getNumberValue(self);
        if (Double.isNaN(x)) {
            return "NaN";
        }
        if (Math.abs(x) >= 1.0E21) {
            return JSType.toString(x);
        }
        NumberFormat format = NumberFormat.getNumberInstance(Locale.US);
        format.setMinimumFractionDigits(fractionDigits);
        format.setMaximumFractionDigits(fractionDigits);
        format.setGroupingUsed(false);
        format.setRoundingMode(RoundingMode.HALF_UP);
        return format.format(x);
    }

    public static String toExponential(Object self, Object fractionDigits) {
        int f;
        double x = NativeNumber.getNumberValue(self);
        boolean trimZeros = fractionDigits == ScriptRuntime.UNDEFINED;
        int n = f = trimZeros ? 16 : JSType.toInteger(fractionDigits);
        if (Double.isNaN(x)) {
            return "NaN";
        }
        if (Double.isInfinite(x)) {
            return x > 0.0 ? "Infinity" : "-Infinity";
        }
        if (fractionDigits != ScriptRuntime.UNDEFINED && (f < 0 || f > 20)) {
            throw ECMAErrors.rangeError("invalid.fraction.digits", "toExponential");
        }
        String res = String.format(Locale.US, "%1." + f + "e", x);
        return NativeNumber.fixExponent(res, trimZeros);
    }

    public static String toPrecision(Object self, Object precision) {
        double x = NativeNumber.getNumberValue(self);
        if (precision == ScriptRuntime.UNDEFINED) {
            return JSType.toString(x);
        }
        return NativeNumber.toPrecision(x, JSType.toInteger(precision));
    }

    public static String toPrecision(Object self, int precision) {
        return NativeNumber.toPrecision(NativeNumber.getNumberValue(self), precision);
    }

    private static String toPrecision(double x, int p) {
        if (Double.isNaN(x)) {
            return "NaN";
        }
        if (Double.isInfinite(x)) {
            return x > 0.0 ? "Infinity" : "-Infinity";
        }
        if (p < 1 || p > 21) {
            throw ECMAErrors.rangeError("invalid.precision", new String[0]);
        }
        if (x == 0.0 && p <= 1) {
            return "0";
        }
        return NativeNumber.fixExponent(String.format(Locale.US, "%." + p + "g", x), false);
    }

    public static String toString(Object self, Object radix) {
        int intRadix;
        if (radix != ScriptRuntime.UNDEFINED && (intRadix = JSType.toInteger(radix)) != 10) {
            if (intRadix < 2 || intRadix > 36) {
                throw ECMAErrors.rangeError("invalid.radix", new String[0]);
            }
            return JSType.toString(NativeNumber.getNumberValue(self), intRadix);
        }
        return JSType.toString(NativeNumber.getNumberValue(self));
    }

    public static String toLocaleString(Object self) {
        return JSType.toString(NativeNumber.getNumberValue(self));
    }

    public static double valueOf(Object self) {
        return NativeNumber.getNumberValue(self);
    }

    public static GuardedInvocation lookupPrimitive(LinkRequest request, Object receiver) {
        return PrimitiveLookup.lookupPrimitive(request, NashornGuards.getNumberGuard(), (ScriptObject)new NativeNumber(((Number)receiver).doubleValue()), WRAPFILTER, PROTOFILTER);
    }

    private static NativeNumber wrapFilter(Object receiver) {
        return new NativeNumber(((Number)receiver).doubleValue());
    }

    private static Object protoFilter(Object object) {
        return Global.instance().getNumberPrototype();
    }

    private static double getNumberValue(Object self) {
        if (self instanceof Number) {
            return ((Number)self).doubleValue();
        }
        if (self instanceof NativeNumber) {
            return ((NativeNumber)self).getValue();
        }
        if (self != null && self == Global.instance().getNumberPrototype()) {
            return 0.0;
        }
        throw ECMAErrors.typeError("not.a.number", ScriptRuntime.safeToString(self));
    }

    private static String fixExponent(String str, boolean trimZeros) {
        int index = str.indexOf(101);
        if (index < 1) {
            return str;
        }
        int expPadding = str.charAt(index + 2) == '0' ? 3 : 2;
        int fractionOffset = index;
        if (trimZeros) {
            assert (fractionOffset > 0);
            char c = str.charAt(fractionOffset - 1);
            while (fractionOffset > 1 && (c == '0' || c == '.')) {
                c = str.charAt(--fractionOffset - 1);
            }
        }
        if (fractionOffset < index || expPadding == 3) {
            return str.substring(0, fractionOffset) + str.substring(index, index + 2) + str.substring(index + expPadding);
        }
        return str;
    }

    private static MethodHandle findOwnMH(String name, MethodType type) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), NativeNumber.class, name, type);
    }

    static {
        NativeNumber.$clinit$();
    }

    public static void $clinit$() {
        $nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
}

