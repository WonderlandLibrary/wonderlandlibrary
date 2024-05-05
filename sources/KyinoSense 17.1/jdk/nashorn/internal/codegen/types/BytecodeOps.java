/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen.types;

import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.nashorn.internal.codegen.types.Type;

interface BytecodeOps {
    public Type dup(MethodVisitor var1, int var2);

    public Type pop(MethodVisitor var1);

    public Type swap(MethodVisitor var1, Type var2);

    public Type add(MethodVisitor var1, int var2);

    public Type load(MethodVisitor var1, int var2);

    public void store(MethodVisitor var1, int var2);

    public Type ldc(MethodVisitor var1, Object var2);

    public Type loadUndefined(MethodVisitor var1);

    public Type loadForcedInitializer(MethodVisitor var1);

    public Type loadEmpty(MethodVisitor var1);

    public Type convert(MethodVisitor var1, Type var2);

    public void _return(MethodVisitor var1);
}

