/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.JSType;

public final class UnwarrantedOptimismException
extends RuntimeException {
    public static final int INVALID_PROGRAM_POINT = -1;
    public static final int FIRST_PROGRAM_POINT = 1;
    private Object returnValue;
    private final int programPoint;
    private final Type returnType;

    public UnwarrantedOptimismException(Object returnValue, int programPoint) {
        this(returnValue, programPoint, UnwarrantedOptimismException.getReturnType(returnValue));
    }

    public static boolean isValid(int programPoint) {
        assert (programPoint >= -1);
        return programPoint != -1;
    }

    private static Type getReturnType(Object v) {
        if (v instanceof Double) {
            return Type.NUMBER;
        }
        assert (!(v instanceof Integer)) : v + " is an int";
        return Type.OBJECT;
    }

    public UnwarrantedOptimismException(Object returnValue, int programPoint, Type returnType) {
        super("", null, false, Context.DEBUG);
        assert (returnType != Type.OBJECT || returnValue == null || !Type.typeFor(returnValue.getClass()).isNumeric());
        assert (returnType != Type.INT);
        this.returnValue = returnValue;
        this.programPoint = programPoint;
        this.returnType = returnType;
    }

    public static UnwarrantedOptimismException createNarrowest(Object returnValue, int programPoint) {
        if (returnValue instanceof Float || returnValue instanceof Long && JSType.isRepresentableAsDouble((Long)returnValue)) {
            return new UnwarrantedOptimismException(((Number)returnValue).doubleValue(), programPoint, Type.NUMBER);
        }
        return new UnwarrantedOptimismException(returnValue, programPoint);
    }

    public Object getReturnValueDestructive() {
        Object retval = this.returnValue;
        this.returnValue = null;
        return retval;
    }

    Object getReturnValueNonDestructive() {
        return this.returnValue;
    }

    public Type getReturnType() {
        return this.returnType;
    }

    public boolean hasInvalidProgramPoint() {
        return this.programPoint == -1;
    }

    public int getProgramPoint() {
        return this.programPoint;
    }

    public boolean hasPrimitiveReturnValue() {
        return this.returnValue instanceof Number || this.returnValue instanceof Boolean;
    }

    @Override
    public String getMessage() {
        return "UNWARRANTED OPTIMISM: [returnValue=" + this.returnValue + " (class=" + (this.returnValue == null ? "null" : this.returnValue.getClass().getSimpleName()) + (this.hasInvalidProgramPoint() ? " <invalid program point>" : " @ program point #" + this.programPoint) + ")]";
    }

    private void writeObject(ObjectOutputStream out) throws NotSerializableException {
        throw new NotSerializableException(this.getClass().getName());
    }

    private void readObject(ObjectInputStream in) throws NotSerializableException {
        throw new NotSerializableException(this.getClass().getName());
    }
}

