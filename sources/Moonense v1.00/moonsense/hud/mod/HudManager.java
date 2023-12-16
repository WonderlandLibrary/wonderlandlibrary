package moonsense.hud.mod;

import java.util.ArrayList;

import moonsense.hud.mod.impl.ArmorStatusMod;
import moonsense.hud.mod.impl.CoordinatesMod;
import moonsense.hud.mod.impl.FPSMod;
import moonsense.hud.mod.impl.Fullbright;
import moonsense.hud.mod.impl.PingMod;
import moonsense.hud.mod.impl.TargetHUD;
import moonsense.hud.mod.impl.TestMod;

public class HudManager {
	
	public ArrayList<HudMod> hudMods = new ArrayList<>();
	
	public TestMod testMod;
	public FPSMod fps;
	public TargetHUD targetHud;
	public CoordinatesMod coords;
	public PingMod ping;
	public ArmorStatusMod armorStatus;
	public Fullbright fullBright;
	
	public HudManager() {
		hudMods.add(testMod = new TestMod());
		hudMods.add(fps = new FPSMod());
		hudMods.add(targetHud = new TargetHUD());
		hudMods.add(coords = new CoordinatesMod());
		hudMods.add(ping = new PingMod());
		hudMods.add(armorStatus = new ArmorStatusMod());
		hudMods.add(fullBright = new Fullbright());
		
	}
	
	public void renderMods() {
		for(HudMod m : hudMods) {
			if(m.isEnabled()) {
				m.draw();	
			}
		}
	}
	
}
