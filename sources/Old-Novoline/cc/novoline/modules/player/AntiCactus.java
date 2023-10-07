package cc.novoline.modules.player;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.CollideWithBlockEvent;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class AntiCactus extends AbstractModule {

    /* constructors */
    public AntiCactus(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "AntiCactus", "Anti Cactus", EnumModuleType.PLAYER, "");
    }

    /* methods */
    @EventTarget
    private void onCollision(CollideWithBlockEvent event) {
        if (event.getBlock() == Blocks.cactus) {
            event.setBoundingBox(new AxisAlignedBB(event.getPos(), event.getPos().add(1, 1, 1)));
        }
    }
}
