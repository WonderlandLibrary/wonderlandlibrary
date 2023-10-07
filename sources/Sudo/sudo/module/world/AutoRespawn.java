package sudo.module.world;

import net.minecraft.client.MinecraftClient;
import sudo.module.Mod;

public class AutoRespawn extends Mod {
	public static MinecraftClient mc = MinecraftClient.getInstance();

	public AutoRespawn() {
		super("AutoRespawn", "Automatically respawns when the player dies", Category.WORLD, 0);
	}

	@Override
	public void onTick() {
		if (this.isEnabled()) {
			if(mc.player.isDead()) {
				mc.player.requestRespawn();
				mc.player.closeScreen();;
			}
		}
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