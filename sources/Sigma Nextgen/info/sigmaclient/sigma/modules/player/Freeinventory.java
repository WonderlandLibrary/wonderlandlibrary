package info.sigmaclient.sigma.modules.player;

import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Freeinventory extends Module {
    public Freeinventory() {
        super("FreeInventory", Category.Player, "Fix viaversion bug.");
    }
}
