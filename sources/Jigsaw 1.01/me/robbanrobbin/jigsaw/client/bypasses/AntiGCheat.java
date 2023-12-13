package me.robbanrobbin.jigsaw.client.bypasses;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;

public class AntiGCheat extends ModPreset {
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		
		ClientSettings.KillauraAPS = 8;
		ClientSettings.KillauraRange = 3.5;
		ClientSettings.ExtendedReachrange = 3.7;
		ClientSettings.chestStealDelay = true;
		ClientSettings.smoothAim = true;
		ClientSettings.smoothAimSpeed = 3.5;
		ClientSettings.AutoClickermax = 10;
		ClientSettings.AutoClickermin = 5;
		ClientSettings.flightkick = false;
		ClientSettings.killauraUseRaytrace = true;
		ClientSettings.useCooldown = false;
		
		
		Jigsaw.getModuleByName("AntiBot").setMode("Hypixel");
		Jigsaw.getModuleByName("Aimbot").setMode("Always");
		Jigsaw.getModuleByName("Flight").setMode("AirWalk");
		Jigsaw.getModuleByName("AutoSneak").setMode("Client");
		Jigsaw.getModuleByName("AutoSprint").setMode("Forwards");
		Jigsaw.getModuleByName("FastEat").setMode("NCP");
		Jigsaw.getModuleByName("BunnyHop").setMode("SlowHop");
		Jigsaw.getModuleByName("Speed").setMode("NewNCP");
		
		
//		Jigsaw.getModuleByName("AutoBlock").setToggled(true, true);
		
		Jigsaw.getModuleByName("Team").setToggled(true, true);
		Jigsaw.getModuleByName("NonPlayers").setToggled(false, true);
		Jigsaw.getModuleByName("Players").setToggled(true, true);
		
		Jigsaw.getModuleByName("ESP").setToggled(true, true);
		Jigsaw.getModuleByName("Tracers").setToggled(true, true);
		Jigsaw.getModuleByName("InvMove").setToggled(true, true);
		Jigsaw.getModuleByName("Animations").setToggled(true, true);
		
		
	}
	
	@Override
	public String getName() {
		return "Badlion (Alpha)";
	}
	
}
