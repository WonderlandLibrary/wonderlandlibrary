/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.Utils;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class Sprint
extends Module {
    private static Utils util = new Utils();

    public Sprint() {
        super("Sprint", 0, Category.MOVEMENT, 16711782, true, "Better TTF Chat", new String[]{"srnt", "spnt"});
    }

    @Override
    public void onUpdate() {
        if (!this.getState()) {
            return;
        }
        if (!Wrapper.mc.thePlayer.isCollidedHorizontally && Wrapper.mc.thePlayer.moveForward > 0.0f && !Wrapper.mc.thePlayer.isSneaking()) {
            Wrapper.mc.thePlayer.setSprinting(true);
        } else {
            Wrapper.mc.thePlayer.setSprinting(false);
        }
    }
}

