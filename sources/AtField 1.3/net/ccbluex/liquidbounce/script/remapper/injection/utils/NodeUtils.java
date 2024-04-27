/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.InsnList
 */
package net.ccbluex.liquidbounce.script.remapper.injection.utils;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

public final class NodeUtils {
    public static final NodeUtils INSTANCE;

    private NodeUtils() {
    }

    static {
        NodeUtils nodeUtils;
        INSTANCE = nodeUtils = new NodeUtils();
    }

    public final InsnList toNodes(AbstractInsnNode ... abstractInsnNodeArray) {
        InsnList insnList = new InsnList();
        for (AbstractInsnNode abstractInsnNode : abstractInsnNodeArray) {
            insnList.add(abstractInsnNode);
        }
        return insnList;
    }
}

