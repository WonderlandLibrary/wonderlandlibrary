package com.masterof13fps.features.modules.impl.player;

import com.masterof13fps.Client;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.settingsmanager.Setting;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventUpdate;
import com.masterof13fps.features.modules.Category;

@ModuleInfo(name = "AutoRespawn", category = Category.PLAYER, description = "Automatically respawns when dead")
public class AutoRespawn extends Module {

    public Setting delay = new Setting("Delay", this, 0,0,1000,false);

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        timeHelper.reset();
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            if (!(mc.thePlayer.isEntityAlive())) {
                double delay = Client.main().setMgr().settingByName("Delay", this).getCurrentValue();
                if (timeHelper.isDelayComplete((long) delay)) {
                    mc.thePlayer.respawnPlayer();
                    timeHelper.reset();
                }
            }
        }
    }
}
