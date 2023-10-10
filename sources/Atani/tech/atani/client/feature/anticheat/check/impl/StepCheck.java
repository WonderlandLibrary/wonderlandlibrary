package tech.atani.client.feature.anticheat.check.impl;

import net.minecraft.network.Packet;
import tech.atani.client.feature.anticheat.check.Check;
import tech.atani.client.feature.anticheat.data.PlayerData;

@Check.Info(name = "Step")
public class StepCheck extends Check {

    public StepCheck(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet<?> event) {
        double deltaY = (getData().getMovementTracker().getY() - getData().getMovementTracker().getLastY());

        boolean invalid = deltaY > 0.6 && getData().getMovementTracker().isLastOnGround();

        boolean exempt = System.currentTimeMillis() - getData().getLastTeleport() < 200;

        if (invalid && !exempt) {
            this.flag();
        }
    }

}
