package xyz.cucumber.base.module.feat.movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.EventType;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.utils.MovementUtils;

@ModuleInfo(category = Category.MOVEMENT, description = "Allows you jump further", name = "Long Jump", key = Keyboard.KEY_NONE)
public class LongJumpModule extends Mod {

	public ModeSettings mode = new ModeSettings("Mode", new String[] { "Intave Boat", "Intave", "Polar" });
	public int jumps = 0;

	public LongJumpModule() {
		this.addSettings(mode);
	}

	public void onEnable() {

		if (mc.thePlayer == null || mc.theWorld == null) {
			return;
		}

		jumps = 0;
	}

	@EventListener
	public void onMotion(EventMotion e) {
		this.setInfo(mode.getMode() + "");
		
		if (mc.thePlayer == null || mc.theWorld == null) {
			return;
		}

		switch (mode.getMode().toLowerCase()) {
		case "intave boat":
			if (e.getType() == EventType.PRE) {
				if (mc.thePlayer.isRiding()) {
					mc.getNetHandler().getNetworkManager()
							.sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, Action.START_SNEAKING));
					mc.getNetHandler().getNetworkManager()
							.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(
									mc.thePlayer.posX - Math.sin(Math.toRadians(mc.thePlayer.rotationYaw)) * 3,
									mc.thePlayer.posY + 2,
									mc.thePlayer.posZ + Math.cos(Math.toRadians(mc.thePlayer.rotationYaw)) * 3, false));
					this.toggle();
				}
			}
			break;
		case "intave":
			if (e.getType() == EventType.PRE) {
				if (mc.thePlayer.onGround) {
					mc.thePlayer.jump();
					mc.thePlayer.jump();
					this.toggle();
				}
			}
			break;

		case "polar":
			if (e.getType() == EventType.PRE) {
				if (mc.thePlayer.onGround) {
					mc.thePlayer.jump();
				} else {
					mc.thePlayer.motionY += 0.05999;
					MovementUtils.strafe((float) (MovementUtils.getSpeed() * 1.15f));
				}
			}
			break;
		}
	}
}
