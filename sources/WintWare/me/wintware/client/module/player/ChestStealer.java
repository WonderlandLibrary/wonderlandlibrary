/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.player;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.other.TimerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;

public class ChestStealer
extends Module {
    TimerHelper timer = new TimerHelper();

    public ChestStealer() {
        super("ChestStealer", Category.Player);
        Main.instance.setmgr.rSetting(new Setting("Stealer Speed", this, 100.0, 0.0, 1000.0, false));
    }

    @EventTarget
    public void onPre(EventPreMotionUpdate event) {
        if (Minecraft.player.openContainer != null) {
            if (Minecraft.player.openContainer instanceof ContainerChest) {
                ContainerChest container = (ContainerChest)Minecraft.player.openContainer;
                for (int i = 0; i < container.inventorySlots.size(); ++i) {
                    if (container.getLowerChestInventory().getStackInSlot(i).getItem() != Item.getItemById(0) && this.timer.check((float)Main.instance.setmgr.getSettingByName("Stealer Speed").getValDouble())) {
                        ChestStealer.mc.playerController.windowClick(container.windowId, i, 0, ClickType.QUICK_MOVE, Minecraft.player);
                        this.timer.reset();
                        continue;
                    }
                    if (!this.empty(container)) continue;
                    Minecraft.player.closeScreen();
                }
            }
        }
    }

    public boolean empty(Container container) {
        int slotAmount;
        boolean voll = true;
        int n = slotAmount = container.inventorySlots.size() == 90 ? 54 : 27;
        for (int i = 0; i < slotAmount; ++i) {
            if (!container.getSlot(i).getHasStack()) continue;
            voll = false;
        }
        return voll;
    }
}

