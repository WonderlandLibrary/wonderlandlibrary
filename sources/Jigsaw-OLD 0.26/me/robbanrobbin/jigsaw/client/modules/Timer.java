package me.robbanrobbin.jigsaw.client.modules;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Slider;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import org.darkstorm.minecraft.gui.listener.SliderListener;
import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.module.Module;

public class Timer extends Module {

	@Override
	public Component[] getModSettings() {
		// timerspeedslider

		Slider timerSpeedSlider = new BasicSlider("Timer Speed", Timer.getTimer(), 0.05, 10.0, 0.0,
				ValueDisplay.DECIMAL);

		timerSpeedSlider.addSliderListener(new SliderListener() {

			@Override
			public void onSliderValueChanged(Slider slider) {

				Timer.setTimer(slider.getValue());

			}
		});
		return new Component[] { timerSpeedSlider };
	}

	public Timer() {
		super("Timer", Keyboard.KEY_G, Category.WORLD, "Speeds up or slows down minecraft.");
	}

	@Override
	public void onDisable() {

		mc.timer.timerSpeed = 1.0f;

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {

		mc.timer.timerSpeed = (float) ClientSettings.Timerspeed;

		super.onUpdate();
	}

	public static double getTimer() {
		return ClientSettings.Timerspeed;
	}

	public static void setTimer(double set) {
		ClientSettings.Timerspeed = set;
	}

}
