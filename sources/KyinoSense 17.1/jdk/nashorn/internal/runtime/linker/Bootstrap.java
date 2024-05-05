/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.linker;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.TypeDescriptor;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.DynamicLinker;
import jdk.internal.dynalink.DynamicLinkerFactory;
import jdk.internal.dynalink.GuardedInvocationFilter;
import jdk.internal.dynalink.beans.BeansLinker;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.MethodTypeConversionStrategy;
import jdk.internal.dynalink.support.TypeUtilities;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.lookup.MethodHandleFactory;
import jdk.nashorn.internal.lookup.MethodHandleFunctionality;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ECMAException;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.OptimisticReturnFilters;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.linker.BoundCallable;
import jdk.nashorn.internal.runtime.linker.BoundCallableLinker;
import jdk.nashorn.internal.runtime.linker.BrowserJSObjectLinker;
import jdk.nashorn.internal.runtime.linker.JSObjectLinker;
import jdk.nashorn.internal.runtime.linker.JavaSuperAdapter;
import jdk.nashorn.internal.runtime.linker.JavaSuperAdapterLinker;
import jdk.nashorn.internal.runtime.linker.LinkerCallSite;
import jdk.nashorn.internal.runtime.linker.NashornBeansLinker;
import jdk.nashorn.internal.runtime.linker.NashornBottomLinker;
import jdk.nashorn.internal.runtime.linker.NashornLinker;
import jdk.nashorn.internal.runtime.linker.NashornPrimitiveLinker;
import jdk.nashorn.internal.runtime.linker.NashornStaticClassLinker;
import jdk.nashorn.internal.runtime.linker.ReflectionCheckLinker;
import jdk.nashorn.internal.runtime.options.Options;

public final class Bootstrap {
    public static final CompilerConstants.Call BOOTSTRAP = CompilerConstants.staticCallNoLookup(Bootstrap.class, "bootstrap", CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, Integer.TYPE);
    private static final MethodHandleFunctionality MH = MethodHandleFactory.getFunctionality();
    private static final MethodHandle VOID_TO_OBJECT = MH.constant(Object.class, ScriptRuntime.UNDEFINED);
    private static final int NASHORN_DEFAULT_UNSTABLE_RELINK_THRESHOLD = 16;
    private static final DynamicLinker dynamicLinker;

    private Bootstrap() {
    }

    public static boolean isCallable(Object obj) {
        if (obj == ScriptRuntime.UNDEFINED || obj == null) {
            return false;
        }
        return obj instanceof ScriptFunction || Bootstrap.isJSObjectFunction(obj) || BeansLinker.isDynamicMethod(obj) || obj instanceof BoundCallable || Bootstrap.isFunctionalInterfaceObject(obj) || obj instanceof StaticClass;
    }

    public static boolean isStrictCallable(Object callable) {
        if (callable instanceof ScriptFunction) {
            return ((ScriptFunction)callable).isStrict();
        }
        if (Bootstrap.isJSObjectFunction(callable)) {
            return ((JSObject)callable).isStrictFunction();
        }
        if (callable instanceof BoundCallable) {
            return Bootstrap.isStrictCallable(((BoundCallable)callable).getCallable());
        }
        if (BeansLinker.isDynamicMethod(callable) || callable instanceof StaticClass || Bootstrap.isFunctionalInterfaceObject(callable)) {
            return false;
        }
        throw Bootstrap.notFunction(callable);
    }

    private static ECMAException notFunction(Object obj) {
        return ECMAErrors.typeError("not.a.function", ScriptRuntime.safeToString(obj));
    }

    private static boolean isJSObjectFunction(Object obj) {
        return obj instanceof JSObject && ((JSObject)obj).isFunction();
    }

    public static boolean isDynamicMethod(Object obj) {
        return BeansLinker.isDynamicMethod(obj instanceof BoundCallable ? ((BoundCallable)obj).getCallable() : obj);
    }

    public static boolean isFunctionalInterfaceObject(Object obj) {
        return !JSType.isPrimitive(obj) && NashornBeansLinker.getFunctionalInterfaceMethodName(obj.getClass()) != null;
    }

    public static CallSite bootstrap(MethodHandles.Lookup lookup, String opDesc, MethodType type, int flags) {
        return dynamicLinker.link(LinkerCallSite.newLinkerCallSite(lookup, opDesc, type, flags));
    }

    public static CallSite mathBootstrap(MethodHandles.Lookup lookup, String name, MethodType type, int programPoint) {
        MethodHandle mh;
        switch (name) {
            case "iadd": {
                mh = JSType.ADD_EXACT.methodHandle();
                break;
            }
            case "isub": {
                mh = JSType.SUB_EXACT.methodHandle();
                break;
            }
            case "imul": {
                mh = JSType.MUL_EXACT.methodHandle();
                break;
            }
            case "idiv": {
                mh = JSType.DIV_EXACT.methodHandle();
                break;
            }
            case "irem": {
                mh = JSType.REM_EXACT.methodHandle();
                break;
            }
            case "ineg": {
                mh = JSType.NEGATE_EXACT.methodHandle();
                break;
            }
            default: {
                throw new AssertionError((Object)"unsupported math intrinsic");
            }
        }
        return new ConstantCallSite(MH.insertArguments(mh, mh.type().parameterCount() - 1, programPoint));
    }

    public static MethodHandle createDynamicInvoker(String opDesc, Class<?> rtype, Class<?> ... ptypes) {
        return Bootstrap.createDynamicInvoker(opDesc, MethodType.methodType(rtype, ptypes));
    }

    public static MethodHandle createDynamicInvoker(String opDesc, int flags, Class<?> rtype, Class<?> ... ptypes) {
        return Bootstrap.bootstrap(MethodHandles.publicLookup(), opDesc, MethodType.methodType(rtype, ptypes), flags).dynamicInvoker();
    }

    public static MethodHandle createDynamicInvoker(String opDesc, MethodType type) {
        return Bootstrap.bootstrap(MethodHandles.publicLookup(), opDesc, type, 0).dynamicInvoker();
    }

    public static Object bindCallable(Object callable, Object boundThis, Object[] boundArgs) {
        if (callable instanceof ScriptFunction) {
            return ((ScriptFunction)callable).createBound(boundThis, boundArgs);
        }
        if (callable instanceof BoundCallable) {
            return ((BoundCallable)callable).bind(boundArgs);
        }
        if (Bootstrap.isCallable(callable)) {
            return new BoundCallable(callable, boundThis, boundArgs);
        }
        throw Bootstrap.notFunction(callable);
    }

    public static Object createSuperAdapter(Object adapter) {
        return new JavaSuperAdapter(adapter);
    }

    public static void checkReflectionAccess(Class<?> clazz, boolean isStatic) {
        ReflectionCheckLinker.checkReflectionAccess(clazz, isStatic);
    }

    public static LinkerServices getLinkerServices() {
        return dynamicLinker.getLinkerServices();
    }

    static GuardedInvocation asTypeSafeReturn(GuardedInvocation inv, LinkerServices linkerServices, CallSiteDescriptor desc) {
        return inv == null ? null : inv.asTypeSafeReturn(linkerServices, desc.getMethodType());
    }

    private static MethodHandle unboxReturnType(MethodHandle target, MethodType newType) {
        MethodType targetType = target.type();
        TypeDescriptor.OfField oldReturnType = targetType.returnType();
        TypeDescriptor.OfField newReturnType = newType.returnType();
        if (TypeUtilities.isWrapperType(oldReturnType)) {
            if (((Class)newReturnType).isPrimitive()) {
                assert (TypeUtilities.isMethodInvocationConvertible(oldReturnType, newReturnType));
                return MethodHandles.explicitCastArguments(target, targetType.changeReturnType((Class<?>)newReturnType));
            }
        } else if (oldReturnType == Void.TYPE && newReturnType == Object.class) {
            return MethodHandles.filterReturnValue(target, VOID_TO_OBJECT);
        }
        return target;
    }

    static {
        DynamicLinkerFactory factory = new DynamicLinkerFactory();
        NashornBeansLinker nashornBeansLinker = new NashornBeansLinker();
        factory.setPrioritizedLinkers(new NashornLinker(), new NashornPrimitiveLinker(), new NashornStaticClassLinker(), new BoundCallableLinker(), new JavaSuperAdapterLinker(), new JSObjectLinker(nashornBeansLinker), new BrowserJSObjectLinker(nashornBeansLinker), new ReflectionCheckLinker());
        factory.setFallbackLinkers(nashornBeansLinker, new NashornBottomLinker());
        factory.setSyncOnRelink(true);
        factory.setPrelinkFilter(new GuardedInvocationFilter(){

            @Override
            public GuardedInvocation filter(GuardedInvocation inv, LinkRequest request, LinkerServices linkerServices) {
                CallSiteDescriptor desc = request.getCallSiteDescriptor();
                return OptimisticReturnFilters.filterOptimisticReturnValue(inv, desc).asType(linkerServices, desc.getMethodType());
            }
        });
        factory.setAutoConversionStrategy(new MethodTypeConversionStrategy(){

            @Override
            public MethodHandle asType(MethodHandle target, MethodType newType) {
                return Bootstrap.unboxReturnType(target, newType);
            }
        });
        factory.setInternalObjectsFilter(NashornBeansLinker.createHiddenObjectFilter());
        int relinkThreshold = Options.getIntProperty("nashorn.unstable.relink.threshold", 16);
        if (relinkThreshold > -1) {
            factory.setUnstableRelinkThreshold(relinkThreshold);
        }
        factory.setClassLoader(Bootstrap.class.getClassLoader());
        dynamicLinker = factory.createLinker();
    }
}

