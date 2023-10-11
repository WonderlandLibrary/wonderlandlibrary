/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventBoundingBox;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.util.AxisAlignedBB;

public class AntiCactus
extends Module {
    public AntiCactus() {
        super("AntiCactus", 0, Category.PLAYER, 6697932, true, "Stops you from getting prciked by cactuses", new String[]{"ac", "cactus"});
    }

    @EventTarget
    private void onBoundingBox(EventBoundingBox event) {
        if (event.getBlock() instanceof BlockCactus) {
            event.boundingBox = new AxisAlignedBB(event.getX(), event.getY(), event.getZ(), event.getX() + 1.0, event.getY() + 1.0, event.getZ() + 1.0);
        }
    }
}

