package ravenweave.client.module.modules.movement;

import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import ravenweave.client.clickgui.raven.ClickGui;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.SliderSetting;
import ravenweave.client.module.setting.impl.TickSetting;
import ravenweave.client.utils.Utils;

public class Timer extends Module {
    public static SliderSetting speed;
    public static TickSetting strafe;

    public Timer() {
        super("Timer", ModuleCategory.movement);
        this.registerSetting(speed = new SliderSetting("Speed", 1.0D, 0.5D, 2.5D, 0.01D));
        this.registerSetting(strafe = new TickSetting("Strafe only", false));
    }

    @SubscribeEvent
    public void onTick(TickEvent e) {
        if (!(mc.currentScreen instanceof ClickGui)) {
            if (strafe.isToggled() && mc.thePlayer.moveStrafing == 0.0F) {
                Utils.Client.resetTimer();
                return;
            }

            Utils.Client.getTimer().timerSpeed = (float) speed.getInput();
        } else {
            Utils.Client.resetTimer();
        }

    }

    public void onDisable() {
        Utils.Client.resetTimer();
    }
}
