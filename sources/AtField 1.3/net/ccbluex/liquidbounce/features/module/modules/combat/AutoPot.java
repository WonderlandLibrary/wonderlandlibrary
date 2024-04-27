/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.WEnumHand;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemPotion;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoPot$WhenMappings;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.misc.FallingPlayer;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="AutoPot", description="Automatically throws healing potions.", category=ModuleCategory.COMBAT)
public final class AutoPot
extends Module {
    private int potion = -1;
    private final BoolValue simulateInventory;
    private final FloatValue healthValue = new FloatValue("Health", 15.0f, 1.0f, 20.0f);
    private final MSTimer msTimer;
    private final ListValue modeValue;
    private final IntegerValue delayValue = new IntegerValue("Delay", 500, 500, 1000);
    private final BoolValue openInventoryValue = new BoolValue("OpenInv", false);
    private final FloatValue groundDistanceValue;

    public AutoPot() {
        this.simulateInventory = new BoolValue("SimulateInventory", true);
        this.groundDistanceValue = new FloatValue("GroundDistance", 2.0f, 0.0f, 5.0f);
        this.modeValue = new ListValue("Mode", new String[]{"Normal", "Jump", "Port"}, "Normal");
        this.msTimer = new MSTimer();
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onMotion(MotionEvent var1_1) {
        if (!this.msTimer.hasTimePassed(((Number)this.delayValue.get()).intValue()) || MinecraftInstance.mc.getPlayerController().isInCreativeMode()) {
            return;
        }
        v0 = MinecraftInstance.mc.getThePlayer();
        if (v0 == null) {
            return;
        }
        var2_2 = v0;
        switch (AutoPot$WhenMappings.$EnumSwitchMapping$0[var1_1.getEventState().ordinal()]) {
            case 1: {
                var3_3 = this.findPotion(36, 45);
                if (var2_2.getHealth() <= ((Number)this.healthValue.get()).floatValue() && var3_3 != -1) {
                    if (var2_2.getOnGround()) {
                        var4_5 = (String)this.modeValue.get();
                        var5_8 = false;
                        v1 = var4_5;
                        if (v1 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        var4_5 = v1.toLowerCase();
                        switch (var4_5.hashCode()) {
                            case 3446913: {
                                if (!var4_5.equals("port")) ** break;
                                break;
                            }
                            case 3273774: {
                                if (!var4_5.equals("jump")) ** break;
                                var2_2.jump();
                                ** break;
                            }
                        }
                        var2_2.moveEntity(0.0, 0.42, 0.0);
                        ** break;
                    }
lbl28:
                    // 7 sources

                    var4_5 = new FallingPlayer(var2_2.getPosX(), var2_2.getPosY(), var2_2.getPosZ(), var2_2.getMotionX(), var2_2.getMotionY(), var2_2.getMotionZ(), var2_2.getRotationYaw(), var2_2.getMoveStrafing(), var2_2.getMoveForward());
                    v2 = var4_5.findCollision(20);
                    var5_9 = v2 != null ? v2.getPos() : null;
                    v3 = var2_2.getPosY();
                    v4 = var5_9;
                    v5 = v4 != null ? v4.getY() : 0;
                    if (v3 - (double)v5 >= ((Number)this.groundDistanceValue.get()).doubleValue()) {
                        return;
                    }
                    this.potion = var3_3;
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(this.potion - 36));
                    if (var2_2.getRotationPitch() <= 80.0f) {
                        RotationUtils.setTargetRotation(new Rotation(var2_2.getRotationYaw(), RandomUtils.INSTANCE.nextFloat(80.0f, 90.0f)));
                    }
                    return;
                }
                var4_6 = this.findPotion(9, 36);
                if (var4_6 == -1 || !InventoryUtils.hasSpaceHotbar()) break;
                if (((Boolean)this.openInventoryValue.get()).booleanValue() && !MinecraftInstance.classProvider.isGuiInventory(MinecraftInstance.mc.getCurrentScreen())) {
                    return;
                }
                v6 = var5_10 = MinecraftInstance.classProvider.isGuiInventory(MinecraftInstance.mc.getCurrentScreen()) == false && (Boolean)this.simulateInventory.get() != false;
                if (var5_10) {
                    var7_12 = MinecraftInstance.mc.getNetHandler();
                    var6_14 = false;
                    v7 = WrapperImpl.INSTANCE.getClassProvider();
                    v8 = LiquidBounce.INSTANCE.getWrapper().getMinecraft().getThePlayer();
                    if (v8 == null) {
                        Intrinsics.throwNpe();
                    }
                    var8_15 = v7.createCPacketEntityAction(v8, ICPacketEntityAction.WAction.OPEN_INVENTORY);
                    var7_12.addToSendQueue(var8_15);
                }
                MinecraftInstance.mc.getPlayerController().windowClick(0, var4_6, 0, 1, var2_2);
                if (var5_10) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketCloseWindow());
                }
                this.msTimer.reset();
                break;
            }
            case 2: {
                if (this.potion < 0 || !(RotationUtils.serverRotation.getPitch() >= 75.0f)) break;
                var3_4 = var2_2.getInventory().getStackInSlot(this.potion);
                if (var3_4 != null) {
                    var4_7 = WEnumHand.MAIN_HAND;
                    var7_13 = MinecraftInstance.mc.getNetHandler();
                    var5_11 = false;
                    var8_16 = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(var4_7);
                    var7_13.addToSendQueue(var8_16);
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(var2_2.getInventory().getCurrentItem()));
                    this.msTimer.reset();
                }
                this.potion = -1;
                break;
            }
        }
    }

    @Override
    public String getTag() {
        return String.valueOf(((Number)this.healthValue.get()).floatValue());
    }

    private final int findPotion(int n, int n2) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        int n3 = n2;
        for (int i = n; i < n3; ++i) {
            IItemStack iItemStack = iEntityPlayerSP2.getInventoryContainer().getSlot(i).getStack();
            if (iItemStack == null || !MinecraftInstance.classProvider.isItemPotion(iItemStack.getItem()) || !iItemStack.isSplash()) continue;
            IItem iItem = iItemStack.getItem();
            if (iItem == null) {
                Intrinsics.throwNpe();
            }
            IItemPotion iItemPotion = iItem.asItemPotion();
            for (IPotionEffect iPotionEffect : iItemPotion.getEffects(iItemStack)) {
                if (iPotionEffect.getPotionID() != MinecraftInstance.classProvider.getPotionEnum(PotionType.HEAL).getId()) continue;
                return i;
            }
            if (iEntityPlayerSP2.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.REGENERATION))) continue;
            for (IPotionEffect iPotionEffect : iItemPotion.getEffects(iItemStack)) {
                if (iPotionEffect.getPotionID() != MinecraftInstance.classProvider.getPotionEnum(PotionType.REGENERATION).getId()) continue;
                return i;
            }
        }
        return -1;
    }
}

