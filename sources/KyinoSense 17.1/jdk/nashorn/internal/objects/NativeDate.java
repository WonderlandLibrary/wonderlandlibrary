/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.util.Collections;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.DateParser;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.linker.InvokeByName;

public final class NativeDate
extends ScriptObject {
    private static final String INVALID_DATE = "Invalid Date";
    private static final int YEAR = 0;
    private static final int MONTH = 1;
    private static final int DAY = 2;
    private static final int HOUR = 3;
    private static final int MINUTE = 4;
    private static final int SECOND = 5;
    private static final int MILLISECOND = 6;
    private static final int FORMAT_DATE_TIME = 0;
    private static final int FORMAT_DATE = 1;
    private static final int FORMAT_TIME = 2;
    private static final int FORMAT_LOCAL_DATE_TIME = 3;
    private static final int FORMAT_LOCAL_DATE = 4;
    private static final int FORMAT_LOCAL_TIME = 5;
    private static final int hoursPerDay = 24;
    private static final int minutesPerHour = 60;
    private static final int secondsPerMinute = 60;
    private static final int msPerSecond = 1000;
    private static final int msPerMinute = 60000;
    private static final double msPerHour = 3600000.0;
    private static final double msPerDay = 8.64E7;
    private static int[][] firstDayInMonth = new int[][]{{0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334}, {0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335}};
    private static String[] weekDays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private static String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private static final Object TO_ISO_STRING = new Object();
    private double time;
    private final TimeZone timezone;
    private static PropertyMap $nasgenmap$;

    private static InvokeByName getTO_ISO_STRING() {
        return Global.instance().getInvokeByName(TO_ISO_STRING, new Callable<InvokeByName>(){

            @Override
            public InvokeByName call() {
                return new InvokeByName("toISOString", ScriptObject.class, Object.class, Object.class);
            }
        });
    }

    private NativeDate(double time, ScriptObject proto, PropertyMap map) {
        super(proto, map);
        ScriptEnvironment env = Global.getEnv();
        this.time = time;
        this.timezone = env._timezone;
    }

    NativeDate(double time, ScriptObject proto) {
        this(time, proto, $nasgenmap$);
    }

    NativeDate(double time, Global global) {
        this(time, global.getDatePrototype(), $nasgenmap$);
    }

    private NativeDate(double time) {
        this(time, Global.instance());
    }

    private NativeDate() {
        this(System.currentTimeMillis());
    }

    @Override
    public String getClassName() {
        return "Date";
    }

    @Override
    public Object getDefaultValue(Class<?> hint) {
        return super.getDefaultValue(hint == null ? String.class : hint);
    }

    public static Object construct(boolean isNew, Object self) {
        NativeDate result = new NativeDate();
        return isNew ? result : NativeDate.toStringImpl(result, 0);
    }

    public static Object construct(boolean isNew, Object self, Object ... args2) {
        NativeDate result;
        if (!isNew) {
            return NativeDate.toStringImpl(new NativeDate(), 0);
        }
        switch (args2.length) {
            case 0: {
                result = new NativeDate();
                break;
            }
            case 1: {
                Object arg = JSType.toPrimitive(args2[0]);
                double num = JSType.isString(arg) ? NativeDate.parseDateString(arg.toString()) : NativeDate.timeClip(JSType.toNumber(args2[0]));
                result = new NativeDate(num);
                break;
            }
            default: {
                result = new NativeDate(0.0);
                double[] d = NativeDate.convertCtorArgs(args2);
                if (d == null) {
                    result.setTime(Double.NaN);
                    break;
                }
                double time = NativeDate.timeClip(NativeDate.utc(NativeDate.makeDate(d), result.getTimeZone()));
                result.setTime(time);
            }
        }
        return result;
    }

    @Override
    public String safeToString() {
        String str = this.isValidDate() ? NativeDate.toISOStringImpl(this) : INVALID_DATE;
        return "[Date " + str + "]";
    }

    public String toString() {
        return this.isValidDate() ? NativeDate.toString(this).toString() : INVALID_DATE;
    }

    public static double parse(Object self, Object string) {
        return NativeDate.parseDateString(JSType.toString(string));
    }

    public static double UTC(Object self, Object ... args2) {
        NativeDate nd = new NativeDate(0.0);
        double[] d = NativeDate.convertCtorArgs(args2);
        double time = d == null ? Double.NaN : NativeDate.timeClip(NativeDate.makeDate(d));
        nd.setTime(time);
        return time;
    }

    public static double now(Object self) {
        return System.currentTimeMillis();
    }

    public static String toString(Object self) {
        return NativeDate.toStringImpl(self, 0);
    }

    public static String toDateString(Object self) {
        return NativeDate.toStringImpl(self, 1);
    }

    public static String toTimeString(Object self) {
        return NativeDate.toStringImpl(self, 2);
    }

    public static String toLocaleString(Object self) {
        return NativeDate.toStringImpl(self, 3);
    }

    public static String toLocaleDateString(Object self) {
        return NativeDate.toStringImpl(self, 4);
    }

    public static String toLocaleTimeString(Object self) {
        return NativeDate.toStringImpl(self, 5);
    }

    public static double valueOf(Object self) {
        NativeDate nd = NativeDate.getNativeDate(self);
        return nd != null ? nd.getTime() : Double.NaN;
    }

    public static double getTime(Object self) {
        NativeDate nd = NativeDate.getNativeDate(self);
        return nd != null ? nd.getTime() : Double.NaN;
    }

    public static Object getFullYear(Object self) {
        return NativeDate.getField(self, 0);
    }

    public static double getUTCFullYear(Object self) {
        return NativeDate.getUTCField(self, 0);
    }

    public static double getYear(Object self) {
        NativeDate nd = NativeDate.getNativeDate(self);
        return nd != null && nd.isValidDate() ? (double)(NativeDate.yearFromTime(nd.getLocalTime()) - 1900) : Double.NaN;
    }

    public static double getMonth(Object self) {
        return NativeDate.getField(self, 1);
    }

    public static double getUTCMonth(Object self) {
        return NativeDate.getUTCField(self, 1);
    }

    public static double getDate(Object self) {
        return NativeDate.getField(self, 2);
    }

    public static double getUTCDate(Object self) {
        return NativeDate.getUTCField(self, 2);
    }

    public static double getDay(Object self) {
        NativeDate nd = NativeDate.getNativeDate(self);
        return nd != null && nd.isValidDate() ? (double)NativeDate.weekDay(nd.getLocalTime()) : Double.NaN;
    }

    public static double getUTCDay(Object self) {
        NativeDate nd = NativeDate.getNativeDate(self);
        return nd != null && nd.isValidDate() ? (double)NativeDate.weekDay(nd.getTime()) : Double.NaN;
    }

    public static double getHours(Object self) {
        return NativeDate.getField(self, 3);
    }

    public static double getUTCHours(Object self) {
        return NativeDate.getUTCField(self, 3);
    }

    public static double getMinutes(Object self) {
        return NativeDate.getField(self, 4);
    }

    public static double getUTCMinutes(Object self) {
        return NativeDate.getUTCField(self, 4);
    }

    public static double getSeconds(Object self) {
        return NativeDate.getField(self, 5);
    }

    public static double getUTCSeconds(Object self) {
        return NativeDate.getUTCField(self, 5);
    }

    public static double getMilliseconds(Object self) {
        return NativeDate.getField(self, 6);
    }

    public static double getUTCMilliseconds(Object self) {
        return NativeDate.getUTCField(self, 6);
    }

    public static double getTimezoneOffset(Object self) {
        NativeDate nd = NativeDate.getNativeDate(self);
        if (nd != null && nd.isValidDate()) {
            long msec = (long)nd.getTime();
            return -nd.getTimeZone().getOffset(msec) / 60000;
        }
        return Double.NaN;
    }

    public static double setTime(Object self, Object time) {
        NativeDate nd = NativeDate.getNativeDate(self);
        double num = NativeDate.timeClip(JSType.toNumber(time));
        nd.setTime(num);
        return num;
    }

    public static double setMilliseconds(Object self, Object ... args2) {
        NativeDate nd = NativeDate.getNativeDate(self);
        NativeDate.setFields(nd, 6, args2, true);
        return nd.getTime();
    }

    public static double setUTCMilliseconds(Object self, Object ... args2) {
        NativeDate nd = NativeDate.getNativeDate(self);
        NativeDate.setFields(nd, 6, args2, false);
        return nd.getTime();
    }

    public static double setSeconds(Object self, Object ... args2) {
        NativeDate nd = NativeDate.getNativeDate(self);
        NativeDate.setFields(nd, 5, args2, true);
        return nd.getTime();
    }

    public static double setUTCSeconds(Object self, Object ... args2) {
        NativeDate nd = NativeDate.getNativeDate(self);
        NativeDate.setFields(nd, 5, args2, false);
        return nd.getTime();
    }

    public static double setMinutes(Object self, Object ... args2) {
        NativeDate nd = NativeDate.getNativeDate(self);
        NativeDate.setFields(nd, 4, args2, true);
        return nd.getTime();
    }

    public static double setUTCMinutes(Object self, Object ... args2) {
        NativeDate nd = NativeDate.getNativeDate(self);
        NativeDate.setFields(nd, 4, args2, false);
        return nd.getTime();
    }

    public static double setHours(Object self, Object ... args2) {
        NativeDate nd = NativeDate.getNativeDate(self);
        NativeDate.setFields(nd, 3, args2, true);
        return nd.getTime();
    }

    public static double setUTCHours(Object self, Object ... args2) {
        NativeDate nd = NativeDate.getNativeDate(self);
        NativeDate.setFields(nd, 3, args2, false);
        return nd.getTime();
    }

    public static double setDate(Object self, Object ... args2) {
        NativeDate nd = NativeDate.getNativeDate(self);
        NativeDate.setFields(nd, 2, args2, true);
        return nd.getTime();
    }

    public static double setUTCDate(Object self, Object ... args2) {
        NativeDate nd = NativeDate.getNativeDate(self);
        NativeDate.setFields(nd, 2, args2, false);
        return nd.getTime();
    }

    public static double setMonth(Object self, Object ... args2) {
        NativeDate nd = NativeDate.getNativeDate(self);
        NativeDate.setFields(nd, 1, args2, true);
        return nd.getTime();
    }

    public static double setUTCMonth(Object self, Object ... args2) {
        NativeDate nd = NativeDate.ensureNativeDate(self);
        NativeDate.setFields(nd, 1, args2, false);
        return nd.getTime();
    }

    public static double setFullYear(Object self, Object ... args2) {
        NativeDate nd = NativeDate.ensureNativeDate(self);
        if (nd.isValidDate()) {
            NativeDate.setFields(nd, 0, args2, true);
        } else {
            double[] d = NativeDate.convertArgs(args2, 0.0, 0, 0, 3);
            if (d != null) {
                nd.setTime(NativeDate.timeClip(NativeDate.utc(NativeDate.makeDate(NativeDate.makeDay(d[0], d[1], d[2]), 0.0), nd.getTimeZone())));
            } else {
                nd.setTime(Double.NaN);
            }
        }
        return nd.getTime();
    }

    public static double setUTCFullYear(Object self, Object ... args2) {
        NativeDate nd = NativeDate.ensureNativeDate(self);
        if (nd.isValidDate()) {
            NativeDate.setFields(nd, 0, args2, false);
        } else {
            double[] d = NativeDate.convertArgs(args2, 0.0, 0, 0, 3);
            nd.setTime(NativeDate.timeClip(NativeDate.makeDate(NativeDate.makeDay(d[0], d[1], d[2]), 0.0)));
        }
        return nd.getTime();
    }

    public static double setYear(Object self, Object year) {
        double yearNum;
        NativeDate nd = NativeDate.getNativeDate(self);
        if (Double.isNaN(nd.getTime())) {
            nd.setTime(NativeDate.utc(0.0, nd.getTimeZone()));
        }
        if (Double.isNaN(yearNum = JSType.toNumber(year))) {
            nd.setTime(Double.NaN);
            return nd.getTime();
        }
        int yearInt = (int)yearNum;
        if (0 <= yearInt && yearInt <= 99) {
            yearInt += 1900;
        }
        NativeDate.setFields(nd, 0, new Object[]{yearInt}, true);
        return nd.getTime();
    }

    public static String toUTCString(Object self) {
        return NativeDate.toGMTStringImpl(self);
    }

    public static String toGMTString(Object self) {
        return NativeDate.toGMTStringImpl(self);
    }

    public static String toISOString(Object self) {
        return NativeDate.toISOStringImpl(self);
    }

    public static Object toJSON(Object self, Object key) {
        double num;
        Object selfObj = Global.toObject(self);
        if (!(selfObj instanceof ScriptObject)) {
            return null;
        }
        ScriptObject sobj = (ScriptObject)selfObj;
        Object value = sobj.getDefaultValue(Number.class);
        if (value instanceof Number && (Double.isInfinite(num = ((Number)value).doubleValue()) || Double.isNaN(num))) {
            return null;
        }
        try {
            InvokeByName toIsoString = NativeDate.getTO_ISO_STRING();
            Object func = toIsoString.getGetter().invokeExact(sobj);
            if (Bootstrap.isCallable(func)) {
                return toIsoString.getInvoker().invokeExact(func, sobj, key);
            }
            throw ECMAErrors.typeError("not.a.function", ScriptRuntime.safeToString(func));
        }
        catch (Error | RuntimeException e) {
            throw e;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private static double parseDateString(String str) {
        DateParser parser = new DateParser(str);
        if (parser.parse()) {
            Integer[] fields = parser.getDateFields();
            double d = NativeDate.makeDate(fields);
            d = fields[7] != null ? (d -= (double)(fields[7] * 60000)) : NativeDate.utc(d, Global.getEnv()._timezone);
            d = NativeDate.timeClip(d);
            return d;
        }
        return Double.NaN;
    }

    private static void zeroPad(StringBuilder sb, int n, int length) {
        int l = 1;
        int d = 10;
        while (l < length) {
            if (n < d) {
                sb.append('0');
            }
            ++l;
            d *= 10;
        }
        sb.append(n);
    }

    private static String toStringImpl(Object self, int format) {
        NativeDate nd = NativeDate.getNativeDate(self);
        if (nd != null && nd.isValidDate()) {
            StringBuilder sb = new StringBuilder(40);
            double t = nd.getLocalTime();
            switch (format) {
                case 0: 
                case 1: 
                case 3: {
                    sb.append(weekDays[NativeDate.weekDay(t)]).append(' ').append(months[NativeDate.monthFromTime(t)]).append(' ');
                    NativeDate.zeroPad(sb, NativeDate.dayFromTime(t), 2);
                    sb.append(' ');
                    NativeDate.zeroPad(sb, NativeDate.yearFromTime(t), 4);
                    if (format == 1) break;
                    sb.append(' ');
                }
                case 2: {
                    TimeZone tz = nd.getTimeZone();
                    double utcTime = nd.getTime();
                    int offset = tz.getOffset((long)utcTime) / 60000;
                    boolean inDaylightTime = offset != tz.getRawOffset() / 60000;
                    offset = offset / 60 * 100 + offset % 60;
                    NativeDate.zeroPad(sb, NativeDate.hourFromTime(t), 2);
                    sb.append(':');
                    NativeDate.zeroPad(sb, NativeDate.minFromTime(t), 2);
                    sb.append(':');
                    NativeDate.zeroPad(sb, NativeDate.secFromTime(t), 2);
                    sb.append(" GMT").append(offset < 0 ? (char)'-' : '+');
                    NativeDate.zeroPad(sb, Math.abs(offset), 4);
                    sb.append(" (").append(tz.getDisplayName(inDaylightTime, 0, Locale.US)).append(')');
                    break;
                }
                case 4: {
                    NativeDate.zeroPad(sb, NativeDate.yearFromTime(t), 4);
                    sb.append('-');
                    NativeDate.zeroPad(sb, NativeDate.monthFromTime(t) + 1, 2);
                    sb.append('-');
                    NativeDate.zeroPad(sb, NativeDate.dayFromTime(t), 2);
                    break;
                }
                case 5: {
                    NativeDate.zeroPad(sb, NativeDate.hourFromTime(t), 2);
                    sb.append(':');
                    NativeDate.zeroPad(sb, NativeDate.minFromTime(t), 2);
                    sb.append(':');
                    NativeDate.zeroPad(sb, NativeDate.secFromTime(t), 2);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("format: " + format);
                }
            }
            return sb.toString();
        }
        return INVALID_DATE;
    }

    private static String toGMTStringImpl(Object self) {
        NativeDate nd = NativeDate.getNativeDate(self);
        if (nd != null && nd.isValidDate()) {
            StringBuilder sb = new StringBuilder(29);
            double t = nd.getTime();
            sb.append(weekDays[NativeDate.weekDay(t)]).append(", ");
            NativeDate.zeroPad(sb, NativeDate.dayFromTime(t), 2);
            sb.append(' ').append(months[NativeDate.monthFromTime(t)]).append(' ');
            NativeDate.zeroPad(sb, NativeDate.yearFromTime(t), 4);
            sb.append(' ');
            NativeDate.zeroPad(sb, NativeDate.hourFromTime(t), 2);
            sb.append(':');
            NativeDate.zeroPad(sb, NativeDate.minFromTime(t), 2);
            sb.append(':');
            NativeDate.zeroPad(sb, NativeDate.secFromTime(t), 2);
            sb.append(" GMT");
            return sb.toString();
        }
        throw ECMAErrors.rangeError("invalid.date", new String[0]);
    }

    private static String toISOStringImpl(Object self) {
        NativeDate nd = NativeDate.getNativeDate(self);
        if (nd != null && nd.isValidDate()) {
            StringBuilder sb = new StringBuilder(24);
            double t = nd.getTime();
            NativeDate.zeroPad(sb, NativeDate.yearFromTime(t), 4);
            sb.append('-');
            NativeDate.zeroPad(sb, NativeDate.monthFromTime(t) + 1, 2);
            sb.append('-');
            NativeDate.zeroPad(sb, NativeDate.dayFromTime(t), 2);
            sb.append('T');
            NativeDate.zeroPad(sb, NativeDate.hourFromTime(t), 2);
            sb.append(':');
            NativeDate.zeroPad(sb, NativeDate.minFromTime(t), 2);
            sb.append(':');
            NativeDate.zeroPad(sb, NativeDate.secFromTime(t), 2);
            sb.append('.');
            NativeDate.zeroPad(sb, NativeDate.msFromTime(t), 3);
            sb.append("Z");
            return sb.toString();
        }
        throw ECMAErrors.rangeError("invalid.date", new String[0]);
    }

    private static double day(double t) {
        return Math.floor(t / 8.64E7);
    }

    private static double timeWithinDay(double t) {
        double val = t % 8.64E7;
        return val < 0.0 ? val + 8.64E7 : val;
    }

    private static boolean isLeapYear(int y) {
        return y % 4 == 0 && (y % 100 != 0 || y % 400 == 0);
    }

    private static int daysInYear(int y) {
        return NativeDate.isLeapYear(y) ? 366 : 365;
    }

    private static double dayFromYear(double y) {
        return 365.0 * (y - 1970.0) + Math.floor((y - 1969.0) / 4.0) - Math.floor((y - 1901.0) / 100.0) + Math.floor((y - 1601.0) / 400.0);
    }

    private static double timeFromYear(int y) {
        return NativeDate.dayFromYear(y) * 8.64E7;
    }

    private static int yearFromTime(double t) {
        int y = (int)Math.floor(t / 3.1556952E10) + 1970;
        double t2 = NativeDate.timeFromYear(y);
        if (t2 > t) {
            --y;
        } else if (t2 + 8.64E7 * (double)NativeDate.daysInYear(y) <= t) {
            ++y;
        }
        return y;
    }

    private static int dayWithinYear(double t, int year) {
        return (int)(NativeDate.day(t) - NativeDate.dayFromYear(year));
    }

    private static int monthFromTime(double t) {
        int month;
        int year = NativeDate.yearFromTime(t);
        int day = NativeDate.dayWithinYear(t, year);
        int[] firstDay = firstDayInMonth[NativeDate.isLeapYear(year) ? 1 : 0];
        for (month = 0; month < 11 && firstDay[month + 1] <= day; ++month) {
        }
        return month;
    }

    private static int dayFromTime(double t) {
        int month;
        int year = NativeDate.yearFromTime(t);
        int day = NativeDate.dayWithinYear(t, year);
        int[] firstDay = firstDayInMonth[NativeDate.isLeapYear(year) ? 1 : 0];
        for (month = 0; month < 11 && firstDay[month + 1] <= day; ++month) {
        }
        return 1 + day - firstDay[month];
    }

    private static int dayFromMonth(int month, int year) {
        assert (month >= 0 && month <= 11);
        int[] firstDay = firstDayInMonth[NativeDate.isLeapYear(year) ? 1 : 0];
        return firstDay[month];
    }

    private static int weekDay(double time) {
        int day = (int)(NativeDate.day(time) + 4.0) % 7;
        return day < 0 ? day + 7 : day;
    }

    private static double localTime(double time, TimeZone tz) {
        return time + (double)tz.getOffset((long)time);
    }

    private static double utc(double time, TimeZone tz) {
        return time - (double)tz.getOffset((long)(time - (double)tz.getRawOffset()));
    }

    private static int hourFromTime(double t) {
        int h = (int)(Math.floor(t / 3600000.0) % 24.0);
        return h < 0 ? h + 24 : h;
    }

    private static int minFromTime(double t) {
        int m = (int)(Math.floor(t / 60000.0) % 60.0);
        return m < 0 ? m + 60 : m;
    }

    private static int secFromTime(double t) {
        int s = (int)(Math.floor(t / 1000.0) % 60.0);
        return s < 0 ? s + 60 : s;
    }

    private static int msFromTime(double t) {
        int m = (int)(t % 1000.0);
        return m < 0 ? m + 1000 : m;
    }

    private static int valueFromTime(int unit, double t) {
        switch (unit) {
            case 0: {
                return NativeDate.yearFromTime(t);
            }
            case 1: {
                return NativeDate.monthFromTime(t);
            }
            case 2: {
                return NativeDate.dayFromTime(t);
            }
            case 3: {
                return NativeDate.hourFromTime(t);
            }
            case 4: {
                return NativeDate.minFromTime(t);
            }
            case 5: {
                return NativeDate.secFromTime(t);
            }
            case 6: {
                return NativeDate.msFromTime(t);
            }
        }
        throw new IllegalArgumentException(Integer.toString(unit));
    }

    private static double makeTime(double hour, double min, double sec, double ms) {
        return hour * 3600000.0 + min * 60000.0 + sec * 1000.0 + ms;
    }

    private static double makeDay(double year, double month, double date) {
        double y = year + Math.floor(month / 12.0);
        int m = (int)(month % 12.0);
        if (m < 0) {
            m += 12;
        }
        double d = NativeDate.dayFromYear(y);
        return (d += (double)NativeDate.dayFromMonth(m, (int)y)) + date - 1.0;
    }

    private static double makeDate(double day, double time) {
        return day * 8.64E7 + time;
    }

    private static double makeDate(Integer[] d) {
        double time = NativeDate.makeDay(d[0].intValue(), d[1].intValue(), d[2].intValue()) * 8.64E7;
        return time + NativeDate.makeTime(d[3].intValue(), d[4].intValue(), d[5].intValue(), d[6].intValue());
    }

    private static double makeDate(double[] d) {
        double time = NativeDate.makeDay(d[0], d[1], d[2]) * 8.64E7;
        return time + NativeDate.makeTime(d[3], d[4], d[5], d[6]);
    }

    private static double[] convertCtorArgs(Object[] args2) {
        double[] d = new double[7];
        boolean nullReturn = false;
        for (int i = 0; i < d.length; ++i) {
            if (i < args2.length) {
                double darg = JSType.toNumber(args2[i]);
                if (Double.isNaN(darg) || Double.isInfinite(darg)) {
                    nullReturn = true;
                }
                d[i] = (long)darg;
                continue;
            }
            d[i] = i == 2 ? 1.0 : 0.0;
        }
        if (0.0 <= d[0] && d[0] <= 99.0) {
            d[0] = d[0] + 1900.0;
        }
        return nullReturn ? null : d;
    }

    private static double[] convertArgs(Object[] args2, double time, int fieldId, int start, int length) {
        double[] d = new double[length];
        boolean nullReturn = false;
        for (int i = start; i < start + length; ++i) {
            if (fieldId <= i && i < fieldId + args2.length) {
                double darg = JSType.toNumber(args2[i - fieldId]);
                if (Double.isNaN(darg) || Double.isInfinite(darg)) {
                    nullReturn = true;
                }
                d[i - start] = (long)darg;
                continue;
            }
            if (i == fieldId) {
                nullReturn = true;
            }
            if (nullReturn || Double.isNaN(time)) continue;
            d[i - start] = NativeDate.valueFromTime(i, time);
        }
        return nullReturn ? null : d;
    }

    private static double timeClip(double time) {
        if (Double.isInfinite(time) || Double.isNaN(time) || Math.abs(time) > 8.64E15) {
            return Double.NaN;
        }
        return (long)time;
    }

    private static NativeDate ensureNativeDate(Object self) {
        return NativeDate.getNativeDate(self);
    }

    private static NativeDate getNativeDate(Object self) {
        if (self instanceof NativeDate) {
            return (NativeDate)self;
        }
        if (self != null && self == Global.instance().getDatePrototype()) {
            return Global.instance().getDefaultDate();
        }
        throw ECMAErrors.typeError("not.a.date", ScriptRuntime.safeToString(self));
    }

    private static double getField(Object self, int field) {
        NativeDate nd = NativeDate.getNativeDate(self);
        return nd != null && nd.isValidDate() ? (double)NativeDate.valueFromTime(field, nd.getLocalTime()) : Double.NaN;
    }

    private static double getUTCField(Object self, int field) {
        NativeDate nd = NativeDate.getNativeDate(self);
        return nd != null && nd.isValidDate() ? (double)NativeDate.valueFromTime(field, nd.getTime()) : Double.NaN;
    }

    private static void setFields(NativeDate nd, int fieldId, Object[] args2, boolean local) {
        double newTime;
        int length;
        int start;
        if (fieldId < 3) {
            start = 0;
            length = 3;
        } else {
            start = 3;
            length = 4;
        }
        double time = local ? nd.getLocalTime() : nd.getTime();
        double[] d = NativeDate.convertArgs(args2, time, fieldId, start, length);
        if (!nd.isValidDate()) {
            return;
        }
        if (d == null) {
            newTime = Double.NaN;
        } else {
            newTime = start == 0 ? NativeDate.makeDate(NativeDate.makeDay(d[0], d[1], d[2]), NativeDate.timeWithinDay(time)) : NativeDate.makeDate(NativeDate.day(time), NativeDate.makeTime(d[0], d[1], d[2], d[3]));
            if (local) {
                newTime = NativeDate.utc(newTime, nd.getTimeZone());
            }
            newTime = NativeDate.timeClip(newTime);
        }
        nd.setTime(newTime);
    }

    private boolean isValidDate() {
        return !Double.isNaN(this.time);
    }

    private double getLocalTime() {
        return NativeDate.localTime(this.time, this.timezone);
    }

    private double getTime() {
        return this.time;
    }

    private void setTime(double time) {
        this.time = time;
    }

    private TimeZone getTimeZone() {
        return this.timezone;
    }

    static {
        NativeDate.$clinit$();
    }

    public static void $clinit$() {
        $nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
}

