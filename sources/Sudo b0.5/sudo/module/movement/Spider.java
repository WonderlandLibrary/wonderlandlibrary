package sudo.module.movement;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import sudo.module.Mod;

public class Spider extends Mod {
	
	private static MinecraftClient mc = MinecraftClient.getInstance();

	
	public Spider() {
		super("Spider", "Allows the player to climb walls", Category.MOVEMENT, 0);
	}
	
	@Override
	public void onTick()
	{
		ClientPlayerEntity player = mc.player;
		if(!player.horizontalCollision)
			return;
		
		Vec3d velocity = player.getVelocity();
		if(velocity.y >= 0.2)
			return;
		
		player.setVelocity(velocity.x, 0.2, velocity.z);
	}
}