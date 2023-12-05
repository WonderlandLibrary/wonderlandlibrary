package koks.manager.module.impl.player;

import god.buddy.aot.BCompiler;
import koks.manager.event.Event;
import koks.manager.event.impl.EventHeadLook;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.api.settings.Setting;
import koks.manager.module.ModuleInfo;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 15:29
 */

@ModuleInfo(name = "GodMode", description = "You cant take any damage", category = Module.Category.PLAYER)
public class GodMode extends Module {

    public Setting mode = new Setting("Mode", new String[]{"Intave Border", "DBD"}, "Intave Border", this);

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        switch (mode.getCurrentMode()) {
            case "Intave Border":
                if (event instanceof EventHeadLook) {
                    if (mc.thePlayer.isOutsideBorder()) {
                        EventHeadLook eventHeadLook = (EventHeadLook) event;
                        ((EventHeadLook) event).setF1(mc.thePlayer.rotationYaw);
                        ((EventHeadLook) event).setF2(mc.thePlayer.rotationPitch);
                    }
                }

                if (event instanceof EventUpdate) {
                    setInfo(mode.getCurrentMode());

                    if (mode.getCurrentMode().equalsIgnoreCase("Intave Border")) {
                        if (!mc.thePlayer.isOutsideBorder()) {
                            if (mc.gameSettings.keyBindForward.pressed) {
                                if (timeHelper.hasReached(150)) {
                                    mc.thePlayer.setPosition(mc.thePlayer.posX - Math.sin(Math.toRadians(mc.thePlayer.rotationYaw)) * 0.1, mc.thePlayer.posY, getPlayer().posZ + Math.cos(Math.toRadians(mc.thePlayer.rotationYaw)) * 0.1);
                                    timeHelper.reset();
                                }
                            }
                        } else {
                            mc.thePlayer.motionY = 0;
                            mc.gameSettings.keyBindForward.pressed = false;
                            mc.gameSettings.keyBindBack.pressed = false;
                            mc.gameSettings.keyBindLeft.pressed = false;
                            mc.gameSettings.keyBindRight.pressed = false;
                        }
                    }
                }
                break;
            case "DBD":
                if (event instanceof EventUpdate) {
                    getPlayer().ridingEntity = null;
                }
                break;
        }
    }

    @Override
    public void onEnable() {
        timeHelper.reset();
    }

    @Override
    public void onDisable() {

    }
}
