package markgg.modules.impl.render;

import markgg.modules.ModuleInfo;
import markgg.settings.BooleanSetting;
import markgg.settings.ModeSetting;
import markgg.modules.Module;

@ModuleInfo(name = "HUD", category = Module.Category.RENDER)
public class HUD2 extends Module{

	public ModeSetting markMode = new ModeSetting("HUD Mode", this, "Sense", "Sense", "Classic", "Simple", "LGBT");
	public ModeSetting moduleMode = new ModeSetting("Array", this, "Raze", "Raze", "Simple");
	public BooleanSetting moduleList = new BooleanSetting("Arraylist", this, true);
	public BooleanSetting waterMark = new BooleanSetting("Watermark", this, true);
	public BooleanSetting coordinates = new BooleanSetting("Coordinates", this, true);
	public BooleanSetting lowCase = new BooleanSetting("Lowercase", this, false);
	public BooleanSetting BPS = new BooleanSetting("BPS", this, true);
	public BooleanSetting FPS = new BooleanSetting("FPS", this, false);
	
	public HUD2() {
		toggled = true;
	}
}
