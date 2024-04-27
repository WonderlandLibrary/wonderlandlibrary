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
    private final TextValue fakeNameValue;
    @JvmField
    public final BoolValue skinProtectValue;
    @JvmField
    public final BoolValue allPlayersValue = new BoolValue("AllPlayers", false);

    @EventTarget(ignoreCondition=true)
    public final void onText(TextEvent textEvent) {
        IEntityPlayerSP iEntityPlayerSP;
        block9: {
            block8: {
                iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) break block8;
                String string = textEvent.getText();
                if (string == null) {
                    Intrinsics.throwNpe();
                }
                if (!string.equals("\u00a78[\u00a79\u00a7lAtField\u00a78] \u00a73")) break block9;
            }
            return;
        }
        for (Object object : LiquidBounce.INSTANCE.getFileManager().friendsConfig.getFriends()) {
            textEvent.setText(StringUtils.replace(textEvent.getText(), ((FriendsConfig.Friend)object).getPlayerName(), ColorUtils.translateAlternateColorCodes(((FriendsConfig.Friend)object).getAlias()) + "\u00a7f"));
        }
        if (!this.getState()) {
            return;
        }
        textEvent.setText(StringUtils.replace(textEvent.getText(), iEntityPlayerSP.getName(), ColorUtils.translateAlternateColorCodes((String)this.fakeNameValue.get()) + "\u00a7f"));
        if (((Boolean)this.allPlayersValue.get()).booleanValue()) {
            for (Object object : MinecraftInstance.mc.getNetHandler().getPlayerInfoMap()) {
                textEvent.setText(StringUtils.replace(textEvent.getText(), object.getGameProfile().getName(), "Protected User"));
            }
        }
    }

    public NameProtect() {
        this.skinProtectValue = new BoolValue("SkinProtect", true);
        this.fakeNameValue = new TextValue("FakeName", "&cMe");
    }
}

