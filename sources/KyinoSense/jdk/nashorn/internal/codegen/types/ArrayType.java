/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen.types;

import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.nashorn.internal.codegen.types.BytecodeArrayOps;
import jdk.nashorn.internal.codegen.types.ObjectType;
import jdk.nashorn.internal.codegen.types.Type;

public class ArrayType
extends ObjectType
implements BytecodeArrayOps {
    private static final long serialVersionUID = 1L;

    protected ArrayType(Class<?> clazz) {
        super(clazz);
    }

    public Type getElementType() {
        return Type.typeFor(this.getTypeClass().getComponentType());
    }

    @Override
    public void astore(MethodVisitor method) {
        method.visitInsn(83);
    }

    @Override
    public Type aload(MethodVisitor method) {
        method.visitInsn(50);
        return this.getElementType();
    }

    @Override
    public Type arraylength(MethodVisitor method) {
        method.visitInsn(190);
        return INT;
    }

    @Override
    public Type newarray(MethodVisitor method) {
        method.visitTypeInsn(189, this.getElementType().getInternalName());
        return this;
    }

    @Override
    public Type newarray(MethodVisitor method, int dims) {
        method.visitMultiANewArrayInsn(this.getInternalName(), dims);
        return this;
    }

    @Override
    public Type load(MethodVisitor method, int slot) {
        assert (slot != -1);
        method.visitVarInsn(25, slot);
        return this;
    }

    @Override
    public String toString() {
        return "array<elementType=" + this.getElementType().getTypeClass().getSimpleName() + '>';
    }

    @Override
    public Type convert(MethodVisitor method, Type to) {
        assert (to.isObject());
        assert (!to.isArray() || ((ArrayType)to).getElementType() == this.getElementType());
        return to;
    }
}

