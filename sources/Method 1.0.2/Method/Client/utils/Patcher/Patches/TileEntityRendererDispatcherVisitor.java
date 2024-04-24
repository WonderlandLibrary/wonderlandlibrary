
package Method.Client.utils.Patcher.Patches;

import Method.Client.utils.Patcher.Events.RenderTileEntityEvent;
import Method.Client.utils.Patcher.ModClassVisitor;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.*;

public final class TileEntityRendererDispatcherVisitor extends ModClassVisitor {
    public TileEntityRendererDispatcherVisitor(ClassVisitor cv, boolean obf) {
        super(cv);

        String tileEntity = unmap("net/minecraft/tileentity/TileEntity");

        String render_name = obf ? "a" : "render";
        String render_desc = "(L" + tileEntity + ";FI)V";

        registerMethodVisitor(render_name, render_desc, RenderVisitor::new);
    }

    public static class RenderVisitor extends MethodVisitor {
        public RenderVisitor(MethodVisitor mv) {
            super(Opcodes.ASM4, mv);
        }

        @Override
        public void visitCode() {
            System.out.println("TileEntityRendererDispatcherVisitor.RenderVisitor.visitCode()");

            super.visitCode();
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(this.getClass()), "renderTileEntity", "(Lnet/minecraft/tileentity/TileEntity;)Z", false);
            Label l1 = new Label();
            mv.visitJumpInsn(Opcodes.IFNE, l1);
            mv.visitInsn(Opcodes.RETURN);
            mv.visitLabel(l1);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        }

        public static boolean renderTileEntity(TileEntity tileEntity) {
            return !MinecraftForge.EVENT_BUS.post(new RenderTileEntityEvent(tileEntity));
        }
    }


}
