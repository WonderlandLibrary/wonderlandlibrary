/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.SinceKotlin;
import kotlin.jvm.internal.CallableReference;
import kotlin.jvm.internal.FunctionBase;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KCallable;
import kotlin.reflect.KFunction;

public class FunctionReference
extends CallableReference
implements FunctionBase,
KFunction {
    private final int arity;

    public FunctionReference(int arity) {
        this.arity = arity;
    }

    @SinceKotlin(version="1.1")
    public FunctionReference(int arity, Object receiver) {
        super(receiver);
        this.arity = arity;
    }

    @Override
    public int getArity() {
        return this.arity;
    }

    @Override
    @SinceKotlin(version="1.1")
    protected KFunction getReflected() {
        return (KFunction)super.getReflected();
    }

    @Override
    @SinceKotlin(version="1.1")
    protected KCallable computeReflected() {
        return Reflection.function(this);
    }

    @Override
    @SinceKotlin(version="1.1")
    public boolean isInline() {
        return this.getReflected().isInline();
    }

    @Override
    @SinceKotlin(version="1.1")
    public boolean isExternal() {
        return this.getReflected().isExternal();
    }

    @Override
    @SinceKotlin(version="1.1")
    public boolean isOperator() {
        return this.getReflected().isOperator();
    }

    @Override
    @SinceKotlin(version="1.1")
    public boolean isInfix() {
        return this.getReflected().isInfix();
    }

    @Override
    @SinceKotlin(version="1.1")
    public boolean isSuspend() {
        return this.getReflected().isSuspend();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof FunctionReference) {
            FunctionReference other = (FunctionReference)obj;
            return (this.getOwner() == null ? other.getOwner() == null : this.getOwner().equals(other.getOwner())) && this.getName().equals(other.getName()) && this.getSignature().equals(other.getSignature()) && Intrinsics.areEqual(this.getBoundReceiver(), other.getBoundReceiver());
        }
        if (obj instanceof KFunction) {
            return obj.equals(this.compute());
        }
        return false;
    }

    public int hashCode() {
        return ((this.getOwner() == null ? 0 : this.getOwner().hashCode() * 31) + this.getName().hashCode()) * 31 + this.getSignature().hashCode();
    }

    public String toString() {
        KCallable reflected = this.compute();
        if (reflected != this) {
            return reflected.toString();
        }
        return "<init>".equals(this.getName()) ? "constructor (Kotlin reflection is not available)" : "function " + this.getName() + " (Kotlin reflection is not available)";
    }
}

