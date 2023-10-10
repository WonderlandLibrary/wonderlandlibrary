package tech.atani.client.feature.anticheat.check.impl;

import net.minecraft.network.Packet;
import tech.atani.client.feature.anticheat.check.Check;
import tech.atani.client.feature.anticheat.data.PlayerData;

@Check.Info(name = "Move")
public class MoveCheck extends Check {

    public MoveCheck(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet<?> event) {
        double difference = Math.abs(getData().getMovementTracker().getSpeed() - getData().getMovementTracker().getLastSpeed());

        boolean invalid = difference < 3E-6 && !getData().getMovementTracker().isLastOnGround() && getData().getMovementTracker().getyDiff() > 0.4;

        boolean exempt = System.currentTimeMillis() - getData().getLastTeleport() < 200;

        if(invalid && !exempt) {
            if(increaseBuffer() > 2) {
                this.flag();
            }
        }
        reduceBuffer(0.05);
    }

}
