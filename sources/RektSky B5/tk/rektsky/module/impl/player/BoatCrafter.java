/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.player;

import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class BoatCrafter
extends Module {
    public BoatCrafter() {
        super("BoatCrafter", "Automatically crafts a boat (For GodMode)", Category.PLAYER);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Subscribe
    public void onWorldTick(WorldTickEvent event) {
        if (this.mc.currentScreen instanceof GuiCrafting) {
            int windowId = this.mc.thePlayer.openContainer.windowId;
            for (Slot inventorySlot : this.mc.thePlayer.inventoryContainer.inventorySlots) {
                if (!inventorySlot.getHasStack() || inventorySlot.getStack().stackSize < 6 || !(inventorySlot.getStack().getItem() instanceof ItemBlock) || ((ItemBlock)inventorySlot.getStack().getItem()).getBlock() != Blocks.log) continue;
                this.mc.playerController.windowClick(windowId, inventorySlot.slotNumber + 1, 0, 0, this.mc.thePlayer);
                this.mc.playerController.windowClick(windowId, 1, 0, 0, this.mc.thePlayer);
                this.mc.playerController.windowClick(windowId, 0, 0, 1, this.mc.thePlayer);
                return;
            }
            for (Slot inventorySlot : this.mc.thePlayer.inventoryContainer.inventorySlots) {
                if (!inventorySlot.getHasStack() || inventorySlot.getStack().stackSize < 6 || !(inventorySlot.getStack().getItem() instanceof ItemBlock) || ((ItemBlock)inventorySlot.getStack().getItem()).getBlock() != Blocks.planks) continue;
                this.mc.playerController.windowClick(windowId, inventorySlot.slotNumber + 1, 0, 0, this.mc.thePlayer);
                this.mc.playerController.windowClick(windowId, 4, 1, 0, this.mc.thePlayer);
                this.mc.playerController.windowClick(windowId, 7, 1, 0, this.mc.thePlayer);
                this.mc.playerController.windowClick(windowId, 8, 1, 0, this.mc.thePlayer);
                this.mc.playerController.windowClick(windowId, 9, 1, 0, this.mc.thePlayer);
                this.mc.playerController.windowClick(windowId, 6, 1, 0, this.mc.thePlayer);
                this.mc.playerController.windowClick(windowId, inventorySlot.slotNumber + 1, 0, 0, this.mc.thePlayer);
                this.mc.playerController.windowClick(windowId, 0, 0, 1, this.mc.thePlayer);
                for (Slot slot : this.mc.thePlayer.inventoryContainer.inventorySlots) {
                    if (!slot.getHasStack() || slot.getStack().stackSize < 6 || !(slot.getStack().getItem() instanceof ItemBlock) || ((ItemBlock)slot.getStack().getItem()).getBlock() != Blocks.planks) continue;
                    this.mc.playerController.windowClick(windowId, slot.slotNumber + 1, 0, 0, this.mc.thePlayer);
                    this.mc.playerController.windowClick(windowId, 4, 1, 0, this.mc.thePlayer);
                    this.mc.playerController.windowClick(windowId, 7, 1, 0, this.mc.thePlayer);
                    this.mc.playerController.windowClick(windowId, 8, 1, 0, this.mc.thePlayer);
                    this.mc.playerController.windowClick(windowId, 9, 1, 0, this.mc.thePlayer);
                    this.mc.playerController.windowClick(windowId, 6, 1, 0, this.mc.thePlayer);
                    this.mc.playerController.windowClick(windowId, slot.slotNumber + 1, 0, 0, this.mc.thePlayer);
                    this.mc.playerController.windowClick(windowId, 0, 0, 1, this.mc.thePlayer);
                    this.mc.thePlayer.closeScreen();
                    return;
                }
                this.mc.thePlayer.closeScreen();
                return;
            }
        }
    }
}

