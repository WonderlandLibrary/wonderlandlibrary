/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.util.List;
import java.util.Objects;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.GuardedInvocationFilter;
import jdk.internal.dynalink.NoSuchDynamicMethodException;
import jdk.internal.dynalink.RelinkableCallSite;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.support.LinkRequestImpl;
import jdk.internal.dynalink.support.Lookup;
import jdk.internal.dynalink.support.RuntimeContextLinkRequestImpl;

public class DynamicLinker {
    private static final String CLASS_NAME = DynamicLinker.class.getName();
    private static final String RELINK_METHOD_NAME = "relink";
    private static final String INITIAL_LINK_CLASS_NAME = "java.lang.invoke.MethodHandleNatives";
    private static final String INITIAL_LINK_METHOD_NAME = "linkCallSite";
    private final LinkerServices linkerServices;
    private final GuardedInvocationFilter prelinkFilter;
    private final int runtimeContextArgCount;
    private final boolean syncOnRelink;
    private final int unstableRelinkThreshold;
    private static final MethodHandle RELINK = Lookup.findOwnSpecial(MethodHandles.lookup(), "relink", MethodHandle.class, RelinkableCallSite.class, Integer.TYPE, Object[].class);

    DynamicLinker(LinkerServices linkerServices, GuardedInvocationFilter prelinkFilter, int runtimeContextArgCount, boolean syncOnRelink, int unstableRelinkThreshold) {
        if (runtimeContextArgCount < 0) {
            throw new IllegalArgumentException("runtimeContextArgCount < 0");
        }
        if (unstableRelinkThreshold < 0) {
            throw new IllegalArgumentException("unstableRelinkThreshold < 0");
        }
        this.linkerServices = linkerServices;
        this.prelinkFilter = prelinkFilter;
        this.runtimeContextArgCount = runtimeContextArgCount;
        this.syncOnRelink = syncOnRelink;
        this.unstableRelinkThreshold = unstableRelinkThreshold;
    }

    public <T extends RelinkableCallSite> T link(T callSite) {
        callSite.initialize(this.createRelinkAndInvokeMethod(callSite, 0));
        return callSite;
    }

    public LinkerServices getLinkerServices() {
        return this.linkerServices;
    }

    private MethodHandle createRelinkAndInvokeMethod(RelinkableCallSite callSite, int relinkCount) {
        MethodHandle boundRelinker = MethodHandles.insertArguments(RELINK, 0, this, callSite, relinkCount);
        MethodType type = callSite.getDescriptor().getMethodType();
        MethodHandle collectingRelinker = boundRelinker.asCollector(Object[].class, type.parameterCount());
        return MethodHandles.foldArguments(MethodHandles.exactInvoker(type), collectingRelinker.asType(type.changeReturnType(MethodHandle.class)));
    }

    private MethodHandle relink(RelinkableCallSite callSite, int relinkCount, Object ... arguments) throws Exception {
        CallSiteDescriptor callSiteDescriptor = callSite.getDescriptor();
        boolean unstableDetectionEnabled = this.unstableRelinkThreshold > 0;
        boolean callSiteUnstable = unstableDetectionEnabled && relinkCount >= this.unstableRelinkThreshold;
        LinkRequestImpl linkRequest = this.runtimeContextArgCount == 0 ? new LinkRequestImpl(callSiteDescriptor, callSite, relinkCount, callSiteUnstable, arguments) : new RuntimeContextLinkRequestImpl(callSiteDescriptor, (Object)callSite, relinkCount, callSiteUnstable, arguments, this.runtimeContextArgCount);
        GuardedInvocation guardedInvocation = this.linkerServices.getGuardedInvocation(linkRequest);
        if (guardedInvocation == null) {
            throw new NoSuchDynamicMethodException(callSiteDescriptor.toString());
        }
        if (this.runtimeContextArgCount > 0) {
            MethodType origType = callSiteDescriptor.getMethodType();
            MethodHandle invocation = guardedInvocation.getInvocation();
            if (invocation.type().parameterCount() == origType.parameterCount() - this.runtimeContextArgCount) {
                List<Class<?>> prefix = origType.parameterList().subList(1, this.runtimeContextArgCount + 1);
                MethodHandle guard = guardedInvocation.getGuard();
                guardedInvocation = guardedInvocation.dropArguments(1, prefix);
            }
        }
        guardedInvocation = this.prelinkFilter.filter(guardedInvocation, linkRequest, this.linkerServices);
        Objects.requireNonNull(guardedInvocation);
        int newRelinkCount = relinkCount;
        if (unstableDetectionEnabled && newRelinkCount <= this.unstableRelinkThreshold && newRelinkCount++ == this.unstableRelinkThreshold) {
            callSite.resetAndRelink(guardedInvocation, this.createRelinkAndInvokeMethod(callSite, newRelinkCount));
        } else {
            callSite.relink(guardedInvocation, this.createRelinkAndInvokeMethod(callSite, newRelinkCount));
        }
        if (this.syncOnRelink) {
            MutableCallSite.syncAll(new MutableCallSite[]{(MutableCallSite)((Object)callSite)});
        }
        return guardedInvocation.getInvocation();
    }

    public static StackTraceElement getLinkedCallSiteLocation() {
        StackTraceElement[] trace = new Throwable().getStackTrace();
        for (int i = 0; i < trace.length - 1; ++i) {
            StackTraceElement frame = trace[i];
            if (!DynamicLinker.isRelinkFrame(frame) && !DynamicLinker.isInitialLinkFrame(frame)) continue;
            return trace[i + 1];
        }
        return null;
    }

    @Deprecated
    public static StackTraceElement getRelinkedCallSiteLocation() {
        return DynamicLinker.getLinkedCallSiteLocation();
    }

    private static boolean isInitialLinkFrame(StackTraceElement frame) {
        return DynamicLinker.testFrame(frame, INITIAL_LINK_METHOD_NAME, INITIAL_LINK_CLASS_NAME);
    }

    private static boolean isRelinkFrame(StackTraceElement frame) {
        return DynamicLinker.testFrame(frame, RELINK_METHOD_NAME, CLASS_NAME);
    }

    private static boolean testFrame(StackTraceElement frame, String methodName, String className) {
        return methodName.equals(frame.getMethodName()) && className.equals(frame.getClassName());
    }
}

