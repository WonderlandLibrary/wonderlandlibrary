package me.robbanrobbin.jigsaw.client.modules;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Slider;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import org.darkstorm.minecraft.gui.listener.SliderListener;
import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Teleport extends Module {

	@Override
	public Component[] getModSettings() {
		// zoooom range slider

		Slider zoooomRangeSlider = new BasicSlider("Teleport Range", ClientSettings.TPrange, 0.05, 10, 0.0,
				ValueDisplay.DECIMAL);

		zoooomRangeSlider.addSliderListener(new SliderListener() {

			@Override
			public void onSliderValueChanged(Slider slider) {

				ClientSettings.TPrange = slider.getValue();

			}
		});
		return new Component[] { zoooomRangeSlider };

	}

	public Teleport() {
		super("Teleport", Keyboard.KEY_X, Category.MOVEMENT, "Teleports you forward.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		double angleA = Math.toRadians(Utils.normalizeAngle(mc.thePlayer.rotationYawHead - 90));
		if (this.currentMode.equalsIgnoreCase("Client")) {
			mc.thePlayer.setPosition(mc.thePlayer.posX - Math.cos(angleA) * ClientSettings.TPrange, mc.thePlayer.posY,
					mc.thePlayer.posZ - Math.sin(angleA) * ClientSettings.TPrange);
		} else {
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
					mc.thePlayer.posX - Math.cos(angleA) * ClientSettings.TPrange, mc.thePlayer.posY,
					mc.thePlayer.posZ - Math.sin(angleA) * ClientSettings.TPrange, mc.thePlayer.onGround));
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
		double angleA = Math.toRadians(Utils.normalizeAngle(mc.thePlayer.rotationYawHead - 90));
		mc.thePlayer.setPosition(mc.thePlayer.posX - Math.cos(angleA) * range, mc.thePlayer.posY,
				mc.thePlayer.posZ - Math.sin(angleA) * range);
	}

}
