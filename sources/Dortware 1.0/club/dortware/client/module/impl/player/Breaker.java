package club.dortware.client.module.impl.player;

import club.dortware.client.Client;
import club.dortware.client.event.impl.UpdateEvent;
import club.dortware.client.manager.impl.PropertyManager;
import club.dortware.client.module.Module;
import club.dortware.client.module.annotations.ModuleData;
import club.dortware.client.module.enums.ModuleCategory;
import club.dortware.client.property.impl.BooleanProperty;
import club.dortware.client.property.impl.DoubleProperty;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleData(name = "Breaker", category = ModuleCategory.PLAYER)
public class Breaker extends Module {

    private static final EnumFacing[] DIRECTIONS = new EnumFacing[] { EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST };

    @Override
    public void setup() {
        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
        propertyManager.add(new BooleanProperty<>("Break Surrounding", this, true));
        propertyManager.add(new DoubleProperty<>("Distance", this, 4, 0, 7, false));
    }

    @Subscribe
    public void handlePlayerUpdate(UpdateEvent event) {
        if (event.isPre()) {
            PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
            Double distance = (Double) propertyManager.getProperty(this, "Distance").getValue();
            Boolean breakSurrounding = (Boolean) propertyManager.getProperty(this, "Break Surrounding").getValue();
            for (int x = -distance.intValue(); x < distance.intValue(); ++x) {
                for (int y = distance.intValue(); y > -distance.intValue(); --y) {
                    for (int z = -distance.intValue(); z < distance.intValue(); ++z) {
                        int xPos = (int) this.mc.thePlayer.posX + x;
                        int yPos = (int) this.mc.thePlayer.posY + y;
                        int zPos = (int) this.mc.thePlayer.posZ + z;
                        BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
                        Block block = this.mc.theWorld.getBlockState(blockPos).getBlock();
                        if (block != Blocks.cake) {
                            continue;
                        }
                        if (!isOpen(blockPos) &&  breakSurrounding) {
                            for (EnumFacing direction : DIRECTIONS) {
                                BlockPos toCheck = blockPos.offset(direction);
                                if (!(mc.theWorld.getBlockState(toCheck).getBlock() instanceof BlockAir)) {
                                    BlockPos preventingBlock = blockPos.offset(direction);
                                    breakBlock(preventingBlock);
                                    return;
                                }
                            }
                        }
                        breakBlock(blockPos);
                    }
                }
            }
        }
    }

    private void breakBlock(BlockPos blockPos) {
        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
                C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
                C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
    }

    private boolean isOpen(BlockPos blockPos) {
        for (EnumFacing direction : DIRECTIONS) {
            BlockPos toCheck = blockPos.offset(direction);
            if (mc.theWorld.getBlockState(toCheck).getBlock() instanceof BlockAir)
                return true;
        }
        return false;
    }
}
