/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.NetworkPlayerInfo
 */
package net.ccbluex.liquidbounce.features.module.modules.fun;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.TextEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.file.configs.FriendsConfig;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.minecraft.client.network.NetworkPlayerInfo;

@ModuleInfo(name="NameProtect", description="Changes playernames clientside.", category=ModuleCategory.FUN)
public class NameProtect
extends Module {
    private final TextValue fakeNameValue = new TextValue("FakeName", "&cReport.");
    public final BoolValue allPlayersValue = new BoolValue("AllPlayers", false);
    public final BoolValue skinProtectValue = new BoolValue("SkinProtect", true);

    @EventTarget(ignoreCondition=true)
    public void onText(TextEvent event) {
        if (NameProtect.mc.field_71439_g == null || event.getText().contains("\u00a78[\u00a79\u00a7lReport.\u00a78] \u00a73")) {
            return;
        }
        for (FriendsConfig.Friend friend : LiquidBounce.fileManager.friendsConfig.getFriends()) {
            event.setText(StringUtils.replace(event.getText(), friend.getPlayerName(), ColorUtils.translateAlternateColorCodes(friend.getAlias()) + "\u00a7f"));
        }
        if (!this.getState()) {
            return;
        }
        event.setText(StringUtils.replace(event.getText(), NameProtect.mc.field_71439_g.func_70005_c_(), ColorUtils.translateAlternateColorCodes((String)this.fakeNameValue.get()) + "\u00a7f"));
        if (((Boolean)this.allPlayersValue.get()).booleanValue()) {
            for (NetworkPlayerInfo playerInfo : mc.func_147114_u().func_175106_d()) {
                event.setText(StringUtils.replace(event.getText(), playerInfo.func_178845_a().getName(), "Protected User"));
            }
        }
    }
}

