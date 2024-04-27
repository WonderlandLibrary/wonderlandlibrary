/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.ClassReader
 *  org.objectweb.asm.ClassWriter
 */
package net.dev.important.patcher.tweaker;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

public class PatcherClassWriter
extends ClassWriter {
    public PatcherClassWriter(int flags) {
        super(flags);
    }

    public PatcherClassWriter(ClassReader classReader, int flags) {
        super(classReader, flags);
    }

    protected String getCommonSuperClass(String type1, String type2) {
        return ClassInfo.getCommonSuperClass(type1, type2).getName();
    }
}

