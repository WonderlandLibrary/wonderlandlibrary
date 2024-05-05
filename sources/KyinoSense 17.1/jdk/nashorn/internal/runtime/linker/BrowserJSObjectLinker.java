/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.nashorn.internal.lookup.MethodHandleFactory;
import jdk.nashorn.internal.lookup.MethodHandleFunctionality;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.linker.NashornBeansLinker;

final class BrowserJSObjectLinker
implements TypeBasedGuardingDynamicLinker {
    private static final ClassLoader myLoader = BrowserJSObjectLinker.class.getClassLoader();
    private static final String JSOBJECT_CLASS = "netscape.javascript.JSObject";
    private static volatile Class<?> jsObjectClass;
    private final NashornBeansLinker nashornBeansLinker;
    private static final MethodHandleFunctionality MH;
    private static final MethodHandle IS_JSOBJECT_GUARD;
    private static final MethodHandle JSOBJECTLINKER_GET;
    private static final MethodHandle JSOBJECTLINKER_PUT;

    BrowserJSObjectLinker(NashornBeansLinker nashornBeansLinker) {
        this.nashornBeansLinker = nashornBeansLinker;
    }

    @Override
    public boolean canLinkType(Class<?> type) {
        return BrowserJSObjectLinker.canLinkTypeStatic(type);
    }

    static boolean canLinkTypeStatic(Class<?> type) {
        if (jsObjectClass != null && jsObjectClass.isAssignableFrom(type)) {
            return true;
        }
        for (Class<?> clazz = type; clazz != null; clazz = clazz.getSuperclass()) {
            if (clazz.getClassLoader() != myLoader || !clazz.getName().equals(JSOBJECT_CLASS)) continue;
            jsObjectClass = clazz;
            return true;
        }
        return false;
    }

    private static void checkJSObjectClass() {
        assert (jsObjectClass != null) : "netscape.javascript.JSObject not found!";
    }

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest request, LinkerServices linkerServices) throws Exception {
        LinkRequest requestWithoutContext = request.withoutRuntimeContext();
        Object self = requestWithoutContext.getReceiver();
        CallSiteDescriptor desc = requestWithoutContext.getCallSiteDescriptor();
        BrowserJSObjectLinker.checkJSObjectClass();
        if (desc.getNameTokenCount() < 2 || !"dyn".equals(desc.getNameToken(0))) {
            return null;
        }
        if (!jsObjectClass.isInstance(self)) {
            throw new AssertionError();
        }
        GuardedInvocation inv = this.lookup(desc, request, linkerServices);
        inv = inv.replaceMethods(linkerServices.filterInternalObjects(inv.getInvocation()), inv.getGuard());
        return Bootstrap.asTypeSafeReturn(inv, linkerServices, desc);
    }

    private GuardedInvocation lookup(CallSiteDescriptor desc, LinkRequest request, LinkerServices linkerServices) throws Exception {
        GuardedInvocation inv;
        String operator = CallSiteDescriptorFactory.tokenizeOperators(desc).get(0);
        int c = desc.getNameTokenCount();
        try {
            inv = this.nashornBeansLinker.getGuardedInvocation(request, linkerServices);
        }
        catch (Throwable th) {
            inv = null;
        }
        switch (operator) {
            case "getProp": 
            case "getElem": 
            case "getMethod": {
                return c > 2 ? BrowserJSObjectLinker.findGetMethod(desc, inv) : BrowserJSObjectLinker.findGetIndexMethod(inv);
            }
            case "setProp": 
            case "setElem": {
                return c > 2 ? BrowserJSObjectLinker.findSetMethod(desc, inv) : BrowserJSObjectLinker.findSetIndexMethod();
            }
            case "call": {
                return BrowserJSObjectLinker.findCallMethod(desc);
            }
        }
        return null;
    }

    private static GuardedInvocation findGetMethod(CallSiteDescriptor desc, GuardedInvocation inv) {
        if (inv != null) {
            return inv;
        }
        String name = desc.getNameToken(2);
        MethodHandle getter = MH.insertArguments(JSObjectHandles.JSOBJECT_GETMEMBER, 1, name);
        return new GuardedInvocation(getter, IS_JSOBJECT_GUARD);
    }

    private static GuardedInvocation findGetIndexMethod(GuardedInvocation inv) {
        MethodHandle getter = MH.insertArguments(JSOBJECTLINKER_GET, 0, inv.getInvocation());
        return inv.replaceMethods(getter, inv.getGuard());
    }

    private static GuardedInvocation findSetMethod(CallSiteDescriptor desc, GuardedInvocation inv) {
        if (inv != null) {
            return inv;
        }
        MethodHandle getter = MH.insertArguments(JSObjectHandles.JSOBJECT_SETMEMBER, 1, desc.getNameToken(2));
        return new GuardedInvocation(getter, IS_JSOBJECT_GUARD);
    }

    private static GuardedInvocation findSetIndexMethod() {
        return new GuardedInvocation(JSOBJECTLINKER_PUT, IS_JSOBJECT_GUARD);
    }

    private static GuardedInvocation findCallMethod(CallSiteDescriptor desc) {
        MethodHandle call = MH.insertArguments(JSObjectHandles.JSOBJECT_CALL, 1, "call");
        return new GuardedInvocation(MH.asCollector(call, Object[].class, desc.getMethodType().parameterCount() - 1), IS_JSOBJECT_GUARD);
    }

    private static boolean isJSObject(Object self) {
        return jsObjectClass.isInstance(self);
    }

    private static Object get(MethodHandle fallback, Object jsobj, Object key) throws Throwable {
        if (key instanceof Integer) {
            return JSObjectHandles.JSOBJECT_GETSLOT.invokeExact(jsobj, (Integer)key);
        }
        if (key instanceof Number) {
            int index = BrowserJSObjectLinker.getIndex((Number)key);
            if (index > -1) {
                return JSObjectHandles.JSOBJECT_GETSLOT.invokeExact(jsobj, index);
            }
        } else if (JSType.isString(key)) {
            String name = key.toString();
            if (name.indexOf(40) != -1) {
                return fallback.invokeExact(jsobj, name);
            }
            return JSObjectHandles.JSOBJECT_GETMEMBER.invokeExact(jsobj, name);
        }
        return null;
    }

    private static void put(Object jsobj, Object key, Object value) throws Throwable {
        if (key instanceof Integer) {
            JSObjectHandles.JSOBJECT_SETSLOT.invokeExact(jsobj, (Integer)key, value);
        } else if (key instanceof Number) {
            JSObjectHandles.JSOBJECT_SETSLOT.invokeExact(jsobj, BrowserJSObjectLinker.getIndex((Number)key), value);
        } else if (JSType.isString(key)) {
            JSObjectHandles.JSOBJECT_SETMEMBER.invokeExact(jsobj, key.toString(), value);
        }
    }

    private static int getIndex(Number n) {
        double value = n.doubleValue();
        return JSType.isRepresentableAsInt(value) ? (int)value : -1;
    }

    private static MethodHandle findOwnMH_S(String name, Class<?> rtype, Class<?> ... types) {
        return MH.findStatic(MethodHandles.lookup(), BrowserJSObjectLinker.class, name, MH.type(rtype, types));
    }

    static {
        MH = MethodHandleFactory.getFunctionality();
        IS_JSOBJECT_GUARD = BrowserJSObjectLinker.findOwnMH_S("isJSObject", Boolean.TYPE, Object.class);
        JSOBJECTLINKER_GET = BrowserJSObjectLinker.findOwnMH_S("get", Object.class, MethodHandle.class, Object.class, Object.class);
        JSOBJECTLINKER_PUT = BrowserJSObjectLinker.findOwnMH_S("put", Void.TYPE, Object.class, Object.class, Object.class);
    }

    static class JSObjectHandles {
        static final MethodHandle JSOBJECT_GETMEMBER = JSObjectHandles.findJSObjectMH_V("getMember", Object.class, String.class).asType(BrowserJSObjectLinker.access$000().type(Object.class, Object.class, String.class));
        static final MethodHandle JSOBJECT_GETSLOT = JSObjectHandles.findJSObjectMH_V("getSlot", Object.class, Integer.TYPE).asType(BrowserJSObjectLinker.access$000().type(Object.class, Object.class, Integer.TYPE));
        static final MethodHandle JSOBJECT_SETMEMBER = JSObjectHandles.findJSObjectMH_V("setMember", Void.TYPE, String.class, Object.class).asType(BrowserJSObjectLinker.access$000().type(Void.TYPE, Object.class, String.class, Object.class));
        static final MethodHandle JSOBJECT_SETSLOT = JSObjectHandles.findJSObjectMH_V("setSlot", Void.TYPE, Integer.TYPE, Object.class).asType(BrowserJSObjectLinker.access$000().type(Void.TYPE, Object.class, Integer.TYPE, Object.class));
        static final MethodHandle JSOBJECT_CALL = JSObjectHandles.findJSObjectMH_V("call", Object.class, String.class, Object[].class).asType(BrowserJSObjectLinker.access$000().type(Object.class, Object.class, String.class, Object[].class));

        JSObjectHandles() {
        }

        private static MethodHandle findJSObjectMH_V(String name, Class<?> rtype, Class<?> ... types) {
            BrowserJSObjectLinker.checkJSObjectClass();
            return MH.findVirtual(MethodHandles.publicLookup(), jsObjectClass, name, MH.type(rtype, types));
        }
    }
}

