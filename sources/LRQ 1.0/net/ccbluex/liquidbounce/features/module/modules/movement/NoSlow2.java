/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketUseEntity
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.util.LinkedList;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.enums.WEnumHand;
import net.ccbluex.liquidbounce.api.minecraft.INetworkManager;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.SlowDownEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.hyt.Animations;
import net.ccbluex.liquidbounce.injection.backend.ItemImpl;
import net.ccbluex.liquidbounce.injection.backend.NetworkManagerImpl;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;

@ModuleInfo(name="NoSlow2", description="FDPhyt\u662f\u8dd1\u5403\uff01 Hytab\u662f\u9632\u780d\uff01 By Tiangong ", category=ModuleCategory.HYT)
public final class NoSlow2
extends Module {
    private final MSTimer msTimer = new MSTimer();
    private final ListValue modeValue = new ListValue("PacketMode", new String[]{"FDPHyt", "AntiCheat", "HytAB", "Custom", "NCP", "Vanilla", "AAC"}, "Vanilla");
    private final FloatValue blockForwardMultiplier = new FloatValue("BlockForwardMultiplier", 1.0f, 0.2f, 1.0f);
    private final FloatValue blockStrafeMultiplier = new FloatValue("BlockStrafeMultiplier", 1.0f, 0.2f, 1.0f);
    private final FloatValue consumeForwardMultiplier = new FloatValue("ConsumeForwardMultiplier", 1.0f, 0.2f, 1.0f);
    private final FloatValue consumeStrafeMultiplier = new FloatValue("ConsumeStrafeMultiplier", 1.0f, 0.2f, 1.0f);
    private final FloatValue bowForwardMultiplier = new FloatValue("BowForwardMultiplier", 1.0f, 0.2f, 1.0f);
    private final FloatValue bowStrafeMultiplier = new FloatValue("BowStrafeMultiplier", 1.0f, 0.2f, 1.0f);
    private final BoolValue customOnGround = new BoolValue("CustomOnGround", false);
    private final IntegerValue customDelayValue = new IntegerValue("CustomDelay", 60, 10, 200);
    private final IntegerValue blinkValue = new IntegerValue("Blink Delay", 230, 10, 1000);
    private LinkedList<Packet<INetHandlerPlayServer>> packetBuf = new LinkedList();
    private boolean nextTemp;
    private boolean waitC03;
    private boolean lastBlockingStat;
    private final BoolValue soulsandValue = new BoolValue("Soulsand", false);
    private boolean pendingFlagApplyPacket;
    private final KillAura killAura;

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

    @EventTarget
    public final void onMotion(MotionEvent event) {
        if (!MovementUtils.isMoving()) {
            return;
        }
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string2.toLowerCase()) {
            case "anticheat": {
                this.sendPacket(event, true, false, false, 0L, false);
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP.getTicksExisted() % 2 != 0) break;
                this.sendPacket(event, false, true, false, 0L, false);
                break;
            }
            case "hytab": {
                if (event.getEventState() != EventState.PRE) break;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                IItemStack iItemStack = iEntityPlayerSP.getInventory().getCurrentItemInHand();
                if (iItemStack == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.item.IItemStack");
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerBlockPlacement(iItemStack));
                break;
            }
            case "aac": {
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP.getTicksExisted() % 3 == 0) {
                    this.sendPacket(event, true, false, false, 0L, false);
                    break;
                }
                this.sendPacket(event, false, true, false, 0L, false);
                break;
            }
            case "custom": {
                this.sendPacket(event, true, true, true, ((Number)this.customDelayValue.get()).intValue(), (Boolean)this.customOnGround.get());
                break;
            }
            case "ncp": {
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

    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        IItemStack iItemStack = thePlayer.getHeldItem();
        if (iItemStack == null) {
            return;
        }
        IItemStack heldItem = iItemStack;
        if (MinecraftInstance.mc.getThePlayer() == null || MinecraftInstance.mc.getTheWorld() == null) {
            return;
        }
        Object object = (String)this.modeValue.get();
        boolean bl = false;
        String string = object;
        if (string == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        if (string.toLowerCase().equals("fdphyt") && (this.lastBlockingStat || this.isEatting())) {
            if (this.msTimer.hasTimePassed(((Number)this.blinkValue.get()).intValue()) && this.nextTemp) {
                this.nextTemp = false;
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                INetworkManager iNetworkManager = iEntityPlayerSP2.getSendQueue().getNetworkManager();
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                iNetworkManager.sendPacket(MinecraftInstance.classProvider.createCPacketHeldItemChange((iEntityPlayerSP3.getInventory().getCurrentItem() + 1) % 9));
                IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP4 == null) {
                    Intrinsics.throwNpe();
                }
                INetworkManager iNetworkManager2 = iEntityPlayerSP4.getSendQueue().getNetworkManager();
                IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP5 == null) {
                    Intrinsics.throwNpe();
                }
                iNetworkManager2.sendPacket(MinecraftInstance.classProvider.createCPacketHeldItemChange(iEntityPlayerSP5.getInventory().getCurrentItem()));
                object = this.packetBuf;
                boolean bl2 = false;
                if (!object.isEmpty()) {
                    boolean canAttack = false;
                    for (Packet packet : this.packetBuf) {
                        if (packet instanceof CPacketPlayer) {
                            canAttack = true;
                        }
                        if ((packet instanceof CPacketUseEntity || packet instanceof CPacketAnimation) && !canAttack) continue;
                        IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP6 == null) {
                            Intrinsics.throwNpe();
                        }
                        INetworkManager $this$unwrap$iv = iEntityPlayerSP6.getSendQueue().getNetworkManager();
                        boolean $i$f$unwrap = false;
                        INetworkManager iNetworkManager3 = $this$unwrap$iv;
                        if (iNetworkManager3 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.NetworkManagerImpl");
                        }
                        ((NetworkManagerImpl)iNetworkManager3).getWrapped().func_179290_a(packet);
                    }
                    this.packetBuf.clear();
                }
            }
            if (!this.nextTemp) {
                this.lastBlockingStat = this.isEatting();
                if (!this.isEatting()) {
                    return;
                }
                IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP7 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP7.getSendQueue().getNetworkManager().sendPacket(MinecraftInstance.classProvider.createCPacketTryUseItem(WEnumHand.MAIN_HAND));
                this.nextTemp = true;
                this.waitC03 = false;
                this.msTimer.reset();
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final boolean isEatting() {
        boolean $i$f$unwrap;
        IItem $this$unwrap$iv;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.isUsingItem()) {
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP2.getHeldItem() != null) {
                IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP3 == null) {
                    Intrinsics.throwNpe();
                }
                IItemStack iItemStack = iEntityPlayerSP3.getHeldItem();
                if (iItemStack == null) {
                    Intrinsics.throwNpe();
                }
                IItem iItem = iItemStack.getItem();
                if (iItem == null) {
                    Intrinsics.throwNpe();
                }
                $this$unwrap$iv = iItem;
                $i$f$unwrap = false;
                IItem iItem2 = $this$unwrap$iv;
                if (iItem2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.ItemImpl<*>");
                }
                if (((ItemImpl)iItem2).getWrapped() instanceof ItemFood) return true;
            }
        }
        IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP4 == null) {
            Intrinsics.throwNpe();
        }
        IItemStack iItemStack = iEntityPlayerSP4.getHeldItem();
        if (iItemStack == null) {
            Intrinsics.throwNpe();
        }
        IItem iItem = iItemStack.getItem();
        if (iItem == null) {
            Intrinsics.throwNpe();
        }
        $this$unwrap$iv = iItem;
        $i$f$unwrap = false;
        IItem iItem3 = $this$unwrap$iv;
        if (iItem3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.ItemImpl<*>");
        }
        if (!(((ItemImpl)iItem3).getWrapped() instanceof ItemPotion)) return false;
        return true;
    }

    private final float getMultiplier(IItem item, boolean isForward) {
        return MinecraftInstance.classProvider.isItemFood(item) || MinecraftInstance.classProvider.isItemPotion(item) || MinecraftInstance.classProvider.isItemBucketMilk(item) ? (isForward ? ((Number)this.consumeForwardMultiplier.get()).floatValue() : ((Number)this.consumeStrafeMultiplier.get()).floatValue()) : (MinecraftInstance.classProvider.isItemSword(item) ? (isForward ? ((Number)this.blockForwardMultiplier.get()).floatValue() : ((Number)this.blockStrafeMultiplier.get()).floatValue()) : (MinecraftInstance.classProvider.isItemBow(item) ? (isForward ? ((Number)this.bowForwardMultiplier.get()).floatValue() : ((Number)this.bowStrafeMultiplier.get()).floatValue()) : 0.2f));
    }

    public NoSlow2() {
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
        }
        this.killAura = (KillAura)module;
    }
}

