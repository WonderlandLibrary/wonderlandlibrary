package today.flux.module.implement.Combat;

import com.darkmagician6.eventapi.EventTarget;
import com.soterdev.SoterObfuscator;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import today.flux.event.PacketReceiveEvent;
import today.flux.event.PacketSendEvent;
import today.flux.event.PreUpdateEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.FloatValue;
import today.flux.module.value.ModeValue;
import today.flux.utility.ChatUtils;
import today.flux.utility.PlayerUtils;

public class Velocity extends Module {
    public static ModeValue mode = new ModeValue("Velocity", "Mode", "Packet", "AAC3", "AAC4");

    public static FloatValue horizontal = new FloatValue("Velocity", "Horizontal", 0.0f, -100.0f, 100.0f, 1.0f, "%");
    public static FloatValue vertical = new FloatValue("Velocity", "Vertical", 0.0f, 0.0f, 100.0f, 1.0f, "%");

    public Velocity() {
        super("Velocity", Category.Combat, mode);
    }

    @EventTarget
    public void onPacketReceive(PacketReceiveEvent event) {
        if (mode.getValue().equals("Packet")) {
            if (event.getPacket() instanceof S12PacketEntityVelocity) {
                S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();

                if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
                    if(horizontal.getValue() == 0f && vertical.getValue() == 0f){
                        event.setCancelled(true);
                    }
                    else {
                        packet.setMotionX((int) (packet.getMotionX() * horizontal.getValue() / 100.0));
                        packet.setMotionY((int) (packet.getMotionY() * vertical.getValue() / 100.0));
                        packet.setMotionZ((int) (packet.getMotionZ() * horizontal.getValue() / 100.0));
                    }
                }
            }

            //hypixel fucker
            if (event.getPacket() instanceof S27PacketExplosion) {
                handle(event);
            }
        }
    }

    @SoterObfuscator.Obfuscation(flags = "+native")
    public void handle(PacketReceiveEvent event) {
        S27PacketExplosion packet = (S27PacketExplosion) event.getPacket();

        if(horizontal.getValue() == 0f && vertical.getValue() == 0f){
            event.setCancelled(true);
        }
        else {
            packet.setMotionX(packet.getMotionX() * horizontal.getValue() / 100.0f);
            packet.setMotionY(packet.getMotionY() * vertical.getValue() / 100.0f);
            packet.setMotionZ(packet.getMotionZ() * horizontal.getValue() / 100.0f);
        }
    }

    @EventTarget
    public void onUpdate(PreUpdateEvent e) {
        if (mode.getValue().equals("AAC3")) {
            if (mc.thePlayer.hurtTime > 0 && mc.thePlayer.fallDistance < 3) {
                if (mc.thePlayer.moveForward == 0 && mc.thePlayer.moveStrafing == 0) {
                    mc.thePlayer.motionY -= 1.001D;
                    mc.thePlayer.motionX *= 0.2D;
                    mc.thePlayer.motionZ *= 0.2D;
                    mc.thePlayer.motionY += 1D;
                } else {
                    mc.thePlayer.motionX *= 0.5D;
                    mc.thePlayer.motionZ *= 0.5D;
                }

                ChatUtils.debug("Velocity!");
            }
        } else if (mode.getValue().equals("AAC4")) {
            if (mc.thePlayer.hurtTime > 0 && PlayerUtils.isMoving()) {
                mc.thePlayer.motionX *= 0.6;
                mc.thePlayer.motionZ *= 0.6;
            }
        }
    }

    @EventTarget
    public void onPacketSend(PacketSendEvent event) {
        if (!mode.getValue().equals("AAC"))
            return;

        if (event.getPacket() instanceof C02PacketUseEntity && mc.thePlayer.movementInput.moveForward == 1) {
            final C02PacketUseEntity packet = (C02PacketUseEntity) event.getPacket();
            if (packet.getAction() == C02PacketUseEntity.Action.ATTACK && mc.thePlayer.onGround) {
                mc.thePlayer.setSprinting(true);
                ChatUtils.debug("WTapped");
            }
        }
    }
}
