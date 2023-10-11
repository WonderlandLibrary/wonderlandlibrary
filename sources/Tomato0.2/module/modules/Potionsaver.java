/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.Wrapper;
import java.util.Collection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class Potionsaver
extends Module {
    public static int color;
    public static boolean canSave;

    public Potionsaver() {
        super("PotionSaver", 0, Category.PLAYER, 6710886, true, "Potion effects and their timer freeze when not moving", new String[]{"ps", "psaver", "potions", "potsaver"});
    }

    @EventTarget
    public void onPreMotionUpdate(EventPreMotionUpdates event) {
        if (Wrapper.isMoving() || Wrapper.mc.thePlayer.getActivePotionEffects().isEmpty()) {
            if (Wrapper.mc.thePlayer.isInWater() && !Wrapper.isMoving()) {
                Wrapper.mc.thePlayer.isEating();
            }
        } else if (!Wrapper.isMoving()) {
            this.setColor(26367);
            event.setCancelled(true);
            return;
        }
        this.setColor(6710886);
    }
}

