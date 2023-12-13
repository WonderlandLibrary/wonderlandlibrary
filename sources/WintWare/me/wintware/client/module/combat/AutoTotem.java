/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.combat;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.Event2D;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public class AutoTotem
extends Module {
    public Setting health = new Setting("Health", this, 10.0, 0.1, 20.0, false);
    public Setting counttotem;
    public Setting checkcrystal;

    public AutoTotem() {
        super("AutoTotem", Category.Combat);
        Main.instance.setmgr.rSetting(this.health);
        this.counttotem = new Setting("Count Totem", this, true);
        Main.instance.setmgr.rSetting(this.counttotem);
        this.checkcrystal = new Setting("Check Crystal", this, true);
        Main.instance.setmgr.rSetting(this.checkcrystal);
    }

    @EventTarget
    public void on2D(Event2D event) {
        if (this.fountTotemCount() > 0 && this.counttotem.getValue()) {
            AutoTotem.mc.arraylist.drawStringWithShadow(this.fountTotemCount() + "", event.getWidth() / 2.0f + 7.0f, event.getHeight() / 2.0f, -1);
        }
    }

    public int fountTotemCount() {
        int count = 0;
        int i = 0;
        while (true) {
            if (i >= Minecraft.player.inventory.getSizeInventory()) break;
            ItemStack stack = Minecraft.player.inventory.getStackInSlot(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                ++count;
            }
            ++i;
        }
        return count;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        block4: {
            block3: {
                block2: {
                    if (Minecraft.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING || this.findTotem() == -1) break block2;
                    if ((double)Minecraft.player.getHealth() <= this.health.getValDouble()) break block3;
                }
                if (!this.checkcrystal.getValue() || !this.checkCrystal()) break block4;
            }
            AutoTotem.mc.playerController.windowClick(0, this.findTotem(), 1, ClickType.PICKUP, Minecraft.player);
            AutoTotem.mc.playerController.windowClick(0, 45, 1, ClickType.PICKUP, Minecraft.player);
        }
    }

    private boolean checkCrystal() {
        for (Entity entity : AutoTotem.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            if (!(Minecraft.player.getDistanceToEntity(entity) <= 6.0f)) continue;
            return true;
        }
        return false;
    }

    public int findTotem() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = Minecraft.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() != Items.TOTEM_OF_UNDYING) continue;
            return i;
        }
        return -1;
    }
}

