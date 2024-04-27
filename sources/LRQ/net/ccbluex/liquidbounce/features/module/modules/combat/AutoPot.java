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
    private final FloatValue healthValue = new FloatValue("Health", 15.0f, 1.0f, 20.0f);
    private final IntegerValue delayValue = new IntegerValue("Delay", 500, 500, 1000);
    private final BoolValue openInventoryValue = new BoolValue("OpenInv", false);
    private final BoolValue simulateInventory = new BoolValue("SimulateInventory", true);
    private final FloatValue groundDistanceValue = new FloatValue("GroundDistance", 2.0f, 0.0f, 5.0f);
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Normal", "Jump", "Port"}, "Normal");
    private final MSTimer msTimer = new MSTimer();
    private int potion = -1;

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onMotion(MotionEvent motionEvent) {
        if (!this.msTimer.hasTimePassed(((Number)this.delayValue.get()).intValue()) || MinecraftInstance.mc.getPlayerController().isInCreativeMode()) {
            return;
        }
        v0 = MinecraftInstance.mc.getThePlayer();
        if (v0 == null) {
            return;
        }
        thePlayer = v0;
        switch (AutoPot$WhenMappings.$EnumSwitchMapping$0[motionEvent.getEventState().ordinal()]) {
            case 1: {
                potionInHotbar = this.findPotion(36, 45);
                if (thePlayer.getHealth() <= ((Number)this.healthValue.get()).floatValue() && potionInHotbar != -1) {
                    if (thePlayer.getOnGround()) {
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
                                thePlayer.jump();
                                ** break;
                            }
                        }
                        thePlayer.moveEntity(0.0, 0.42, 0.0);
                    }
lbl27:
                    // 7 sources

                    fallingPlayer = new FallingPlayer(thePlayer.getPosX(), thePlayer.getPosY(), thePlayer.getPosZ(), thePlayer.getMotionX(), thePlayer.getMotionY(), thePlayer.getMotionZ(), thePlayer.getRotationYaw(), thePlayer.getMoveStrafing(), thePlayer.getMoveForward());
                    v2 = fallingPlayer.findCollision(20);
                    collisionBlock = v2 != null ? v2.getPos() : null;
                    v3 = thePlayer.getPosY();
                    v4 = collisionBlock;
                    v5 = v4 != null ? v4.getY() : 0;
                    if (v3 - (double)v5 >= ((Number)this.groundDistanceValue.get()).doubleValue()) {
                        return;
                    }
                    this.potion = potionInHotbar;
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(this.potion - 36));
                    if (thePlayer.getRotationPitch() <= 80.0f) {
                        RotationUtils.setTargetRotation(new Rotation(thePlayer.getRotationYaw(), RandomUtils.INSTANCE.nextFloat(80.0f, 90.0f)));
                    }
                    return;
                }
                potionInInventory = this.findPotion(9, 36);
                if (potionInInventory == -1 || !InventoryUtils.hasSpaceHotbar()) break;
                if (((Boolean)this.openInventoryValue.get()).booleanValue() && !MinecraftInstance.classProvider.isGuiInventory(MinecraftInstance.mc.getCurrentScreen())) {
                    return;
                }
                v6 = openInventory = MinecraftInstance.classProvider.isGuiInventory(MinecraftInstance.mc.getCurrentScreen()) == false && (Boolean)this.simulateInventory.get() != false;
                if (openInventory) {
                    var7_12 = MinecraftInstance.mc.getNetHandler();
                    $i$f$createOpenInventoryPacket = false;
                    v7 = WrapperImpl.INSTANCE.getClassProvider();
                    v8 = LiquidBounce.INSTANCE.getWrapper().getMinecraft().getThePlayer();
                    if (v8 == null) {
                        Intrinsics.throwNpe();
                    }
                    var8_15 = v7.createCPacketEntityAction(v8, ICPacketEntityAction.WAction.OPEN_INVENTORY);
                    var7_12.addToSendQueue(var8_15);
                }
                MinecraftInstance.mc.getPlayerController().windowClick(0, potionInInventory, 0, 1, thePlayer);
                if (openInventory) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketCloseWindow());
                }
                this.msTimer.reset();
                break;
            }
            case 2: {
                if (this.potion < 0 || !(RotationUtils.serverRotation.getPitch() >= 75.0f)) break;
                itemStack = thePlayer.getInventory().getStackInSlot(this.potion);
                if (itemStack != null) {
                    potionInInventory = WEnumHand.MAIN_HAND;
                    var7_13 = MinecraftInstance.mc.getNetHandler();
                    $i$f$createUseItemPacket = false;
                    var8_16 = WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem((WEnumHand)hand$iv);
                    var7_13.addToSendQueue(var8_16);
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(thePlayer.getInventory().getCurrentItem()));
                    this.msTimer.reset();
                }
                this.potion = -1;
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    private final int findPotion(int startSlot, int endSlot) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        int n = startSlot;
        int n2 = endSlot;
        while (n < n2) {
            void i;
            IItemStack stack = thePlayer.getInventoryContainer().getSlot((int)i).getStack();
            if (stack != null && MinecraftInstance.classProvider.isItemPotion(stack.getItem()) && stack.isSplash()) {
                IItem iItem = stack.getItem();
                if (iItem == null) {
                    Intrinsics.throwNpe();
                }
                IItemPotion itemPotion = iItem.asItemPotion();
                for (IPotionEffect potionEffect : itemPotion.getEffects(stack)) {
                    if (potionEffect.getPotionID() != MinecraftInstance.classProvider.getPotionEnum(PotionType.HEAL).getId()) continue;
                    return (int)i;
                }
                if (!thePlayer.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.REGENERATION))) {
                    for (IPotionEffect potionEffect : itemPotion.getEffects(stack)) {
                        if (potionEffect.getPotionID() != MinecraftInstance.classProvider.getPotionEnum(PotionType.REGENERATION).getId()) continue;
                        return (int)i;
                    }
                }
            }
            ++i;
        }
        return -1;
    }

    @Override
    public String getTag() {
        return String.valueOf(((Number)this.healthValue.get()).floatValue());
    }
}

