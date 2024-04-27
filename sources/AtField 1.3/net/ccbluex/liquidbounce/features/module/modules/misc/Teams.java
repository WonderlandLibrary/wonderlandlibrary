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

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import liying.utils.CodeTool;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
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
    private final BoolValue gommeSWValue;
    private final BoolValue colorValue;
    private final BoolValue armorColorValue;
    private final BoolValue scoreboardValue = new BoolValue("ScoreboardTeam", true);
    private final BoolValue hytValue;

    public Teams() {
        this.colorValue = new BoolValue("Color", true);
        this.gommeSWValue = new BoolValue("GommeSW", false);
        this.armorColorValue = new BoolValue("ArmorColor", false);
        this.hytValue = new BoolValue("HytFixed", false);
    }

    public final boolean isInYourTeam(IEntityLivingBase iEntityLivingBase) {
        Object object;
        Object object2;
        Object object3;
        Object object4;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return false;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (((Boolean)this.scoreboardValue.get()).booleanValue() && iEntityPlayerSP2.getTeam() != null && iEntityLivingBase.getTeam() != null) {
            ITeam iTeam = iEntityPlayerSP2.getTeam();
            if (iTeam == null) {
                Intrinsics.throwNpe();
            }
            ITeam iTeam2 = iEntityLivingBase.getTeam();
            if (iTeam2 == null) {
                Intrinsics.throwNpe();
            }
            if (iTeam.isSameTeam(iTeam2)) {
                return true;
            }
        }
        IIChatComponent iIChatComponent = iEntityPlayerSP2.getDisplayName();
        if (((Boolean)this.armorColorValue.get()).booleanValue()) {
            object4 = iEntityLivingBase.asEntityPlayer();
            if (iEntityPlayerSP2.getInventory().getArmorInventory().get(3) != null && object4.getInventory().getArmorInventory().get(3) != null) {
                Object object5 = object3 = (IItemStack)iEntityPlayerSP2.getInventory().getArmorInventory().get(3);
                if (object5 == null) {
                    Intrinsics.throwNpe();
                }
                IItem iItem = object5.getItem();
                if (iItem == null) {
                    Intrinsics.throwNpe();
                }
                object2 = iItem.asItemArmor();
                object = (IItemStack)object4.getInventory().getArmorInventory().get(3);
                IItem iItem2 = object3.getItem();
                if (iItem2 == null) {
                    Intrinsics.throwNpe();
                }
                IItemArmor iItemArmor = iItem2.asItemArmor();
                int n = object2.getColor((IItemStack)object3);
                Object object6 = object;
                if (object6 == null) {
                    Intrinsics.throwNpe();
                }
                if (n == iItemArmor.getColor((IItemStack)object6)) {
                    return true;
                }
            }
        }
        if (((Boolean)this.hytValue.get()).booleanValue()) {
            object4 = (EntityPlayer)iEntityLivingBase;
            object3 = MinecraftInstance.mc.getNetHandler().getPlayerInfo(object4.func_110124_au());
            if (object3 != null) {
                GuiPlayerTabOverlay guiPlayerTabOverlay = CodeTool.guiIngame.func_175181_h();
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                INetworkPlayerInfo iNetworkPlayerInfo = iINetHandlerPlayClient.getPlayerInfo(iEntityPlayerSP3.getUniqueID());
                if (iNetworkPlayerInfo == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.minecraft.client.network.NetworkPlayerInfo");
                }
                object = guiPlayerTabOverlay.func_175243_a((NetworkPlayerInfo)iNetworkPlayerInfo);
                int n = 0;
                int n2 = 4;
                boolean bl = false;
                Object object7 = object;
                if (object7 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                object2 = ((String)object7).substring(n, n2);
                object2 = StringsKt.replace$default((String)object2, (String)"\u00a7", (String)"&", (boolean)false, (int)4, null);
                object = StringsKt.replace$default((String)CodeTool.guiIngame.func_175181_h().func_175243_a((NetworkPlayerInfo)object3), (String)"\u00a7", (String)"&", (boolean)false, (int)4, null);
                n = 0;
                n2 = 4;
                bl = false;
                Object object8 = object;
                if (object8 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                if (((String)object8).substring(n, n2).equals(object2)) {
                    return true;
                }
            }
        }
        if (((Boolean)this.gommeSWValue.get()).booleanValue() && iIChatComponent != null && iEntityLivingBase.getDisplayName() != null) {
            IIChatComponent iIChatComponent2 = iEntityLivingBase.getDisplayName();
            if (iIChatComponent2 == null) {
                Intrinsics.throwNpe();
            }
            object4 = StringsKt.replace$default((String)iIChatComponent2.getFormattedText(), (String)"\u00a7r", (String)"", (boolean)false, (int)4, null);
            object3 = StringsKt.replace$default((String)iIChatComponent.getFormattedText(), (String)"\u00a7r", (String)"", (boolean)false, (int)4, null);
            if (StringsKt.startsWith$default((String)object4, (String)"T", (boolean)false, (int)2, null) && StringsKt.startsWith$default((String)object3, (String)"T", (boolean)false, (int)2, null)) {
                char c = ((String)object4).charAt(1);
                boolean bl = false;
                if (Character.isDigit(c)) {
                    c = ((String)object3).charAt(1);
                    bl = false;
                    if (Character.isDigit(c)) {
                        return ((String)object4).charAt(1) == ((String)object3).charAt(1);
                    }
                }
            }
        }
        if (((Boolean)this.colorValue.get()).booleanValue() && iIChatComponent != null && iEntityLivingBase.getDisplayName() != null) {
            IIChatComponent iIChatComponent3 = iEntityLivingBase.getDisplayName();
            if (iIChatComponent3 == null) {
                Intrinsics.throwNpe();
            }
            object4 = StringsKt.replace$default((String)iIChatComponent3.getFormattedText(), (String)"\u00a7r", (String)"", (boolean)false, (int)4, null);
            object3 = StringsKt.replace$default((String)iIChatComponent.getFormattedText(), (String)"\u00a7r", (String)"", (boolean)false, (int)4, null);
            return StringsKt.startsWith$default((String)object4, (String)("" + '\u00a7' + ((String)object3).charAt(1)), (boolean)false, (int)2, null);
        }
        return false;
    }
}

