package com.ohare.client.command.impl;

import com.ohare.client.Client;
import com.ohare.client.command.Command;
import com.ohare.client.module.Module;
import com.ohare.client.utils.Printer;

import java.util.Objects;

public class Toggle extends Command {

	public Toggle() {
		super("Toggle",new String[]{"t","toggle"});
	}

	@Override
	public void onRun(final String[] s) {
		if (s.length <= 1) {
			Printer.print("Not enough args.");
			return;
		}
			for (Module m : Client.INSTANCE.getModuleManager().getModuleMap().values()) {
				if (m.getLabel().toLowerCase().equals(s[1])) {
					m.toggle();
					Client.INSTANCE.getNotificationManager().addNotification("Toggled " + (Objects.nonNull(m.getRenderlabel()) ? m.getRenderlabel():m.getLabel()), 2000);
                    Printer.print("Toggled " + m.getLabel());
					break;
				}
			}
	}
}
