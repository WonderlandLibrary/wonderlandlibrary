package Velo.impl.Command;

import Velo.api.Command.Command;
import Velo.api.Module.Module;
import Velo.api.Module.ModuleManager;
import Velo.api.Util.Other.ChatUtil;
import Velo.impl.Modules.visuals.hud.HUD;

public class ClientVersion extends Command {
	
	public ClientVersion() {
		super("ClientVersion", "Change the clientnversion", "clientversion <clientversion>", "cv", "clientver", "ver");
	}

	@Override
	public void onCommand(String[] args, String command) {
		if(args.length > 0) {
			HUD.clientversion = "";
			for(String arg : args) {
				HUD.clientversion += arg + " ";
			}
		}
	}
}
