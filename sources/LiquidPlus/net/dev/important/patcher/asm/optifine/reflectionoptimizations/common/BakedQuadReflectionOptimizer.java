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

public class BakedQuadReflectionOptimizer
implements PatcherTransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.renderer.block.model.BakedQuad"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode methodNode : classNode.methods) {
            if (!methodNode.name.equals("pipe")) continue;
            this.clearInstructions(methodNode);
            methodNode.instructions.insert(this.pipeReflectionOptimization());
            break;
        }
    }

    private InsnList pipeReflectionOptimization() {
        InsnList list = new InsnList();
        list.add((AbstractInsnNode)new VarInsnNode(25, 1));
        list.add((AbstractInsnNode)new VarInsnNode(25, 0));
        list.add((AbstractInsnNode)new MethodInsnNode(184, "net/minecraftforge/client/model/pipeline/LightUtil", "putBakedQuad", "(Lnet/minecraftforge/client/model/pipeline/IVertexConsumer;Lnet/minecraft/client/renderer/block/model/BakedQuad;)V", false));
        list.add((AbstractInsnNode)new InsnNode(177));
        return list;
    }
}

