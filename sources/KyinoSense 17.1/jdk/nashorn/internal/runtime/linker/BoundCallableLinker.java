/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.TypeDescriptor;
import java.util.Arrays;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;
import jdk.internal.dynalink.support.Guards;
import jdk.nashorn.internal.runtime.linker.BoundCallable;

final class BoundCallableLinker
implements TypeBasedGuardingDynamicLinker {
    BoundCallableLinker() {
    }

    @Override
    public boolean canLinkType(Class<?> type) {
        return type == BoundCallable.class;
    }

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) throws Exception {
        int firstArgIndex;
        boolean isCall;
        Object objBoundCallable = linkRequest.getReceiver();
        if (!(objBoundCallable instanceof BoundCallable)) {
            return null;
        }
        CallSiteDescriptor descriptor = linkRequest.getCallSiteDescriptor();
        if (descriptor.getNameTokenCount() < 2 || !"dyn".equals(descriptor.getNameToken(0))) {
            return null;
        }
        String operation = descriptor.getNameToken(1);
        if ("new".equals(operation)) {
            isCall = false;
        } else if ("call".equals(operation)) {
            isCall = true;
        } else {
            return null;
        }
        BoundCallable boundCallable = (BoundCallable)objBoundCallable;
        Object callable = boundCallable.getCallable();
        Object boundThis = boundCallable.getBoundThis();
        Object[] args2 = linkRequest.getArguments();
        Object[] boundArgs = boundCallable.getBoundArgs();
        int argsLen = args2.length;
        int boundArgsLen = boundArgs.length;
        Object[] newArgs = new Object[argsLen + boundArgsLen];
        newArgs[0] = callable;
        if (isCall) {
            newArgs[1] = boundThis;
            firstArgIndex = 2;
        } else {
            firstArgIndex = 1;
        }
        System.arraycopy(boundArgs, 0, newArgs, firstArgIndex, boundArgsLen);
        System.arraycopy(args2, firstArgIndex, newArgs, firstArgIndex + boundArgsLen, argsLen - firstArgIndex);
        MethodType type = descriptor.getMethodType();
        MethodType newMethodType = descriptor.getMethodType().changeParameterType(0, callable.getClass());
        if (isCall) {
            newMethodType = newMethodType.changeParameterType(1, boundThis == null ? Object.class : boundThis.getClass());
        }
        int i = boundArgs.length;
        while (i-- > 0) {
            newMethodType = newMethodType.insertParameterTypes(firstArgIndex, boundArgs[i] == null ? Object.class : boundArgs[i].getClass());
        }
        CallSiteDescriptor newDescriptor = descriptor.changeMethodType(newMethodType);
        GuardedInvocation inv = linkerServices.getGuardedInvocation(linkRequest.replaceArguments(newDescriptor, newArgs));
        if (inv == null) {
            return null;
        }
        MethodHandle boundHandle = MethodHandles.insertArguments(inv.getInvocation(), 0, Arrays.copyOf(newArgs, firstArgIndex + boundArgs.length));
        TypeDescriptor.OfField p0Type = type.parameterType(0);
        MethodHandle droppingHandle = isCall ? MethodHandles.dropArguments(boundHandle, 0, new Class[]{p0Type, type.parameterType(1)}) : MethodHandles.dropArguments(boundHandle, 0, new Class[]{p0Type});
        MethodHandle newGuard = Guards.getIdentityGuard(boundCallable);
        return inv.replaceMethods(droppingHandle, newGuard.asType(newGuard.type().changeParameterType(0, (Class<?>)p0Type)));
    }
}

