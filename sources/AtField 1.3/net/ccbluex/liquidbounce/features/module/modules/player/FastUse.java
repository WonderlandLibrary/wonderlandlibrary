/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketConfirmTransaction
 *  net.minecraft.network.play.client.CPacketPlayer
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.ccbluex.liquidbounce.utils.TimeHelper;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketPlayer;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="FastUse", description="\u5feb\u901f\u4f7f\u7528\u7269\u54c1.", category=ModuleCategory.PLAYER)
public final class FastUse
extends Module {
    private final FloatValue customTimer;
    private boolean usedTimer;
    private final ListValue modeValue = new ListValue("Mode", new String[]{"NewHytFix", "NewHYT", "Instant", "NCP", "AAC", "Custom", "Vulcan", "Hypixel", "Hyt"}, "NCP");
    private final IntegerValue delayValue;
    private int packet;
    private final MSTimer msTimer;
    private boolean fixed;
    private final IntegerValue customSpeedValue;
    private final BoolValue noMoveValue = new BoolValue("NoMove", false);
    private final BoolValue stopMoving;
    private final TimeHelper time;

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        IItem iItem;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        IItemStack iItemStack = iEntityPlayerSP2.getItemInUse();
        if (iItemStack == null) {
            Intrinsics.throwNpe();
        }
        if (MinecraftInstance.classProvider.isItemFood(iItem = iItemStack.getItem()) || MinecraftInstance.classProvider.isItemBucketMilk(iItem) || MinecraftInstance.classProvider.isItemPotion(iItem)) {
            String string = (String)this.modeValue.get();
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            string = string2.toLowerCase();
            switch (string.hashCode()) {
                case 10179922: {
                    if (!string.equals("newhytfix")) break;
                    MinecraftInstance.mc.getTimer().setTimerSpeed(((Number)this.customTimer.get()).floatValue());
                    IPacket iPacket = packetEvent.getPacket();
                    this.usedTimer = true;
                    if (!iEntityPlayerSP2.getOnGround() || !(iPacket instanceof CPacketConfirmTransaction)) break;
                    packetEvent.cancelEvent();
                    break;
                }
            }
        }
    }

    public FastUse() {
        this.delayValue = new IntegerValue("CustomDelay", 0, 0, 300);
        this.customSpeedValue = new IntegerValue("CustomSpeed", 2, 1, 35);
        this.customTimer = new FloatValue("CustomTimer", 1.1f, 0.5f, 2.0f);
        this.stopMoving = new BoolValue("StopMoving", false);
        this.msTimer = new MSTimer();
        this.time = new TimeHelper();
    }

    @EventTarget
    public final void onMotion(MotionEvent motionEvent) {
        if (motionEvent.isPre()) {
            EntityPlayerSP entityPlayerSP = MinecraftInstance.mc2.field_71439_g;
            if (entityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (!(entityPlayerSP.field_71071_by.func_70448_g() == null || !MinecraftInstance.mc.getGameSettings().getKeyBindUseItem().isKeyDown() || ((Boolean)this.stopMoving.get()).booleanValue() && MovementUtils.isMoving())) {
                Item item;
                EntityPlayerSP entityPlayerSP2 = MinecraftInstance.mc2.field_71439_g;
                if (entityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                if ((item = entityPlayerSP2.field_71071_by.func_70448_g().func_77973_b()) instanceof ItemFood || item instanceof ItemPotion) {
                    this.fixed = false;
                    String string = (String)this.modeValue.get();
                    int n = 0;
                    String string2 = string;
                    if (string2 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    switch (string2.toLowerCase()) {
                        case "vulcan": {
                            if (this.packet != 16) {
                                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (iEntityPlayerSP.getTicksExisted() % 2 == 0) {
                                    MinecraftInstance.mc.getTimer().setTimerSpeed(0.33f);
                                } else {
                                    MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
                                }
                                PacketUtils.INSTANCE.sendPacketNoEvent((Packet)new CPacketPlayer(true));
                                n = this.packet;
                                this.packet = n + 1;
                                break;
                            }
                            IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP == null) {
                                Intrinsics.throwNpe();
                            }
                            iPlayerControllerMP.onStoppedUsingItem(iEntityPlayerSP);
                            MinecraftInstance.mc.getGameSettings().getKeyBindUseItem().setPressed(false);
                            break;
                        }
                        case "pridehyt": {
                            MinecraftInstance.mc.getTimer().setTimerSpeed(0.7f);
                            this.usedTimer = true;
                            IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP == null) {
                                Intrinsics.throwNpe();
                            }
                            iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(iEntityPlayerSP.getOnGround()));
                            break;
                        }
                        case "hypixel": {
                            if (this.packet != 20) {
                                MinecraftInstance.mc.getTimer().setTimerSpeed(0.5f);
                                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP == null) {
                                    Intrinsics.throwNpe();
                                }
                                iEntityPlayerSP.getSendQueue().getNetworkManager().sendPacket(MinecraftInstance.classProvider.createCPacketPlayer(true));
                                n = this.packet;
                                this.packet = n + 1;
                                break;
                            }
                            IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP == null) {
                                Intrinsics.throwNpe();
                            }
                            iPlayerControllerMP.onStoppedUsingItem(iEntityPlayerSP);
                            break;
                        }
                    }
                }
            }
            if (!MinecraftInstance.mc.getGameSettings().getKeyBindUseItem().isKeyDown() && !this.fixed) {
                this.packet = 0;
                MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
                this.fixed = true;
                this.time.reset();
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        int n;
        IEntityPlayerSP iEntityPlayerSP;
        block21: {
            block20: {
                block22: {
                    IItem iItem;
                    IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP2 == null) {
                        return;
                    }
                    iEntityPlayerSP = iEntityPlayerSP2;
                    if (this.usedTimer) {
                        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
                        this.usedTimer = false;
                    }
                    if (!iEntityPlayerSP.isUsingItem()) {
                        this.msTimer.reset();
                        return;
                    }
                    IItemStack iItemStack = iEntityPlayerSP.getItemInUse();
                    if (iItemStack == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!MinecraftInstance.classProvider.isItemFood(iItem = iItemStack.getItem()) && !MinecraftInstance.classProvider.isItemBucketMilk(iItem) && !MinecraftInstance.classProvider.isItemPotion(iItem)) return;
                    String string = (String)this.modeValue.get();
                    n = 0;
                    String string2 = string;
                    if (string2 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    string = string2.toLowerCase();
                    switch (string.hashCode()) {
                        case 103811: {
                            if (!string.equals("hyt")) return;
                            break;
                        }
                        case 96323: {
                            if (!string.equals("aac")) return;
                            break block20;
                        }
                        case -1349088399: {
                            if (!string.equals("custom")) return;
                            break block21;
                        }
                        case 108891: {
                            if (!string.equals("ncp")) return;
                            break block22;
                        }
                        case 1957570017: {
                            if (!string.equals("instant")) return;
                            n = 35;
                            boolean bl = false;
                            int n2 = 0;
                            n2 = 0;
                            int n3 = n;
                            while (n2 < n3) {
                                int n4 = n2++;
                                boolean bl2 = false;
                                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(iEntityPlayerSP.getOnGround()));
                            }
                            MinecraftInstance.mc.getPlayerController().onStoppedUsingItem(iEntityPlayerSP);
                            return;
                        }
                    }
                    MinecraftInstance.mc.getTimer().setTimerSpeed(0.65f);
                    this.usedTimer = true;
                    if (!iEntityPlayerSP.getOnGround()) return;
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP3 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d = iEntityPlayerSP3.getPosX();
                    IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP4 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d2 = iEntityPlayerSP4.getPosY() + 0.0019;
                    IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP5 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d3 = iEntityPlayerSP5.getPosZ();
                    float f = iEntityPlayerSP.getRotationYaw();
                    float f2 = iEntityPlayerSP.getRotationPitch();
                    IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP6 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosLook(d, d2, d3, f, f2, iEntityPlayerSP6.getOnGround()));
                    return;
                }
                if (iEntityPlayerSP.getItemInUseDuration() <= 14) return;
                n = 20;
                boolean bl = false;
                int n5 = 0;
                n5 = 0;
                int n6 = n;
                while (n5 < n6) {
                    int n7 = n5++;
                    boolean bl3 = false;
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(iEntityPlayerSP.getOnGround()));
                }
                MinecraftInstance.mc.getPlayerController().onStoppedUsingItem(iEntityPlayerSP);
                return;
            }
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.22f);
            this.usedTimer = true;
            return;
        }
        MinecraftInstance.mc.getTimer().setTimerSpeed(((Number)this.customTimer.get()).floatValue());
        this.usedTimer = true;
        if (!this.msTimer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
            return;
        }
        n = ((Number)this.customSpeedValue.get()).intValue();
        boolean bl = false;
        int n8 = 0;
        n8 = 0;
        int n9 = n;
        while (n8 < n9) {
            int n10 = n8++;
            boolean bl4 = false;
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(iEntityPlayerSP.getOnGround()));
        }
        this.msTimer.reset();
        return;
    }

    @Override
    public void onDisable() {
        if (this.usedTimer) {
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
            this.usedTimer = false;
        }
    }

    @EventTarget
    public final void onMove(@Nullable MoveEvent moveEvent) {
        IItem iItem;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null || moveEvent == null) {
            return;
        }
        if (!(this.getState() && iEntityPlayerSP.isUsingItem() && ((Boolean)this.noMoveValue.get()).booleanValue())) {
            return;
        }
        IItemStack iItemStack = iEntityPlayerSP.getItemInUse();
        if (iItemStack == null) {
            Intrinsics.throwNpe();
        }
        if (MinecraftInstance.classProvider.isItemFood(iItem = iItemStack.getItem()) || MinecraftInstance.classProvider.isItemBucketMilk(iItem) || MinecraftInstance.classProvider.isItemPotion(iItem)) {
            moveEvent.zero();
        }
    }
}

