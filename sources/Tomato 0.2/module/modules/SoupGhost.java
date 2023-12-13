/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class SoupGhost
extends Module {
    private long currentMS = 0;
    private long lastSoup = -1;

    public SoupGhost() {
        super("SoupGhost", 0, Category.EXPLOITS, -1736894424, true, "", new String[]{"soupg"});
    }

    @EventTarget
    public void onPre(EventPreMotionUpdates event) {
        if (Wrapper.mc.thePlayer.getHealth() < 1.0f) {
            this.eatSoup();
        }
    }

    private void eatSoup() {
        this.currentMS = System.nanoTime() / 1000000;
        if (!this.hasDelayRun(125)) {
            return;
        }
        int oldSlot = Wrapper.mc.thePlayer.inventory.currentItem;
        int slot = 44;
        while (slot >= 9) {
            ItemStack stack = Wrapper.mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
            if (stack != null) {
                if (slot >= 36 && slot <= 44) {
                    if (Item.getIdFromItem(stack.getItem()) == 282) {
                        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot - 36));
                        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Wrapper.mc.thePlayer.inventory.getCurrentItem()));
                        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(oldSlot));
                        this.lastSoup = System.nanoTime() / 1000000;
                        return;
                    }
                } else if (Item.getIdFromItem(stack.getItem()) == 282) {
                    Wrapper.mc.playerController.windowClick(0, slot, 0, 0, Wrapper.mc.thePlayer);
                    Wrapper.mc.playerController.windowClick(0, 44, 0, 0, Wrapper.mc.thePlayer);
                    this.lastSoup = System.nanoTime() / 1000000;
                    return;
                }
            }
            --slot;
        }
    }

    private boolean hasDelayRun(long l2) {
        if (this.currentMS - this.lastSoup >= l2) {
            return true;
        }
        return false;
    }
}

