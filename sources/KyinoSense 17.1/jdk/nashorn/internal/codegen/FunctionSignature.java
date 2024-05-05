/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.ScriptFunction;

public final class FunctionSignature {
    private final Type[] paramTypes;
    private final Type returnType;
    private final String descriptor;
    private final MethodType methodType;

    public FunctionSignature(boolean hasSelf, boolean hasCallee, Type retType, List<? extends Expression> args2) {
        this(hasSelf, hasCallee, retType, FunctionSignature.typeArray(args2));
    }

    public FunctionSignature(boolean hasSelf, boolean hasCallee, Type retType, int nArgs) {
        this(hasSelf, hasCallee, retType, FunctionSignature.objectArgs(nArgs));
    }

    private FunctionSignature(boolean hasSelf, boolean hasCallee, Type retType, Type ... argTypes) {
        boolean isVarArg;
        int count = 1;
        if (argTypes == null) {
            isVarArg = true;
        } else {
            isVarArg = argTypes.length > 250;
            int n = count = isVarArg ? 1 : argTypes.length;
        }
        if (hasCallee) {
            ++count;
        }
        if (hasSelf) {
            ++count;
        }
        this.paramTypes = new Type[count];
        int next = 0;
        if (hasCallee) {
            this.paramTypes[next++] = Type.typeFor(ScriptFunction.class);
        }
        if (hasSelf) {
            this.paramTypes[next++] = Type.OBJECT;
        }
        if (isVarArg) {
            this.paramTypes[next] = Type.OBJECT_ARRAY;
        } else if (argTypes != null) {
            int j = 0;
            while (next < count) {
                Type type = argTypes[j++];
                this.paramTypes[next++] = type.isObject() ? Type.OBJECT : type;
            }
        } else assert (false) : "isVarArgs cannot be false when argTypes are null";
        this.returnType = retType;
        this.descriptor = Type.getMethodDescriptor(this.returnType, this.paramTypes);
        ArrayList paramTypeList = new ArrayList();
        for (Type paramType : this.paramTypes) {
            paramTypeList.add(paramType.getTypeClass());
        }
        this.methodType = Lookup.MH.type(this.returnType.getTypeClass(), paramTypeList.toArray(new Class[this.paramTypes.length]));
    }

    public FunctionSignature(FunctionNode functionNode) {
        this(true, functionNode.needsCallee(), functionNode.getReturnType(), (List<? extends Expression>)(functionNode.isVarArg() && !functionNode.isProgram() ? null : functionNode.getParameters()));
    }

    private static Type[] typeArray(List<? extends Expression> args2) {
        if (args2 == null) {
            return null;
        }
        Type[] typeArray = new Type[args2.size()];
        int pos = 0;
        for (Expression expression : args2) {
            typeArray[pos++] = expression.getType();
        }
        return typeArray;
    }

    public String toString() {
        return this.descriptor;
    }

    public int size() {
        return this.paramTypes.length;
    }

    public Type[] getParamTypes() {
        return (Type[])this.paramTypes.clone();
    }

    public MethodType getMethodType() {
        return this.methodType;
    }

    public Type getReturnType() {
        return this.returnType;
    }

    private static Type[] objectArgs(int nArgs) {
        Type[] array = new Type[nArgs];
        for (int i = 0; i < nArgs; ++i) {
            array[i] = Type.OBJECT;
        }
        return array;
    }
}

