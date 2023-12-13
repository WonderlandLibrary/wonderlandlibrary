package slavikcodd3r.rainbow.command.commands;

import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.NetworkUtils;

@Com(names = { "info", "information", "debug" })
public class Info extends Command
{
	
	Minecraft mc = Minecraft.getMinecraft();
	
    @Override
    public String getHelp() {
        return "info";
    }
    
    @Override
    public void runCommand(final String[] args) {
        String player = "";
        if (args.length > 1) {
            player = args[1];
        }
        if (args.length <= 1) {
        	player = mc.thePlayer.getName().toString();
        }
		for (final Object theObject : Minecraft.theWorld.loadedEntityList) {
            if (theObject instanceof EntityPlayer) {
                final EntityPlayer e = (EntityPlayer)theObject;
            if (e.getName().equalsIgnoreCase(player)) {
            	ClientUtils.sendMessage("Name: " + e.getName());
            	ClientUtils.sendMessage("XYZ: " + e.posX + " " + e.posY + " " + e.posZ);
            	ClientUtils.sendMessage("Server XYZ: " + e.serverPosX + " " + e.serverPosY + " " + e.serverPosZ);
            	ClientUtils.sendMessage("Prev XYZ: " + e.prevPosX + " " + e.prevPosY + " " + e.prevPosZ);
            	ClientUtils.sendMessage("Chunk XYZ: " + e.chunkCoordX + " " + e.chunkCoordY + " " + e.chunkCoordZ);
            	ClientUtils.sendMessage("Motion XYZ: " + e.motionX + " " + e.motionY + " " + e.motionZ);
            	ClientUtils.sendMessage("Health: " + e.getHealth());
            	ClientUtils.sendMessage("Max Health: " + e.getMaxHealth());
            	ClientUtils.sendMessage("Food: " + e.getFoodStats().getFoodLevel());
            	ClientUtils.sendMessage("Potions: " + e.getActivePotionEffects());
            	ClientUtils.sendMessage("Ping: " + NetworkUtils.getPlayerPing(e.toString()) + " ms");
            	ClientUtils.sendMessage("UUID: " + e.getGameProfile().getId());
            	ClientUtils.sendMessage("Entity ID" + e.getEntityId());
            	ClientUtils.sendMessage("Hash Code: " + e.hashCode());
            	ClientUtils.sendMessage("Yaw Pitch: " + e.rotationYaw + " " + e.rotationPitch);
            	ClientUtils.sendMessage("Eye Height: " + e.getEyeHeight());
            	ClientUtils.sendMessage("Ticks Existed: " + e.ticksExisted);
            	ClientUtils.sendMessage("Dimension: " + e.dimension);
            	ClientUtils.sendMessage("Hurttime: " + e.hurtTime);
            	ClientUtils.sendMessage("EXP: " + e.experience);
            	ClientUtils.sendMessage("EXP Level: " + e.experienceLevel);
            	ClientUtils.sendMessage("EXP Total: " + e.experienceTotal);
               	ClientUtils.sendMessage("XP Cooldown: " + e.xpCooldown);
            	ClientUtils.sendMessage("Fall Distance: " + e.fallDistance);
            	ClientUtils.sendMessage("Max Fall Height: " + e.getMaxFallHeight());
            	ClientUtils.sendMessage("Speed In Air: " + e.speedInAir);
            	ClientUtils.sendMessage("Jump Ticks: " + e.jumpTicks);
            	ClientUtils.sendMessage("Jump Movement Factor: " + e.jumpMovementFactor);
            	ClientUtils.sendMessage("Step Height: " + e.stepHeight);
            	ClientUtils.sendMessage("Time Until Portal: " + e.timeUntilPortal);
            	ClientUtils.sendMessage("Fire Resistance: " + e.fireResistance);
            	ClientUtils.sendMessage("Total Armor Value: " + e.getTotalArmorValue());
            	ClientUtils.sendMessage("Armor: Visibility: " + e.getArmorVisibility());
            	ClientUtils.sendMessage("Arrow Count: " + e.getArrowCountInEntity());
            	ClientUtils.sendMessage("Custom NameTag: " + e.getCustomNameTag().toString());
            	ClientUtils.sendMessage("Last Attacker Time: " + e.getLastAttackerTime());
            	ClientUtils.sendMessage("Limb Swing: " + e.limbSwing);
            	ClientUtils.sendMessage("Limb Swing Amount: " + e.limbSwingAmount);
            	ClientUtils.sendMessage("Bounding Box: " + e.boundingBox);
            	ClientUtils.sendMessage("Y Offset: " + e.getYOffset());
            	ClientUtils.sendMessage("Mounted Y Offset: " + e.getMountedYOffset());
            	ClientUtils.sendMessage("Revenge Timer: " + e.getRevengeTimer());
            	ClientUtils.sendMessage("Air: " + e.getAir());
            	ClientUtils.sendMessage("AI Move Speed: " + e.getAIMoveSpeed());
            	ClientUtils.sendMessage("AI Target: " + e.getAITarget());
            	ClientUtils.sendMessage("Arrow Hit Timer: " + e.arrowHitTimer);
            	ClientUtils.sendMessage("Aura Ticks: " + e.auraTicks);
            	ClientUtils.sendMessage("Fly Speed: " + e.capabilities.getFlySpeed());
            	ClientUtils.sendMessage("Walk Speed: " + e.capabilities.getWalkSpeed());
            	ClientUtils.sendMessage("Age: " + e.getAge());
            	ClientUtils.sendMessage("Distance Walked Modified: " + e.distanceWalkedModified);
            	ClientUtils.sendMessage("Distance Walked On Step Modified: " + e.distanceWalkedOnStepModified);
            	ClientUtils.sendMessage("Move Forward: " + e.moveForward);
            	ClientUtils.sendMessage("Move Strafing: " + e.moveStrafing);
            	ClientUtils.sendMessage("Width: " + e.width);
            	ClientUtils.sendMessage("Teleport Direction: " + e.getTeleportDirection());
            	ClientUtils.sendMessage("Bed Orientation In Degrees: " + e.getBedOrientationInDegrees());
            	ClientUtils.sendMessage("Collision Border Size: " + e.getCollisionBorderSize());
            	ClientUtils.sendMessage("Item In Use Count: " + e.getItemInUseCount());
            	ClientUtils.sendMessage("Item In Use Duration: " + e.getItemInUseDuration());
            	ClientUtils.sendMessage("Score: " + e.getScore());
            	ClientUtils.sendMessage("World Time: " + e.worldObj.getWorldTime());
            	ClientUtils.sendMessage("Parts: " + e.getParts());
            	if (e.isEntityAlive()) {
            		ClientUtils.sendMessage("Alive: true");
            	}
            	if (!e.isEntityAlive()) {
            		ClientUtils.sendMessage("Alive: false");
            	}
            	if (e.isDead) {
            		ClientUtils.sendMessage("Dead: true");
            	}
            	if (!e.isDead) {
            		ClientUtils.sendMessage("Dead: false");
            	}
            	if (e.isSprinting()) {
            		ClientUtils.sendMessage("Sprinting: true");
            	}
            	if (!e.isSprinting()) {
            		ClientUtils.sendMessage("Sprinting: false");
            	}
            	if (e.isSneaking()) {
            		ClientUtils.sendMessage("Sneaking: true");
            	}
            	if (!e.isSneaking()) {
            		ClientUtils.sendMessage("Sneaking: false");
            	}
            	if (e.isBlocking()) {
            		ClientUtils.sendMessage("Blocking: true");
            	}
            	if (!e.isBlocking()) {
            		ClientUtils.sendMessage("Blocking: false");
            	}
            	if (e.isBurning()) {
            		ClientUtils.sendMessage("Burning: true");
            	}
            	if (!e.isBurning()) {
            		ClientUtils.sendMessage("Burning: false");
            	}
            	if (e.isEating()) {
            		ClientUtils.sendMessage("Eating: true");
            	}
            	if (!e.isEating()) {
            		ClientUtils.sendMessage("Eating: false");
            	}
            	if (e.onGround) {
            		ClientUtils.sendMessage("On Ground: true");
            	}
            	if (!e.onGround) {
            		ClientUtils.sendMessage("On Ground: false");
            	}
            	if (e.isAirBorne) {
            		ClientUtils.sendMessage("Air Borne: true");
            	}
            	if (!e.isAirBorne) {
            		ClientUtils.sendMessage("Air Borne: false");
            	}
            	if (e.isPlayerSleeping()) {
            		ClientUtils.sendMessage("Sleeping: true");
            	}
            	if (!e.isPlayerSleeping()) {
            		ClientUtils.sendMessage("Sleeping: false");
            	}
            	if (e.isPlayerFullyAsleep()) {
            		ClientUtils.sendMessage("Full Sleep: true");
            	}
            	if (!e.isPlayerFullyAsleep()) {
            		ClientUtils.sendMessage("Full Sleep: false");
            	}
            	if (e.isInvisible()) {
            		ClientUtils.sendMessage("Invisible: true");
            	}
            	if (!e.isInvisible()) {
            		ClientUtils.sendMessage("Invisible: false");
            	}
            	if (e.isInWater()) {
            		ClientUtils.sendMessage("In Water: true");
            	}
            	if (!e.isInWater()) {
            		ClientUtils.sendMessage("In Water: false");
            	}
            	if (e.isOnLadder()) {
            		ClientUtils.sendMessage("On Ladder: true");
            	}
            	if (!e.isOnLadder()) {
            		ClientUtils.sendMessage("On Ladder: false");
            	}
            	if (e.isOutsideBorder()) {
            		ClientUtils.sendMessage("Outside Border: true");
            	}
            	if (!e.isOutsideBorder()) {
            		ClientUtils.sendMessage("Outside Border: false");
            	}
            	if (e.isRiding()) {
            		ClientUtils.sendMessage("Riding: true");
            	}
            	if (!e.isRiding()) {
            		ClientUtils.sendMessage("Riding: false");
            	}
            	if (e.isChild()) {
            		ClientUtils.sendMessage("Child: true");
            	}
            	if (!e.isChild()) {
            		ClientUtils.sendMessage("Child: false");
            	}
            	if (e.isSlient()) {
            		ClientUtils.sendMessage("Silent: true");
            	}
            	if (!e.isSlient()) {
            		ClientUtils.sendMessage("Silent: false");
            	}
            	if (e.isSpawnForced()) {
            		ClientUtils.sendMessage("Spawn Forced: true");
            	}
            	if (!e.isSpawnForced()) {
            		ClientUtils.sendMessage("Spawn Forced: false");
            	}
            	if (e.isUsingItem()) {
            		ClientUtils.sendMessage("Using Item: true");
            	}
            	if (!e.isUsingItem()) {
            		ClientUtils.sendMessage("Using Item: false");
            	}
            	if (e.isWet()) {
            		ClientUtils.sendMessage("Wet: true");
            	}
            	if (!e.isWet()) {
            		ClientUtils.sendMessage("Wet: false");
            	}
            	if (e.capabilities.isCreativeMode) {
            		ClientUtils.sendMessage("Creative: true");
            	}
            	if (!e.capabilities.isCreativeMode) {
            		ClientUtils.sendMessage("Creative: false");
            	}
            	if (e.capabilities.isFlying) {
            		ClientUtils.sendMessage("Flying: true");
            	}
            	if (!e.capabilities.isFlying) {
            		ClientUtils.sendMessage("Flying: false");
            	}
            	if (e.noClip) {
            		ClientUtils.sendMessage("NoClip: true");
            	}
            	if (!e.noClip) {
            		ClientUtils.sendMessage("NoClip: false");
            	}
            	if (e.isSwingInProgress) {
            		ClientUtils.sendMessage("Swing: true");
            	}
            	if (!e.isSwingInProgress) {
            		ClientUtils.sendMessage("Swing: false");
            	}
            	if (e.shouldHeal()) {
            		ClientUtils.sendMessage("Should Heal: true");
            	}
            	if (!e.shouldHeal()) {
            		ClientUtils.sendMessage("Should Heal: false");
            	}
            	if (e.isCollided) {
            		ClientUtils.sendMessage("Collided: true");
            	}
            	if (!e.isCollided) {
            		ClientUtils.sendMessage("Collided: false");
            	}
            	if (e.isCollidedHorizontally) {
            		ClientUtils.sendMessage("Collided Horizontally: true");
            	}
            	if (!e.isCollidedHorizontally) {
            		ClientUtils.sendMessage("Collided Horizontally: false");
            	}
            	if (e.isCollidedVertically) {
            		ClientUtils.sendMessage("Collided Vertically: true");
            	}
            	if (!e.isCollidedVertically) {
            		ClientUtils.sendMessage("Collided Vertically: false");
            	}
            	if (e.canAttackWithItem()) {
            		ClientUtils.sendMessage("Can Attack With Item: true");
            	}
            	if (!e.canAttackWithItem()) {
            		ClientUtils.sendMessage("Can Attack With Item: false");
            	}
            	if (e.canBeCollidedWith()) {
            		ClientUtils.sendMessage("Can Be Collided: true");
            	}
            	if (!e.canBeCollidedWith()) {
            		ClientUtils.sendMessage("Can Be Collided: false");
            	}
            	if (e.canBePushed()) {
            		ClientUtils.sendMessage("Can Be Pushed: true");
            	}
            	if (!e.canBePushed()) {
            		ClientUtils.sendMessage("Can Be Pushed: false");
            	}
            	if (e.canBreatheUnderwater()) {
            		ClientUtils.sendMessage("Can Breathe Underwater: true");
            	}
            	if (!e.canBreatheUnderwater()) {
            		ClientUtils.sendMessage("Can Breathe Underwater: false");
            	}
            	if (e.canRenderOnFire()) {
            		ClientUtils.sendMessage("Can Render On Fire: true");
            	}
            	if (!e.canRenderOnFire()) {
            		ClientUtils.sendMessage("Can Render On Fire: false");
            	}
            }
        }
    }
    }
}
