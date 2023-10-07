/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.linker;

import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import java.util.Map;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.beans.BeansLinker;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.GuardedTypeConversion;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;
import jdk.internal.dynalink.linker.GuardingTypeConverterFactory;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.support.Guards;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;
import jdk.nashorn.internal.runtime.linker.NashornLinker;

final class NashornBottomLinker
implements GuardingDynamicLinker,
GuardingTypeConverterFactory {
    private static final MethodHandle EMPTY_PROP_GETTER = Lookup.MH.dropArguments(Lookup.MH.constant(Object.class, ScriptRuntime.UNDEFINED), 0, Object.class);
    private static final MethodHandle EMPTY_ELEM_GETTER = Lookup.MH.dropArguments(EMPTY_PROP_GETTER, 0, Object.class);
    private static final MethodHandle EMPTY_PROP_SETTER = Lookup.MH.asType(EMPTY_ELEM_GETTER, EMPTY_ELEM_GETTER.type().changeReturnType(Void.TYPE));
    private static final MethodHandle EMPTY_ELEM_SETTER = Lookup.MH.dropArguments(EMPTY_PROP_SETTER, 0, Object.class);
    private static final Map<Class<?>, MethodHandle> CONVERTERS = new HashMap();

    NashornBottomLinker() {
    }

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) throws Exception {
        Object self = linkRequest.getReceiver();
        if (self == null) {
            return NashornBottomLinker.linkNull(linkRequest);
        }
        assert (NashornBottomLinker.isExpectedObject(self)) : "Couldn't link " + linkRequest.getCallSiteDescriptor() + " for " + self.getClass().getName();
        return NashornBottomLinker.linkBean(linkRequest, linkerServices);
    }

    private static GuardedInvocation linkBean(LinkRequest linkRequest, LinkerServices linkerServices) throws Exception {
        String operator;
        NashornCallSiteDescriptor desc = (NashornCallSiteDescriptor)linkRequest.getCallSiteDescriptor();
        Object self = linkRequest.getReceiver();
        switch (operator = desc.getFirstOperator()) {
            case "new": {
                if (BeansLinker.isDynamicConstructor(self)) {
                    throw ECMAErrors.typeError("no.constructor.matches.args", ScriptRuntime.safeToString(self));
                }
                if (BeansLinker.isDynamicMethod(self)) {
                    throw ECMAErrors.typeError("method.not.constructor", ScriptRuntime.safeToString(self));
                }
                throw ECMAErrors.typeError("not.a.function", desc.getFunctionErrorMessage(self));
            }
            case "call": {
                if (BeansLinker.isDynamicConstructor(self)) {
                    throw ECMAErrors.typeError("constructor.requires.new", ScriptRuntime.safeToString(self));
                }
                if (BeansLinker.isDynamicMethod(self)) {
                    throw ECMAErrors.typeError("no.method.matches.args", ScriptRuntime.safeToString(self));
                }
                throw ECMAErrors.typeError("not.a.function", desc.getFunctionErrorMessage(self));
            }
            case "callMethod": {
                throw ECMAErrors.typeError("no.such.function", NashornBottomLinker.getArgument(linkRequest), ScriptRuntime.safeToString(self));
            }
            case "getMethod": {
                return NashornBottomLinker.getInvocation(Lookup.MH.dropArguments(JSType.GET_UNDEFINED.get(2), 0, Object.class), self, linkerServices, desc);
            }
            case "getProp": 
            case "getElem": {
                if (NashornCallSiteDescriptor.isOptimistic(desc)) {
                    throw new UnwarrantedOptimismException(ScriptRuntime.UNDEFINED, NashornCallSiteDescriptor.getProgramPoint(desc), Type.OBJECT);
                }
                if (desc.getOperand() != null) {
                    return NashornBottomLinker.getInvocation(EMPTY_PROP_GETTER, self, linkerServices, desc);
                }
                return NashornBottomLinker.getInvocation(EMPTY_ELEM_GETTER, self, linkerServices, desc);
            }
            case "setProp": 
            case "setElem": {
                boolean strict = NashornCallSiteDescriptor.isStrict(desc);
                if (strict) {
                    throw ECMAErrors.typeError("cant.set.property", NashornBottomLinker.getArgument(linkRequest), ScriptRuntime.safeToString(self));
                }
                if (desc.getOperand() != null) {
                    return NashornBottomLinker.getInvocation(EMPTY_PROP_SETTER, self, linkerServices, desc);
                }
                return NashornBottomLinker.getInvocation(EMPTY_ELEM_SETTER, self, linkerServices, desc);
            }
        }
        throw new AssertionError((Object)("unknown call type " + desc));
    }

    @Override
    public GuardedTypeConversion convertToType(Class<?> sourceType, Class<?> targetType) throws Exception {
        GuardedInvocation gi = NashornBottomLinker.convertToTypeNoCast(sourceType, targetType);
        return gi == null ? null : new GuardedTypeConversion(gi.asType(Lookup.MH.type(targetType, sourceType)), true);
    }

    private static GuardedInvocation convertToTypeNoCast(Class<?> sourceType, Class<?> targetType) throws Exception {
        MethodHandle mh = CONVERTERS.get(targetType);
        if (mh != null) {
            return new GuardedInvocation(mh);
        }
        return null;
    }

    private static GuardedInvocation getInvocation(MethodHandle handle, Object self, LinkerServices linkerServices, CallSiteDescriptor desc) {
        return Bootstrap.asTypeSafeReturn(new GuardedInvocation(handle, Guards.getClassGuard(self.getClass())), linkerServices, desc);
    }

    private static boolean isExpectedObject(Object obj) {
        return !NashornLinker.canLinkTypeStatic(obj.getClass());
    }

    private static GuardedInvocation linkNull(LinkRequest linkRequest) {
        String operator;
        NashornCallSiteDescriptor desc = (NashornCallSiteDescriptor)linkRequest.getCallSiteDescriptor();
        switch (operator = desc.getFirstOperator()) {
            case "new": 
            case "call": {
                throw ECMAErrors.typeError("not.a.function", "null");
            }
            case "callMethod": 
            case "getMethod": {
                throw ECMAErrors.typeError("no.such.function", NashornBottomLinker.getArgument(linkRequest), "null");
            }
            case "getProp": 
            case "getElem": {
                throw ECMAErrors.typeError("cant.get.property", NashornBottomLinker.getArgument(linkRequest), "null");
            }
            case "setProp": 
            case "setElem": {
                throw ECMAErrors.typeError("cant.set.property", NashornBottomLinker.getArgument(linkRequest), "null");
            }
        }
        throw new AssertionError((Object)("unknown call type " + desc));
    }

    private static String getArgument(LinkRequest linkRequest) {
        CallSiteDescriptor desc = linkRequest.getCallSiteDescriptor();
        if (desc.getNameTokenCount() > 2) {
            return desc.getNameToken(2);
        }
        return ScriptRuntime.safeToString(linkRequest.getArguments()[1]);
    }

    static {
        CONVERTERS.put(Boolean.TYPE, JSType.TO_BOOLEAN.methodHandle());
        CONVERTERS.put(Double.TYPE, JSType.TO_NUMBER.methodHandle());
        CONVERTERS.put(Integer.TYPE, JSType.TO_INTEGER.methodHandle());
        CONVERTERS.put(Long.TYPE, JSType.TO_LONG.methodHandle());
        CONVERTERS.put(String.class, JSType.TO_STRING.methodHandle());
    }
}

