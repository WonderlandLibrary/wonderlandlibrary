package me.aquavit.liquidsense.script.remapper.injection.utils;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

public class NodeUtils {
    public static final NodeUtils INSTANCE = new NodeUtils();

    public final InsnList toNodes(AbstractInsnNode... nodes) {
        InsnList insnList = new InsnList();
        for (AbstractInsnNode node : nodes) {
            insnList.add(node);
        }
        return insnList;
    }

}
