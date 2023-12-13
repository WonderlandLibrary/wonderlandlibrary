package vestige.util.entity;

import lombok.experimental.UtilityClass;
import net.minecraft.entity.EntityLivingBase;
import vestige.util.base.IMinecraft;

@UtilityClass
public class RotationsUtil implements IMinecraft {
	
	public float[] getRotations(EntityLivingBase entity) {
		double deltaX = entity.posX + (entity.posX - entity.lastTickPosX) - mc.thePlayer.posX,
				deltaY = entity.posY - 3.5 + entity.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
				deltaZ = entity.posZ + (entity.posZ - entity.lastTickPosZ) - mc.thePlayer.posZ,
				distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));

		float yaw = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ)),
				pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));

		if(deltaX < 0 && deltaZ < 0) {
			yaw = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		}else if(deltaX > 0 && deltaZ < 0) {
			yaw = (float) (-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		}

		return new float[] {yaw, pitch};
	}
	
	public float[] getRotationFromPosition(double x, double y, double z) {
		double deltaX = x - mc.thePlayer.posX,
				deltaY = y - mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
				deltaZ = z - mc.thePlayer.posZ,
				distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));

		float yaw = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ)),
				pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));

		if(deltaX < 0 && deltaZ < 0) {
			yaw = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		}else if(deltaX > 0 && deltaZ < 0) {
			yaw = (float) (-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		}

		return new float[] {yaw, pitch};
	}
	
}