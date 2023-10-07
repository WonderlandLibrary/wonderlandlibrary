package koks.module.movement;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.event.RightClickEvent;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

@Module.Info(name = "VClip", description = "You can clip through walls", category = Module.Category.MOVEMENT)
public class VClip extends Module {

    @Value(name = "Mode", modes = {"Click"})
    String mode = "Click";

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof RightClickEvent) {
            if (mode.equalsIgnoreCase("Click")) {
                if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    final BlockPos blockPos = mc.objectMouseOver.getBlockPos();
                    if (getPlayer().rotationPitch == -90) {
                        final BlockPos above = searchSpaceAbove(blockPos);
                        if (above != null) {
                            setPosition(getX(), above.getY(), getZ());
                        }
                    } else if (getPlayer().rotationPitch == 90) {
                        final BlockPos under = searchSpaceUnder(blockPos);
                        if (under != null) {
                            setPosition(getX(), under.getY(), getZ());
                        }
                    }
                }
            }
        }
    }

    public BlockPos searchSpaceAbove(BlockPos blockPos) {
        for (int y = blockPos.getY() + 1; y < 255; y++) {
            final BlockPos pos = new BlockPos(blockPos.getX(), y, blockPos.getZ());
            if (getWorld().getBlockState(pos).getBlock() == Blocks.air && getWorld().getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.air) {
                return pos.add(0, 1, 0);
            }
        }
        return null;
    }

    public BlockPos searchSpaceUnder(BlockPos blockPos) {
        boolean wasAir = false;
        int distance = 0;
        BlockPos airPosition = null;
        for (int y = blockPos.getY(); y > 0; y--) {
            distance++;
            final BlockPos pos = new BlockPos(blockPos.getX(), y, blockPos.getZ());
            if (getWorld().getBlockState(pos).getBlock() == Blocks.air) {
                wasAir = true;
                if (airPosition == null)
                    airPosition = pos.add(0, -1, 0);
            } else if (wasAir) {
                return pos.add(0, 1, 0);
            }
            if (distance > 20) break;
        }
        return airPosition;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
