package com.masterof13fps.features.commands.impl;

import com.masterof13fps.Client;
import com.masterof13fps.features.commands.Command;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.utils.NotifyUtil;
import com.masterof13fps.manager.notificationmanager.NotificationType;

public class Toggle extends Command {
	
	String syntax = Client.main().getClientPrefix() + "t <Module>";

	@Override
	public void execute(String[] args) {
		if (args.length == 1) {
			String modName = args[0];
			for (Module m : Client.main().modMgr().getModules()) {
				if (m.name().equalsIgnoreCase(modName)) {
					if (m.state()) {
						m.setState(false);
					} else {
						if (!m.state()) {
							m.setState(true);
						}
					}
				}
			}
		} else {
			notify.notification("Falscher Syntax!", "Nutze §c" + syntax + "§r!", NotificationType.ERROR, 5);
		}
	}

	public Toggle(){
		super("toggle", "t");
	}

}
