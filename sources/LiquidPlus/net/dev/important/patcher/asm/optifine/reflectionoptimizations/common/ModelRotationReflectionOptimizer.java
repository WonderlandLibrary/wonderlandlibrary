/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.InsnNode
 *  org.objectweb.asm.tree.MethodInsnNode
 *  org.objectweb.asm.tree.MethodNode
 *  org.objectweb.asm.tree.VarInsnNode
 */
package net.dev.important.patcher.asm.optifine.reflectionoptimizations.common;

import net.dev.important.patcher.tweaker.transform.PatcherTransformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ModelRotationReflectionOptimizer
implements PatcherTransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.resources.model.ModelRotation"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals("apply")) {
                this.clearInstructions(methodNode);
                methodNode.instructions.insert(this.applyReflectionOptimization());
                continue;
            }
            if (!methodNode.name.equals("getMatrix")) continue;
            this.clearInstructions(methodNode);
            methodNode.instructions.insert(this.getMatrixReflectionOptimization());
        }
    }

    private InsnList getMatrixReflectionOptimization() {
        InsnList list = new InsnList();
        list.add((AbstractInsnNode)new VarInsnNode(25, 0));
        list.add((AbstractInsnNode)new MethodInsnNode(184, "net/minecraftforge/client/ForgeHooksClient", "getMatrix", "(Lnet/minecraft/client/resources/model/ModelRotation;)Ljavax/vecmath/Matrix4f;", false));
        list.add((AbstractInsnNode)new InsnNode(176));
        return list;
    }

    private InsnList applyReflectionOptimization() {
        InsnList list = new InsnList();
        list.add((AbstractInsnNode)new VarInsnNode(25, 0));
        list.add((AbstractInsnNode)new MethodInsnNode(182, "net/minecraft/client/resources/model/ModelRotation", "getMatrix", "()Ljavax/vecmath/Matrix4f;", false));
        list.add((AbstractInsnNode)new VarInsnNode(25, 1));
        list.add((AbstractInsnNode)new MethodInsnNode(184, "net/minecraftforge/client/ForgeHooksClient", "applyTransform", "(Ljavax/vecmath/Matrix4f;Lcom/google/common/base/Optional;)Lcom/google/common/base/Optional;", false));
        list.add((AbstractInsnNode)new InsnNode(176));
        return list;
    }
}

