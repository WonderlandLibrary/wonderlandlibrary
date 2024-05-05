package club.async.module.impl.player;

import club.async.event.impl.EventPreUpdate;
import club.async.event.impl.EventUpdate;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.optifine.config.ConnectedParser;
import rip.hippo.lwjeb.annotation.Handler;

@ModuleInfo(name = "NoFall", description = "Prevents FallDamage", category = Category.PLAYER)
public class NoFall extends Module {

    @Handler
    public void preUpdate(EventPreUpdate preUpdate) {
        if (mc.thePlayer.fallDistance > 2.9) {
            preUpdate.setOnGround(true);
        }
    }

}
