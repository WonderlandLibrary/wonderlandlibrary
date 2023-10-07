/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen.types;

import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.nashorn.internal.codegen.types.Type;

interface BytecodeArrayOps {
    public Type aload(MethodVisitor var1);

    public void astore(MethodVisitor var1);

    public Type arraylength(MethodVisitor var1);

    public Type newarray(MethodVisitor var1);

    public Type newarray(MethodVisitor var1, int var2);
}

