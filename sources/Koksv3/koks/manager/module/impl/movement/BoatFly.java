package koks.manager.module.impl.movement;

import god.buddy.aot.BCompiler;
import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.api.settings.Setting;
import koks.manager.module.ModuleInfo;
import net.minecraft.util.Vec3;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 05:41
 */

@ModuleInfo(name = "BoatFly", description = "You can fly with the boat", category = Module.Category.MOVEMENT)
public class BoatFly extends Module {

    public Setting Mode = new Setting("Mode", new String[] {"AAC4"}, "AAC4", this);
    public Setting AAC4Boost = new Setting("AAC4-Boost", 8F, 1F, 10F,true, this);

    public boolean ride;

    @BCompiler(aot = BCompiler.AOT.NORMAL)
    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if(event instanceof EventUpdate) {
            String extra = Mode.getCurrentMode().equalsIgnoreCase("AAC4") ? " [" + AAC4Boost.getCurrentValue() + "]" : "";
            setInfo(Mode.getCurrentMode() + extra);
            if(mc.thePlayer.isRiding())ride = true;
            else {
                if(ride) {
                    Vec3 look = mc.thePlayer.getLookVec();
                    mc.thePlayer.motionZ = look.zCoord * (AAC4Boost.getCurrentValue());
                    mc.thePlayer.motionX = look.xCoord * (AAC4Boost.getCurrentValue());
                    mc.thePlayer.motionY = 1;
                    ride = false;
                }
            }
        }
    }

    @Override
    public void onEnable() {
        ride = false;
    }

    @Override
    public void onDisable() {

    }
}
