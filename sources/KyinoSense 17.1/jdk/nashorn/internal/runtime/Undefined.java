/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.internal.dynalink.support.Guards;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.DefaultPropertyAccess;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ECMAException;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;

public final class Undefined
extends DefaultPropertyAccess {
    private static final Undefined UNDEFINED = new Undefined();
    private static final Undefined EMPTY = new Undefined();
    private static final MethodHandle UNDEFINED_GUARD = Guards.getIdentityGuard(UNDEFINED);
    private static final MethodHandle GET_METHOD = Undefined.findOwnMH("get", Object.class, Object.class);
    private static final MethodHandle SET_METHOD = Lookup.MH.insertArguments(Undefined.findOwnMH("set", Void.TYPE, Object.class, Object.class, Integer.TYPE), 3, 2);

    private Undefined() {
    }

    public static Undefined getUndefined() {
        return UNDEFINED;
    }

    public static Undefined getEmpty() {
        return EMPTY;
    }

    public String getClassName() {
        return "Undefined";
    }

    public String toString() {
        return "undefined";
    }

    public static GuardedInvocation lookup(CallSiteDescriptor desc) {
        String operator;
        switch (operator = CallSiteDescriptorFactory.tokenizeOperators(desc).get(0)) {
            case "new": 
            case "call": {
                String name = NashornCallSiteDescriptor.getFunctionDescription(desc);
                String msg = name != null ? "not.a.function" : "cant.call.undefined";
                throw ECMAErrors.typeError(msg, name);
            }
            case "callMethod": {
                throw Undefined.lookupTypeError("cant.read.property.of.undefined", desc);
            }
            case "getProp": 
            case "getElem": 
            case "getMethod": {
                if (desc.getNameTokenCount() < 3) {
                    return Undefined.findGetIndexMethod(desc);
                }
                return Undefined.findGetMethod(desc);
            }
            case "setProp": 
            case "setElem": {
                if (desc.getNameTokenCount() < 3) {
                    return Undefined.findSetIndexMethod(desc);
                }
                return Undefined.findSetMethod(desc);
            }
        }
        return null;
    }

    private static ECMAException lookupTypeError(String msg, CallSiteDescriptor desc) {
        String name = desc.getNameToken(2);
        return ECMAErrors.typeError(msg, name != null && !name.isEmpty() ? name : null);
    }

    private static GuardedInvocation findGetMethod(CallSiteDescriptor desc) {
        return new GuardedInvocation(Lookup.MH.insertArguments(GET_METHOD, 1, desc.getNameToken(2)), UNDEFINED_GUARD).asType(desc);
    }

    private static GuardedInvocation findGetIndexMethod(CallSiteDescriptor desc) {
        return new GuardedInvocation(GET_METHOD, UNDEFINED_GUARD).asType(desc);
    }

    private static GuardedInvocation findSetMethod(CallSiteDescriptor desc) {
        return new GuardedInvocation(Lookup.MH.insertArguments(SET_METHOD, 1, desc.getNameToken(2)), UNDEFINED_GUARD).asType(desc);
    }

    private static GuardedInvocation findSetIndexMethod(CallSiteDescriptor desc) {
        return new GuardedInvocation(SET_METHOD, UNDEFINED_GUARD).asType(desc);
    }

    @Override
    public Object get(Object key) {
        throw ECMAErrors.typeError("cant.read.property.of.undefined", ScriptRuntime.safeToString(key));
    }

    @Override
    public void set(Object key, Object value, int flags) {
        throw ECMAErrors.typeError("cant.set.property.of.undefined", ScriptRuntime.safeToString(key));
    }

    @Override
    public boolean delete(Object key, boolean strict) {
        throw ECMAErrors.typeError("cant.delete.property.of.undefined", ScriptRuntime.safeToString(key));
    }

    @Override
    public boolean has(Object key) {
        return false;
    }

    @Override
    public boolean hasOwnProperty(Object key) {
        return false;
    }

    private static MethodHandle findOwnMH(String name, Class<?> rtype, Class<?> ... types) {
        return Lookup.MH.findVirtual(MethodHandles.lookup(), Undefined.class, name, Lookup.MH.type(rtype, types));
    }
}

