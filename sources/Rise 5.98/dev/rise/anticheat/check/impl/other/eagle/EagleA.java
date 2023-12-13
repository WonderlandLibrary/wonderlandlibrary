package dev.rise.anticheat.check.impl.other.eagle;

import dev.rise.anticheat.check.Check;
import dev.rise.anticheat.check.api.CheckInfo;
import dev.rise.anticheat.data.PlayerData;
import dev.rise.anticheat.util.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.DataWatcher;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;

@CheckInfo(name = "Eagle", type = "A", description = "Detects Eagle")
public final class EagleA extends Check {

    private boolean shouldEagle;
    private int ticks;

    public EagleA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet<?> packet) {
        if (PacketUtil.isRelMove(packet)) {
            final S14PacketEntity wrapper = ((S14PacketEntity) packet);

            if (wrapper.onGround) {
                final Block block = PlayerUtil.getBlock(data.getX(), data.getY() - 1, data.getZ());

                if (block instanceof BlockAir) {
                    shouldEagle = true;
                    ticks++;
                } else {
                    shouldEagle = false;
                    ticks = 0;
                }
            } else {
                shouldEagle = false;
                ticks = 0;
            }
        }

        if (packet instanceof S1CPacketEntityMetadata) {
            final S1CPacketEntityMetadata wrapper = (S1CPacketEntityMetadata) packet;
            if (wrapper.func_149376_c() != null && wrapper.getEntityId() == data.getPlayer().getEntityId()) {
                for (final DataWatcher.WatchableObject object : wrapper.func_149376_c()) {
                    if (object.getObject() instanceof Byte && (byte) object.getObject() == 2) {
                        if (shouldEagle && ticks <= 2) {
                            if (increaseBuffer() > 10) {
                                fail();
                                shouldEagle = false;
                                ticks = 0;
                            }
                        } else
                            decreaseBufferBy(5);
                    }
                }
            }
        }
    }
}
