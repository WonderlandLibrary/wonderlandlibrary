package rip.helium.cheat.impl.misc;

import me.hippo.systems.lwjeb.annotation.Collect;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.PlayerUpdateEvent;

public class AntiFreeze extends Cheat {

    public AntiFreeze() {
        super("Anti Freeze", "Closes Inventories with 'frozen'", CheatCategory.MISC);
    }

    @Collect
    public void update(PlayerUpdateEvent event) {
        if (mc.thePlayer.inventory.getName().contains("frozen")) {
            mc.thePlayer.inventory.closeInventory(mc.thePlayer);
        }
    }

}
