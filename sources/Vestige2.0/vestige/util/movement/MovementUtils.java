package vestige.util.movement;

import lombok.experimental.UtilityClass;
import net.minecraft.potion.Potion;
import vestige.Vestige;
import vestige.api.event.impl.MoveEvent;
import vestige.impl.module.combat.Killaura;
import vestige.impl.module.combat.TargetStrafe;
import vestige.util.base.IMinecraft;
import vestige.util.entity.RotationsUtil;
import vestige.util.network.ServerUtils;
import vestige.util.world.WorldUtil;

@UtilityClass
public class MovementUtils implements IMinecraft {
	
	public static final double JUMP_MOTION = 0.41999998688698;
	
	public void strafe() {
		strafe(getHorizontalMotion());
	}
	
	public void strafe(MoveEvent event) {
		strafe(event, getHorizontalMotion());
	}
	
	public void strafe(double speed) {
		float direction = (float) Math.toRadians(getPlayerDirection());
		
		if (isMoving()) {
			mc.thePlayer.motionX = -Math.sin(direction) * speed;
			mc.thePlayer.motionZ = Math.cos(direction) * speed;
		} else {
			mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
		}
	}
	
	public void strafe(MoveEvent event, double speed) {
		float direction;
		
		Killaura killaura = (Killaura) Vestige.getInstance().getModuleManager().getModule(Killaura.class);
		TargetStrafe targetStrafe = (TargetStrafe) Vestige.getInstance().getModuleManager().getModule(TargetStrafe.class);

		if(killaura.isEnabled() && killaura.getTarget() != null && targetStrafe.isEnabled()) {
			if(!WorldUtil.isBlockUnder() || mc.thePlayer.isCollidedHorizontally) {
				targetStrafe.direction = !targetStrafe.direction;
			}
			
			if(mc.thePlayer.getDistanceToEntity(killaura.getTarget()) >= targetStrafe.distance.getCurrentValue()) {
				direction = RotationsUtil.getRotations(killaura.getTarget())[0];
			} else {
				direction = RotationsUtil.getRotations(killaura.getTarget())[0] + (targetStrafe.direction ? 91 - mc.thePlayer.getDistanceToEntity(killaura.getTarget()) * 3 : -91 + mc.thePlayer.getDistanceToEntity(killaura.getTarget()) * 3);
			}
			
			direction = (float) Math.toRadians(direction);
		} else {
			direction = (float) Math.toRadians(getPlayerDirection());
		}
		
		if (isMoving()) {
			event.setX(mc.thePlayer.motionX = -Math.sin(direction) * speed);
			event.setZ(mc.thePlayer.motionZ = Math.cos(direction) * speed);
		} else {
			event.setX(mc.thePlayer.motionX = 0);
			event.setZ(mc.thePlayer.motionZ = 0);
		}
	}
	
	public float getPlayerDirection() {
	    float direction = mc.thePlayer.rotationYaw;
	    
	    if (mc.thePlayer.moveForward > 0) {
	    	if (mc.thePlayer.moveStrafing > 0) {
	    		direction -= 45;
	    	} else if (mc.thePlayer.moveStrafing < 0) {
	    		direction += 45;
	    	}
	    } else if (mc.thePlayer.moveForward < 0) {
	    	if (mc.thePlayer.moveStrafing > 0) {
	    		direction -= 135;
	    	} else if (mc.thePlayer.moveStrafing < 0) {
	    		direction += 135;
	    	} else {
	    		direction -= 180;
	    	}
	    } else {
	    	if (mc.thePlayer.moveStrafing > 0) {
	    		direction -= 90;
	    	} else if (mc.thePlayer.moveStrafing < 0) {
	    		direction += 90;
	    	}
	    }
	    
	    return direction;
	}
	
	public double getHorizontalMotion() {
		return Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ);
	}
	
	public boolean isMoving() {
		return mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0;
	}
	
	public double getBaseMoveSpeed() {
        double speed = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            speed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return speed;
    }
	
	public double getSpeedBoost() {
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			return 0.06 + mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() * 0.06;
		}
		
		return 0;
	}
	
	public boolean isGoingDiagonally() {
		return Math.abs(mc.thePlayer.motionX) > 0.08 && Math.abs(mc.thePlayer.motionZ) > 0.08;
	}
	
	public void motionMult(double mult) {
		mc.thePlayer.motionX *= mult;
		mc.thePlayer.motionZ *= mult;
	}
	
	public void hclip(double dist) {
		float direction = (float) Math.toRadians(mc.thePlayer.rotationYaw);
		mc.thePlayer.setPosition(mc.thePlayer.posX - Math.sin(direction) * dist, mc.thePlayer.posY, mc.thePlayer.posZ + Math.cos(direction) * dist);
	}
	
}