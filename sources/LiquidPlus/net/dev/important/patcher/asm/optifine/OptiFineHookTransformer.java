/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.FieldInsnNode
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.InsnNode
 *  org.objectweb.asm.tree.MethodInsnNode
 *  org.objectweb.asm.tree.MethodNode
 *  org.objectweb.asm.tree.VarInsnNode
 */
package net.dev.important.patcher.asm.optifine;

import net.dev.important.patcher.tweaker.ClassTransformer;
import net.dev.important.patcher.tweaker.transform.PatcherTransformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class OptiFineHookTransformer
implements PatcherTransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.ccbluex.liquidbounce.patcher.hooks.font.OptiFineHook"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        if (ClassTransformer.smoothFontDetected) {
            return;
        }
        for (MethodNode method : classNode.methods) {
            InsnList insns;
            String methodName = method.name;
            if (methodName.equals("getOptifineBoldOffset")) {
                method.instructions.clear();
                insns = new InsnList();
                insns.add((AbstractInsnNode)new VarInsnNode(25, 1));
                insns.add((AbstractInsnNode)new FieldInsnNode(180, "net/minecraft/client/gui/FontRenderer", "offsetBold", "F"));
                insns.add((AbstractInsnNode)new InsnNode(174));
                method.instructions.add(insns);
                continue;
            }
            if (!methodName.equals("getCharWidth")) continue;
            method.instructions.clear();
            insns = new InsnList();
            insns.add((AbstractInsnNode)new VarInsnNode(25, 1));
            insns.add((AbstractInsnNode)new VarInsnNode(21, 2));
            insns.add((AbstractInsnNode)new MethodInsnNode(182, "net/minecraft/client/gui/FontRenderer", "getCharWidthFloat", "(C)F", false));
            insns.add((AbstractInsnNode)new InsnNode(174));
            method.instructions.add(insns);
        }
    }
}

