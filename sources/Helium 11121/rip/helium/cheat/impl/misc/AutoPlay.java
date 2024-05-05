package rip.helium.cheat.impl.misc;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.play.server.S02PacketChat;
import rip.helium.cheat.Cheat;
import rip.helium.event.minecraft.ProcessPacketEvent;
import rip.helium.notification.mgmt.NotificationManager;
import rip.helium.utils.Mafs;
import rip.helium.utils.Stopwatch;
import rip.helium.utils.property.impl.StringsProperty;

import java.util.Objects;

public class AutoPlay extends Cheat {

    public static StringsProperty mode = new StringsProperty("Mode", "Auto joins a game", null, false, true, new String[]{"Doubles", "Solo"}, new Boolean[]{false, true});

    public AutoPlay() {
        super("AutoPlay", "Auto plays the next skywars game.");
        registerProperties(mode);
    }

    @Collect
    public void onproc(ProcessPacketEvent e) {
        if (e.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = (S02PacketChat) e.getPacket();
            if (packet.getChatComponent().getUnformattedText().contains("here!") || mc.thePlayer.isSpectator()) {
                if (!(mc.currentScreen instanceof GuiDownloadTerrain) || Objects.nonNull(mc.thePlayer)) {
                    Stopwatch timer = new Stopwatch();
                    boolean solo = AutoPlay.mode.getValue().get("Solo");
                    if (timer.hasPassed(Mafs.getRandomInRange(4000, 5000))) {
                        if (solo) {
                            mc.thePlayer.sendChatMessage("/play solo_insane");
                            NotificationManager.postInfo("AutoPlay", "Joined a solo insane match.");
                        } else {
                            mc.thePlayer.sendChatMessage("/play doubles_insane");
                            NotificationManager.postInfo("AutoPlay", "Joined a doubles insane match.");
                        }
                    }
                }
            }
        }
    }
}
