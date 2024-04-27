/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="FastUse", description="Allows you to use items faster.", category=ModuleCategory.PLAYER)
public final class FastUse
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Instant", "NCP", "AAC", "CustomDelay", "Hyt", "VulCan"}, "NCP");
    private final BoolValue noMoveValue = new BoolValue("NoMove", false);
    private final IntegerValue delayValue = new IntegerValue("CustomDelay", 0, 0, 300);
    private final IntegerValue customSpeedValue = new IntegerValue("CustomSpeed", 2, 1, 35);
    private final FloatValue customTimer = new FloatValue("CustomTimer", 1.1f, 0.5f, 2.0f);
    private final MSTimer msTimer = new MSTimer();
    private boolean usedTimer;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        int n;
        IEntityPlayerSP thePlayer;
        block29: {
            block27: {
                block28: {
                    block26: {
                        IItem usingItem;
                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP == null) {
                            return;
                        }
                        thePlayer = iEntityPlayerSP;
                        if (this.usedTimer) {
                            MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
                            this.usedTimer = false;
                        }
                        if (!thePlayer.isUsingItem()) {
                            this.msTimer.reset();
                            return;
                        }
                        IItemStack iItemStack = thePlayer.getItemInUse();
                        if (iItemStack == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!MinecraftInstance.classProvider.isItemFood(usingItem = iItemStack.getItem()) && !MinecraftInstance.classProvider.isItemBucketMilk(usingItem) && !MinecraftInstance.classProvider.isItemPotion(usingItem)) return;
                        String string = (String)this.modeValue.get();
                        n = 0;
                        String string2 = string;
                        if (string2 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        string = string2.toLowerCase();
                        switch (string.hashCode()) {
                            case 96323: {
                                if (!string.equals("aac")) return;
                                break block26;
                            }
                            case 103811: {
                                if (!string.equals("hyt")) return;
                                break block27;
                            }
                            case 108891: {
                                if (!string.equals("ncp")) return;
                                break;
                            }
                            case -805359837: {
                                if (!string.equals("vulcan")) return;
                                break block28;
                            }
                            case -1773359950: {
                                if (!string.equals("customdelay")) return;
                                break block29;
                            }
                            case 1957570017: {
                                if (!string.equals("instant")) return;
                                n = 35;
                                boolean bl = false;
                                int n2 = 0;
                                n2 = 0;
                                int n3 = n;
                                while (n2 < n3) {
                                    int it = n2++;
                                    boolean bl2 = false;
                                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(thePlayer.getOnGround()));
                                }
                                MinecraftInstance.mc.getPlayerController().onStoppedUsingItem(thePlayer);
                                return;
                            }
                        }
                        if (thePlayer.getItemInUseDuration() <= 14) return;
                        n = 20;
                        boolean bl = false;
                        int n4 = 0;
                        n4 = 0;
                        int n5 = n;
                        while (n4 < n5) {
                            int it = n4++;
                            boolean bl3 = false;
                            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(thePlayer.getOnGround()));
                        }
                        MinecraftInstance.mc.getPlayerController().onStoppedUsingItem(thePlayer);
                        return;
                    }
                    MinecraftInstance.mc.getTimer().setTimerSpeed(1.22f);
                    this.usedTimer = true;
                    return;
                }
                MinecraftInstance.mc.getTimer().setTimerSpeed(0.4f);
                this.usedTimer = true;
                if (!thePlayer.getOnGround()) return;
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(iEntityPlayerSP.getOnGround()));
                IINetHandlerPlayClient iINetHandlerPlayClient2 = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                double d = iEntityPlayerSP2.getPosX();
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                double d2 = iEntityPlayerSP3.getPosY();
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                double d3 = iEntityPlayerSP4.getPosZ();
                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP5 == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient2.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2, d3, iEntityPlayerSP5.getOnGround()));
                return;
            }
            MinecraftInstance.mc.getTimer().setTimerSpeed(0.65f);
            this.usedTimer = true;
            if (!thePlayer.getOnGround()) return;
            IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            double d = iEntityPlayerSP.getPosX();
            IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP6 == null) {
                Intrinsics.throwNpe();
            }
            double d4 = iEntityPlayerSP6.getPosY() + 0.0019;
            IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP7 == null) {
                Intrinsics.throwNpe();
            }
            double d5 = iEntityPlayerSP7.getPosZ();
            float f = thePlayer.getRotationYaw();
            float f2 = thePlayer.getRotationPitch();
            IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP8 == null) {
                Intrinsics.throwNpe();
            }
            iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosLook(d, d4, d5, f, f2, iEntityPlayerSP8.getOnGround()));
            return;
        }
        MinecraftInstance.mc.getTimer().setTimerSpeed(((Number)this.customTimer.get()).floatValue());
        this.usedTimer = true;
        if (!this.msTimer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
            return;
        }
        n = ((Number)this.customSpeedValue.get()).intValue();
        boolean bl = false;
        int n6 = 0;
        n6 = 0;
        int n7 = n;
        while (n6 < n7) {
            int it = n6++;
            boolean bl4 = false;
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(thePlayer.getOnGround()));
        }
        this.msTimer.reset();
    }

    @EventTarget
    public final void onMove(@Nullable MoveEvent event) {
        IItem usingItem;
        IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
        if (thePlayer == null || event == null) {
            return;
        }
        if (!(this.getState() && thePlayer.isUsingItem() && ((Boolean)this.noMoveValue.get()).booleanValue())) {
            return;
        }
        IItemStack iItemStack = thePlayer.getItemInUse();
        if (iItemStack == null) {
            Intrinsics.throwNpe();
        }
        if (MinecraftInstance.classProvider.isItemFood(usingItem = iItemStack.getItem()) || MinecraftInstance.classProvider.isItemBucketMilk(usingItem) || MinecraftInstance.classProvider.isItemPotion(usingItem)) {
            event.zero();
        }
    }

    @Override
    public void onDisable() {
        if (this.usedTimer) {
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
            this.usedTimer = false;
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

