/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.lang.invoke.SwitchPoint;
import java.lang.invoke.TypeDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.logging.Level;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.codegen.TypeMap;
import jdk.nashorn.internal.codegen.types.ArrayType;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.objects.annotations.SpecializedFunction;
import jdk.nashorn.internal.runtime.CodeStore;
import jdk.nashorn.internal.runtime.Debug;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.OptimisticReturnFilters;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import jdk.nashorn.internal.runtime.RewriteException;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptFunctionData;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.Specialization;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import jdk.nashorn.internal.runtime.events.RecompilationEvent;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.logging.DebugLogger;

final class CompiledFunction {
    private static final MethodHandle NEWFILTER = CompiledFunction.findOwnMH("newFilter", Object.class, Object.class, Object.class);
    private static final MethodHandle RELINK_COMPOSABLE_INVOKER = CompiledFunction.findOwnMH("relinkComposableInvoker", Void.TYPE, CallSite.class, CompiledFunction.class, Boolean.TYPE);
    private static final MethodHandle HANDLE_REWRITE_EXCEPTION = CompiledFunction.findOwnMH("handleRewriteException", MethodHandle.class, CompiledFunction.class, OptimismInfo.class, RewriteException.class);
    private static final MethodHandle RESTOF_INVOKER = MethodHandles.exactInvoker(MethodType.methodType(Object.class, RewriteException.class));
    private final DebugLogger log;
    static final Collection<CompiledFunction> NO_FUNCTIONS = Collections.emptySet();
    private MethodHandle invoker;
    private MethodHandle constructor;
    private OptimismInfo optimismInfo;
    private final int flags;
    private final MethodType callSiteType;
    private final Specialization specialization;

    CompiledFunction(MethodHandle invoker) {
        this(invoker, null, null);
    }

    static CompiledFunction createBuiltInConstructor(MethodHandle invoker, Specialization specialization) {
        return new CompiledFunction(Lookup.MH.insertArguments(invoker, 0, false), CompiledFunction.createConstructorFromInvoker(Lookup.MH.insertArguments(invoker, 0, true)), specialization);
    }

    CompiledFunction(MethodHandle invoker, MethodHandle constructor, Specialization specialization) {
        this(invoker, constructor, 0, null, specialization, DebugLogger.DISABLED_LOGGER);
    }

    CompiledFunction(MethodHandle invoker, MethodHandle constructor, int flags, MethodType callSiteType, Specialization specialization, DebugLogger log) {
        this.specialization = specialization;
        if (specialization != null && specialization.isOptimistic()) {
            this.invoker = Lookup.MH.insertArguments(invoker, invoker.type().parameterCount() - 1, 1);
            throw new AssertionError((Object)"Optimistic (UnwarrantedOptimismException throwing) builtin functions are currently not in use");
        }
        this.invoker = invoker;
        this.constructor = constructor;
        this.flags = flags;
        this.callSiteType = callSiteType;
        this.log = log;
    }

    CompiledFunction(MethodHandle invoker, RecompilableScriptFunctionData functionData, Map<Integer, Type> invalidatedProgramPoints, MethodType callSiteType, int flags) {
        this(invoker, null, flags, callSiteType, null, functionData.getLogger());
        this.optimismInfo = (flags & 0x800) != 0 ? new OptimismInfo(functionData, invalidatedProgramPoints) : null;
    }

    static CompiledFunction createBuiltInConstructor(MethodHandle invoker) {
        return new CompiledFunction(Lookup.MH.insertArguments(invoker, 0, false), CompiledFunction.createConstructorFromInvoker(Lookup.MH.insertArguments(invoker, 0, true)), null);
    }

    boolean isSpecialization() {
        return this.specialization != null;
    }

    boolean hasLinkLogic() {
        return this.getLinkLogicClass() != null;
    }

    Class<? extends SpecializedFunction.LinkLogic> getLinkLogicClass() {
        if (this.isSpecialization()) {
            Class<? extends SpecializedFunction.LinkLogic> linkLogicClass = this.specialization.getLinkLogicClass();
            assert (!SpecializedFunction.LinkLogic.isEmpty(linkLogicClass)) : "empty link logic classes should have been removed by nasgen";
            return linkLogicClass;
        }
        return null;
    }

    int getFlags() {
        return this.flags;
    }

    boolean isOptimistic() {
        return this.isSpecialization() ? this.specialization.isOptimistic() : false;
    }

    boolean isApplyToCall() {
        return (this.flags & 0x1000) != 0;
    }

    boolean isVarArg() {
        return CompiledFunction.isVarArgsType(this.invoker.type());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Class<? extends SpecializedFunction.LinkLogic> linkLogicClass = this.getLinkLogicClass();
        sb.append("[invokerType=").append(this.invoker.type()).append(" ctor=").append(this.constructor).append(" weight=").append(this.weight()).append(" linkLogic=").append(linkLogicClass != null ? linkLogicClass.getSimpleName() : "none");
        return sb.toString();
    }

    boolean needsCallee() {
        return ScriptFunctionData.needsCallee(this.invoker);
    }

    MethodHandle createComposableInvoker() {
        return this.createComposableInvoker(false);
    }

    private MethodHandle getConstructor() {
        if (this.constructor == null) {
            this.constructor = CompiledFunction.createConstructorFromInvoker(this.createInvokerForPessimisticCaller());
        }
        return this.constructor;
    }

    private MethodHandle createInvokerForPessimisticCaller() {
        return this.createInvoker(Object.class, -1);
    }

    private static MethodHandle createConstructorFromInvoker(MethodHandle invoker) {
        boolean needsCallee = ScriptFunctionData.needsCallee(invoker);
        MethodHandle swapped = needsCallee ? CompiledFunction.swapCalleeAndThis(invoker) : invoker;
        MethodHandle returnsObject = Lookup.MH.asType(swapped, swapped.type().changeReturnType(Object.class));
        MethodType ctorType = returnsObject.type();
        Class<?>[] ctorArgs = ctorType.dropParameterTypes(0, 1).parameterArray();
        MethodHandle filtered = Lookup.MH.foldArguments(Lookup.MH.dropArguments(NEWFILTER, 2, ctorArgs), returnsObject);
        if (needsCallee) {
            return Lookup.MH.foldArguments(filtered, ScriptFunction.ALLOCATE);
        }
        return Lookup.MH.filterArguments(filtered, 0, ScriptFunction.ALLOCATE);
    }

    private static MethodHandle swapCalleeAndThis(MethodHandle mh) {
        MethodType type = mh.type();
        assert (type.parameterType(0) == ScriptFunction.class) : type;
        assert (type.parameterType(1) == Object.class) : type;
        MethodType newType = type.changeParameterType(0, Object.class).changeParameterType(1, ScriptFunction.class);
        int[] reorder = new int[type.parameterCount()];
        reorder[0] = 1;
        assert (reorder[1] == 0);
        for (int i = 2; i < reorder.length; ++i) {
            reorder[i] = i;
        }
        return MethodHandles.permuteArguments(mh, newType, reorder);
    }

    MethodHandle createComposableConstructor() {
        return this.createComposableInvoker(true);
    }

    boolean hasConstructor() {
        return this.constructor != null;
    }

    MethodType type() {
        return this.invoker.type();
    }

    int weight() {
        return CompiledFunction.weight(this.type());
    }

    private static int weight(MethodType type) {
        if (CompiledFunction.isVarArgsType(type)) {
            return Integer.MAX_VALUE;
        }
        int weight = Type.typeFor(type.returnType()).getWeight();
        for (int i = 0; i < type.parameterCount(); ++i) {
            TypeDescriptor.OfField paramType = type.parameterType(i);
            int pweight = Type.typeFor(paramType).getWeight() * 2;
            weight += pweight;
        }
        return weight += type.parameterCount();
    }

    static boolean isVarArgsType(MethodType type) {
        assert (type.parameterCount() >= 1) : type;
        return type.parameterType(type.parameterCount() - 1) == Object[].class;
    }

    static boolean moreGenericThan(MethodType mt0, MethodType mt1) {
        return CompiledFunction.weight(mt0) > CompiledFunction.weight(mt1);
    }

    boolean betterThanFinal(CompiledFunction other, MethodType callSiteMethodType) {
        if (other == null) {
            return true;
        }
        return CompiledFunction.betterThanFinal(this, other, callSiteMethodType);
    }

    private static boolean betterThanFinal(CompiledFunction cf, CompiledFunction other, MethodType callSiteMethodType) {
        int fnParamDelta;
        boolean otherVarArg;
        int otherDiscardsParams;
        MethodType thisMethodType = cf.type();
        MethodType otherMethodType = other.type();
        int thisParamCount = CompiledFunction.getParamCount(thisMethodType);
        int otherParamCount = CompiledFunction.getParamCount(otherMethodType);
        int callSiteRawParamCount = CompiledFunction.getParamCount(callSiteMethodType);
        boolean csVarArg = callSiteRawParamCount == Integer.MAX_VALUE;
        int callSiteParamCount = csVarArg ? callSiteRawParamCount : callSiteRawParamCount - 1;
        int thisDiscardsParams = Math.max(callSiteParamCount - thisParamCount, 0);
        if (thisDiscardsParams < (otherDiscardsParams = Math.max(callSiteParamCount - otherParamCount, 0))) {
            return true;
        }
        if (thisDiscardsParams > otherDiscardsParams) {
            return false;
        }
        boolean thisVarArg = thisParamCount == Integer.MAX_VALUE;
        boolean bl = otherVarArg = otherParamCount == Integer.MAX_VALUE;
        if (!(thisVarArg && otherVarArg && csVarArg)) {
            int i;
            Type[] thisType = CompiledFunction.toTypeWithoutCallee(thisMethodType, 0);
            Type[] otherType = CompiledFunction.toTypeWithoutCallee(otherMethodType, 0);
            Type[] callSiteType = CompiledFunction.toTypeWithoutCallee(callSiteMethodType, 1);
            int narrowWeightDelta = 0;
            int widenWeightDelta = 0;
            int minParamsCount = Math.min(Math.min(thisParamCount, otherParamCount), callSiteParamCount);
            for (i = 0; i < minParamsCount; ++i) {
                int callSiteParamWeight = CompiledFunction.getParamType(i, callSiteType, csVarArg).getWeight();
                int thisParamWeightDelta = CompiledFunction.getParamType(i, thisType, thisVarArg).getWeight() - callSiteParamWeight;
                int otherParamWeightDelta = CompiledFunction.getParamType(i, otherType, otherVarArg).getWeight() - callSiteParamWeight;
                narrowWeightDelta += Math.max(-thisParamWeightDelta, 0) - Math.max(-otherParamWeightDelta, 0);
                widenWeightDelta += Math.max(thisParamWeightDelta, 0) - Math.max(otherParamWeightDelta, 0);
            }
            if (!thisVarArg) {
                for (i = callSiteParamCount; i < thisParamCount; ++i) {
                    narrowWeightDelta += Math.max(Type.OBJECT.getWeight() - thisType[i].getWeight(), 0);
                }
            }
            if (!otherVarArg) {
                for (i = callSiteParamCount; i < otherParamCount; ++i) {
                    narrowWeightDelta -= Math.max(Type.OBJECT.getWeight() - otherType[i].getWeight(), 0);
                }
            }
            if (narrowWeightDelta < 0) {
                return true;
            }
            if (narrowWeightDelta > 0) {
                return false;
            }
            if (widenWeightDelta < 0) {
                return true;
            }
            if (widenWeightDelta > 0) {
                return false;
            }
        }
        if (thisParamCount == callSiteParamCount && otherParamCount != callSiteParamCount) {
            return true;
        }
        if (thisParamCount != callSiteParamCount && otherParamCount == callSiteParamCount) {
            return false;
        }
        if (thisVarArg) {
            if (!otherVarArg) {
                return true;
            }
        } else if (otherVarArg) {
            return false;
        }
        if ((fnParamDelta = thisParamCount - otherParamCount) < 0) {
            return true;
        }
        if (fnParamDelta > 0) {
            return false;
        }
        int callSiteRetWeight = Type.typeFor(callSiteMethodType.returnType()).getWeight();
        int thisRetWeightDelta = Type.typeFor(thisMethodType.returnType()).getWeight() - callSiteRetWeight;
        int otherRetWeightDelta = Type.typeFor(otherMethodType.returnType()).getWeight() - callSiteRetWeight;
        int widenRetDelta = Math.max(thisRetWeightDelta, 0) - Math.max(otherRetWeightDelta, 0);
        if (widenRetDelta < 0) {
            return true;
        }
        if (widenRetDelta > 0) {
            return false;
        }
        int narrowRetDelta = Math.max(-thisRetWeightDelta, 0) - Math.max(-otherRetWeightDelta, 0);
        if (narrowRetDelta < 0) {
            return true;
        }
        if (narrowRetDelta > 0) {
            return false;
        }
        if (cf.isSpecialization() != other.isSpecialization()) {
            return cf.isSpecialization();
        }
        if (cf.isSpecialization() && other.isSpecialization()) {
            return cf.getLinkLogicClass() != null;
        }
        throw new AssertionError((Object)(thisMethodType + " identically applicable to " + otherMethodType + " for " + callSiteMethodType));
    }

    private static Type[] toTypeWithoutCallee(MethodType type, int thisIndex) {
        int paramCount = type.parameterCount();
        Type[] t = new Type[paramCount - thisIndex];
        for (int i = thisIndex; i < paramCount; ++i) {
            t[i - thisIndex] = Type.typeFor(type.parameterType(i));
        }
        return t;
    }

    private static Type getParamType(int i, Type[] paramTypes, boolean isVarArg) {
        int fixParamCount = paramTypes.length - (isVarArg ? 1 : 0);
        if (i < fixParamCount) {
            return paramTypes[i];
        }
        assert (isVarArg);
        return ((ArrayType)paramTypes[paramTypes.length - 1]).getElementType();
    }

    boolean matchesCallSite(MethodType other, boolean pickVarArg) {
        int i;
        boolean csIsVarArg;
        boolean isVarArg;
        if (other.equals((Object)this.callSiteType)) {
            return true;
        }
        MethodType type = this.type();
        int fnParamCount = CompiledFunction.getParamCount(type);
        boolean bl = isVarArg = fnParamCount == Integer.MAX_VALUE;
        if (isVarArg) {
            return pickVarArg;
        }
        int csParamCount = CompiledFunction.getParamCount(other);
        boolean bl2 = csIsVarArg = csParamCount == Integer.MAX_VALUE;
        if (csIsVarArg && this.isApplyToCall()) {
            return false;
        }
        int thisThisIndex = this.needsCallee() ? 1 : 0;
        int fnParamCountNoCallee = fnParamCount - thisThisIndex;
        int minParams = Math.min(csParamCount - 1, fnParamCountNoCallee);
        for (i = 0; i < minParams; ++i) {
            Type csType;
            Type fnType = Type.typeFor(type.parameterType(i + thisThisIndex));
            Type type2 = csType = csIsVarArg ? Type.OBJECT : Type.typeFor(other.parameterType(i + 1));
            if (fnType.isEquivalentTo(csType)) continue;
            return false;
        }
        for (i = minParams; i < fnParamCountNoCallee; ++i) {
            if (Type.typeFor(type.parameterType(i + thisThisIndex)).isEquivalentTo(Type.OBJECT)) continue;
            return false;
        }
        return true;
    }

    private static int getParamCount(MethodType type) {
        int paramCount = type.parameterCount();
        return ((Class)type.parameterType(paramCount - 1)).isArray() ? Integer.MAX_VALUE : paramCount;
    }

    private boolean canBeDeoptimized() {
        return this.optimismInfo != null;
    }

    private MethodHandle createComposableInvoker(boolean isConstructor) {
        MethodHandle handle = this.getInvokerOrConstructor(isConstructor);
        if (!this.canBeDeoptimized()) {
            return handle;
        }
        MutableCallSite cs = new MutableCallSite(handle.type());
        CompiledFunction.relinkComposableInvoker(cs, this, isConstructor);
        return ((CallSite)cs).dynamicInvoker();
    }

    private synchronized HandleAndAssumptions getValidOptimisticInvocation(Supplier<MethodHandle> invocationSupplier) {
        SwitchPoint assumptions;
        MethodHandle handle;
        while (true) {
            handle = invocationSupplier.get();
            SwitchPoint switchPoint = assumptions = this.canBeDeoptimized() ? this.optimismInfo.optimisticAssumptions : null;
            if (assumptions == null || !assumptions.hasBeenInvalidated()) break;
            try {
                this.wait();
            }
            catch (InterruptedException interruptedException) {}
        }
        return new HandleAndAssumptions(handle, assumptions);
    }

    private static void relinkComposableInvoker(CallSite cs, final CompiledFunction inv, final boolean constructor) {
        MethodHandle target;
        HandleAndAssumptions handleAndAssumptions = inv.getValidOptimisticInvocation(new Supplier<MethodHandle>(){

            @Override
            public MethodHandle get() {
                return inv.getInvokerOrConstructor(constructor);
            }
        });
        MethodHandle handle = handleAndAssumptions.handle;
        SwitchPoint assumptions = handleAndAssumptions.assumptions;
        if (assumptions == null) {
            target = handle;
        } else {
            MethodHandle relink = MethodHandles.insertArguments(RELINK_COMPOSABLE_INVOKER, 0, cs, inv, constructor);
            target = assumptions.guardWithTest(handle, MethodHandles.foldArguments(cs.dynamicInvoker(), relink));
        }
        cs.setTarget(target.asType(cs.type()));
    }

    private MethodHandle getInvokerOrConstructor(boolean selectCtor) {
        return selectCtor ? this.getConstructor() : this.createInvokerForPessimisticCaller();
    }

    GuardedInvocation createFunctionInvocation(final Class<?> callSiteReturnType, final int callerProgramPoint) {
        return this.getValidOptimisticInvocation(new Supplier<MethodHandle>(){

            @Override
            public MethodHandle get() {
                return CompiledFunction.this.createInvoker(callSiteReturnType, callerProgramPoint);
            }
        }).createInvocation();
    }

    GuardedInvocation createConstructorInvocation() {
        return this.getValidOptimisticInvocation(new Supplier<MethodHandle>(){

            @Override
            public MethodHandle get() {
                return CompiledFunction.this.getConstructor();
            }
        }).createInvocation();
    }

    private MethodHandle createInvoker(Class<?> callSiteReturnType, int callerProgramPoint) {
        boolean isOptimistic = this.canBeDeoptimized();
        MethodHandle handleRewriteException = isOptimistic ? this.createRewriteExceptionHandler() : null;
        MethodHandle inv = this.invoker;
        if (UnwarrantedOptimismException.isValid(callerProgramPoint)) {
            inv = OptimisticReturnFilters.filterOptimisticReturnValue(inv, callSiteReturnType, callerProgramPoint);
            inv = CompiledFunction.changeReturnType(inv, callSiteReturnType);
            if (callSiteReturnType.isPrimitive() && handleRewriteException != null) {
                handleRewriteException = OptimisticReturnFilters.filterOptimisticReturnValue(handleRewriteException, callSiteReturnType, callerProgramPoint);
            }
        } else if (isOptimistic) {
            inv = CompiledFunction.changeReturnType(inv, callSiteReturnType);
        }
        if (isOptimistic) {
            assert (handleRewriteException != null);
            MethodHandle typedHandleRewriteException = CompiledFunction.changeReturnType(handleRewriteException, inv.type().returnType());
            return Lookup.MH.catchException(inv, RewriteException.class, typedHandleRewriteException);
        }
        return inv;
    }

    private MethodHandle createRewriteExceptionHandler() {
        return Lookup.MH.foldArguments(RESTOF_INVOKER, Lookup.MH.insertArguments(HANDLE_REWRITE_EXCEPTION, 0, this, this.optimismInfo));
    }

    private static MethodHandle changeReturnType(MethodHandle mh, Class<?> newReturnType) {
        return Bootstrap.getLinkerServices().asType(mh, mh.type().changeReturnType(newReturnType));
    }

    private static MethodHandle handleRewriteException(CompiledFunction function, OptimismInfo oldOptimismInfo, RewriteException re) {
        return function.handleRewriteException(oldOptimismInfo, re);
    }

    private static List<String> toStringInvalidations(Map<Integer, Type> ipp) {
        if (ipp == null) {
            return Collections.emptyList();
        }
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<Integer, Type> entry : ipp.entrySet()) {
            String type;
            char bct = entry.getValue().getBytecodeStackType();
            switch (entry.getValue().getBytecodeStackType()) {
                case 'A': {
                    type = "object";
                    break;
                }
                case 'I': {
                    type = "int";
                    break;
                }
                case 'J': {
                    type = "long";
                    break;
                }
                case 'D': {
                    type = "double";
                    break;
                }
                default: {
                    type = String.valueOf(bct);
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append('[').append("program point: ").append(entry.getKey()).append(" -> ").append(type).append(']');
            list.add(sb.toString());
        }
        return list;
    }

    private void logRecompile(String reason, FunctionNode fn, MethodType type, Map<Integer, Type> ipp) {
        if (this.log.isEnabled()) {
            this.log.info(reason, DebugLogger.quote(fn.getName()), " signature: ", type);
            this.log.indent();
            for (String str : CompiledFunction.toStringInvalidations(ipp)) {
                this.log.fine(str);
            }
            this.log.unindent();
        }
    }

    private synchronized MethodHandle handleRewriteException(OptimismInfo oldOptInfo, RewriteException re) {
        MethodType type;
        if (this.log.isEnabled()) {
            this.log.info(new RecompilationEvent(Level.INFO, re, re.getReturnValueNonDestructive()), "caught RewriteException ", re.getMessageShort());
            this.log.indent();
        }
        MethodType ct = (type = this.type()).parameterType(0) == ScriptFunction.class ? type : type.insertParameterTypes(0, ScriptFunction.class);
        OptimismInfo currentOptInfo = this.optimismInfo;
        boolean shouldRecompile = currentOptInfo != null && currentOptInfo.requestRecompile(re);
        OptimismInfo effectiveOptInfo = currentOptInfo != null ? currentOptInfo : oldOptInfo;
        FunctionNode fn = effectiveOptInfo.reparse();
        boolean cached = fn.isCached();
        Compiler compiler = effectiveOptInfo.getCompiler(fn, ct, re);
        if (!shouldRecompile) {
            this.logRecompile("Rest-of compilation [STANDALONE] ", fn, ct, effectiveOptInfo.invalidatedProgramPoints);
            return this.restOfHandle(effectiveOptInfo, compiler.compile(fn, cached ? Compiler.CompilationPhases.COMPILE_CACHED_RESTOF : Compiler.CompilationPhases.COMPILE_ALL_RESTOF), currentOptInfo != null);
        }
        this.logRecompile("Deoptimizing recompilation (up to bytecode) ", fn, ct, effectiveOptInfo.invalidatedProgramPoints);
        fn = compiler.compile(fn, cached ? Compiler.CompilationPhases.RECOMPILE_CACHED_UPTO_BYTECODE : Compiler.CompilationPhases.COMPILE_UPTO_BYTECODE);
        this.log.fine("Reusable IR generated");
        this.log.info("Generating and installing bytecode from reusable IR...");
        this.logRecompile("Rest-of compilation [CODE PIPELINE REUSE] ", fn, ct, effectiveOptInfo.invalidatedProgramPoints);
        FunctionNode normalFn = compiler.compile(fn, Compiler.CompilationPhases.GENERATE_BYTECODE_AND_INSTALL);
        if (effectiveOptInfo.data.usePersistentCodeCache()) {
            RecompilableScriptFunctionData data = effectiveOptInfo.data;
            int functionNodeId = data.getFunctionNodeId();
            TypeMap typeMap = data.typeMap(ct);
            Type[] paramTypes = typeMap == null ? null : typeMap.getParameterTypes(functionNodeId);
            String cacheKey = CodeStore.getCacheKey(functionNodeId, paramTypes);
            compiler.persistClassInfo(cacheKey, normalFn);
        }
        boolean canBeDeoptimized = normalFn.canBeDeoptimized();
        if (this.log.isEnabled()) {
            this.log.unindent();
            this.log.info("Done.");
            this.log.info("Recompiled '", fn.getName(), "' (", Debug.id(this), ") ", canBeDeoptimized ? "can still be deoptimized." : " is completely deoptimized.");
            this.log.finest("Looking up invoker...");
        }
        MethodHandle newInvoker = effectiveOptInfo.data.lookup(fn);
        this.invoker = newInvoker.asType(type.changeReturnType((Class<?>)newInvoker.type().returnType()));
        this.constructor = null;
        this.log.info("Done: ", this.invoker);
        MethodHandle restOf = this.restOfHandle(effectiveOptInfo, compiler.compile(fn, Compiler.CompilationPhases.GENERATE_BYTECODE_AND_INSTALL_RESTOF), canBeDeoptimized);
        if (canBeDeoptimized) {
            effectiveOptInfo.newOptimisticAssumptions();
        } else {
            this.optimismInfo = null;
        }
        this.notifyAll();
        return restOf;
    }

    private MethodHandle restOfHandle(OptimismInfo info, FunctionNode restOfFunction, boolean canBeDeoptimized) {
        assert (info != null);
        assert (restOfFunction.getCompileUnit().getUnitClassName().contains("restOf"));
        MethodHandle restOf = CompiledFunction.changeReturnType(info.data.lookupCodeMethod(restOfFunction.getCompileUnit().getCode(), Lookup.MH.type(restOfFunction.getReturnType().getTypeClass(), RewriteException.class)), Object.class);
        if (!canBeDeoptimized) {
            return restOf;
        }
        return Lookup.MH.catchException(restOf, RewriteException.class, this.createRewriteExceptionHandler());
    }

    private static Object newFilter(Object result, Object allocation) {
        return result instanceof ScriptObject || !JSType.isPrimitive(result) ? result : allocation;
    }

    private static MethodHandle findOwnMH(String name, Class<?> rtype, Class<?> ... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), CompiledFunction.class, name, Lookup.MH.type(rtype, types));
    }

    private static class OptimismInfo {
        private final RecompilableScriptFunctionData data;
        private final Map<Integer, Type> invalidatedProgramPoints;
        private SwitchPoint optimisticAssumptions;
        private final DebugLogger log;

        OptimismInfo(RecompilableScriptFunctionData data, Map<Integer, Type> invalidatedProgramPoints) {
            this.data = data;
            this.log = data.getLogger();
            this.invalidatedProgramPoints = invalidatedProgramPoints == null ? new TreeMap() : invalidatedProgramPoints;
            this.newOptimisticAssumptions();
        }

        private void newOptimisticAssumptions() {
            this.optimisticAssumptions = new SwitchPoint();
        }

        boolean requestRecompile(RewriteException e) {
            Type retType = e.getReturnType();
            Type previousFailedType = this.invalidatedProgramPoints.put(e.getProgramPoint(), retType);
            if (previousFailedType != null && !previousFailedType.narrowerThan(retType)) {
                StackTraceElement[] stack = e.getStackTrace();
                String functionId = stack.length == 0 ? this.data.getName() : stack[0].getClassName() + "." + stack[0].getMethodName();
                this.log.info("RewriteException for an already invalidated program point ", e.getProgramPoint(), " in ", functionId, ". This is okay for a recursive function invocation, but a bug otherwise.");
                return false;
            }
            SwitchPoint.invalidateAll(new SwitchPoint[]{this.optimisticAssumptions});
            return true;
        }

        Compiler getCompiler(FunctionNode fn, MethodType actualCallSiteType, RewriteException e) {
            return this.data.getCompiler(fn, actualCallSiteType, e.getRuntimeScope(), this.invalidatedProgramPoints, OptimismInfo.getEntryPoints(e));
        }

        private static int[] getEntryPoints(RewriteException e) {
            int[] entryPoints;
            int[] prevEntryPoints = e.getPreviousContinuationEntryPoints();
            if (prevEntryPoints == null) {
                entryPoints = new int[1];
            } else {
                int l = prevEntryPoints.length;
                entryPoints = new int[l + 1];
                System.arraycopy(prevEntryPoints, 0, entryPoints, 1, l);
            }
            entryPoints[0] = e.getProgramPoint();
            return entryPoints;
        }

        FunctionNode reparse() {
            return this.data.reparse();
        }
    }

    private static class HandleAndAssumptions {
        final MethodHandle handle;
        final SwitchPoint assumptions;

        HandleAndAssumptions(MethodHandle handle, SwitchPoint assumptions) {
            this.handle = handle;
            this.assumptions = assumptions;
        }

        GuardedInvocation createInvocation() {
            return new GuardedInvocation(this.handle, this.assumptions);
        }
    }
}

