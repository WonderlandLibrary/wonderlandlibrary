package club.async.module.impl.player;

import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import club.async.module.setting.impl.ModeSetting;
import club.async.module.setting.impl.NumberSetting;
import rip.hippo.lwjeb.annotation.Handler;

@ModuleInfo(name = "Clip", description = "Clip down or up wooho Set Y", category = Category.PLAYER)
public class Clip extends Module {
  
    private ModeSetting mode = new ModeSetting("Mode", this, new String[]{"Down", "Up"}, "Down");
    private NumberSetting amount = new NumberSetting("Amount", this,1,5,1, 1);

    @Handler
    public void onEnable() {
        switch (mode.getCurrMode())
        {
            case "Down":
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - amount.getDouble(), mc.thePlayer.posZ);
                break;
            case "Up":
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + amount.getDouble(), mc.thePlayer.posZ);
                break;
        }
        toggle();
    }

}
