package keystrokesmod.client.module.modules.movement;

import keystrokesmod.client.module.Module;
import keystrokesmod.client.module.setting.impl.TickSetting;
import keystrokesmod.client.utils.Utils;

public class LongJump extends Module {
    public LongJump() {
        super("LongJump", ModuleCategory.movement);
    }

    public void onEnable() {
        if(!Utils.Player.isPlayerInGame()){
            this.disable();
            return;
        }

        mc.thePlayer.motionX *= 1.0;
        mc.thePlayer.motionZ *= 0.7;
        mc.thePlayer.motionY += 1.0;

        this.disable();
    }
}
