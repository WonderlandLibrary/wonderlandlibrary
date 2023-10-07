package sudo.utils.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import sudo.utils.misc.Pool;

@SuppressWarnings("unused")
public class RotationUtils {
	public static MinecraftClient mc = MinecraftClient.getInstance();
	
	private static final Pool<Rotation> rotationPool = new Pool<>(Rotation::new);
	private static final List<Rotation> rotations = new ArrayList<>();
	public static float serverPitch;
	public static boolean isCustomPitch = false;
	public static boolean isCustomYaw = false;
	public static float serverYaw;
	
    private static float clientPitch;
    private static float clientYaw;
	private static long lastModificationTime = 0;
    public static void setSilentPitch(float pitch) {
        RotationUtils.serverPitch = pitch;
        isCustomPitch = true;
    }
    
    public static void setSilentYaw(float yaw) {
        RotationUtils.serverYaw = yaw;
        isCustomYaw = true;
    }
    
    public static void resetPitch() {
        isCustomPitch = false;
    }
    
    public static void resetYaw() {
        isCustomYaw = false;
    }
	
	public static void setSilentRotations(LivingEntity target, float yaw, float pitch) {
		mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround((float) RotationUtils.getRotations(target)[0], (float) RotationUtils.getRotations(target)[1], mc.player.isOnGround()));
		RotationUtils.serverPitch = pitch;
		isCustomPitch = true;
		RotationUtils.serverYaw = yaw;
		isCustomYaw = true;
	}
	
	public static void setSilentRotations(float yaw, float pitch) {
		mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, mc.player.isOnGround()));
		RotationUtils.serverPitch = pitch;
		isCustomPitch = true;
		RotationUtils.serverYaw = yaw;
		isCustomYaw = true;
	}
	
	public static float[] getRotationFromPosition(double x, double z, double y) {
	    double xDiff = x - mc.player.getX();
	    double zDiff = z - mc.player.getZ();
	    double yDiff = y - mc.player.getY() - 1.2;
	
	    double dist = MathHelper.sqrt((float) (xDiff * xDiff + zDiff * zDiff));
	    float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
	    float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
	    return new float[]{yaw, pitch};
	}

    public static float[] getSmoothRotations(LivingEntity e) {
        double deltaX = e.getPos().getX() + (e.getPos().getX() - e.lastRenderX) - mc.player.getPos().getX(),
                deltaY = e.getPos().getY() - 3.5 + e.getEyeHeight(e.getPose()) - mc.player.getPos().getY() + mc.player.getEyeHeight(mc.player.getPose()),
                deltaZ = e.getPos().getZ() + (e.getPos().getZ() - e.lastRenderZ) - mc.player.getPos().getZ(),
                distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));

        float yaw = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ)),
                pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));

        if (deltaX < 0 && deltaZ < 0) {
            yaw = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
        } else if (deltaX > 0 && deltaZ < 0) {
            yaw = (float) (-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
        }

        return new float[] {yaw, pitch};
    }
	
	public static float[] getRotations(LivingEntity ent) {
        double x = ent.getX();
        double z = ent.getZ();
        double y = ent.getY() + ent.getStandingEyeHeight() / 2.0F;
        return getRotationFromPosition(x, z, y);
    }
	
	public static float[] getRotations(Entity ent) {
        double x = ent.getX();
        double z = ent.getZ();
        double y = ent.getY() + ent.getStandingEyeHeight() / 2.0F;
        return getRotationFromPosition(x, z, y);
    }
	
	public static double getYaw(Vec3d pos) {
        return mc.player.getYaw() + MathHelper.wrapDegrees((float) Math.toDegrees(Math.atan2(pos.getZ() - mc.player.getZ(), pos.getX() - mc.player.getX())) - 90f - mc.player.getYaw());
    }
	
	public static double getPitch(Vec3d pos) {
        double diffX = pos.getX() - mc.player.getX();
        double diffY = pos.getY() - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        double diffZ = pos.getZ() - mc.player.getZ();

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        return mc.player.getPitch() + MathHelper.wrapDegrees((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)) - mc.player.getPitch());
    }
	
	public static double getYaw(BlockPos pos) {
        return mc.player.getYaw() + MathHelper.wrapDegrees((float) Math.toDegrees(Math.atan2(pos.getZ() + 0.5 - mc.player.getZ(), pos.getX() + 0.5 - mc.player.getX())) - 90f - mc.player.getYaw());
    }

    public static double getPitch(BlockPos pos) {
        double diffX = pos.getX() + 0.5 - mc.player.getX();
        double diffY = pos.getY() + 0.5 - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        double diffZ = pos.getZ() + 0.5 - mc.player.getZ();

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        return mc.player.getPitch() + MathHelper.wrapDegrees((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)) - mc.player.getPitch());
    }
    
    public static void rotate(double yaw, double pitch, int priority, boolean clientSide, Runnable callback) {
        Rotation rotation = rotationPool.get();
        rotation.set(yaw, pitch, priority, clientSide, callback);

        int i = 0;
        for (; i < rotations.size(); i++) {
            if (priority > rotations.get(i).priority) break;
        }

        rotations.add(i, rotation);
    }

    public static void rotate(double yaw, double pitch, int priority, Runnable callback) {
        rotate(yaw, pitch, priority, false, callback);
    }

    public static void rotate(double yaw, double pitch, Runnable callback) {
        rotate(yaw, pitch, 0, callback);
    }

    public static void rotate(double yaw, double pitch) {
        rotate(yaw, pitch, 0, null);
    }
    
    private static class Rotation {
        public double yaw, pitch;
        public int priority;
		public boolean clientSide;
        public Runnable callback;

        public void set(double yaw, double pitch, int priority, boolean clientSide, Runnable callback) {
            this.yaw = yaw;
            this.pitch = pitch;
            this.priority = priority;
            this.clientSide = clientSide;
            this.callback = callback;
        }

		public void sendPacket() {
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround((float) yaw, (float) pitch, mc.player.isOnGround()));
            runCallback();
        }

        public void runCallback() {
            if (callback != null) callback.run();
        }
    }

    public static double getYaw(Entity entity) {
        return mc.player.getYaw() + MathHelper.wrapDegrees((float) Math.toDegrees(Math.atan2(entity.getZ() - mc.player.getZ(), entity.getX() - mc.player.getX())) - 90f - mc.player.getYaw());
    }
    
    public static double getPitch(Entity entity, Target target) {
        double y;
        if (target == Target.Head) y = entity.getEyeY();
        else if (target == Target.Body) y = entity.getY() + entity.getHeight() / 2;
        else y = entity.getY();

        double diffX = entity.getX() - mc.player.getX();
        double diffY = y - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        double diffZ = entity.getZ() - mc.player.getZ();

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        return mc.player.getPitch() + MathHelper.wrapDegrees((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)) - mc.player.getPitch());
    }
    
    public static Vec3d getEyesPos()
	{
		ClientPlayerEntity player = mc.player;
		
		return new Vec3d(player.getX(),
			player.getY() + player.getEyeHeight(player.getPose()),
			player.getZ());
	}
    
    public static Vec3d getLegitLookPos(BlockPos pos, Direction dir, boolean raycast, int res) {
		return getLegitLookPos(new Box(pos), dir, raycast, res, 0.01);
	}

	public static Vec3d getLegitLookPos(Box box, Direction dir, boolean raycast, int res, double extrude) {
		Vec3d eyePos = mc.player.getEyePos();
		Vec3d blockPos = new Vec3d(box.minX, box.minY, box.minZ).add(
				(dir == Direction.WEST ? -extrude : dir.getOffsetX() * box.getXLength() + extrude),
				(dir == Direction.DOWN ? -extrude : dir.getOffsetY() * box.getYLength() + extrude),
				(dir == Direction.NORTH ? -extrude : dir.getOffsetZ() * box.getZLength() + extrude));

		for (double i = 0; i <= 1; i += 1d / (double) res) {
			for (double j = 0; j <= 1; j += 1d / (double) res) {
				Vec3d lookPos = blockPos.add(
						(dir.getAxis() == Axis.X ? 0 : i * box.getXLength()),
						(dir.getAxis() == Axis.Y ? 0 : dir.getAxis() == Axis.Z ? j * box.getYLength() : i * box.getYLength()),
						(dir.getAxis() == Axis.Z ? 0 : j * box.getZLength()));

				if (eyePos.distanceTo(lookPos) > 4.55)
					continue;

				if (raycast) {
					if (mc.world.raycast(new RaycastContext(eyePos, lookPos,
							RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player)).getType() == HitResult.Type.MISS) {
						return lookPos;
					}
				} else {
					return lookPos;
				}
			}
		}

		return null;
	}

	public static double getPitch(Entity entity) {
        return getPitch(entity, Target.Body);
    }
	
    public static Vec2f getPitchYaw(Vec3d targetV3) {
        return getPitchYawFromOtherEntity(Objects.requireNonNull(mc.player).getEyePos(), targetV3);
    }
    
    public static Vec2f getPitchYawFromOtherEntity(Vec3d eyePos, Vec3d targetV3) {
        double vec = 57.2957763671875;
        Vec3d target = targetV3.subtract(eyePos);
        double square = Math.sqrt(target.x * target.x + target.z * target.z);
        float pitch = MathHelper.wrapDegrees((float) (-(MathHelper.atan2(target.y, square) * vec)));
        float yaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(target.z, target.x) * vec) - 90.0F);
        return new Vec2f(pitch, yaw);
    }
    
    public static void setClientPitch(float clientPitch) {
        lastModificationTime = System.currentTimeMillis();
        RotationUtils.clientPitch = clientPitch;
    }
    
    public static void setClientYaw(float clientYaw) {
        lastModificationTime = System.currentTimeMillis();
        RotationUtils.clientYaw = clientYaw;
    }
    
}
