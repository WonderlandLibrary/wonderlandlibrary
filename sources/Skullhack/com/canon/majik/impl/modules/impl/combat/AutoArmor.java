package com.canon.majik.impl.modules.impl.combat;

import com.canon.majik.api.utils.player.PlayerUtils;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.settings.NumberSetting;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class AutoArmor extends Module {

    private Thread thread;
    public NumberSetting delay = setting("Delay", 200, 0, 500);

    public AutoArmor(String name, Category category) {
        super(name, category);
    }

    @Override
    public void onEnable() {
        (thread = new Thread() {
            @Override
            public void run() {
                while (thread != null && thread.equals(this)) {
                    loop();
                    try {
                        sleep(150);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    @Override
    public void onDisable() {
        thread = null;
    }

    public void loop() {
        if (mc.player == null || isContainerOpen()) {
            return;
        }
        if (PlayerUtils.getItemStack(39).getItem() == Items.AIR) {
            this.switchSlot(getBestSlot(new Item[] { Items.DIAMOND_HELMET, Items.IRON_HELMET, Items.CHAINMAIL_HELMET, Items.GOLDEN_HELMET, Items.LEATHER_HELMET }), 39);
        }
        if (PlayerUtils.getItemStack(38).getItem() == Items.AIR) {
            this.switchSlot(getBestSlot(new Item[] { Items.DIAMOND_CHESTPLATE, Items.IRON_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.LEATHER_CHESTPLATE }), 38);
        }
        if (PlayerUtils.getItemStack(37).getItem() == Items.AIR) {
            this.switchSlot(getBestSlot(new Item[] { Items.DIAMOND_LEGGINGS, Items.IRON_LEGGINGS, Items.CHAINMAIL_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.LEATHER_LEGGINGS }), 37);
        }
        if (PlayerUtils.getItemStack(36).getItem() == Items.AIR) {
            this.switchSlot(getBestSlot(new Item[] { Items.DIAMOND_BOOTS, Items.IRON_BOOTS, Items.CHAINMAIL_BOOTS, Items.GOLDEN_BOOTS, Items.LEATHER_BOOTS }), 36);
        }
    }

    public void switchSlot(final int slot, final int slot2) {
        if (slot == -1) {
            return;
        }
        PlayerUtils.clickSlot(slot);
        PlayerUtils.clickSlot(slot2);
        sleep(delay.getValue().intValue());
    }

    public static int getBestSlot(final Item[] items) {
        for (final Item item : items) {
            for (final PlayerUtils.ItemStackUtil itemStack : PlayerUtils.getAllItems()) {
                if (itemStack.itemStack.getItem() == item) {
                    return itemStack.slotId;
                }
            }
        }
        return -1;
    }

    public boolean isContainerOpen() {
        return mc.currentScreen != null && (mc.currentScreen instanceof GuiChest || mc.currentScreen instanceof GuiShulkerBox);
    }

    public void sleep(final int ms) {
        try {
            Thread.sleep(ms);
        }
        catch (Exception ex) {}
    }
}
