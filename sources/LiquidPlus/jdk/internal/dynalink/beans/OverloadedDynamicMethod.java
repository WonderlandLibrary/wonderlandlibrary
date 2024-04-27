/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.beans.ApplicableOverloadedMethods;
import jdk.internal.dynalink.beans.DynamicMethod;
import jdk.internal.dynalink.beans.OverloadedMethod;
import jdk.internal.dynalink.beans.SingleDynamicMethod;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.support.TypeUtilities;

class OverloadedDynamicMethod
extends DynamicMethod {
    private final LinkedList<SingleDynamicMethod> methods;
    private final ClassLoader classLoader;

    OverloadedDynamicMethod(Class<?> clazz, String name) {
        this(new LinkedList<SingleDynamicMethod>(), clazz.getClassLoader(), OverloadedDynamicMethod.getClassAndMethodName(clazz, name));
    }

    private OverloadedDynamicMethod(LinkedList<SingleDynamicMethod> methods, ClassLoader classLoader, String name) {
        super(name);
        this.methods = methods;
        this.classLoader = classLoader;
    }

    @Override
    SingleDynamicMethod getMethodForExactParamTypes(String paramTypes) {
        LinkedList<SingleDynamicMethod> matchingMethods = new LinkedList<SingleDynamicMethod>();
        for (SingleDynamicMethod method : this.methods) {
            SingleDynamicMethod matchingMethod = method.getMethodForExactParamTypes(paramTypes);
            if (matchingMethod == null) continue;
            matchingMethods.add(matchingMethod);
        }
        switch (matchingMethods.size()) {
            case 0: {
                return null;
            }
            case 1: {
                return (SingleDynamicMethod)matchingMethods.getFirst();
            }
        }
        throw new BootstrapMethodError("Can't choose among " + matchingMethods + " for argument types " + paramTypes + " for method " + this.getName());
    }

    @Override
    public MethodHandle getInvocation(CallSiteDescriptor callSiteDescriptor, LinkerServices linkerServices) {
        MethodType callSiteType = callSiteDescriptor.getMethodType();
        ApplicableOverloadedMethods subtypingApplicables = this.getApplicables(callSiteType, ApplicableOverloadedMethods.APPLICABLE_BY_SUBTYPING);
        ApplicableOverloadedMethods methodInvocationApplicables = this.getApplicables(callSiteType, ApplicableOverloadedMethods.APPLICABLE_BY_METHOD_INVOCATION_CONVERSION);
        ApplicableOverloadedMethods variableArityApplicables = this.getApplicables(callSiteType, ApplicableOverloadedMethods.APPLICABLE_BY_VARIABLE_ARITY);
        List<SingleDynamicMethod> maximallySpecifics = subtypingApplicables.findMaximallySpecificMethods();
        if (maximallySpecifics.isEmpty() && (maximallySpecifics = methodInvocationApplicables.findMaximallySpecificMethods()).isEmpty()) {
            maximallySpecifics = variableArityApplicables.findMaximallySpecificMethods();
        }
        List invokables = (List)this.methods.clone();
        invokables.removeAll(subtypingApplicables.getMethods());
        invokables.removeAll(methodInvocationApplicables.getMethods());
        invokables.removeAll(variableArityApplicables.getMethods());
        Iterator it = invokables.iterator();
        while (it.hasNext()) {
            SingleDynamicMethod m = (SingleDynamicMethod)it.next();
            if (OverloadedDynamicMethod.isApplicableDynamically(linkerServices, callSiteType, m)) continue;
            it.remove();
        }
        if (invokables.isEmpty() && maximallySpecifics.size() > 1) {
            throw new BootstrapMethodError("Can't choose among " + maximallySpecifics + " for argument types " + callSiteType);
        }
        invokables.addAll(maximallySpecifics);
        switch (invokables.size()) {
            case 0: {
                return null;
            }
            case 1: {
                return ((SingleDynamicMethod)invokables.iterator().next()).getInvocation(callSiteDescriptor, linkerServices);
            }
        }
        ArrayList<MethodHandle> methodHandles = new ArrayList<MethodHandle>(invokables.size());
        MethodHandles.Lookup lookup = callSiteDescriptor.getLookup();
        for (SingleDynamicMethod method : invokables) {
            methodHandles.add(method.getTarget(lookup));
        }
        return new OverloadedMethod(methodHandles, this, callSiteType, linkerServices).getInvoker();
    }

    @Override
    public boolean contains(SingleDynamicMethod m) {
        for (SingleDynamicMethod method : this.methods) {
            if (!method.contains(m)) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isConstructor() {
        assert (!this.methods.isEmpty());
        return this.methods.getFirst().isConstructor();
    }

    @Override
    public String toString() {
        ArrayList<String> names = new ArrayList<String>(this.methods.size());
        int len = 0;
        for (SingleDynamicMethod m : this.methods) {
            String name = m.getName();
            len += name.length();
            names.add(name);
        }
        Collator collator = Collator.getInstance();
        collator.setStrength(1);
        Collections.sort(names, collator);
        String className = this.getClass().getName();
        int totalLength = className.length() + len + 2 * names.size() + 3;
        StringBuilder b = new StringBuilder(totalLength);
        b.append('[').append(className).append('\n');
        for (String name : names) {
            b.append(' ').append(name).append('\n');
        }
        b.append(']');
        assert (b.length() == totalLength);
        return b.toString();
    }

    ClassLoader getClassLoader() {
        return this.classLoader;
    }

    private static boolean isApplicableDynamically(LinkerServices linkerServices, MethodType callSiteType, SingleDynamicMethod m) {
        MethodType methodType = m.getMethodType();
        boolean varArgs = m.isVarArgs();
        int fixedArgLen = methodType.parameterCount() - (varArgs ? 1 : 0);
        int callSiteArgLen = callSiteType.parameterCount();
        if (varArgs ? callSiteArgLen < fixedArgLen : callSiteArgLen != fixedArgLen) {
            return false;
        }
        for (int i = 1; i < fixedArgLen; ++i) {
            if (OverloadedDynamicMethod.isApplicableDynamically(linkerServices, callSiteType.parameterType(i), methodType.parameterType(i))) continue;
            return false;
        }
        if (!varArgs) {
            return true;
        }
        Class<?> varArgArrayType = methodType.parameterType(fixedArgLen);
        Class<?> varArgType = varArgArrayType.getComponentType();
        if (fixedArgLen == callSiteArgLen - 1) {
            Class<?> callSiteArgType = callSiteType.parameterType(fixedArgLen);
            return OverloadedDynamicMethod.isApplicableDynamically(linkerServices, callSiteArgType, varArgArrayType) || OverloadedDynamicMethod.isApplicableDynamically(linkerServices, callSiteArgType, varArgType);
        }
        for (int i = fixedArgLen; i < callSiteArgLen; ++i) {
            if (OverloadedDynamicMethod.isApplicableDynamically(linkerServices, callSiteType.parameterType(i), varArgType)) continue;
            return false;
        }
        return true;
    }

    private static boolean isApplicableDynamically(LinkerServices linkerServices, Class<?> callSiteType, Class<?> methodType) {
        return TypeUtilities.isPotentiallyConvertible(callSiteType, methodType) || linkerServices.canConvert(callSiteType, methodType);
    }

    private ApplicableOverloadedMethods getApplicables(MethodType callSiteType, ApplicableOverloadedMethods.ApplicabilityTest test) {
        return new ApplicableOverloadedMethods(this.methods, callSiteType, test);
    }

    public void addMethod(SingleDynamicMethod method) {
        assert (this.constructorFlagConsistent(method));
        this.methods.add(method);
    }

    private boolean constructorFlagConsistent(SingleDynamicMethod method) {
        return this.methods.isEmpty() ? true : this.methods.getFirst().isConstructor() == method.isConstructor();
    }
}

