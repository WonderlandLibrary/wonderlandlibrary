/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.properties.Property;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class AutoSoup
extends Module {
    public static Property<Integer> health;
    private int delay;
    public static int soups;

    public AutoSoup() {
        super("AutoSoup", 0, Category.COMBAT, 15120384, true, "Automaticly soups for you!", new String[]{"as", "autos", "autosop", "asoup", "soup"});
        health = new Property<Integer>(this, "Health", 15);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.setDisplayName("AutoSoup\u00a77 - " + soups);
    }

    @EventTarget
    public void onPre(EventPreMotionUpdates pre) {
        this.getTotalSoups();
        if (this.shouldHeal()) {
            ++this.delay;
        }
        if (this.isSoupOnHotbar() && this.shouldHeal() && this.delay >= 5) {
            this.drinkSoup();
            this.delay = 0;
        } else if (!this.isSoupOnHotbar() && this.shouldHeal() && this.getTotalSoups() > 0) {
            this.getSoup();
        }
        this.setDisplayName("AutoSoup\u00a77 - " + soups);
    }

    private boolean isStackSoup(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemSoup) {
            return true;
        }
        return false;
    }

    private boolean shouldHeal() {
        if (Wrapper.mc.thePlayer.getHealth() <= 14.0f) {
            return true;
        }
        return false;
    }

    private boolean isSoupOnHotbar() {
        int index = 36;
        while (index < 45) {
            ItemStack stack = Wrapper.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack != null && this.isStackSoup(stack)) {
                return true;
            }
            ++index;
        }
        return false;
    }

    private void getSoup() {
        if (!(Wrapper.mc.currentScreen instanceof GuiChest)) {
            int index = 9;
            while (index < 36) {
                ItemStack stack = Wrapper.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                if (!this.isStackSoup(stack)) {
                    this.dropLastSlot();
                }
                if (stack != null && this.isStackSoup(stack)) {
                    Wrapper.mc.playerController.windowClick(0, index, 0, 1, Wrapper.mc.thePlayer);
                    break;
                }
                ++index;
            }
        }
    }

    private void dropLastSlot() {
        int i1 = 0;
        while (i1 < 45) {
            ItemStack itemStack = Wrapper.mc.thePlayer.inventoryContainer.getSlot(i1).getStack();
            if (itemStack != null && i1 > 43 && i1 <= 44) {
                Wrapper.mc.playerController.windowClick(0, i1, 0, 0, Wrapper.mc.thePlayer);
                Wrapper.mc.playerController.windowClick(0, -999, 0, 0, Wrapper.mc.thePlayer);
                break;
            }
            ++i1;
        }
    }

    private void drinkSoup() {
        int slot = 36;
        while (slot < 45) {
            ItemStack stack = Wrapper.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
            if (stack != null && this.isStackSoup(stack)) {
                int lastSlot = Wrapper.mc.thePlayer.inventory.currentItem;
                Wrapper.mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(slot - 36));
                Wrapper.mc.playerController.updateController();
                Wrapper.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(stack));
                Wrapper.mc.playerController.windowClick(0, slot, 0, 1, Wrapper.mc.thePlayer);
                Wrapper.mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(lastSlot));
                break;
            }
            ++slot;
        }
    }

    public int getTotalSoups() {
        soups = 0;
        int index = 9;
        while (index < 45) {
            ItemStack stack = Wrapper.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (this.isStackSoup(stack)) {
                soups += stack.stackSize;
            }
            ++index;
        }
        return soups;
    }
}

