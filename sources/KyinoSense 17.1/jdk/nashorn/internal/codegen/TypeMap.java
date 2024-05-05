/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.lang.invoke.MethodType;
import java.util.Arrays;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.runtime.ScriptFunction;

public final class TypeMap {
    private final int functionNodeId;
    private final Type[] paramTypes;
    private final Type returnType;
    private final boolean needsCallee;

    public TypeMap(int functionNodeId, MethodType type, boolean needsCallee) {
        Type[] types = new Type[type.parameterCount()];
        int pos = 0;
        for (Class<?> p : type.parameterArray()) {
            types[pos++] = Type.typeFor(p);
        }
        this.functionNodeId = functionNodeId;
        this.paramTypes = types;
        this.returnType = Type.typeFor(type.returnType());
        this.needsCallee = needsCallee;
    }

    public Type[] getParameterTypes(int functionNodeId) {
        assert (this.functionNodeId == functionNodeId);
        return (Type[])this.paramTypes.clone();
    }

    MethodType getCallSiteType(FunctionNode functionNode) {
        assert (this.functionNodeId == functionNode.getId());
        Type[] types = this.paramTypes;
        MethodType mt = MethodType.methodType(this.returnType.getTypeClass());
        if (this.needsCallee) {
            mt = mt.appendParameterTypes(ScriptFunction.class);
        }
        mt = mt.appendParameterTypes(Object.class);
        for (Type type : types) {
            if (type == null) {
                return null;
            }
            mt = mt.appendParameterTypes(type.getTypeClass());
        }
        return mt;
    }

    public boolean needsCallee() {
        return this.needsCallee;
    }

    Type get(FunctionNode functionNode, int pos) {
        assert (this.functionNodeId == functionNode.getId());
        Object[] types = this.paramTypes;
        assert (types == null || pos < types.length) : "fn = " + functionNode.getId() + " types=" + Arrays.toString(types) + " || pos=" + pos + " >= length=" + types.length + " in " + this;
        if (types != null && pos < types.length) {
            return types[pos];
        }
        return null;
    }

    public String toString() {
        return this.toString("");
    }

    String toString(String prefix) {
        StringBuilder sb = new StringBuilder();
        int id = this.functionNodeId;
        sb.append(prefix).append('\t');
        sb.append("function ").append(id).append('\n');
        sb.append(prefix).append("\t\tparamTypes=");
        sb.append(Arrays.toString(this.paramTypes));
        sb.append('\n');
        sb.append(prefix).append("\t\treturnType=");
        Type ret = this.returnType;
        sb.append(ret == null ? "N/A" : ret);
        sb.append('\n');
        return sb.toString();
    }
}

