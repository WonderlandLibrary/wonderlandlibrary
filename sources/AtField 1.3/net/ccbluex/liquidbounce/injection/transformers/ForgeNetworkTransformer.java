/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.launchwrapper.IClassTransformer
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.InsnNode
 *  org.objectweb.asm.tree.JumpInsnNode
 *  org.objectweb.asm.tree.LabelNode
 *  org.objectweb.asm.tree.MethodInsnNode
 *  org.objectweb.asm.tree.MethodNode
 */
package net.ccbluex.liquidbounce.injection.transformers;

import net.ccbluex.liquidbounce.features.special.AntiForge;
import net.ccbluex.liquidbounce.script.remapper.injection.utils.ClassUtils;
import net.ccbluex.liquidbounce.script.remapper.injection.utils.NodeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ForgeNetworkTransformer
implements IClassTransformer {
    private static boolean lambda$transform$2(MethodNode methodNode) {
        return methodNode.name.equals("channelRead0");
    }

    public byte[] transform(String string, String string2, byte[] byArray) {
        if (string.equals("net.minecraftforge.fml.common.network.handshake.NetworkDispatcher")) {
            try {
                ClassNode classNode = ClassUtils.INSTANCE.toClassNode(byArray);
                classNode.methods.stream().filter(ForgeNetworkTransformer::lambda$transform$0).forEach(ForgeNetworkTransformer::lambda$transform$1);
                return ClassUtils.INSTANCE.toBytes(classNode);
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        if (string.equals("net.minecraftforge.fml.common.network.handshake.HandshakeMessageHandler")) {
            try {
                ClassNode classNode = ClassUtils.INSTANCE.toClassNode(byArray);
                classNode.methods.stream().filter(ForgeNetworkTransformer::lambda$transform$2).forEach(ForgeNetworkTransformer::lambda$transform$3);
                return ClassUtils.INSTANCE.toBytes(classNode);
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return byArray;
    }

    public static boolean returnMethod() {
        return AntiForge.enabled && AntiForge.blockFML && !Minecraft.func_71410_x().func_71387_A();
    }

    private static boolean lambda$transform$0(MethodNode methodNode) {
        return methodNode.name.equals("handleVanilla");
    }

    private static void lambda$transform$3(MethodNode methodNode) {
        LabelNode labelNode = new LabelNode();
        methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), NodeUtils.INSTANCE.toNodes(new AbstractInsnNode[]{new MethodInsnNode(184, "net/ccbluex/liquidbounce/injection/transformers/ForgeNetworkTransformer", "returnMethod", "()Z", false), new JumpInsnNode(153, labelNode), new InsnNode(177), labelNode}));
    }

    private static void lambda$transform$1(MethodNode methodNode) {
        LabelNode labelNode = new LabelNode();
        methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), NodeUtils.INSTANCE.toNodes(new AbstractInsnNode[]{new MethodInsnNode(184, "net/ccbluex/liquidbounce/injection/transformers/ForgeNetworkTransformer", "returnMethod", "()Z", false), new JumpInsnNode(153, labelNode), new InsnNode(3), new InsnNode(172), labelNode}));
    }
}

