package me.aquavit.liquidsense.module.modules.player;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import net.minecraft.client.settings.GameSettings;

@ModuleInfo(name = "AntiAFK", description = "Prevents you from getting kicked for being AFK.", category = ModuleCategory.PLAYER)
public class AntiAFK extends Module {
    private MSTimer timer = new MSTimer();

    @EventTarget
    public void onUpdate(UpdateEvent event){
        mc.gameSettings.keyBindForward.pressed = true;

        if (timer.hasTimePassed(500)) {
            mc.thePlayer.rotationYaw += 180F;
            timer.reset();
        }
    }

    @Override
    public void onDisable() {
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindForward))
            mc.gameSettings.keyBindForward.pressed = false;
    }
}
