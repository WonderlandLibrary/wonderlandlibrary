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
package net.dev.important.patcher.asm.forge;

import net.dev.important.patcher.tweaker.transform.PatcherTransformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class LightUtilTransformer
implements PatcherTransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraftforge.client.model.pipeline.LightUtil"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode method : classNode.methods) {
            if (!method.name.equals("diffuseLight") || !method.desc.equals("(FFF)F")) continue;
            this.clearInstructions(method);
            method.instructions.insert(this.fixLightingPipeline());
            break;
        }
    }

    private InsnList fixLightingPipeline() {
        InsnList list = new InsnList();
        list.add((AbstractInsnNode)new VarInsnNode(23, 0));
        list.add((AbstractInsnNode)new VarInsnNode(23, 1));
        list.add((AbstractInsnNode)new VarInsnNode(23, 2));
        list.add((AbstractInsnNode)new MethodInsnNode(184, this.getHookClass("misc/LightUtilHook"), "diffuseLight", "(FFF)F", false));
        list.add((AbstractInsnNode)new InsnNode(174));
        return list;
    }
}

