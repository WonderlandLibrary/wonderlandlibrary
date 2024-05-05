/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Map;
import javax.script.Bindings;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.lookup.MethodHandleFactory;
import jdk.nashorn.internal.lookup.MethodHandleFunctionality;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.linker.NashornBeansLinker;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;
import jdk.nashorn.internal.runtime.linker.NashornGuards;

final class JSObjectLinker
implements TypeBasedGuardingDynamicLinker {
    private final NashornBeansLinker nashornBeansLinker;
    private static final MethodHandleFunctionality MH = MethodHandleFactory.getFunctionality();
    private static final MethodHandle IS_JSOBJECT_GUARD = JSObjectLinker.findOwnMH_S("isJSObject", Boolean.TYPE, Object.class);
    private static final MethodHandle JSOBJECTLINKER_GET = JSObjectLinker.findOwnMH_S("get", Object.class, MethodHandle.class, Object.class, Object.class);
    private static final MethodHandle JSOBJECTLINKER_PUT = JSObjectLinker.findOwnMH_S("put", Void.TYPE, Object.class, Object.class, Object.class);
    private static final MethodHandle JSOBJECT_GETMEMBER = JSObjectLinker.findJSObjectMH_V("getMember", Object.class, String.class);
    private static final MethodHandle JSOBJECT_SETMEMBER = JSObjectLinker.findJSObjectMH_V("setMember", Void.TYPE, String.class, Object.class);
    private static final MethodHandle JSOBJECT_CALL = JSObjectLinker.findJSObjectMH_V("call", Object.class, Object.class, Object[].class);
    private static final MethodHandle JSOBJECT_SCOPE_CALL = JSObjectLinker.findOwnMH_S("jsObjectScopeCall", Object.class, JSObject.class, Object.class, Object[].class);
    private static final MethodHandle JSOBJECT_CALL_TO_APPLY = JSObjectLinker.findOwnMH_S("callToApply", Object.class, MethodHandle.class, JSObject.class, Object.class, Object[].class);
    private static final MethodHandle JSOBJECT_NEW = JSObjectLinker.findJSObjectMH_V("newObject", Object.class, Object[].class);

    JSObjectLinker(NashornBeansLinker nashornBeansLinker) {
        this.nashornBeansLinker = nashornBeansLinker;
    }

    @Override
    public boolean canLinkType(Class<?> type) {
        return JSObjectLinker.canLinkTypeStatic(type);
    }

    static boolean canLinkTypeStatic(Class<?> type) {
        return Map.class.isAssignableFrom(type) || Bindings.class.isAssignableFrom(type) || JSObject.class.isAssignableFrom(type);
    }

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest request, LinkerServices linkerServices) throws Exception {
        GuardedInvocation inv;
        LinkRequest requestWithoutContext = request.withoutRuntimeContext();
        Object self = requestWithoutContext.getReceiver();
        CallSiteDescriptor desc = requestWithoutContext.getCallSiteDescriptor();
        if (desc.getNameTokenCount() < 2 || !"dyn".equals(desc.getNameToken(0))) {
            return null;
        }
        if (self instanceof JSObject) {
            inv = this.lookup(desc, request, linkerServices);
            inv = inv.replaceMethods(linkerServices.filterInternalObjects(inv.getInvocation()), inv.getGuard());
        } else if (self instanceof Map || self instanceof Bindings) {
            GuardedInvocation beanInv = this.nashornBeansLinker.getGuardedInvocation(request, linkerServices);
            inv = new GuardedInvocation(beanInv.getInvocation(), NashornGuards.combineGuards(beanInv.getGuard(), NashornGuards.getNotJSObjectGuard()));
        } else {
            throw new AssertionError();
        }
        return Bootstrap.asTypeSafeReturn(inv, linkerServices, desc);
    }

    private GuardedInvocation lookup(CallSiteDescriptor desc, LinkRequest request, LinkerServices linkerServices) throws Exception {
        String operator = CallSiteDescriptorFactory.tokenizeOperators(desc).get(0);
        int c = desc.getNameTokenCount();
        switch (operator) {
            case "getProp": 
            case "getElem": 
            case "getMethod": {
                if (c > 2) {
                    return JSObjectLinker.findGetMethod(desc);
                }
                return JSObjectLinker.findGetIndexMethod(this.nashornBeansLinker.getGuardedInvocation(request, linkerServices));
            }
            case "setProp": 
            case "setElem": {
                return c > 2 ? JSObjectLinker.findSetMethod(desc) : JSObjectLinker.findSetIndexMethod();
            }
            case "call": {
                return JSObjectLinker.findCallMethod(desc);
            }
            case "new": {
                return JSObjectLinker.findNewMethod(desc);
            }
        }
        return null;
    }

    private static GuardedInvocation findGetMethod(CallSiteDescriptor desc) {
        String name = desc.getNameToken(2);
        MethodHandle getter = MH.insertArguments(JSOBJECT_GETMEMBER, 1, name);
        return new GuardedInvocation(getter, IS_JSOBJECT_GUARD);
    }

    private static GuardedInvocation findGetIndexMethod(GuardedInvocation inv) {
        MethodHandle getter = MH.insertArguments(JSOBJECTLINKER_GET, 0, inv.getInvocation());
        return inv.replaceMethods(getter, inv.getGuard());
    }

    private static GuardedInvocation findSetMethod(CallSiteDescriptor desc) {
        MethodHandle getter = MH.insertArguments(JSOBJECT_SETMEMBER, 1, desc.getNameToken(2));
        return new GuardedInvocation(getter, IS_JSOBJECT_GUARD);
    }

    private static GuardedInvocation findSetIndexMethod() {
        return new GuardedInvocation(JSOBJECTLINKER_PUT, IS_JSOBJECT_GUARD);
    }

    private static GuardedInvocation findCallMethod(CallSiteDescriptor desc) {
        MethodType type;
        MethodHandle mh;
        MethodHandle methodHandle = mh = NashornCallSiteDescriptor.isScope(desc) ? JSOBJECT_SCOPE_CALL : JSOBJECT_CALL;
        if (NashornCallSiteDescriptor.isApplyToCall(desc)) {
            mh = MH.insertArguments(JSOBJECT_CALL_TO_APPLY, 0, mh);
        }
        mh = (type = desc.getMethodType()).parameterType(type.parameterCount() - 1) == Object[].class ? mh : MH.asCollector(mh, Object[].class, type.parameterCount() - 2);
        return new GuardedInvocation(mh, IS_JSOBJECT_GUARD);
    }

    private static GuardedInvocation findNewMethod(CallSiteDescriptor desc) {
        MethodHandle func = MH.asCollector(JSOBJECT_NEW, Object[].class, desc.getMethodType().parameterCount() - 1);
        return new GuardedInvocation(func, IS_JSOBJECT_GUARD);
    }

    private static boolean isJSObject(Object self) {
        return self instanceof JSObject;
    }

    private static Object get(MethodHandle fallback, Object jsobj, Object key) throws Throwable {
        if (key instanceof Integer) {
            return ((JSObject)jsobj).getSlot((Integer)key);
        }
        if (key instanceof Number) {
            int index = JSObjectLinker.getIndex((Number)key);
            if (index > -1) {
                return ((JSObject)jsobj).getSlot(index);
            }
            return ((JSObject)jsobj).getMember(JSType.toString(key));
        }
        if (JSType.isString(key)) {
            String name = key.toString();
            if (name.indexOf(40) != -1) {
                return fallback.invokeExact(jsobj, name);
            }
            return ((JSObject)jsobj).getMember(name);
        }
        return null;
    }

    private static void put(Object jsobj, Object key, Object value) {
        if (key instanceof Integer) {
            ((JSObject)jsobj).setSlot((Integer)key, value);
        } else if (key instanceof Number) {
            int index = JSObjectLinker.getIndex((Number)key);
            if (index > -1) {
                ((JSObject)jsobj).setSlot(index, value);
            } else {
                ((JSObject)jsobj).setMember(JSType.toString(key), value);
            }
        } else if (JSType.isString(key)) {
            ((JSObject)jsobj).setMember(key.toString(), value);
        }
    }

    private static int getIndex(Number n) {
        double value = n.doubleValue();
        return JSType.isRepresentableAsInt(value) ? (int)value : -1;
    }

    private static Object callToApply(MethodHandle mh, JSObject obj, Object thiz, Object ... args2) {
        assert (args2.length >= 2);
        Object receiver = args2[0];
        Object[] arguments = new Object[args2.length - 1];
        System.arraycopy(args2, 1, arguments, 0, arguments.length);
        try {
            return mh.invokeExact(obj, thiz, new Object[]{receiver, arguments});
        }
        catch (Error | RuntimeException e) {
            throw e;
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static Object jsObjectScopeCall(JSObject jsObj, Object thiz, Object[] args2) {
        Object modifiedThiz;
        if (thiz == ScriptRuntime.UNDEFINED && !jsObj.isStrictFunction()) {
            Global global = Context.getGlobal();
            modifiedThiz = ScriptObjectMirror.wrap(global, global);
        } else {
            modifiedThiz = thiz;
        }
        return jsObj.call(modifiedThiz, args2);
    }

    private static MethodHandle findJSObjectMH_V(String name, Class<?> rtype, Class<?> ... types) {
        return MH.findVirtual(MethodHandles.lookup(), JSObject.class, name, MH.type(rtype, types));
    }

    private static MethodHandle findOwnMH_S(String name, Class<?> rtype, Class<?> ... types) {
        return MH.findStatic(MethodHandles.lookup(), JSObjectLinker.class, name, MH.type(rtype, types));
    }
}

