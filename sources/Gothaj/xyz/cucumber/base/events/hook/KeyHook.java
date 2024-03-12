package xyz.cucumber.base.events.hook;

import xyz.cucumber.base.Client;
import xyz.cucumber.base.module.Mod;

public class KeyHook {
	public static void handle(int key) {
		for(Mod m : Client.INSTANCE.getModuleManager().getModules()) {
			if(m.getKey() == key) {
				m.toggle();
			}
		}
	}
}
