/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.TypeDescriptor;
import java.util.Collection;
import java.util.LinkedList;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.CompiledFunction;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.DebuggerSupport;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.FinalScriptFunctionData;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;

public abstract class ScriptFunctionData
implements Serializable {
    static final int MAX_ARITY = 250;
    protected final String name;
    protected transient LinkedList<CompiledFunction> code = new LinkedList();
    protected int flags;
    private int arity;
    private volatile transient GenericInvokers genericInvokers;
    private static final MethodHandle BIND_VAR_ARGS = ScriptFunctionData.findOwnMH("bindVarArgs", Object[].class, Object[].class, Object[].class);
    public static final int IS_STRICT = 1;
    public static final int IS_BUILTIN = 2;
    public static final int IS_CONSTRUCTOR = 4;
    public static final int NEEDS_CALLEE = 8;
    public static final int USES_THIS = 16;
    public static final int IS_VARIABLE_ARITY = 32;
    public static final int IS_PROPERTY_ACCESSOR = 64;
    public static final int IS_STRICT_OR_BUILTIN = 3;
    public static final int IS_BUILTIN_CONSTRUCTOR = 6;
    private static final long serialVersionUID = 4252901245508769114L;

    ScriptFunctionData(String name, int arity, int flags) {
        this.name = name;
        this.flags = flags;
        this.setArity(arity);
    }

    final int getArity() {
        return this.arity;
    }

    final boolean isVariableArity() {
        return (this.flags & 0x20) != 0;
    }

    final boolean isPropertyAccessor() {
        return (this.flags & 0x40) != 0;
    }

    void setArity(int arity) {
        if (arity < 0 || arity > 250) {
            throw new IllegalArgumentException(String.valueOf(arity));
        }
        this.arity = arity;
    }

    CompiledFunction bind(CompiledFunction originalInv, ScriptFunction fn, Object self, Object[] args2) {
        MethodHandle boundInvoker = this.bindInvokeHandle(originalInv.createComposableInvoker(), fn, self, args2);
        if (this.isConstructor()) {
            return new CompiledFunction(boundInvoker, ScriptFunctionData.bindConstructHandle(originalInv.createComposableConstructor(), fn, args2), null);
        }
        return new CompiledFunction(boundInvoker);
    }

    public final boolean isStrict() {
        return (this.flags & 1) != 0;
    }

    protected String getFunctionName() {
        return this.getName();
    }

    final boolean isBuiltin() {
        return (this.flags & 2) != 0;
    }

    final boolean isConstructor() {
        return (this.flags & 4) != 0;
    }

    abstract boolean needsCallee();

    final boolean needsWrappedThis() {
        return (this.flags & 0x10) != 0 && (this.flags & 3) == 0;
    }

    String toSource() {
        return "function " + (this.name == null ? "" : this.name) + "() { [native code] }";
    }

    String getName() {
        return this.name;
    }

    public String toString() {
        return this.name.isEmpty() ? "<anonymous>" : this.name;
    }

    public String toStringVerbose() {
        StringBuilder sb = new StringBuilder();
        sb.append("name='").append(this.name.isEmpty() ? "<anonymous>" : this.name).append("' ").append(this.code.size()).append(" invokers=").append(this.code);
        return sb.toString();
    }

    final CompiledFunction getBestInvoker(MethodType callSiteType, ScriptObject runtimeScope) {
        return this.getBestInvoker(callSiteType, runtimeScope, CompiledFunction.NO_FUNCTIONS);
    }

    final CompiledFunction getBestInvoker(MethodType callSiteType, ScriptObject runtimeScope, Collection<CompiledFunction> forbidden) {
        CompiledFunction cf = this.getBest(callSiteType, runtimeScope, forbidden);
        assert (cf != null);
        return cf;
    }

    final CompiledFunction getBestConstructor(MethodType callSiteType, ScriptObject runtimeScope, Collection<CompiledFunction> forbidden) {
        if (!this.isConstructor()) {
            throw ECMAErrors.typeError("not.a.constructor", this.toSource());
        }
        CompiledFunction cf = this.getBest(callSiteType.insertParameterTypes(1, Object.class), runtimeScope, forbidden);
        return cf;
    }

    protected void ensureCompiled() {
    }

    final MethodHandle getGenericInvoker(ScriptObject runtimeScope) {
        GenericInvokers lgenericInvokers = this.ensureGenericInvokers();
        MethodHandle invoker = lgenericInvokers.invoker;
        if (invoker == null) {
            lgenericInvokers.invoker = invoker = this.createGenericInvoker(runtimeScope);
        }
        return invoker;
    }

    private MethodHandle createGenericInvoker(ScriptObject runtimeScope) {
        return ScriptFunctionData.makeGenericMethod(this.getGeneric(runtimeScope).createComposableInvoker());
    }

    final MethodHandle getGenericConstructor(ScriptObject runtimeScope) {
        GenericInvokers lgenericInvokers = this.ensureGenericInvokers();
        MethodHandle constructor = lgenericInvokers.constructor;
        if (constructor == null) {
            lgenericInvokers.constructor = constructor = this.createGenericConstructor(runtimeScope);
        }
        return constructor;
    }

    private MethodHandle createGenericConstructor(ScriptObject runtimeScope) {
        return ScriptFunctionData.makeGenericMethod(this.getGeneric(runtimeScope).createComposableConstructor());
    }

    private GenericInvokers ensureGenericInvokers() {
        GenericInvokers lgenericInvokers = this.genericInvokers;
        if (lgenericInvokers == null) {
            this.genericInvokers = lgenericInvokers = new GenericInvokers();
        }
        return lgenericInvokers;
    }

    private static MethodType widen(MethodType cftype) {
        Class[] paramTypes = new Class[cftype.parameterCount()];
        for (int i = 0; i < cftype.parameterCount(); ++i) {
            paramTypes[i] = ((Class)cftype.parameterType(i)).isPrimitive() ? cftype.parameterType(i) : Object.class;
        }
        return Lookup.MH.type((Class<?>)cftype.returnType(), paramTypes);
    }

    CompiledFunction lookupExactApplyToCall(MethodType type) {
        for (CompiledFunction cf : this.code) {
            MethodType cftype;
            if (!cf.isApplyToCall() || (cftype = cf.type()).parameterCount() != type.parameterCount() || !ScriptFunctionData.widen(cftype).equals((Object)ScriptFunctionData.widen(type))) continue;
            return cf;
        }
        return null;
    }

    CompiledFunction pickFunction(MethodType callSiteType, boolean canPickVarArg) {
        for (CompiledFunction candidate : this.code) {
            if (!candidate.matchesCallSite(callSiteType, canPickVarArg)) continue;
            return candidate;
        }
        return null;
    }

    abstract CompiledFunction getBest(MethodType var1, ScriptObject var2, Collection<CompiledFunction> var3, boolean var4);

    final CompiledFunction getBest(MethodType callSiteType, ScriptObject runtimeScope, Collection<CompiledFunction> forbidden) {
        return this.getBest(callSiteType, runtimeScope, forbidden, true);
    }

    boolean isValidCallSite(MethodType callSiteType) {
        return callSiteType.parameterCount() >= 2 && ((Class)callSiteType.parameterType(0)).isAssignableFrom(ScriptFunction.class);
    }

    CompiledFunction getGeneric(ScriptObject runtimeScope) {
        return this.getBest(this.getGenericType(), runtimeScope, CompiledFunction.NO_FUNCTIONS, false);
    }

    abstract MethodType getGenericType();

    ScriptObject allocate(PropertyMap map) {
        return null;
    }

    PropertyMap getAllocatorMap(ScriptObject prototype) {
        return null;
    }

    ScriptFunctionData makeBoundFunctionData(ScriptFunction fn, Object self, Object[] args2) {
        Object[] allArgs = args2 == null ? ScriptRuntime.EMPTY_ARRAY : args2;
        int length = args2 == null ? 0 : args2.length;
        int boundFlags = this.flags & 0xFFFFFFF7 & 0xFFFFFFEF;
        LinkedList<CompiledFunction> boundList = new LinkedList<CompiledFunction>();
        ScriptObject runtimeScope = fn.getScope();
        CompiledFunction bindTarget = new CompiledFunction(this.getGenericInvoker(runtimeScope), this.getGenericConstructor(runtimeScope), null);
        boundList.add(this.bind(bindTarget, fn, self, allArgs));
        return new FinalScriptFunctionData(this.name, Math.max(0, this.getArity() - length), boundList, boundFlags);
    }

    private Object convertThisObject(Object thiz) {
        return this.needsWrappedThis() ? ScriptFunctionData.wrapThis(thiz) : thiz;
    }

    static Object wrapThis(Object thiz) {
        if (!(thiz instanceof ScriptObject)) {
            if (JSType.nullOrUndefined(thiz)) {
                return Context.getGlobal();
            }
            if (ScriptFunctionData.isPrimitiveThis(thiz)) {
                return Context.getGlobal().wrapAsObject(thiz);
            }
        }
        return thiz;
    }

    static boolean isPrimitiveThis(Object obj) {
        return JSType.isString(obj) || obj instanceof Number || obj instanceof Boolean;
    }

    private MethodHandle bindInvokeHandle(MethodHandle originalInvoker, ScriptFunction targetFn, Object self, Object[] args2) {
        MethodHandle boundInvoker;
        Object boundSelf;
        boolean isTargetBound = targetFn.isBoundFunction();
        boolean needsCallee = ScriptFunctionData.needsCallee(originalInvoker);
        assert (needsCallee == this.needsCallee()) : "callee contract violation 2";
        assert (!isTargetBound || !needsCallee);
        Object object = boundSelf = isTargetBound ? null : this.convertThisObject(self);
        if (ScriptFunctionData.isVarArg(originalInvoker)) {
            MethodHandle noArgBoundInvoker = isTargetBound ? originalInvoker : (needsCallee ? Lookup.MH.insertArguments(originalInvoker, 0, targetFn, boundSelf) : Lookup.MH.bindTo(originalInvoker, boundSelf));
            boundInvoker = args2.length > 0 ? ScriptFunctionData.varArgBinder(noArgBoundInvoker, args2) : noArgBoundInvoker;
        } else {
            int argInsertPos = isTargetBound ? 1 : 0;
            Object[] boundArgs = new Object[Math.min(originalInvoker.type().parameterCount() - argInsertPos, args2.length + (isTargetBound ? 0 : (needsCallee ? 2 : 1)))];
            int next = 0;
            if (!isTargetBound) {
                if (needsCallee) {
                    boundArgs[next++] = targetFn;
                }
                boundArgs[next++] = boundSelf;
            }
            System.arraycopy(args2, 0, boundArgs, next, boundArgs.length - next);
            boundInvoker = Lookup.MH.insertArguments(originalInvoker, argInsertPos, boundArgs);
        }
        if (isTargetBound) {
            return boundInvoker;
        }
        return Lookup.MH.dropArguments(boundInvoker, 0, Object.class);
    }

    private static MethodHandle bindConstructHandle(MethodHandle originalConstructor, ScriptFunction fn, Object[] args2) {
        Object[] boundArgs;
        MethodHandle calleeBoundConstructor;
        assert (originalConstructor != null);
        MethodHandle methodHandle = calleeBoundConstructor = fn.isBoundFunction() ? originalConstructor : Lookup.MH.dropArguments(Lookup.MH.bindTo(originalConstructor, fn), 0, ScriptFunction.class);
        if (args2.length == 0) {
            return calleeBoundConstructor;
        }
        if (ScriptFunctionData.isVarArg(calleeBoundConstructor)) {
            return ScriptFunctionData.varArgBinder(calleeBoundConstructor, args2);
        }
        int maxArgCount = calleeBoundConstructor.type().parameterCount() - 1;
        if (args2.length <= maxArgCount) {
            boundArgs = args2;
        } else {
            boundArgs = new Object[maxArgCount];
            System.arraycopy(args2, 0, boundArgs, 0, maxArgCount);
        }
        return Lookup.MH.insertArguments(calleeBoundConstructor, 1, boundArgs);
    }

    private static MethodHandle makeGenericMethod(MethodHandle mh) {
        MethodType newType;
        MethodType type = mh.type();
        return type.equals((Object)(newType = ScriptFunctionData.makeGenericType(type))) ? mh : mh.asType(newType);
    }

    private static MethodType makeGenericType(MethodType type) {
        MethodType newType = type.generic();
        if (ScriptFunctionData.isVarArg(type)) {
            newType = newType.changeParameterType(type.parameterCount() - 1, Object[].class);
        }
        if (ScriptFunctionData.needsCallee(type)) {
            newType = newType.changeParameterType(0, ScriptFunction.class);
        }
        return newType;
    }

    Object invoke(ScriptFunction fn, Object self, Object ... arguments) throws Throwable {
        MethodHandle mh = this.getGenericInvoker(fn.getScope());
        Object selfObj = this.convertThisObject(self);
        Object[] args2 = arguments == null ? ScriptRuntime.EMPTY_ARRAY : arguments;
        DebuggerSupport.notifyInvoke(mh);
        if (ScriptFunctionData.isVarArg(mh)) {
            if (ScriptFunctionData.needsCallee(mh)) {
                return mh.invokeExact(fn, selfObj, args2);
            }
            return mh.invokeExact(selfObj, args2);
        }
        int paramCount = mh.type().parameterCount();
        if (ScriptFunctionData.needsCallee(mh)) {
            switch (paramCount) {
                case 2: {
                    return mh.invokeExact(fn, selfObj);
                }
                case 3: {
                    return mh.invokeExact(fn, selfObj, ScriptFunctionData.getArg(args2, 0));
                }
                case 4: {
                    return mh.invokeExact(fn, selfObj, ScriptFunctionData.getArg(args2, 0), ScriptFunctionData.getArg(args2, 1));
                }
                case 5: {
                    return mh.invokeExact(fn, selfObj, ScriptFunctionData.getArg(args2, 0), ScriptFunctionData.getArg(args2, 1), ScriptFunctionData.getArg(args2, 2));
                }
                case 6: {
                    return mh.invokeExact(fn, selfObj, ScriptFunctionData.getArg(args2, 0), ScriptFunctionData.getArg(args2, 1), ScriptFunctionData.getArg(args2, 2), ScriptFunctionData.getArg(args2, 3));
                }
                case 7: {
                    return mh.invokeExact(fn, selfObj, ScriptFunctionData.getArg(args2, 0), ScriptFunctionData.getArg(args2, 1), ScriptFunctionData.getArg(args2, 2), ScriptFunctionData.getArg(args2, 3), ScriptFunctionData.getArg(args2, 4));
                }
                case 8: {
                    return mh.invokeExact(fn, selfObj, ScriptFunctionData.getArg(args2, 0), ScriptFunctionData.getArg(args2, 1), ScriptFunctionData.getArg(args2, 2), ScriptFunctionData.getArg(args2, 3), ScriptFunctionData.getArg(args2, 4), ScriptFunctionData.getArg(args2, 5));
                }
            }
            return mh.invokeWithArguments(ScriptFunctionData.withArguments(fn, selfObj, paramCount, args2));
        }
        switch (paramCount) {
            case 1: {
                return mh.invokeExact(selfObj);
            }
            case 2: {
                return mh.invokeExact(selfObj, ScriptFunctionData.getArg(args2, 0));
            }
            case 3: {
                return mh.invokeExact(selfObj, ScriptFunctionData.getArg(args2, 0), ScriptFunctionData.getArg(args2, 1));
            }
            case 4: {
                return mh.invokeExact(selfObj, ScriptFunctionData.getArg(args2, 0), ScriptFunctionData.getArg(args2, 1), ScriptFunctionData.getArg(args2, 2));
            }
            case 5: {
                return mh.invokeExact(selfObj, ScriptFunctionData.getArg(args2, 0), ScriptFunctionData.getArg(args2, 1), ScriptFunctionData.getArg(args2, 2), ScriptFunctionData.getArg(args2, 3));
            }
            case 6: {
                return mh.invokeExact(selfObj, ScriptFunctionData.getArg(args2, 0), ScriptFunctionData.getArg(args2, 1), ScriptFunctionData.getArg(args2, 2), ScriptFunctionData.getArg(args2, 3), ScriptFunctionData.getArg(args2, 4));
            }
            case 7: {
                return mh.invokeExact(selfObj, ScriptFunctionData.getArg(args2, 0), ScriptFunctionData.getArg(args2, 1), ScriptFunctionData.getArg(args2, 2), ScriptFunctionData.getArg(args2, 3), ScriptFunctionData.getArg(args2, 4), ScriptFunctionData.getArg(args2, 5));
            }
        }
        return mh.invokeWithArguments(ScriptFunctionData.withArguments(null, selfObj, paramCount, args2));
    }

    Object construct(ScriptFunction fn, Object ... arguments) throws Throwable {
        MethodHandle mh = this.getGenericConstructor(fn.getScope());
        Object[] args2 = arguments == null ? ScriptRuntime.EMPTY_ARRAY : arguments;
        DebuggerSupport.notifyInvoke(mh);
        if (ScriptFunctionData.isVarArg(mh)) {
            if (ScriptFunctionData.needsCallee(mh)) {
                return mh.invokeExact(fn, args2);
            }
            return mh.invokeExact(args2);
        }
        int paramCount = mh.type().parameterCount();
        if (ScriptFunctionData.needsCallee(mh)) {
            switch (paramCount) {
                case 1: {
                    return mh.invokeExact(fn);
                }
                case 2: {
                    return mh.invokeExact(fn, ScriptFunctionData.getArg(args2, 0));
                }
                case 3: {
                    return mh.invokeExact(fn, ScriptFunctionData.getArg(args2, 0), ScriptFunctionData.getArg(args2, 1));
                }
                case 4: {
                    return mh.invokeExact(fn, ScriptFunctionData.getArg(args2, 0), ScriptFunctionData.getArg(args2, 1), ScriptFunctionData.getArg(args2, 2));
                }
                case 5: {
                    return mh.invokeExact(fn, ScriptFunctionData.getArg(args2, 0), ScriptFunctionData.getArg(args2, 1), ScriptFunctionData.getArg(args2, 2), ScriptFunctionData.getArg(args2, 3));
                }
                case 6: {
                    return mh.invokeExact(fn, ScriptFunctionData.getArg(args2, 0), ScriptFunctionData.getArg(args2, 1), ScriptFunctionData.getArg(args2, 2), ScriptFunctionData.getArg(args2, 3), ScriptFunctionData.getArg(args2, 4));
                }
                case 7: {
                    return mh.invokeExact(fn, ScriptFunctionData.getArg(args2, 0), ScriptFunctionData.getArg(args2, 1), ScriptFunctionData.getArg(args2, 2), ScriptFunctionData.getArg(args2, 3), ScriptFunctionData.getArg(args2, 4), ScriptFunctionData.getArg(args2, 5));
                }
            }
            return mh.invokeWithArguments(ScriptFunctionData.withArguments(fn, paramCount, args2));
        }
        switch (paramCount) {
            case 0: {
                return mh.invokeExact();
            }
            case 1: {
                return mh.invokeExact(ScriptFunctionData.getArg(args2, 0));
            }
            case 2: {
                return mh.invokeExact(ScriptFunctionData.getArg(args2, 0), ScriptFunctionData.getArg(args2, 1));
            }
            case 3: {
                return mh.invokeExact(ScriptFunctionData.getArg(args2, 0), ScriptFunctionData.getArg(args2, 1), ScriptFunctionData.getArg(args2, 2));
            }
            case 4: {
                return mh.invokeExact(ScriptFunctionData.getArg(args2, 0), ScriptFunctionData.getArg(args2, 1), ScriptFunctionData.getArg(args2, 2), ScriptFunctionData.getArg(args2, 3));
            }
            case 5: {
                return mh.invokeExact(ScriptFunctionData.getArg(args2, 0), ScriptFunctionData.getArg(args2, 1), ScriptFunctionData.getArg(args2, 2), ScriptFunctionData.getArg(args2, 3), ScriptFunctionData.getArg(args2, 4));
            }
            case 6: {
                return mh.invokeExact(ScriptFunctionData.getArg(args2, 0), ScriptFunctionData.getArg(args2, 1), ScriptFunctionData.getArg(args2, 2), ScriptFunctionData.getArg(args2, 3), ScriptFunctionData.getArg(args2, 4), ScriptFunctionData.getArg(args2, 5));
            }
        }
        return mh.invokeWithArguments(ScriptFunctionData.withArguments(null, paramCount, args2));
    }

    private static Object getArg(Object[] args2, int i) {
        return i < args2.length ? args2[i] : ScriptRuntime.UNDEFINED;
    }

    private static Object[] withArguments(ScriptFunction fn, int argCount, Object[] args2) {
        Object[] finalArgs = new Object[argCount];
        int nextArg = 0;
        if (fn != null) {
            finalArgs[nextArg++] = fn;
        }
        int i = 0;
        while (i < args2.length && nextArg < argCount) {
            finalArgs[nextArg++] = args2[i++];
        }
        while (nextArg < argCount) {
            finalArgs[nextArg++] = ScriptRuntime.UNDEFINED;
        }
        return finalArgs;
    }

    private static Object[] withArguments(ScriptFunction fn, Object self, int argCount, Object[] args2) {
        Object[] finalArgs = new Object[argCount];
        int nextArg = 0;
        if (fn != null) {
            finalArgs[nextArg++] = fn;
        }
        finalArgs[nextArg++] = self;
        int i = 0;
        while (i < args2.length && nextArg < argCount) {
            finalArgs[nextArg++] = args2[i++];
        }
        while (nextArg < argCount) {
            finalArgs[nextArg++] = ScriptRuntime.UNDEFINED;
        }
        return finalArgs;
    }

    private static MethodHandle varArgBinder(MethodHandle mh, Object[] args2) {
        assert (args2 != null);
        assert (args2.length > 0);
        return Lookup.MH.filterArguments(mh, mh.type().parameterCount() - 1, Lookup.MH.bindTo(BIND_VAR_ARGS, args2));
    }

    protected static boolean needsCallee(MethodHandle mh) {
        return ScriptFunctionData.needsCallee(mh.type());
    }

    static boolean needsCallee(MethodType type) {
        int length = type.parameterCount();
        if (length == 0) {
            return false;
        }
        TypeDescriptor.OfField param0 = type.parameterType(0);
        return param0 == ScriptFunction.class || param0 == Boolean.TYPE && length > 1 && type.parameterType(1) == ScriptFunction.class;
    }

    protected static boolean isVarArg(MethodHandle mh) {
        return ScriptFunctionData.isVarArg(mh.type());
    }

    static boolean isVarArg(MethodType type) {
        return ((Class)type.parameterType(type.parameterCount() - 1)).isArray();
    }

    public boolean inDynamicContext() {
        return false;
    }

    private static Object[] bindVarArgs(Object[] array1, Object[] array2) {
        if (array2 == null) {
            return (Object[])array1.clone();
        }
        int l2 = array2.length;
        if (l2 == 0) {
            return (Object[])array1.clone();
        }
        int l1 = array1.length;
        Object[] concat = new Object[l1 + l2];
        System.arraycopy(array1, 0, concat, 0, l1);
        System.arraycopy(array2, 0, concat, l1, l2);
        return concat;
    }

    private static MethodHandle findOwnMH(String name, Class<?> rtype, Class<?> ... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), ScriptFunctionData.class, name, Lookup.MH.type(rtype, types));
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.code = new LinkedList();
    }

    private static final class GenericInvokers {
        volatile MethodHandle invoker;
        volatile MethodHandle constructor;

        private GenericInvokers() {
        }
    }
}

