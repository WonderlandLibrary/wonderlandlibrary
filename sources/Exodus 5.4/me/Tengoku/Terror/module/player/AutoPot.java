/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.player;

import de.Hero.settings.Setting;
import java.util.List;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.TimerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class AutoPot
extends Module {
    public Setting delay;
    private static boolean activated;
    public Setting speed;
    public Setting autoHeal;
    public Setting other;
    private TimerUtils timer = new TimerUtils();
    public Setting healPercent;
    public Setting jumpBoost;

    private int getStackToThrow() {
        int n = 0;
        while (n < 9) {
            ItemStack itemStack = Minecraft.thePlayer.inventory.mainInventory[n];
            if (itemStack != null && this.isValid(itemStack)) {
                return n;
            }
            ++n;
        }
        return -1;
    }

    public static boolean isBadPotion(ItemStack itemStack) {
        if (itemStack != null && itemStack.getItem() instanceof ItemPotion) {
            ItemPotion itemPotion = (ItemPotion)itemStack.getItem();
            if (ItemPotion.isSplash(itemStack.getItemDamage())) {
                for (PotionEffect potionEffect : itemPotion.getEffects(itemStack)) {
                    PotionEffect potionEffect2 = potionEffect;
                    if (potionEffect2.getPotionID() != Potion.poison.getId() && potionEffect2.getPotionID() != Potion.harm.getId() && potionEffect2.getPotionID() != Potion.moveSlowdown.getId() && potionEffect2.getPotionID() != Potion.weakness.getId()) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isValid(ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemPotion)) {
            return false;
        }
        if (!ItemPotion.isSplash(itemStack.getMetadata())) {
            return false;
        }
        if (itemStack.getDisplayName().equalsIgnoreCase("Potion (20s)") && !this.jumpBoost.getValBoolean()) {
            return false;
        }
        if (AutoPot.isBadPotion(itemStack)) {
            return false;
        }
        ItemPotion itemPotion = (ItemPotion)itemStack.getItem();
        List<PotionEffect> list = itemPotion.getEffects(itemStack);
        boolean bl = false;
        for (PotionEffect potionEffect : list) {
            if (Minecraft.thePlayer.isPotionActive(potionEffect.getPotionID())) {
                bl = true;
                continue;
            }
            if (potionEffect.getPotionID() == Potion.regeneration.id || potionEffect.getPotionID() == Potion.heal.id) {
                if (!this.autoHeal.getValBoolean()) {
                    bl = true;
                    continue;
                }
                if (!((double)(Minecraft.thePlayer.getHealth() / Minecraft.thePlayer.getMaxHealth()) > this.healPercent.getValDouble() / 100.0)) continue;
                bl = true;
                continue;
            }
            if (potionEffect.getPotionID() == Potion.moveSpeed.id && !this.speed.getValBoolean()) {
                bl = true;
                continue;
            }
            if (this.other.getValBoolean()) continue;
            bl = true;
        }
        return !bl;
    }

    public static boolean isActivated() {
        return activated;
    }

    private ItemStack getStackToThrow2() {
        int n = 0;
        while (n < 9) {
            ItemStack itemStack = Minecraft.thePlayer.inventory.mainInventory[n];
            if (itemStack != null && this.isValid(itemStack)) {
                return itemStack;
            }
            ++n;
        }
        return null;
    }

    public AutoPot() {
        super("AutoPot", 0, Category.PLAYER, "Automatically throws splash potions.");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        activated = false;
    }

    @Override
    public void setup() {
        this.delay = new Setting("Delay", this, 600.0, 150.0, 1500.0, true);
        Exodus.INSTANCE.settingsManager.addSetting(this.delay);
        this.autoHeal = new Setting("AutoHeal", this, true);
        Exodus.INSTANCE.settingsManager.addSetting(this.autoHeal);
        this.healPercent = new Setting("HealPercent", this, 70.0, 5.0, 100.0, true);
        Exodus.INSTANCE.settingsManager.addSetting(this.healPercent);
        this.speed = new Setting("Speed", this, true);
        Exodus.INSTANCE.settingsManager.addSetting(this.speed);
        this.jumpBoost = new Setting("JumpBoost", this, false);
        Exodus.INSTANCE.settingsManager.addSetting(this.jumpBoost);
        this.other = new Setting("Other", this, true);
        Exodus.INSTANCE.settingsManager.addSetting(this.other);
    }

    @EventTarget
    public void update(EventMotion eventMotion) {
        if (Minecraft.thePlayer.isOnLadder()) {
            return;
        }
        EventMotion eventMotion2 = eventMotion;
        if (eventMotion2.isPre() && this.timer.hasReached(this.delay.getValDouble())) {
            int n = this.getStackToThrow();
            if (n == -1) {
                activated = false;
                this.timer.reset();
                return;
            }
            activated = true;
            if (eventMotion2.getLastPitch() == 85.0f) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n));
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.getStackToThrow2()));
                this.timer.reset();
                activated = false;
            } else {
                eventMotion2.setPitch(85.0f);
            }
        }
    }
}

