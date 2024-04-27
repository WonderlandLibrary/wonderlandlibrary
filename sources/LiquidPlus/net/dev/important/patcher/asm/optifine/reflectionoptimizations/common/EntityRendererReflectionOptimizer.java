/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.FieldInsnNode
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.MethodInsnNode
 *  org.objectweb.asm.tree.MethodNode
 *  org.objectweb.asm.tree.VarInsnNode
 */
package net.dev.important.patcher.asm.optifine.reflectionoptimizations.common;

import net.dev.important.patcher.tweaker.transform.PatcherTransformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class EntityRendererReflectionOptimizer
implements PatcherTransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.renderer.EntityRenderer"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        block0: for (MethodNode methodNode : classNode.methods) {
            String methodName = this.mapMethodName(classNode, methodNode);
            if (!methodName.equals("updateCameraAndRender") && !methodName.equals("func_181560_a")) continue;
            for (AbstractInsnNode next : methodNode.instructions) {
                if (!(next instanceof MethodInsnNode) || !((MethodInsnNode)next).name.equals("exists")) continue;
                next = next.getPrevious();
                for (int i = 0; i < 29; ++i) {
                    methodNode.instructions.remove(next.getNext());
                }
                methodNode.instructions.insertBefore(next, this.optimizeReflection());
                methodNode.instructions.remove(next);
                break block0;
            }
        }
    }

    private InsnList optimizeReflection() {
        InsnList list = new InsnList();
        list.add((AbstractInsnNode)new VarInsnNode(25, 0));
        list.add((AbstractInsnNode)new FieldInsnNode(180, "net/minecraft/client/renderer/EntityRenderer", "field_78531_r", "Lnet/minecraft/client/Minecraft;"));
        list.add((AbstractInsnNode)new FieldInsnNode(180, "net/minecraft/client/Minecraft", "field_71462_r", "Lnet/minecraft/client/gui/GuiScreen;"));
        list.add((AbstractInsnNode)new VarInsnNode(21, 8));
        list.add((AbstractInsnNode)new VarInsnNode(21, 9));
        list.add((AbstractInsnNode)new VarInsnNode(23, 1));
        list.add((AbstractInsnNode)new MethodInsnNode(184, "net/minecraftforge/client/ForgeHooksClient", "drawScreen", "(Lnet/minecraft/client/gui/GuiScreen;IIF)V", false));
        return list;
    }
}

