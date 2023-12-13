package vip.radium.module.impl.movement;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.event.impl.world.BlockCollisionEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.utils.Wrapper;

@ModuleInfo(label = "Jesus", category = ModuleCategory.MOVEMENT)
public final class Jesus extends Module {

    private boolean onLiquid;

    @EventLink
    private final Listener<UpdatePositionEvent> onUpdatePosition = event -> {
        if (event.isPre() && onLiquid && Wrapper.getPlayer().ticksExisted % 2 == 0) {
            event.setPosY(event.getPosY() + 0.000001F);
            onLiquid = false;
        }
    };

    @EventLink
    private final Listener<BlockCollisionEvent> onBlockCollision = event -> {
        if (event.getBlock() instanceof BlockLiquid && !Wrapper.getPlayer().isSneaking()) {
            final BlockPos blockPos = event.getBlockPos();
            final double x = blockPos.getX();
            final double y = blockPos.getY();
            final double z = blockPos.getZ();
            onLiquid = true;
            event.setBoundingBox(new AxisAlignedBB(x, y, z, x + 1, y + 1 - 0.000001F, z + 1));
        }
    };
}
