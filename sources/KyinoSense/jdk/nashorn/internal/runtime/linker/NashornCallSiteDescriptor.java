/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.linker;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.support.AbstractCallSiteDescriptor;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.nashorn.internal.runtime.ScriptRuntime;

public final class NashornCallSiteDescriptor
extends AbstractCallSiteDescriptor {
    public static final int CALLSITE_SCOPE = 1;
    public static final int CALLSITE_STRICT = 2;
    public static final int CALLSITE_FAST_SCOPE = 4;
    public static final int CALLSITE_OPTIMISTIC = 8;
    public static final int CALLSITE_APPLY_TO_CALL = 16;
    public static final int CALLSITE_DECLARE = 32;
    public static final int CALLSITE_PROFILE = 64;
    public static final int CALLSITE_TRACE = 128;
    public static final int CALLSITE_TRACE_MISSES = 256;
    public static final int CALLSITE_TRACE_ENTEREXIT = 512;
    public static final int CALLSITE_TRACE_VALUES = 1024;
    public static final int CALLSITE_PROGRAM_POINT_SHIFT = 11;
    public static final int MAX_PROGRAM_POINT_VALUE = 0x1FFFFF;
    public static final int FLAGS_MASK = 2047;
    private static final ClassValue<ConcurrentMap<NashornCallSiteDescriptor, NashornCallSiteDescriptor>> canonicals = new ClassValue<ConcurrentMap<NashornCallSiteDescriptor, NashornCallSiteDescriptor>>(){

        @Override
        protected ConcurrentMap<NashornCallSiteDescriptor, NashornCallSiteDescriptor> computeValue(Class<?> type) {
            return new ConcurrentHashMap<NashornCallSiteDescriptor, NashornCallSiteDescriptor>();
        }
    };
    private final MethodHandles.Lookup lookup;
    private final String operator;
    private final String operand;
    private final MethodType methodType;
    private final int flags;

    public static String toString(int flags) {
        StringBuilder sb = new StringBuilder();
        if ((flags & 1) != 0) {
            if ((flags & 4) != 0) {
                sb.append("fastscope ");
            } else {
                assert ((flags & 4) == 0) : "can't be fastscope without scope";
                sb.append("scope ");
            }
            if ((flags & 0x20) != 0) {
                sb.append("declare ");
            }
        }
        if ((flags & 0x10) != 0) {
            sb.append("apply2call ");
        }
        if ((flags & 2) != 0) {
            sb.append("strict ");
        }
        return sb.length() == 0 ? "" : " " + sb.toString().trim();
    }

    public static NashornCallSiteDescriptor get(MethodHandles.Lookup lookup, String name, MethodType methodType, int flags) {
        String[] tokenizedName = CallSiteDescriptorFactory.tokenizeName(name);
        assert (tokenizedName.length >= 2);
        assert ("dyn".equals(tokenizedName[0]));
        assert (tokenizedName[1] != null);
        return NashornCallSiteDescriptor.get(lookup, tokenizedName[1], tokenizedName.length == 3 ? tokenizedName[2].intern() : null, methodType, flags);
    }

    private static NashornCallSiteDescriptor get(MethodHandles.Lookup lookup, String operator, String operand, MethodType methodType, int flags) {
        NashornCallSiteDescriptor csd = new NashornCallSiteDescriptor(lookup, operator, operand, methodType, flags);
        ConcurrentMap<NashornCallSiteDescriptor, NashornCallSiteDescriptor> classCanonicals = canonicals.get(lookup.lookupClass());
        NashornCallSiteDescriptor canonical = classCanonicals.putIfAbsent(csd, csd);
        return canonical != null ? canonical : csd;
    }

    private NashornCallSiteDescriptor(MethodHandles.Lookup lookup, String operator, String operand, MethodType methodType, int flags) {
        this.lookup = lookup;
        this.operator = operator;
        this.operand = operand;
        this.methodType = methodType;
        this.flags = flags;
    }

    @Override
    public int getNameTokenCount() {
        return this.operand == null ? 2 : 3;
    }

    @Override
    public String getNameToken(int i) {
        switch (i) {
            case 0: {
                return "dyn";
            }
            case 1: {
                return this.operator;
            }
            case 2: {
                if (this.operand == null) break;
                return this.operand;
            }
        }
        throw new IndexOutOfBoundsException(String.valueOf(i));
    }

    @Override
    public MethodHandles.Lookup getLookup() {
        return this.lookup;
    }

    @Override
    public boolean equals(CallSiteDescriptor csd) {
        return super.equals(csd) && this.flags == NashornCallSiteDescriptor.getFlags(csd);
    }

    @Override
    public MethodType getMethodType() {
        return this.methodType;
    }

    public String getOperator() {
        return this.operator;
    }

    public String getFirstOperator() {
        int delim = this.operator.indexOf("|");
        return delim == -1 ? this.operator : this.operator.substring(0, delim);
    }

    public String getOperand() {
        return this.operand;
    }

    public String getFunctionDescription() {
        assert (this.getFirstOperator().equals("call") || this.getFirstOperator().equals("new"));
        return this.getNameTokenCount() > 2 ? this.getNameToken(2) : null;
    }

    public static String getFunctionDescription(CallSiteDescriptor desc) {
        return desc instanceof NashornCallSiteDescriptor ? ((NashornCallSiteDescriptor)desc).getFunctionDescription() : null;
    }

    public String getFunctionErrorMessage(Object obj) {
        String funcDesc = this.getFunctionDescription();
        return funcDesc != null ? funcDesc : ScriptRuntime.safeToString(obj);
    }

    public static String getFunctionErrorMessage(CallSiteDescriptor desc, Object obj) {
        return desc instanceof NashornCallSiteDescriptor ? ((NashornCallSiteDescriptor)desc).getFunctionErrorMessage(obj) : ScriptRuntime.safeToString(obj);
    }

    public static int getFlags(CallSiteDescriptor desc) {
        return desc instanceof NashornCallSiteDescriptor ? ((NashornCallSiteDescriptor)desc).flags : 0;
    }

    private boolean isFlag(int flag) {
        return (this.flags & flag) != 0;
    }

    private static boolean isFlag(CallSiteDescriptor desc, int flag) {
        return (NashornCallSiteDescriptor.getFlags(desc) & flag) != 0;
    }

    public static boolean isScope(CallSiteDescriptor desc) {
        return NashornCallSiteDescriptor.isFlag(desc, 1);
    }

    public static boolean isFastScope(CallSiteDescriptor desc) {
        return NashornCallSiteDescriptor.isFlag(desc, 4);
    }

    public static boolean isStrict(CallSiteDescriptor desc) {
        return NashornCallSiteDescriptor.isFlag(desc, 2);
    }

    public static boolean isApplyToCall(CallSiteDescriptor desc) {
        return NashornCallSiteDescriptor.isFlag(desc, 16);
    }

    public static boolean isOptimistic(CallSiteDescriptor desc) {
        return NashornCallSiteDescriptor.isFlag(desc, 8);
    }

    public static boolean isDeclaration(CallSiteDescriptor desc) {
        return NashornCallSiteDescriptor.isFlag(desc, 32);
    }

    public static boolean isStrictFlag(int flags) {
        return (flags & 2) != 0;
    }

    public static boolean isScopeFlag(int flags) {
        return (flags & 1) != 0;
    }

    public static int getProgramPoint(CallSiteDescriptor desc) {
        assert (NashornCallSiteDescriptor.isOptimistic(desc)) : "program point requested from non-optimistic descriptor " + desc;
        return NashornCallSiteDescriptor.getFlags(desc) >> 11;
    }

    boolean isProfile() {
        return this.isFlag(64);
    }

    boolean isTrace() {
        return this.isFlag(128);
    }

    boolean isTraceMisses() {
        return this.isFlag(256);
    }

    boolean isTraceEnterExit() {
        return this.isFlag(512);
    }

    boolean isTraceObjects() {
        return this.isFlag(1024);
    }

    boolean isOptimistic() {
        return this.isFlag(8);
    }

    @Override
    public CallSiteDescriptor changeMethodType(MethodType newMethodType) {
        return NashornCallSiteDescriptor.get(this.getLookup(), this.operator, this.operand, newMethodType, this.flags);
    }
}

