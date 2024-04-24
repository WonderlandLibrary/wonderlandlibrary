
package Method.Client.utils.Patcher.Patches;
import Method.Client.Main;
import Method.Client.utils.Patcher.Events.GetAmbientOcclusionLightValueEvent;
import Method.Client.utils.Patcher.Events.ShouldSideBeRenderedEvent;
import Method.Client.utils.Patcher.ModClassVisitor;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public final class StateImplementationVisitor extends ModClassVisitor {

    public StateImplementationVisitor(ClassVisitor cv, boolean obf) {
        super(cv);

        String getAmbientOcclusionLightValueName = obf ? "j" : "getAmbientOcclusionLightValue";
        String getAmbientOcclusionLightValueDesc = "()F";
        String shouldSideBeRenderedName = obf ? "c" : "shouldSideBeRendered";
        String IBLOCK_ACCESS = unmap("net/minecraft/world/IBlockAccess");
        String BLOCK_POS = unmap("net/minecraft/util/math/BlockPos");
        String ENUM_FACING = unmap("net/minecraft/util/EnumFacing");
        String shouldSideBeRenderedDesc = "(L" + IBLOCK_ACCESS + ";L" + BLOCK_POS + ";L" + ENUM_FACING + ";)Z";

        registerMethodVisitor(getAmbientOcclusionLightValueName, getAmbientOcclusionLightValueDesc, GetAmbientOcclusionLightValueVisitor::new);
        registerMethodVisitor(shouldSideBeRenderedName, shouldSideBeRenderedDesc, ShouldSideBeRenderedVisitor::new);
    }

    public static class GetAmbientOcclusionLightValueVisitor extends MethodVisitor {
        public GetAmbientOcclusionLightValueVisitor(MethodVisitor mv) {
            super(Opcodes.ASM4, mv);
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == Opcodes.FRETURN) {
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(this.getClass()), "getAmbientOcclusionLightValue", "(FLnet/minecraft/block/state/IBlockState;)F", false);
            }
            super.visitInsn(opcode);
        }

        public static float getAmbientOcclusionLightValue(float f, IBlockState state) {
            GetAmbientOcclusionLightValueEvent event = new GetAmbientOcclusionLightValueEvent(state, f);
            MinecraftForge.EVENT_BUS.post(event);
            return event.getLightValue();
        }
    }

    public static class ShouldSideBeRenderedVisitor extends MethodVisitor {
        public ShouldSideBeRenderedVisitor(MethodVisitor mv) {
            super(Opcodes.ASM4, mv);
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == Opcodes.IRETURN) {
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(this.getClass()), "shouldSideBeRendered", "(ZLnet/minecraft/block/state/IBlockState;)Z", false);
            }
            super.visitInsn(opcode);
        }

        public static boolean shouldSideBeRendered(boolean b, IBlockState state) {
            ShouldSideBeRenderedEvent event = new ShouldSideBeRenderedEvent(state, b);
            Main.xray.onShouldSideBeRendered(event);
            return event.isRendered();
        }
    }
}

