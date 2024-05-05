/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.input.Mouse
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.settings.IKeyBinding;
import net.ccbluex.liquidbounce.api.minecraft.entity.player.IInventoryPlayer;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;

@ModuleInfo(name="AutoHead", description="faq", category=ModuleCategory.COMBAT)
public final class AutoHead
extends Module {
    private boolean eatingApple;
    private int switched = -1;
    private boolean doingStuff;
    private final TimeUtils timer = new TimeUtils();
    private final BoolValue eatHeads = new BoolValue("EatHead", true);
    private final BoolValue eatApples = new BoolValue("EatApples", true);
    private final FloatValue health = new FloatValue("Health", 10.0f, 1.0f, 20.0f);
    private final IntegerValue delay = new IntegerValue("Delay", 750, 100, 2000);

    public final boolean getDoingStuff() {
        return this.doingStuff;
    }

    public final void setDoingStuff(boolean bl) {
        this.doingStuff = bl;
    }

    @Override
    public void onEnable() {
        this.eatingApple = this.doingStuff = false;
        this.switched = -1;
        this.timer.reset();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.doingStuff = false;
        if (this.eatingApple) {
            this.repairItemPress();
            this.repairItemSwitch();
        }
        super.onDisable();
    }

    private final void repairItemPress() {
        IKeyBinding keyBindUseItem;
        if (MinecraftInstance.mc.getGameSettings() != null && (keyBindUseItem = MinecraftInstance.mc.getGameSettings().getKeyBindUseItem()) != null) {
            keyBindUseItem.setPressed(false);
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onUpdate(@Nullable MotionEvent event) {
        block20: {
            block22: {
                block21: {
                    if (MinecraftInstance.mc.getThePlayer() == null) {
                        return;
                    }
                    v0 = MinecraftInstance.mc.getThePlayer();
                    if (v0 == null) {
                        Intrinsics.throwNpe();
                    }
                    v1 = v0.getInventory();
                    if (v1 == null) {
                        return;
                    }
                    inventory = v1;
                    this.doingStuff = false;
                    if (Mouse.isButtonDown((int)0) || Mouse.isButtonDown((int)1)) break block20;
                    useItem = MinecraftInstance.mc.getGameSettings().getKeyBindUseItem();
                    if (!this.timer.hasReached(((Number)this.delay.get()).intValue())) {
                        this.eatingApple = false;
                        this.repairItemPress();
                        this.repairItemSwitch();
                        return;
                    }
                    v2 = MinecraftInstance.mc.getThePlayer();
                    if (v2 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (v2.getCapabilities().isCreativeMode()) break block21;
                    v3 = MinecraftInstance.mc.getThePlayer();
                    if (v3 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (v3.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.REGENERATION))) break block21;
                    v4 = MinecraftInstance.mc.getThePlayer();
                    if (v4 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!(v4.getHealth() >= ((Number)this.health.get()).floatValue())) break block22;
                }
                this.timer.reset();
                if (this.eatingApple) {
                    this.eatingApple = false;
                    this.repairItemPress();
                    this.repairItemSwitch();
                }
                return;
            }
            var4_4 = false;
            var5_5 = true;
            while (var4_4 <= var5_5) {
                block24: {
                    block23: {
                        v5 = doEatHeads = i != false;
                        if (!doEatHeads) break block23;
                        if (((Boolean)this.eatHeads.get()).booleanValue()) ** GOTO lbl-1000
                        break block24;
                    }
                    if (!((Boolean)this.eatApples.get()).booleanValue()) {
                        this.eatingApple = false;
                        this.repairItemPress();
                        this.repairItemSwitch();
                    } else lbl-1000:
                    // 2 sources

                    {
                        slot = 0;
                        v6 = slot = doEatHeads != false ? this.getItemFromHotbar(397) : this.getItemFromHotbar(322);
                        if (slot != -1) {
                            tempSlot = inventory.getCurrentItem();
                            this.doingStuff = true;
                            if (doEatHeads) {
                                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(slot));
                                v7 = MinecraftInstance.mc.getNetHandler();
                                v8 = MinecraftInstance.mc.getThePlayer();
                                if (v8 == null) {
                                    Intrinsics.throwNpe();
                                }
                                v7.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerBlockPlacement(v8.getInventory().getCurrentItemInHand()));
                                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(tempSlot));
                                this.timer.reset();
                            } else {
                                inventory.setCurrentItem(slot);
                                useItem.setPressed(true);
                                if (!this.eatingApple) {
                                    this.eatingApple = true;
                                    this.switched = tempSlot;
                                }
                            }
                        }
                    }
                }
                ++i;
            }
        }
    }

    private final void repairItemSwitch() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP p = iEntityPlayerSP;
        IInventoryPlayer iInventoryPlayer = p.getInventory();
        if (iInventoryPlayer == null) {
            return;
        }
        IInventoryPlayer inventory = iInventoryPlayer;
        int switched = this.switched;
        if (switched == -1) {
            return;
        }
        inventory.setCurrentItem(switched);
        this.switched = switched = -1;
    }

    /*
     * WARNING - void declaration
     */
    private final int getItemFromHotbar(int id) {
        int n = 0;
        int n2 = 8;
        while (n <= n2) {
            void i;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.getInventory().getMainInventory().get((int)i) != null) {
                IItem item;
                IItemStack a;
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                IItemStack iItemStack = a = iEntityPlayerSP2.getInventory().getMainInventory().get((int)i);
                if (iItemStack == null) {
                    Intrinsics.throwNpe();
                }
                IItem iItem = item = iItemStack.getItem();
                if (iItem == null) {
                    Intrinsics.throwNpe();
                }
                if (MinecraftInstance.functions.getIdFromItem(iItem) == id) {
                    return (int)i;
                }
            }
            ++i;
        }
        return -1;
    }
}

