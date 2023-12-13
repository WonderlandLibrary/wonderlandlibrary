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
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public class AutoShield
extends Module {
    public Setting health = new Setting("Health", this, 10.0, 0.1, 20.0, false);

    public AutoShield() {
        super("AutoShield", Category.Combat);
        Main.instance.setmgr.rSetting(this.health);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        block4: {
            if (Minecraft.player.getHeldItemOffhand().getItem() == Items.SHIELD) {
                if (!((double)Minecraft.player.getHealth() <= this.health.getValDouble())) break block4;
            }
        }
        if (Minecraft.player.getHeldItemOffhand().getItem() != Items.SHIELD && this.findShield() != -1) {
            if (AutoShield.mc.currentScreen instanceof GuiInventory || AutoShield.mc.currentScreen == null) {
                if ((double)Minecraft.player.getHealth() <= this.health.getValDouble()) {
                    AutoShield.mc.playerController.windowClick(0, this.findShield(), 1, ClickType.PICKUP, Minecraft.player);
                    AutoShield.mc.playerController.windowClick(0, 45, 1, ClickType.PICKUP, Minecraft.player);
                }
            }
        }
    }

    public int findShield() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = Minecraft.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() != Items.SHIELD) continue;
            return i;
        }
        return -1;
    }
}

