/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.launchwrapper.IClassTransformer
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.MethodInsnNode
 */
package net.dev.important.injection.transformers;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import net.dev.important.utils.ASMUtils;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class OptimizeTransformer
implements IClassTransformer {
    private static final HashMap<String, String> transformMap = new HashMap();

    private static void addTransform(String mcpName, String notchName, String targetName) {
        transformMap.put(mcpName, targetName);
        transformMap.put(notchName, targetName);
    }

    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!(name.startsWith("net.ccbluex") || name.startsWith("kotlin") || basicClass == null || transformMap.containsKey(transformedName))) {
            try {
                ClassNode classNode = ASMUtils.INSTANCE.toClassNode(basicClass);
                AtomicBoolean changed = new AtomicBoolean(false);
                classNode.methods.forEach(methodNode -> {
                    for (int i = 0; i < methodNode.instructions.size(); ++i) {
                        String owner;
                        MethodInsnNode min;
                        AbstractInsnNode abstractInsnNode = methodNode.instructions.get(i);
                        if (!(abstractInsnNode instanceof MethodInsnNode) || (min = (MethodInsnNode)abstractInsnNode).getOpcode() != 184 || !min.name.equals("values") || !transformMap.containsKey(owner = min.owner.replaceAll("/", "."))) continue;
                        changed.set(true);
                        min.owner = "net/dev/liquidplus/injection/access/StaticStorage";
                        min.name = transformMap.get(owner);
                    }
                });
                if (changed.get()) {
                    return ASMUtils.INSTANCE.toBytes(classNode);
                }
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return basicClass;
    }

    static {
        OptimizeTransformer.addTransform("net.minecraft.util.EnumFacing", "cq", "facings");
        OptimizeTransformer.addTransform("net.minecraft.util.EnumChatFormatting", "a", "chatFormatting");
        OptimizeTransformer.addTransform("net.minecraft.util.EnumParticleTypes", "cy", "particleTypes");
        OptimizeTransformer.addTransform("net.minecraft.util.EnumWorldBlockLayer", "adf", "worldBlockLayers");
    }
}

