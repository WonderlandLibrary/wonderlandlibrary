/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.launchwrapper.Launch
 *  net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.FieldInsnNode
 *  org.objectweb.asm.tree.FieldNode
 *  org.objectweb.asm.tree.MethodInsnNode
 *  org.objectweb.asm.tree.MethodNode
 */
package net.dev.important.patcher.tweaker.transform;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public interface PatcherTransformer {
    public String[] getClassName();

    public void transform(ClassNode var1, String var2);

    default public String mapMethodName(ClassNode classNode, MethodNode methodNode) {
        return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, methodNode.name, methodNode.desc);
    }

    default public String mapFieldName(ClassNode classNode, FieldNode fieldNode) {
        return FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(classNode.name, fieldNode.name, fieldNode.desc);
    }

    default public String mapClassName(String className) {
        return FMLDeobfuscatingRemapper.INSTANCE.map(className);
    }

    default public String mapMethodDesc(MethodNode methodNode) {
        return FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(methodNode.desc);
    }

    default public String mapMethodNameFromNode(AbstractInsnNode node) {
        MethodInsnNode methodInsnNode = (MethodInsnNode)node;
        return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc);
    }

    default public String mapFieldNameFromNode(AbstractInsnNode node) {
        FieldInsnNode fieldInsnNode = (FieldInsnNode)node;
        return FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(fieldInsnNode.owner, fieldInsnNode.name, fieldInsnNode.desc);
    }

    default public String mapMethodDescFromNode(AbstractInsnNode node) {
        MethodInsnNode methodInsnNode = (MethodInsnNode)node;
        return FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(methodInsnNode.desc);
    }

    default public void clearInstructions(MethodNode methodNode) {
        methodNode.instructions.clear();
        if (!methodNode.localVariables.isEmpty()) {
            methodNode.localVariables.clear();
        }
        if (!methodNode.tryCatchBlocks.isEmpty()) {
            methodNode.tryCatchBlocks.clear();
        }
    }

    default public String getHookClass(String name) {
        return "net/dev/liquidplus/patcher/hooks/" + name;
    }

    default public boolean isDevelopment() {
        Object o = Launch.blackboard.get("fml.deobfuscatedEnvironment");
        return o != null && (Boolean)o != false;
    }
}

