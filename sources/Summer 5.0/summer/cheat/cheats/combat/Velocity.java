package summer.cheat.cheats.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import summer.Summer;
import summer.base.manager.Selection;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.client.EventPacket;
import summer.base.manager.config.Cheats;
import summer.cheat.guiutil.Setting;

import java.util.ArrayList;

public class Velocity extends Cheats {
    public Minecraft mc = Minecraft.getMinecraft();
    private Setting velocityMode;
    private Setting horizontal;
    private Setting vertical;

    public Velocity() {
        super("Velocity", "What's knockback?", Selection.COMBAT, false);
        ArrayList velocityType = new ArrayList();
        velocityType.add("Hypixel");
        velocityType.add("Percentage");
        Summer.INSTANCE.settingsManager.Property(velocityMode = new Setting("Mode", this, "Hypixel", velocityType));
        Summer.INSTANCE.settingsManager.Property(horizontal = new Setting("Horizontal", this, 0, 0, 100, true, () -> velocityMode.getValString().equalsIgnoreCase("Percentage")));
        Summer.INSTANCE.settingsManager.Property(vertical = new Setting("Vertical", this, 0, 0, 100, true, () -> velocityMode.getValString().equalsIgnoreCase("Percentage")));
    }

    @EventTarget
    public void onPacket(EventPacket event) {
        if (velocityMode.getValString().equalsIgnoreCase("Hypixel")) {
            setDisplayName("Velocity\2477 Hypixel");
            if (!event.isSending()) {
                if ((event.getPacket() instanceof S12PacketEntityVelocity)) {
                    event.setCancelled(true);
                }
                if (event.getPacket() instanceof S27PacketExplosion) {
                    event.setCancelled(true);
                }
            }
        }
        if (velocityMode.getValString().equalsIgnoreCase("Percentage")) {
            setDisplayName("Velocity\2477 " + horizontal.getValFloat() + "% "
                    + this.vertical.getValFloat() + "%");
            if (event.getPacket() instanceof S12PacketEntityVelocity) {
                S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
                if (packet.entityID != Minecraft.thePlayer.getEntityId()) {
                    return;
                }
                event.setCancelled(true);

                double x = packet.getMotionX() / 8000.0D;
                double y = packet.getMotionY() / 8000.0D;
                double z = packet.getMotionZ() / 8000.0D;

                double horizontalMultiplier = horizontal.getValFloat() / 100.0D;
                double verticalMultiplier = vertical.getValFloat() / 100.0D;

                x *= horizontalMultiplier;
                y *= verticalMultiplier;
                z *= horizontalMultiplier;

                if (horizontal.getValFloat() == 0 && vertical.getValFloat() == 0) {
                    return;
                }
                Minecraft.thePlayer.motionX = x;
                Minecraft.thePlayer.motionY = y;
                Minecraft.thePlayer.motionZ = z;
            }
        }
    }
}
