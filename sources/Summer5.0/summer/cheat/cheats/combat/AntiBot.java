package summer.cheat.cheats.combat;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import summer.Summer;
import summer.base.manager.CheatManager;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.cheat.guiutil.Setting;

public class AntiBot extends Cheats {
    public static Setting ticks;

    public AntiBot() {
        super("Antibot", "Removes the bots so you don't hit them", Selection.COMBAT, false);
        Summer.INSTANCE.settingsManager.Property(ticks = new Setting("Ticks Existed", this, 8.50, 0, 10, false));
        this.setToggled(true);
    }

    public static boolean isBot(EntityPlayer player) {
        if (!CheatManager.getInstance(AntiBot.class).isToggled()) {
            return false;
        }
        if (player.getGameProfile() == null) {
            return true;
        }
        NetworkPlayerInfo npi = mc.getNetHandler().getPlayerInfo(player.getGameProfile().getId());
        return (npi == null || npi.getGameProfile() == null || player.ticksExisted < ticks.getValInt() || npi.getResponseTime() != 1);
    }
}