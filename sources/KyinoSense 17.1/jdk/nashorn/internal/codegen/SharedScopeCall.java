/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.util.Arrays;
import java.util.EnumSet;
import jdk.nashorn.internal.codegen.ClassEmitter;
import jdk.nashorn.internal.codegen.CodeGenerator;
import jdk.nashorn.internal.codegen.CompileUnit;
import jdk.nashorn.internal.codegen.Label;
import jdk.nashorn.internal.codegen.MethodEmitter;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.runtime.ScriptObject;

class SharedScopeCall {
    public static final int FAST_SCOPE_CALL_THRESHOLD = 4;
    public static final int SLOW_SCOPE_CALL_THRESHOLD = 500;
    public static final int FAST_SCOPE_GET_THRESHOLD = 200;
    final Type valueType;
    final Symbol symbol;
    final Type returnType;
    final Type[] paramTypes;
    final int flags;
    final boolean isCall;
    private CompileUnit compileUnit;
    private String methodName;
    private String staticSignature;

    SharedScopeCall(Symbol symbol, Type valueType, Type returnType, Type[] paramTypes, int flags) {
        this.symbol = symbol;
        this.valueType = valueType;
        this.returnType = returnType;
        this.paramTypes = paramTypes;
        assert ((flags & 8) == 0);
        this.flags = flags;
        this.isCall = paramTypes != null;
    }

    public int hashCode() {
        return this.symbol.hashCode() ^ this.returnType.hashCode() ^ Arrays.hashCode(this.paramTypes) ^ this.flags;
    }

    public boolean equals(Object obj) {
        if (obj instanceof SharedScopeCall) {
            SharedScopeCall c = (SharedScopeCall)obj;
            return this.symbol.equals(c.symbol) && this.flags == c.flags && this.returnType.equals(c.returnType) && Arrays.equals(this.paramTypes, c.paramTypes);
        }
        return false;
    }

    protected void setClassAndName(CompileUnit compileUnit, String methodName) {
        this.compileUnit = compileUnit;
        this.methodName = methodName;
    }

    public MethodEmitter generateInvoke(MethodEmitter method) {
        return method.invokestatic(this.compileUnit.getUnitClassName(), this.methodName, this.getStaticSignature());
    }

    protected void generateScopeCall() {
        ClassEmitter classEmitter = this.compileUnit.getClassEmitter();
        EnumSet<ClassEmitter.Flag> methodFlags = EnumSet.of(ClassEmitter.Flag.STATIC);
        MethodEmitter method = classEmitter.method(methodFlags, this.methodName, this.getStaticSignature());
        method.begin();
        Label parentLoopStart = new Label("parent_loop_start");
        Label parentLoopDone = new Label("parent_loop_done");
        method.load(Type.OBJECT, 0);
        method.label(parentLoopStart);
        method.load(Type.INT, 1);
        method.iinc(1, -1);
        method.ifle(parentLoopDone);
        method.invoke(ScriptObject.GET_PROTO);
        method._goto(parentLoopStart);
        method.label(parentLoopDone);
        assert (!this.isCall || this.valueType.isObject());
        method.dynamicGet(this.valueType, this.symbol.getName(), this.isCall ? CodeGenerator.nonOptimisticFlags(this.flags) : this.flags, this.isCall, false);
        if (this.isCall) {
            method.convert(Type.OBJECT);
            method.loadUndefined(Type.OBJECT);
            int slot = 2;
            for (Type type : this.paramTypes) {
                method.load(type, slot);
                slot += type.getSlots();
            }
            method.dynamicCall(this.returnType, 2 + this.paramTypes.length, this.flags, this.symbol.getName());
        }
        method._return(this.returnType);
        method.end();
    }

    private String getStaticSignature() {
        if (this.staticSignature == null) {
            if (this.paramTypes == null) {
                this.staticSignature = Type.getMethodDescriptor(this.returnType, Type.typeFor(ScriptObject.class), Type.INT);
            } else {
                Type[] params = new Type[this.paramTypes.length + 2];
                params[0] = Type.typeFor(ScriptObject.class);
                params[1] = Type.INT;
                System.arraycopy(this.paramTypes, 0, params, 2, this.paramTypes.length);
                this.staticSignature = Type.getMethodDescriptor(this.returnType, params);
            }
        }
        return this.staticSignature;
    }

    public String toString() {
        return this.methodName + " " + this.staticSignature;
    }
}

