/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.combat;

import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.hitentity.PreHitEntityEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.utils.inventory.InventoryUtils;

public class AutoSword
extends Module {
    public AutoSword() {
        super("AutoSword", "Switch to sword when you hit a player", Category.COMBAT);
    }

    @Subscribe
    public void onDamageEntity(PreHitEntityEvent event) {
        if (this.getSlot() == -1) {
            return;
        }
        this.mc.thePlayer.inventory.currentItem = this.getSlot();
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    public int getSlot() {
        int item = -1;
        double highest = 0.0;
        for (int i2 = 36; i2 < 45; ++i2) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(i2).getStack() == null || !(InventoryUtils.getAttackDamage(this.mc.thePlayer.inventoryContainer.getSlot(i2).getStack()) > highest)) continue;
            highest = InventoryUtils.getAttackDamage(this.mc.thePlayer.inventoryContainer.getSlot(i2).getStack());
            item = i2 - 36;
        }
        if (highest == 0.0) {
            return -1;
        }
        return item;
    }
}

