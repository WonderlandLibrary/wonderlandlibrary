package io.github.raze.modules.collection.movement;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPosition;

public class Parkour extends AbstractModule {

    public Parkour() {
        super("Parkour", "Jumps on the edge of a block.", ModuleCategory.MOVEMENT);
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getState() == Event.State.PRE) {
            if (mc.theWorld.getBlockState(new BlockPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ)).getBlock() instanceof BlockAir && mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
            }
        }
    }
}