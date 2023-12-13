package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.CheckBtnModLinker;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.CheckBtnSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;

public class TriggerBot extends Module {

	private WaitTimer timer = new WaitTimer();

	public TriggerBot() {
		super("TriggerBot", Keyboard.KEY_NONE, Category.COMBAT, "Attacks the entity you are looking at");
	}

	@Override
	public void onUpdate() {
		if (mc.objectMouseOver == null) {
			return;
		}
		if (mc.objectMouseOver.entityHit != null && mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
			EntityLivingBase entityHit = (EntityLivingBase) mc.objectMouseOver.entityHit;
			if (!Utils.validEntity(entityHit)
					|| mc.player.getDistanceToEntity(entityHit) > ClientSettings.triggerbotRange) {
				return;
			}
			if(!isReadyToAttack()) {
				return;
			}
			
			boolean sprinting = mc.player.isSprinting();
			boolean sprintCrits = ClientSettings.sprintCrits;
			
			if(sprintCrits) {
				if(sprinting) {
					sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
				}
			}
			
			AutoBlock.stopBlock();
			if(rand.nextDouble() <= ClientSettings.triggerbotHitRatio) {
				sendPacket(new CPacketUseEntity(entityHit));
			}
			mc.player.swingArm(EnumHand.MAIN_HAND);
			if(ClientSettings.useCooldown) {
				mc.player.resetCooldown();
			}
			
			if(sprintCrits) {
				if(sprinting) {
					sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
				}
			}
			
			AutoBlock.startBlock();
			
			Utils.createHitParticles(Jigsaw.getModuleByName("Criticals").isToggled(), entityHit);
		}
		super.onUpdate();
	}
	
	@Override
	public String getAddonText() {
		String text = super.getAddonText() == null ? "" : super.getAddonText();
		if(ClientSettings.sprintCrits) {
			text += "SprintCrits";
		}
		text += ", Cooldown: " + (ClientSettings.useCooldown ? "§aTrue§r" : "§cFalse§r");
		return text;
	}
	
	private boolean isReadyToAttack() {
		return (ClientSettings.useCooldown ? mc.player.getCooledAttackStrength(-0.5f) >= 1f : timer.hasTimeElapsed(1000 / ClientSettings.triggerbotAPS, true));
	}
	
	@Override
	public ModSetting[] getModSettings() {
		return new ModSetting[] {
				new CheckBtnSetting("Use Cooldown", "useCooldown"),
				new CheckBtnModLinker("Criticals"),
				new CheckBtnSetting("Enable Sprint-Crits", "sprintCrits"),
				new SliderSetting("Hit Range", "triggerbotRange", 3.0, 6.0, 0.0, ValueFormat.DECIMAL),
				new SliderSetting("Hits Per Second", "triggerbotAPS", 1.0, 20.0, 0.0, ValueFormat.DECIMAL),
				new SliderSetting("Hit Chance", "triggerbotHitRatio", 0.0, 1.0, 0.0, ValueFormat.PERCENT), 
		};
	}

}
