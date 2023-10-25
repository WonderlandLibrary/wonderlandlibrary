/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.skyblock;

import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.TimerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class AutoSuperiorFinder
extends Module {
    TimerUtils timer = new TimerUtils();

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        for (Entity entity : Minecraft.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityPlayer)) continue;
            EntityPlayer entityPlayer = (EntityPlayer)entity;
            int n = 0;
            while (n < entityPlayer.inventoryContainer.getInventory().size()) {
                ItemStack itemStack = entityPlayer.inventoryContainer.getInventory().get(n);
                if (itemStack != null && itemStack.getDisplayName().contains("Superior") && this.timer.hasReached(500.0)) {
                    this.timer.reset();
                    Exodus.addChatMessage(String.valueOf(entity.getName()) + " has superior armor!" + " " + "(" + itemStack.getDisplayName() + ")");
                }
                ++n;
            }
        }
    }

    public AutoSuperiorFinder() {
        super("AutoSuperiorFinder", 0, Category.SKYBLOCK, "Finds people that have superior.");
    }
}

