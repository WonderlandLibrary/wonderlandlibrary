package com.initial.modules.impl.player;

import com.initial.utils.player.*;
import com.initial.settings.impl.*;
import com.initial.modules.*;
import com.initial.settings.*;
import com.initial.events.impl.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.gui.inventory.*;
import com.initial.utils.*;
import com.initial.events.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;

public class Stealer extends Module
{
    public Timer timer;
    public Timer closedtimer;
    public DoubleSet stealerSe;
    
    public Stealer() {
        super("Stealer", 0, Category.PLAYER);
        this.timer = new Timer();
        this.closedtimer = new Timer();
        this.stealerSe = new DoubleSet("Delay", 40.0, 0.0, 500.0, 1.0);
        this.addSettings(this.stealerSe);
    }
    
    @EventTarget
    @Override
    public void onEvent(final EventNigger e) {
        if (this.mc.thePlayer == null) {
            this.toggled = false;
        }
        if (this.mc.thePlayer.openContainer != null && this.mc.thePlayer.openContainer instanceof ContainerChest) {
            final ContainerChest chest = (ContainerChest)this.mc.thePlayer.openContainer;
            for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); ++i) {
                if (chest.getLowerChestInventory().getStackInSlot(i) != null && this.timer.hasReached((long)this.stealerSe.getValue())) {
                    this.mc.playerController.windowClick(chest.windowId, i, 0, 1, this.mc.thePlayer);
                    this.timer.reset();
                }
            }
            final GuiChest chest2 = (GuiChest)this.mc.currentScreen;
            if ((!this.hasItems(chest2) || this.isInventoryFull()) && this.closedtimer.hasReached((long)MathUtils.getRandomInRange(75.0, 150.0))) {
                this.mc.thePlayer.closeScreen();
            }
        }
    }
    
    private boolean hasItems(final GuiChest chest) {
        int items = 0;
        for (int rows = chest.getInventoryRows() * 9, i = 0; i < rows; ++i) {
            final Slot slot = chest.inventorySlots.getSlot(i);
            if (slot.getHasStack()) {
                ++items;
            }
        }
        return items > 0;
    }
    
    private boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            final ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }
        return true;
    }
}
