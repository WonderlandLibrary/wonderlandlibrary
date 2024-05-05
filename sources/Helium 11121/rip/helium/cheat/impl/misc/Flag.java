package rip.helium.cheat.impl.misc;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.ProcessPacketEvent;
import rip.helium.notification.mgmt.NotificationManager;

public class Flag extends Cheat {

    public Flag() {
        super("Flag Detector", "Detects when you flag", CheatCategory.MISC);
    }

    @Collect
    public void onproc(ProcessPacketEvent e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) e.getPacket();
            if (Helium.instance.cheatManager.isCheatEnabled("Speed") || Helium.instance.cheatManager.isCheatEnabled("Flight")) {
                NotificationManager.postWarning("Flag Detected!", "Flag/Teleport detected, disabling some mods...");
                Helium.instance.cheatManager.getCheatRegistry().get("Speed").setState(false, false);
                Helium.instance.cheatManager.getCheatRegistry().get("Flight").setState(false, false);
            }
        }
    }

}
