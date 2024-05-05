/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.enums.WEnumHand;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.SlowDownEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.hyt.Animations;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.render.BlockAnimationUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;

@ModuleInfo(name="NoSlow", category=ModuleCategory.MOVEMENT, description="\u4f60\u5988\u6b7b\u4e86\u662f\u4e0d\u662f\u5c0f\u903c\u5d3d\u5b50")
public final class NoSlow
extends Module {
    private final BoolValue packet = new BoolValue("Packet", false);
    private final MSTimer msTimer = new MSTimer();
    private final ListValue modeValue = new ListValue("PacketMode", new String[]{"HuaYuTing", "AntiCheat", "Custom", "NCP", "Plt", "AAC", "AAC5", "Grim", "NoPacket"}, "HuaYuTing");
    private final FloatValue blockForwardMultiplier = new FloatValue("BlockForwardMultiplier", 1.0f, 0.2f, 1.0f);
    private final FloatValue blockStrafeMultiplier = new FloatValue("BlockStrafeMultiplier", 1.0f, 0.2f, 1.0f);
    private final FloatValue consumeForwardMultiplier = new FloatValue("ConsumeForwardMultiplier", 1.0f, 0.2f, 1.0f);
    private final FloatValue consumeStrafeMultiplier = new FloatValue("ConsumeStrafeMultiplier", 1.0f, 0.2f, 1.0f);
    private final FloatValue bowForwardMultiplier = new FloatValue("BowForwardMultiplier", 1.0f, 0.2f, 1.0f);
    private final FloatValue bowStrafeMultiplier = new FloatValue("BowStrafeMultiplier", 1.0f, 0.2f, 1.0f);
    private final BoolValue customOnGround = new BoolValue("CustomOnGround", false);
    private final IntegerValue customDelayValue = new IntegerValue("CustomDelay", 60, 10, 200);
    private final BoolValue soulsandValue = new BoolValue("Soulsand", false);
    private boolean pendingFlagApplyPacket;
    private final KillAura killAura;

    public final BoolValue getValue() {
        return this.packet;
    }

    public final BoolValue getSoulsandValue() {
        return this.soulsandValue;
    }

    public final boolean getPendingFlagApplyPacket() {
        return this.pendingFlagApplyPacket;
    }

    public final void setPendingFlagApplyPacket(boolean bl) {
        this.pendingFlagApplyPacket = bl;
    }

    @Override
    public void onDisable() {
        this.msTimer.reset();
        this.pendingFlagApplyPacket = false;
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
                if (LiquidBounce.INSTANCE.getModuleManager().get(Animations.class).getState()) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(blockOFF);
                } else {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(blockMain);
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(blockOFF);
                }
                this.msTimer.reset();
            } else if (!delay) {
                if (LiquidBounce.INSTANCE.getModuleManager().get(Animations.class).getState()) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(blockOFF);
                } else {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(blockMain);
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(blockOFF);
                }
            }
        }
    }

    public final KillAura getKillAura() {
        return this.killAura;
    }

    public final boolean isBlock() {
        return BlockAnimationUtils.thePlayerisBlocking != false || this.killAura.getBlockingStatus();
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onMotion(MotionEvent event) {
        if (!MovementUtils.isMoving()) {
            return;
        }
        var2_2 = (String)this.modeValue.get();
        var3_3 = false;
        v0 = var2_2;
        if (v0 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        var2_2 = v0.toLowerCase();
        tmp = -1;
        switch (var2_2.hashCode()) {
            case 96323: {
                if (!var2_2.equals("aac")) break;
                tmp = 1;
                break;
            }
            case -2116882767: {
                if (!var2_2.equals("anticheat")) break;
                tmp = 2;
                break;
            }
            case -1349088399: {
                if (!var2_2.equals("custom")) break;
                tmp = 3;
                break;
            }
            case -1777040898: {
                if (!var2_2.equals("huayuting")) break;
                tmp = 4;
                break;
            }
            case 108891: {
                if (!var2_2.equals("ncp")) break;
                tmp = 5;
                break;
            }
            case 2986066: {
                if (!var2_2.equals("aac5")) break;
                tmp = 6;
                break;
            }
            case 3181391: {
                if (!var2_2.equals("grim")) break;
                tmp = 7;
                break;
            }
        }
        switch (tmp) {
            case 2: {
                this.sendPacket(event, true, false, false, 0L, false);
                v1 = MinecraftInstance.mc.getThePlayer();
                if (v1 == null) {
                    Intrinsics.throwNpe();
                }
                if (v1.getTicksExisted() % 2 != 0) break;
                this.sendPacket(event, false, true, false, 0L, false);
                break;
            }
            case 4: {
                if (event.getEventState() != EventState.PRE) break;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                v2 = MinecraftInstance.mc.getNetHandler();
                v3 = MinecraftInstance.mc.getThePlayer();
                if (v3 == null) {
                    Intrinsics.throwNpe();
                }
                v4 = v3.getInventory().getCurrentItemInHand();
                if (v4 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.item.IItemStack");
                }
                v2.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerBlockPlacement(v4));
                break;
            }
            case 1: {
                v5 = MinecraftInstance.mc.getThePlayer();
                if (v5 == null) {
                    Intrinsics.throwNpe();
                }
                if (v5.getTicksExisted() % 3 == 0) {
                    this.sendPacket(event, true, false, false, 0L, false);
                    break;
                }
                this.sendPacket(event, false, true, false, 0L, false);
                break;
            }
            case 6: {
                v6 = MinecraftInstance.mc.getThePlayer();
                if (v6 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v6.isUsingItem()) {
                    v7 = MinecraftInstance.mc.getThePlayer();
                    if (v7 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!v7.isBlocking() && !this.isBlock()) break;
                }
                v8 = MinecraftInstance.mc.getNetHandler();
                v9 = MinecraftInstance.mc.getThePlayer();
                if (v9 == null) {
                    Intrinsics.throwNpe();
                }
                var3_4 = v9.getInventory().getCurrentItemInHand();
                var4_5 = WEnumHand.MAIN_HAND;
                var6_6 = v8;
                $i$f$createUseItemPacket = false;
                var7_8 = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(hand$iv);
                var6_6.addToSendQueue(var7_8);
                v10 = MinecraftInstance.mc.getNetHandler();
                v11 = MinecraftInstance.mc.getThePlayer();
                if (v11 == null) {
                    Intrinsics.throwNpe();
                }
                itemStack$iv = v11.getInventory().getCurrentItemInHand();
                hand$iv = WEnumHand.OFF_HAND;
                var6_6 = v10;
                $i$f$createUseItemPacket = false;
                var7_8 = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(hand$iv);
                var6_6.addToSendQueue(var7_8);
                break;
            }
            case 3: {
                this.sendPacket(event, true, true, true, ((Number)this.customDelayValue.get()).intValue(), (Boolean)this.customOnGround.get());
                break;
            }
            case 7: {
                if (event.getEventState() != EventState.PRE) ** GOTO lbl129
                v12 = MinecraftInstance.mc.getThePlayer();
                if (v12 == null) {
                    Intrinsics.throwNpe();
                }
                if (v12.getItemInUse() == null) ** GOTO lbl129
                v13 = MinecraftInstance.mc.getThePlayer();
                if (v13 == null) {
                    Intrinsics.throwNpe();
                }
                v14 = v13.getItemInUse();
                if (v14 == null) {
                    Intrinsics.throwNpe();
                }
                if (v14.getItem() == null) ** GOTO lbl129
                v15 = MinecraftInstance.mc.getThePlayer();
                if (v15 == null) {
                    Intrinsics.throwNpe();
                }
                if (v15.isBlocking()) ** GOTO lbl129
                v16 = MinecraftInstance.mc.getThePlayer();
                if (v16 == null) {
                    Intrinsics.throwNpe();
                }
                v17 = v16.getHeldItem();
                if (v17 == null) {
                    Intrinsics.throwNpe();
                }
                if (MinecraftInstance.classProvider.isItemFood(v17.getItem())) ** GOTO lbl136
lbl129:
                // 5 sources

                v18 = MinecraftInstance.mc.getThePlayer();
                if (v18 == null) {
                    Intrinsics.throwNpe();
                }
                v19 = v18.getHeldItem();
                if (v19 == null) {
                    Intrinsics.throwNpe();
                }
                if (!MinecraftInstance.classProvider.isItemPotion(v19.getItem())) ** GOTO lbl152
lbl136:
                // 2 sources

                v20 = MinecraftInstance.mc.getThePlayer();
                if (v20 == null) {
                    Intrinsics.throwNpe();
                }
                if (v20.isUsingItem()) {
                    v21 = MinecraftInstance.mc.getThePlayer();
                    if (v21 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (v21.getItemInUseCount() >= 1) {
                        v22 = MinecraftInstance.mc2.func_147114_u();
                        if (v22 == null) {
                            Intrinsics.throwNpe();
                        }
                        v22.func_147297_a((Packet)new CPacketHeldItemChange((MinecraftInstance.mc2.field_71439_g.field_71071_by.field_70461_c + 1) % 9));
                        v23 = MinecraftInstance.mc2.func_147114_u();
                        if (v23 == null) {
                            Intrinsics.throwNpe();
                        }
                        v23.func_147297_a((Packet)new CPacketHeldItemChange(MinecraftInstance.mc2.field_71439_g.field_71071_by.field_70461_c));
                    }
                }
lbl152:
                // 6 sources

                if (event.getEventState() != EventState.PRE) break;
                v24 = MinecraftInstance.mc.getThePlayer();
                if (v24 == null) {
                    Intrinsics.throwNpe();
                }
                v25 = v24.getHeldItem();
                if (v25 == null) {
                    Intrinsics.throwNpe();
                }
                if (!MinecraftInstance.classProvider.isItemSword(v25.getItem())) break;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                v26 = MinecraftInstance.mc.getNetHandler();
                v27 = MinecraftInstance.mc.getThePlayer();
                if (v27 == null) {
                    Intrinsics.throwNpe();
                }
                v28 = v27.getInventory().getCurrentItemInHand();
                if (v28 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.item.IItemStack");
                }
                v26.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerBlockPlacement(v28));
                break;
            }
            case 5: {
                this.sendPacket(event, true, true, false, 0L, false);
            }
        }
    }

    @EventTarget
    public final void onSlowDown(SlowDownEvent event) {
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

    public NoSlow() {
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
        }
        this.killAura = (KillAura)module;
    }
}

