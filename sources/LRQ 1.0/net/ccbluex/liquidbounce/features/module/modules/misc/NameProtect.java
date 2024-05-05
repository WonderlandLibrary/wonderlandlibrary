/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmField
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.misc;

import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.INetworkPlayerInfo;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.TextEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.file.configs.FriendsConfig;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.StringUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.TextValue;

@ModuleInfo(name="NameProtect", description="Changes playernames clientside.", category=ModuleCategory.MISC)
public final class NameProtect
extends Module {
    @JvmField
    public final BoolValue allPlayersValue = new BoolValue("AllPlayers", false);
    @JvmField
    public final BoolValue skinProtectValue = new BoolValue("SkinProtect", true);
    private final TextValue fakeNameValue = new TextValue("FakeName", "&cMe");

    @EventTarget(ignoreCondition=true)
    public final void onText(TextEvent event) {
        IEntityPlayerSP thePlayer;
        block9: {
            block8: {
                thePlayer = MinecraftInstance.mc.getThePlayer();
                if (thePlayer == null) break block8;
                String string = event.getText();
                if (string == null) {
                    Intrinsics.throwNpe();
                }
                if (!string.equals("\u00a78[\u00a79\u00a7lLRQ\u00a78] \u00a73")) break block9;
            }
            return;
        }
        for (FriendsConfig.Friend friend : LiquidBounce.INSTANCE.getFileManager().friendsConfig.getFriends()) {
            event.setText(StringUtils.replace(event.getText(), friend.getPlayerName(), ColorUtils.translateAlternateColorCodes(friend.getAlias()) + "\u00a7f"));
        }
        if (!this.getState()) {
            return;
        }
        event.setText(StringUtils.replace(event.getText(), thePlayer.getName(), ColorUtils.translateAlternateColorCodes((String)this.fakeNameValue.get()) + "\u00a7f"));
        if (((Boolean)this.allPlayersValue.get()).booleanValue()) {
            for (INetworkPlayerInfo playerInfo : MinecraftInstance.mc.getNetHandler().getPlayerInfoMap()) {
                event.setText(StringUtils.replace(event.getText(), playerInfo.getGameProfile().getName(), "Protected User"));
            }
        }
    }
}

