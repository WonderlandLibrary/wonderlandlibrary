package me.aidanmees.trivia.client.tools;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.modules.Movement.Flight;
import me.aidanmees.trivia.client.settings.ClientSettings;

public class Bypass {

	
	public static void Watchdog() {
		
		if (!EqualsMode("Flight","Hypixel")) {
			
			ModeChanger("Flight", "Hypixel");
		}
		if (!IsToggled("AntiBot")) {
			Toggle("Antibot");
		}
		if(!EqualsMode("Antibot", "Invisible")) {
			ModeChanger("Antibot", "Invisible");
		}
		if (ClientSettings.rangeBeta > 4.3) {
			ClientSettings.rangeBeta = 4.3;
			BeenChanged("KillauraBeta Range");
			
		}
		if (!ClientSettings.HypixelNofall) {
			ClientSettings.HypixelNofall = true;
			trivia.chatMessage("Hypixel mode has been enabled for Hypixel!");
		}
		if (ClientSettings.AUTOBLOCKBETA) {
			ClientSettings.AUTOBLOCKBETA = false;
			trivia.chatMessage("AutoBlock has been disabled because it currenly doesn't bypass!");
		}
		if (ClientSettings.apsBeta > 15) {
			ClientSettings.apsBeta = 13;
			BeenChanged("KillauraBeta APS");
		}
		if (IsToggled("MoreKnockback")) {
			Toggle("MoreKnockback");
			trivia.chatMessage("Moreknockback has been toggled off because it doesn't bypass!");
		}
if (!IsToggled("Speed")) {
			
			Toggle("Speed");
			trivia.chatMessage("We currently do not have a bypassing speed mode!");
		}

if (ClientSettings.AUTOBLOCKBETA) {
	
	ClientSettings.AUTOBLOCKBETA = false;
	trivia.chatMessage("AutoBlock currently doesn't bypass!");
}
if (ClientSettings.autoblockn) {
	
	ClientSettings.autoblockn = false;
	trivia.chatMessage("AutoBlock currently doesn't bypass!");
}
		
		if(!EqualsMode("Step", "Packet")) {
			ModeChanger("Step", "Packet");
		}
		 if (!EqualsMode("LongJump","NCP") && !EqualsMode("LongJump","New")) {
				
				ModeChanger("LongJump", "NCP");
			}
		 if (IsToggled("NoSwing")) {
				Toggle("NoSwing");
				trivia.chatMessage("Toggled off noswing!");
		 }
		if (trivia.getModuleByName("NCP").isToggled()) {
			trivia.getModuleByName("NCP").toggle();
		}
		if (trivia.getModuleByName("Guardian").isToggled()) {
			trivia.getModuleByName("Guardian").toggle();
		}
		if (trivia.getModuleByName("AAC").isToggled()) {
			trivia.getModuleByName("AAC").toggle();
		}
		if (trivia.getModuleByName("Gwen").isToggled()) {
			trivia.getModuleByName("Gwen").toggle();
		}
		
		
		
		
	}
	public static void NCP() {
		if(!EqualsMode("Step", "Packet")) {
			ModeChanger("Step", "Packet");
		}
		
		if (!EqualsMode("Speed","SlowHop")) {
			
			ModeChanger("Speed", "SlowHop");
		}
		
        if (!EqualsMode("LongJump","NCP") && !EqualsMode("LongJump","New")) {
			
			ModeChanger("LongJump", "NCP");
		}
		
        if (IsToggled("Airjump")) {
			Toggle("Airjump");
			trivia.chatMessage("Toggled off Airjump because it doesn't bypass!");
			
		}
		
		if (IsToggled("NoSwing")) {
			Toggle("NoSwing");
			trivia.chatMessage("Toggled off noswing!");
			
		}
		
		if (ClientSettings.apsBeta > 16) {
			ClientSettings.apsBeta = 16;
			BeenChanged("KillauraBeta APS");
		}
		if (ClientSettings.rangeBeta > 4.3) {
			ClientSettings.rangeBeta = 4.3;
			BeenChanged("KillauraBeta Range");
			
		}
		

		
		if (trivia.getModuleByName("AAC").isToggled()) {
			trivia.getModuleByName("AAC").toggle();
		}
		if (trivia.getModuleByName("Guardian").isToggled()) {
			trivia.getModuleByName("Guardian").toggle();
		}
		if (trivia.getModuleByName("Watchdog").isToggled()) {
			trivia.getModuleByName("Watchdog").toggle();
		}
		if (trivia.getModuleByName("Gwen").isToggled()) {
			trivia.getModuleByName("Gwen").toggle();
		}
	}
	public static void AAC() {
		if (ClientSettings.apsBeta > 14) {
			ClientSettings.apsBeta = 14;
			BeenChanged("KillauraBeta APS");
		}
		if (ClientSettings.rangeBeta > 4.2) {
			ClientSettings.rangeBeta = 4.2;
			BeenChanged("KillauraBeta Range");
			
		}
		
		if (IsToggled("NoSwing")) {
			trivia.chatMessage("Toggled off noswing!");
			
		}
		
		if (trivia.getModuleByName("NCP").isToggled()) {
			trivia.getModuleByName("NCP").toggle();
		}
		if (trivia.getModuleByName("Guardian").isToggled()) {
			trivia.getModuleByName("Guardian").toggle();
		}
		if (trivia.getModuleByName("Watchdog").isToggled()) {
			trivia.getModuleByName("Watchdog").toggle();
		}
		if (trivia.getModuleByName("Gwen").isToggled()) {
			trivia.getModuleByName("Gwen").toggle();
		}
	}
	public static void Guardian() {
		if ((trivia.getModuleByName("InfiniteReach").isToggled() || trivia.getModuleByName("ReachAura").isToggled()) && !trivia.getModuleByName("NoSwing").isToggled()) {
			trivia.getModuleByName("NoSwing").toggle();
			trivia.chatMessage("NoSwing has been enabled in order to make tpaura bypass!");
		}
		if ((trivia.getModuleByName("MoreKnockback").isToggled())){
			trivia.getModuleByName("MoreKnockback").toggle();
			trivia.chatMessage("MoreKnockback has been Disabled, Because this module doesn't bypass!");
		}
		
		
		if (!EqualsMode("Flight","Guardian") && !EqualsMode("Flight","Guardian-Fast")) {
			
			ModeChanger("Flight","Guardian");
		}
		if (!EqualsMode("Speed","Guardian-Hop") && !EqualsMode("Speed","Guardian-YPort")) {
		
			ModeChanger("Speed","Guardian-Hop");
		}
		
		if (!EqualsMode("LongJump", "Guardian")) {
			ModeChanger("LongJump", "Guardian");
		}

		
		if (trivia.getModuleByName("NCP").isToggled()) {
			trivia.getModuleByName("NCP").toggle();
		}
		if (trivia.getModuleByName("AAC").isToggled()) {
			trivia.getModuleByName("AAC").toggle();
		}
		if (trivia.getModuleByName("Watchdog").isToggled()) {
			trivia.getModuleByName("Watchdog").toggle();
		}
		if (trivia.getModuleByName("Gwen").isToggled()) {
			trivia.getModuleByName("Gwen").toggle();
		}
	}
	public static void Gwen() {
		
		
	/*	if (!EqualsMode("Flight","Mineplex")) {
			
			ModeChanger("Flight", "Mineplex");
		
		}*/
		if (IsToggled("Flight")) {
			Toggle("Flight");
			trivia.chatMessage("Flight has been toggled of because it currently doesn't fully bypass!");
		}
		
		if (!EqualsMode("Speed","Mineplex")) {
			
			ModeChanger("Speed", "Mineplex");
		}	

		
		if (trivia.getModuleByName("NCP").isToggled()) {
			trivia.getModuleByName("NCP").toggle();
		}
		if (trivia.getModuleByName("Guardian").isToggled()) {
			trivia.getModuleByName("Guardian").toggle();
		}
		if (trivia.getModuleByName("Watchdog").isToggled()) {
			trivia.getModuleByName("Watchdog").toggle();
		}
		if (trivia.getModuleByName("AAC").isToggled()) {
			trivia.getModuleByName("AAC").toggle();
		}
	}
	private static void BeenChanged(String text) {
		trivia.chatMessage("Your "+ text +" has been changed!");
	}
	private static void ModeChanger(String modname, String to) {
		
		trivia.chatMessage("Your "+modname+" mode has been set from "+trivia.getModuleByName(modname).getCurrentMode() +" to "+ to +"!");
		trivia.getModuleByName(modname).setMode(to);
		
	}
	private static boolean EqualsMode(String ModName,String Mode) {
		return trivia.getModuleByName(ModName).getCurrentMode().equals(Mode);
	}
	private static boolean IsToggled(String MOD) {
		return trivia.getModuleByName(MOD).isToggled();
	}
	private static void Toggle(String MOD) {
		 trivia.getModuleByName(MOD).toggle();
	}
	
}

	  

