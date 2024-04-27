package net.ccbluex.liquidbounce.features.module.modules.misc;

import kotlin.Metadata;
import kotlin.jvm.JvmField;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
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
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="NameProtect", description="Changes playernames clientside.", category=ModuleCategory.MISC)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\b\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J\b0\t2\n0HR08X¢\n\u0000R0X¢\n\u0000R08X¢\n\u0000¨\f"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/misc/NameProtect;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "allPlayersValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "fakeNameValue", "Lnet/ccbluex/liquidbounce/value/TextValue;", "skinProtectValue", "onText", "", "event", "Lnet/ccbluex/liquidbounce/event/TextEvent;", "Pride"})
public final class NameProtect
extends Module {
    @JvmField
    @NotNull
    public final BoolValue allPlayersValue = new BoolValue("AllPlayers", false);
    @JvmField
    @NotNull
    public final BoolValue skinProtectValue = new BoolValue("SkinProtect", true);
    private final TextValue fakeNameValue = new TextValue("FakeName", "&cMe");

    @EventTarget(ignoreCondition=true)
    public final void onText(@NotNull TextEvent event) {
        IEntityPlayerSP thePlayer;
        block9: {
            block8: {
                Intrinsics.checkParameterIsNotNull(event, "event");
                thePlayer = MinecraftInstance.mc.getThePlayer();
                if (thePlayer == null) break block8;
                String string = event.getText();
                if (string == null) {
                    Intrinsics.throwNpe();
                }
                if (!StringsKt.contains$default((CharSequence)string, "§8[§9§lRedStar§8] §3", false, 2, null)) break block9;
            }
            return;
        }
        FriendsConfig friendsConfig = LiquidBounce.INSTANCE.getFileManager().friendsConfig;
        Intrinsics.checkExpressionValueIsNotNull(friendsConfig, "LiquidBounce.fileManager.friendsConfig");
        for (FriendsConfig.Friend friend : friendsConfig.getFriends()) {
            String string = event.getText();
            FriendsConfig.Friend friend2 = friend;
            Intrinsics.checkExpressionValueIsNotNull(friend2, "friend");
            String string2 = friend2.getPlayerName();
            StringBuilder stringBuilder = new StringBuilder();
            String string3 = friend.getAlias();
            Intrinsics.checkExpressionValueIsNotNull(string3, "friend.alias");
            event.setText(StringUtils.replace(string, string2, stringBuilder.append(ColorUtils.translateAlternateColorCodes(string3)).append("§f").toString()));
        }
        if (!this.getState()) {
            return;
        }
        event.setText(StringUtils.replace(event.getText(), thePlayer.getName(), ColorUtils.translateAlternateColorCodes((String)this.fakeNameValue.get()) + "§f"));
        if (((Boolean)this.allPlayersValue.get()).booleanValue()) {
            for (INetworkPlayerInfo playerInfo : MinecraftInstance.mc.getNetHandler().getPlayerInfoMap()) {
                event.setText(StringUtils.replace(event.getText(), playerInfo.getGameProfile().getName(), "Protected User"));
            }
        }
    }
}
