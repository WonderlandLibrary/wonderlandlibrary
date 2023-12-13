/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.combat;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;

public class AutoGapple
extends Module {
    private boolean eating = false;
    Setting health = new Setting("Health", this, 15.0, 1.0, 20.0, false);

    public AutoGapple() {
        super("AutoGapple", Category.Combat);
        Main.instance.setmgr.rSetting(this.health);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if ((double)(Minecraft.player.getHealth() + Minecraft.player.getAbsorptionAmount()) > this.health.getValDouble() && this.eating) {
            this.eating = false;
            this.stop();
            return;
        }
        if (!this.canEat()) {
            return;
        }
        if (this.isFood(Minecraft.player.getHeldItemOffhand())) {
            if ((double)Minecraft.player.getHealth() <= this.health.getValDouble() && this.canEat()) {
                KeyBinding.setKeyBindState(AutoGapple.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                this.eating = true;
            }
        }
        if (!this.canEat()) {
            this.stop();
        }
    }

    public static boolean isNullOrEmptyStack(ItemStack itemStack) {
        return itemStack == null || itemStack.func_190926_b();
    }

    boolean isFood(ItemStack itemStack) {
        return !AutoGapple.isNullOrEmptyStack(itemStack) && itemStack.getItem() instanceof ItemAppleGold;
    }

    public boolean canEat() {
        return AutoGapple.mc.objectMouseOver == null || !(AutoGapple.mc.objectMouseOver.entityHit instanceof EntityVillager);
    }

    void stop() {
        KeyBinding.setKeyBindState(AutoGapple.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
    }
}

