package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;
import java.util.Comparator;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

public class KillAura extends Module {
	public static WaitTimer timer = new WaitTimer();
	public static EntityLivingBase en = null;
	static ArrayList<EntityLivingBase> ens = new ArrayList<EntityLivingBase>();
	boolean attack = false;
	/**
	 * For "Switch" mode
	 */
	private ArrayList<EntityLivingBase> lastEns = new ArrayList<EntityLivingBase>();
	public static boolean walls = true;
	
	private float[] preRot;

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public ModSetting[] getModSettings() {
		
		SliderSetting killaruarangeslider = new SliderSetting("Hit Range", "KillauraRange", 3.5, 10.0, 0.0, ValueFormat.DECIMAL);
		SliderSetting killaruaspeedslider = new SliderSetting("Hits Per Second", "KillauraAPS", 1.0, 20.0, 0.0, ValueFormat.DECIMAL);
		
		return new ModSetting[] { killaruarangeslider, killaruaspeedslider, 
				new SliderSetting("Hit Chance", "killauraHitRatio", 0.0, 1.0, 0.0, ValueFormat.PERCENT)
				};
	}

	public KillAura() {
		super("KillAura", Keyboard.KEY_R, Category.COMBAT, "Automatically attacks entities in range.");
	}

	@Override
	public void onToggle() {
		en = null;
		lastEns.clear();
		ens.clear();
		AuraUtils.targets.clear();
		preRot = null;
		super.onToggle();
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		attack = false;
		if(en == null) {
			preRot = new float[]{event.yaw, event.pitch};
		}
		if (en != null && !event.autopot && mc.thePlayer.getDistanceToEntity(en) < ClientSettings.KillauraRange) {
			float[] rots = Utils.getFacePosEntity(en);
			if ((this.currentMode.equals("Single") || this.currentMode.equals("Switch"))) {
				event.yaw = rots[0];
				event.pitch = rots[1];
			}
			if(this.currentMode.equals("Silent")) {
				if(Math.abs(preRot[0] - rots[0]) > 50
						|| Math.abs(preRot[1] - rots[1]) > 50) {
					if(rots[0] > preRot[0]) {
						event.yaw = (float) (preRot[0] + 40 + Math.pow((rand.nextFloat() - 0.5f) * 6f, 2));
					}
					if(rots[0] < preRot[0]) {
						event.yaw = (float) (preRot[0] - 40 + Math.pow((rand.nextFloat() - 0.5f) * 6f, 2));
					}
					if(rots[1] > preRot[1]) {
						event.pitch = (float) (preRot[1] + 40 + Math.pow((rand.nextFloat() - 0.5f) * 6f, 2));
					}
					if(rots[1] < preRot[1]) {
						event.pitch = (float) (preRot[1] - 40 + Math.pow((rand.nextFloat() - 0.5f) * 6f, 2));
					}
					preRot = new float[]{event.yaw, event.pitch};
					if(event.pitch > 90) {
						event.pitch = 90;
					}
					if(event.pitch < -90) {
						event.pitch = -90;
					}
					return;
				}
				else {
					event.yaw = (float) (rots[0] + Math.pow((rand.nextFloat() - 0.5f), 2));
					event.pitch = (float) (rots[1] + Math.pow((rand.nextFloat() - 0.5f), 2));
				}
			}
		}
		AuraUtils.targets.clear();
		if (!AuraUtils.hasEntity(en)) {
			AuraUtils.targets.add(en);
		}
		if(currentMode.equals("Silent")) {
			if (!timer.hasTimeElapsed(1000 / Math.min(9, ClientSettings.KillauraAPS), false)) {
				return;
			}
		}
		else {
			if(ClientSettings.KillauraAPS != 20) {
				if(en != null) {
					if(en.hurtTime < 3 && en.hurtTime != 0) {
						timer.reset();
					}
					else {
						if (!timer.hasTimeElapsed(1000 / ClientSettings.KillauraAPS, false)) {
							return;
						}
					}
				}
				else {
					if (!timer.hasTimeElapsed(1000 / ClientSettings.KillauraAPS, false)) {
						return;
					}
				}
			}
		}
		if (this.currentMode.equals("Single")) {
			EntityLivingBase newEn = null;
			newEn = Utils.getClosestEntity((float) ClientSettings.Blockrange);
			if (newEn == null) {							
				en = null;
				return;
			}
			if (mc.thePlayer.getDistanceToEntity(newEn) > ClientSettings.KillauraRange) {
				attack = false;
				en = newEn;
				return;
			}
			en = newEn;
			if (en == null) {
				return;
			}
			float[] rots = Utils.getFacePosEntity(en);
			if (!event.autopot) {
				event.yaw = rots[0];
				event.pitch = rots[1];
			}
			attack = true;
		}
		if (this.currentMode.equals("Multi")) {
			ens = Utils.getClosestEntities((float) ClientSettings.KillauraRange);
			if (ens.isEmpty()) {
				return;
			}
			for (EntityLivingBase en : ens) {
				float[] rots = Utils.getFacePosEntity(en);
				event.yaw = rots[0];
				event.pitch = rots[1];
			}
			AuraUtils.targets = ens;
			attack = true;
		}
		if (this.currentMode.equals("Switch")) {
			ens = Utils.getClosestEntities((float) ClientSettings.KillauraRange);
			if (Jigsaw.java8) {
				ens.sort(new Comparator<EntityLivingBase>() {
					public int compare(EntityLivingBase o1, EntityLivingBase o2) {
						if (mc.thePlayer.getDistanceToEntity(o1) > mc.thePlayer.getDistanceToEntity(o2)) {
							return 1;
						}
						if (mc.thePlayer.getDistanceToEntity(o1) < mc.thePlayer.getDistanceToEntity(o2)) {
							return -1;
						}
						if (mc.thePlayer.getDistanceToEntity(o1) == mc.thePlayer.getDistanceToEntity(o2)) {
							return 0;
						}
						return 0;
					};
				});
			}
			boolean choseEntity = false;
			for (EntityLivingBase en : ens) {
				if (!attackedEntity(en)) {
					this.en = en;
					choseEntity = true;
					lastEns.add(en);
					attack = true;
					float[] rots = Utils.getFacePosEntity(en);
					event.yaw = rots[0];
					event.pitch = rots[1];
					break;
				} else {
					continue;
				}
			}
			if (!choseEntity) {
				en = Utils.getClosestEntity((float) ClientSettings.KillauraRange);
				if (en == null) {
					return;
				}
				attack = true;
				lastEns.clear();
			}
		}
		if (attack) {
			timer.reset();
		}
		super.onUpdate();
	}

	@Override
	public void onLateUpdate() {
		if (!attack) {
			return;
		}
		attack = false;
		if (this.currentMode.equals("Multi")) {
			AutoBlock.stopBlock();
			for (EntityLivingBase en : ens) {
				mc.thePlayer.swingItem();
				if(rand.nextDouble() <= ClientSettings.killauraHitRatio) {
					sendPacket(new C02PacketUseEntity(en, Action.ATTACK));
					float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(),
							en.getCreatureAttribute());
					boolean vanillaCrit = (mc.thePlayer.fallDistance > 0.0F) && (!mc.thePlayer.onGround)
							&& (!mc.thePlayer.isOnLadder()) && (!mc.thePlayer.isInWater())
							&& (!mc.thePlayer.isPotionActive(Potion.blindness)) && (mc.thePlayer.ridingEntity == null);
					if ((Jigsaw.getModuleByName("Criticals").isToggled()) || (vanillaCrit)) {
						mc.thePlayer.onCriticalHit(en);
					}
					if (sharpLevel > 0.0F) {
						mc.thePlayer.onEnchantmentCritical(en);
					}
				}
			}
			AutoBlock.startBlock();
		} else {
			AutoBlock.stopBlock();
			mc.thePlayer.swingItem();
			if(rand.nextDouble() <= ClientSettings.killauraHitRatio) {
				sendPacket(new C02PacketUseEntity(en, Action.ATTACK));
				AutoBlock.startBlock();
				float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(),
						en.getCreatureAttribute());
				boolean vanillaCrit = (mc.thePlayer.fallDistance > 0.0F) && (!mc.thePlayer.onGround)
						&& (!mc.thePlayer.isOnLadder()) && (!mc.thePlayer.isInWater())
						&& (!mc.thePlayer.isPotionActive(Potion.blindness)) && (mc.thePlayer.ridingEntity == null);
				if ((Jigsaw.getModuleByName("Criticals").isToggled()) || (vanillaCrit)) {
					mc.thePlayer.onCriticalHit(en);
				}
				if (sharpLevel > 0.0F) {
					mc.thePlayer.onEnchantmentCritical(en);
				}
			}
		}
		super.onLateUpdate();
	}

	public boolean attackedEntity(EntityLivingBase en) {
		for (EntityLivingBase entity : lastEns) {
			if (entity.isEntityEqual(en)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onPacketRecieved(AbstractPacket packetIn) {
		super.onPacketRecieved(packetIn);
	}

	@Override
	public String[] getModes() {

		return new String[] { "Single", "Switch", "Multi" };
	}

	@Override
	protected void onModeChanged(String modeBefore, String newMode) {
		en = null;
		ens.clear();
		attack = false;
		super.onModeChanged(modeBefore, newMode);
	}

	// public static boolean doBlock() {
	// return en != null || !ens.isEmpty();
	// }
	//
	public static boolean doBlock() {
		return (en != null && mc.thePlayer.getDistanceToEntity(en) < ClientSettings.Blockrange) || (Jigsaw.getModuleByName("KillAura").getCurrentMode().equals("Multi") && !ens.isEmpty());
	}

	public static boolean getShouldChangePackets() {
		return en != null && !Jigsaw.getModuleByName("KillAura").getCurrentMode().equals("Multi")
				&& !ClientSettings.KillauraHeadsnap;
	}

}
