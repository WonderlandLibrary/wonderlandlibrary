// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.PLAYER;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import ru.tuskevich.event.EventTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemAir;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ContainerChest;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "AirDropStealer", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd \ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.PLAYER)
public class AirStealler extends Module
{
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        final Minecraft mc = AirStealler.mc;
        if (Minecraft.player.openContainer instanceof ContainerChest) {
            final Minecraft mc2 = AirStealler.mc;
            final ContainerChest chest = (ContainerChest)Minecraft.player.openContainer;
            final StringBuilder builder = new StringBuilder();
            final String s = chest.getLowerChestInventory().getDisplayName().getUnformattedText();
            final char[] buffer = s.toCharArray();
            for (int w = 0; w < buffer.length; ++w) {
                final char c = buffer[w];
                if (c == '\ufffd') {
                    ++w;
                }
                else {
                    builder.append(c);
                }
            }
            final String s2 = builder.toString();
            if (s2.contains("\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd")) {
                for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); ++i) {
                    final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(i);
                    if (!(stack.getItem() instanceof ItemAir)) {
                        click(chest.windowId, i, chest.getLowerChestInventory().getSizeInventory());
                    }
                }
            }
        }
    }
    
    public static void click(final int window, final int slot, final int multi) {
        for (int i = 0; i < multi; ++i) {
            final PlayerControllerMP playerController = AirStealler.mc.playerController;
            final int slotId = slot + i;
            final int mouseButton = 0;
            final ClickType quick_MOVE = ClickType.QUICK_MOVE;
            final Minecraft mc = AirStealler.mc;
            playerController.windowClick(window, slotId, mouseButton, quick_MOVE, Minecraft.player);
        }
    }
}
