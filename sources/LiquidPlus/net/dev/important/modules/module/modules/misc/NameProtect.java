/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.NetworkPlayerInfo
 */
package net.dev.important.modules.module.modules.misc;

import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.TextEvent;
import net.dev.important.file.configs.FriendsConfig;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.misc.StringUtils;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.TextValue;
import net.minecraft.client.network.NetworkPlayerInfo;

@Info(name="NameProtect", spacedName="Name Protect", description="Changes playernames clientside.", category=Category.MISC, cnName="\u5047\u7684\u540d\u5b57")
public class NameProtect
extends Module {
    private final TextValue fakeNameValue = new TextValue("FakeName", "&cMe");
    private final TextValue allFakeNameValue = new TextValue("AllPlayersFakeName", "Censored");
    public final BoolValue selfValue = new BoolValue("Yourself", true);
    public final BoolValue tagValue = new BoolValue("Tag", false);
    public final BoolValue allPlayersValue = new BoolValue("AllPlayers", false);
    public final BoolValue skinProtectValue = new BoolValue("SkinProtect", false);

    @EventTarget(ignoreCondition=true)
    public void onText(TextEvent event) {
        if (NameProtect.mc.field_71439_g == null || event.getText().contains("\u00a78[\u00a79\u00a7lLiquidPlus\u00a78] \u00a73") || event.getText().startsWith("/") || event.getText().startsWith(Client.commandManager.getPrefix() + "")) {
            return;
        }
        if (!this.getState()) {
            return;
        }
        for (FriendsConfig.Friend friend : Client.fileManager.friendsConfig.getFriends()) {
            event.setText(StringUtils.replace(event.getText(), friend.getPlayerName(), ColorUtils.translateAlternateColorCodes(friend.getAlias()) + "\u00a7f"));
        }
        event.setText(StringUtils.replace(event.getText(), NameProtect.mc.field_71439_g.func_70005_c_(), ((Boolean)this.selfValue.get()).booleanValue() ? (((Boolean)this.tagValue.get()).booleanValue() ? StringUtils.injectAirString(NameProtect.mc.field_71439_g.func_70005_c_()) + " \u00a77(\u00a7r" + ColorUtils.translateAlternateColorCodes((String)this.fakeNameValue.get() + "\u00a7r\u00a77)") : ColorUtils.translateAlternateColorCodes((String)this.fakeNameValue.get()) + "\u00a7r") : NameProtect.mc.field_71439_g.func_70005_c_()));
        if (((Boolean)this.allPlayersValue.get()).booleanValue()) {
            for (NetworkPlayerInfo playerInfo : mc.func_147114_u().func_175106_d()) {
                event.setText(StringUtils.replace(event.getText(), playerInfo.func_178845_a().getName(), ColorUtils.translateAlternateColorCodes((String)this.allFakeNameValue.get()) + "\u00a7f"));
            }
        }
    }
}

