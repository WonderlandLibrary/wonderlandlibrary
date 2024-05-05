/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.SwitchPoint;
import java.lang.invoke.TypeDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.atomic.LongAdder;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.support.Guards;
import jdk.nashorn.internal.codegen.ApplySpecialization;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.objects.NativeFunction;
import jdk.nashorn.internal.objects.annotations.SpecializedFunction;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.CompiledFunction;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.FinalScriptFunctionData;
import jdk.nashorn.internal.runtime.GlobalFunctions;
import jdk.nashorn.internal.runtime.OptimisticBuiltins;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.PrototypeObject;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import jdk.nashorn.internal.runtime.ScriptFunctionData;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.Specialization;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;
import jdk.nashorn.internal.runtime.logging.DebugLogger;

public class ScriptFunction
extends ScriptObject {
    public static final MethodHandle G$PROTOTYPE = ScriptFunction.findOwnMH_S("G$prototype", Object.class, Object.class);
    public static final MethodHandle S$PROTOTYPE = ScriptFunction.findOwnMH_S("S$prototype", Void.TYPE, Object.class, Object.class);
    public static final MethodHandle G$LENGTH = ScriptFunction.findOwnMH_S("G$length", Integer.TYPE, Object.class);
    public static final MethodHandle G$NAME = ScriptFunction.findOwnMH_S("G$name", Object.class, Object.class);
    public static final MethodHandle INVOKE_SYNC = ScriptFunction.findOwnMH_S("invokeSync", Object.class, ScriptFunction.class, Object.class, Object.class, Object[].class);
    static final MethodHandle ALLOCATE = ScriptFunction.findOwnMH_V("allocate", Object.class, new Class[0]);
    private static final MethodHandle WRAPFILTER = ScriptFunction.findOwnMH_S("wrapFilter", Object.class, Object.class);
    private static final MethodHandle SCRIPTFUNCTION_GLOBALFILTER = ScriptFunction.findOwnMH_S("globalFilter", Object.class, Object.class);
    public static final CompilerConstants.Call GET_SCOPE = CompilerConstants.virtualCallNoLookup(ScriptFunction.class, "getScope", ScriptObject.class, new Class[0]);
    private static final MethodHandle IS_FUNCTION_MH = ScriptFunction.findOwnMH_S("isFunctionMH", Boolean.TYPE, Object.class, ScriptFunctionData.class);
    private static final MethodHandle IS_APPLY_FUNCTION = ScriptFunction.findOwnMH_S("isApplyFunction", Boolean.TYPE, Boolean.TYPE, Object.class, Object.class);
    private static final MethodHandle IS_NONSTRICT_FUNCTION = ScriptFunction.findOwnMH_S("isNonStrictFunction", Boolean.TYPE, Object.class, Object.class, ScriptFunctionData.class);
    private static final MethodHandle ADD_ZEROTH_ELEMENT = ScriptFunction.findOwnMH_S("addZerothElement", Object[].class, Object[].class, Object.class);
    private static final MethodHandle WRAP_THIS = Lookup.MH.findStatic(MethodHandles.lookup(), ScriptFunctionData.class, "wrapThis", Lookup.MH.type(Object.class, Object.class));
    private static final PropertyMap anonmap$;
    private static final PropertyMap strictmodemap$;
    private static final PropertyMap boundfunctionmap$;
    private static final PropertyMap map$;
    private static final Object LAZY_PROTOTYPE;
    private final ScriptObject scope;
    private final ScriptFunctionData data;
    protected PropertyMap allocatorMap;
    protected Object prototype;
    private static LongAdder constructorCount;
    private static LongAdder invokes;
    private static LongAdder allocations;

    private static PropertyMap createStrictModeMap(PropertyMap map) {
        int flags = 6;
        PropertyMap newMap = map;
        newMap = newMap.addPropertyNoHistory(map.newUserAccessors("arguments", 6));
        newMap = newMap.addPropertyNoHistory(map.newUserAccessors("caller", 6));
        return newMap;
    }

    private static PropertyMap createBoundFunctionMap(PropertyMap strictModeMap) {
        return strictModeMap.deleteProperty(strictModeMap.findProperty("prototype"));
    }

    private static boolean isStrict(int flags) {
        return (flags & 1) != 0;
    }

    private static PropertyMap getMap(boolean strict) {
        return strict ? strictmodemap$ : map$;
    }

    private ScriptFunction(ScriptFunctionData data, PropertyMap map, ScriptObject scope, Global global) {
        super(map);
        if (Context.DEBUG) {
            constructorCount.increment();
        }
        this.data = data;
        this.scope = scope;
        this.setInitialProto(global.getFunctionPrototype());
        this.prototype = LAZY_PROTOTYPE;
        assert (this.objectSpill == null);
        if (this.isStrict() || this.isBoundFunction()) {
            ScriptFunction typeErrorThrower = global.getTypeErrorThrower();
            this.initUserAccessors("arguments", 6, typeErrorThrower, typeErrorThrower);
            this.initUserAccessors("caller", 6, typeErrorThrower, typeErrorThrower);
        }
    }

    private ScriptFunction(String name, MethodHandle methodHandle, PropertyMap map, ScriptObject scope, Specialization[] specs, int flags, Global global) {
        this(new FinalScriptFunctionData(name, methodHandle, specs, flags), map, scope, global);
    }

    private ScriptFunction(String name, MethodHandle methodHandle, ScriptObject scope, Specialization[] specs, int flags) {
        this(name, methodHandle, ScriptFunction.getMap(ScriptFunction.isStrict(flags)), scope, specs, flags, Global.instance());
    }

    protected ScriptFunction(String name, MethodHandle invokeHandle, Specialization[] specs) {
        this(name, invokeHandle, map$, null, specs, 6, Global.instance());
    }

    protected ScriptFunction(String name, MethodHandle invokeHandle, PropertyMap map, Specialization[] specs) {
        this(name, invokeHandle, map.addAll(map$), null, specs, 6, Global.instance());
    }

    public static ScriptFunction create(Object[] constants, int index, ScriptObject scope) {
        RecompilableScriptFunctionData data = (RecompilableScriptFunctionData)constants[index];
        return new ScriptFunction(data, ScriptFunction.getMap(data.isStrict()), scope, Global.instance());
    }

    public static ScriptFunction create(Object[] constants, int index) {
        return ScriptFunction.create(constants, index, null);
    }

    public static ScriptFunction createAnonymous() {
        return new ScriptFunction("", GlobalFunctions.ANONYMOUS, anonmap$, null);
    }

    private static ScriptFunction createBuiltin(String name, MethodHandle methodHandle, Specialization[] specs, int flags) {
        ScriptFunction func = new ScriptFunction(name, methodHandle, null, specs, flags);
        func.setPrototype(ScriptRuntime.UNDEFINED);
        func.deleteOwnProperty(func.getMap().findProperty("prototype"));
        return func;
    }

    public static ScriptFunction createBuiltin(String name, MethodHandle methodHandle, Specialization[] specs) {
        return ScriptFunction.createBuiltin(name, methodHandle, specs, 2);
    }

    public static ScriptFunction createBuiltin(String name, MethodHandle methodHandle) {
        return ScriptFunction.createBuiltin(name, methodHandle, null);
    }

    public static ScriptFunction createStrictBuiltin(String name, MethodHandle methodHandle) {
        return ScriptFunction.createBuiltin(name, methodHandle, null, 3);
    }

    public final ScriptFunction createBound(Object self, Object[] args2) {
        return new Bound(this.data.makeBoundFunctionData(this, self, args2), this.getTargetFunction());
    }

    public final ScriptFunction createSynchronized(Object sync) {
        MethodHandle mh = Lookup.MH.insertArguments(INVOKE_SYNC, 0, this, sync);
        return ScriptFunction.createBuiltin(this.getName(), mh);
    }

    @Override
    public String getClassName() {
        return "Function";
    }

    @Override
    public boolean isInstance(ScriptObject instance) {
        Object basePrototype = this.getTargetFunction().getPrototype();
        if (!(basePrototype instanceof ScriptObject)) {
            throw ECMAErrors.typeError("prototype.not.an.object", ScriptRuntime.safeToString(this.getTargetFunction()), ScriptRuntime.safeToString(basePrototype));
        }
        for (ScriptObject proto = instance.getProto(); proto != null; proto = proto.getProto()) {
            if (proto != basePrototype) continue;
            return true;
        }
        return false;
    }

    protected ScriptFunction getTargetFunction() {
        return this;
    }

    final boolean isBoundFunction() {
        return this.getTargetFunction() != this;
    }

    public final void setArity(int arity) {
        this.data.setArity(arity);
    }

    public final boolean isStrict() {
        return this.data.isStrict();
    }

    public final boolean needsWrappedThis() {
        return this.data.needsWrappedThis();
    }

    private static boolean needsWrappedThis(Object fn) {
        return fn instanceof ScriptFunction ? ((ScriptFunction)fn).needsWrappedThis() : false;
    }

    final Object invoke(Object self, Object ... arguments) throws Throwable {
        if (Context.DEBUG) {
            invokes.increment();
        }
        return this.data.invoke(this, self, arguments);
    }

    final Object construct(Object ... arguments) throws Throwable {
        return this.data.construct(this, arguments);
    }

    private Object allocate() {
        if (Context.DEBUG) {
            allocations.increment();
        }
        assert (!this.isBoundFunction());
        ScriptObject prototype = this.getAllocatorPrototype();
        ScriptObject object = this.data.allocate(this.getAllocatorMap(prototype));
        if (object != null) {
            object.setInitialProto(prototype);
        }
        return object;
    }

    private synchronized PropertyMap getAllocatorMap(ScriptObject prototype) {
        if (this.allocatorMap == null || this.allocatorMap.isInvalidSharedMapFor(prototype)) {
            this.allocatorMap = this.data.getAllocatorMap(prototype);
        }
        return this.allocatorMap;
    }

    private ScriptObject getAllocatorPrototype() {
        Object prototype = this.getPrototype();
        if (prototype instanceof ScriptObject) {
            return (ScriptObject)prototype;
        }
        return Global.objectPrototype();
    }

    @Override
    public final String safeToString() {
        return this.toSource();
    }

    public final String toString() {
        return this.data.toString();
    }

    public final String toSource() {
        return this.data.toSource();
    }

    public final Object getPrototype() {
        if (this.prototype == LAZY_PROTOTYPE) {
            this.prototype = new PrototypeObject(this);
        }
        return this.prototype;
    }

    public final synchronized void setPrototype(Object newPrototype) {
        if (newPrototype instanceof ScriptObject && newPrototype != this.prototype && this.allocatorMap != null) {
            this.allocatorMap = null;
        }
        this.prototype = newPrototype;
    }

    public final MethodHandle getBoundInvokeHandle(Object self) {
        return Lookup.MH.bindTo(this.bindToCalleeIfNeeded(this.data.getGenericInvoker(this.scope)), self);
    }

    private MethodHandle bindToCalleeIfNeeded(MethodHandle methodHandle) {
        return ScriptFunctionData.needsCallee(methodHandle) ? Lookup.MH.bindTo(methodHandle, this) : methodHandle;
    }

    public final String getName() {
        return this.data.getName();
    }

    public final ScriptObject getScope() {
        return this.scope;
    }

    public static Object G$prototype(Object self) {
        return self instanceof ScriptFunction ? ((ScriptFunction)self).getPrototype() : ScriptRuntime.UNDEFINED;
    }

    public static void S$prototype(Object self, Object prototype) {
        if (self instanceof ScriptFunction) {
            ((ScriptFunction)self).setPrototype(prototype);
        }
    }

    public static int G$length(Object self) {
        if (self instanceof ScriptFunction) {
            return ((ScriptFunction)self).data.getArity();
        }
        return 0;
    }

    public static Object G$name(Object self) {
        if (self instanceof ScriptFunction) {
            return ((ScriptFunction)self).getName();
        }
        return ScriptRuntime.UNDEFINED;
    }

    public static ScriptObject getPrototype(ScriptFunction constructor) {
        Object proto;
        if (constructor != null && (proto = constructor.getPrototype()) instanceof ScriptObject) {
            return (ScriptObject)proto;
        }
        return null;
    }

    public static long getConstructorCount() {
        return constructorCount.longValue();
    }

    public static long getInvokes() {
        return invokes.longValue();
    }

    public static long getAllocations() {
        return allocations.longValue();
    }

    @Override
    protected GuardedInvocation findNewMethod(CallSiteDescriptor desc, LinkRequest request) {
        MethodType type = desc.getMethodType();
        assert (desc.getMethodType().returnType() == Object.class && !NashornCallSiteDescriptor.isOptimistic(desc));
        CompiledFunction cf = this.data.getBestConstructor(type, this.scope, CompiledFunction.NO_FUNCTIONS);
        GuardedInvocation bestCtorInv = cf.createConstructorInvocation();
        return new GuardedInvocation(ScriptFunction.pairArguments(bestCtorInv.getInvocation(), type), ScriptFunction.getFunctionGuard(this, cf.getFlags()), bestCtorInv.getSwitchPoints(), null);
    }

    private static Object wrapFilter(Object obj) {
        if (obj instanceof ScriptObject || !ScriptFunctionData.isPrimitiveThis(obj)) {
            return obj;
        }
        return Context.getGlobal().wrapAsObject(obj);
    }

    private static Object globalFilter(Object object) {
        return Context.getGlobal();
    }

    private static SpecializedFunction.LinkLogic getLinkLogic(Object self, Class<? extends SpecializedFunction.LinkLogic> linkLogicClass) {
        if (linkLogicClass == null) {
            return SpecializedFunction.LinkLogic.EMPTY_INSTANCE;
        }
        if (!Context.getContextTrusted().getEnv()._optimistic_types) {
            return null;
        }
        Object wrappedSelf = ScriptFunction.wrapFilter(self);
        if (wrappedSelf instanceof OptimisticBuiltins) {
            if (wrappedSelf != self && ((OptimisticBuiltins)wrappedSelf).hasPerInstanceAssumptions()) {
                return null;
            }
            return ((OptimisticBuiltins)wrappedSelf).getLinkLogic(linkLogicClass);
        }
        return null;
    }

    @Override
    protected GuardedInvocation findCallMethod(CallSiteDescriptor desc, LinkRequest request) {
        MethodHandle boundHandle;
        Object[] args2;
        MethodType type = desc.getMethodType();
        String name = this.getName();
        boolean isUnstable = request.isCallSiteUnstable();
        boolean scopeCall = NashornCallSiteDescriptor.isScope(desc);
        boolean isCall = !scopeCall && this.data.isBuiltin() && "call".equals(name);
        boolean isApply = !scopeCall && this.data.isBuiltin() && "apply".equals(name);
        boolean isApplyOrCall = isCall | isApply;
        if (isUnstable && !isApplyOrCall) {
            MethodHandle handle = type.parameterCount() == 3 && type.parameterType(2) == Object[].class ? ScriptRuntime.APPLY.methodHandle() : Lookup.MH.asCollector(ScriptRuntime.APPLY.methodHandle(), Object[].class, type.parameterCount() - 2);
            return new GuardedInvocation(handle, null, (SwitchPoint)null, ClassCastException.class);
        }
        MethodHandle guard = null;
        if (isApplyOrCall && !isUnstable && Bootstrap.isCallable((args2 = request.getArguments())[1])) {
            return this.createApplyOrCallCall(isApply, desc, request, args2);
        }
        int programPoint = -1;
        if (NashornCallSiteDescriptor.isOptimistic(desc)) {
            programPoint = NashornCallSiteDescriptor.getProgramPoint(desc);
        }
        CompiledFunction cf = this.data.getBestInvoker(type, this.scope, CompiledFunction.NO_FUNCTIONS);
        Object self = request.getArguments()[1];
        HashSet<CompiledFunction> forbidden = new HashSet<CompiledFunction>();
        ArrayList<SwitchPoint> sps = new ArrayList<SwitchPoint>();
        Class<? extends Throwable> exceptionGuard = null;
        while (cf.isSpecialization()) {
            Class<? extends SpecializedFunction.LinkLogic> linkLogicClass = cf.getLinkLogicClass();
            SpecializedFunction.LinkLogic linkLogic = ScriptFunction.getLinkLogic(self, linkLogicClass);
            if (linkLogic != null && linkLogic.checkLinkable(self, desc, request)) {
                DebugLogger log = Context.getContextTrusted().getLogger(Compiler.class);
                if (log.isEnabled()) {
                    log.info("Linking optimistic builtin function: '", name, "' args=", Arrays.toString(request.getArguments()), " desc=", desc);
                }
                exceptionGuard = linkLogic.getRelinkException();
                break;
            }
            forbidden.add(cf);
            CompiledFunction oldCf = cf;
            cf = this.data.getBestInvoker(type, this.scope, forbidden);
            assert (oldCf != cf);
        }
        GuardedInvocation bestInvoker = cf.createFunctionInvocation((Class<?>)type.returnType(), programPoint);
        MethodHandle callHandle = bestInvoker.getInvocation();
        if (this.data.needsCallee()) {
            boundHandle = scopeCall && this.needsWrappedThis() ? Lookup.MH.filterArguments(callHandle, 1, SCRIPTFUNCTION_GLOBALFILTER) : callHandle;
        } else if (this.data.isBuiltin() && "extend".equals(this.data.getName())) {
            boundHandle = Lookup.MH.dropArguments(Lookup.MH.bindTo(callHandle, desc.getLookup()), 0, new Class[]{type.parameterType(0), type.parameterType(1)});
        } else if (scopeCall && this.needsWrappedThis()) {
            boundHandle = Lookup.MH.filterArguments(callHandle, 0, SCRIPTFUNCTION_GLOBALFILTER);
            boundHandle = Lookup.MH.dropArguments(boundHandle, 0, new Class[]{type.parameterType(0)});
        } else {
            boundHandle = Lookup.MH.dropArguments(callHandle, 0, new Class[]{type.parameterType(0)});
        }
        if (!scopeCall && this.needsWrappedThis()) {
            if (ScriptFunctionData.isPrimitiveThis(request.getArguments()[1])) {
                boundHandle = Lookup.MH.filterArguments(boundHandle, 1, WRAPFILTER);
            } else {
                guard = ScriptFunction.getNonStrictFunctionGuard(this);
            }
        }
        if (isUnstable && NashornCallSiteDescriptor.isApplyToCall(desc)) {
            boundHandle = Lookup.MH.asCollector(boundHandle, Object[].class, type.parameterCount() - 2);
        }
        boundHandle = ScriptFunction.pairArguments(boundHandle, type);
        if (bestInvoker.getSwitchPoints() != null) {
            sps.addAll(Arrays.asList(bestInvoker.getSwitchPoints()));
        }
        SwitchPoint[] spsArray = sps.isEmpty() ? null : sps.toArray(new SwitchPoint[sps.size()]);
        return new GuardedInvocation(boundHandle, guard == null ? ScriptFunction.getFunctionGuard(this, cf.getFlags()) : guard, spsArray, exceptionGuard);
    }

    private GuardedInvocation createApplyOrCallCall(boolean isApply, CallSiteDescriptor desc, LinkRequest request, Object[] args2) {
        GuardedInvocation appliedInvocation;
        int paramCount;
        MethodType descType = desc.getMethodType();
        if (((Class)descType.parameterType((paramCount = descType.parameterCount()) - 1)).isArray()) {
            return this.createVarArgApplyOrCallCall(isApply, desc, request, args2);
        }
        boolean passesThis = paramCount > 2;
        boolean passesArgs = paramCount > 3;
        int realArgCount = passesArgs ? paramCount - 3 : 0;
        Object appliedFn = args2[1];
        boolean appliedFnNeedsWrappedThis = ScriptFunction.needsWrappedThis(appliedFn);
        CallSiteDescriptor appliedDesc = desc;
        SwitchPoint applyToCallSwitchPoint = Global.getBuiltinFunctionApplySwitchPoint();
        boolean isApplyToCall = NashornCallSiteDescriptor.isApplyToCall(desc);
        boolean isFailedApplyToCall = isApplyToCall && applyToCallSwitchPoint.hasBeenInvalidated();
        MethodType appliedType = descType.dropParameterTypes(0, 1);
        if (!passesThis) {
            appliedType = appliedType.insertParameterTypes(1, Object.class);
        } else if (appliedFnNeedsWrappedThis) {
            appliedType = appliedType.changeParameterType(1, Object.class);
        }
        MethodType dropArgs = Lookup.MH.type(Void.TYPE, new Class[0]);
        if (isApply && !isFailedApplyToCall) {
            int pc = appliedType.parameterCount();
            for (int i = 3; i < pc; ++i) {
                dropArgs = dropArgs.appendParameterTypes(new Class[]{appliedType.parameterType(i)});
            }
            if (pc > 3) {
                appliedType = appliedType.dropParameterTypes(3, pc);
            }
        }
        if (isApply || isFailedApplyToCall) {
            if (passesArgs) {
                appliedType = appliedType.changeParameterType(2, Object[].class);
                if (isFailedApplyToCall) {
                    appliedType = appliedType.dropParameterTypes(3, paramCount - 1);
                }
            } else {
                appliedType = appliedType.insertParameterTypes(2, Object[].class);
            }
        }
        appliedDesc = appliedDesc.changeMethodType(appliedType);
        Object[] appliedArgs = new Object[isApply ? 3 : appliedType.parameterCount()];
        appliedArgs[0] = appliedFn;
        Object object = passesThis ? (appliedFnNeedsWrappedThis ? ScriptFunctionData.wrapThis(args2[2]) : args2[2]) : (appliedArgs[1] = ScriptRuntime.UNDEFINED);
        if (isApply && !isFailedApplyToCall) {
            appliedArgs[2] = passesArgs ? NativeFunction.toApplyArgs(args2[3]) : ScriptRuntime.EMPTY_ARRAY;
        } else if (passesArgs) {
            if (isFailedApplyToCall) {
                Object[] tmp = new Object[args2.length - 3];
                System.arraycopy(args2, 3, tmp, 0, tmp.length);
                appliedArgs[2] = NativeFunction.toApplyArgs(tmp);
            } else {
                assert (!isApply);
                System.arraycopy(args2, 3, appliedArgs, 2, args2.length - 3);
            }
        } else if (isFailedApplyToCall) {
            appliedArgs[2] = ScriptRuntime.EMPTY_ARRAY;
        }
        LinkRequest appliedRequest = request.replaceArguments(appliedDesc, appliedArgs);
        try {
            appliedInvocation = Bootstrap.getLinkerServices().getGuardedInvocation(appliedRequest);
        }
        catch (Error | RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        assert (appliedRequest != null);
        TypeDescriptor.OfField applyFnType = descType.parameterType(0);
        MethodHandle inv = appliedInvocation.getInvocation();
        if (isApply && !isFailedApplyToCall) {
            inv = passesArgs ? Lookup.MH.filterArguments(inv, 2, NativeFunction.TO_APPLY_ARGS) : Lookup.MH.insertArguments(inv, 2, new Object[]{ScriptRuntime.EMPTY_ARRAY});
        }
        if (isApplyToCall) {
            if (isFailedApplyToCall) {
                Context.getContextTrusted().getLogger(ApplySpecialization.class).info("Collection arguments to revert call to apply in " + appliedFn);
                inv = Lookup.MH.asCollector(inv, Object[].class, realArgCount);
            } else {
                appliedInvocation = appliedInvocation.addSwitchPoint(applyToCallSwitchPoint);
            }
        }
        if (!passesThis) {
            inv = ScriptFunction.bindImplicitThis(appliedFn, inv);
        } else if (appliedFnNeedsWrappedThis) {
            inv = Lookup.MH.filterArguments(inv, 1, WRAP_THIS);
        }
        inv = Lookup.MH.dropArguments(inv, 0, new Class[]{applyFnType});
        for (int i = 0; i < dropArgs.parameterCount(); ++i) {
            inv = Lookup.MH.dropArguments(inv, 4 + i, new Class[]{dropArgs.parameterType(i)});
        }
        MethodHandle guard = appliedInvocation.getGuard();
        if (!passesThis && guard.type().parameterCount() > 1) {
            guard = ScriptFunction.bindImplicitThis(appliedFn, guard);
        }
        MethodType guardType = guard.type();
        guard = Lookup.MH.dropArguments(guard, 0, new Class[]{descType.parameterType(0)});
        MethodHandle applyFnGuard = Lookup.MH.insertArguments(IS_APPLY_FUNCTION, 2, this);
        applyFnGuard = Lookup.MH.dropArguments(applyFnGuard, 2, guardType.parameterArray());
        guard = Lookup.MH.foldArguments(applyFnGuard, guard);
        return appliedInvocation.replaceMethods(inv, guard);
    }

    private GuardedInvocation createVarArgApplyOrCallCall(boolean isApply, CallSiteDescriptor desc, LinkRequest request, Object[] args2) {
        MethodType descType = desc.getMethodType();
        int paramCount = descType.parameterCount();
        Object[] varArgs = (Object[])args2[paramCount - 1];
        int copiedArgCount = args2.length - 1;
        int varArgCount = varArgs.length;
        Object[] spreadArgs = new Object[copiedArgCount + varArgCount];
        System.arraycopy(args2, 0, spreadArgs, 0, copiedArgCount);
        System.arraycopy(varArgs, 0, spreadArgs, copiedArgCount, varArgCount);
        MethodType spreadType = descType.dropParameterTypes(paramCount - 1, paramCount).appendParameterTypes(Collections.nCopies(varArgCount, Object.class));
        CallSiteDescriptor spreadDesc = desc.changeMethodType(spreadType);
        LinkRequest spreadRequest = request.replaceArguments(spreadDesc, spreadArgs);
        GuardedInvocation spreadInvocation = this.createApplyOrCallCall(isApply, spreadDesc, spreadRequest, spreadArgs);
        return spreadInvocation.replaceMethods(ScriptFunction.pairArguments(spreadInvocation.getInvocation(), descType), ScriptFunction.spreadGuardArguments(spreadInvocation.getGuard(), descType));
    }

    private static MethodHandle spreadGuardArguments(MethodHandle guard, MethodType descType) {
        int descParamCount;
        MethodType guardType = guard.type();
        int guardParamCount = guardType.parameterCount();
        int spreadCount = guardParamCount - (descParamCount = descType.parameterCount()) + 1;
        if (spreadCount <= 0) {
            return guard;
        }
        MethodHandle arrayConvertingGuard = ((Class)guardType.parameterType(guardParamCount - 1)).isArray() ? Lookup.MH.filterArguments(guard, guardParamCount - 1, NativeFunction.TO_APPLY_ARGS) : guard;
        return ScriptObject.adaptHandleToVarArgCallSite(arrayConvertingGuard, descParamCount);
    }

    private static MethodHandle bindImplicitThis(Object fn, MethodHandle mh) {
        MethodHandle bound = fn instanceof ScriptFunction && ((ScriptFunction)fn).needsWrappedThis() ? Lookup.MH.filterArguments(mh, 1, SCRIPTFUNCTION_GLOBALFILTER) : mh;
        return Lookup.MH.insertArguments(bound, 1, ScriptRuntime.UNDEFINED);
    }

    MethodHandle getCallMethodHandle(MethodType type, String bindName) {
        return ScriptFunction.pairArguments(ScriptFunction.bindToNameIfNeeded(this.bindToCalleeIfNeeded(this.data.getGenericInvoker(this.scope)), bindName), type);
    }

    private static MethodHandle bindToNameIfNeeded(MethodHandle methodHandle, String bindName) {
        boolean isVarArg;
        if (bindName == null) {
            return methodHandle;
        }
        MethodType methodType = methodHandle.type();
        int parameterCount = methodType.parameterCount();
        boolean bl = isVarArg = parameterCount > 0 && ((Class)methodType.parameterType(parameterCount - 1)).isArray();
        if (isVarArg) {
            return Lookup.MH.filterArguments(methodHandle, 1, Lookup.MH.insertArguments(ADD_ZEROTH_ELEMENT, 1, bindName));
        }
        return Lookup.MH.insertArguments(methodHandle, 1, bindName);
    }

    private static MethodHandle getFunctionGuard(ScriptFunction function, int flags) {
        assert (function.data != null);
        if (function.data.isBuiltin()) {
            return Guards.getIdentityGuard(function);
        }
        return Lookup.MH.insertArguments(IS_FUNCTION_MH, 1, function.data);
    }

    private static MethodHandle getNonStrictFunctionGuard(ScriptFunction function) {
        assert (function.data != null);
        return Lookup.MH.insertArguments(IS_NONSTRICT_FUNCTION, 2, function.data);
    }

    private static boolean isFunctionMH(Object self, ScriptFunctionData data) {
        return self instanceof ScriptFunction && ((ScriptFunction)self).data == data;
    }

    private static boolean isNonStrictFunction(Object self, Object arg, ScriptFunctionData data) {
        return self instanceof ScriptFunction && ((ScriptFunction)self).data == data && arg instanceof ScriptObject;
    }

    private static boolean isApplyFunction(boolean appliedFnCondition, Object self, Object expectedSelf) {
        return appliedFnCondition && self == expectedSelf;
    }

    private static Object[] addZerothElement(Object[] args2, Object value) {
        Object[] src = args2 == null ? ScriptRuntime.EMPTY_ARRAY : args2;
        Object[] result = new Object[src.length + 1];
        System.arraycopy(src, 0, result, 1, src.length);
        result[0] = value;
        return result;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static Object invokeSync(ScriptFunction func, Object sync, Object self, Object ... args2) throws Throwable {
        Object syncObj;
        Object object = syncObj = sync == ScriptRuntime.UNDEFINED ? self : sync;
        synchronized (object) {
            return func.invoke(self, args2);
        }
    }

    private static MethodHandle findOwnMH_S(String name, Class<?> rtype, Class<?> ... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), ScriptFunction.class, name, Lookup.MH.type(rtype, types));
    }

    private static MethodHandle findOwnMH_V(String name, Class<?> rtype, Class<?> ... types) {
        return Lookup.MH.findVirtual(MethodHandles.lookup(), ScriptFunction.class, name, Lookup.MH.type(rtype, types));
    }

    static {
        LAZY_PROTOTYPE = new Object();
        anonmap$ = PropertyMap.newMap();
        ArrayList<Property> properties = new ArrayList<Property>(3);
        properties.add(AccessorProperty.create("prototype", 6, G$PROTOTYPE, S$PROTOTYPE));
        properties.add(AccessorProperty.create("length", 7, G$LENGTH, null));
        properties.add(AccessorProperty.create("name", 7, G$NAME, null));
        map$ = PropertyMap.newMap(properties);
        strictmodemap$ = ScriptFunction.createStrictModeMap(map$);
        boundfunctionmap$ = ScriptFunction.createBoundFunctionMap(strictmodemap$);
        if (Context.DEBUG) {
            constructorCount = new LongAdder();
            invokes = new LongAdder();
            allocations = new LongAdder();
        }
    }

    private static class Bound
    extends ScriptFunction {
        private final ScriptFunction target;

        Bound(ScriptFunctionData boundData, ScriptFunction target) {
            super(boundData, boundfunctionmap$, null, Global.instance());
            this.setPrototype(ScriptRuntime.UNDEFINED);
            this.target = target;
        }

        @Override
        protected ScriptFunction getTargetFunction() {
            return this.target;
        }
    }
}

