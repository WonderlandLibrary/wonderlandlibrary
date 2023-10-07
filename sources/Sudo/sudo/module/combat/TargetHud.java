package sudo.module.combat;

import sudo.module.Mod;
import sudo.module.settings.NumberSetting;

public class TargetHud extends Mod {

	public static NumberSetting round = new NumberSetting("Roundness", 0, 8, 8, 1);
	public static NumberSetting shadow = new NumberSetting("Shadow", 0, 10, 2, 1);
	
	public TargetHud() {
		super("TargetHud", "Get informations about the current target", Category.COMBAT, 0);
		addSettings(round, shadow);
	}

	@Override
	public void onTick() {
		super.onTick();
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
