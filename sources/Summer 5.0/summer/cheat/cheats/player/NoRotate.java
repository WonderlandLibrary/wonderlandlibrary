package summer.cheat.cheats.player;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import summer.base.manager.Selection;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.client.EventPacket;
import summer.base.manager.config.Cheats;

public class NoRotate extends Cheats {
    public Minecraft mc = Minecraft.getMinecraft();

    public NoRotate() {
        super("NoRotate", "Yes", Selection.PLAYER);
    }

    @EventTarget
    public void onPacket(EventPacket e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) e.getPacket();
            packet.yaw = Minecraft.thePlayer.rotationYaw;
            packet.pitch = Minecraft.thePlayer.rotationPitch;
        }
    }
}