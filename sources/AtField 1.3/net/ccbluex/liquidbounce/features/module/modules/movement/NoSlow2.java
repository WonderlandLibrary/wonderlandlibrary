/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
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
import net.ccbluex.liquidbounce.features.module.modules.render.Animations;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

@ModuleInfo(name="NoSlow2", category=ModuleCategory.MOVEMENT, description="Skid by Autumn")
public final class NoSlow2
extends Module {
    private boolean pendingFlagApplyPacket;
    private final BoolValue soulsandValue;
    private final FloatValue bowStrafeMultiplier;
    private final MSTimer msTimer;
    private final IntegerValue customDelayValue;
    private final ListValue modeValue;
    private final FloatValue blockForwardMultiplier;
    private final FloatValue blockStrafeMultiplier;
    private final FloatValue consumeStrafeMultiplier;
    private final KillAura killAura;
    private final FloatValue consumeForwardMultiplier;
    private final FloatValue bowForwardMultiplier;
    private final BoolValue customOnGround;
    private final BoolValue packet = new BoolValue("Packet", false);

    @EventTarget
    public final void onSlowDown(SlowDownEvent slowDownEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IItemStack iItemStack = iEntityPlayerSP.getHeldItem();
        IItem iItem = iItemStack != null ? iItemStack.getItem() : null;
        slowDownEvent.setForward(this.getMultiplier(iItem, true));
        slowDownEvent.setStrafe(this.getMultiplier(iItem, false));
    }

    public final BoolValue getValue() {
        return this.packet;
    }

    public NoSlow2() {
        this.msTimer = new MSTimer();
        this.modeValue = new ListValue("PacketMode", new String[]{"Hyt", "AntiCheat", "Custom", "NCP", "Plt", "AAC", "AAC5", "Autumn4v4", "AutumnPlt", "HytNewFix"}, "Plt");
        this.blockForwardMultiplier = new FloatValue("BlockForwardMultiplier", 1.0f, 0.2f, 1.0f);
        this.blockStrafeMultiplier = new FloatValue("BlockStrafeMultiplier", 1.0f, 0.2f, 1.0f);
        this.consumeForwardMultiplier = new FloatValue("ConsumeForwardMultiplier", 1.0f, 0.2f, 1.0f);
        this.consumeStrafeMultiplier = new FloatValue("ConsumeStrafeMultiplier", 1.0f, 0.2f, 1.0f);
        this.bowForwardMultiplier = new FloatValue("BowForwardMultiplier", 1.0f, 0.2f, 1.0f);
        this.bowStrafeMultiplier = new FloatValue("BowStrafeMultiplier", 1.0f, 0.2f, 1.0f);
        this.customOnGround = new BoolValue("CustomOnGround", false);
        this.customDelayValue = new IntegerValue("CustomDelay", 60, 10, 200);
        this.soulsandValue = new BoolValue("Soulsand", false);
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
        }
        this.killAura = (KillAura)module;
    }

    public final boolean getPendingFlagApplyPacket() {
        return this.pendingFlagApplyPacket;
    }

    public final void setPendingFlagApplyPacket(boolean bl) {
        this.pendingFlagApplyPacket = bl;
    }

    private final float getMultiplier(IItem iItem, boolean bl) {
        return MinecraftInstance.classProvider.isItemFood(iItem) || MinecraftInstance.classProvider.isItemPotion(iItem) || MinecraftInstance.classProvider.isItemBucketMilk(iItem) ? (bl ? ((Number)this.consumeForwardMultiplier.get()).floatValue() : ((Number)this.consumeStrafeMultiplier.get()).floatValue()) : (MinecraftInstance.classProvider.isItemSword(iItem) ? (bl ? ((Number)this.blockForwardMultiplier.get()).floatValue() : ((Number)this.blockStrafeMultiplier.get()).floatValue()) : (MinecraftInstance.classProvider.isItemBow(iItem) ? (bl ? ((Number)this.bowForwardMultiplier.get()).floatValue() : ((Number)this.bowStrafeMultiplier.get()).floatValue()) : 0.2f));
    }

    @Override
    public void onDisable() {
        this.msTimer.reset();
        this.pendingFlagApplyPacket = false;
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    private final void sendPacket(MotionEvent motionEvent, boolean bl, boolean bl2, boolean bl3, long l, boolean bl4) {
        IPacket iPacket = MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, new WBlockPos(-1, -1, -1), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN));
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        Object object = iEntityPlayerSP.getInventory().getCurrentItemInHand();
        Object object2 = WEnumHand.MAIN_HAND;
        boolean bl5 = false;
        IPacket iPacket2 = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem((WEnumHand)((Object)object2));
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        object2 = iEntityPlayerSP2.getInventory().getCurrentItemInHand();
        WEnumHand wEnumHand = WEnumHand.OFF_HAND;
        boolean bl6 = false;
        object = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(wEnumHand);
        if (bl4) {
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            if (!iEntityPlayerSP3.getOnGround()) {
                return;
            }
        }
        if (bl && motionEvent.getEventState() == EventState.PRE) {
            if (bl3 && this.msTimer.hasTimePassed(l)) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(iPacket);
            } else if (!bl3) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(iPacket);
            }
        }
        if (bl2 && motionEvent.getEventState() == EventState.POST) {
            if (bl3 && this.msTimer.hasTimePassed(l)) {
                if (LiquidBounce.INSTANCE.getModuleManager().get(Animations.class).getState()) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue((IPacket)object);
                } else {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(iPacket2);
                    MinecraftInstance.mc.getNetHandler().addToSendQueue((IPacket)object);
                }
                this.msTimer.reset();
            } else if (!bl3) {
                if (LiquidBounce.INSTANCE.getModuleManager().get(Animations.class).getState()) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue((IPacket)object);
                } else {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(iPacket2);
                    MinecraftInstance.mc.getNetHandler().addToSendQueue((IPacket)object);
                }
            }
        }
    }

    public final KillAura getKillAura() {
        return this.killAura;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onMotion(MotionEvent var1_1) {
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
            case 103811: {
                if (!var2_2.equals("hyt")) break;
                tmp = 1;
                break;
            }
            case 252323256: {
                if (!var2_2.equals("hytnewfix")) break;
                tmp = 2;
                break;
            }
            case 96323: {
                if (!var2_2.equals("aac")) break;
                tmp = 3;
                break;
            }
            case -2116882767: {
                if (!var2_2.equals("anticheat")) break;
                tmp = 4;
                break;
            }
            case 1845829666: {
                if (!var2_2.equals("autumnplt")) break;
                tmp = 5;
                break;
            }
            case -1349088399: {
                if (!var2_2.equals("custom")) break;
                tmp = 6;
                break;
            }
            case 1845772252: {
                if (!var2_2.equals("autumn4v4")) break;
                tmp = 7;
                break;
            }
            case 108891: {
                if (!var2_2.equals("ncp")) break;
                tmp = 8;
                break;
            }
            case 2986066: {
                if (!var2_2.equals("aac5")) break;
                tmp = 9;
                break;
            }
        }
        switch (tmp) {
            case 4: {
                this.sendPacket(var1_1, true, false, false, 0L, false);
                v1 = MinecraftInstance.mc.getThePlayer();
                if (v1 == null) {
                    Intrinsics.throwNpe();
                }
                if (v1.getTicksExisted() % 2 != 0) break;
                this.sendPacket(var1_1, false, true, false, 0L, false);
                break;
            }
            case 1: {
                if (var1_1.getEventState() != EventState.PRE) break;
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
            case 2: {
                if (var1_1.getEventState() == EventState.PRE) {
                    v5 = MinecraftInstance.mc.getThePlayer();
                    if (v5 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (v5.getItemInUse() != null) {
                        v6 = MinecraftInstance.mc.getThePlayer();
                        if (v6 == null) {
                            Intrinsics.throwNpe();
                        }
                        v7 = v6.getItemInUse();
                        if (v7 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (v7.getItem() != null) {
                            v8 = MinecraftInstance.mc.getThePlayer();
                            if (v8 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (!MinecraftInstance.classProvider.isItemSword(v8.getInventory().getCurrentItemInHand())) {
                                v9 = MinecraftInstance.mc.getThePlayer();
                                if (v9 == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (v9.isUsingItem()) {
                                    v10 = MinecraftInstance.mc.getThePlayer();
                                    if (v10 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    if (v10.getItemInUseCount() >= 1) {
                                        v11 = MinecraftInstance.mc2.func_147114_u();
                                        if (v11 == null) {
                                            Intrinsics.throwNpe();
                                        }
                                        v11.func_147297_a((Packet)new CPacketHeldItemChange((MinecraftInstance.mc2.field_71439_g.field_71071_by.field_70461_c + 1) % 9));
                                        v12 = MinecraftInstance.mc2.func_147114_u();
                                        if (v12 == null) {
                                            Intrinsics.throwNpe();
                                        }
                                        v12.func_147297_a((Packet)new CPacketHeldItemChange(MinecraftInstance.mc2.field_71439_g.field_71071_by.field_70461_c));
                                    }
                                }
                            }
                        }
                    }
                }
                if (var1_1.getEventState() != EventState.PRE) break;
                v13 = MinecraftInstance.mc.getThePlayer();
                if (v13 == null) {
                    Intrinsics.throwNpe();
                }
                if (v13.getItemInUse() == null) break;
                v14 = MinecraftInstance.mc.getThePlayer();
                if (v14 == null) {
                    Intrinsics.throwNpe();
                }
                v15 = v14.getItemInUse();
                if (v15 == null) {
                    Intrinsics.throwNpe();
                }
                if (v15.getItem() == null) break;
                v16 = MinecraftInstance.mc.getThePlayer();
                if (v16 == null) {
                    Intrinsics.throwNpe();
                }
                if (!MinecraftInstance.classProvider.isItemSword(v16.getInventory().getCurrentItemInHand())) break;
                v17 = MinecraftInstance.mc.getThePlayer();
                if (v17 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v17.isUsingItem()) {
                    v18 = MinecraftInstance.mc.getThePlayer();
                    if (v18 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!v18.isBlocking() && this != false) break;
                }
                v19 = MinecraftInstance.mc.getNetHandler();
                v20 = MinecraftInstance.mc.getThePlayer();
                if (v20 == null) {
                    Intrinsics.throwNpe();
                }
                v21 = v20.getInventory().getCurrentItemInHand();
                if (v21 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.item.IItemStack");
                }
                v19.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerBlockPlacement(v21));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                break;
            }
            case 5: {
                if (var1_1.getEventState() != EventState.PRE) break;
                v22 = MinecraftInstance.mc2.func_147114_u();
                if (v22 == null) {
                    Intrinsics.throwNpe();
                }
                v22.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(new BlockPos(-1, -1, -1), EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                break;
            }
            case 7: {
                if (var1_1.getEventState() != EventState.PRE) ** GOTO lbl-1000
                v23 = MinecraftInstance.mc.getThePlayer();
                if (v23 == null) {
                    Intrinsics.throwNpe();
                }
                if (v23.isBlocking()) ** GOTO lbl-1000
                v24 = MinecraftInstance.mc.getThePlayer();
                if (v24 == null) {
                    Intrinsics.throwNpe();
                }
                v25 = v24.getHeldItem();
                if (v25 == null) {
                    Intrinsics.throwNpe();
                }
                if (MinecraftInstance.classProvider.isItemPotion(v25.getItem())) ** GOTO lbl-1000
                v26 = MinecraftInstance.mc.getThePlayer();
                if (v26 == null) {
                    Intrinsics.throwNpe();
                }
                v27 = v26.getHeldItem();
                if (v27 == null) {
                    Intrinsics.throwNpe();
                }
                if (!MinecraftInstance.classProvider.isItemFood(v27.getItem())) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                    v28 = MinecraftInstance.mc.getNetHandler();
                    v29 = MinecraftInstance.mc.getThePlayer();
                    if (v29 == null) {
                        Intrinsics.throwNpe();
                    }
                    v30 = v29.getInventory().getCurrentItemInHand();
                    if (v30 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.item.IItemStack");
                    }
                    v28.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerBlockPlacement(v30));
                } else if (var1_1.getEventState() == EventState.PRE) {
                    v31 = MinecraftInstance.mc.getThePlayer();
                    if (v31 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (v31.getItemInUse() != null) {
                        v32 = MinecraftInstance.mc.getThePlayer();
                        if (v32 == null) {
                            Intrinsics.throwNpe();
                        }
                        v33 = v32.getItemInUse();
                        if (v33 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (v33.getItem() != null) {
                            v34 = MinecraftInstance.mc.getThePlayer();
                            if (v34 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (!v34.isBlocking()) {
                                v35 = MinecraftInstance.mc.getThePlayer();
                                if (v35 == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (v35.isUsingItem()) {
                                    v36 = MinecraftInstance.mc.getThePlayer();
                                    if (v36 == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    if (v36.getItemInUseCount() >= 1) {
                                        v37 = MinecraftInstance.mc2.func_147114_u();
                                        if (v37 == null) {
                                            Intrinsics.throwNpe();
                                        }
                                        v37.func_147297_a((Packet)new CPacketHeldItemChange((MinecraftInstance.mc2.field_71439_g.field_71071_by.field_70461_c + 1) % 9));
                                        v38 = MinecraftInstance.mc2.func_147114_u();
                                        if (v38 == null) {
                                            Intrinsics.throwNpe();
                                        }
                                        v38.func_147297_a((Packet)new CPacketHeldItemChange(MinecraftInstance.mc2.field_71439_g.field_71071_by.field_70461_c));
                                    }
                                }
                            }
                        }
                    }
                }
                if (var1_1.getEventState() != EventState.PRE) break;
                v39 = MinecraftInstance.mc.getThePlayer();
                if (v39 == null) {
                    Intrinsics.throwNpe();
                }
                if (v39.getItemInUse() == null) break;
                v40 = MinecraftInstance.mc.getThePlayer();
                if (v40 == null) {
                    Intrinsics.throwNpe();
                }
                v41 = v40.getItemInUse();
                if (v41 == null) {
                    Intrinsics.throwNpe();
                }
                if (v41.getItem() == null) break;
                v42 = MinecraftInstance.mc.getThePlayer();
                if (v42 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v42.isUsingItem()) break;
                v43 = MinecraftInstance.mc.getThePlayer();
                if (v43 == null) {
                    Intrinsics.throwNpe();
                }
                if (v43.getItemInUseCount() < 1) break;
                v44 = MinecraftInstance.mc2.func_147114_u();
                if (v44 == null) {
                    Intrinsics.throwNpe();
                }
                v44.func_147297_a((Packet)new CPacketHeldItemChange((MinecraftInstance.mc2.field_71439_g.field_71071_by.field_70461_c + 1) % 9));
                v45 = MinecraftInstance.mc2.func_147114_u();
                if (v45 == null) {
                    Intrinsics.throwNpe();
                }
                v45.func_147297_a((Packet)new CPacketHeldItemChange(MinecraftInstance.mc2.field_71439_g.field_71071_by.field_70461_c));
                break;
            }
            case 3: {
                v46 = MinecraftInstance.mc.getThePlayer();
                if (v46 == null) {
                    Intrinsics.throwNpe();
                }
                if (v46.getTicksExisted() % 3 == 0) {
                    this.sendPacket(var1_1, true, false, false, 0L, false);
                    break;
                }
                this.sendPacket(var1_1, false, true, false, 0L, false);
                break;
            }
            case 9: {
                v47 = MinecraftInstance.mc.getThePlayer();
                if (v47 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v47.isUsingItem()) {
                    v48 = MinecraftInstance.mc.getThePlayer();
                    if (v48 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!v48.isBlocking() && this != false) break;
                }
                v49 = MinecraftInstance.mc.getNetHandler();
                v50 = MinecraftInstance.mc.getThePlayer();
                if (v50 == null) {
                    Intrinsics.throwNpe();
                }
                var3_4 = v50.getInventory().getCurrentItemInHand();
                var4_5 = WEnumHand.MAIN_HAND;
                var6_6 = v49;
                var5_7 = false;
                var7_8 = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(var4_5);
                var6_6.addToSendQueue(var7_8);
                v51 = MinecraftInstance.mc.getNetHandler();
                v52 = MinecraftInstance.mc.getThePlayer();
                if (v52 == null) {
                    Intrinsics.throwNpe();
                }
                var3_4 = v52.getInventory().getCurrentItemInHand();
                var4_5 = WEnumHand.OFF_HAND;
                var6_6 = v51;
                var5_7 = false;
                var7_8 = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(var4_5);
                var6_6.addToSendQueue(var7_8);
                break;
            }
            case 6: {
                this.sendPacket(var1_1, true, true, true, ((Number)this.customDelayValue.get()).intValue(), (Boolean)this.customOnGround.get());
                break;
            }
            case 8: {
                this.sendPacket(var1_1, true, true, false, 0L, false);
                break;
            }
        }
    }

    public final BoolValue getSoulsandValue() {
        return this.soulsandValue;
    }
}

