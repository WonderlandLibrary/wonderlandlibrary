package moonsense.mod;

import java.util.ArrayList;

import moonsense.mod.impl.*;

public class ModManager {
	
	public TestMod testMod;
	public ToggleSprint toggleSprint;

	public ArrayList<Mod> mod;

	public ModManager() {
		mod = new ArrayList<>();
		
		//MISC
		mod.add(testMod = new TestMod());
		mod.add(toggleSprint = new ToggleSprint());
	}

}
