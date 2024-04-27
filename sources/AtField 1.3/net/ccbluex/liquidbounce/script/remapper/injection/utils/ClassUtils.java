/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.ClassReader
 *  org.objectweb.asm.ClassVisitor
 *  org.objectweb.asm.ClassWriter
 *  org.objectweb.asm.tree.ClassNode
 */
package net.ccbluex.liquidbounce.script.remapper.injection.utils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public final class ClassUtils {
    public static final ClassUtils INSTANCE;

    public final byte[] toBytes(ClassNode classNode) {
        ClassWriter classWriter = new ClassWriter(3);
        classNode.accept((ClassVisitor)classWriter);
        return classWriter.toByteArray();
    }

    private ClassUtils() {
    }

    public final ClassNode toClassNode(byte[] byArray) {
        ClassReader classReader = new ClassReader(byArray);
        ClassNode classNode = new ClassNode();
        classReader.accept((ClassVisitor)classNode, 0);
        return classNode;
    }

    static {
        ClassUtils classUtils;
        INSTANCE = classUtils = new ClassUtils();
    }
}

