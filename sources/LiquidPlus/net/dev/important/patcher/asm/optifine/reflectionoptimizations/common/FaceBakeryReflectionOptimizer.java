/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.FieldInsnNode
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.InsnNode
 *  org.objectweb.asm.tree.JumpInsnNode
 *  org.objectweb.asm.tree.LabelNode
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
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class FaceBakeryReflectionOptimizer
implements PatcherTransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.renderer.block.model.FaceBakery"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        block0: for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals("makeBakedQuad") && methodNode.desc.contains("minecraftforge")) {
                for (AbstractInsnNode next : methodNode.instructions) {
                    if (!(next instanceof MethodInsnNode) || !((MethodInsnNode)next).name.equals("exists")) continue;
                    for (int i = 0; i < 17; ++i) {
                        methodNode.instructions.remove(next.getNext());
                    }
                    methodNode.instructions.remove(next.getPrevious());
                    methodNode.instructions.insertBefore(next, this.fillNormalReflectionOptimization());
                    methodNode.instructions.remove(next);
                    continue block0;
                }
                continue;
            }
            if (!methodNode.name.equals("rotateVertex") || !methodNode.desc.contains("minecraftforge")) continue;
            this.clearInstructions(methodNode);
            methodNode.instructions.insert(this.rotateVertexReflectionOptimization());
        }
    }

    private InsnList rotateVertexReflectionOptimization() {
        InsnList list = new InsnList();
        list.add((AbstractInsnNode)new VarInsnNode(25, 4));
        list.add((AbstractInsnNode)new FieldInsnNode(178, "net/minecraft/client/resources/model/ModelRotation", "X0_Y0", "Lnet/minecraft/client/resources/model/ModelRotation;"));
        LabelNode ifacmpne = new LabelNode();
        list.add((AbstractInsnNode)new JumpInsnNode(166, ifacmpne));
        list.add((AbstractInsnNode)new VarInsnNode(21, 3));
        list.add((AbstractInsnNode)new InsnNode(172));
        list.add((AbstractInsnNode)ifacmpne);
        list.add((AbstractInsnNode)new VarInsnNode(25, 1));
        list.add((AbstractInsnNode)new VarInsnNode(25, 4));
        list.add((AbstractInsnNode)new MethodInsnNode(185, "net/minecraftforge/client/model/ITransformation", "getMatrix", "()Ljavax/vecmath/Matrix4f;", true));
        list.add((AbstractInsnNode)new MethodInsnNode(184, "net/minecraftforge/client/ForgeHooksClient", "transform", "(Lorg/lwjgl/util/vector/Vector3f;Ljavax/vecmath/Matrix4f;)V", false));
        list.add((AbstractInsnNode)new VarInsnNode(25, 4));
        list.add((AbstractInsnNode)new VarInsnNode(25, 2));
        list.add((AbstractInsnNode)new VarInsnNode(21, 3));
        list.add((AbstractInsnNode)new MethodInsnNode(185, "net/minecraftforge/client/model/ITransformation", "rotate", "(Lnet/minecraft/util/EnumFacing;I)I", true));
        list.add((AbstractInsnNode)new InsnNode(172));
        return list;
    }

    private InsnList fillNormalReflectionOptimization() {
        InsnList list = new InsnList();
        list.add((AbstractInsnNode)new VarInsnNode(25, 10));
        list.add((AbstractInsnNode)new VarInsnNode(25, 11));
        list.add((AbstractInsnNode)new MethodInsnNode(184, "net/minecraftforge/client/ForgeHooksClient", "fillNormal", "([ILnet/minecraft/util/EnumFacing;)V", false));
        return list;
    }
}

