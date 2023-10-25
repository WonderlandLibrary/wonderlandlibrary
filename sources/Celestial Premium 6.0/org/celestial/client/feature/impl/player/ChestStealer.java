/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCompass;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class ChestStealer
extends Feature {
    private final NumberSetting delay;
    private final BooleanSetting drop;
    private final BooleanSetting noMove;
    TimerHelper timer = new TimerHelper();
    TimerHelper timerClose = new TimerHelper();

    public ChestStealer() {
        super("ChestStealer", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0437\u0430\u0431\u0438\u0440\u0430\u0435\u0442 \u0432\u0435\u0449\u0438 \u0438\u0437 \u0441\u0443\u043d\u0434\u0443\u043a\u043e\u0432", Type.Player);
        this.delay = new NumberSetting("Stealer Speed", 10.0f, 0.0f, 100.0f, 1.0f, () -> true);
        this.drop = new BooleanSetting("Drop Items", false, () -> true);
        this.noMove = new BooleanSetting("No Move Swap", false, () -> true);
        this.addSettings(this.delay, this.drop, this.noMove);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix("" + (int)this.delay.getCurrentValue());
        float delay = this.delay.getCurrentValue() * 10.0f;
        if (ChestStealer.mc.currentScreen instanceof GuiChest) {
            if (this.noMove.getCurrentValue() && MovementHelper.isMoving() && !ChestStealer.mc.player.onGround) {
                return;
            }
            GuiChest chest = (GuiChest)ChestStealer.mc.currentScreen;
            for (int index = 0; index < chest.lowerChestInventory.getSizeInventory(); ++index) {
                ItemStack stack = chest.lowerChestInventory.getStackInSlot(index);
                ContainerChest container = (ContainerChest)ChestStealer.mc.player.openContainer;
                if (!this.isWhiteItem(stack) || !this.timerClose.hasReached(delay)) continue;
                if (!this.drop.getCurrentValue()) {
                    ChestStealer.mc.playerController.windowClick(container.windowId, index, 0, ClickType.QUICK_MOVE, ChestStealer.mc.player);
                } else {
                    ChestStealer.mc.playerController.windowClick(container.windowId, index, 1, ClickType.THROW, ChestStealer.mc.player);
                }
                this.timerClose.reset();
            }
            if (this.isEmpty(chest)) {
                ChestStealer.mc.player.closeScreen();
            } else {
                this.timer.reset();
            }
        }
        if (ChestStealer.mc.currentScreen == null) {
            this.timer.reset();
        }
    }

    public boolean isWhiteItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemEnderPearl || itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemFood || itemStack.getItem() instanceof ItemPotion || itemStack.getItem() instanceof ItemBlock || itemStack.getItem() instanceof ItemArrow || itemStack.getItem() instanceof ItemCompass;
    }

    private boolean isEmpty(GuiChest chest) {
        for (int index = 0; index < chest.lowerChestInventory.getSizeInventory(); ++index) {
            ItemStack stack = chest.lowerChestInventory.getStackInSlot(index);
            if (!this.isWhiteItem(stack)) continue;
            return false;
        }
        return true;
    }
}

