package summer.cheat.guiutil;

import java.util.ArrayList;

import summer.base.manager.config.Cheats;



/**
 * Made by HeroCode it's free to use but you have to credit me
 *
 * @author HeroCode
 */
public class SM {

	private ArrayList<Setting> settings;

	public SM() {
		this.settings = new ArrayList<>();
	}

	public void Property(Setting in) {
		this.settings.add(in);
	}

	public ArrayList<Setting> getSettings() {
		return this.settings;
	}

	public ArrayList<Setting> getSettingsByMod(Cheats mod) {
		ArrayList<Setting> out = new ArrayList<>();
		for (Setting s : getSettings()) {
			if (s.getParentMod().equals(mod)) {
				out.add(s);
			}
		}
		if (out.isEmpty()) {
			return null;
		}
		return out;
	}

	public ArrayList<String> getSettingsNameByMod(Cheats mod) {
		ArrayList<String> out = new ArrayList<>();
		for (Setting s : getSettings()) {
			if (s.getParentMod().equals(mod)) {
				out.add(s.getName());
			}
		}
		if (out.isEmpty()) {
			return null;
		}
		return out;
	}

	public Setting getSettingByName(Cheats mod, String name) {
		for (Setting set : getSettings()) {
			if (set.getName().equalsIgnoreCase(name) && set.getParentMod().equals(mod)) {
				return set;
			}
		}
		return null;
	}

	public Setting getSettingByModByName(Cheats mod, String name) {
		for (Setting s : getSettings()) {
			if (s.getParentMod().equals(mod) && s.getName().equalsIgnoreCase(name))
				return s;
		}
		return null;
	}

	public Setting getSettingByName(String name) {
		for (Setting set : getSettings()) {
			if (set.getName().equalsIgnoreCase(name)) {
				return set;
			}
		}
		System.err.println("[" + "Summer" + "] Error Setting NOT found: '" + name + "'!");
		return null;
	}

}