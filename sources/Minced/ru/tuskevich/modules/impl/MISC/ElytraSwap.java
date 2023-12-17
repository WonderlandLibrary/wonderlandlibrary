// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MISC;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import ru.tuskevich.event.EventTarget;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "ElytraSwap", desc = "\u0441\u0432\u0430\u043f \u0441\u0443\u043a\u0430", type = Type.MISC)
public class ElytraSwap extends Module
{
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        final Minecraft mc = ElytraSwap.mc;
        if (Minecraft.player.ticksExisted % 4 == 0) {
            swapElytraToChestplate();
        }
    }
    
    public static int getSlowWithArmor() {
        for (int i = 0; i < 45; ++i) {
            final Minecraft mc = ElytraSwap.mc;
            final ItemStack itemStack = Minecraft.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.DIAMOND_CHESTPLATE || itemStack.getItem() == Items.GOLDEN_CHESTPLATE || itemStack.getItem() == Items.LEATHER_CHESTPLATE || itemStack.getItem() == Items.CHAINMAIL_CHESTPLATE || itemStack.getItem() == Items.IRON_LEGGINGS) {
                return (i < 9) ? (i + 36) : i;
            }
        }
        return -1;
    }
    
    public static void swapElytraToChestplate() {
        final Minecraft mc = ElytraSwap.mc;
        for (final ItemStack stack : Minecraft.player.inventory.armorInventory) {
            if (stack.getItem() == Items.ELYTRA) {
                final int slot = (getSlowWithArmor() < 9) ? (getSlowWithArmor() + 36) : getSlowWithArmor();
                if (getSlowWithArmor() == -1) {
                    continue;
                }
                final PlayerControllerMP playerController = ElytraSwap.mc.playerController;
                final int windowId = 0;
                final int slotId = slot;
                final int mouseButton = 1;
                final ClickType pickup = ClickType.PICKUP;
                final Minecraft mc2 = ElytraSwap.mc;
                playerController.windowClick(windowId, slotId, mouseButton, pickup, Minecraft.player);
                final PlayerControllerMP playerController2 = ElytraSwap.mc.playerController;
                final int windowId2 = 0;
                final int slotId2 = 6;
                final int mouseButton2 = 0;
                final ClickType pickup2 = ClickType.PICKUP;
                final Minecraft mc3 = ElytraSwap.mc;
                playerController2.windowClick(windowId2, slotId2, mouseButton2, pickup2, Minecraft.player);
            }
        }
    }
}
