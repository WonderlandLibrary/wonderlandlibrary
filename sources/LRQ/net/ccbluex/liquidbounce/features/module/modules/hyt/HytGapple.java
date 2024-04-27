/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.Minecraft
 *  net.minecraft.init.Items
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.enums.ItemType;
import net.ccbluex.liquidbounce.api.enums.WEnumHand;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="HytGapple", category=ModuleCategory.HYT, description="\u4fee\u590d\u7248")
public final class HytGapple
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Auto", "LegitAuto", "Once", "Head"}, "Once");
    private final FloatValue healthValue = new FloatValue("Health", 10.0f, 1.0f, 20.0f);
    private final IntegerValue delayValue = new IntegerValue("Delay", 150, 0, 1000);
    private final BoolValue noAbsorption = new BoolValue("NoAbsorption", true);
    private final MSTimer timer = new MSTimer();
    private int eating = -1;

    public final ListValue getModeValue() {
        return this.modeValue;
    }

    @Override
    public void onEnable() {
        this.eating = -1;
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string2.toLowerCase()) {
            case "once": {
                this.doEat(true);
                this.setState(false);
                break;
            }
            case "auto": {
                if (!this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
                    return;
                }
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (!(iEntityPlayerSP.getHealth() <= ((Number)this.healthValue.get()).floatValue())) break;
                this.doEat(false);
                this.timer.reset();
                break;
            }
            case "legitauto": {
                if (this.eating == -1) {
                    int gappleInHotbar = InventoryUtils.findItem2(36, 45, Items.field_151153_ao);
                    if (gappleInHotbar == -1) {
                        return;
                    }
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(gappleInHotbar - 36));
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerBlockPlacement(iEntityPlayerSP.getHeldItem()));
                    this.eating = 0;
                    break;
                }
                if (this.eating <= 35) break;
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(iEntityPlayerSP.getInventory().getCurrentItem()));
                this.timer.reset();
                break;
            }
            case "head": {
                int headInHotbar;
                if (!this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
                    return;
                }
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (!(iEntityPlayerSP.getHealth() <= ((Number)this.healthValue.get()).floatValue()) || (headInHotbar = InventoryUtils.findItem(36, 45, MinecraftInstance.classProvider.getItemEnum(ItemType.SKULL))) == -1) break;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(headInHotbar - 36));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketTryUseItem(WEnumHand.OFF_HAND));
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(iEntityPlayerSP2.getInventory().getCurrentItem()));
                this.timer.reset();
            }
        }
    }

    private final void doEat(boolean warn) {
        block5: {
            float abAmount;
            if (((Boolean)this.noAbsorption.get()).booleanValue() && !warn && (abAmount = (float)Math.round((double)Minecraft.func_71410_x().field_71439_g.func_110139_bj() * 10.0 / (double)10.0f)) > 0.0f) {
                return;
            }
            int gappleInHotbar = InventoryUtils.findItem2(36, 45, Items.field_151153_ao);
            if (gappleInHotbar == -1) break block5;
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(gappleInHotbar - 36));
            IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerBlockPlacement(iEntityPlayerSP.getHeldItem()));
            int n = 35;
            boolean bl = false;
            int n2 = 0;
            int n3 = n;
            for (n2 = 0; n2 < n3; ++n2) {
                int it = n2;
                boolean bl2 = false;
                IINetHandlerPlayClient iINetHandlerPlayClient2 = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient2.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(iEntityPlayerSP2.getOnGround()));
            }
            IINetHandlerPlayClient iINetHandlerPlayClient3 = MinecraftInstance.mc.getNetHandler();
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            iINetHandlerPlayClient3.addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(iEntityPlayerSP3.getInventory().getCurrentItem()));
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

