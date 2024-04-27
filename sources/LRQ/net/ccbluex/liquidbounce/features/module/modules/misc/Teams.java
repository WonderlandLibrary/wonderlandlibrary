/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  net.minecraft.client.gui.GuiPlayerTabOverlay
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.entity.player.EntityPlayer
 */
package net.ccbluex.liquidbounce.features.module.modules.misc;

import jx.utils.CodeTool;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.client.network.INetworkPlayerInfo;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemArmor;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.ITeam;
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;

@ModuleInfo(name="Teams", description="\u4fee\u590d\u7248", category=ModuleCategory.MISC)
public final class Teams
extends Module {
    private final BoolValue scoreboardValue = new BoolValue("ScoreboardTeam", true);
    private final BoolValue colorValue = new BoolValue("Color", true);
    private final BoolValue gommeSWValue = new BoolValue("GommeSW", false);
    private final BoolValue armorColorValue = new BoolValue("ArmorColor", false);
    private final BoolValue hytValue = new BoolValue("HytFixed", false);

    public final boolean isInYourTeam(IEntityLivingBase entity) {
        String clientName;
        String targetName;
        IEntityPlayer entityPlayer;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return false;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (((Boolean)this.scoreboardValue.get()).booleanValue() && thePlayer.getTeam() != null && entity.getTeam() != null) {
            ITeam iTeam = thePlayer.getTeam();
            if (iTeam == null) {
                Intrinsics.throwNpe();
            }
            ITeam iTeam2 = entity.getTeam();
            if (iTeam2 == null) {
                Intrinsics.throwNpe();
            }
            if (iTeam.isSameTeam(iTeam2)) {
                return true;
            }
        }
        IIChatComponent displayName = thePlayer.getDisplayName();
        if (((Boolean)this.armorColorValue.get()).booleanValue()) {
            entityPlayer = entity.asEntityPlayer();
            if (thePlayer.getInventory().getArmorInventory().get(3) != null && entityPlayer.getInventory().getArmorInventory().get(3) != null) {
                IItemStack myHead;
                IItemStack iItemStack = myHead = thePlayer.getInventory().getArmorInventory().get(3);
                if (iItemStack == null) {
                    Intrinsics.throwNpe();
                }
                IItem iItem = iItemStack.getItem();
                if (iItem == null) {
                    Intrinsics.throwNpe();
                }
                IItemArmor myItemArmor = iItem.asItemArmor();
                IItemStack entityHead = entityPlayer.getInventory().getArmorInventory().get(3);
                IItem iItem2 = myHead.getItem();
                if (iItem2 == null) {
                    Intrinsics.throwNpe();
                }
                IItemArmor entityItemArmor = iItem2.asItemArmor();
                int n = myItemArmor.getColor(myHead);
                IItemStack iItemStack2 = entityHead;
                if (iItemStack2 == null) {
                    Intrinsics.throwNpe();
                }
                if (n == entityItemArmor.getColor(iItemStack2)) {
                    return true;
                }
            }
        }
        if (((Boolean)this.hytValue.get()).booleanValue()) {
            entityPlayer = (EntityPlayer)entity;
            INetworkPlayerInfo networkPlayerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo(entityPlayer.func_110124_au());
            if (networkPlayerInfo != null) {
                GuiPlayerTabOverlay guiPlayerTabOverlay = CodeTool.guiIngame.func_175181_h();
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                INetworkPlayerInfo iNetworkPlayerInfo = iINetHandlerPlayClient.getPlayerInfo(iEntityPlayerSP2.getUniqueID());
                if (iNetworkPlayerInfo == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.minecraft.client.network.NetworkPlayerInfo");
                }
                String string = guiPlayerTabOverlay.func_175243_a((NetworkPlayerInfo)iNetworkPlayerInfo);
                int n = 0;
                int n2 = 4;
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                String startString = string2.substring(n, n2);
                startString = StringsKt.replace$default((String)startString, (String)"\u00a7", (String)"&", (boolean)false, (int)4, null);
                string = StringsKt.replace$default((String)CodeTool.guiIngame.func_175181_h().func_175243_a((NetworkPlayerInfo)networkPlayerInfo), (String)"\u00a7", (String)"&", (boolean)false, (int)4, null);
                n = 0;
                n2 = 4;
                bl = false;
                String string3 = string;
                if (string3 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                if (string3.substring(n, n2).equals(startString)) {
                    return true;
                }
            }
        }
        if (((Boolean)this.gommeSWValue.get()).booleanValue() && displayName != null && entity.getDisplayName() != null) {
            IIChatComponent iIChatComponent = entity.getDisplayName();
            if (iIChatComponent == null) {
                Intrinsics.throwNpe();
            }
            targetName = StringsKt.replace$default((String)iIChatComponent.getFormattedText(), (String)"\u00a7r", (String)"", (boolean)false, (int)4, null);
            clientName = StringsKt.replace$default((String)displayName.getFormattedText(), (String)"\u00a7r", (String)"", (boolean)false, (int)4, null);
            if (StringsKt.startsWith$default((String)targetName, (String)"T", (boolean)false, (int)2, null) && StringsKt.startsWith$default((String)clientName, (String)"T", (boolean)false, (int)2, null)) {
                char c = targetName.charAt(1);
                boolean bl = false;
                if (Character.isDigit(c)) {
                    c = clientName.charAt(1);
                    bl = false;
                    if (Character.isDigit(c)) {
                        return targetName.charAt(1) == clientName.charAt(1);
                    }
                }
            }
        }
        if (((Boolean)this.colorValue.get()).booleanValue() && displayName != null && entity.getDisplayName() != null) {
            IIChatComponent iIChatComponent = entity.getDisplayName();
            if (iIChatComponent == null) {
                Intrinsics.throwNpe();
            }
            targetName = StringsKt.replace$default((String)iIChatComponent.getFormattedText(), (String)"\u00a7r", (String)"", (boolean)false, (int)4, null);
            clientName = StringsKt.replace$default((String)displayName.getFormattedText(), (String)"\u00a7r", (String)"", (boolean)false, (int)4, null);
            return StringsKt.startsWith$default((String)targetName, (String)("" + '\u00a7' + clientName.charAt(1)), (boolean)false, (int)2, null);
        }
        return false;
    }
}

