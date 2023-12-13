package me.robbanrobbin.jigsaw.client.bypasses;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;

public class NCP extends ModPreset {
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		
		ClientSettings.KillauraAPS = 11;
		ClientSettings.KillauraRange = 4.2;
		ClientSettings.longJumpTimerSpeed = 0.7;
		ClientSettings.ExtendedReachrange = 4.2;
		ClientSettings.chestStealDelay = true;
		ClientSettings.KBHorizontal = 0.0;
		ClientSettings.KBVertical = 0.0;
		ClientSettings.flightkick = false;
		ClientSettings.glideDmg = true;
		ClientSettings.killauraHitRatio = 1.0;
		ClientSettings.killauraAttackDelay = 0;
		ClientSettings.killauraSmoothRotsMaxRotation = 180;
		ClientSettings.Blockrange = 10;
		
		
		Jigsaw.getModuleByName("Flight").setMode("Glide");
		Jigsaw.getModuleByName("AutoSneak").setMode("Packet");
		Jigsaw.getModuleByName("AutoSprint").setMode("MultiDir");
		Jigsaw.getModuleByName("FastEat").setMode("NCP");
		Jigsaw.getModuleByName("Speed").setMode("NewNCP-1");
		Jigsaw.getModuleByName("NoSlowDown").setMode("NCP");
		
		
		Jigsaw.getModuleByName("AutoBlock").setToggled(true, true);
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
		return "NCP";
	}
	
}
