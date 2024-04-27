/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen.types;

import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.types.NumericType;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.JSType;

class NumberType
extends NumericType {
    private static final long serialVersionUID = 1L;
    private static final CompilerConstants.Call VALUE_OF = CompilerConstants.staticCallNoLookup(Double.class, "valueOf", Double.class, Double.TYPE);

    protected NumberType() {
        super("double", Double.TYPE, 4, 2);
    }

    @Override
    public Type nextWider() {
        return OBJECT;
    }

    @Override
    public Class<?> getBoxedType() {
        return Double.class;
    }

    @Override
    public char getBytecodeStackType() {
        return 'D';
    }

    @Override
    public Type cmp(MethodVisitor method, boolean isCmpG) {
        method.visitInsn(isCmpG ? 152 : 151);
        return INT;
    }

    @Override
    public Type load(MethodVisitor method, int slot) {
        assert (slot != -1);
        method.visitVarInsn(24, slot);
        return NUMBER;
    }

    @Override
    public void store(MethodVisitor method, int slot) {
        assert (slot != -1);
        method.visitVarInsn(57, slot);
    }

    @Override
    public Type loadUndefined(MethodVisitor method) {
        method.visitLdcInsn(Double.NaN);
        return NUMBER;
    }

    @Override
    public Type loadForcedInitializer(MethodVisitor method) {
        method.visitInsn(14);
        return NUMBER;
    }

    @Override
    public Type ldc(MethodVisitor method, Object c) {
        assert (c instanceof Double);
        double value = (Double)c;
        if (Double.doubleToLongBits(value) == 0L) {
            method.visitInsn(14);
        } else if (value == 1.0) {
            method.visitInsn(15);
        } else {
            method.visitLdcInsn(value);
        }
        return NUMBER;
    }

    @Override
    public Type convert(MethodVisitor method, Type to) {
        if (this.isEquivalentTo(to)) {
            return null;
        }
        if (to.isInteger()) {
            NumberType.invokestatic(method, JSType.TO_INT32_D);
        } else if (to.isLong()) {
            NumberType.invokestatic(method, JSType.TO_LONG_D);
        } else if (to.isBoolean()) {
            NumberType.invokestatic(method, JSType.TO_BOOLEAN_D);
        } else if (to.isString()) {
            NumberType.invokestatic(method, JSType.TO_STRING_D);
        } else if (to.isObject()) {
            NumberType.invokestatic(method, VALUE_OF);
        } else {
            throw new UnsupportedOperationException("Illegal conversion " + this + " -> " + to);
        }
        return to;
    }

    @Override
    public Type add(MethodVisitor method, int programPoint) {
        method.visitInsn(99);
        return NUMBER;
    }

    @Override
    public Type sub(MethodVisitor method, int programPoint) {
        method.visitInsn(103);
        return NUMBER;
    }

    @Override
    public Type mul(MethodVisitor method, int programPoint) {
        method.visitInsn(107);
        return NUMBER;
    }

    @Override
    public Type div(MethodVisitor method, int programPoint) {
        method.visitInsn(111);
        return NUMBER;
    }

    @Override
    public Type rem(MethodVisitor method, int programPoint) {
        method.visitInsn(115);
        return NUMBER;
    }

    @Override
    public Type neg(MethodVisitor method, int programPoint) {
        method.visitInsn(119);
        return NUMBER;
    }

    @Override
    public void _return(MethodVisitor method) {
        method.visitInsn(175);
    }
}

