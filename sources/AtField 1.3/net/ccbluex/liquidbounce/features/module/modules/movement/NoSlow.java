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
import net.ccbluex.liquidbounce.features.module.modules.render.Animations;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.ccbluex.liquidbounce.utils.render.BlockAnimationUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;

@ModuleInfo(name="NoSlow", description="\u53d6\u6d88\u7075\u9b42\u6c99\u548c\u4f7f\u7528\u7269\u54c1\u65f6\u9020\u6210\u7684\u51cf\u901f\u6548\u679c", category=ModuleCategory.MOVEMENT)
public final class NoSlow
extends Module {
    private final ListValue modeValue;
    private final IntegerValue customDelayValue;
    private final FloatValue consumeStrafeMultiplier;
    private final FloatValue bowForwardMultiplier;
    private final KillAura killAura;
    private final BoolValue soulsandValue;
    private final FloatValue consumeForwardMultiplier;
    private final MSTimer msTimer = new MSTimer();
    private final FloatValue bowStrafeMultiplier;
    private boolean pendingFlagApplyPacket;
    private final BoolValue customOnGround;
    private final FloatValue blockForwardMultiplier;
    private final FloatValue blockStrafeMultiplier;

    public final boolean isBlock() {
        return BlockAnimationUtils.thePlayerisBlocking != false || this.killAura.getBlockingStatus();
    }

    public final KillAura getKillAura() {
        return this.killAura;
    }

    private final float getMultiplier(IItem iItem, boolean bl) {
        return MinecraftInstance.classProvider.isItemFood(iItem) || MinecraftInstance.classProvider.isItemPotion(iItem) || MinecraftInstance.classProvider.isItemBucketMilk(iItem) ? (bl ? ((Number)this.consumeForwardMultiplier.get()).floatValue() : ((Number)this.consumeStrafeMultiplier.get()).floatValue()) : (MinecraftInstance.classProvider.isItemSword(iItem) ? (bl ? ((Number)this.blockForwardMultiplier.get()).floatValue() : ((Number)this.blockStrafeMultiplier.get()).floatValue()) : (MinecraftInstance.classProvider.isItemBow(iItem) ? (bl ? ((Number)this.bowForwardMultiplier.get()).floatValue() : ((Number)this.bowStrafeMultiplier.get()).floatValue()) : 0.2f));
    }

    public final boolean getPendingFlagApplyPacket() {
        return this.pendingFlagApplyPacket;
    }

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

    @Override
    public void onDisable() {
        this.msTimer.reset();
        this.pendingFlagApplyPacket = false;
    }

    public final void setPendingFlagApplyPacket(boolean bl) {
        this.pendingFlagApplyPacket = bl;
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
        var3_3 = 0;
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
            case 1931157809: {
                if (!var2_2.equals("hyt-vanilla")) break;
                tmp = 4;
                break;
            }
            case 108891: {
                if (!var2_2.equals("ncp")) break;
                tmp = 5;
                break;
            }
            case 3181391: {
                if (!var2_2.equals("grim")) break;
                tmp = 6;
                break;
            }
            case 2986066: {
                if (!var2_2.equals("aac5")) break;
                tmp = 7;
                break;
            }
        }
        switch (tmp) {
            case 6: {
                if (var1_1.getEventState() != EventState.PRE) ** GOTO lbl58
                v1 = MinecraftInstance.mc.getThePlayer();
                if (v1 == null) {
                    Intrinsics.throwNpe();
                }
                if (v1.getItemInUse() == null) ** GOTO lbl58
                v2 = MinecraftInstance.mc.getThePlayer();
                if (v2 == null) {
                    Intrinsics.throwNpe();
                }
                v3 = v2.getItemInUse();
                if (v3 == null) {
                    Intrinsics.throwNpe();
                }
                if (v3.getItem() == null) ** GOTO lbl58
                v4 = MinecraftInstance.mc.getThePlayer();
                if (v4 == null) {
                    Intrinsics.throwNpe();
                }
                if (v4.isBlocking()) ** GOTO lbl59
lbl58:
                // 4 sources

                if (!BlockAnimationUtils.thePlayerisBlocking.booleanValue() && !this.killAura.getBlockingStatus()) ** GOTO lbl73
lbl59:
                // 2 sources

                v5 = MinecraftInstance.mc.getThePlayer();
                if (v5 == null) {
                    Intrinsics.throwNpe();
                }
                if (v5.isUsingItem()) {
                    v6 = MinecraftInstance.mc.getThePlayer();
                    if (v6 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (v6.getItemInUseCount() >= 1) {
                        v7 = MinecraftInstance.mc.getThePlayer();
                        if (v7 == null) {
                            Intrinsics.throwNpe();
                        }
                        var4_6 = (var3_3 = v7.getInventory().getCurrentItem()) == 0 ? 1 : -1;
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(var3_3 + var4_6));
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(var3_3));
                    }
                }
lbl73:
                // 6 sources

                if (var1_1.getEventState() == EventState.PRE) {
                    v8 = MinecraftInstance.mc.getThePlayer();
                    if (v8 == null) {
                        Intrinsics.throwNpe();
                    }
                    v9 = v8.getHeldItem();
                    if (v9 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (MinecraftInstance.classProvider.isItemSword(v9.getItem())) {
                        v10 = MinecraftInstance.mc.getNetHandler();
                        v11 = MinecraftInstance.mc.getThePlayer();
                        if (v11 == null) {
                            Intrinsics.throwNpe();
                        }
                        v10.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerBlockPlacement(v11.getInventory().getCurrentItemInHand()));
                    }
                }
                v12 = MinecraftInstance.mc.getThePlayer();
                if (v12 == null) {
                    Intrinsics.throwNpe();
                }
                v13 = v12.getHeldItem();
                v14 = var3_4 = v13 != null ? v13.getItem() : null;
                if ((var1_1.getEventState() != EventState.PRE || !MinecraftInstance.classProvider.isItemFood(var3_4)) && !MinecraftInstance.classProvider.isItemPotion(var3_4) && !MinecraftInstance.classProvider.isItemBucketMilk(var3_4)) break;
                v15 = MinecraftInstance.mc.getThePlayer();
                if (v15 == null) {
                    Intrinsics.throwNpe();
                }
                var5_8 = (var4_6 = v15.getInventory().getCurrentItem()) == 0 ? 1 : -1;
                PacketUtils.INSTANCE.sendPacketNoEvent((Packet)new CPacketHeldItemChange(var4_6 + var5_8));
                PacketUtils.INSTANCE.sendPacketNoEvent((Packet)new CPacketHeldItemChange(var4_6));
                break;
            }
            case 4: {
                v16 = MinecraftInstance.mc.getThePlayer();
                if (v16 == null) {
                    Intrinsics.throwNpe();
                }
                v17 = v16.getHeldItem();
                v18 = var3_5 = v17 != null ? v17.getItem() : null;
                if ((var1_1.getEventState() != EventState.PRE || !MinecraftInstance.classProvider.isItemFood(var3_5)) && !MinecraftInstance.classProvider.isItemPotion(var3_5) && !MinecraftInstance.classProvider.isItemBucketMilk(var3_5)) break;
                v19 = MinecraftInstance.mc.getThePlayer();
                if (v19 == null) {
                    Intrinsics.throwNpe();
                }
                var5_9 = (var4_7 = v19.getInventory().getCurrentItem()) == 0 ? 1 : -1;
                PacketUtils.INSTANCE.sendPacketNoEvent((Packet)new CPacketHeldItemChange(var4_7 + var5_9));
                PacketUtils.INSTANCE.sendPacketNoEvent((Packet)new CPacketHeldItemChange(var4_7));
                break;
            }
            case 2: {
                this.sendPacket(var1_1, true, false, false, 0L, false);
                v20 = MinecraftInstance.mc.getThePlayer();
                if (v20 == null) {
                    Intrinsics.throwNpe();
                }
                if (v20.getTicksExisted() % 2 != 0) break;
                this.sendPacket(var1_1, false, true, false, 0L, false);
                break;
            }
            case 1: {
                v21 = MinecraftInstance.mc.getThePlayer();
                if (v21 == null) {
                    Intrinsics.throwNpe();
                }
                if (v21.getTicksExisted() % 3 == 0) {
                    this.sendPacket(var1_1, true, false, false, 0L, false);
                    break;
                }
                this.sendPacket(var1_1, false, true, false, 0L, false);
                break;
            }
            case 7: {
                if (var1_1.getEventState() != EventState.POST) break;
                v22 = MinecraftInstance.mc.getThePlayer();
                if (v22 == null) {
                    Intrinsics.throwNpe();
                }
                if (!v22.isUsingItem()) {
                    v23 = MinecraftInstance.mc.getThePlayer();
                    if (v23 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!v23.isBlocking() && !this.killAura.getBlockingStatus()) break;
                }
                v24 = MinecraftInstance.mc.getNetHandler();
                v25 = new WBlockPos(-1, -1, -1);
                v26 = MinecraftInstance.mc.getThePlayer();
                if (v26 == null) {
                    Intrinsics.throwNpe();
                }
                v24.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerBlockPlacement(v25, 255, v26.getInventory().getCurrentItemInHand(), 0.0f, 0.0f, 0.0f));
                break;
            }
            case 3: {
                this.sendPacket(var1_1, true, true, true, ((Number)this.customDelayValue.get()).intValue(), (Boolean)this.customOnGround.get());
                break;
            }
            case 5: {
                this.sendPacket(var1_1, true, true, false, 0L, false);
                break;
            }
        }
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

    public final BoolValue getSoulsandValue() {
        return this.soulsandValue;
    }

    public NoSlow() {
        this.modeValue = new ListValue("PacketMode", new String[]{"AntiCheat", "Custom", "NCP", "Vanilla", "AAC", "AAC5", "Grim", "Hyt-Vanilla"}, "Vanilla");
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
}

