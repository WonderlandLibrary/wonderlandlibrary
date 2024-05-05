package client.module.impl.player;

import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.module.impl.player.nofall.CollisionNoFall;
import client.module.impl.player.nofall.MushNoFall;
import client.module.impl.player.nofall.VanillaNoFall;
import client.value.impl.ModeValue;

@ModuleInfo(name = "No Fall", description = "Reduces or eliminates fall damage", category = Category.PLAYER)
public class NoFall extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new VanillaNoFall("Vanilla", this))
            .add(new CollisionNoFall("Collision", this))
            .add(new MushNoFall("Mush", this))
            .setDefault("Vanilla");
}
