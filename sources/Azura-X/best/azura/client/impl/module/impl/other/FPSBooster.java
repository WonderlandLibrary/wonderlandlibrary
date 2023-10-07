package best.azura.client.impl.module.impl.other;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.value.BooleanValue;


@ModuleInfo(name = "FPS Booster", keyBind = 0, category = Category.OTHER, description = "Boosts the FPS by disabling or changing some features")
public class FPSBooster extends Module {
	
	public static final BooleanValue idleFPS = new BooleanValue("Unfocused FPS", "Sets your FPS to 5 when the window is not focused", true);
	public static final BooleanValue noSkins = new BooleanValue("No Skins", "Save memory by not loading minecraft skins", true);
	public static final BooleanValue noCapes = new BooleanValue("No Capes", "Save memory by not loading minecraft capes", true);
	public static final BooleanValue noTickInvis = new BooleanValue("Don't tick invisibles", "Save fps by not ticking invisible entities", true);

}
