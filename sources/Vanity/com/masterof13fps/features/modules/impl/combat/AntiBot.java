package com.masterof13fps.features.modules.impl.combat;

import com.masterof13fps.Client;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.settingsmanager.Setting;
import com.masterof13fps.manager.settingsmanager.SettingsManager;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.features.modules.Category;

@ModuleInfo(name = "AntiBot",category = Category.COMBAT, description = "You don't attack bots")
public class AntiBot extends Module {
    public AntiBot() {

        SettingsManager s = Client.main().setMgr();

        s.addSetting(new Setting("Ticks Existed", this, 30, 0, 100, true));
        s.addSetting(new Setting("Is Alive?", this, true));
    }

    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEvent(Event event) {

    }
}
