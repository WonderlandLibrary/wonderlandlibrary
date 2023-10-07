package sudo.module.movement;

import net.minecraft.util.math.Vec3d;
import sudo.module.Mod;
import sudo.module.settings.ModeSetting;
import sudo.module.settings.NumberSetting;

public class Speed extends Mod {
	
	public NumberSetting speed = new NumberSetting("Ice speed", 0, 10, 2, 1);
	public ModeSetting mode = new ModeSetting("Mode", "Hop", "Hop", "Ice");
	
	
	public Speed() {
		super("Speed", "Makes the player run faster", Category.MOVEMENT, 0);
		addSettings(mode, speed);
	}
	
	@Override
	public void onTick() {
		nullCheck();
		if (mode.getMode().equalsIgnoreCase("Ice")) {
			if(mc.player.isSneaking() || mc.player.forwardSpeed == 0 && mc.player.sidewaysSpeed == 0)
				return;
			if(mc.player.forwardSpeed > 0 && !mc.player.horizontalCollision)
				mc.player.setSprinting(true);
			if(!mc.player.isOnGround())
				return;
			Vec3d v = mc.player.getVelocity();
			mc.player.setVelocity(v.x * (1 + (speed.getValueFloat() / 10)), v.y + 0.1, v.z * (1 + (speed.getValueFloat() / 10)));
			v = mc.player.getVelocity();
			double currentSpeed = Math.sqrt(Math.pow(v.x, 2) + Math.pow(v.z, 2));
			double maxSpeed = 1.66F;
			if(currentSpeed > maxSpeed)
				mc.player.setVelocity(v.x / currentSpeed * maxSpeed, v.y,
					v.z / currentSpeed * maxSpeed);
		} else if (mode.is("Hop")) {
			if (mc.player.isOnGround() && (mc.options.forwardKey.isPressed() || mc.options.leftKey.isPressed() || mc.options.rightKey.isPressed())) {
				mc.player.jump();
				mc.player.addVelocity(0, -0.5, 0);
			}
		}
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