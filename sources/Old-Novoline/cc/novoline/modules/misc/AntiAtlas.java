package cc.novoline.modules.misc;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.LoadWorldEvent;
import cc.novoline.events.events.PacketEvent;
import cc.novoline.events.events.PlayerUpdateEvent;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.utils.Timer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class AntiAtlas extends AbstractModule {

    private List<UUID> reported = new CopyOnWriteArrayList();
    private Timer timer = new Timer();

    public AntiAtlas(@NotNull ModuleManager novoline) {
        super(novoline, EnumModuleType.MISC, "AntiAtlas", "Anti Atlas");
    }

    @EventTarget
    public void onUpdate(PlayerUpdateEvent event) {
        for (NetworkPlayerInfo playerInfo : mc.getNetHandler().getPlayerInfoMap()) {
            UUID uuid = playerInfo.getGameProfile().getId();
            String name = playerInfo.getGameProfile().getName();

            if (timer.delay(5000) && !reported.contains(uuid) && !uuid.equals(mc.player.getEntityUniqueID())) {
                sendPacketNoEvent(new C01PacketChatMessage("/report " + name + " killaura fly speed"));
                reported.add(uuid);
                timer.reset();
            }
        }
    }

    @EventTarget
    public void onWorldLoad(LoadWorldEvent event) {
        reported.clear();
        timer.reset();
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.INCOMING)) {
            if (event.getPacket() instanceof S02PacketChat) {
                S02PacketChat packet = (S02PacketChat) event.getPacket();

                if (packet.getChatComponent().getFormattedText().startsWith("\u00A7cThere is no player named")) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
