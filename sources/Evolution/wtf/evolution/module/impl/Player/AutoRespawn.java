package wtf.evolution.module.impl.Player;

import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(name = "AutoRespawn", type = Category.Player)
public class AutoRespawn extends Module {

    @EventTarget
    public void update(EventUpdate e){
        if (mc.player != null && mc.world != null) {
            if (mc.player.deathTime > 0) {
                mc.player.respawnPlayer();
            }
        }
    }
}
