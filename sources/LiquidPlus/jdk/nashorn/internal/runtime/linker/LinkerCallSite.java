/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.linker;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import jdk.internal.dynalink.ChainedCallSite;
import jdk.internal.dynalink.DynamicLinker;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.Debug;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;
import jdk.nashorn.internal.runtime.options.Options;

public class LinkerCallSite
extends ChainedCallSite {
    public static final int ARGLIMIT = 250;
    private static final String PROFILEFILE = Options.getStringProperty("nashorn.profilefile", "NashornProfile.txt");
    private static final MethodHandle INCREASE_MISS_COUNTER = Lookup.MH.findStatic(MethodHandles.lookup(), LinkerCallSite.class, "increaseMissCount", Lookup.MH.type(Object.class, String.class, Object.class));
    private static final MethodHandle ON_CATCH_INVALIDATION = Lookup.MH.findStatic(MethodHandles.lookup(), LinkerCallSite.class, "onCatchInvalidation", Lookup.MH.type(ChainedCallSite.class, LinkerCallSite.class));
    private int catchInvalidations;
    private static LongAdder count;
    private static final HashMap<String, AtomicInteger> missCounts;
    private static LongAdder missCount;
    private static final Random r;
    private static final int missSamplingPercentage;

    LinkerCallSite(NashornCallSiteDescriptor descriptor) {
        super(descriptor);
        if (Context.DEBUG) {
            count.increment();
        }
    }

    @Override
    protected MethodHandle getPruneCatches() {
        return Lookup.MH.filterArguments(super.getPruneCatches(), 0, ON_CATCH_INVALIDATION);
    }

    private static ChainedCallSite onCatchInvalidation(LinkerCallSite callSite) {
        ++callSite.catchInvalidations;
        return callSite;
    }

    public static int getCatchInvalidationCount(Object callSiteToken) {
        if (callSiteToken instanceof LinkerCallSite) {
            return ((LinkerCallSite)callSiteToken).catchInvalidations;
        }
        return 0;
    }

    static LinkerCallSite newLinkerCallSite(MethodHandles.Lookup lookup, String name, MethodType type, int flags) {
        NashornCallSiteDescriptor desc = NashornCallSiteDescriptor.get(lookup, name, type, flags);
        if (desc.isProfile()) {
            return ProfilingLinkerCallSite.newProfilingLinkerCallSite(desc);
        }
        if (desc.isTrace()) {
            return new TracingLinkerCallSite(desc);
        }
        return new LinkerCallSite(desc);
    }

    public String toString() {
        return this.getDescriptor().toString();
    }

    public NashornCallSiteDescriptor getNashornDescriptor() {
        return (NashornCallSiteDescriptor)this.getDescriptor();
    }

    @Override
    public void relink(GuardedInvocation invocation, MethodHandle relink) {
        super.relink(invocation, this.getDebuggingRelink(relink));
    }

    @Override
    public void resetAndRelink(GuardedInvocation invocation, MethodHandle relink) {
        super.resetAndRelink(invocation, this.getDebuggingRelink(relink));
    }

    private MethodHandle getDebuggingRelink(MethodHandle relink) {
        if (Context.DEBUG) {
            return Lookup.MH.filterArguments(relink, 0, this.getIncreaseMissCounter(relink.type().parameterType(0)));
        }
        return relink;
    }

    private MethodHandle getIncreaseMissCounter(Class<?> type) {
        MethodHandle missCounterWithDesc = Lookup.MH.bindTo(INCREASE_MISS_COUNTER, this.getDescriptor().getName() + " @ " + LinkerCallSite.getScriptLocation());
        if (type == Object.class) {
            return missCounterWithDesc;
        }
        return Lookup.MH.asType(missCounterWithDesc, missCounterWithDesc.type().changeParameterType(0, type).changeReturnType(type));
    }

    private static String getScriptLocation() {
        StackTraceElement caller = DynamicLinker.getLinkedCallSiteLocation();
        return caller == null ? "unknown location" : caller.getFileName() + ":" + caller.getLineNumber();
    }

    public static Object increaseMissCount(String desc, Object self) {
        missCount.increment();
        if (r.nextInt(100) < missSamplingPercentage) {
            AtomicInteger i = missCounts.get(desc);
            if (i == null) {
                missCounts.put(desc, new AtomicInteger(1));
            } else {
                i.incrementAndGet();
            }
        }
        return self;
    }

    @Override
    protected int getMaxChainLength() {
        return 8;
    }

    public static long getCount() {
        return count.longValue();
    }

    public static long getMissCount() {
        return missCount.longValue();
    }

    public static int getMissSamplingPercentage() {
        return missSamplingPercentage;
    }

    public static void getMissCounts(PrintWriter out) {
        ArrayList<Map.Entry<String, AtomicInteger>> entries = new ArrayList<Map.Entry<String, AtomicInteger>>(missCounts.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, AtomicInteger>>(){

            @Override
            public int compare(Map.Entry<String, AtomicInteger> o1, Map.Entry<String, AtomicInteger> o2) {
                return o2.getValue().get() - o1.getValue().get();
            }
        });
        for (Map.Entry<String, AtomicInteger> entry : entries) {
            out.println("  " + entry.getKey() + "\t" + entry.getValue().get());
        }
    }

    static {
        missCounts = new HashMap();
        r = new Random();
        missSamplingPercentage = Options.getIntProperty("nashorn.tcs.miss.samplePercent", 1);
        if (Context.DEBUG) {
            count = new LongAdder();
            missCount = new LongAdder();
        }
    }

    private static class TracingLinkerCallSite
    extends LinkerCallSite {
        private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
        private static final MethodHandle TRACEOBJECT = Lookup.MH.findVirtual(LOOKUP, TracingLinkerCallSite.class, "traceObject", Lookup.MH.type(Object.class, MethodHandle.class, Object[].class));
        private static final MethodHandle TRACEVOID = Lookup.MH.findVirtual(LOOKUP, TracingLinkerCallSite.class, "traceVoid", Lookup.MH.type(Void.TYPE, MethodHandle.class, Object[].class));
        private static final MethodHandle TRACEMISS = Lookup.MH.findVirtual(LOOKUP, TracingLinkerCallSite.class, "traceMiss", Lookup.MH.type(Void.TYPE, String.class, Object[].class));

        TracingLinkerCallSite(NashornCallSiteDescriptor desc) {
            super(desc);
        }

        @Override
        public void setTarget(MethodHandle newTarget) {
            if (!this.getNashornDescriptor().isTraceEnterExit()) {
                super.setTarget(newTarget);
                return;
            }
            MethodType type = this.type();
            boolean isVoid = type.returnType() == Void.TYPE;
            MethodHandle traceMethodHandle = isVoid ? TRACEVOID : TRACEOBJECT;
            traceMethodHandle = Lookup.MH.bindTo(traceMethodHandle, this);
            traceMethodHandle = Lookup.MH.bindTo(traceMethodHandle, newTarget);
            traceMethodHandle = Lookup.MH.asCollector(traceMethodHandle, Object[].class, type.parameterCount());
            traceMethodHandle = Lookup.MH.asType(traceMethodHandle, type);
            super.setTarget(traceMethodHandle);
        }

        @Override
        public void initialize(MethodHandle relinkAndInvoke) {
            super.initialize(this.getFallbackLoggingRelink(relinkAndInvoke));
        }

        @Override
        public void relink(GuardedInvocation invocation, MethodHandle relink) {
            super.relink(invocation, this.getFallbackLoggingRelink(relink));
        }

        @Override
        public void resetAndRelink(GuardedInvocation invocation, MethodHandle relink) {
            super.resetAndRelink(invocation, this.getFallbackLoggingRelink(relink));
        }

        private MethodHandle getFallbackLoggingRelink(MethodHandle relink) {
            if (!this.getNashornDescriptor().isTraceMisses()) {
                return relink;
            }
            MethodType type = relink.type();
            return Lookup.MH.foldArguments(relink, Lookup.MH.asType(Lookup.MH.asCollector(Lookup.MH.insertArguments(TRACEMISS, 0, this, "MISS " + LinkerCallSite.getScriptLocation() + " "), Object[].class, type.parameterCount()), type.changeReturnType(Void.TYPE)));
        }

        private void printObject(PrintWriter out, Object arg) {
            if (!this.getNashornDescriptor().isTraceObjects()) {
                out.print(arg instanceof ScriptObject ? "ScriptObject" : arg);
                return;
            }
            if (arg instanceof ScriptObject) {
                ScriptObject object = (ScriptObject)arg;
                boolean isFirst = true;
                Set<Object> keySet = object.keySet();
                if (keySet.isEmpty()) {
                    out.print(ScriptRuntime.safeToString(arg));
                } else {
                    out.print("{ ");
                    for (Object key : keySet) {
                        if (!isFirst) {
                            out.print(", ");
                        }
                        out.print(key);
                        out.print(":");
                        Object value = object.get(key);
                        if (value instanceof ScriptObject) {
                            out.print("...");
                        } else {
                            this.printObject(out, value);
                        }
                        isFirst = false;
                    }
                    out.print(" }");
                }
            } else {
                out.print(ScriptRuntime.safeToString(arg));
            }
        }

        private void tracePrint(PrintWriter out, String tag, Object[] args2, Object result) {
            out.print(Debug.id(this) + " TAG " + tag);
            out.print(this.getDescriptor().getName() + "(");
            if (args2.length > 0) {
                this.printObject(out, args2[0]);
                for (int i = 1; i < args2.length; ++i) {
                    Object arg = args2[i];
                    out.print(", ");
                    if (!(arg instanceof ScriptObject) || !((ScriptObject)arg).isScope()) {
                        this.printObject(out, arg);
                        continue;
                    }
                    out.print("SCOPE");
                }
            }
            out.print(")");
            if (tag.equals("EXIT  ")) {
                out.print(" --> ");
                this.printObject(out, result);
            }
            out.println();
        }

        public Object traceObject(MethodHandle mh, Object ... args2) throws Throwable {
            PrintWriter out = Context.getCurrentErr();
            this.tracePrint(out, "ENTER ", args2, null);
            Object result = mh.invokeWithArguments(args2);
            this.tracePrint(out, "EXIT  ", args2, result);
            return result;
        }

        public void traceVoid(MethodHandle mh, Object ... args2) throws Throwable {
            PrintWriter out = Context.getCurrentErr();
            this.tracePrint(out, "ENTER ", args2, null);
            mh.invokeWithArguments(args2);
            this.tracePrint(out, "EXIT  ", args2, null);
        }

        public void traceMiss(String desc, Object ... args2) throws Throwable {
            this.tracePrint(Context.getCurrentErr(), desc, args2, null);
        }
    }

    private static class ProfilingLinkerCallSite
    extends LinkerCallSite {
        private static LinkedList<ProfilingLinkerCallSite> profileCallSites = null;
        private long startTime;
        private int depth;
        private long totalTime;
        private long hitCount;
        private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
        private static final MethodHandle PROFILEENTRY = Lookup.MH.findVirtual(LOOKUP, ProfilingLinkerCallSite.class, "profileEntry", Lookup.MH.type(Object.class, Object.class));
        private static final MethodHandle PROFILEEXIT = Lookup.MH.findVirtual(LOOKUP, ProfilingLinkerCallSite.class, "profileExit", Lookup.MH.type(Object.class, Object.class));
        private static final MethodHandle PROFILEVOIDEXIT = Lookup.MH.findVirtual(LOOKUP, ProfilingLinkerCallSite.class, "profileVoidExit", Lookup.MH.type(Void.TYPE, new Class[0]));

        ProfilingLinkerCallSite(NashornCallSiteDescriptor desc) {
            super(desc);
        }

        public static ProfilingLinkerCallSite newProfilingLinkerCallSite(NashornCallSiteDescriptor desc) {
            if (profileCallSites == null) {
                profileCallSites = new LinkedList();
                Thread profileDumperThread = new Thread(new ProfileDumper());
                Runtime.getRuntime().addShutdownHook(profileDumperThread);
            }
            ProfilingLinkerCallSite callSite = new ProfilingLinkerCallSite(desc);
            profileCallSites.add(callSite);
            return callSite;
        }

        @Override
        public void setTarget(MethodHandle newTarget) {
            MethodType type = this.type();
            boolean isVoid = type.returnType() == Void.TYPE;
            Class<?> newSelfType = newTarget.type().parameterType(0);
            MethodHandle selfFilter = Lookup.MH.bindTo(PROFILEENTRY, this);
            if (newSelfType != Object.class) {
                MethodType selfFilterType = MethodType.methodType(newSelfType, newSelfType);
                selfFilter = selfFilter.asType(selfFilterType);
            }
            MethodHandle methodHandle = Lookup.MH.filterArguments(newTarget, 0, selfFilter);
            if (isVoid) {
                methodHandle = Lookup.MH.filterReturnValue(methodHandle, Lookup.MH.bindTo(PROFILEVOIDEXIT, this));
            } else {
                MethodType filter = Lookup.MH.type(type.returnType(), type.returnType());
                methodHandle = Lookup.MH.filterReturnValue(methodHandle, Lookup.MH.asType(Lookup.MH.bindTo(PROFILEEXIT, this), filter));
            }
            super.setTarget(methodHandle);
        }

        public Object profileEntry(Object self) {
            if (this.depth == 0) {
                this.startTime = System.nanoTime();
            }
            ++this.depth;
            ++this.hitCount;
            return self;
        }

        public Object profileExit(Object result) {
            --this.depth;
            if (this.depth == 0) {
                this.totalTime += System.nanoTime() - this.startTime;
            }
            return result;
        }

        public void profileVoidExit() {
            --this.depth;
            if (this.depth == 0) {
                this.totalTime += System.nanoTime() - this.startTime;
            }
        }

        static class ProfileDumper
        implements Runnable {
            ProfileDumper() {
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void run() {
                PrintWriter out = null;
                boolean fileOutput = false;
                try {
                    try {
                        out = new PrintWriter(new FileOutputStream(PROFILEFILE));
                        fileOutput = true;
                    }
                    catch (FileNotFoundException e) {
                        out = Context.getCurrentErr();
                    }
                    ProfileDumper.dump(out);
                }
                finally {
                    if (out != null && fileOutput) {
                        out.close();
                    }
                }
            }

            private static void dump(PrintWriter out) {
                int index = 0;
                for (ProfilingLinkerCallSite callSite : profileCallSites) {
                    out.println("" + index++ + '\t' + callSite.getDescriptor().getName() + '\t' + callSite.totalTime + '\t' + callSite.hitCount);
                }
            }
        }
    }
}

