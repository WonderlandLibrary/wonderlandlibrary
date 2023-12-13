package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.PacketEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityVelocity;

public class Knockback extends Module {
	
	@Override
	public ModSetting[] getModSettings() {
//		Slider vert = new BasicSlider("Vertical Knockback", ClientSettings.KBVertical, 0.0, 1.0, 0.0,
//				ValueDisplay.PERCENTAGE);
//		vert.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				ClientSettings.KBVertical = slider.getValue();
//
//			}
//		});
//		
//		Slider hori = new BasicSlider("Horizontal Knockback", ClientSettings.KBHorizontal, 0.0, 1.0, 0.0,
//				ValueDisplay.PERCENTAGE);
//		hori.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				ClientSettings.KBHorizontal = slider.getValue();
//
//			}
//		});
		SliderSetting slider1 = new SliderSetting("Vertical", "KBVertical", 0.0, 1.0, 0.0, ValueFormat.PERCENT);
		SliderSetting slider2 = new SliderSetting("Horizontal", "KBHorizontal", 0.0, 1.0, 0.0, ValueFormat.PERCENT);
		return new ModSetting[]{slider1, slider2};
	}

	public Knockback() {
		super("Knockback", Keyboard.KEY_NONE, Category.PLAYER,
				"Modifies your knockback.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
	}
	
	@Override
	public String getAddonText() {
		return Math.round(ClientSettings.KBVertical * 100d) / 100d + ", " + Math.round(ClientSettings.KBHorizontal * 100d) / 100d;
	}

	@Override
	public void onPacketRecieved(PacketEvent event) {
		if(Jigsaw.getModuleByName("BowPush").isToggled()) {
			return;
		}
		Packet packet = event.getPacket();
		if (packet instanceof SPacketEntityVelocity) {
			Entity entity = mc.world
					.getEntityByID(((SPacketEntityVelocity) packet).getEntityID());
			if (entity instanceof EntityPlayerSP) {
				SPacketEntityVelocity vel = ((SPacketEntityVelocity) packet);
				event.cancel();
				mc.player.addVelocity(((double) vel.getMotionX() / 8000.0D) * ClientSettings.KBHorizontal, 
						((double) vel.getMotionY() / 8000.0D) * ClientSettings.KBVertical,
						((double) vel.getMotionZ() / 8000.0D) * ClientSettings.KBHorizontal);
			}
		}
		super.onPacketRecieved(event);
	}

}
