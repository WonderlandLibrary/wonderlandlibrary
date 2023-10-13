/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.player;

import markgg.events.Event;
import markgg.events.listeners.EventMotion;
import markgg.modules.Module;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AntiFire
extends Module {
    public AntiFire() {
        super("AntiFire", 0, Module.Category.PLAYER);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventMotion) {
            EventMotion event = (EventMotion)e;
            if (this.mc.thePlayer.isBurning()) {
                for (int i = 0; i < InventoryPlayer.getHotbarSize(); ++i) {
                    ItemStack itemStack = this.mc.thePlayer.inventory.getStackInSlot(i);
                    if (itemStack == null) continue;
                    itemStack.getItem();
                    if (Item.getIdFromItem(itemStack.getItem()) != 326) continue;
                    this.mc.thePlayer.inventory.currentItem = i;
                }
                if (this.mc.thePlayer.getHeldItem() == null) {
                    return;
                }
                this.mc.thePlayer.getHeldItem().getItem();
                if (Item.getIdFromItem(this.mc.thePlayer.getHeldItem().getItem()) == 326) {
                    float oldpitch = this.mc.thePlayer.rotationPitch;
                    this.mc.thePlayer.rotationPitch = 90.0f;
                    this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem());
                    this.mc.thePlayer.rotationPitch = oldpitch;
                }
            }
        }
    }
}

