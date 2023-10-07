package dev.rise.anticheat.check.impl.movement.other;

import dev.rise.Rise;
import dev.rise.anticheat.check.Check;
import dev.rise.anticheat.check.api.CheckInfo;
import dev.rise.anticheat.data.PlayerData;
import dev.rise.anticheat.util.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;

import javax.vecmath.Vector2d;

@CheckInfo(name = "Onmisprint", type = "A", description = "Detects sprinting in impossible directions")
public class Omnisprint extends Check {

    public Omnisprint(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet<?> packet) {
        if (PacketUtil.isRelMove(packet)) {

            Vector2d delta = new Vector2d(data.getDeltaX(), data.getDeltaZ());
            Vector2d lastDelta = new Vector2d(data.getLastDeltaX(), data.getLastDeltaZ());

            if (Math.round(delta.length() * 100000D) / 100000D == Math.round(lastDelta.length() * 100000D) / 100000D && delta.length() > 0.5 && data.isOnGround() && data.isLastOnGround()) {

                double moveAngle = Math.toDegrees(delta.angle(new Vector2d(0, 1)));

                if (delta.getX() > 0) moveAngle *= -1;

                if (Math.abs(data.getYaw() * (360 / 256D)) - Math.abs(moveAngle) > 90 && increaseBuffer() > 4) {
                    fail();
                } else {
                    decreaseBufferBy(0.5);
                }
            }
        }
    }
}
