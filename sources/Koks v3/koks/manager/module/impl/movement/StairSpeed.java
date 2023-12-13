package koks.manager.module.impl.movement;

import god.buddy.aot.BCompiler;
import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

/**
 * @author kroko
 * @created on 14.10.2020 : 03:01
 */

@ModuleInfo(name = "StairSpeed", description = "You are fast on stairs", category = Module.Category.MOVEMENT)
public class StairSpeed extends Module {

    public Setting mode = new Setting("Mode", new String[]{"Intave"}, "Intave", this);

    @BCompiler(aot = BCompiler.AOT.NORMAL)
    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {
            setInfo(mode.getCurrentMode());
            switch (mode.getCurrentMode()) {
                case "Intave":
                    float f1 = (getPlayer().rotationYaw) * 0.017453292F;
                    if(getWorld().getBlockState(new BlockPos((int)mc.thePlayer.posX, (int) mc.thePlayer.posY - 0.5, (int)mc.thePlayer.posZ)).getBlock() instanceof BlockStairs) {
                        getPlayer().motionX -= MathHelper.sin(f1) * 0.22F;
                        getPlayer().motionZ += MathHelper.cos(f1) * 0.22F;
                    }
                    break;
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
