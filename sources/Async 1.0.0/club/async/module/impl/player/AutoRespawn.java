package club.async.module.impl.player;

import club.async.event.impl.EventUpdate;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import rip.hippo.lwjeb.annotation.Handler;

@ModuleInfo(name = "AutoRespawn", description = "Auto. respawns, when dead", category = Category.PLAYER)
public class AutoRespawn extends Module {

    @Handler
    public void update(EventUpdate eventUpdate) {
     if (!mc.thePlayer.isEntityAlive())
         mc.thePlayer.respawnPlayer();
    }

}
