package sudo.module.combat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import sudo.Client;
import sudo.module.Mod;
import sudo.module.settings.NumberSetting;
import sudo.utils.player.RotationUtils;

public class AimAssist extends Mod {

	static PlayerEntity target = null;
    public NumberSetting smoothing = new NumberSetting("Smoothing", 0.5, 50, 3, 0.1);
    
	public AimAssist() {
		super("AimAssist", "Automatically aims for you", Category.COMBAT, 0);
		addSettings(smoothing);
	}

	float tempPitch=0, tempYaw=0, BestPitch=0, BestYaw=0;
	@Override
	public void onTick() {
		HitResult hit = mc.crosshairTarget;
		if (mc.player != null) {
			if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
			    if (((EntityHitResult) hit).getEntity() instanceof PlayerEntity player) {
			        target = player;
			    }
			} else if (target == null) return;
			
			int maxDistance = 8;
			if (!(target == null)) {
				if (target.isDead() || mc.player.squaredDistanceTo(target) > maxDistance) target = null;
			}
			
			if (target != null) {
				tempPitch = mc.player.getPitch();
				tempYaw = mc.player.getYaw();

				mc.player.setYaw(newYAW());
				mc.player.setPitch(newPITCH());
			}
		}

		super.onTick();
	}

	@Override
	public void onEnable() {
		Client.logger.info("sex");
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		Client.logger.info("sex2");
		super.onDisable();
	}
	

	private float newYAW() {
		return RotationUtils.getSmoothRotations(target)[0];
//		return RotationUtils.getRotationFromPosition((double) target.getX(), (double) target.getZ(), (double) target.getY()+1)[0];
	}
	
	private float newPITCH() {
		return RotationUtils.getSmoothRotations(target)[1];
//		return RotationUtils.getRotationFromPosition((double) target.getX(), (double) target.getZ(), (double) target.getY()+1)[1];
	}
}