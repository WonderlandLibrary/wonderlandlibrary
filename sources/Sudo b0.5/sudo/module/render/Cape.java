package sudo.module.render;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import sudo.module.Mod;
import sudo.module.settings.ModeSetting;

public class Cape extends Mod {

	public ModeSetting mode = new ModeSetting("Texture", "Sudo", "Sudo", "2016");
	
	public Cape() {
		super("Cape", "Renders a custom cape on you", Category.RENDER, 0);
		addSettings(mode);
	}
	
	public Identifier getTexture(PlayerEntity player) {
		if (this.isEnabled() && (player == mc.player)) {
			return mode.is("Sudo") ? new Identifier("sudo", "images/cape.png") : new Identifier("sudo", "images/2016.png");
		} else {
			return null;
		}
	}
}
