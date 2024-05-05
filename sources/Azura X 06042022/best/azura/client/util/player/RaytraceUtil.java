package best.azura.client.util.player;

import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import net.optifine.reflect.Reflector;

import java.util.List;

public class RaytraceUtil {

	public static MovingObjectPosition rayTrace(double reach, float yaw, float pitch) {
		final Vec3 vec3 = Minecraft.getMinecraft().thePlayer.getPositionEyes(1.0f);
		final Vec3 vec31 = getVectorForRotation(pitch, yaw);
		final Vec3 vec32 = vec3.addVector(vec31.xCoord * reach, vec31.yCoord * reach, vec31.zCoord * reach);
		return Minecraft.getMinecraft().theWorld.rayTraceBlocks(vec3, vec32, false, false, true);
	}

	public static Vec3 getVectorForRotation(float pitch, float yaw) {
		final float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI), f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI),
				f2 = -MathHelper.cos(-pitch * 0.017453292F), f3 = MathHelper.sin(-pitch * 0.017453292F);
		return new Vec3(f1 * f2, f3, f * f2);
	}

	@SuppressWarnings("Guava")
	public static Entity rayCast(double reach, float yaw, float pitch) {
		final Minecraft mc = Minecraft.getMinecraft();
		Entity entity = mc.getRenderViewEntity();
		mc.pointedEntity = null;
		Vec3 vec3 = entity.getPositionEyes(1.0f);
		Vec3 vec31 = getVectorForRotation(pitch, yaw);
		Vec3 vec32 = vec3.addVector(vec31.xCoord * reach, vec31.yCoord * reach, vec31.zCoord * reach);
		Entity pointedEntity = null;
		float f = 1.0F;
		List<Entity> list = mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * reach, vec31.yCoord * reach, vec31.zCoord * reach).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
		double d2 = reach;
		for (Entity value : list) {
			float f1 = value.getCollisionBorderSize();
			AxisAlignedBB axisalignedbb = value.getEntityBoundingBox().expand(f1, f1, f1);
			MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

			if (axisalignedbb.isVecInside(vec3)) {
				if (d2 >= 0.0D) {
					pointedEntity = value;
					d2 = 0.0D;
				}
			} else if (movingobjectposition != null) {
				double d3 = vec3.distanceTo(movingobjectposition.hitVec);

				if (d3 < d2 || d2 == 0.0D) {
					boolean flag1 = false;

					if (Reflector.ForgeEntity_canRiderInteract.exists()) {
						flag1 = Reflector.callBoolean(value, Reflector.ForgeEntity_canRiderInteract);
					}

					if (!flag1 && value == entity.ridingEntity) {
						if (d2 == 0.0D) pointedEntity = value;
					} else {
						pointedEntity = value;
						d2 = d3;
					}
				}
			}
		}
		return pointedEntity;
	}

}
