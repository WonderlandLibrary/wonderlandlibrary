package artemis;

import java.util.ArrayList;

public class ModuleManager {

	public ArrayList<module> modules;
	
	public ModuleManager() {
		(modules = new ArrayList<module>()).clear();
		}
	
	
	public ArrayList<module> getModulesInCategory(Category c) {
		ArrayList<module> mods = new ArrayList<module>();
		for(module m : this.modules) {
			if(m.getCategory() == c) {
				mods.add(m);
			}
		}
		return mods;
	}

	
	
}
