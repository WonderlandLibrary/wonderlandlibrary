// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.combat;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemSoup;
import net.minecraft.init.Items;
import net.augustus.events.EventTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.modules.Module;

public class AutoSoup extends Module
{
    public DoubleValue health;
    public BooleanValue drop;
    public BooleanValue fill;
    public BooleanValue autoOpen;
    public BooleanValue autoClose;
    private int slotOnLastTick;
    private int previousSlot;
    private boolean clicked;
    private boolean dropped;
    
    public AutoSoup() {
        super("AutoSoup", new Color(19, 178, 110), Categorys.COMBAT);
        this.health = new DoubleValue(1, "Health", this, 13.0, 1.0, 20.0, 0);
        this.drop = new BooleanValue(2, "Drop", this, true);
        this.fill = new BooleanValue(4, "Fill", this, true);
        this.autoOpen = new BooleanValue(5, "AutoOpen", this, true);
        this.autoClose = new BooleanValue(5, "AutoClose", this, true);
        this.slotOnLastTick = 0;
    }
    
    @Override
    public void onEnable() {
    }
    
    @EventTarget
    public void onEventClick(final EventTick eventClick) {
        if (AutoSoup.mc.currentScreen == null) {
            if (((AutoSoup.mc.thePlayer.getCurrentEquippedItem() != null && AutoSoup.mc.thePlayer.getCurrentEquippedItem().getItem() == Items.bowl) || this.clicked) && AutoSoup.mc.thePlayer.inventory.currentItem == this.slotOnLastTick) {
                this.dropped = true;
                this.clicked = false;
                if (this.drop.getBoolean()) {
                    AutoSoup.mc.thePlayer.dropOneItem(true);
                    return;
                }
            }
            if (this.dropped) {
                this.dropped = false;
                if (this.previousSlot != -1) {
                    AutoSoup.mc.thePlayer.inventory.currentItem = this.previousSlot;
                }
                this.previousSlot = -1;
            }
            final int slot = this.getSoup();
            if (slot != -1) {
                if (AutoSoup.mc.thePlayer.getHealth() <= this.health.getValue() && !this.clicked) {
                    if (AutoSoup.mc.thePlayer.getCurrentEquippedItem() != null && AutoSoup.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSoup) {
                        AutoSoup.mc.rightClickMouse();
                        this.clicked = true;
                    }
                    else {
                        if (this.previousSlot == -1) {
                            this.previousSlot = AutoSoup.mc.thePlayer.inventory.currentItem;
                        }
                        AutoSoup.mc.thePlayer.inventory.currentItem = slot;
                    }
                }
            }
            else if (this.autoOpen.getBoolean() && this.fill.getBoolean()) {
                final int wholeInv = this.getSoupInWholeInventory();
                if (wholeInv != -1) {
                    this.openInventory();
                }
            }
        }
        else if (AutoSoup.mc.currentScreen instanceof GuiInventory && this.fill.getBoolean()) {
            final int emptySoup = this.getEmptySoup();
            if (emptySoup != -1) {
                if (Math.sin(ThreadLocalRandom.current().nextDouble(0.0, 6.283185307179586)) <= 0.5) {
                    AutoSoup.mc.playerController.windowClick(AutoSoup.mc.thePlayer.inventoryContainer.windowId, emptySoup, 1, 4, AutoSoup.mc.thePlayer);
                }
            }
            else {
                final int slot2 = this.getSoupExceptHotbar();
                boolean full = true;
                for (int i = 0; i < 9; ++i) {
                    final ItemStack item = AutoSoup.mc.thePlayer.inventory.mainInventory[i];
                    if (item == null) {
                        full = false;
                        break;
                    }
                }
                if (this.autoClose.getBoolean() && (slot2 == -1 || full)) {
                    AutoSoup.mc.thePlayer.closeScreen();
                    AutoSoup.mc.displayGuiScreen(null);
                    AutoSoup.mc.setIngameFocus();
                    return;
                }
                AutoSoup.mc.playerController.windowClick(AutoSoup.mc.thePlayer.inventoryContainer.windowId, slot2, 0, 1, AutoSoup.mc.thePlayer);
            }
        }
        this.slotOnLastTick = AutoSoup.mc.thePlayer.inventory.currentItem;
    }
    
    public void openInventory() {
        AutoSoup.mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
        AutoSoup.mc.displayGuiScreen(new GuiInventory(AutoSoup.mc.thePlayer));
    }
    
    public int getSoup() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack item = AutoSoup.mc.thePlayer.inventory.mainInventory[i];
            if (item != null && item.getItem() instanceof ItemSoup) {
                return i;
            }
        }
        return -1;
    }
    
    public int getEmptySoup() {
        if (AutoSoup.mc.currentScreen instanceof GuiInventory) {
            final GuiInventory inventory = (GuiInventory)AutoSoup.mc.currentScreen;
            for (int i = 36; i < 45; ++i) {
                final ItemStack item = inventory.inventorySlots.getInventory().get(i);
                if (item != null && item.getItem() == Items.bowl) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public int getSoupExceptHotbar() {
        for (int i = 9; i < AutoSoup.mc.thePlayer.inventory.mainInventory.length; ++i) {
            final ItemStack item = AutoSoup.mc.thePlayer.inventory.mainInventory[i];
            if (item != null && item.getItem() instanceof ItemSoup) {
                return i;
            }
        }
        return -1;
    }
    
    public int getSoupInWholeInventory() {
        for (int i = 0; i < AutoSoup.mc.thePlayer.inventory.mainInventory.length; ++i) {
            final ItemStack item = AutoSoup.mc.thePlayer.inventory.mainInventory[i];
            if (item != null && item.getItem() instanceof ItemSoup) {
                return i;
            }
        }
        return -1;
    }
}
