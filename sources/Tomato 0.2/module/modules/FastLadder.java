/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.MoveEvent;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class FastLadder
extends Module {
    public FastLadder() {
        super("FastLadder", 0, Category.MOVEMENT, 0, true, "Makes you climb up ladders fast", new String[]{"fladder"});
    }

    @EventTarget
    private void onMove(MoveEvent event) {
        if (event.y > 0.0 && Wrapper.mc.thePlayer.isOnLadder()) {
            event.y *= 2.4;
        }
    }
}

