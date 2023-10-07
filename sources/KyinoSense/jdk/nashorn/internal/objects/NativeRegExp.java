/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.objects.NativeRegExpExecResult;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.BitVector;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ParserException;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyAccess;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.Undefined;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.regexp.RegExp;
import jdk.nashorn.internal.runtime.regexp.RegExpFactory;
import jdk.nashorn.internal.runtime.regexp.RegExpMatcher;
import jdk.nashorn.internal.runtime.regexp.RegExpResult;

public final class NativeRegExp
extends ScriptObject {
    public Object lastIndex;
    private RegExp regexp;
    private final Global globalObject;
    private static PropertyMap $nasgenmap$;
    private static final Object REPLACE_VALUE;

    private NativeRegExp(Global global) {
        super(global.getRegExpPrototype(), $nasgenmap$);
        this.globalObject = global;
    }

    NativeRegExp(String input, String flagString, Global global, ScriptObject proto) {
        super(proto, $nasgenmap$);
        try {
            this.regexp = RegExpFactory.create(input, flagString);
        }
        catch (ParserException e) {
            e.throwAsEcmaException();
            throw new AssertionError();
        }
        this.globalObject = global;
        this.setLastIndex(0);
    }

    NativeRegExp(String input, String flagString, Global global) {
        this(input, flagString, global, global.getRegExpPrototype());
    }

    NativeRegExp(String input, String flagString) {
        this(input, flagString, Global.instance());
    }

    NativeRegExp(String string, Global global) {
        this(string, "", global);
    }

    NativeRegExp(String string) {
        this(string, Global.instance());
    }

    NativeRegExp(NativeRegExp regExp) {
        this(Global.instance());
        this.lastIndex = regExp.getLastIndexObject();
        this.regexp = regExp.getRegExp();
    }

    @Override
    public String getClassName() {
        return "RegExp";
    }

    public static NativeRegExp constructor(boolean isNew, Object self, Object ... args2) {
        if (args2.length > 1) {
            return NativeRegExp.newRegExp(args2[0], args2[1]);
        }
        if (args2.length > 0) {
            return NativeRegExp.newRegExp(args2[0], ScriptRuntime.UNDEFINED);
        }
        return NativeRegExp.newRegExp(ScriptRuntime.UNDEFINED, ScriptRuntime.UNDEFINED);
    }

    public static NativeRegExp constructor(boolean isNew, Object self) {
        return new NativeRegExp("", "");
    }

    public static NativeRegExp constructor(boolean isNew, Object self, Object pattern) {
        return NativeRegExp.newRegExp(pattern, ScriptRuntime.UNDEFINED);
    }

    public static NativeRegExp constructor(boolean isNew, Object self, Object pattern, Object flags) {
        return NativeRegExp.newRegExp(pattern, flags);
    }

    public static NativeRegExp newRegExp(Object regexp, Object flags) {
        String patternString = "";
        String flagString = "";
        if (regexp != ScriptRuntime.UNDEFINED) {
            if (regexp instanceof NativeRegExp) {
                if (flags != ScriptRuntime.UNDEFINED) {
                    throw ECMAErrors.typeError("regex.cant.supply.flags", new String[0]);
                }
                return (NativeRegExp)regexp;
            }
            patternString = JSType.toString(regexp);
        }
        if (flags != ScriptRuntime.UNDEFINED) {
            flagString = JSType.toString(flags);
        }
        return new NativeRegExp(patternString, flagString);
    }

    static NativeRegExp flatRegExp(String string) {
        StringBuilder sb = null;
        int length = string.length();
        block3: for (int i = 0; i < length; ++i) {
            char c = string.charAt(i);
            switch (c) {
                case '$': 
                case '(': 
                case ')': 
                case '*': 
                case '+': 
                case '.': 
                case '?': 
                case '[': 
                case '\\': 
                case '^': 
                case '{': 
                case '|': {
                    if (sb == null) {
                        sb = new StringBuilder(length * 2);
                        sb.append(string, 0, i);
                    }
                    sb.append('\\');
                    sb.append(c);
                    continue block3;
                }
                default: {
                    if (sb == null) continue block3;
                    sb.append(c);
                }
            }
        }
        return new NativeRegExp(sb == null ? string : sb.toString(), "");
    }

    private String getFlagString() {
        StringBuilder sb = new StringBuilder(3);
        if (this.regexp.isGlobal()) {
            sb.append('g');
        }
        if (this.regexp.isIgnoreCase()) {
            sb.append('i');
        }
        if (this.regexp.isMultiline()) {
            sb.append('m');
        }
        return sb.toString();
    }

    @Override
    public String safeToString() {
        return "[RegExp " + this.toString() + "]";
    }

    public String toString() {
        return "/" + this.regexp.getSource() + "/" + this.getFlagString();
    }

    public static ScriptObject compile(Object self, Object pattern, Object flags) {
        NativeRegExp regExp = NativeRegExp.checkRegExp(self);
        NativeRegExp compiled = NativeRegExp.newRegExp(pattern, flags);
        regExp.setRegExp(compiled.getRegExp());
        return regExp;
    }

    public static ScriptObject exec(Object self, Object string) {
        return NativeRegExp.checkRegExp(self).exec(JSType.toString(string));
    }

    public static boolean test(Object self, Object string) {
        return NativeRegExp.checkRegExp(self).test(JSType.toString(string));
    }

    public static String toString(Object self) {
        return NativeRegExp.checkRegExp(self).toString();
    }

    public static Object source(Object self) {
        return NativeRegExp.checkRegExp(self).getRegExp().getSource();
    }

    public static Object global(Object self) {
        return NativeRegExp.checkRegExp(self).getRegExp().isGlobal();
    }

    public static Object ignoreCase(Object self) {
        return NativeRegExp.checkRegExp(self).getRegExp().isIgnoreCase();
    }

    public static Object multiline(Object self) {
        return NativeRegExp.checkRegExp(self).getRegExp().isMultiline();
    }

    public static Object getLastInput(Object self) {
        RegExpResult match = Global.instance().getLastRegExpResult();
        return match == null ? "" : match.getInput();
    }

    public static Object getLastMultiline(Object self) {
        return false;
    }

    public static Object getLastMatch(Object self) {
        RegExpResult match = Global.instance().getLastRegExpResult();
        return match == null ? "" : match.getGroup(0);
    }

    public static Object getLastParen(Object self) {
        RegExpResult match = Global.instance().getLastRegExpResult();
        return match == null ? "" : match.getLastParen();
    }

    public static Object getLeftContext(Object self) {
        RegExpResult match = Global.instance().getLastRegExpResult();
        return match == null ? "" : match.getInput().substring(0, match.getIndex());
    }

    public static Object getRightContext(Object self) {
        RegExpResult match = Global.instance().getLastRegExpResult();
        return match == null ? "" : match.getInput().substring(match.getIndex() + match.length());
    }

    public static Object getGroup1(Object self) {
        RegExpResult match = Global.instance().getLastRegExpResult();
        return match == null ? "" : match.getGroup(1);
    }

    public static Object getGroup2(Object self) {
        RegExpResult match = Global.instance().getLastRegExpResult();
        return match == null ? "" : match.getGroup(2);
    }

    public static Object getGroup3(Object self) {
        RegExpResult match = Global.instance().getLastRegExpResult();
        return match == null ? "" : match.getGroup(3);
    }

    public static Object getGroup4(Object self) {
        RegExpResult match = Global.instance().getLastRegExpResult();
        return match == null ? "" : match.getGroup(4);
    }

    public static Object getGroup5(Object self) {
        RegExpResult match = Global.instance().getLastRegExpResult();
        return match == null ? "" : match.getGroup(5);
    }

    public static Object getGroup6(Object self) {
        RegExpResult match = Global.instance().getLastRegExpResult();
        return match == null ? "" : match.getGroup(6);
    }

    public static Object getGroup7(Object self) {
        RegExpResult match = Global.instance().getLastRegExpResult();
        return match == null ? "" : match.getGroup(7);
    }

    public static Object getGroup8(Object self) {
        RegExpResult match = Global.instance().getLastRegExpResult();
        return match == null ? "" : match.getGroup(8);
    }

    public static Object getGroup9(Object self) {
        RegExpResult match = Global.instance().getLastRegExpResult();
        return match == null ? "" : match.getGroup(9);
    }

    private RegExpResult execInner(String string) {
        boolean isGlobal = this.regexp.isGlobal();
        int start = this.getLastIndex();
        if (!isGlobal) {
            start = 0;
        }
        if (start < 0 || start > string.length()) {
            if (isGlobal) {
                this.setLastIndex(0);
            }
            return null;
        }
        RegExpMatcher matcher = this.regexp.match(string);
        if (matcher == null || !matcher.search(start)) {
            if (isGlobal) {
                this.setLastIndex(0);
            }
            return null;
        }
        if (isGlobal) {
            this.setLastIndex(matcher.end());
        }
        RegExpResult match = new RegExpResult(string, matcher.start(), this.groups(matcher));
        this.globalObject.setLastRegExpResult(match);
        return match;
    }

    private RegExpResult execSplit(String string, int start) {
        if (start < 0 || start > string.length()) {
            return null;
        }
        RegExpMatcher matcher = this.regexp.match(string);
        if (matcher == null || !matcher.search(start)) {
            return null;
        }
        RegExpResult match = new RegExpResult(string, matcher.start(), this.groups(matcher));
        this.globalObject.setLastRegExpResult(match);
        return match;
    }

    private Object[] groups(RegExpMatcher matcher) {
        int groupCount = matcher.groupCount();
        Object[] groups2 = new Object[groupCount + 1];
        BitVector groupsInNegativeLookahead = this.regexp.getGroupsInNegativeLookahead();
        int lastGroupStart = matcher.start();
        for (int i = 0; i <= groupCount; ++i) {
            int groupStart = matcher.start(i);
            if (lastGroupStart > groupStart || groupsInNegativeLookahead != null && groupsInNegativeLookahead.isSet(i)) {
                groups2[i] = ScriptRuntime.UNDEFINED;
                continue;
            }
            String group = matcher.group(i);
            groups2[i] = group == null ? ScriptRuntime.UNDEFINED : group;
            lastGroupStart = groupStart;
        }
        return groups2;
    }

    public NativeRegExpExecResult exec(String string) {
        RegExpResult match = this.execInner(string);
        if (match == null) {
            return null;
        }
        return new NativeRegExpExecResult(match, this.globalObject);
    }

    public boolean test(String string) {
        return this.execInner(string) != null;
    }

    String replace(String string, String replacement, Object function) throws Throwable {
        RegExpMatcher matcher = this.regexp.match(string);
        if (matcher == null) {
            return string;
        }
        if (!this.regexp.isGlobal()) {
            if (!matcher.search(0)) {
                return string;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(string, 0, matcher.start());
            if (function != null) {
                PropertyAccess self = Bootstrap.isStrictCallable(function) ? ScriptRuntime.UNDEFINED : Global.instance();
                sb.append(this.callReplaceValue(NativeRegExp.getReplaceValueInvoker(), function, self, matcher, string));
            } else {
                this.appendReplacement(matcher, string, replacement, sb);
            }
            sb.append(string, matcher.end(), string.length());
            return sb.toString();
        }
        this.setLastIndex(0);
        if (!matcher.search(0)) {
            return string;
        }
        int thisIndex = 0;
        int previousLastIndex = 0;
        StringBuilder sb = new StringBuilder();
        MethodHandle invoker = function == null ? null : NativeRegExp.getReplaceValueInvoker();
        Undefined self = function == null || Bootstrap.isStrictCallable(function) ? ScriptRuntime.UNDEFINED : Global.instance();
        do {
            sb.append(string, thisIndex, matcher.start());
            if (function != null) {
                sb.append(this.callReplaceValue(invoker, function, self, matcher, string));
            } else {
                this.appendReplacement(matcher, string, replacement, sb);
            }
            thisIndex = matcher.end();
            if (thisIndex == string.length() && matcher.start() == matcher.end()) break;
            if (thisIndex == previousLastIndex) {
                this.setLastIndex(thisIndex + 1);
                previousLastIndex = thisIndex + 1;
                continue;
            }
            previousLastIndex = thisIndex;
        } while (previousLastIndex <= string.length() && matcher.search(previousLastIndex));
        sb.append(string, thisIndex, string.length());
        return sb.toString();
    }

    private void appendReplacement(RegExpMatcher matcher, String text, String replacement, StringBuilder sb) {
        int cursor = 0;
        Object[] groups2 = null;
        while (cursor < replacement.length()) {
            char nextChar = replacement.charAt(cursor);
            if (nextChar == '$') {
                if (++cursor == replacement.length()) {
                    sb.append('$');
                    break;
                }
                nextChar = replacement.charAt(cursor);
                int firstDigit = nextChar - 48;
                if (firstDigit >= 0 && firstDigit <= 9 && firstDigit <= matcher.groupCount()) {
                    int newRefNum;
                    int secondDigit;
                    int refNum = firstDigit;
                    if (++cursor < replacement.length() && firstDigit < matcher.groupCount() && (secondDigit = replacement.charAt(cursor) - 48) >= 0 && secondDigit <= 9 && (newRefNum = firstDigit * 10 + secondDigit) <= matcher.groupCount() && newRefNum > 0) {
                        refNum = newRefNum;
                        ++cursor;
                    }
                    if (refNum > 0) {
                        if (groups2 == null) {
                            groups2 = this.groups(matcher);
                        }
                        if (groups2[refNum] == ScriptRuntime.UNDEFINED) continue;
                        sb.append((String)groups2[refNum]);
                        continue;
                    }
                    assert (refNum == 0);
                    sb.append("$0");
                    continue;
                }
                if (nextChar == '$') {
                    sb.append('$');
                    ++cursor;
                    continue;
                }
                if (nextChar == '&') {
                    sb.append(matcher.group());
                    ++cursor;
                    continue;
                }
                if (nextChar == '`') {
                    sb.append(text, 0, matcher.start());
                    ++cursor;
                    continue;
                }
                if (nextChar == '\'') {
                    sb.append(text, matcher.end(), text.length());
                    ++cursor;
                    continue;
                }
                sb.append('$');
                continue;
            }
            sb.append(nextChar);
            ++cursor;
        }
    }

    private static final MethodHandle getReplaceValueInvoker() {
        return Global.instance().getDynamicInvoker(REPLACE_VALUE, new Callable<MethodHandle>(){

            @Override
            public MethodHandle call() {
                return Bootstrap.createDynamicInvoker("dyn:call", String.class, Object.class, Object.class, Object[].class);
            }
        });
    }

    private String callReplaceValue(MethodHandle invoker, Object function, Object self, RegExpMatcher matcher, String string) throws Throwable {
        Object[] groups2 = this.groups(matcher);
        Object[] args2 = Arrays.copyOf(groups2, groups2.length + 2);
        args2[groups2.length] = matcher.start();
        args2[groups2.length + 1] = string;
        return invoker.invokeExact(function, self, args2);
    }

    NativeArray split(String string, long limit) {
        RegExpResult match;
        if (limit == 0L) {
            return new NativeArray();
        }
        ArrayList<Object> matches = new ArrayList<Object>();
        int inputLength = string.length();
        int splitLastLength = -1;
        int splitLastIndex = 0;
        int splitLastLastIndex = 0;
        while ((match = this.execSplit(string, splitLastIndex)) != null) {
            splitLastIndex = match.getIndex() + match.length();
            if (splitLastIndex > splitLastLastIndex) {
                matches.add(string.substring(splitLastLastIndex, match.getIndex()));
                Object[] groups2 = match.getGroups();
                if (groups2.length > 1 && match.getIndex() < inputLength) {
                    for (int index = 1; index < groups2.length && (long)matches.size() < limit; ++index) {
                        matches.add(groups2[index]);
                    }
                }
                splitLastLength = match.length();
                if ((long)matches.size() >= limit) break;
            }
            if (splitLastIndex == splitLastLastIndex) {
                ++splitLastIndex;
                continue;
            }
            splitLastLastIndex = splitLastIndex;
        }
        if ((long)matches.size() < limit) {
            if (splitLastLastIndex == string.length()) {
                if (splitLastLength > 0 || this.execSplit("", 0) == null) {
                    matches.add("");
                }
            } else {
                matches.add(string.substring(splitLastLastIndex, inputLength));
            }
        }
        return new NativeArray(matches.toArray());
    }

    int search(String string) {
        RegExpResult match = this.execInner(string);
        if (match == null) {
            return -1;
        }
        return match.getIndex();
    }

    public int getLastIndex() {
        return JSType.toInteger(this.lastIndex);
    }

    public Object getLastIndexObject() {
        return this.lastIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = JSType.toObject(lastIndex);
    }

    private static NativeRegExp checkRegExp(Object self) {
        if (self instanceof NativeRegExp) {
            return (NativeRegExp)self;
        }
        if (self != null && self == Global.instance().getRegExpPrototype()) {
            return Global.instance().getDefaultRegExp();
        }
        throw ECMAErrors.typeError("not.a.regexp", ScriptRuntime.safeToString(self));
    }

    boolean getGlobal() {
        return this.regexp.isGlobal();
    }

    private RegExp getRegExp() {
        return this.regexp;
    }

    private void setRegExp(RegExp regexp) {
        this.regexp = regexp;
    }

    static {
        REPLACE_VALUE = new Object();
        NativeRegExp.$clinit$();
    }

    public static void $clinit$() {
        ArrayList<Property> arrayList = new ArrayList<Property>(5);
        arrayList.add(AccessorProperty.create("lastIndex", 6, cfr_ldc_0(), cfr_ldc_1()));
        arrayList.add(AccessorProperty.create("source", 7, cfr_ldc_2(), null));
        arrayList.add(AccessorProperty.create("global", 7, cfr_ldc_3(), null));
        arrayList.add(AccessorProperty.create("ignoreCase", 7, cfr_ldc_4(), null));
        arrayList.add(AccessorProperty.create("multiline", 7, cfr_ldc_5(), null));
        $nasgenmap$ = PropertyMap.newMap(arrayList);
    }

    public Object G$lastIndex() {
        return this.lastIndex;
    }

    public void S$lastIndex(Object object) {
        this.lastIndex = object;
    }

    /*
     * Works around MethodHandle LDC.
     */
    static MethodHandle cfr_ldc_0() {
        try {
            return MethodHandles.lookup().findVirtual(NativeRegExp.class, "G$lastIndex", MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findVirtual(NativeRegExp.class, "S$lastIndex", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", null));
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
            return MethodHandles.lookup().findStatic(NativeRegExp.class, "source", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findStatic(NativeRegExp.class, "global", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findStatic(NativeRegExp.class, "ignoreCase", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
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
            return MethodHandles.lookup().findStatic(NativeRegExp.class, "multiline", MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;", null));
        }
        catch (NoSuchMethodException | IllegalAccessException except) {
            throw new IllegalArgumentException(except);
        }
    }
}

