package me.crimp.claudius.mod.modules.pvp;

import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.utils.InventoryUtil;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;

public class MainHandTotem extends Module {
    public MainHandTotem() {
        super("MainHandTotem", "Puts Totem In Your MainHand When Your Low Health To Stop You From Dieing To 32k", Category.Pvp, false, false);
    }

    @Override
    public void onUpdate() {
        //this.swapTotem();
        float kek = mc.player.getHealth();
        if (kek < 3.5f) {
            swapTotem();
        }
    }

    public void swapTotem() {
        if (MainHandTotem.mc.player.inventory.getStackInSlot(0).getItem().equals(Items.TOTEM_OF_UNDYING)) {
            return;
        }
        final int totem = InventoryUtil.findStackInventory(Items.TOTEM_OF_UNDYING);
        if (totem != -1 && totem != 0) {
            if (MainHandTotem.mc.currentScreen instanceof GuiHopper) {
                MainHandTotem.mc.playerController.windowClick(MainHandTotem.mc.player.inventoryContainer.windowId, totem - 4, 0, ClickType.SWAP, (EntityPlayer)MainHandTotem.mc.player);
            }
            else if (MainHandTotem.mc.currentScreen instanceof GuiDispenser) {
                MainHandTotem.mc.playerController.windowClick(MainHandTotem.mc.player.inventoryContainer.windowId, totem, 0, ClickType.SWAP, (EntityPlayer)MainHandTotem.mc.player);
            }
            else if (MainHandTotem.mc.currentScreen != null && !(MainHandTotem.mc.currentScreen instanceof GuiInventory)) {
                MainHandTotem.mc.playerController.windowClick(MainHandTotem.mc.player.inventoryContainer.windowId, totem, 0, ClickType.SWAP, (EntityPlayer)MainHandTotem.mc.player);
            }
            else {
                MainHandTotem.mc.playerController.windowClick(MainHandTotem.mc.player.inventoryContainer.windowId, totem, 0, ClickType.SWAP, (EntityPlayer)MainHandTotem.mc.player);
            }
        }
    }
}

