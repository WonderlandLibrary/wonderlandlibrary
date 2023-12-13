package koks.manager.module.impl.movement;

import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.api.settings.Setting;
import koks.manager.module.ModuleInfo;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 16:56
 */

@ModuleInfo(name = "NoCobweb", description = "You doesnt't affect by cobweb", category = Module.Category.MOVEMENT)
public class NoCobweb extends Module {

    public Setting mode = new Setting("Mode", new String[]{"Intave"}, "Intave", this);

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {
            setInfo(mode.getCurrentMode());
            switch (mode.getCurrentMode()) {
                case "Intave":
                    BlockPos bPos = new BlockPos(mc.thePlayer.getPosition());
                    if (!mc.thePlayer.isInWeb) {
                        if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.1, mc.thePlayer.posZ)).getBlock() == Blocks.web) {

                            mc.thePlayer.motionY = 0;
                        }
                    } else {
                        mc.thePlayer.motionY = 0.26;
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
