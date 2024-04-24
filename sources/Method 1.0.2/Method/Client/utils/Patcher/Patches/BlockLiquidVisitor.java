package Method.Client.utils.Patcher.Patches;

import Method.Client.utils.Patcher.Events.EventCanCollide;
import Method.Client.utils.Patcher.ModClassVisitor;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.*;


public final class BlockLiquidVisitor extends ModClassVisitor {

    public BlockLiquidVisitor(ClassVisitor cv, boolean obf) {
        super(cv);
        String iBlockState = unmap("net/minecraft/block/state/IBlockState");
        String Getcollidecheck_name = obf ? "a" : "canCollideCheck";
        String Getcollidecheck_desc = "(L" + iBlockState + ";Z)Z";
        registerMethodVisitor(Getcollidecheck_name, Getcollidecheck_desc, canCollideCheckVisitor::new);
    }


    public static class canCollideCheckVisitor extends MethodVisitor {
        public canCollideCheckVisitor(MethodVisitor mv) {
            super(Opcodes.ASM4, mv);
        }

        @Override
        public void visitCode() {
            System.out.println("BlockLiquidVisitor.canCollideCheck.visitFieldInsn()");

            mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(this.getClass()), "canCollideCheckHook", "()Z", false);
            Label l1 = new Label();
            mv.visitJumpInsn(Opcodes.IFEQ, l1);
            mv.visitInsn(Opcodes.ICONST_1);
            mv.visitInsn(Opcodes.IRETURN);
            mv.visitLabel(l1);
            super.visitCode();
        }

        public static boolean canCollideCheckHook() {
            return MinecraftForge.EVENT_BUS.post(new EventCanCollide());
        }
    }


}
