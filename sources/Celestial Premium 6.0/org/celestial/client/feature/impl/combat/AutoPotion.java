/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.helpers.player.InventoryHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class AutoPotion
extends Feature {
    private final TimerHelper timerUtils = new TimerHelper();
    private final NumberSetting delay;
    private final BooleanSetting groundOnly;
    private final BooleanSetting noGui;
    private final BooleanSetting usingItemCheck;
    private final BooleanSetting strength;
    private final BooleanSetting speed;
    private final BooleanSetting fireResistance;
    private final BooleanSetting heal;
    private final BooleanSetting heldOnly;
    private final NumberSetting healHealth;
    private final BooleanSetting autoMoveToHotbar;
    private final ListSetting potMode = new ListSetting("AutoPot Mode", "Robot", () -> true, "Robot", "AutoPitch");

    public AutoPotion() {
        super("AutoPotion", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0431\u0440\u043e\u0441\u0430\u0435\u0442 Splash \u0437\u0435\u043b\u044c\u044f \u043d\u0430\u0445\u043e\u0434\u044f\u0449\u0438\u0435\u0441\u044f \u0432 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u0435", Type.Combat);
        this.delay = new NumberSetting("Throw Delay", 500.0f, 10.0f, 1000.0f, 10.0f, () -> this.potMode.currentMode.equals("Robot"));
        this.groundOnly = new BooleanSetting("Ground Only", true, () -> this.potMode.currentMode.equals("Robot"));
        this.noGui = new BooleanSetting("No Gui", true, () -> true);
        this.usingItemCheck = new BooleanSetting("Using Item Check", true, () -> true);
        this.autoMoveToHotbar = new BooleanSetting("AutoMoveToHotbar", true, () -> this.potMode.currentMode.equals("Robot"));
        this.strength = new BooleanSetting("Strength", true, () -> this.potMode.currentMode.equals("Robot"));
        this.speed = new BooleanSetting("Speed", true, () -> this.potMode.currentMode.equals("Robot"));
        this.fireResistance = new BooleanSetting("Fire Resistance", true, () -> this.potMode.currentMode.equals("Robot"));
        this.heal = new BooleanSetting("Heal", false, () -> this.potMode.currentMode.equals("Robot"));
        this.healHealth = new NumberSetting("Heal Health", 12.0f, 1.0f, 20.0f, 0.5f, () -> this.potMode.currentMode.equals("Robot") && this.heal.getCurrentValue());
        this.heldOnly = new BooleanSetting("Only Held", false, () -> this.potMode.currentMode.equals("Robot"));
        this.addSettings(this.potMode, this.delay, this.heldOnly, this.autoMoveToHotbar, this.groundOnly, this.noGui, this.usingItemCheck, this.strength, this.speed, this.fireResistance, this.heal, this.healHealth);
    }

    private int findPotionSlot(int start, int end) {
        for (int i = start; i < end; ++i) {
            ItemStack stack = AutoPotion.mc.player.inventoryContainer.getSlot(i).getStack();
            if (stack.getItem() != Items.SPLASH_POTION) continue;
            for (PotionEffect potionEffect : PotionUtils.getEffectsFromStack(stack)) {
                if (potionEffect.getPotion() != Potion.getPotionById(1) || AutoPotion.mc.player.isPotionActive(MobEffects.SPEED) || !this.speed.getCurrentValue()) continue;
                return i;
            }
            for (PotionEffect potionEffect : PotionUtils.getEffectsFromStack(stack)) {
                if (potionEffect.getPotion() != Potion.getPotionById(5) || AutoPotion.mc.player.isPotionActive(MobEffects.STRENGTH) || !this.strength.getCurrentValue()) continue;
                return i;
            }
            for (PotionEffect potionEffect : PotionUtils.getEffectsFromStack(stack)) {
                if (potionEffect.getPotion() != Potion.getPotionById(12) || AutoPotion.mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE) || !this.fireResistance.getCurrentValue()) continue;
                return i;
            }
            for (PotionEffect potionEffect : PotionUtils.getEffectsFromStack(stack)) {
                if (potionEffect.getPotion() != Potion.getPotionById(6) || !(AutoPotion.mc.player.getHealth() <= this.healHealth.getCurrentValue()) || !this.heal.getCurrentValue()) continue;
                return i;
            }
        }
        return -1;
    }

    private void usePotionSilent(int slot) {
        float pitch = 90.0f;
        AutoPotion.mc.player.rotationPitchHead = pitch = MathHelper.clamp(pitch, 0.0f, 90.0f);
        AutoPotion.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(AutoPotion.mc.player.rotationYaw, pitch, AutoPotion.mc.player.onGround));
        if (pitch == 90.0f) {
            AutoPotion.mc.player.connection.sendPacket(new CPacketHeldItemChange(slot - 36));
            AutoPotion.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            AutoPotion.mc.player.connection.sendPacket(new CPacketHeldItemChange(AutoPotion.mc.player.inventory.currentItem));
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String mode = this.potMode.getCurrentMode();
        this.setSuffix(mode);
        if (AutoPotion.mc.player == null || AutoPotion.mc.world == null || AutoPotion.mc.player.ticksExisted < 20) {
            return;
        }
        if (this.noGui.getCurrentValue() && AutoPotion.mc.currentScreen != null) {
            return;
        }
        if (mode.equalsIgnoreCase("AutoPitch")) {
            if (AutoPotion.mc.player.getHeldItemMainhand().getItem() == Items.SPLASH_POTION) {
                event.setPitch(90.0f);
                AutoPotion.mc.player.rotationPitchHead = 90.0f;
            }
        } else if (mode.equalsIgnoreCase("Robot")) {
            if (AutoPotion.mc.player.getHeldItemMainhand().getItem() != Items.SPLASH_POTION && this.heldOnly.getCurrentValue()) {
                return;
            }
            if (this.groundOnly.getCurrentValue() && !AutoPotion.mc.player.onGround) {
                return;
            }
            if (this.usingItemCheck.getCurrentValue() && AutoPotion.mc.player.isUsingItem()) {
                return;
            }
            if (!this.timerUtils.hasReached(this.delay.getCurrentValue()) || AutoPotion.mc.playerController.isInCreativeMode()) {
                return;
            }
            int potionSlot = this.findPotionSlot(9, 36);
            int potionSlot1 = this.findPotionSlot(36, 45);
            if (InventoryHelper.doesHotbarHavePot()) {
                if (potionSlot1 != -1) {
                    this.usePotionSilent(potionSlot1);
                    this.timerUtils.reset();
                    return;
                }
                if (potionSlot != -1 && InventoryHelper.hotbarHasAir()) {
                    AutoPotion.mc.playerController.windowClick(0, potionSlot, 0, ClickType.QUICK_MOVE, AutoPotion.mc.player);
                    this.timerUtils.reset();
                }
            } else if (this.autoMoveToHotbar.getCurrentValue() && potionSlot != -1 && InventoryHelper.inventoryHasPotion()) {
                AutoPotion.mc.playerController.windowClick(0, potionSlot, 1, ClickType.QUICK_MOVE, AutoPotion.mc.player);
            }
        }
    }
}

