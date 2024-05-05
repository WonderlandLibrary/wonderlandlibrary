package sudo.module.movement;

import sudo.module.Mod;

public class Jetpack extends Mod {
	public Jetpack() {
		super("Jetpack", "Gives the player a jetpack (hold space)", Category.MOVEMENT, 0);
	}
	
	
	@Override
	public void onTick() {
		if(mc.options.jumpKey.isPressed()){
            mc.player.jump();
        }
		super.onTick();
	}
}
