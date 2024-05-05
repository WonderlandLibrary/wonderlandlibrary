package Velo.impl.Command;

import org.lwjgl.opengl.Display;

import Velo.api.Command.Command;
import Velo.api.Module.Module;
import Velo.api.Module.ModuleManager;
import Velo.api.Util.Other.ChatUtil;
import Velo.impl.Modules.visuals.hud.HUD;

public class ClientName extends Command {
	
	public ClientName() {
		super("ClientName", "Change the clientname", "clientname <name>", "cn");
	}

	@Override
	public void onCommand(String[] args, String command) {
		if(args.length > 0) {
			HUD.clientname = "";
			for(String arg : args) {
				HUD.clientname += arg + " ";
			}
			Display.setTitle(HUD.clientname);
		}
	}
}
