/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.MethodInsnNode
 *  org.objectweb.asm.tree.MethodNode
 *  org.objectweb.asm.tree.TypeInsnNode
 *  org.objectweb.asm.tree.VarInsnNode
 */
package net.dev.important.patcher.asm.optifine.reflectionoptimizations.common;

import net.dev.important.patcher.tweaker.transform.PatcherTransformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ExtendedBlockStorageReflectionOptimizer
implements PatcherTransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.world.chunk.storage.ExtendedBlockStorage"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        block0: for (MethodNode methodNode : classNode.methods) {
            String methodName = this.mapMethodName(classNode, methodNode);
            if (!methodName.equals("func_177484_a")) continue;
            InsnList instructions = methodNode.instructions;
            for (AbstractInsnNode node : instructions) {
                if (node.getOpcode() == 182 && ((MethodInsnNode)node).name.equals("isInstance")) {
                    instructions.remove(node.getPrevious());
                    instructions.remove(node.getPrevious());
                    instructions.insertBefore(node, (AbstractInsnNode)new VarInsnNode(25, 4));
                    instructions.insertBefore(node, (AbstractInsnNode)new TypeInsnNode(193, "net/minecraftforge/common/property/IExtendedBlockState"));
                    instructions.remove(node);
                    continue;
                }
                if (node.getOpcode() != 192 || !((TypeInsnNode)node).desc.equals("net/minecraft/block/state/IBlockState")) continue;
                instructions.remove(node.getPrevious());
                instructions.remove(node.getPrevious());
                instructions.remove(node.getPrevious());
                instructions.remove(node.getPrevious());
                instructions.insertBefore(node, (AbstractInsnNode)new TypeInsnNode(192, "net/minecraftforge/common/property/IExtendedBlockState"));
                instructions.insertBefore(node, (AbstractInsnNode)new MethodInsnNode(182, "net/minecraftforge/common/property/IExtendedBlockState", "getClean", "()Lnet/minecraft/block/state/IBlockState;", false));
                instructions.remove(node);
                continue block0;
            }
        }
    }
}

