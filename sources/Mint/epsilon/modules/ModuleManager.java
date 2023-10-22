package epsilon.modules;

import java.util.concurrent.CopyOnWriteArrayList;

import epsilon.Epsilon;
import epsilon.modules.Module;
import epsilon.modules.render.SmallItem;
import epsilon.settings.Setting;

public class ModuleManager {
	
	public final static SmallItem smallItems = new SmallItem();
	
	private static Module fastget = null;
	private static String sfastget = null;
	private static Setting fastgets = null;
	
	public static CopyOnWriteArrayList<Module> getModules() {
		return Epsilon.modules;
	}
	
	public static Module getModule(final String m) {
		if(sfastget !=null && fastget!=null)
			if(m==sfastget) return fastget;
		
		for(final Module get : getModules()) {
			if(get.getName()==m) {
				fastget = get;
				sfastget = m;
				return get;
			}
		}
		return null;
	}
	
	public static boolean isToggled(final String m) {
		return getModule(m).isToggled();
	}
	
	public static Setting getSetting(final String m, final String s) {

		if(sfastget !=null && fastget!=null) {
			if(m==sfastget) {
				
				if(fastgets!=null)
					if(s==fastgets.getName()) return fastgets;
				
				for(final Setting s2 : fastget.getSettings()) {
					if(s2.getName()==s) {
						fastgets = s2;
						return s2;
					}
				}
				
			}
		}
		
		
		return null;
	}
	
	
}