package koks.module.misc;

import god.buddy.aot.BCompiler;
import koks.Koks;
import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.event.PacketEvent;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.event.ClickEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Module.Info(name = "AntiVanish", description = "You can see if a player is vanished", category = Module.Category.MISC)
public class AntiVanish extends Module implements Module.NotBypass {

    public final HashMap<UUID, String> vanished = new HashMap<>();
    public final List<UUID> uuids = new ArrayList<>();
    public final CopyOnWriteArrayList<NetworkPlayerInfo> playerInfo = new CopyOnWriteArrayList<>();

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof final PacketEvent packetEvent) {
            final Packet<?> packet = packetEvent.getPacket();
            if (packet instanceof final S38PacketPlayerListItem s38PacketPlayerListItem) {
                if (s38PacketPlayerListItem.func_179768_b() == S38PacketPlayerListItem.Action.UPDATE_LATENCY)
                    for (S38PacketPlayerListItem.AddPlayerData addPlayerData : s38PacketPlayerListItem.func_179767_a())
                        if (!mc.getNetHandler().playerInfoMap.containsKey(addPlayerData.getProfile().getId())) {
                            final ChatComponentText text = new ChatComponentText(Koks.prefix + "§aA player is vanished!");
                            text.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://de.namemc.com/profile/" + addPlayerData.getProfile().getId().toString()));
                            try {
                                getPlayer().addChatMessage(text);
                            } catch (ConcurrentModificationException e) {
                                e.printStackTrace();
                            }
                        }
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
