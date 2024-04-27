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
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
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
    private final IntegerValue delay;
    private final TimeUtils timer = new TimeUtils();
    private int switched = -1;
    private final FloatValue health;
    private boolean eatingApple;
    private boolean doingStuff;
    private final BoolValue eatApples;
    private final BoolValue eatHeads = new BoolValue("EatHead", true);

    private final void repairItemPress() {
        IKeyBinding iKeyBinding;
        if (MinecraftInstance.mc.getGameSettings() != null && (iKeyBinding = MinecraftInstance.mc.getGameSettings().getKeyBindUseItem()) != null) {
            iKeyBinding.setPressed(false);
        }
    }

    public AutoHead() {
        this.eatApples = new BoolValue("EatApples", true);
        this.health = new FloatValue("Health", 10.0f, 1.0f, 20.0f);
        this.delay = new IntegerValue("Delay", 750, 100, 2000);
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

    private final void repairItemSwitch() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        IInventoryPlayer iInventoryPlayer = iEntityPlayerSP2.getInventory();
        if (iInventoryPlayer == null) {
            return;
        }
        IInventoryPlayer iInventoryPlayer2 = iInventoryPlayer;
        int n = this.switched;
        if (n == -1) {
            return;
        }
        iInventoryPlayer2.setCurrentItem(n);
        this.switched = n = -1;
    }

    public final boolean getDoingStuff() {
        return this.doingStuff;
    }

    private final int getItemFromHotbar(int n) {
        int n2 = 8;
        for (int i = 0; i <= n2; ++i) {
            IItemStack iItemStack;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (iEntityPlayerSP.getInventory().getMainInventory().get(i) == null) continue;
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            IItemStack iItemStack2 = iItemStack = (IItemStack)iEntityPlayerSP2.getInventory().getMainInventory().get(i);
            if (iItemStack2 == null) {
                Intrinsics.throwNpe();
            }
            IItem iItem = iItemStack2.getItem();
            IExtractedFunctions iExtractedFunctions = AutoHead.access$getFunctions$p$s1046033730();
            IItem iItem2 = iItem;
            if (iItem2 == null) {
                Intrinsics.throwNpe();
            }
            if (iExtractedFunctions.getIdFromItem(iItem2) != n) continue;
            return i;
        }
        return -1;
    }

    @Override
    public void onEnable() {
        this.eatingApple = this.doingStuff = false;
        this.switched = -1;
        this.timer.reset();
        super.onEnable();
    }

    public static final IExtractedFunctions access$getFunctions$p$s1046033730() {
        return MinecraftInstance.functions;
    }

    public final void setDoingStuff(boolean bl) {
        this.doingStuff = bl;
    }

    @EventTarget
    public final void onUpdate(@Nullable MotionEvent motionEvent) {
        block19: {
            IKeyBinding iKeyBinding;
            IInventoryPlayer iInventoryPlayer;
            block21: {
                block20: {
                    if (MinecraftInstance.mc.getThePlayer() == null) {
                        return;
                    }
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    IInventoryPlayer iInventoryPlayer2 = iEntityPlayerSP.getInventory();
                    if (iInventoryPlayer2 == null) {
                        return;
                    }
                    iInventoryPlayer = iInventoryPlayer2;
                    this.doingStuff = false;
                    if (Mouse.isButtonDown((int)0) || Mouse.isButtonDown((int)1)) break block19;
                    iKeyBinding = MinecraftInstance.mc.getGameSettings().getKeyBindUseItem();
                    if (!this.timer.hasReached(((Number)this.delay.get()).intValue())) {
                        this.eatingApple = false;
                        this.repairItemPress();
                        this.repairItemSwitch();
                        return;
                    }
                    IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP2 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP2.getCapabilities().isCreativeMode()) break block20;
                    IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP3 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP3.isPotionActive(MinecraftInstance.classProvider.getPotionEnum(PotionType.REGENERATION))) break block20;
                    IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP4 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!(iEntityPlayerSP4.getHealth() >= ((Number)this.health.get()).floatValue())) break block21;
                }
                this.timer.reset();
                if (this.eatingApple) {
                    this.eatingApple = false;
                    this.repairItemPress();
                    this.repairItemSwitch();
                }
                return;
            }
            int n = 1;
            for (int i = 0; i <= n; ++i) {
                boolean bl;
                boolean bl2 = bl = i != 0;
                if (bl) {
                    if (!((Boolean)this.eatHeads.get()).booleanValue()) {
                        continue;
                    }
                } else if (!((Boolean)this.eatApples.get()).booleanValue()) {
                    this.eatingApple = false;
                    this.repairItemPress();
                    this.repairItemSwitch();
                    continue;
                }
                int n2 = 0;
                int n3 = n2 = bl ? this.getItemFromHotbar(397) : this.getItemFromHotbar(322);
                if (n2 == -1) continue;
                int n4 = iInventoryPlayer.getCurrentItem();
                this.doingStuff = true;
                if (bl) {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(n2));
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerBlockPlacement(iEntityPlayerSP.getInventory().getCurrentItemInHand()));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(n4));
                    this.timer.reset();
                    continue;
                }
                iInventoryPlayer.setCurrentItem(n2);
                iKeyBinding.setPressed(true);
                if (this.eatingApple) continue;
                this.eatingApple = true;
                this.switched = n4;
            }
        }
    }
}

