package koks.manager.module.impl.movement;

import god.buddy.aot.BCompiler;
import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.api.settings.Setting;
import koks.manager.module.ModuleInfo;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 16:38
 */

@ModuleInfo(name = "Jesus", description = "You can walk on water", category = Module.Category.MOVEMENT)
public class Jesus extends Module {

    public Setting mode = new Setting("Mode", new String[]{"Vanilla", "Intave"}, "Vanilla", this);

    @BCompiler(aot = BCompiler.AOT.NORMAL)
    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {
            setInfo(mode.getCurrentMode());
            switch (mode.getCurrentMode()) {
                case "Intave":
                    BlockPos bPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

                    if (mc.theWorld.getBlockState(bPos).getBlock() == Blocks.water || mc.theWorld.getBlockState(bPos).getBlock() == Blocks.flowing_water || mc.theWorld.getBlockState(bPos).getBlock() == Blocks.lava || mc.theWorld.getBlockState(bPos).getBlock() == Blocks.flowing_lava) {
                        if (mc.thePlayer.isInWater()) {
                            mc.gameSettings.keyBindJump.pressed = false;
                            mc.thePlayer.motionY = 0.005;

                            mc.thePlayer.onGround = true;
                            mc.thePlayer.motionZ *= 0.9F;
                            mc.thePlayer.motionX *= 0.9F;
                            break;
                        } else {
                            if (!mc.thePlayer.isCollidedHorizontally)
                                mc.thePlayer.motionY = 0;
                            else
                                mc.thePlayer.motionY = 0.02;
                        }
                    }
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
