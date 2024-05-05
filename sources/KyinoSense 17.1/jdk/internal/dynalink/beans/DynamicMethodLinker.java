/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.beans.DynamicMethod;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.internal.dynalink.support.Guards;

class DynamicMethodLinker
implements TypeBasedGuardingDynamicLinker {
    DynamicMethodLinker() {
    }

    @Override
    public boolean canLinkType(Class<?> type) {
        return DynamicMethod.class.isAssignableFrom(type);
    }

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) {
        MethodHandle invocation;
        Object receiver = linkRequest.getReceiver();
        if (!(receiver instanceof DynamicMethod)) {
            return null;
        }
        CallSiteDescriptor desc = linkRequest.getCallSiteDescriptor();
        if (desc.getNameTokenCount() != 2 && desc.getNameToken(0) != "dyn") {
            return null;
        }
        String operator = desc.getNameToken(1);
        DynamicMethod dynMethod = (DynamicMethod)receiver;
        boolean constructor = dynMethod.isConstructor();
        if (operator == "call" && !constructor) {
            invocation = dynMethod.getInvocation(CallSiteDescriptorFactory.dropParameterTypes(desc, 0, 1), linkerServices);
        } else if (operator == "new" && constructor) {
            MethodHandle ctorInvocation = dynMethod.getInvocation(desc, linkerServices);
            if (ctorInvocation == null) {
                return null;
            }
            invocation = MethodHandles.insertArguments(ctorInvocation, 0, new Object[]{null});
        } else {
            return null;
        }
        if (invocation != null) {
            return new GuardedInvocation(MethodHandles.dropArguments(invocation, 0, new Class[]{desc.getMethodType().parameterType(0)}), Guards.getIdentityGuard(receiver));
        }
        return null;
    }
}

