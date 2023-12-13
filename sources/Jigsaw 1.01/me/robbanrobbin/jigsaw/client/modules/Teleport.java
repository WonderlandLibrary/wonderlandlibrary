package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.play.client.CPacketPlayer;

public class Teleport extends Module {

	@Override
	public ModSetting[] getModSettings() {
		// zoooom range slider

//		Slider zoooomRangeSlider = new BasicSlider("Teleport Range", ClientSettings.TPrange, 0.05, 10, 0.0,
//				ValueDisplay.DECIMAL);
//
//		zoooomRangeSlider.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				ClientSettings.TPrange = slider.getValue();
//
//			}
//		});
		SliderSetting zoooomRangeSlider = new SliderSetting("Teleport Range", "TPrange", 0.05, 9.9, 0.0, ValueFormat.DECIMAL);
		return new ModSetting[] { zoooomRangeSlider };

	}

	public Teleport() {
		super("Teleport", Keyboard.KEY_NONE, Category.MOVEMENT, "Teleports you forward.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		double angleA = Math.toRadians(Utils.normalizeAngle(mc.player.rotationYawHead - 90));
		if (this.currentMode.equalsIgnoreCase("Client")) {
			mc.player.setPosition(mc.player.posX - Math.cos(angleA) * ClientSettings.TPrange, mc.player.posY,
					mc.player.posZ - Math.sin(angleA) * ClientSettings.TPrange);
		} else {
			mc.player.connection.sendPacket(new CPacketPlayer.Position(
					mc.player.posX - Math.cos(angleA) * ClientSettings.TPrange, mc.player.posY,
					mc.player.posZ - Math.sin(angleA) * ClientSettings.TPrange, mc.player.onGround));
		}
		this.setToggled(false, true);
		super.onEnable();
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
	}

	@Override
	public String[] getModes() {
		return new String[] { "Client", "Packet" };
	}

	@Override
	public String getAddonText() {
		return this.currentMode;
	}

	public static void tpStatic(double range) {
		double angleA = Math.toRadians(Utils.normalizeAngle(mc.player.rotationYawHead - 90));
		mc.player.setPosition(mc.player.posX - Math.cos(angleA) * range, mc.player.posY,
				mc.player.posZ - Math.sin(angleA) * range);
	}

}
