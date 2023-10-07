/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.TypeDescriptor;
import java.lang.reflect.Array;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.beans.DynamicMethod;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.support.Guards;
import jdk.internal.dynalink.support.Lookup;

abstract class SingleDynamicMethod
extends DynamicMethod {
    private static final MethodHandle CAN_CONVERT_TO = Lookup.findOwnStatic(MethodHandles.lookup(), "canConvertTo", Boolean.TYPE, LinkerServices.class, Class.class, Object.class);

    SingleDynamicMethod(String name) {
        super(name);
    }

    abstract boolean isVarArgs();

    abstract MethodType getMethodType();

    abstract MethodHandle getTarget(MethodHandles.Lookup var1);

    @Override
    MethodHandle getInvocation(CallSiteDescriptor callSiteDescriptor, LinkerServices linkerServices) {
        return SingleDynamicMethod.getInvocation(this.getTarget(callSiteDescriptor.getLookup()), callSiteDescriptor.getMethodType(), linkerServices);
    }

    @Override
    SingleDynamicMethod getMethodForExactParamTypes(String paramTypes) {
        return SingleDynamicMethod.typeMatchesDescription(paramTypes, this.getMethodType()) ? this : null;
    }

    @Override
    boolean contains(SingleDynamicMethod method) {
        return this.getMethodType().parameterList().equals(method.getMethodType().parameterList());
    }

    static String getMethodNameWithSignature(MethodType type, String methodName, boolean withReturnType) {
        String typeStr = type.toString();
        int retTypeIndex = typeStr.lastIndexOf(41) + 1;
        int secondParamIndex = typeStr.indexOf(44) + 1;
        if (secondParamIndex == 0) {
            secondParamIndex = retTypeIndex - 1;
        }
        StringBuilder b = new StringBuilder();
        if (withReturnType) {
            b.append(typeStr, retTypeIndex, typeStr.length()).append(' ');
        }
        return b.append(methodName).append('(').append(typeStr, secondParamIndex, retTypeIndex).toString();
    }

    static MethodHandle getInvocation(MethodHandle target, MethodType callSiteType, LinkerServices linkerServices) {
        MethodHandle filteredTarget = linkerServices.filterInternalObjects(target);
        MethodType methodType = filteredTarget.type();
        int paramsLen = methodType.parameterCount();
        boolean varArgs = target.isVarargsCollector();
        MethodHandle fixTarget = varArgs ? filteredTarget.asFixedArity() : filteredTarget;
        int fixParamsLen = varArgs ? paramsLen - 1 : paramsLen;
        int argsLen = callSiteType.parameterCount();
        if (argsLen < fixParamsLen) {
            return null;
        }
        if (argsLen == fixParamsLen) {
            MethodHandle matchedMethod = varArgs ? MethodHandles.insertArguments(fixTarget, fixParamsLen, Array.newInstance(((Class)methodType.parameterType(fixParamsLen)).getComponentType(), 0)) : fixTarget;
            return SingleDynamicMethod.createConvertingInvocation(matchedMethod, linkerServices, callSiteType);
        }
        if (!varArgs) {
            return null;
        }
        TypeDescriptor.OfField varArgType = methodType.parameterType(fixParamsLen);
        if (argsLen == paramsLen) {
            TypeDescriptor.OfField callSiteLastArgType = callSiteType.parameterType(fixParamsLen);
            if (((Class)varArgType).isAssignableFrom((Class<?>)callSiteLastArgType)) {
                return SingleDynamicMethod.createConvertingInvocation(filteredTarget, linkerServices, callSiteType).asVarargsCollector((Class<?>)callSiteLastArgType);
            }
            MethodHandle varArgCollectingInvocation = SingleDynamicMethod.createConvertingInvocation(SingleDynamicMethod.collectArguments(fixTarget, argsLen), linkerServices, callSiteType);
            boolean isAssignableFromArray = ((Class)callSiteLastArgType).isAssignableFrom((Class<?>)varArgType);
            boolean isCustomConvertible = linkerServices.canConvert((Class<?>)callSiteLastArgType, (Class<?>)varArgType);
            if (!isAssignableFromArray && !isCustomConvertible) {
                return varArgCollectingInvocation;
            }
            MethodHandle arrayConvertingInvocation = SingleDynamicMethod.createConvertingInvocation(MethodHandles.filterArguments(fixTarget, fixParamsLen, linkerServices.getTypeConverter((Class<?>)callSiteLastArgType, (Class<?>)varArgType)), linkerServices, callSiteType);
            MethodHandle canConvertArgToArray = MethodHandles.insertArguments(CAN_CONVERT_TO, 0, linkerServices, varArgType);
            MethodHandle canConvertLastArgToArray = MethodHandles.dropArguments(canConvertArgToArray, 0, MethodType.genericMethodType(fixParamsLen).parameterList()).asType(callSiteType.changeReturnType(Boolean.TYPE));
            MethodHandle convertToArrayWhenPossible = MethodHandles.guardWithTest(canConvertLastArgToArray, arrayConvertingInvocation, varArgCollectingInvocation);
            if (isAssignableFromArray) {
                return MethodHandles.guardWithTest(Guards.isInstance(varArgType, fixParamsLen, callSiteType), SingleDynamicMethod.createConvertingInvocation(fixTarget, linkerServices, callSiteType), isCustomConvertible ? convertToArrayWhenPossible : varArgCollectingInvocation);
            }
            assert (isCustomConvertible);
            return convertToArrayWhenPossible;
        }
        return SingleDynamicMethod.createConvertingInvocation(SingleDynamicMethod.collectArguments(fixTarget, argsLen), linkerServices, callSiteType);
    }

    private static boolean canConvertTo(LinkerServices linkerServices, Class<?> to, Object obj) {
        return obj == null ? false : linkerServices.canConvert(obj.getClass(), to);
    }

    static MethodHandle collectArguments(MethodHandle target, int parameterCount) {
        MethodType methodType = target.type();
        int fixParamsLen = methodType.parameterCount() - 1;
        TypeDescriptor.OfField arrayType = methodType.parameterType(fixParamsLen);
        return target.asCollector((Class<?>)arrayType, parameterCount - fixParamsLen);
    }

    private static MethodHandle createConvertingInvocation(MethodHandle sizedMethod, LinkerServices linkerServices, MethodType callSiteType) {
        return linkerServices.asTypeLosslessReturn(sizedMethod, callSiteType);
    }

    private static boolean typeMatchesDescription(String paramTypes, MethodType type) {
        StringTokenizer tok = new StringTokenizer(paramTypes, ", ");
        for (int i = 1; i < type.parameterCount(); ++i) {
            if (tok.hasMoreTokens() && SingleDynamicMethod.typeNameMatches(tok.nextToken(), type.parameterType(i))) continue;
            return false;
        }
        return !tok.hasMoreTokens();
    }

    private static boolean typeNameMatches(String typeName, Class<?> type) {
        return typeName.equals(typeName.indexOf(46) == -1 ? type.getSimpleName() : type.getCanonicalName());
    }
}

