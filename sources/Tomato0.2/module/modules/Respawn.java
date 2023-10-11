/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class Respawn
extends Module {
    public Respawn() {
        super("Respawn", 0, Category.PLAYER, 13421619, true, "AutoRespawn", new String[]{"res"});
    }

    @EventTarget
    private void onTick(EventTick event) {
        if (!Wrapper.mc.thePlayer.isEntityAlive()) {
            Wrapper.mc.thePlayer.respawnPlayer();
        }
    }
}

