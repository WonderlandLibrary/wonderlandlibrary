package ravenweave.client.module.modules.movement;

import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.SliderSetting;

public class HClip extends Module {
    public static SliderSetting distance;

    public HClip() {
        super("HClip", ModuleCategory.movement);
        this.registerSetting(distance = new SliderSetting("Distance", 2.0D, -10.0D, 10.0D, 0.5D));
    }

    public void onEnable() {
        double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        double xClip = -Math.sin(yaw) * distance.getInput();
        double zClip = Math.cos(yaw) * distance.getInput();

        mc.thePlayer.setPosition(mc.thePlayer.posX + xClip, mc.thePlayer.posY, mc.thePlayer.posZ + zClip);
        this.disable();
    }
}