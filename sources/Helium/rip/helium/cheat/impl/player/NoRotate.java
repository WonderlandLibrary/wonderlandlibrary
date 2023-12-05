package rip.helium.cheat.impl.player;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.ProcessPacketEvent;

public class NoRotate extends Cheat {

    public NoRotate() {
        super("No Rotate", " ", CheatCategory.PLAYER);
    }

    @Collect
    public void onProcess(ProcessPacketEvent e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) e.getPacket();
            packet.setYaw(mc.thePlayer.rotationYaw);
            packet.setPitch(mc.thePlayer.rotationPitch);

        }
    }
}
