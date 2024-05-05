package sudo.module.movement;

import net.minecraft.client.option.KeyBinding;
import sudo.module.Mod;

public class AutoWalk extends Mod{

	public AutoWalk() {
		super("AutoWalk", "Automatically makes you walk forward", Category.MOVEMENT, 0);
	}
	
	@Override
	public void onTick() {
		KeyBinding.setKeyPressed(mc.options.forwardKey.getDefaultKey(), true);
		super.onTick();
	}
	
	@Override
	public void onDisable() {
		KeyBinding.setKeyPressed(mc.options.forwardKey.getDefaultKey(), false);
		super.onDisable();
	}
}
