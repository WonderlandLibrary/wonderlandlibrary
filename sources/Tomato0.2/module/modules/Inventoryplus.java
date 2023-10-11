/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventPacketSent;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;

public class Inventoryplus
extends Module {
    public Inventoryplus() {
        super("Inventory+", 0, Category.EXPLOITS, 16777215, true, "Prevents items from falling out your crafting spots in your inventory when closing", new String[]{"inv+", "xcarry", "moreinventory", "i+", "mi"});
    }

    @EventTarget
    public void onSent(EventPacketSent sent) {
        if (sent.getPacket() instanceof C0DPacketCloseWindow) {
            sent.setCancelled(true);
        }
    }
}

