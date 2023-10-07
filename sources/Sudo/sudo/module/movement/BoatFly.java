package sudo.module.movement;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import sudo.module.Mod;

public class BoatFly extends Mod {
	
	private static MinecraftClient mc = MinecraftClient.getInstance();

	
	public BoatFly() {
		super("BoatFly", "Allows the player to fly when in a boat", Category.MOVEMENT, 0);
	}
	
	
	@Override
	public void onTick() {
		if(mc.player!=null && mc.player.hasVehicle()){
            Entity vehicle = mc.player.getVehicle();
            Vec3d velocity = vehicle.getVelocity();
            double motionY = mc.options.jumpKey.isPressed() ? 0.5 : 0;
            vehicle.setVelocity(new Vec3d(velocity.x, motionY, velocity.z));
            vehicle.stepHeight = 3;
            vehicle.fallDistance = 0;
        }
		
		super.onTick();
	}
}
