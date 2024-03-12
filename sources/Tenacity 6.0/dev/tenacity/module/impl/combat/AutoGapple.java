// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.combat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import dev.tenacity.utils.player.InventoryUtils;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import dev.tenacity.event.impl.player.UpdateEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.Module;

public class AutoGapple extends Module
{
    private final NumberSetting minHealHP;
    int slot;
    private int oldSlot;
    private int eatingTicks;
    
    public AutoGapple() {
        super("AutoGapple", "Auto Gapple", Category.COMBAT, "auto eats golden apples");
        this.minHealHP = new NumberSetting("Heal HP", 12.0, 20.0, 1.0, 0.5);
        this.oldSlot = -1;
        this.eatingTicks = 0;
        this.addSettings(this.minHealHP);
    }
    
    @Override
    public void onUpdateEvent(final UpdateEvent e) {
        if (this.eatingTicks == 0 && AutoGapple.mc.thePlayer.getHealth() < this.minHealHP.getValue()) {
            this.slot = this.getAppleFromInventory();
            if (this.slot != -1) {
                this.slot -= 36;
                this.oldSlot = AutoGapple.mc.thePlayer.inventory.currentItem;
                AutoGapple.mc.thePlayer.inventory.currentItem = this.slot;
                AutoGapple.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(AutoGapple.mc.thePlayer.inventory.getCurrentItem()));
                AutoGapple.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange((this.slot + 1 >= 9) ? 0 : (this.slot + 1)));
                AutoGapple.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.slot));
                this.eatingTicks = 40;
            }
        }
        else if (this.eatingTicks > 0) {
            --this.eatingTicks;
            if (this.eatingTicks == 0 && this.oldSlot != -1) {
                AutoGapple.mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(this.oldSlot));
            }
        }
    }
    
    private int getAppleFromInventory() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack stack = AutoGapple.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null) {
                final Item item = stack.getItem();
                if (item != null) {
                    if (!InventoryUtils.isItemEmpty(item)) {
                        if (item == Items.golden_apple) {
                            return i;
                        }
                    }
                }
            }
        }
        for (int i = 9; i < 36; ++i) {
            final ItemStack stack = AutoGapple.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null) {
                final Item item = stack.getItem();
                if (item != null) {
                    if (!InventoryUtils.isItemEmpty(item)) {
                        if (item == Items.golden_apple) {
                            AutoGapple.mc.playerController.windowClick(AutoGapple.mc.thePlayer.openContainer.windowId, i, this.slot, 2, AutoGapple.mc.thePlayer);
                        }
                    }
                }
            }
        }
        return -1;
    }
}
