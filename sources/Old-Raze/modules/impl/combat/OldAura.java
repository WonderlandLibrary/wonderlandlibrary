package markgg.modules.impl.combat;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import markgg.RazeClient;
import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.modules.impl.render.HUD2;
import markgg.settings.BooleanSetting;
import markgg.settings.ModeSetting;
import markgg.settings.NumberSetting;
import markgg.util.MathUtil;
import markgg.util.timer.Timer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.world.World;

@ModuleInfo(name = "OldAura", category = Module.Category.COMBAT)
public class OldAura extends Module{

	public Timer timer = new Timer();
	public static List<EntityLivingBase> targets;
	public static EntityLivingBase target;
	public ModeSetting blockMode = new ModeSetting("Block Mode", this, "Legit", "Legit", "Spam", "AAC");
	public NumberSetting minCps = new NumberSetting("Min CPS", this, 10, 1, 20, 1);
	public NumberSetting maxCps = new NumberSetting("Max CPS", this, 10, 1, 20, 1);
	public NumberSetting range = new NumberSetting("Range", this, 4, 1, 6, 0.1);
	public BooleanSetting noSwing = new BooleanSetting("No Swing", this, false);
	public BooleanSetting autoBlock = new BooleanSetting("Auto Block", this, true);
	public BooleanSetting silentRotate = new BooleanSetting("Silent Rotate", this, true);
	public BooleanSetting smoothRotate = new BooleanSetting("Smooth Rotate", this, true);
	public BooleanSetting attackMobs = new BooleanSetting("Attack Mobs", this, false);
	public BooleanSetting wallHit = new BooleanSetting("Through Walls", this, false);

	@Override
	public void onEnable() {
		target = null;
	}

	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		if(e.getType() == MotionEvent.Type.PRE){
			MotionEvent event = (MotionEvent)e;

			List<EntityLivingBase> targets = (List<EntityLivingBase>) mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
			targets = targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) < range.getValue() && entity != mc.thePlayer  && !entity.isInvisible()).collect(Collectors.toList());
			targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase)entity).getDistanceToEntity(mc.thePlayer)));

			if(!attackMobs.getValue())
				targets = targets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
			else
				//not filtered

				if(!targets.isEmpty()) {
					this.target = targets.get(0);

					mc.thePlayer.rotationYawHead = getRotations(target)[0];
					mc.thePlayer.renderYawOffset = getRotations(target)[1];
					
					if(silentRotate.getValue()) {
						event.setYaw(getRotations(target)[0]);
						event.setPitch(getRotations(target)[1]);
					}else {
						if(!target.isDead) {
							mc.thePlayer.rotationYaw = getRotations(target)[0];
							mc.thePlayer.rotationPitch = getRotations(target)[1];
						}
					}

					int cps = (int) MathUtil.randomNumber(minCps.getValue(), maxCps.getValue());
					Math.round(cps);

					if(timer.hasTimeElapsed((long) (1000 / cps), true)) {
						if(noSwing.getValue()) {
				            mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
				        }else {
				            if(!wallHit.getValue() && mc.thePlayer.canEntityBeSeen(target)) {
				                mc.thePlayer.swingItem();
								mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
				            }
				            else if(wallHit.getValue()) {
				                mc.thePlayer.swingItem();
								mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
				            }
				        }
						
						if(autoBlock.getValue()) {
							switch(blockMode.getMode()) {
							case "Legit":
								if(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
									if(!wallHit.getValue() && mc.thePlayer.canEntityBeSeen(target))
										mc.playerController.sendUseItem((EntityPlayer)mc.thePlayer, (World)mc.theWorld,mc.thePlayer.inventory.getCurrentItem());
									else if(wallHit.getValue())
										mc.playerController.sendUseItem((EntityPlayer)mc.thePlayer, (World)mc.theWorld,mc.thePlayer.inventory.getCurrentItem());
								}
								break;
							case "Spam":
								if(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
									if(!wallHit.getValue() && mc.thePlayer.canEntityBeSeen(target))
										mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), 32767);
									else if(wallHit.getValue())
										mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), 32767);
								}
								break;
							case "AAC":
								if(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword && mc.thePlayer.ticksExisted % 2 == 0) {
									if(!wallHit.getValue() && mc.thePlayer.canEntityBeSeen(target)) {
										mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
										mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
									}else if(wallHit.getValue()) {
										mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
										mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
									}
								}
								break;
							}
						}

						if(target.hurtTime < 8) {
							if(wallHit.getValue())
								mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
							else if(mc.thePlayer.canEntityBeSeen(target))
									mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
						}
					}

				}else{
					target = null;
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

	private Entity getClosestEntity() {
		Entity closestEntity = null;
		double closestDistanceSq = Double.POSITIVE_INFINITY;
		EntityPlayerSP player = mc.thePlayer;

		for (Object obj : mc.theWorld.loadedEntityList) {
			if (obj instanceof Entity && obj != player) {
				Entity entity = (Entity) obj;
				double distanceSq = entity.getDistanceSq(player.posX, player.posY, player.posZ);
				if (distanceSq < closestDistanceSq) {
					closestEntity = entity;
					closestDistanceSq = distanceSq;
				}
			}
		}

		return closestEntity;
	}

	@Override
	public void onDisable() {
		mc.gameSettings.keyBindUseItem.pressed = false;
		target = null;
	}

}
