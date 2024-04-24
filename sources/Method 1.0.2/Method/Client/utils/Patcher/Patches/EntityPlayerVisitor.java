
package Method.Client.utils.Patcher.Patches;


import Method.Client.utils.Patcher.Events.EntityPlayerJumpEvent;
import Method.Client.utils.Patcher.ModClassVisitor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.*;

public final class EntityPlayerVisitor extends ModClassVisitor {

    public EntityPlayerVisitor(ClassVisitor cv, boolean obf) {
        super(cv);

        String jump_name = obf ? "cu" : "jump";
        String jump_desc = "()V";

        registerMethodVisitor(jump_name, jump_desc, JumpVisitor::new);
    }

    public static class JumpVisitor extends MethodVisitor {
        public JumpVisitor(MethodVisitor mv) {
            super(Opcodes.ASM4, mv);
        }

        @Override
        public void visitCode() {
            System.out.println("EntityPlayerVisitor.JumpVisitor.visitCode()");
            super.visitCode();
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(this.getClass()), "entityPlayerJump", "(Lnet/minecraft/entity/player/EntityPlayer;)Z", false);
            Label l1 = new Label();
            mv.visitJumpInsn(Opcodes.IFNE, l1);
            mv.visitInsn(Opcodes.RETURN);
            mv.visitLabel(l1);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        }

        public static boolean entityPlayerJump(EntityPlayer player) {
            return !MinecraftForge.EVENT_BUS.post(new EntityPlayerJumpEvent(player));
        }
    }


}
