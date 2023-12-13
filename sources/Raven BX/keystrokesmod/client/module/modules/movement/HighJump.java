package keystrokesmod.client.module.modules.movement;

import keystrokesmod.client.module.Module;
import keystrokesmod.client.module.setting.impl.TickSetting;
import keystrokesmod.client.utils.Utils;

public class HighJump extends Module {
    public static TickSetting a;
    public static TickSetting b;
    public static TickSetting c;

    public HighJump() {
        super("HighJump", ModuleCategory.movement);
        this.registerSetting(a = new TickSetting("Stop X", true));
        this.registerSetting(b = new TickSetting("Stop Y", true));
        this.registerSetting(c = new TickSetting("Stop Z", true));
    }

    public void onEnable() {
        if(!Utils.Player.isPlayerInGame()){
            this.disable();
            return;
        }

        mc.thePlayer.motionX *= 0.7;
        mc.thePlayer.motionZ *= 0.7;
        mc.thePlayer.motionY += 0.7;

        this.disable();
    }
}
