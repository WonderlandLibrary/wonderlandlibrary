/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.LdcInsnNode
 *  org.objectweb.asm.tree.MethodInsnNode
 *  org.objectweb.asm.tree.MethodNode
 */
package net.dev.important.patcher.asm.optifine;

import net.dev.important.patcher.tweaker.ClassTransformer;
import net.dev.important.patcher.tweaker.transform.PatcherTransformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class GuiIngameForgeTransformer
implements PatcherTransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraftforge.client.GuiIngameForge"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        block0: for (MethodNode method : classNode.methods) {
            if (!method.name.equals("renderExperience")) continue;
            for (AbstractInsnNode next : method.instructions) {
                if (!(next instanceof LdcInsnNode) || !((LdcInsnNode)next).cst.equals(8453920)) continue;
                method.instructions.insertBefore(next.getNext(), (AbstractInsnNode)new MethodInsnNode(184, ClassTransformer.optifineVersion.equals("I7") ? "CustomColors" : "net/optifine/CustomColors", "getExpBarTextColor", "(I)I", false));
                break block0;
            }
        }
    }
}

