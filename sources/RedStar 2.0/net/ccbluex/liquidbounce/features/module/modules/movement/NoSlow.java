package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.util.Collection;
import java.util.LinkedList;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import me.utils.Debug;
import me.utils.PacketUtils;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.enums.WEnumHand;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketAnimation;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerBlockPlacement;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.SlowDownEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.render.OldHitting;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="NoSlow", description="防砍+跑吃用Grim", category=ModuleCategory.MOVEMENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\b\n\n\b\n\n\n\n\b\b\n\n\b\b\n\n\b\n\b\n\u0000\n\n\u0000\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\t\n\b\b\b\u000020B¢J002102HJ302102HJ402506J7082\b90:2;0HJ\b<0J=0J\b>0?HJ@0?2102HJA0?210BHJC0?210DHJE0?210FHJ8G0?21022H02I02J02K0L2M0HJBG0?2N022O02P02Q02R0L2M02\b\bS0HR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0X¢\n\u0000R\t0X¢\n\u0000R\n0X¢\n\u0000R0X¢\n\u0000R\f0\rX¢\n\u0000R0X¢\n\u0000R08BX¢\bR0¢\b\n\u0000\bR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\n\b0 00X¢\n\u0000R!0¢\b\n\u0000\b\"#R$0X¢\n\u0000R%0X¢\n\u0000R&0¢\b\n\u0000\b'#R(0)8VX¢\b*+R,0¢\b\n\u0000\b-.R/0X¢\n\u0000¨T"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/NoSlow;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "Timer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "blockForwardMultiplier", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "blockStrafeMultiplier", "bowForwardMultiplier", "bowStrafeMultiplier", "consumeForwardMultiplier", "consumeStrafeMultiplier", "customDelayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "customOnGround", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "isBlocking", "", "()Z", "killAura", "Lnet/ccbluex/liquidbounce/features/module/modules/combat/KillAura;", "getKillAura", "()Lnet/ccbluex/liquidbounce/features/module/modules/combat/KillAura;", "lastBlockingStat", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "msTimer", "nextTemp", "packet", "packetBuf", "Ljava/util/LinkedList;", "Lnet/minecraft/network/Packet;", "Lnet/minecraft/network/play/INetHandlerPlayServer;", "packetValue", "getPacketValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "pendingFlagApplyPacket", "sendBuf", "soulsandValue", "getSoulsandValue", "tag", "", "getTag", "()Ljava/lang/String;", "timer", "getTimer", "()Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "waitC03", "OnPost", "event", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "OnPre", "fuckKotline", "value", "", "getMultiplier", "", "item", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItem;", "isForward", "getValue", "isBlock", "onDisable", "", "onMotion", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onSlowDown", "Lnet/ccbluex/liquidbounce/event/SlowDownEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "sendPacket", "sendC07", "sendC08", "delay", "delayValue", "", "onGround", "Event", "SendC07", "SendC08", "Delay", "DelayValue", "Hypixel", "Pride"})
public final class NoSlow
extends Module {
    private final BoolValue packet = new BoolValue("Packet", false);
    private final ListValue modeValue = new ListValue("PacketMode", new String[]{"None", "HytGrimAc", "HuaYuTing", "Vanilla", "NoPacket", "AAC", "AAC5", "Matrix", "Vulcan", "Custom"}, "AntiCheat");
    private final FloatValue blockForwardMultiplier = new FloatValue("BlockForwardMultiplier", 1.0f, 0.2f, 1.0f);
    private final FloatValue blockStrafeMultiplier = new FloatValue("BlockStrafeMultiplier", 1.0f, 0.2f, 1.0f);
    private final FloatValue consumeForwardMultiplier = new FloatValue("ConsumeForwardMultiplier", 1.0f, 0.2f, 1.0f);
    private final FloatValue consumeStrafeMultiplier = new FloatValue("ConsumeStrafeMultiplier", 1.0f, 0.2f, 1.0f);
    private final FloatValue bowForwardMultiplier = new FloatValue("BowForwardMultiplier", 1.0f, 0.2f, 1.0f);
    private final FloatValue bowStrafeMultiplier = new FloatValue("BowStrafeMultiplier", 1.0f, 0.2f, 1.0f);
    private final BoolValue customOnGround = new BoolValue("CustomOnGround", false);
    private final IntegerValue customDelayValue = new IntegerValue("CustomDelay", 60, 10, 200);
    @NotNull
    private final BoolValue packetValue = new BoolValue("Renderer", true);
    @NotNull
    private final BoolValue soulsandValue = new BoolValue("Soulsand", true);
    @NotNull
    private final MSTimer timer = new MSTimer();
    private final MSTimer Timer = new MSTimer();
    private boolean pendingFlagApplyPacket;
    private final MSTimer msTimer = new MSTimer();
    private boolean sendBuf;
    private LinkedList<Packet<INetHandlerPlayServer>> packetBuf = new LinkedList();
    private boolean nextTemp;
    private boolean waitC03;
    private boolean lastBlockingStat;
    @NotNull
    private final KillAura killAura;

    @Nullable
    public final BoolValue getValue() {
        return this.packet;
    }

    @NotNull
    public final BoolValue getPacketValue() {
        return this.packetValue;
    }

    @NotNull
    public final BoolValue getSoulsandValue() {
        return this.soulsandValue;
    }

    @NotNull
    public final MSTimer getTimer() {
        return this.timer;
    }

    @NotNull
    public final KillAura getKillAura() {
        return this.killAura;
    }

    private final void sendPacket(MotionEvent event, boolean sendC07, boolean sendC08, boolean delay, long delayValue, boolean onGround) {
        IPacket digging = MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, new WBlockPos(-1, -1, -1), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN));
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IItemStack iItemStack = iEntityPlayerSP.getInventory().getCurrentItemInHand();
        Object hand$iv = WEnumHand.MAIN_HAND;
        boolean $i$f$createUseItemPacket = false;
        IPacket blockMain = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem((WEnumHand)((Object)hand$iv));
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        hand$iv = iEntityPlayerSP2.getInventory().getCurrentItemInHand();
        WEnumHand hand$iv2 = WEnumHand.OFF_HAND;
        boolean $i$f$createUseItemPacket2 = false;
        IPacket blockOFF = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(hand$iv2);
        if (onGround) {
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            if (!iEntityPlayerSP3.getOnGround()) {
                return;
            }
        }
        if (sendC07 && event.getEventState() == EventState.PRE) {
            if (delay && this.msTimer.hasTimePassed(delayValue)) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(digging);
            } else if (!delay) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(digging);
            }
        }
        if (sendC08 && event.getEventState() == EventState.POST) {
            if (delay && this.msTimer.hasTimePassed(delayValue)) {
                if (LiquidBounce.INSTANCE.getModuleManager().get(OldHitting.class).getState()) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(blockOFF);
                } else {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(blockMain);
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(blockOFF);
                }
                this.msTimer.reset();
            } else if (!delay) {
                if (LiquidBounce.INSTANCE.getModuleManager().get(OldHitting.class).getState()) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(blockOFF);
                } else {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(blockMain);
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(blockOFF);
                }
            }
        }
    }

    public final boolean isBlock() {
        Boolean bl = Debug.thePlayerisBlocking;
        Intrinsics.checkExpressionValueIsNotNull(bl, "thePlayerisBlocking");
        return bl != false || this.killAura.getBlockingStatus();
    }

    public final boolean fuckKotline(int value) {
        return value == 1;
    }

    private final boolean OnPre(MotionEvent event) {
        return event.getEventState() == EventState.PRE;
    }

    private final boolean OnPost(MotionEvent event) {
        return event.getEventState() == EventState.POST;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final boolean isBlocking() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (!iEntityPlayerSP.isUsingItem()) {
            Module module = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
            if (module == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
            }
            if (!((KillAura)module).getBlockingStatus()) return false;
        }
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP2.getHeldItem() == null) return false;
        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        IItemStack iItemStack = iEntityPlayerSP3.getHeldItem();
        if (iItemStack == null) {
            Intrinsics.throwNpe();
        }
        if (!(iItemStack.getItem() instanceof ItemSword)) return false;
        return true;
    }

    @Override
    public void onDisable() {
        this.Timer.reset();
        this.msTimer.reset();
        this.pendingFlagApplyPacket = false;
        this.sendBuf = false;
        this.packetBuf.clear();
        this.nextTemp = false;
        this.waitC03 = false;
    }

    private final void sendPacket(MotionEvent Event2, boolean SendC07, boolean SendC08, boolean Delay, long DelayValue, boolean onGround, boolean Hypixel) {
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
        }
        KillAura aura = (KillAura)module;
        EnumFacing enumFacing = EnumFacing.DOWN;
        if (enumFacing == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing");
        }
        IPacket digging = MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, new WBlockPos(-1, -1, -1), (IEnumFacing)enumFacing);
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        ICPacketPlayerBlockPlacement blockPlace = MinecraftInstance.classProvider.createCPacketPlayerBlockPlacement((IItemStack)((Object)Integer.valueOf(iEntityPlayerSP.getInventory().getCurrentItem())));
        WBlockPos wBlockPos = new WBlockPos(-1, -1, -1);
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        ICPacketPlayerBlockPlacement blockMent = MinecraftInstance.classProvider.createCPacketPlayerBlockPlacement(wBlockPos, 255, (IItemStack)((Object)Integer.valueOf(iEntityPlayerSP2.getInventory().getCurrentItem())), 0.0f, 0.0f, 0.0f);
        if (onGround) {
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            if (!iEntityPlayerSP3.getOnGround()) {
                return;
            }
        }
        if (SendC07 && this.OnPre(Event2)) {
            if (Delay && this.Timer.hasTimePassed(DelayValue)) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(digging);
            } else if (!Delay) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(digging);
            }
        }
        if (SendC08 && this.OnPost(Event2)) {
            if (Delay && this.Timer.hasTimePassed(DelayValue) && !Hypixel) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(blockPlace);
                this.Timer.reset();
            } else if (!Delay && !Hypixel) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(blockPlace);
            } else if (Hypixel) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(blockMent);
            }
        }
    }

    static void sendPacket$default(NoSlow noSlow, MotionEvent motionEvent, boolean bl, boolean bl2, boolean bl3, long l, boolean bl4, boolean bl5, int n, Object object) {
        if ((n & 0x40) != 0) {
            bl5 = false;
        }
        noSlow.sendPacket(motionEvent, bl, bl2, bl3, l, bl4, bl5);
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        v0 = MinecraftInstance.mc.getThePlayer();
        if (v0 == null) {
            return;
        }
        thePlayer = v0;
        v1 = MinecraftInstance.mc.getThePlayer();
        if (v1 == null) {
            Intrinsics.throwNpe();
        }
        test = this.fuckKotline(v1.getTicksExisted() & 1);
        heldItem = thePlayer.getHeldItem();
        if (!MovementUtils.isMoving()) {
            return;
        }
        var5_5 = (String)this.modeValue.get();
        var6_6 = false;
        v2 = var5_5;
        if (v2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        v3 = v2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(v3, "(this as java.lang.String).toLowerCase()");
        var5_5 = v3;
        tmp = -1;
        switch (var5_5.hashCode()) {
            case 96323: {
                if (!var5_5.equals("aac")) break;
                tmp = 1;
                break;
            }
            case -1349088399: {
                if (!var5_5.equals("custom")) break;
                tmp = 2;
                break;
            }
            case 63514356: {
                if (!var5_5.equals("hytgrimac")) break;
                tmp = 3;
                break;
            }
            case 233102203: {
                if (!var5_5.equals("vanilla")) break;
                tmp = 4;
                break;
            }
            case -1703304794: {
                if (!var5_5.equals("hauyuting")) break;
                tmp = 5;
                break;
            }
            case 2986066: {
                if (!var5_5.equals("aac5")) break;
                tmp = 6;
                break;
            }
        }
        switch (tmp) {
            case 2: {
                this.sendPacket(event, true, true, true, ((Number)this.customDelayValue.get()).intValue(), (Boolean)this.customOnGround.get());
                break;
            }
            case 3: {
                if (event.getEventState() != EventState.PRE) ** GOTO lbl-1000
                v4 = MinecraftInstance.mc.getThePlayer();
                if (v4 == null) {
                    Intrinsics.throwNpe();
                }
                if (v4.isBlocking()) ** GOTO lbl-1000
                v5 = MinecraftInstance.mc.getThePlayer();
                if (v5 == null) {
                    Intrinsics.throwNpe();
                }
                v6 = v5.getHeldItem();
                if (v6 == null) {
                    Intrinsics.throwNpe();
                }
                if (MinecraftInstance.classProvider.isItemPotion(v6.getItem())) ** GOTO lbl-1000
                v7 = MinecraftInstance.mc.getThePlayer();
                if (v7 == null) {
                    Intrinsics.throwNpe();
                }
                v8 = v7.getHeldItem();
                if (v8 == null) {
                    Intrinsics.throwNpe();
                }
                if (!MinecraftInstance.classProvider.isItemFood(v8.getItem())) {
                    v9 = MinecraftInstance.mc2;
                    Intrinsics.checkExpressionValueIsNotNull(v9, "mc2");
                    v10 = v9.getConnection();
                    if (v10 == null) {
                        Intrinsics.throwNpe();
                    }
                    v10.sendPacket((Packet)new CPacketHeldItemChange((MinecraftInstance.mc2.player.field_71071_by.currentItem + 1) % 9));
                    v11 = MinecraftInstance.mc2;
                    Intrinsics.checkExpressionValueIsNotNull(v11, "mc2");
                    v12 = v11.getConnection();
                    if (v12 == null) {
                        Intrinsics.throwNpe();
                    }
                    v12.sendPacket((Packet)new CPacketHeldItemChange(MinecraftInstance.mc2.player.field_71071_by.currentItem));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                } else if (event.getEventState() == EventState.PRE) {
                    v13 = MinecraftInstance.mc.getThePlayer();
                    if (v13 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (v13.getItemInUse() != null) {
                        v14 = MinecraftInstance.mc.getThePlayer();
                        if (v14 == null) {
                            Intrinsics.throwNpe();
                        }
                        v15 = v14.getItemInUse();
                        if (v15 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (v15.getItem() != null) {
                            v16 = MinecraftInstance.mc.getThePlayer();
                            if (v16 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (!v16.isBlocking()) {
                                v17 = MinecraftInstance.mc.getThePlayer();
                                if (v17 == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (v17.isUsingItem()) {
                                    v18 = MinecraftInstance.mc.getThePlayer();
                                    if (v18 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    if (v18.getItemInUseCount() >= 1) {
                                        v19 = MinecraftInstance.mc2;
                                        Intrinsics.checkExpressionValueIsNotNull(v19, "mc2");
                                        v20 = v19.getConnection();
                                        if (v20 == null) {
                                            Intrinsics.throwNpe();
                                        }
                                        v20.sendPacket((Packet)new CPacketHeldItemChange((MinecraftInstance.mc2.player.field_71071_by.currentItem + 1) % 9));
                                        v21 = MinecraftInstance.mc2;
                                        Intrinsics.checkExpressionValueIsNotNull(v21, "mc2");
                                        v22 = v21.getConnection();
                                        if (v22 == null) {
                                            Intrinsics.throwNpe();
                                        }
                                        v22.sendPacket((Packet)new CPacketHeldItemChange(MinecraftInstance.mc2.player.field_71071_by.currentItem));
                                    }
                                }
                            }
                        }
                    }
                }
                if (event.getEventState() != EventState.PRE) break;
                v23 = MinecraftInstance.mc.getThePlayer();
                if (v23 == null) {
                    Intrinsics.throwNpe();
                }
                if (v23.getItemInUse() == null) break;
                v24 = MinecraftInstance.mc.getThePlayer();
                if (v24 == null) {
                    Intrinsics.throwNpe();
                }
                v25 = v24.getItemInUse();
                if (v25 == null) {
                    Intrinsics.throwNpe();
                }
                if (v25.getItem() == null) break;
                v26 = MinecraftInstance.mc.getThePlayer();
                if (v26 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v26.isUsingItem()) break;
                v27 = MinecraftInstance.mc.getThePlayer();
                if (v27 == null) {
                    Intrinsics.throwNpe();
                }
                if (v27.getItemInUseCount() < 1) break;
                v28 = MinecraftInstance.mc2;
                Intrinsics.checkExpressionValueIsNotNull(v28, "mc2");
                v29 = v28.getConnection();
                if (v29 == null) {
                    Intrinsics.throwNpe();
                }
                v29.sendPacket((Packet)new CPacketHeldItemChange((MinecraftInstance.mc2.player.field_71071_by.currentItem + 1) % 9));
                v30 = MinecraftInstance.mc2;
                Intrinsics.checkExpressionValueIsNotNull(v30, "mc2");
                v31 = v30.getConnection();
                if (v31 == null) {
                    Intrinsics.throwNpe();
                }
                v31.sendPacket((Packet)new CPacketHeldItemChange(MinecraftInstance.mc2.player.field_71071_by.currentItem));
                break;
            }
            case 4: {
                v32 = MinecraftInstance.mc.getThePlayer();
                if (v32 == null) {
                    Intrinsics.throwNpe();
                }
                v33 = MinecraftInstance.mc.getThePlayer();
                if (v33 == null) {
                    Intrinsics.throwNpe();
                }
                v32.setMotionX(v33.getMotionX());
                v34 = MinecraftInstance.mc.getThePlayer();
                if (v34 == null) {
                    Intrinsics.throwNpe();
                }
                v35 = MinecraftInstance.mc.getThePlayer();
                if (v35 == null) {
                    Intrinsics.throwNpe();
                }
                v34.setMotionY(v35.getMotionY());
                v36 = MinecraftInstance.mc.getThePlayer();
                if (v36 == null) {
                    Intrinsics.throwNpe();
                }
                v37 = MinecraftInstance.mc.getThePlayer();
                if (v37 == null) {
                    Intrinsics.throwNpe();
                }
                v36.setMotionZ(v37.getMotionZ());
                break;
            }
            case 5: {
                if (event.getEventState() != EventState.PRE) break;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                v38 = MinecraftInstance.mc.getNetHandler();
                v39 = MinecraftInstance.mc.getThePlayer();
                if (v39 == null) {
                    Intrinsics.throwNpe();
                }
                v40 = v39.getInventory().getCurrentItemInHand();
                if (v40 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.item.IItemStack");
                }
                v38.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerBlockPlacement(v40));
                break;
            }
            case 1: {
                v41 = MinecraftInstance.mc.getThePlayer();
                if (v41 == null) {
                    Intrinsics.throwNpe();
                }
                if (v41.getTicksExisted() % 3 == 0) {
                    this.sendPacket(event, true, false, false, 0L, false);
                    break;
                }
                this.sendPacket(event, false, true, false, 0L, false);
                break;
            }
            case 6: {
                v42 = MinecraftInstance.mc.getThePlayer();
                if (v42 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v42.isUsingItem()) {
                    v43 = MinecraftInstance.mc.getThePlayer();
                    if (v43 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!v43.isBlocking() && !this.isBlock()) break;
                }
                v44 = MinecraftInstance.mc.getNetHandler();
                v45 = MinecraftInstance.mc.getThePlayer();
                if (v45 == null) {
                    Intrinsics.throwNpe();
                }
                var6_7 = v45.getInventory().getCurrentItemInHand();
                var7_8 = WEnumHand.MAIN_HAND;
                var9_9 = v44;
                $i$f$createUseItemPacket = false;
                var10_11 = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(hand$iv);
                var9_9.addToSendQueue(var10_11);
                v46 = MinecraftInstance.mc.getNetHandler();
                v47 = MinecraftInstance.mc.getThePlayer();
                if (v47 == null) {
                    Intrinsics.throwNpe();
                }
                itemStack$iv = v47.getInventory().getCurrentItemInHand();
                hand$iv = WEnumHand.OFF_HAND;
                var9_9 = v46;
                $i$f$createUseItemPacket = false;
                var10_11 = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(hand$iv);
                var9_9.addToSendQueue(var10_11);
                break;
            }
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IPacket packet = event.getPacket();
        if (this.modeValue.equals("Matrix") || this.modeValue.equals("Vulcan") && this.nextTemp) {
            if ((packet instanceof CPacketPlayerDigging || packet instanceof ICPacketPlayerBlockPlacement) && this.isBlocking()) {
                event.cancelEvent();
            }
            event.cancelEvent();
        } else if (packet instanceof CPacketPlayer || packet instanceof CPacketAnimation || packet instanceof CPacketEntityAction || packet instanceof CPacketUseEntity || packet instanceof CPacketPlayerDigging || packet instanceof ICPacketPlayerBlockPlacement) {
            if (this.modeValue.equals("Vulcan") && this.waitC03 && packet instanceof ICPacketPlayer) {
                this.waitC03 = false;
                return;
            }
            this.packetBuf.add((Packet<INetHandlerPlayServer>)((Packet)packet));
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if ((this.modeValue.equals("Matrix") || this.modeValue.equals("Vulcan")) && (this.lastBlockingStat || this.isBlocking())) {
            if (this.msTimer.hasTimePassed(230L) && this.nextTemp) {
                this.nextTemp = false;
                EnumFacing enumFacing = EnumFacing.DOWN;
                if (enumFacing == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing");
                }
                MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, new WBlockPos(-1, -1, -1), (IEnumFacing)enumFacing);
                Collection collection = this.packetBuf;
                boolean bl = false;
                if (!collection.isEmpty()) {
                    boolean canAttack = false;
                    for (Packet packet : this.packetBuf) {
                        if (packet instanceof CPacketPlayer) {
                            canAttack = true;
                        }
                        if ((packet instanceof ICPacketUseEntity || packet instanceof ICPacketAnimation) && !canAttack) continue;
                        Packet packet2 = packet;
                        Intrinsics.checkExpressionValueIsNotNull(packet2, "packet");
                        PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)packet2);
                    }
                    this.packetBuf.clear();
                }
            }
            if (!this.nextTemp) {
                this.lastBlockingStat = this.isBlocking();
                if (!this.isBlocking()) {
                    return;
                }
                WBlockPos wBlockPos = new WBlockPos(-1, -1, -1);
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                MinecraftInstance.classProvider.createCPacketPlayerBlockPlacement(wBlockPos, 255, (IItemStack)((Object)Integer.valueOf(iEntityPlayerSP.getInventory().getCurrentItem())), 0.0f, 0.0f, 0.0f);
                this.nextTemp = true;
                this.waitC03 = this.modeValue.equals("Vulcan");
                this.msTimer.reset();
            }
        }
    }

    @EventTarget
    public final void onSlowDown(@NotNull SlowDownEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IItemStack iItemStack = iEntityPlayerSP.getHeldItem();
        IItem heldItem = iItemStack != null ? iItemStack.getItem() : null;
        event.setForward(this.getMultiplier(heldItem, true));
        event.setStrafe(this.getMultiplier(heldItem, false));
    }

    private final float getMultiplier(IItem item, boolean isForward) {
        return MinecraftInstance.classProvider.isItemFood(item) || MinecraftInstance.classProvider.isItemPotion(item) || MinecraftInstance.classProvider.isItemBucketMilk(item) ? (isForward ? ((Number)this.consumeForwardMultiplier.get()).floatValue() : ((Number)this.consumeStrafeMultiplier.get()).floatValue()) : (MinecraftInstance.classProvider.isItemSword(item) ? (isForward ? ((Number)this.blockForwardMultiplier.get()).floatValue() : ((Number)this.blockStrafeMultiplier.get()).floatValue()) : (MinecraftInstance.classProvider.isItemBow(item) ? (isForward ? ((Number)this.bowForwardMultiplier.get()).floatValue() : ((Number)this.bowStrafeMultiplier.get()).floatValue()) : 0.2f));
    }

    @Override
    @Nullable
    public String getTag() {
        return (String)this.modeValue.get();
    }

    public NoSlow() {
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
        }
        this.killAura = (KillAura)module;
    }
}
