package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.CheckBtnModLinker;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.CheckBtnSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;

public class KillAura extends Module {
	
	public static WaitTimer timer = new WaitTimer();
	public static WaitTimer readyToAttackTimer = new WaitTimer();
	private int tickTimer;
	
	public static EntityLivingBase preEn = null;
	public static EntityLivingBase en = null;
	
	private boolean doAttack = false;
	
	private boolean readyToAttack = false;
	
	private int rotationStage = 0;
	
	RayTraceResult rayTrace = null;
	
	Vec3d src;
	
	Vec3d dest;

	public KillAura() {
		super("KillAura", Keyboard.KEY_R, Category.COMBAT, "Automatically attacks entities in range.");
	}
	
	public float[] getNextPos(Vec3d vec) {
		
		float rotationAmount = ClientSettings.killauraSmoothRotsMaxRotation;
		
		float offset = (rand.nextFloat() - 0.5f) * 10f;
		
		if(rand.nextFloat() > 0.20f) {
			offset = 0;
		}
		
		double diffX = vec.x - mc.player.posX;
		double diffY = vec.y - (mc.player.posY + mc.player.getEyeHeight());
		double diffZ = vec.z - mc.player.posZ;
		
		double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
		
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		
		float wrapYaw = MathHelper.wrapDegrees(yaw - mc.player.lastReportedYaw);
		
		float wrapPitch = MathHelper.wrapDegrees(pitch - mc.player.lastReportedPitch);
		
		if(Math.abs(wrapYaw) < 10 && Math.abs(wrapPitch) < 10) {
			setReadyToAttack();
			return new float[]{mc.player.lastReportedYaw + offset, mc.player.lastReportedPitch + offset};
		}
		else if(Math.abs(wrapYaw) < 25 && Math.abs(wrapPitch) < 25) {
			setReadyToAttack();
			return new float[]{mc.player.lastReportedYaw + wrapYaw + offset, mc.player.lastReportedPitch + wrapPitch + offset};
		}
		else if(Math.abs(wrapYaw) < ClientSettings.killauraSmoothRotsMaxRotation && Math.abs(wrapPitch) < ClientSettings.killauraSmoothRotsMaxRotation) {
			setReadyToAttack();
			return new float[]{mc.player.lastReportedYaw + wrapYaw + offset, mc.player.lastReportedPitch + wrapPitch + offset};
		}
		
		float[] rots = new float[2];
		
		if(wrapYaw > 0) {
			rots[0] = mc.player.lastReportedYaw + Math.min(rotationAmount, wrapYaw);
		}
		else {
			rots[0] = mc.player.lastReportedYaw + Math.max(-rotationAmount, wrapYaw);
		}
		
		if(wrapPitch > 0) {
			rots[1] = mc.player.lastReportedPitch + Math.min(rotationAmount, wrapPitch);
		}
		else {
			rots[1] = mc.player.lastReportedPitch + Math.max(-rotationAmount, wrapPitch);
		}
		
		readyToAttack = false;
		
		return rots;
	}
	
	private void setReadyToAttack() {
		if(!readyToAttack) {
			readyToAttackTimer.reset();
		}
		readyToAttack = true;
	}
	
	private boolean isReadyToAttack(boolean onlyTimers) {
		if(onlyTimers) {
			return (ClientSettings.useCooldown ? mc.player.getCooledAttackStrength(-0.5f) >= 1f : 
				readyToAttackTimer.hasTimeElapsed(ClientSettings.killauraAttackDelay, false));
		}
		else {
			return (ClientSettings.useCooldown ? readyToAttack && mc.player.getCooledAttackStrength(-0.5f) >= 1f : 
				readyToAttack && readyToAttackTimer.hasTimeElapsed(ClientSettings.killauraAttackDelay, false));
		}
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		
		if(ClientSettings.disableKillauraInInventory && mc.currentScreen instanceof GuiContainer) {
			return;
		}
		
		preEn = en;
		
		double adjustedRange = ClientSettings.KillauraRange;
		
		float searchRange = (float) Math.max(adjustedRange, ClientSettings.Blockrange);
		
		if(isMode("Single")) {
			en = Utils.getClosestEntity(searchRange);
		}
		
		if(en == null) {
			AuraUtils.targets.clear();
			return;
		}
		
		AuraUtils.targets.clear();
		AuraUtils.targets.add(en);
		
		if(preEn != en) {
			readyToAttack = false;
		}
		
		Vec3d enPos;
		
		enPos = new Vec3d(en.posX, en.posY, en.posZ);
		
		double distance = mc.player.getDistance(enPos.x, enPos.y, enPos.z);
		
		if(isMode("Single")) {
			
			if(distance > adjustedRange) {
				AuraUtils.targets.clear();
				return;
			}
			
			float[] rots = getFacePos(en);
			
			event.yaw = rots[0];
			event.pitch = rots[1];
			
			if(isReadyToAttack(false) && timer.hasTimeElapsed(1000 / ClientSettings.KillauraAPS, true)) {
				doAttack = true;
			}
			
		}
		
		super.onUpdate(event);
	}
	
	@Override
	public void onLateUpdate() {
		if(!doAttack) {
			return;
		}
		doAttack = false;
		
		if(isMode("Single")) {
			AutoBlock.stopBlock();
			attack(en);
			AutoBlock.startBlock();
		}
		
		super.onLateUpdate();
	}
	
	private boolean isTickTimerReady(int tickTimer) {
		return tickTimer % 2 == 1 || tickTimer == 6;
	}
	
	public void attack(EntityLivingBase entity) {
		attack(entity, true, Jigsaw.getModuleByName("Criticals").isToggled(), ClientSettings.sprintCrits, ClientSettings.killauraUseRaytrace);
	}
	
	public void attack(EntityLivingBase entity, boolean useHitRatio, boolean crit, boolean sprintCrits, boolean useRayTrace) {
		
		if(useRayTrace) {

			Vec3d lookVec = mc.player.getVectorForRotation(mc.player.lastReportedPitch, mc.player.lastReportedYaw);
			Vec3d lookVecWithRange = new Vec3d(lookVec.x * ClientSettings.KillauraRange, lookVec.y * ClientSettings.KillauraRange, lookVec.z * ClientSettings.KillauraRange);
			
			src = Utils.getPlayerVec3dEyeHeight();
			dest = lookVecWithRange.add(Utils.getPlayerVec3dEyeHeight());
			
			rayTrace = Utils.rayTraceBlocksAndEntities(10, src, dest);

			if(rayTrace == null) {
				return;
			}
			if(rayTrace.typeOfHit == Type.MISS) {
				return;
			}
			if(rayTrace.typeOfHit == Type.BLOCK) {
				return;
			}
			if(rayTrace.typeOfHit == Type.ENTITY) {
				if(!mc.player.canEntityBeSeen(rayTrace.entityHit)) {
					return;
				}
				if(rayTrace.entityHit instanceof EntityLivingBase) {
					en = (EntityLivingBase) rayTrace.entityHit;
				}
				else {
					return;
				}
			}
		}
		
		boolean sprinting = mc.player.isSprinting();
		
		if((useHitRatio && rand.nextDouble() <= ClientSettings.killauraHitRatio) || !useHitRatio) {
			
			if(sprintCrits) {
				if(sprinting) {
					sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
				}
			}
			
			boolean critted = false;

			Criticals.disable = true;
			if(crit) {
				if(en.hurtTime < 6) {
					Criticals.critical(en);
					critted = true;
				}
			}
			
			sendPacket(new CPacketUseEntity(en));
			Criticals.disable = false;
			
			mc.player.swingArm(EnumHand.MAIN_HAND);
			if(ClientSettings.useCooldown) {
				mc.player.resetCooldown();
			}
			
			if(sprintCrits) {
				if(sprinting) {
					sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
				}
			}
			
			Utils.createHitParticles(critted, en);
			
		}
		else {
			mc.player.swingArm(EnumHand.MAIN_HAND);
			mc.player.resetCooldown();
		}
		
	}
	
	public void swap(final int slot, final int hotbarNum) {
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, hotbarNum, ClickType.SWAP, mc.player);
    }
	
	@Override
	public ModSetting[] getModSettings() {
		
		SliderSetting killaruarangeslider = new SliderSetting("Hit Range", "KillauraRange", 3.0, 10.0, 0.0, ValueFormat.DECIMAL);
		SliderSetting killaruaspeedslider = new SliderSetting("Hits Per Second", "KillauraAPS", 1.0, 20.0, 0.0, ValueFormat.DECIMAL);
		
		return new ModSetting[] { 
				new CheckBtnModLinker("AutoBlock"),
				new SliderSetting("AutoBlock Range", "Blockrange", 3.0, 10, 0.0, ValueFormat.DECIMAL), 
				new CheckBtnSetting("Use Cooldown", "useCooldown"),
				new CheckBtnModLinker("Criticals"),
				new CheckBtnModLinker("NoSwing"),
				new CheckBtnSetting("Disable On Death", "disableKillauraOnDeath"),
				new CheckBtnSetting("Disable In Inventory", "disableKillauraInInventory"),
				new CheckBtnSetting("Enable Sprint-Crits", "sprintCrits"),
				new CheckBtnSetting("Use Raytracing", "killauraUseRaytrace"),
				killaruarangeslider, killaruaspeedslider, 
				new SliderSetting("Max Rotation/Tick", "killauraSmoothRotsMaxRotation", 25, 180, 0.0, ValueFormat.DEGREES), 
				new SliderSetting("Attack Delay", "killauraAttackDelay", 0, 200, ValueFormat.MS), 
				new SliderSetting("Hit Chance", "killauraHitRatio", 0.0, 1.0, 0.0, ValueFormat.PERCENT), 
				};
	}
	
	@Override
	public void onDeath() {
		if(ClientSettings.disableKillauraOnDeath) {
			this.setToggled(false, true);
		}
		super.onDeath();
	}
	
	@Override
	public void onToggle() {
		en = null;
		doAttack = false;
		AuraUtils.targets.clear();
		tickTimer = 10;
		readyToAttack = false;
		super.onToggle();
	}
	
	@Override
	public void onRender() {
		
		super.onRender();
	}
	
	@Override
	public String[] getModes() {
		return new String[] { "Single" };
	}
	
	@Override
	public String getAddonText() {
		String text = super.getAddonText();
		if(ClientSettings.sprintCrits) {
			text += ", SprintCrits";
		}
//		if(ClientSettings.killauraSmoothRotations) {
//			text += ", SmoothRots";
//		}
		text += ", Cooldown: " + (ClientSettings.useCooldown ? "§aTrue§r" : "§cFalse§r");
		return text;
	}
	
	public static boolean getShouldChangePackets() {
		return en != null;
	}
	
	public static boolean doBlock() {
		return (en != null && mc.player.getDistanceToEntity(en) < ClientSettings.Blockrange);
	}
	
	public float[] getFacePos(EntityLivingBase en) {
//		return ClientSettings.killauraSmoothRotations ? getNextPos(new Vec3(en.posX, en.posY + (en.getEyeHeight() - en.height / 1.5), en.posZ)) : Utils.getFacePosEntity(en);
		return getNextPos(new Vec3d(en.posX, en.posY + (en.getEyeHeight() - en.height / 3), en.posZ));
	}

}
