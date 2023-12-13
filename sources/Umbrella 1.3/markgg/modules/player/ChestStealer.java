/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.player;

import markgg.events.Event;
import markgg.events.listeners.EventUpdate;
import markgg.modules.Module;
import net.minecraft.inventory.ContainerChest;

public class ChestStealer
extends Module {
    public ChestStealer() {
        super("ChestStealer", 0, Module.Category.PLAYER);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventUpdate && this.mc.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest chest = (ContainerChest)this.mc.thePlayer.openContainer;
            for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); ++i) {
                this.mc.playerController.windowClick(chest.windowId, i, 0, 1, this.mc.thePlayer);
            }
        }
    }
}

