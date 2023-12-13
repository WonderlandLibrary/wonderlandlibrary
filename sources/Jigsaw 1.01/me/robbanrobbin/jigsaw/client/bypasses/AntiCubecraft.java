package me.robbanrobbin.jigsaw.client.bypasses;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;

public class AntiCubecraft extends ModPreset {
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		
		ClientSettings.KillauraAPS = 7;
		ClientSettings.KillauraRange = 4.2;
		ClientSettings.killauraHitRatio = 0.79;
		ClientSettings.longJumpTimerSpeed = 0.7;
		ClientSettings.ExtendedReachrange = 3.7;
		ClientSettings.chestStealDelay = true;
		ClientSettings.KBHorizontal = 0.3;
		ClientSettings.KBVertical = 0.1;
		ClientSettings.flightkick = false;
		
		
		Jigsaw.getModuleByName("Flight").setMode("AirWalk");
		Jigsaw.getModuleByName("AutoSneak").setMode("Client");
		Jigsaw.getModuleByName("AutoSprint").setMode("MultiDir");
		Jigsaw.getModuleByName("FastEat").setMode("NCP");
		Jigsaw.getModuleByName("BunnyHop").setMode("SlowHop");
		Jigsaw.getModuleByName("Speed").setMode("NewNCP");
		Jigsaw.getModuleByName("NoSlowDown").setMode("NCP");
		
		
//		Jigsaw.getModuleByName("AutoBlock").setToggled(true, true);
		Jigsaw.getModuleByName("Team").setToggled(true, true);
		Jigsaw.getModuleByName("NonPlayers").setToggled(true, true);
		Jigsaw.getModuleByName("Players").setToggled(true, true);
		Jigsaw.getModuleByName("AntiBot").setToggled(true, true);
		Jigsaw.getModuleByName("Criticals").setToggled(true, true);
		Jigsaw.getModuleByName("ESP").setToggled(true, true);
		Jigsaw.getModuleByName("Tracers").setToggled(true, true);
		Jigsaw.getModuleByName("InvMove").setToggled(true, true);
		Jigsaw.getModuleByName("Animations").setToggled(true, true);
		Jigsaw.getModuleByName("Criticals").setToggled(true, true);
		Jigsaw.getModuleByName("Knockback").setToggled(true, true);
		Jigsaw.getModuleByName("Projectiles").setToggled(true, true);
		Jigsaw.getModuleByName("Criticals").setToggled(true, true);
		Jigsaw.getModuleByName("Jesus").setToggled(true, true);
		Jigsaw.getModuleByName("AutoSprint").setToggled(true, true);
		Jigsaw.getModuleByName("NoSlowDown").setToggled(true, true);
		Jigsaw.getModuleByName("AntiRotate").setToggled(true, true);
		Jigsaw.getModuleByName("ExtendedReach").setToggled(true, true);
		
		
	}
	
	@Override
	public String getName() {
		return "Cubecraft";
	}
	
}
