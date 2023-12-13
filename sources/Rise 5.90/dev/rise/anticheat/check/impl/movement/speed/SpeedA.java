package dev.rise.anticheat.check.impl.movement.speed;

import dev.rise.anticheat.check.Check;
import dev.rise.anticheat.check.api.CheckInfo;
import dev.rise.anticheat.data.PlayerData;
import dev.rise.anticheat.util.PacketUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;

@CheckInfo(name = "Speed", type = "A", description = "Detects invalid strafing")
public final class SpeedA extends Check {

    private double deltaXOffGround, deltaZOffGround;

    public SpeedA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet<?> packet) {
        if (PacketUtil.isRelMove(packet) && getData().isOnGround() && !getData().isLastOnGround() && !getData().getPlayer().isOnLadder() && getData().getTicksSinceLastVelocity() > 20 && getData().getPlayer().ticksExisted > 100) {
            final S14PacketEntity wrapper = ((S14PacketEntity) packet);
            if (wrapper.entityId != getData().getPlayer().getEntityId()) return;

            final double speed = Math.abs(getData().getDeltaX()) + Math.abs(getData().getDeltaZ());

            if (speed <= 0.375) return;

            final double groundX = getData().getGroundX();
            final double groundZ = getData().getGroundZ();
            final double lastGroundX = getData().getLastGroundX();
            final double lastGroundZ = getData().getLastGroundZ();

            final double direction = Math.atan2((lastGroundX - (lastGroundX + deltaXOffGround)), (lastGroundZ - (lastGroundZ + deltaZOffGround))) * 180 / Math.PI;
            final double groundDirection = Math.atan2((lastGroundX - groundX), (lastGroundZ - groundZ)) * 180 / Math.PI;
            final double difference = Math.abs(groundDirection - direction);

            if (difference > 30 && difference < 360 - 30) {
                increaseBuffer();
                if (buffer >= 6) {
                    fail();
                }
            } else
                decreaseBufferBy(0.05);

        } else if (getData().isLastOnGround()) {
            deltaXOffGround = getData().getDeltaX();
            deltaZOffGround = getData().getDeltaZ();
        }
    }
}
