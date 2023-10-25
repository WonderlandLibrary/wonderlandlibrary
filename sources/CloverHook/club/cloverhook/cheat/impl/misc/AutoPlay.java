package club.cloverhook.cheat.impl.misc;

import club.cloverhook.Cloverhook;
import club.cloverhook.cheat.Cheat;
import club.cloverhook.event.minecraft.PlayerUpdateEvent;
import club.cloverhook.event.minecraft.ProcessPacketEvent;
import club.cloverhook.utils.Mafs;
import club.cloverhook.utils.Stopwatch;
import club.cloverhook.utils.property.impl.BooleanProperty;
import club.cloverhook.utils.property.impl.StringsProperty;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.network.play.server.S02PacketChat;

import java.util.Objects;

public class AutoPlay extends Cheat {

    public static StringsProperty mode = new StringsProperty("Mode", "Are you retarded?", null, false, true, new String[]{"Doubles", "Solo"}, new Boolean[]{false, true});

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
                        } else {
                            mc.thePlayer.sendChatMessage("/play doubles_insane");
                        }
                    }
                }
            }
        }
    }
}
