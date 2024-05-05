package club.dortware.client.module.impl.movement;

import club.dortware.client.event.impl.BlockCollisionEvent;
import club.dortware.client.event.impl.UpdateEvent;
import club.dortware.client.module.Module;
import club.dortware.client.module.annotations.ModuleData;
import club.dortware.client.module.enums.ModuleCategory;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

@ModuleData(name = "Jesus", description = "nigga", category = ModuleCategory.MOVEMENT)
public class Jesus extends Module {
    @Override
    public void setup() {

    }

    @Subscribe
    public void onMotionUpdate(UpdateEvent event) {
        if (!event.isPre())
            return;
        if (checkWater()) {
            event.setPosY(event.getPosY() + 0.001425);
        }
    }
    @Subscribe
    public void onCollide(BlockCollisionEvent event) {
        if (event.getBlock() instanceof BlockLiquid) {
            if (mc.thePlayer.isSneaking())
                return;
            double x = event.getX();
            double y = event.getY();
            double z = event.getZ();
            event.setAxisAlignedBB(AxisAlignedBB.fromBounds(-2, -1, -2, 2, 0.999F, 2).offset(x, y, z));
        }
    }

    public boolean checkWater() {
        return mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).offsetDown()).getBlock() instanceof BlockLiquid || mc.theWorld.getBlockState(new BlockPos(mc.thePlayer)).getBlock() instanceof BlockLiquid;
    }

}
