package client.module.impl.combat;

import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.module.impl.combat.criticals.PacketCriticals;
import client.value.impl.ModeValue;

@ModuleInfo(name = "Criticals", description = "", category = Category.COMBAT)
public class Criticals extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new PacketCriticals("Packet", this))
            .setDefault("Packet");
}
