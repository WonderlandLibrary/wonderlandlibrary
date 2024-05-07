package com.masterof13fps.manager.eventmanager;

import com.masterof13fps.Client;
import com.masterof13fps.features.commands.Command;
import com.masterof13fps.features.modules.Module;

public class EventManager {

    public void onEvent(Event event) {
        try {
            for (Module module : Client.main().modMgr().getModules()) {
                if (module.enabled) {
                    module.onEvent(event);
                }
            }
            for (Command cmd : Client.main().getCommandManager().getCommands()){
                cmd.onEvent(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
