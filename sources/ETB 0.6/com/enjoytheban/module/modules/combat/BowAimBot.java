package com.enjoytheban.module.modules.combat;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.management.FriendManager;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.module.modules.player.Freecam;
import com.enjoytheban.utils.math.MathUtil;
import com.enjoytheban.utils.math.RotationUtil;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import optifine.MathUtils;

public class BowAimBot extends Module {

	public BowAimBot() {
		super("BowAimbot", new String[] { "bowaim", "baim", "baimbot" }, ModuleType.Combat);
		addValues(lockView);
		setColor(Color.ORANGE.getRGB());
	}

	private Option<Boolean> lockView = new Option("Lockview", "lockview", false);
	public static ArrayList<Entity> attackList = new ArrayList<>();
	public static ArrayList<Entity> targets = new ArrayList<>();
	public static int currentTarget;
	
	public boolean isValidTarget(final Entity entity) {
		boolean valid = false;
		if (entity == mc.thePlayer.ridingEntity)
			return false;

		if (entity.isInvisible())
			valid = true;

		if (FriendManager.isFriend(entity.getName()) && entity instanceof EntityPlayer || !mc.thePlayer.canEntityBeSeen(entity))
			return false;

		if (entity instanceof EntityPlayer) {
			valid = (entity != null && mc.thePlayer.getDistanceToEntity(entity) <= 50 && entity != mc.thePlayer && entity.isEntityAlive() && !FriendManager.isFriend(entity.getName()));
		} 
		return valid;
	}

	@EventHandler
	public void onPre(EventPreUpdate pre) {
		for (Object o : mc.theWorld.loadedEntityList) {
			Entity e = (Entity) o;
			if (((e instanceof EntityPlayer)) && (!targets.contains(e))) {
				targets.add(e);
			}
			if ((targets.contains(e)) && ((e instanceof EntityPlayer))) {
				targets.remove(e);
			}
		}
		if (currentTarget >= attackList.size())
			currentTarget = 0;

		for (Object o : mc.theWorld.loadedEntityList) {
			Entity e = (Entity) o;
			if ((isValidTarget(e)) && (!attackList.contains(e)))
				attackList.add(e);
			if (!isValidTarget(e) && attackList.contains(e))
				attackList.remove(e);
		}


		sortTargets();
		if (mc.thePlayer != null && attackList.size() != 0 && attackList.get(currentTarget) != null && isValidTarget(attackList.get(currentTarget)) && mc.thePlayer.isUsingItem() && mc.thePlayer.getCurrentEquippedItem().getItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) {
			int bowCurrentCharge = mc.thePlayer.getItemInUseDuration();
			float bowVelocity = (bowCurrentCharge / 20.0f);
			bowVelocity = (bowVelocity * bowVelocity + bowVelocity * 2.0f) / 3.0f;
			bowVelocity = MathHelper.clamp_float(bowVelocity, 0.0F, 1.0F);

			double v = bowVelocity * 3.0F;
			double g = 0.05000000074505806D;

			if (bowVelocity < 0.1)
				return;
		
			if (bowVelocity > 1.0f)
				bowVelocity = 1.0f;

			final double xDistance = attackList.get(currentTarget).posX - mc.thePlayer.posX + (attackList.get(currentTarget).posX - attackList.get(currentTarget).lastTickPosX) * ((float) (bowVelocity) * 10.0f);
			final double zDistance = attackList.get(currentTarget).posZ - mc.thePlayer.posZ + (attackList.get(currentTarget).posZ - attackList.get(currentTarget).lastTickPosZ) * ((float) (bowVelocity) * 10.0f);
			final double trajectoryXZ = Math.sqrt(xDistance * xDistance + zDistance * zDistance);
			final float trajectoryTheta90 = (float) (Math.atan2(zDistance, xDistance) * 180.0 / 3.141592653589793) - 90.0f;
			final float bowTrajectory = (float) ((float) -Math.toDegrees(getLaunchAngle((EntityLivingBase) attackList.get(currentTarget), v, g)) - 3.8);

				if (trajectoryTheta90 <= 360 && bowTrajectory <= 360) {
					
					if(lockView.getValue()){
						mc.thePlayer.rotationYaw = trajectoryTheta90;
						mc.thePlayer.rotationPitch = bowTrajectory;
					} else {
						pre.setYaw(trajectoryTheta90);
						pre.setPitch(bowTrajectory);
					}
				}
			
		}
	}

	public void sortTargets() {
			attackList.sort((ent1, ent2) -> {
				double d1 = mc.thePlayer.getDistanceToEntity(ent1);
				double d2 = mc.thePlayer.getDistanceToEntity(ent2);
				return (d1 < d2) ? -1 : (d1 == d2) ? 0 : 1;
			});
	}

	@Override
	public void onDisable() {
		super.onDisable();
		targets.clear();
		attackList.clear();
		currentTarget = 0;
	}

	private float getLaunchAngle(EntityLivingBase targetEntity, double v, double g) {
		double yDif = targetEntity.posY + targetEntity.getEyeHeight() / 2.0F - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
		double xDif = targetEntity.posX - mc.thePlayer.posX;
		double zDif = targetEntity.posZ - mc.thePlayer.posZ;

		double xCoord = Math.sqrt(xDif * xDif + zDif * zDif);

		return theta(v + 2, g, xCoord, yDif);
	}

	private float theta(double v, double g, double x, double y) {
		double yv = 2.0D * y * (v * v);
		double gx = g * (x * x);
		double g2 = g * (gx + yv);
		double insqrt = v * v * v * v - g2;
		double sqrt = Math.sqrt(insqrt);

		double numerator = v * v + sqrt;
		double numerator2 = v * v - sqrt;

		double atan1 = Math.atan2(numerator, g * x);
		double atan2 = Math.atan2(numerator2, g * x);

		return (float) Math.min(atan1, atan2);
	}
}