package markgg.modules.impl.ghost;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.input.Mouse;

import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.BooleanSetting;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;

@ModuleInfo(name = "AimAssist", category = Module.Category.GHOST)
public class AimAssist extends Module {

	public BooleanSetting aimAtMobs = new BooleanSetting("Aim at Mobs", this, false);
	public BooleanSetting clickAim = new BooleanSetting("Click to Aim", this, false);


	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		if (e.getType() == MotionEvent.Type.PRE) {
			List<EntityLivingBase> targets = (List<EntityLivingBase>) mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
			targets = targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) < 5 && entity != mc.thePlayer && !entity.isDead && entity.getHealth() > 0 && !entity.isInvisible() && !entity.getName().contains("[NCP]") && !entity.getName().contains(" ") && !(entity.getName().length() > 16) && !(entity.getName().length() < 3) && !entity.getName().contains("npc") && !entity.getName().contains("NPC") && !entity.getName().contains("cit") && !entity.getName().contains("CIT")).collect(Collectors.toList());
			targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase) entity).getDistanceToEntity(mc.thePlayer)));

			if (!aimAtMobs.getValue())
				targets = targets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());

			if (!targets.isEmpty()) {
				EntityLivingBase target = targets.get(0);
				if (!(mc.currentScreen instanceof GuiChest) || !(mc.currentScreen instanceof GuiInventory)) {
					ItemStack heldItem = mc.thePlayer.getHeldItem();

					if (heldItem != null && heldItem.getItem() instanceof ItemSword) {
						if (clickAim.getValue()) {
							if (Mouse.isButtonDown(0)) {
								mc.thePlayer.rotationYaw = (getRotations(target)[0]);
								mc.thePlayer.rotationPitch = (getRotations(target)[1]);
							}
						} else if (!clickAim.getValue()) {
							mc.thePlayer.rotationYaw = (getRotations(target)[0]);
							mc.thePlayer.rotationPitch = (getRotations(target)[1]);
						}
					}
				}
			}
		}
	};

	public float cameraShakeSpeed;
	public float rotationSpeed;

	public float[] getRotations(Entity e) {
		double deltaX = e.posX - (e.posX - e.lastTickPosX) - mc.thePlayer.posX;
		double deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight() + 0.2;
		double deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - mc.thePlayer.posZ;
		double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2));

		float yaw = (float) Math.toDegrees(-Math.atan2(deltaX, deltaZ));
		float pitch = (float) -Math.toDegrees(Math.atan2(deltaY, Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2))));
		float deltaYaw = yaw - mc.thePlayer.rotationYaw;
		float deltaPitch = pitch - mc.thePlayer.rotationPitch;
		deltaYaw = MathHelper.wrapAngleTo180_float(deltaYaw);
		deltaPitch = MathHelper.wrapAngleTo180_float(deltaPitch);

		float adjustedRotationSpeed = rotationSpeed;
		deltaYaw = Math.min(adjustedRotationSpeed, Math.max(-adjustedRotationSpeed, deltaYaw));
		deltaPitch = Math.min(adjustedRotationSpeed, Math.max(-adjustedRotationSpeed, deltaPitch));
		yaw = mc.thePlayer.rotationYaw + deltaYaw + this.cameraShakeSpeed;
		pitch = mc.thePlayer.rotationPitch + deltaPitch + this.cameraShakeSpeed;

		return new float[] { yaw, pitch };
	}
}
