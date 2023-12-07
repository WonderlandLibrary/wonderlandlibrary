package me.robbanrobbin.jigsaw.client.tools;

import java.util.ArrayList;
import java.util.Random;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.modules.target.Team;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Utils {

	private static Minecraft mc = Minecraft.getMinecraft();
	private static Random rand = new Random();
	
	public static ArrayList<Integer> blackList = new ArrayList<Integer>();
	
	public static void blinkToPosFromPos(Vec3 src, Vec3 dest, double maxTP) {
		double range = 0;
		double xDist = src.xCoord - dest.xCoord;
		double yDist = src.yCoord - dest.yCoord;
		double zDist = src.zCoord - dest.zCoord;
		double x1 = src.xCoord;
		double y1 = src.yCoord;
		double z1 = src.zCoord;
		double x2 = dest.xCoord;
		double y2 = dest.yCoord;
		double z2 = dest.zCoord;
		range = Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);
		double step = maxTP / range;
		int steps = 0;
		for (int i = 0; i < range; i++) {
			steps++;
			if (maxTP * steps > range) {
				break;
			}
		}
		for (int i = 0; i < steps; i++) {
			double difX = x1 - x2;
			double difY = y1 - y2;
			double difZ = z1 - z2;
			double divider = step * i;
			double x = x1 - difX * divider;
			double y = y1 - difY * divider;
			double z = z1 - difZ * divider;
			//Jigsaw.chatMessage(y);
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
		}
	}
	
	public static boolean isBlacklisted(Entity en) {
		for(int i : blackList) {
			if(en.getEntityId() == i) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<EntityLivingBase> getClosestEntitiesToEntity(float range, Entity ent) {
		ArrayList<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
		for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
			if (Utils.isNotItem(o) && !ent.isEntityEqual((EntityLivingBase) o)) {
				EntityLivingBase en = (EntityLivingBase) o;
				if (ent.getDistanceToEntity(en) < range) {
					entities.add(en);
				}
			}
		}
		return entities;
	}
	
	/**
	 * Returns the distance to the entity. Args: entity
	 */
	public float getDistanceToEntityFromEntity(Entity entityIn, Entity entityIn2) {
		float f = (float) (entityIn.posX - entityIn2.posX);
		float f1 = (float) (entityIn.posY - entityIn2.posY);
		float f2 = (float) (entityIn.posZ - entityIn2.posZ);
		return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
	}

	public static ArrayList<EntityLivingBase> getClosestEntities(float range) {
		ArrayList<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
		for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
			if (isNotItem(o) && !(o instanceof EntityPlayerSP)) {
				EntityLivingBase en = (EntityLivingBase) o;
				if (!validEntity(en)) {
					continue;
				}
				if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) < range) {
					entities.add(en);
				}
			}
		}
		return entities;
	}
	
//	public static boolean checkEntity(boolean friends, boolean invisible, boolean players) {
//		if (en.isEntityEqual(Minecraft.getMinecraft().thePlayer)) {
//			return false;
//		}
//		if (en instanceof EntityPlayer && Jigsaw.getModuleByName("Freecam").isToggled()
//				&& en.getName().equals(Minecraft.getMinecraft().thePlayer.getName())) {
//			return false;
//		}
//		if (en instanceof EntityPlayer && Jigsaw.getModuleByName("Blink").isToggled()
//				&& en.getName().equals(Minecraft.getMinecraft().thePlayer.getName())) {
//			return false;
//		}
//		if (en.isDead) {
//			return false;
//		}
//		if (en.getHealth() <= 0) {
//			return false;
//		}
//		if (!(en instanceof EntityLivingBase)) {
//			return false;
//		}
//		if (en instanceof EntityPlayer && Jigsaw.getFriendsMananger().isFriend((EntityPlayer) en)) {
//			if (!Jigsaw.getModuleByName("Friends").isToggled()) {
//				return false;
//			}
//		}
//		if (en.isInvisible()) {
//			if (!Jigsaw.getModuleByName("Invisible").isToggled()) {
//				return false;
//			}
//		}
//		if (en instanceof EntityPlayer) {
//			if (!Jigsaw.getModuleByName("Players").isToggled() || en.height < 0.21f) {
//				return false;
//			}
//		}
//		if (Team.isOnTeam(en)) {
//			if (!Jigsaw.getModuleByName("Team").isToggled()) {
//				return false;
//			}
//		}
//		if (!(en instanceof EntityPlayer)) {
//			if (!Jigsaw.getModuleByName("NonPlayers").isToggled()) {
//				return false;
//			}
//		}
//		if ((en instanceof EntityPlayer)) {
//			if (Jigsaw.getBypassManager().getEnabledBypass() != null && Jigsaw.getBypassManager().getEnabledBypass().getName().equals("AntiGwen")) {
//				if (!((EntityPlayer) en).didSwingItem) {
//					if (en.onGround) {
//						if (en.isSprinting()) {
//							return true;
//						}
//					} else {
//						if (en.hurtResistantTime == 0) {
//							return false;
//						}
//					}
//				}
//			}
//			if(Jigsaw.getBypassManager().getEnabledBypass() != null && Jigsaw.getBypassManager().getEnabledBypass().getName().equals("AntiWatchdog")) {
//				if(en.ticksExisted < 139) {
//					return false;
//				}
//			}
//		}
//		if(isBlacklisted(en)) {
//			return false;
//		}
//		// if(en.hurtTime > 12 &&
//		// !Jigsaw.getModuleByName("HurtResistant").isToggled()) {
//		// return false;
//		// }
//		return true;
//	}

	public static boolean validEntity(EntityLivingBase en) {
		if (en.isEntityEqual(Minecraft.getMinecraft().thePlayer)) {
			return false;
		}
		if (en instanceof EntityPlayer && Jigsaw.getModuleByName("Freecam").isToggled()
				&& en.getName().equals(Minecraft.getMinecraft().thePlayer.getName())) {
			return false;
		}
		if (en instanceof EntityPlayer && Jigsaw.getModuleByName("Blink").isToggled()
				&& en.getName().equals(Minecraft.getMinecraft().thePlayer.getName())) {
			return false;
		}
		if (en.isDead) {
			return false;
		}
		if (en.getHealth() <= 0) {
			return false;
		}
		if (!(en instanceof EntityLivingBase)) {
			return false;
		}
		if (en instanceof EntityPlayer && Jigsaw.getFriendsMananger().isFriend((EntityPlayer) en)) {
			if (!Jigsaw.getModuleByName("Friends").isToggled()) {
				return false;
			}
		}
		if (en.isInvisible()) {
			if (!Jigsaw.getModuleByName("Invisible").isToggled()) {
				return false;
			}
		}
		if (en instanceof EntityPlayer) {
			if (!Jigsaw.getModuleByName("Players").isToggled() || en.height < 0.21f) {
				return false;
			}
		}
		if (Team.isOnTeam(en)) {
			if (!Jigsaw.getModuleByName("Team").isToggled()) {
				return false;
			}
		}
		if (!(en instanceof EntityPlayer)) {
			if (!Jigsaw.getModuleByName("NonPlayers").isToggled()) {
				return false;
			}
		}
		if (!(en instanceof EntityPlayer) && en instanceof EntityLiving && Jigsaw.getModuleByName("Skip Unarmored Mobs").isToggled()) {
			EntityLiving living = (EntityLiving)en;
			boolean armor = false;
			if(!armor && living.getCurrentArmor(0) != null && living.getCurrentArmor(0).getItem() != null) {
				armor = true;
			}
			if(!armor && living.getCurrentArmor(1) != null && living.getCurrentArmor(1).getItem() != null) {
				armor = true;
			}
			if(!armor && living.getCurrentArmor(2) != null && living.getCurrentArmor(2).getItem() != null) {
				armor = true;
			}
			if(!armor && living.getCurrentArmor(3) != null && living.getCurrentArmor(3).getItem() != null) {
				armor = true;
			}
			if(armor == false) {
				return false;
			}
		}
		if ((en instanceof EntityPlayer) && Jigsaw.getModuleByName("Skip Unarmored Players").isToggled()) {
			EntityPlayer living = (EntityPlayer)en;
			boolean armor = false;
			if(!armor && living.inventory.armorInventory[0] != null && living.inventory.armorInventory[0].getItem() != null) {
				armor = true;
			}
			if(!armor && living.inventory.armorInventory[1] != null && living.inventory.armorInventory[1].getItem() != null) {
				armor = true;
			}
			if(!armor && living.inventory.armorInventory[2] != null && living.inventory.armorInventory[2].getItem() != null) {
				armor = true;
			}
			if(!armor && living.inventory.armorInventory[3] != null && living.inventory.armorInventory[3].getItem() != null) {
				armor = true;
			}
			if(armor == false) {
				return false;
			}
		}
		if ((en instanceof EntityPlayer)) {
			if (Jigsaw.getBypassManager().getEnabledBypass() != null && Jigsaw.getBypassManager().getEnabledBypass().getName().equals("AntiGwen") || Jigsaw.getModuleByName("AntiBot(Gwen)").isToggled()) {
				if (!((EntityPlayer) en).didSwingItem) {
					if (en.onGround) {
						if (en.isSprinting()) {
							return true;
						}
					} else {
						if (en.hurtResistantTime == 0) {
							return false;
						}
					}
				}
			}
			if(Jigsaw.getBypassManager().getEnabledBypass() != null && Jigsaw.getBypassManager().getEnabledBypass().getName().equals("AntiWatchdog") || Jigsaw.getModuleByName("AntiBot(Watchdog)").isToggled()) {
				if(en.ticksExisted < 139) {
					return false;
				}
			}
		}
		if(isBlacklisted(en)) {
			return false;
		}
		// if(en.hurtTime > 12 &&
		// !Jigsaw.getModuleByName("HurtResistant").isToggled()) {
		// return false;
		// }
		return true;
	}

	public static EntityLivingBase getClosestEntity(float range) {
		EntityLivingBase closestEntity = null;
		float mindistance = range;
		for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
			if (isNotItem(o) && !(o instanceof EntityPlayerSP)) {
				EntityLivingBase en = (EntityLivingBase) o;
				if (!validEntity(en)) {
					continue;
				}
				if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) < mindistance) {
					mindistance = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en);
					closestEntity = en;
				}
			}
		}
		return closestEntity;
	}
	
	public static EntityLivingBase getClosestEntitySkipValidCheck(float range) {
		EntityLivingBase closestEntity = null;
		float mindistance = range;
		for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
			if (isNotItem(o) && !(o instanceof EntityPlayerSP)) {
				EntityLivingBase en = (EntityLivingBase) o;
				if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) < mindistance) {
					mindistance = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en);
					closestEntity = en;
				}
			}
		}
		return closestEntity;
	}

	public static EntityLivingBase getClosestEntityToEntity(float range, Entity ent) {
		EntityLivingBase closestEntity = null;
		float mindistance = range;
		for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
			if (isNotItem(o) && !ent.isEntityEqual((EntityLivingBase) o)) {
				EntityLivingBase en = (EntityLivingBase) o;
				if (ent.getDistanceToEntity(en) < mindistance) {
					mindistance = ent.getDistanceToEntity(en);
					closestEntity = en;
				}
			}
		}
		return closestEntity;
	}

	public static boolean isNotItem(Object o) {
		if (!(o instanceof EntityLivingBase)) {
			return false;
		}
		return true;
	}

	public static void faceEntity(Entity en) {
		facePos(new Vec3(en.posX - 0.5, en.posY + (en.getEyeHeight() - en.height / 1.5), en.posZ - 0.5));

	}

	public static void faceBlock(BlockPos blockPos) {
		facePos(getVec3(blockPos));
	}

	public static Vec3 getVec3(BlockPos blockPos) {
		return new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}
	
	public static BlockPos getBlockPos(Vec3 vec) {
		return new BlockPos(vec.xCoord, vec.yCoord, vec.zCoord);
	}

	public static void facePos(Vec3 vec) {
		if (AuraUtils.getSmoothAim()) {
			smoothFacePos(vec);
			return;
		}
		double diffX = vec.xCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posX;
		double diffY = vec.yCoord + 0.5
				- (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		double diffZ = vec.zCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		Minecraft.getMinecraft().thePlayer.rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw
				+ MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw);
		Minecraft.getMinecraft().thePlayer.rotationPitch = Minecraft.getMinecraft().thePlayer.rotationPitch
				+ MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch);
	}

	/**
	 * 
	 * @param vec
	 * @return index 0 = yaw | index 1 = pitch
	 */
	public static float[] getFacePos(Vec3 vec) {
		double diffX = vec.xCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posX;
		double diffY = vec.yCoord + 0.5
				- (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		double diffZ = vec.zCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		return new float[] {
				Minecraft.getMinecraft().thePlayer.rotationYaw
						+ MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw),
				Minecraft.getMinecraft().thePlayer.rotationPitch
						+ MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) };
	}

	/**
	 * 
	 * @param vec
	 * @return index 0 = yaw | index 1 = pitch
	 */
	public static float[] getFacePosRemote(EntityLivingBase facing, Vec3 vec) {
		double diffX = vec.xCoord + 0.5 - facing.posX;
		double diffY = vec.yCoord + 0.5 - (facing.posY + facing.getEyeHeight());
		double diffZ = vec.zCoord + 0.5 - facing.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		return new float[] { facing.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - facing.rotationYaw),
				facing.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - facing.rotationPitch) };
	}

	/**
	 * 
	 * @param vec
	 * @return index 0 = yaw | index 1 = pitch
	 */
	public static float[] getFacePosEntity(Entity en) {
		if (en == null) {
			return new float[] { Minecraft.getMinecraft().thePlayer.rotationYawHead,
					Minecraft.getMinecraft().thePlayer.rotationPitch };
		}
		return getFacePos(new Vec3(en.posX - 0.5, en.posY + (en.getEyeHeight() - en.height / 1.5), en.posZ - 0.5));
	}

	/**
	 * 
	 * @param vec
	 * @return index 0 = yaw | index 1 = pitch
	 */
	public static float[] getFacePosEntityRemote(EntityLivingBase facing, Entity en) {
		if (en == null) {
			return new float[] { facing.rotationYawHead, facing.rotationPitch };
		}
		return getFacePosRemote(facing,
				new Vec3(en.posX - 0.5, en.posY + en.getEyeHeight(), en.posZ - 0.5));
	}

	public static void smoothFacePos(Vec3 vec) {
		double diffX = vec.xCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posX;
		double diffY = vec.yCoord + 0.5
				- (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		double diffZ = vec.zCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;

		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);

		boolean aim = false;
		float max = 5;
		float yawChange = 0;
		if ((MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw)) > max * 2) {
			aim = true;
			yawChange = max;
		} else if ((MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw)) < -max * 2) {
			aim = true;
			yawChange = -max;
		}
		float pitchChange = 0;
		if ((MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch)) > max * 4) {
			aim = true;
			pitchChange = max;
		} else if ((MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch)) < -max
				* 4) {
			aim = true;
			pitchChange = -max;
		}
		// Minecraft.getMinecraft().thePlayer.rotationYaw += yawChange;
		// Minecraft.getMinecraft().thePlayer.rotationPitch += pitchChange;
		if (aim) {
			Minecraft.getMinecraft().thePlayer.rotationYaw += (MathHelper
					.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw))
					/ (AuraUtils.getSmoothAimSpeed() * (rand.nextDouble() * 2 + 1));
			Minecraft.getMinecraft().thePlayer.rotationPitch += (MathHelper
					.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch))
					/ (AuraUtils.getSmoothAimSpeed() * (rand.nextDouble() * 2 + 1));
		}

	}

	public static void smoothFacePos(Vec3 vec, double addSmoothing) {
		double diffX = vec.xCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posX;
		double diffY = vec.yCoord + 0.5
				- (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		double diffZ = vec.zCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;

		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);

		Minecraft.getMinecraft().thePlayer.rotationYaw += (MathHelper
				.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw))
				/ (AuraUtils.getSmoothAimSpeed() * addSmoothing);
		Minecraft.getMinecraft().thePlayer.rotationPitch += (MathHelper
				.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch))
				/ (AuraUtils.getSmoothAimSpeed() * addSmoothing);
	}

	// public static int getDistanceFromMouse(Entity entity)
	// {
	// float[] neededRotations = getRotationsNeeded(entity);
	// if(neededRotations != null)
	// {
	// float neededYaw =
	// Minecraft.getMinecraft().thePlayer.rotationYaw
	// - neededRotations[0], neededPitch =
	// Minecraft.getMinecraft().thePlayer.rotationPitch
	// - neededRotations[1];
	// float distanceFromMouse =
	// MathHelper.sqrt_float(neededYaw * neededYaw + neededPitch
	// * neededPitch);
	// return (int)distanceFromMouse;
	// }
	// return -1;
	// }
	// public static float[] getRotationsNeeded(Entity entity)
	// {
	// if(entity == null)
	// return null;
	// double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
	// double diffY;
	// if(entity instanceof EntityLivingBase)
	// {
	// EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
	// diffY =
	// entityLivingBase.posY
	// + entityLivingBase.getEyeHeight()
	// * 0.9
	// - (Minecraft.getMinecraft().thePlayer.posY + Minecraft
	// .getMinecraft().thePlayer.getEyeHeight());
	// }else
	// diffY =
	// (entity.boundingBox.minY + entity.boundingBox.maxY)
	// / 2.0D
	// - (Minecraft.getMinecraft().thePlayer.posY + Minecraft
	// .getMinecraft().thePlayer.getEyeHeight());
	// double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
	// double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
	// float yaw =
	// (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
	// float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / Math.PI);
	// if(AuraUtils.getSmoothAim()) {
	// return new float[]{
	// (float) (MathHelper.wrapAngleTo180_float(yaw
	// - Minecraft.getMinecraft().thePlayer.rotationYaw) /
	// AuraUtils.getSmoothAimSpeed()),
	// (float) (MathHelper.wrapAngleTo180_float(pitch
	// - Minecraft.getMinecraft().thePlayer.rotationPitch) /
	// AuraUtils.getSmoothAimSpeed())};
	// }
	// return new float[]{
	// Minecraft.getMinecraft().thePlayer.rotationYaw
	// + MathHelper.wrapAngleTo180_float(yaw
	// - Minecraft.getMinecraft().thePlayer.rotationYaw),
	// Minecraft.getMinecraft().thePlayer.rotationPitch
	// + MathHelper.wrapAngleTo180_float(pitch
	// - Minecraft.getMinecraft().thePlayer.rotationPitch)};
	//
	// }
	// public static float[] getRotationsNeededRemote(EntityLivingBase remote,
	// Entity entity)
	// {
	// if(entity == null)
	// return null;
	// double diffX = entity.posX - remote.posX;
	// double diffY;
	// if(entity instanceof EntityLivingBase)
	// {
	// EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
	// diffY =
	// entityLivingBase.posY
	// + entityLivingBase.getEyeHeight()
	// * 0.9
	// - (remote.posY + Minecraft
	// .getMinecraft().thePlayer.getEyeHeight());
	// }else
	// diffY =
	// (entity.boundingBox.minY + entity.boundingBox.maxY)
	// / 2.0D
	// - (remote.posY + remote.getEyeHeight());
	// double diffZ = entity.posZ - remote.posZ;
	// double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
	// float yaw =
	// (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
	// float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / Math.PI);
	// if(AuraUtils.getSmoothAim()) {
	// return new float[]{
	// (float) (MathHelper.wrapAngleTo180_float(yaw
	// - remote.rotationYaw) / AuraUtils.getSmoothAimSpeed()),
	// (float) (MathHelper.wrapAngleTo180_float(pitch
	// - remote.rotationPitch) / AuraUtils.getSmoothAimSpeed())};
	// }
	// return new float[]{
	// remote.rotationYaw
	// + MathHelper.wrapAngleTo180_float(yaw
	// - remote.rotationYaw),
	// remote.rotationPitch
	// + MathHelper.wrapAngleTo180_float(pitch
	// - remote.rotationPitch)};
	//
	// }
	public static float getPlayerBlockDistance(BlockPos blockPos) {
		return getPlayerBlockDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	public static float getPlayerBlockDistance(double posX, double posY, double posZ) {
		float xDiff = (float) (Minecraft.getMinecraft().thePlayer.posX - posX);
		float yDiff = (float) (Minecraft.getMinecraft().thePlayer.posY - posY);
		float zDiff = (float) (Minecraft.getMinecraft().thePlayer.posZ - posZ);
		return getBlockDistance(xDiff, yDiff, zDiff);
	}

	public static float getBlockDistance(float xDiff, float yDiff, float zDiff) {
		return MathHelper.sqrt_float(
				(xDiff - 0.5F) * (xDiff - 0.5F) + (yDiff - 0.5F) * (yDiff - 0.5F) + (zDiff - 0.5F) * (zDiff - 0.5F));
	}

	public static ArrayList<EntityItem> getNearbyItems(int range) {
		ArrayList<EntityItem> eList = new ArrayList<EntityItem>();
		for (Object o : Minecraft.getMinecraft().theWorld.getLoadedEntityList()) {
			if (!(o instanceof EntityItem)) {
				continue;
			}
			EntityItem e = (EntityItem) o;
			if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(e) >= range) {
				continue;
			}

			eList.add(e);
		}
		return eList;
	}

	public static EntityItem getClosestItem(float range) {
		float mindistance = range;
		EntityItem ee = null;
		for (Object o : Minecraft.getMinecraft().theWorld.getLoadedEntityList()) {
			if (!(o instanceof EntityItem)) {
				continue;
			}
			EntityItem e = (EntityItem) o;
			if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(e) >= mindistance) {
				continue;
			}
			ee = e;
		}
		return ee;
	}

	public static Entity getClosestItemOrXPOrb(float range) {
		float mindistance = range;
		Entity ee = null;
		for (Object o : Minecraft.getMinecraft().theWorld.getLoadedEntityList()) {
			if (!(o instanceof EntityItem) && !(o instanceof EntityXPOrb)) {
				continue;
			}
			Entity e = (Entity) o;
			if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(e) >= mindistance) {
				continue;
			}
			ee = e;
		}
		return ee;
	}

	private final static float limitAngleChange(final float current, final float intended, final float maxChange) {
		float change = intended - current;
		if (change > maxChange)
			change = maxChange;
		else if (change < -maxChange)
			change = -maxChange;
		return current + change;
	}

	public static double normalizeAngle(double angle) {
		return (angle + 360) % 360;
	}

	public static float normalizeAngle(float angle) {
		return (angle + 360) % 360;
	}

	public static int getItemIndexHotbar(int itemID) {
		for (int i = 0; i < 9; i++) {
			ItemStack stackInSlot = mc.thePlayer.inventory.getStackInSlot(i);
			if (stackInSlot == null) {
				continue;
			}
			if (itemID == Item.getIdFromItem(stackInSlot.getItem())) {
				return i;
			}
		}
		return -1;
	}

	public static boolean isBlockPosAir(BlockPos blockPos) {
		return mc.theWorld.getBlockState(blockPos).getBlock().getMaterial() == Material.air;
	}

	public static Block getBlockRelativeToEntity(Entity en, double d) {
		return getBlock(new BlockPos(en.posX, en.posY + d, en.posZ));
	}
	
	public static BlockPos getBlockPosRelativeToEntity(Entity en, double d) {
		return new BlockPos(en.posX, en.posY + d, en.posZ);
	}
	
	public static Block getBlock(BlockPos pos) {
		return mc.theWorld.getBlockState(pos).getBlock();
	}
	
	private static Vec3 lastLoc = null;
	
	public static Vec3 getLastGroundLocation() {
		return lastLoc;
		
	}
	
	public static void updateLastGroundLocation() {
		if(mc.thePlayer.onGround) {
			lastLoc = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
		}
	}
	
	public static double getXZDist(Vec3 loc1, Vec3 loc2) {
		double xDist = loc1.getX() - loc2.getX();
		double zDist = loc1.getZ() - loc2.getZ();
		return Math.abs(Math.sqrt(xDist * xDist + zDist * zDist));
	}
}
