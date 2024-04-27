/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.injection.transformers;

import net.ccbluex.liquidbounce.features.special.AntiForge;
import net.ccbluex.liquidbounce.script.remapper.injection.utils.ClassUtils;
import net.ccbluex.liquidbounce.script.remapper.injection.utils.NodeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * Transform bytecode of classes
 */
public class ForgeNetworkTransformer implements IClassTransformer {

    /**
     * Transform a class
     *
     * @param name            of target class
     * @param transformedName of target class
     * @param basicClass      bytecode of target class
     * @return new bytecode
     */
    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass) {
        if (name.equals("net.minecraftforge.fml.common.network.handshake.NetworkDispatcher")) {
            try {
                final ClassNode classNode = ClassUtils.INSTANCE.toClassNode(basicClass);
                final LabelNode labelNode = new LabelNode();
                classNode.methods.stream().filter(methodNode -> methodNode.name.equals("handleVanilla")).forEach(methodNode -> {
                    methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), NodeUtils.INSTANCE.toNodes(new AbstractInsnNode[] { new MethodInsnNode(184, "net/ccbluex/liquidbounce/injection/transformers/ForgeNetworkTransformer", "returnMethod", "()Z", false), new JumpInsnNode(153, labelNode), new InsnNode(3), new InsnNode(172), labelNode }));
                    return;
                });
                return ClassUtils.INSTANCE.toBytes(classNode);
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        if (name.equals("net.minecraftforge.fml.common.network.handshake.HandshakeMessageHandler")) {
            try {
                final ClassNode classNode = ClassUtils.INSTANCE.toClassNode(basicClass);
                final LabelNode labelNode2 = new LabelNode();;
                classNode.methods.stream().filter(method -> method.name.equals("channelRead0")).forEach(methodNode -> {
                    methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), NodeUtils.INSTANCE.toNodes(new AbstractInsnNode[] { new MethodInsnNode(184, "net/ccbluex/liquidbounce/injection/transformers/ForgeNetworkTransformer", "returnMethod", "()Z", false), new JumpInsnNode(153, labelNode2), new InsnNode(177), labelNode2 }));
                    return;
                });
                return ClassUtils.INSTANCE.toBytes(classNode);
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        if (name.equals("net.ccbluex.liquidbounce.injection.forge.mixins.packets.MixinC00Handshake")) {
            try {
                final ClassNode classNode = ClassUtils.INSTANCE.toClassNode(basicClass);
                final LabelNode labelNode3 = new LabelNode();
                classNode.methods.stream().filter(method -> method.name.equals("writePacketData")).forEach(methodNode -> {
                    methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), NodeUtils.INSTANCE.toNodes(new AbstractInsnNode[] { new MethodInsnNode(184, "net/ccbluex/liquidbounce/injection/transformers/ForgeNetworkTransformer", "returnMethod", "()Z", false), new JumpInsnNode(153, labelNode3), new InsnNode(177), labelNode3 }));
                    return;
                });
                return ClassUtils.INSTANCE.toBytes(classNode);
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return basicClass;
    }

    public static boolean returnMethod() {
        return !Minecraft.getMinecraft().isIntegratedServerRunning();
    }

/*
    public static boolean returnMethod() {
        return AntiForge.enabled && !Minecraft.getMinecraft().isIntegratedServerRunning();
    }

 */
}