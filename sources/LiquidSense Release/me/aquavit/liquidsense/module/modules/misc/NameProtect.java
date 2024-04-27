package me.aquavit.liquidsense.module.modules.misc;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.TextEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.file.configs.FriendsConfig;
import me.aquavit.liquidsense.utils.misc.StringUtils;
import me.aquavit.liquidsense.utils.render.ColorUtils;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.TextValue;
import net.minecraft.client.network.NetworkPlayerInfo;

@ModuleInfo(name = "NameProtect", description = "Changes playernames clientside.", category = ModuleCategory.MISC)
public class NameProtect extends Module {
    private final TextValue fakeNameValue = new TextValue("FakeName", "&cMe");
    public final BoolValue allPlayersValue = new BoolValue("AllPlayers", false);
    public final BoolValue skinProtectValue = new BoolValue("SkinProtect", true);

    @EventTarget(ignoreCondition = true)
    public void onText(final TextEvent event) {
        if(mc.thePlayer == null || event.getText().contains("§8[§9§l" + LiquidSense.CLIENT_NAME + "§8] §3"))
            return;

        for (final FriendsConfig.Friend friend : LiquidSense.fileManager.friendsConfig.getFriends())
            event.setText(StringUtils.replace(event.getText(), friend.getPlayerName(), ColorUtils.translateAlternateColorCodes(friend.getAlias()) + "§f"));

        if(!getState())
            return;

        event.setText(StringUtils.replace(event.getText(), mc.thePlayer.getName(), ColorUtils.translateAlternateColorCodes(fakeNameValue.get()) + "§f"));

        if(allPlayersValue.get())
            for(final NetworkPlayerInfo playerInfo : mc.getNetHandler().getPlayerInfoMap())
                event.setText(StringUtils.replace(event.getText(), playerInfo.getGameProfile().getName(), "Protected User"));
    }
}
