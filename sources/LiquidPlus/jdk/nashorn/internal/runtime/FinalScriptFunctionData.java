/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.Collection;
import java.util.List;
import jdk.nashorn.internal.runtime.CompiledFunction;
import jdk.nashorn.internal.runtime.ScriptFunctionData;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.Specialization;

final class FinalScriptFunctionData
extends ScriptFunctionData {
    private static final long serialVersionUID = -930632846167768864L;

    FinalScriptFunctionData(String name, int arity, List<CompiledFunction> functions, int flags) {
        super(name, arity, flags);
        this.code.addAll(functions);
        assert (!this.needsCallee());
    }

    FinalScriptFunctionData(String name, MethodHandle mh, Specialization[] specs, int flags) {
        super(name, FinalScriptFunctionData.methodHandleArity(mh), flags);
        this.addInvoker(mh);
        if (specs != null) {
            for (Specialization spec : specs) {
                this.addInvoker(spec.getMethodHandle(), spec);
            }
        }
    }

    @Override
    protected boolean needsCallee() {
        boolean needsCallee = ((CompiledFunction)this.code.getFirst()).needsCallee();
        assert (this.allNeedCallee(needsCallee));
        return needsCallee;
    }

    private boolean allNeedCallee(boolean needCallee) {
        for (CompiledFunction inv : this.code) {
            if (inv.needsCallee() == needCallee) continue;
            return false;
        }
        return true;
    }

    @Override
    CompiledFunction getBest(MethodType callSiteType, ScriptObject runtimeScope, Collection<CompiledFunction> forbidden, boolean linkLogicOkay) {
        assert (this.isValidCallSite(callSiteType)) : callSiteType;
        CompiledFunction best = null;
        for (CompiledFunction candidate : this.code) {
            if (!linkLogicOkay && candidate.hasLinkLogic() || forbidden.contains(candidate) || !candidate.betterThanFinal(best, callSiteType)) continue;
            best = candidate;
        }
        return best;
    }

    @Override
    MethodType getGenericType() {
        int max = 0;
        for (CompiledFunction fn : this.code) {
            MethodType t = fn.type();
            if (ScriptFunctionData.isVarArg(t)) {
                return MethodType.genericMethodType(2, true);
            }
            int paramCount = t.parameterCount() - (ScriptFunctionData.needsCallee(t) ? 1 : 0);
            if (paramCount <= max) continue;
            max = paramCount;
        }
        return MethodType.genericMethodType(max + 1);
    }

    private CompiledFunction addInvoker(MethodHandle mh, Specialization specialization) {
        CompiledFunction invoker;
        assert (!FinalScriptFunctionData.needsCallee(mh));
        if (FinalScriptFunctionData.isConstructor(mh)) {
            assert (this.isConstructor());
            invoker = CompiledFunction.createBuiltInConstructor(mh);
        } else {
            invoker = new CompiledFunction(mh, null, specialization);
        }
        this.code.add(invoker);
        return invoker;
    }

    private CompiledFunction addInvoker(MethodHandle mh) {
        return this.addInvoker(mh, null);
    }

    private static int methodHandleArity(MethodHandle mh) {
        if (FinalScriptFunctionData.isVarArg(mh)) {
            return 250;
        }
        return mh.type().parameterCount() - 1 - (FinalScriptFunctionData.needsCallee(mh) ? 1 : 0) - (FinalScriptFunctionData.isConstructor(mh) ? 1 : 0);
    }

    private static boolean isConstructor(MethodHandle mh) {
        return mh.type().parameterCount() >= 1 && mh.type().parameterType(0) == Boolean.TYPE;
    }
}

