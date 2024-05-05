package io.github.nevalackin.client.module.movement.extras;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.packet.ReceivePacketEvent;
import io.github.nevalackin.client.event.player.MoveEvent;
import io.github.nevalackin.client.event.player.UpdatePositionEvent;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.Vec3;

public final class Flight extends Module {

    private boolean damageTick;
    private boolean wasOnGround;

    private int extraSendPackets;

    private int onGroundTicks;

    private Vec3 velocity;

    public Flight() {
        super("Flight", Category.MOVEMENT, Category.SubCategory.MOVEMENT_EXTRAS);
    }

    @EventLink
    private final Listener<ReceivePacketEvent> onReceivePacket = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof S12PacketEntityVelocity) {
            final S12PacketEntityVelocity velocityPacket = (S12PacketEntityVelocity) packet;

            if (velocityPacket.getEntityID() == this.mc.thePlayer.getEntityId()) {
                this.velocity = new Vec3(velocityPacket.getMotionX() / 8000.0,
                                         velocityPacket.getMotionY() / 8000.0,
                                         velocityPacket.getMotionZ() / 8000.0);
            }
        } else if (packet instanceof S27PacketExplosion) {
            final S27PacketExplosion explosion = (S27PacketExplosion) packet;

            this.velocity = new Vec3(explosion.getMotionX(), explosion.getMotionY(), explosion.getMotionZ());
        }
    };

    @EventLink
    private final Listener<UpdatePositionEvent> onUpdatePosition = event -> {
        if (event.isPre()) {
            if (this.extraSendPackets != 0) {
                if (this.extraSendPackets < 0) {
                    this.extraSendPackets = this.sendDamagePackets(false);
                } else {
                    this.extraSendPackets -= 3;
                    if (this.extraSendPackets < 0)
                        this.extraSendPackets = 0;
                }
                event.setCancelled();
                return;
            }

            final boolean onGround = this.mc.thePlayer.onGround && this.mc.thePlayer.isCollidedVertically;

            if (this.damageTick && onGround) {
                this.sendDamagePackets(true);
                this.damageTick = false;
            }

            if (this.velocity == null) return;

            if (onGround) {
                ++this.onGroundTicks;

                if (this.onGroundTicks > 1) {
                    this.toggle();
                    return;
                }
            }

            event.setPosY(Math.round(event.getPosY() / 0.015625F) * 0.015625F);

            if (this.mc.thePlayer.ticksExisted % 2 == 0) {
                event.setPosY(event.getPosY() + 0.015625F);
            }

            event.setOnGround(this.mc.thePlayer.onGround = true);

            this.mc.thePlayer.motionY = 0.0;
        }
    };

    @EventLink
    private final Listener<MoveEvent> onMove = event -> {
        if (this.extraSendPackets > 0 || this.velocity == null) {
            // Dont move while waiting for packets to send
            event.setX(0.0);
            event.setZ(0.0);
        }
    };

    private int sendDamagePackets(final boolean send) {
        final double startJumpMotion = 0.42f;
        final double groundOffset = 0.0005f;

        final double requiredFallDist = 3.0; // TODO :: Use jump boots pot
        double fallDistance = 0.0;

        int packetCounter = 0;

        double lastSentOffset = -1; // Start at -1 so < always false

        double motion = startJumpMotion;
        double position = 0.0;

        while (fallDistance < requiredFallDist) {
            while (true) {
                if (Math.abs(motion) < 0.005) motion = 0.0;
                // Accumulate position
                position += motion;
                if (position < 0.0) break;
                // Send packet
                if (send)
                    this.mc.thePlayer.sendQueue.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
                        this.mc.thePlayer.posX,
                        this.mc.thePlayer.posY + position,
                        this.mc.thePlayer.posZ,
                        false
                    ));
                ++packetCounter;
                // Update fall distance
                if (position < lastSentOffset)
                    fallDistance += lastSentOffset - position;
                // Update last offset
                lastSentOffset = position;
                // Apply gravity logic
                motion -= 0.08;
                motion *= 0.98F;
            }

            // Send ground spoof packet
            if (send)
                this.mc.thePlayer.sendQueue.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
                    this.mc.thePlayer.posX,
                    this.mc.thePlayer.posY + groundOffset,
                    this.mc.thePlayer.posZ,
                    false
                ));
            ++packetCounter;
            // Update fall distance
            if (groundOffset < lastSentOffset)
                fallDistance += lastSentOffset - groundOffset;
            position = 0.0;
            motion = startJumpMotion;
            lastSentOffset = groundOffset;
        }

        return packetCounter;
    }

    @Override
    public void onEnable() {
        this.wasOnGround = false;
        this.damageTick = true;
        this.extraSendPackets = -1;
        this.velocity = null;
        this.onGroundTicks = 0;
    }

    @Override
    public void onDisable() {

    }
}
