package wtf.evolution.module.impl.Misc;

import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.util.text.TextComponentString;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventPlayer;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(name = "PlayerLogger", type = Category.Misc)
public class PlayerLogger extends Module {

    @EventTarget
    public void onPacket(EventPlayer e) {
        if (e.getAction() == SPacketPlayerListItem.Action.UPDATE_GAME_MODE) {
            mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(("&8[&e/&8] &r" + mc.getConnection().getPlayerInfo(e.getPlayerData().getProfile().getId()).getDisplayName().getFormattedText() + "&8 → &e" + e.getPlayerData().gamemode.getName().toUpperCase()).replaceAll("&", "§")));
        }
    }

}
