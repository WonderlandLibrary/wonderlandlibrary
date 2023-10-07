package dev.rise.anticheat.check.impl.combat.keepsprint;

import dev.rise.anticheat.check.Check;
import dev.rise.anticheat.check.api.CheckInfo;
import dev.rise.anticheat.data.PlayerData;
import dev.rise.util.misc.EvictingList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S14PacketEntity;

@CheckInfo(name = "KeepSprint", type = "A", description = "Detects sprinting whilst attacking")
public final class KeepSprintA extends Check {

    private final EvictingList<Double> beforeAttackingSamples = new EvictingList<>(4);
    private final EvictingList<Double> attackingSamples = new EvictingList<>(4);
    private int ticksSinceAttack;
    private int ticksSinceSwing;

    public KeepSprintA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet<?> packet) {
        if (packet instanceof S0BPacketAnimation) {
            final S0BPacketAnimation wrapper = ((S0BPacketAnimation) packet);

            /*
             * Check if the packet incoming has the data for our player we are currently checking.
             * We can do this by comparing the entity id on the packet itself and our PlayerData object.
             */
            if (wrapper.getEntityID() != data.getPlayer().getEntityId()) return;

            /*
             * Arm animations id is 0, so we are checking if the animation
             * type is 0, and then we can start running the math for it.
             */
            if (wrapper.getAnimationType() == 0) {
                ticksSinceSwing = 0;
            }
        }

        if (packet instanceof S14PacketEntity) {
            final S14PacketEntity wrapper = ((S14PacketEntity) packet);
            if (data.getPlayer().getEntityId() != wrapper.entityId) return;

            ticksSinceAttack++;
            ticksSinceSwing++;
            final double speed = Math.hypot(data.getDeltaX(), data.getDeltaZ());

            if (ticksSinceSwing <= 2) {
                for (final EntityPlayer player : mc.theWorld.playerEntities) {
                    if (player.hurtTime >= 7 && player != data.getPlayer() && player.getDistanceToEntity(data.getPlayer()) < 9) {
                        ticksSinceAttack = 0;
                    }
                }
            }

            if (ticksSinceAttack <= 5) {
                attackingSamples.add(speed);
            } else {
                beforeAttackingSamples.add(speed);
            }

            if (ticksSinceAttack >= 9) return;

            final double average = averageSpeed(beforeAttackingSamples);
            final double averageAttacking = averageSpeed(attackingSamples);
            final double diff = averageAttacking - average;
            if (diff > 0.15 && average > 0.24 && averageAttacking != 0 && beforeAttackingSamples.size() >= 4 && attackingSamples.size() >= 4 && data.isOnGround()) {
                increaseBuffer();
                if (buffer >= 15) {
                    fail();
                }
            } else {
                decreaseBufferBy(1);
            }
        }
    }

    private double averageSpeed(final EvictingList<Double> list) {
        double combined = 0;
        for (final double speed : list) {
            combined += speed;
        }
        return combined / list.size();
    }
}
