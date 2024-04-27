/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodType;
import java.util.LinkedList;
import java.util.List;
import jdk.internal.dynalink.beans.MaximallySpecific;
import jdk.internal.dynalink.beans.SingleDynamicMethod;
import jdk.internal.dynalink.support.TypeUtilities;

class ApplicableOverloadedMethods {
    private final List<SingleDynamicMethod> methods = new LinkedList<SingleDynamicMethod>();
    private final boolean varArgs;
    static final ApplicabilityTest APPLICABLE_BY_SUBTYPING = new ApplicabilityTest(){

        @Override
        boolean isApplicable(MethodType callSiteType, SingleDynamicMethod method) {
            MethodType methodType = method.getMethodType();
            int methodArity = methodType.parameterCount();
            if (methodArity != callSiteType.parameterCount()) {
                return false;
            }
            for (int i = 1; i < methodArity; ++i) {
                if (TypeUtilities.isSubtype(callSiteType.parameterType(i), methodType.parameterType(i))) continue;
                return false;
            }
            return true;
        }
    };
    static final ApplicabilityTest APPLICABLE_BY_METHOD_INVOCATION_CONVERSION = new ApplicabilityTest(){

        @Override
        boolean isApplicable(MethodType callSiteType, SingleDynamicMethod method) {
            MethodType methodType = method.getMethodType();
            int methodArity = methodType.parameterCount();
            if (methodArity != callSiteType.parameterCount()) {
                return false;
            }
            for (int i = 1; i < methodArity; ++i) {
                if (TypeUtilities.isMethodInvocationConvertible(callSiteType.parameterType(i), methodType.parameterType(i))) continue;
                return false;
            }
            return true;
        }
    };
    static final ApplicabilityTest APPLICABLE_BY_VARIABLE_ARITY = new ApplicabilityTest(){

        @Override
        boolean isApplicable(MethodType callSiteType, SingleDynamicMethod method) {
            int callSiteArity;
            if (!method.isVarArgs()) {
                return false;
            }
            MethodType methodType = method.getMethodType();
            int methodArity = methodType.parameterCount();
            int fixArity = methodArity - 1;
            if (fixArity > (callSiteArity = callSiteType.parameterCount())) {
                return false;
            }
            for (int i = 1; i < fixArity; ++i) {
                if (TypeUtilities.isMethodInvocationConvertible(callSiteType.parameterType(i), methodType.parameterType(i))) continue;
                return false;
            }
            Class<?> varArgType = methodType.parameterType(fixArity).getComponentType();
            for (int i = fixArity; i < callSiteArity; ++i) {
                if (TypeUtilities.isMethodInvocationConvertible(callSiteType.parameterType(i), varArgType)) continue;
                return false;
            }
            return true;
        }
    };

    ApplicableOverloadedMethods(List<SingleDynamicMethod> methods, MethodType callSiteType, ApplicabilityTest test) {
        for (SingleDynamicMethod m : methods) {
            if (!test.isApplicable(callSiteType, m)) continue;
            this.methods.add(m);
        }
        this.varArgs = test == APPLICABLE_BY_VARIABLE_ARITY;
    }

    List<SingleDynamicMethod> getMethods() {
        return this.methods;
    }

    List<SingleDynamicMethod> findMaximallySpecificMethods() {
        return MaximallySpecific.getMaximallySpecificMethods(this.methods, this.varArgs);
    }

    static abstract class ApplicabilityTest {
        ApplicabilityTest() {
        }

        abstract boolean isApplicable(MethodType var1, SingleDynamicMethod var2);
    }
}

