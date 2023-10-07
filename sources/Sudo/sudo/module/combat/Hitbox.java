package sudo.module.combat;

import sudo.module.Mod;
import sudo.module.settings.NumberSetting;

public class Hitbox extends Mod {

	public NumberSetting size = new NumberSetting("Size", 0, 1, 0.2, 0.1);
	
	public Hitbox() {
		super("Hitbox", "Expands entities hitbox", Category.COMBAT, 0);
		addSetting(size);
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
}
