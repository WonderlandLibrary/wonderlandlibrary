package markgg.modules.impl.combat;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.event.impl.PacketEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.BooleanSetting;
import markgg.settings.NumberSetting;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

@ModuleInfo(name = "BowAimbot", category = Module.Category.COMBAT)
public class BowAimbot extends Module {

	public NumberSetting range = new NumberSetting("Range", this, 5, 5, 150, 1);
	public BooleanSetting smoothRotate = new BooleanSetting("Smooth Rotate", this, true);
	public BooleanSetting aimAtMobs = new BooleanSetting("Aim at Mobs", this, false);
	public int state;

	@EventHandler
	public final Listener<MotionEvent> eventListener = e -> {
		if(e.getType() == MotionEvent.Type.PRE){
			List<EntityLivingBase> targets = (List<EntityLivingBase>) mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
			targets = targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) < range.getValue() && entity != mc.thePlayer  && !entity.isInvisible()).collect(Collectors.toList());
			targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase)entity).getDistanceToEntity(mc.thePlayer)));

			if(!aimAtMobs.getValue())
				targets = targets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
			else
				//not filtered

				if(!targets.isEmpty()) {
					EntityLivingBase target = targets.get(0);

					if (!(mc.currentScreen instanceof GuiChest) || !(mc.currentScreen instanceof GuiInventory)) {
						ItemStack heldItem = mc.thePlayer.getHeldItem();
						if (heldItem != null && heldItem.getItem() instanceof ItemBow && mc.thePlayer.isUsingItem()) {
							mc.thePlayer.rotationYaw = (getRotations(target)[0]);
							mc.thePlayer.rotationPitch = (getRotations(target)[1]);
						}
					}

				}
		}
	};

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

		float adjustedRotationSpeed = (float) (400 - Math.random() * 10 + Math.random() * 10);
		deltaYaw = Math.min(adjustedRotationSpeed, Math.max(-adjustedRotationSpeed, deltaYaw));
		deltaPitch = Math.min(adjustedRotationSpeed, Math.max(-adjustedRotationSpeed, deltaPitch));
		yaw = mc.thePlayer.rotationYaw + deltaYaw;
		pitch = mc.thePlayer.rotationPitch + deltaPitch;

		int chance = 4;
		int random = (int) (Math.random() * 100);
		Math.round(random);

		if(chance > random)
			pitch = pitch + (float) (Math.random() - Math.random()) * 7F;

		Math.round(random);

		if(chance > random)
			pitch = pitch + (float) (Math.random() * 7F - Math.random());

		if(smoothRotate.getValue()) {
			adjustedRotationSpeed =- 370;
			adjustedRotationSpeed = adjustedRotationSpeed * 1.3F;
			adjustedRotationSpeed =- 370;
		}

		return new float[] {yaw, pitch };
	}

}
