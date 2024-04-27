/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.objectweb.asm.ClassReader
 *  org.objectweb.asm.ClassVisitor
 *  org.objectweb.asm.ClassWriter
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.InsnList
 */
package net.dev.important.utils;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0004J\u001f\u0010\t\u001a\u00020\n2\u0012\u0010\u000b\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0\f\"\u00020\r\u00a2\u0006\u0002\u0010\u000e\u00a8\u0006\u000f"}, d2={"Lnet/dev/important/utils/ASMUtils;", "", "()V", "toBytes", "", "classNode", "Lorg/objectweb/asm/tree/ClassNode;", "toClassNode", "bytes", "toNodes", "Lorg/objectweb/asm/tree/InsnList;", "nodes", "", "Lorg/objectweb/asm/tree/AbstractInsnNode;", "([Lorg/objectweb/asm/tree/AbstractInsnNode;)Lorg/objectweb/asm/tree/InsnList;", "LiquidBounce"})
public final class ASMUtils {
    @NotNull
    public static final ASMUtils INSTANCE = new ASMUtils();

    private ASMUtils() {
    }

    @NotNull
    public final ClassNode toClassNode(@NotNull byte[] bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        ClassReader classReader = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        classReader.accept((ClassVisitor)classNode, 0);
        return classNode;
    }

    @NotNull
    public final byte[] toBytes(@NotNull ClassNode classNode) {
        Intrinsics.checkNotNullParameter(classNode, "classNode");
        ClassWriter classWriter = new ClassWriter(1);
        classNode.accept((ClassVisitor)classWriter);
        byte[] byArray = classWriter.toByteArray();
        Intrinsics.checkNotNullExpressionValue(byArray, "classWriter.toByteArray()");
        return byArray;
    }

    @NotNull
    public final InsnList toNodes(AbstractInsnNode ... nodes) {
        Intrinsics.checkNotNullParameter(nodes, "nodes");
        InsnList insnList = new InsnList();
        for (AbstractInsnNode node : nodes) {
            insnList.add(node);
        }
        return insnList;
    }
}

