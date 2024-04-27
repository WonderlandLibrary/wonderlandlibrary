package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import me.utils.PacketUtils;
import me.utils.player.PlayerUtil;
import me.utils.timer.TimeHelper;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
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
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="FastUse", description="Allows you to use items faster.", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000d\n\n\n\b\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\b\n\b\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢J\b0HJ020HJ02\b0 HJ!020\"HR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0\tX¢\n\u0000R\n0X¢\n\u0000R\f0\rX¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R08VX¢\bR0X¢\n\u0000R0\tX¢\n\u0000¨#"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/FastUse;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "customSpeedValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "customTimer", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "delayValue", "fixed", "", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "msTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "noMoveValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "packet", "", "stopMoving", "tag", "", "getTag", "()Ljava/lang/String;", "time", "Lme/utils/timer/TimeHelper;", "usedTimer", "onDisable", "", "onMotion", "event", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onMove", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class FastUse
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Instant", "NCP", "AAC", "Custom", "Vulcan", "Hypixel", "Hyt"}, "NCP");
    private final BoolValue noMoveValue = new BoolValue("NoMove", false);
    private final IntegerValue delayValue = new IntegerValue("CustomDelay", 0, 0, 300);
    private final IntegerValue customSpeedValue = new IntegerValue("CustomSpeed", 2, 1, 35);
    private final FloatValue customTimer = new FloatValue("CustomTimer", 1.1f, 0.5f, 2.0f);
    private final BoolValue stopMoving = new BoolValue("StopMoving", false);
    private final MSTimer msTimer = new MSTimer();
    private boolean usedTimer;
    private boolean fixed;
    private int packet;
    private final TimeHelper time = new TimeHelper();

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (event.isPre()) {
            v0 = MinecraftInstance.mc2.player;
            if (v0 == null) {
                Intrinsics.throwNpe();
            }
            if (!(v0.field_71071_by.getCurrentItem() == null || !MinecraftInstance.mc.getGameSettings().getKeyBindUseItem().isKeyDown() || ((Boolean)this.stopMoving.get()).booleanValue() && PlayerUtil.isMoving())) {
                v1 = MinecraftInstance.mc2.player;
                if (v1 == null) {
                    Intrinsics.throwNpe();
                }
                v2 = v1.field_71071_by.getCurrentItem();
                Intrinsics.checkExpressionValueIsNotNull(v2, "mc2.player!!.inventory.getCurrentItem()");
                heldItem = v2.getItem();
                if (heldItem instanceof ItemFood || heldItem instanceof ItemPotion) {
                    this.fixed = false;
                    var3_3 = (String)this.modeValue.get();
                    var4_4 = 0;
                    v3 = var3_3;
                    if (v3 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    v4 = v3.toLowerCase();
                    Intrinsics.checkExpressionValueIsNotNull(v4, "(this as java.lang.String).toLowerCase()");
                    var3_3 = v4;
                    switch (var3_3.hashCode()) {
                        case 1381910549: {
                            if (!var3_3.equals("hypixel")) ** break;
                            break;
                        }
                        case -805359837: {
                            if (!var3_3.equals("vulcan")) ** break;
                            if (this.packet != 16) {
                                v5 = MinecraftInstance.mc.getThePlayer();
                                if (v5 == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (v5.getTicksExisted() % 2 == 0) {
                                    MinecraftInstance.mc.getTimer().setTimerSpeed(0.33f);
                                } else {
                                    MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
                                }
                                PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)((Packet)new CPacketPlayer(true)));
                                var4_4 = this.packet;
                                this.packet = var4_4 + 1;
                                ** break;
                            }
                            v6 = MinecraftInstance.mc.getPlayerController();
                            v7 = MinecraftInstance.mc.getThePlayer();
                            if (v7 == null) {
                                Intrinsics.throwNpe();
                            }
                            v6.onStoppedUsingItem(v7);
                            MinecraftInstance.mc.getGameSettings().getKeyBindUseItem().setPressed(false);
                            ** break;
                        }
                    }
                    if (this.packet != 20) {
                        MinecraftInstance.mc.getTimer().setTimerSpeed(0.5f);
                        v8 = MinecraftInstance.mc.getThePlayer();
                        if (v8 == null) {
                            Intrinsics.throwNpe();
                        }
                        v8.getSendQueue().getNetworkManager().sendPacket(MinecraftInstance.classProvider.createCPacketPlayer(true));
                        var4_4 = this.packet;
                        this.packet = var4_4 + 1;
                        ** break;
                    }
                    v9 = MinecraftInstance.mc.getPlayerController();
                    v10 = MinecraftInstance.mc.getThePlayer();
                    if (v10 == null) {
                        Intrinsics.throwNpe();
                    }
                    v9.onStoppedUsingItem(v10);
                    ** break;
                }
            }
lbl63:
            // 11 sources

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
    public final void onUpdate(@NotNull UpdateEvent event) {
        int n;
        IEntityPlayerSP thePlayer;
        block21: {
            block20: {
                block22: {
                    IItem usingItem;
                    Intrinsics.checkParameterIsNotNull(event, "event");
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
                    String string3 = string2.toLowerCase();
                    Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
                    string = string3;
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
                                int it = n2++;
                                boolean bl2 = false;
                                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(thePlayer.getOnGround()));
                            }
                            MinecraftInstance.mc.getPlayerController().onStoppedUsingItem(thePlayer);
                            return;
                        }
                    }
                    MinecraftInstance.mc.getTimer().setTimerSpeed(0.65f);
                    this.usedTimer = true;
                    if (!thePlayer.getOnGround()) return;
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP2 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d = iEntityPlayerSP2.getPosX();
                    IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP3 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d2 = iEntityPlayerSP3.getPosY() + 0.0019;
                    IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP4 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d3 = iEntityPlayerSP4.getPosZ();
                    float f = thePlayer.getRotationYaw();
                    float f2 = thePlayer.getRotationPitch();
                    IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP5 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosLook(d, d2, d3, f, f2, iEntityPlayerSP5.getOnGround()));
                    return;
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
        return;
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
    @Nullable
    public String getTag() {
        return (String)this.modeValue.get();
    }
}
