
package Method.Client.utils.Patcher.Patches;


import Method.Client.utils.Patcher.Events.PlayerDamageBlockEvent;
import Method.Client.utils.Patcher.ModClassVisitor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public final class PlayerControllerMPVisitor extends ModClassVisitor {
    public PlayerControllerMPVisitor(ClassVisitor cv, boolean obf) {
        super(cv);

        String blockPos = unmap("net/minecraft/util/math/BlockPos");
        String enumFacing = unmap("net/minecraft/util/EnumFacing");

        String onPlayerDamageBlock_name = obf ? "b" : "onPlayerDamageBlock";
        String onPlayerDamageBlock_desc = "(L" + blockPos + ";L" + enumFacing + ";)Z";

        registerMethodVisitor(onPlayerDamageBlock_name, onPlayerDamageBlock_desc, OnPlayerDamageBlockVisitor::new);
    }

    public static class OnPlayerDamageBlockVisitor extends MethodVisitor {
        public OnPlayerDamageBlockVisitor(MethodVisitor mv) {
            super(Opcodes.ASM4, mv);
        }

        @Override
        public void visitCode() {
            System.out.println(
                    "PlayerControllerMPVisitor.OnPlayerDamageBlockVisitor.visitCode()");

            super.visitCode();
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitVarInsn(Opcodes.ALOAD, 2);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(this.getClass()), "onPlayerDamageBlock", "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)V", false);
        }
        public static void onPlayerDamageBlock(BlockPos pos, EnumFacing facing) {
            MinecraftForge.EVENT_BUS.post(new PlayerDamageBlockEvent(pos, facing));
        }
    }


}
