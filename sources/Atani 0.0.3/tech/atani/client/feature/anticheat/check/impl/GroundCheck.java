package tech.atani.client.feature.anticheat.check.impl;

import net.minecraft.network.Packet;
import net.minecraft.util.BlockPos;
import tech.atani.client.feature.anticheat.check.Check;
import tech.atani.client.feature.anticheat.data.PlayerData;
import tech.atani.client.utility.world.block.BlockUtil;

@Check.Info(name = "GroundSpoof")
public class GroundCheck extends Check {

    public GroundCheck(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet<?> event) {
        BlockPos underPlayer = new BlockPos(getData().getPlayer().posX, getData().getPlayer().posY - 1.0D, getData().getPlayer().posZ);

        boolean invalid = !BlockUtil.isCollidingOnGround(underPlayer, 0.8D) && getData().getMovementTracker().isOnGround();

        if(invalid) {
            if(increaseBuffer() > 4) {
                this.flag();
            }
        }
        reduceBuffer(0.05);
    }

}
