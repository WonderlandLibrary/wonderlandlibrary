package epsilon.modules.dev;

import org.lwjgl.input.Keyboard;

import epsilon.modules.Module;
import epsilon.modules.Module.Category;
import epsilon.settings.setting.ModeSetting;

public class AutoGroom extends Module{

	public ModeSetting mode = new ModeSetting ("Mode", "Uncodable", "Uncodable", "Reheatable", "Garry", "Nef(Unconfirmed)", "Vamp", "Output", "Spinyfish", "ZER-0", "Segation");
	
	public AutoGroom(){
		super("AutoGroom", Keyboard.KEY_NONE, Category.DEV, "Simulate a pedos chat activity!");
		this.addSettings(mode);
	}
	
}
