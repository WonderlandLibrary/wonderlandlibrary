package xyz.northclient.features.modules;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import xyz.northclient.draggable.impl.Watermark;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Category;
import xyz.northclient.features.EventLink;
import xyz.northclient.features.ModuleInfo;
import xyz.northclient.features.events.ReceivePacketEvent;
import xyz.northclient.features.events.SendPacketEvent;
import xyz.northclient.features.events.UpdateEvent;
import xyz.northclient.features.values.DoubleValue;
import xyz.northclient.features.values.ModeValue;

@ModuleInfo(name = "Velocity", description = "", category = Category.COMBAT)
public class Velocity extends AbstractModule {
    public ModeValue veloMode = new ModeValue("Mode", this)
            .add(new Watermark.StringMode("Custom", this))
            .add(new Watermark.StringMode("Jump", this))
            .add(new Watermark.StringMode("Intave", this))
            .add(new Watermark.StringMode("Watchdog", this))
            .add(new Watermark.StringMode("NCP", this))
            .add(new Watermark.StringMode("MMC", this))
            .setDefault("Custom");
    public final DoubleValue horizontalVelo = new DoubleValue("Custom H-Velo", this)
            .setDefault(0)
            .setMin(0)
            .setMax(100)
            .enableOnlyInt();
    public final DoubleValue verticalVelo = new DoubleValue("Custom V-Velo", this)
            .setDefault(100)
            .setMin(0)
            .setMax(100)
            .enableOnlyInt();

    public boolean hasAttacked;

    @EventLink
    public void onUpdate(UpdateEvent event) {
        if (veloMode.is("Custom")) {
            setSuffix(String.valueOf(horizontalVelo.get().floatValue()) + "% " + String.valueOf(verticalVelo.get().floatValue()) + "%");
        } else {
            setSuffix(veloMode.get().getName());
        }
    }

    @EventLink
    public void onPacketReceive(ReceivePacketEvent event) {
        Packet p = event.getPacket();

        if (p instanceof S12PacketEntityVelocity) {
            final S12PacketEntityVelocity packetVelocity = (S12PacketEntityVelocity) p;
            if (packetVelocity.getEntityID() == mc.thePlayer.getEntityId()) {
                switch (veloMode.get().getName()) {
                    case "Custom":
                        double horizontalVelocity = this.horizontalVelo.get().doubleValue();
                        double verticalVelocity = this.verticalVelo.get().doubleValue();
                        packetVelocity.motionX *= horizontalVelocity / 100.0;
                        packetVelocity.motionY *= verticalVelocity / 100.0;
                        packetVelocity.motionZ *= horizontalVelocity / 100.0;
                        break;
                    case "Jump":
                        if (mc.thePlayer.hurtTime != 9 || !mc.thePlayer.onGround)
                            return;

                        mc.thePlayer.movementInput.jump = true;
                        break;
                    case "Watchdog":
                        horizontalVelocity = this.horizontalVelo.get().doubleValue();
                        verticalVelocity = this.verticalVelo.get().doubleValue();
                        if (mc.thePlayer.onGround) {
                            packetVelocity.motionX *= horizontalVelocity / 0.0;
                            packetVelocity.motionY *= verticalVelocity / 100.0;
                            packetVelocity.motionZ *= horizontalVelocity / 0.0;
                        } else {
                            packetVelocity.motionX *= horizontalVelocity / 0.0;
                            packetVelocity.motionY *= verticalVelocity / 100.0;
                            packetVelocity.motionZ *= horizontalVelocity / 0.0;
                        }
                        break;
                    case "NCP":
                        horizontalVelocity = this.horizontalVelo.get().doubleValue();
                        verticalVelocity = this.verticalVelo.get().doubleValue();
                        if (mc.thePlayer.onGround) {
                            packetVelocity.motionX *= horizontalVelocity / 0.0;
                            packetVelocity.motionY *= verticalVelocity / 1000.0;
                            packetVelocity.motionZ *= horizontalVelocity / 0.0;
                        } else {
                            packetVelocity.motionX *= horizontalVelocity / 0.0;
                            packetVelocity.motionY *= verticalVelocity / 1000.0;
                            packetVelocity.motionZ *= horizontalVelocity / 0.0;
                        }
                        break;
                    case "MMC":
                        horizontalVelocity = this.horizontalVelo.get().doubleValue();
                        verticalVelocity = this.verticalVelo.get().doubleValue();
                        if (mc.thePlayer.onGround) {
                            packetVelocity.motionX *= horizontalVelocity / 0.0;
                            packetVelocity.motionY *= verticalVelocity / 100.0;
                            packetVelocity.motionZ *= horizontalVelocity / 0.0;
                        } else {
                            packetVelocity.motionX *= horizontalVelocity / 0.0;
                            packetVelocity.motionY *= verticalVelocity / 100.0;
                            packetVelocity.motionZ *= horizontalVelocity / 0.0;
                        }
                        break;
                    case "Intave":
                        if (!hasAttacked && mc.thePlayer.hurtTime > 0) {
                            mc.thePlayer.motionX *= 0.6;
                            mc.thePlayer.motionZ *= 0.6;
                            mc.thePlayer.setSprinting(false);
                        }

                        if (mc.thePlayer.hurtTime != 9 || !mc.thePlayer.onGround)
                            return;

                        mc.thePlayer.movementInput.jump = true;
                        hasAttacked = false;
                        
                        break;
                }
            }
        }
    }

    @EventLink
    public void sendPacket(SendPacketEvent e) {
        if (e.getPacket() instanceof C0APacketAnimation) {
            hasAttacked = true;
        }
    }
}
