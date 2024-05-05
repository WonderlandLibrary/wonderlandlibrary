package club.cloverhook.cheat.impl.player;

import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.event.minecraft.ProcessPacketEvent;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.util.Objects;

public class NoRotate extends Cheat {

    public NoRotate() {
        super("NoRotation", "Lmao ez hypickle bypasses", CheatCategory.PLAYER);
    }

    @Collect
    public void onProcess(ProcessPacketEvent e) {
        if (Objects.nonNull(mc.thePlayer)) {
            if (e.getPacket() instanceof S08PacketPlayerPosLook) {
                S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) e.getPacket();
                packet.setYaw(mc.thePlayer.rotationYaw);
                packet.setPitch(mc.thePlayer.rotationPitch);
            }
        }
    }
}
