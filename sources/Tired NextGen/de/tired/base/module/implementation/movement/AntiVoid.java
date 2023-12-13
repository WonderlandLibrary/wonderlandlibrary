package de.tired.base.module.implementation.movement;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.EventPreMotion;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

@ModuleAnnotation(name = "AntiVoid", category = ModuleCategory.MOVEMENT)
public class AntiVoid extends Module {

    @EventTarget
    public void pre(EventPreMotion e) {
        if (!isState()) return;
        if (MC.thePlayer.fallDistance > 2.5) {
            if (MC.thePlayer.posY < 0)
                e.y = (e.y + 4.42f);
            else {
                for (int i = (int) Math.ceil(MC.thePlayer.posY); i >= 0; i--) {
                    if (MC.theWorld.getBlockState(new BlockPos(MC.thePlayer.posX, i, MC.thePlayer.posZ)).getBlock() != Blocks.air)
                        return;
                }
            }
            e.y = (e.y + 4.42f);
        }
    }


    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }
}
