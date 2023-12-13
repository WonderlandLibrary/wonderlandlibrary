package me.earth.phobos.manager;

import me.earth.phobos.util.Util;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryManager
        implements Util {
    public Map<String, List<ItemStack>> inventories = new HashMap<String, List<ItemStack>>();
    private int recoverySlot = -1;

    public void update() {
        if (this.recoverySlot != -1) {
            InventoryManager.mc.player.connection.sendPacket(new CPacketHeldItemChange(this.recoverySlot == 8 ? 7 : this.recoverySlot + 1));
            InventoryManager.mc.player.connection.sendPacket(new CPacketHeldItemChange(this.recoverySlot));
            InventoryManager.mc.player.inventory.currentItem = this.recoverySlot;
            InventoryManager.mc.playerController.syncCurrentPlayItem();
            this.recoverySlot = -1;
        }
    }

    public void recoverSilent(int slot) {
        this.recoverySlot = slot;
    }
}

