/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.MISC;

import com.darkmagician6.eventapi.EventTarget;
import me.AveReborn.events.EventUpdate;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class FastPlace
extends Mod {
    public FastPlace() {
        super("FastPlace", Category.MISC);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Minecraft.thePlayer.inventory.getCurrentItem() != null) {
            this.mc.rightClickDelayTimer = Minecraft.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock ? 0 : 4;
        }
    }

    @Override
    public void onDisable() {
        this.mc.rightClickDelayTimer = 4;
        super.onDisable();
    }
}

