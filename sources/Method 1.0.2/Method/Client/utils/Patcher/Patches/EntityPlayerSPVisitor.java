
package Method.Client.utils.Patcher.Patches;

import Method.Client.utils.Patcher.Events.PlayerMoveEvent;
import Method.Client.utils.Patcher.Events.PostMotionEvent;
import Method.Client.utils.Patcher.Events.PreMotionEvent;
import Method.Client.utils.Patcher.ModClassVisitor;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public final class EntityPlayerSPVisitor extends ModClassVisitor {

    public EntityPlayerSPVisitor(ClassVisitor cv, boolean obf) {
        super(cv);

        String onUpdate;
        String onUpdatedesc = "()V";

        String moverType = unmap("net/minecraft/entity/MoverType");
        onUpdate = obf ? "B_" : "onUpdate";
        String moveEntity_name = obf ? "a" : "move";
        String moveEntity_desc = "(L" + moverType + ";DDD)V";

        registerMethodVisitor(onUpdate, onUpdatedesc, onUpdateplayer::new);
        registerMethodVisitor(moveEntity_name, moveEntity_desc, MoveEntityVisitor::new);
    }

    public static class onUpdateplayer extends MethodVisitor {
        public onUpdateplayer(MethodVisitor mv) {
            super(Opcodes.ASM4, mv);
        }

        @Override
        public void visitCode() {
            System.out.println("EntityPlayerSPVisitor.onUpdateplayer.visitCode()");
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(this.getClass()), "onPreMotion", "(Lnet/minecraft/client/entity/EntityPlayerSP;)V", false);
            super.visitCode();
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == Opcodes.RETURN) {
                System.out.println("EntityPlayerSPVisitor.onUpdateplayer.visitInsn()");

                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(this.getClass()), "onPostMotion", "(Lnet/minecraft/client/entity/EntityPlayerSP;)V", false);
            }
            super.visitInsn(opcode);
        }

        public static void onPreMotion(EntityPlayerSP player) {
            MinecraftForge.EVENT_BUS.post(new PreMotionEvent(player));
        }

        public static void onPostMotion(EntityPlayerSP player) {
            MinecraftForge.EVENT_BUS.post(new PostMotionEvent(player));
        }
    }

    public static class MoveEntityVisitor extends MethodVisitor {
        public MoveEntityVisitor(MethodVisitor mv) {
            super(Opcodes.ASM4, mv);
        }

        @Override
        public void visitCode() {
            System.out.println("EntityPlayerSPVisitor.MoveEntityVisitor.visitCode()");

            super.visitCode();
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(this.getClass()), "onPlayerMove", "(Lnet/minecraft/client/entity/EntityPlayerSP;)V", false);
        }

        public static void onPlayerMove(EntityPlayerSP player) {
            MinecraftForge.EVENT_BUS.post(new PlayerMoveEvent(player));
        }

    }

}
