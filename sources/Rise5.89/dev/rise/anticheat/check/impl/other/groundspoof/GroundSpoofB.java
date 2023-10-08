package dev.rise.anticheat.check.impl.other.groundspoof;

import dev.rise.anticheat.check.Check;
import dev.rise.anticheat.check.api.CheckInfo;
import dev.rise.anticheat.data.PlayerData;
import dev.rise.anticheat.util.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;

@CheckInfo(name = "GroundSpoof", type = "B", description = "Detects invalid ground state on ground")
public final class GroundSpoofB extends Check {

    public GroundSpoofB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet<?> packet) {
        if (PacketUtil.isRelMove(packet) && !getData().getPlayer().isRiding() && getData().getTicksSinceLastTeleport() > 50 && !PlayerUtil.isOnServer("Hypixel")) { // Disables the check on Hypixel since ground states are hidden on there.
            final S14PacketEntity wrapper = ((S14PacketEntity) packet);

            if (data.getPlayer().getEntityId() != wrapper.entityId) return;

            final double x = data.getX();
            final double y = data.getY();
            final double z = data.getZ();

            final boolean server = !(PlayerUtil.getBlock(x - 0.5, y - 0.52, z - 0.5) instanceof BlockAir)
                    || !(PlayerUtil.getBlock(x + 0.5, y - 0.52, z - 0.5) instanceof BlockAir)
                    || !(PlayerUtil.getBlock(x + 0.5, y - 0.52, z + 0.5) instanceof BlockAir)
                    || !(PlayerUtil.getBlock(x - 0.5, y - 0.52, z + 0.5) instanceof BlockAir);

            final boolean noGround = !wrapper.onGround && server && y % 0.5 == 0.0 && data.getDeltaY() == 0.0;

            if (noGround) {
                if (increaseBuffer() > 2) {
                    this.fail();
                }
            } else {
                decreaseBufferBy(0.1);
            }
        }
    }
}
