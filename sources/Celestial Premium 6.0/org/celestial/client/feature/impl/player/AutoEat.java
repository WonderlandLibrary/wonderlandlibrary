/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.NumberSetting;

public class AutoEat
extends Feature {
    public static NumberSetting feed;
    private boolean isActive;
    private int oldSlot;
    public static boolean isEating;

    public AutoEat() {
        super("AutoEat", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0435\u0441\u0442 \u0435\u0434\u0443 \u0438\u0437 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u044f", Type.Player);
        feed = new NumberSetting("Feed Level", 15.0f, 1.0f, 20.0f, 1.0f, () -> true);
        this.addSettings(feed);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        this.setSuffix("" + (int)feed.getCurrentValue());
        if (AutoEat.mc.player == null || AutoEat.mc.world == null) {
            return;
        }
        if (this.isFood()) {
            if (this.isFood() && (float)AutoEat.mc.player.getFoodStats().getFoodLevel() <= feed.getCurrentValue()) {
                this.isActive = true;
                KeyBinding.setKeyBindState(AutoEat.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
            } else if (this.isActive) {
                KeyBinding.setKeyBindState(AutoEat.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                this.isActive = false;
            }
        } else {
            if (isEating && !AutoEat.mc.player.isHandActive()) {
                if (this.oldSlot != -1) {
                    AutoEat.mc.player.inventory.currentItem = this.oldSlot;
                    this.oldSlot = -1;
                }
                isEating = false;
                KeyBinding.setKeyBindState(AutoEat.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                return;
            }
            if (isEating) {
                return;
            }
            if (this.isValid(AutoEat.mc.player.getHeldItemOffhand(), AutoEat.mc.player.getFoodStats().getFoodLevel())) {
                AutoEat.mc.player.setActiveHand(EnumHand.OFF_HAND);
                isEating = true;
                KeyBinding.setKeyBindState(AutoEat.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                mc.rightClickMouse();
            } else {
                for (int i = 0; i < 9; ++i) {
                    if (!this.isValid(AutoEat.mc.player.inventory.getStackInSlot(i), AutoEat.mc.player.getFoodStats().getFoodLevel())) continue;
                    this.oldSlot = AutoEat.mc.player.inventory.currentItem;
                    AutoEat.mc.player.inventory.currentItem = i;
                    isEating = true;
                    KeyBinding.setKeyBindState(AutoEat.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                    mc.rightClickMouse();
                    return;
                }
            }
        }
    }

    private boolean itemCheck(Item item) {
        return item != Items.ROTTEN_FLESH && item != Items.SPIDER_EYE && item != Items.POISONOUS_POTATO && (item != Items.FISH || new ItemStack(Items.FISH).getItemDamage() != 3);
    }

    private boolean isValid(ItemStack stack, int food) {
        return stack.getItem() instanceof ItemFood && feed.getCurrentValue() - (float)food >= (float)((ItemFood)stack.getItem()).getHealAmount(stack) && this.itemCheck(stack.getItem());
    }

    private boolean isFood() {
        return AutoEat.mc.player.getHeldItemOffhand().getItem() instanceof ItemFood;
    }
}

