package net.minecraft.server.integrated;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayerMP;

class IntegratedServer.3
implements Runnable {
    IntegratedServer.3() {
    }

    public void run() {
        for (EntityPlayerMP entityplayermp : Lists.newArrayList((Iterable)IntegratedServer.this.getConfigurationManager().getPlayerList())) {
            IntegratedServer.this.getConfigurationManager().playerLoggedOut(entityplayermp);
        }
    }
}
