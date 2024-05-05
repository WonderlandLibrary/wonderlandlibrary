package artemis.clickgui.Settings;

import java.util.ArrayList;

import artemis.Artemis;
import artemis.module;

//Deine Imports


/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit me
 *
 *  @author HeroCode
 */
public class SettingsManager {
	
	private ArrayList<Setting> settings;
	
	public SettingsManager(){
		this.settings = new ArrayList<Setting>();
	}
	
	public void rSetting(Setting in){
		this.settings.add(in);
	}
	
	public ArrayList<Setting> getSettings(){
		return this.settings;
	}
	
	//@SuppressWarnings("unlikely-arg-type")
	public ArrayList<Setting> getSettingsByMod(module imod){
		ArrayList<Setting> out = new ArrayList<Setting>();
		for(Setting s : getSettings()){
			if(s.getParentMod().equals(imod)){
				out.add(s);
			}
		}
		if(out.isEmpty()){
			return null;
		}
		return out;
	}
	
	public Setting getSettingByName(String name){
		for(Setting set : getSettings()){
			if(set.getName().equalsIgnoreCase(name)){
				return set;
			}
		}
		System.err.println("["+ Artemis.Name + "] Error Setting NOT found: '" + name +"'!");
		return null;
	}

}
