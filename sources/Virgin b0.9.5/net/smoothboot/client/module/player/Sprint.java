package net.smoothboot.client.module.player;

import net.smoothboot.client.events.Event;
import net.smoothboot.client.module.Mod;

public class Sprint extends Mod {
    public Sprint() {
        super("Sprint", "Actives sprint always", Category.Player);
    }

    @Override
    public void onTick() {
        mc.player.setSprinting(
                mc.player.input.movementForward > 0 && !mc.player.isSneaking());
            super.onTick();
    }

}
