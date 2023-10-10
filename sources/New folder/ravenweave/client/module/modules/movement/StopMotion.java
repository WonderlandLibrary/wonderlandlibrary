package ravenweave.client.module.modules.movement;

import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.TickSetting;
import ravenweave.client.utils.Utils;

public class StopMotion extends Module {
    public static TickSetting a;
    public static TickSetting b;
    public static TickSetting c;

    public StopMotion() {
        super("Stop Motion", ModuleCategory.movement);
        this.registerSetting(a = new TickSetting("Stop X", true));
        this.registerSetting(b = new TickSetting("Stop Y", true));
        this.registerSetting(c = new TickSetting("Stop Z", true));
    }

    public void onEnable() {
        if (!Utils.Player.isPlayerInGame()) {
            this.disable();
            return;
        }

        if (a.isToggled())
            mc.thePlayer.motionX = 0;

        if (b.isToggled())
            mc.thePlayer.motionY = 0;

        if (c.isToggled())
            mc.thePlayer.motionZ = 0;

        this.disable();
    }
}
