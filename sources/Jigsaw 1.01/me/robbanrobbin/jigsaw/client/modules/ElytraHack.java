package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.PreMotionEvent;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.MovementInput;

public class ElytraHack extends Module {
	
	private double moveSpeed;

	public ElytraHack() {
		super("ElytraHack", Keyboard.KEY_NONE, Category.EXPLOITS, "Enables you to fly forever with an elytra! Press space to go up. Press forward to get a boost.");
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		super.onUpdate(event);

		if(isMode("NCP")) {
			sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
			event.y += 0.1;
		}
	}
	
	@Override
	public void onPreMotion(PreMotionEvent event) {
		super.onPreMotion(event);
		
		if(isMode("NCP")) {
			
			moveSpeed = mc.player.isCollided ? 0.2873D : ClientSettings.elytraHackNCPSpeed;
			
			MovementInput movementInput = mc.player.movementInput;
			float forward = movementInput.moveForward;
			float strafe = movementInput.moveStrafe;
			float yaw = Minecraft.getMinecraft().player.rotationYaw;
			
			if ((forward == 0.0F) && (strafe == 0.0F)) {
				event.x = 0.0D;
				event.z = 0.0D;
			} else if (forward != 0.0F) {
				if (strafe >= 1.0F) {
					yaw += (forward > 0.0F ? -45 : 45);
					strafe = 0.0F;
				} else if (strafe <= -1.0F) {
					yaw += (forward > 0.0F ? 45 : -45);
					strafe = 0.0F;
				}
				if (forward > 0.0F) {
					forward = 1.0F;
				} else if (forward < 0.0F) {
					forward = -1.0F;
				}
			}
			double mx = Math.cos(Math.toRadians(yaw + 90.0F));
			double mz = Math.sin(Math.toRadians(yaw + 90.0F));
			event.x = (forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
			event.z = (forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
		}
		
	}
	
	@Override
	public ModSetting[] getModSettings() {
		return new ModSetting[] {
			new SliderSetting("NCP Speed", "elytraHackNCPSpeed", 0.1, 1.5, ValueFormat.DECIMAL)
		};
	}
	
	@Override
	public String[] getModes() {
		return new String[] {"Vanilla", "NCP"};
	}

}
