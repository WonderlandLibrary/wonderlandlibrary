/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.MethodNode
 */
package net.dev.important.patcher.asm.optifine;

import net.dev.important.patcher.tweaker.transform.PatcherTransformer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class OptifineFontRendererTransformer
implements PatcherTransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.gui.FontRenderer"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode method : classNode.methods) {
            if (!method.name.equals("getCharWidthFloat")) continue;
            method.access = 1;
            break;
        }
    }
}

