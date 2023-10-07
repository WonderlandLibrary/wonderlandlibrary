/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.ClassReader
 *  org.objectweb.asm.ClassVisitor
 *  org.objectweb.asm.ClassWriter
 *  org.objectweb.asm.tree.ClassNode
 */
package net.ccbluex.liquidbounce.script.remapper.injection.utils;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0004\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/script/remapper/injection/utils/ClassUtils;", "", "()V", "toBytes", "", "classNode", "Lorg/objectweb/asm/tree/ClassNode;", "toClassNode", "bytes", "KyinoClient"})
public final class ClassUtils {
    public static final ClassUtils INSTANCE;

    @NotNull
    public final ClassNode toClassNode(@NotNull byte[] bytes) {
        Intrinsics.checkParameterIsNotNull(bytes, "bytes");
        ClassReader classReader = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        classReader.accept((ClassVisitor)classNode, 0);
        return classNode;
    }

    @NotNull
    public final byte[] toBytes(@NotNull ClassNode classNode) {
        Intrinsics.checkParameterIsNotNull(classNode, "classNode");
        ClassWriter classWriter = new ClassWriter(1);
        classNode.accept((ClassVisitor)classWriter);
        byte[] byArray = classWriter.toByteArray();
        Intrinsics.checkExpressionValueIsNotNull(byArray, "classWriter.toByteArray()");
        return byArray;
    }

    private ClassUtils() {
    }

    static {
        ClassUtils classUtils;
        INSTANCE = classUtils = new ClassUtils();
    }
}

