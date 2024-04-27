/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen.types;

import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.nashorn.internal.codegen.types.Type;

interface BytecodeNumericOps {
    public Type neg(MethodVisitor var1, int var2);

    public Type sub(MethodVisitor var1, int var2);

    public Type mul(MethodVisitor var1, int var2);

    public Type div(MethodVisitor var1, int var2);

    public Type rem(MethodVisitor var1, int var2);

    public Type cmp(MethodVisitor var1, boolean var2);
}

