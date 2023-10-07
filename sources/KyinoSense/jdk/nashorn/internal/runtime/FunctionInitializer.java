/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.invoke.MethodType;
import java.util.Map;
import jdk.nashorn.internal.codegen.CompileUnit;
import jdk.nashorn.internal.codegen.FunctionSignature;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.FunctionNode;

public final class FunctionInitializer
implements Serializable {
    private final String className;
    private final MethodType methodType;
    private final int flags;
    private transient Map<Integer, Type> invalidatedProgramPoints;
    private transient Class<?> code;
    private static final long serialVersionUID = -5420835725902966692L;

    public FunctionInitializer(FunctionNode functionNode) {
        this(functionNode, null);
    }

    public FunctionInitializer(FunctionNode functionNode, Map<Integer, Type> invalidatedProgramPoints) {
        this.className = functionNode.getCompileUnit().getUnitClassName();
        this.methodType = new FunctionSignature(functionNode).getMethodType();
        this.flags = functionNode.getFlags();
        this.invalidatedProgramPoints = invalidatedProgramPoints;
        CompileUnit cu = functionNode.getCompileUnit();
        if (cu != null) {
            this.code = cu.getCode();
        }
        assert (this.className != null);
    }

    public String getClassName() {
        return this.className;
    }

    public MethodType getMethodType() {
        return this.methodType;
    }

    public int getFlags() {
        return this.flags;
    }

    public Class<?> getCode() {
        return this.code;
    }

    void setCode(Class<?> code) {
        if (this.code != null) {
            throw new IllegalStateException("code already set");
        }
        assert (this.className.equals(code.getTypeName().replace('.', '/'))) : "unexpected class name";
        this.code = code;
    }

    public Map<Integer, Type> getInvalidatedProgramPoints() {
        return this.invalidatedProgramPoints;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        Type.writeTypeMap(this.invalidatedProgramPoints, out);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.invalidatedProgramPoints = Type.readTypeMap(in);
    }
}

