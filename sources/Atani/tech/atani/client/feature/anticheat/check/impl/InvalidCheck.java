package tech.atani.client.feature.anticheat.check.impl;

import net.minecraft.network.Packet;
import net.minecraft.potion.Potion;
import tech.atani.client.feature.anticheat.check.Check;
import tech.atani.client.feature.anticheat.data.PlayerData;

@Check.Info(name = "Invalid Move")
public class InvalidCheck extends Check {

    public InvalidCheck(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet<?> event) {
        double maxSpeed = getData().getPlayer().hurtTime > 0 ? 2.0D : 1.0D;

        if(getData().getPlayer().isPotionActive(Potion.moveSpeed)) {
            maxSpeed *= 1.2D;
        }

        if(getData().getPlayer().isPotionActive(Potion.moveSlowdown)) {
            maxSpeed *= 0.85D;
        }

        boolean invalid = getData().getMovementTracker().getSpeed() > maxSpeed;

        boolean exempt = System.currentTimeMillis() - getData().getLastTeleport() < 200;

        if(invalid && !exempt) {
            if(increaseBuffer() > 3) {
                this.flag();
            }
        }
        reduceBuffer(0.05);
    }

}