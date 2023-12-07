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

public class ExtendedReach extends Module {

	@Override
	public Component[] getModSettings() {
		// reach range slider

		Slider reachRangeSlider = new BasicSlider("Extendedreach Range", ClientSettings.ExtendedReachrange, 3.5, 7.0,
				0.0, ValueDisplay.DECIMAL);

		reachRangeSlider.addSliderListener(new SliderListener() {

			@Override
			public void onSliderValueChanged(Slider slider) {

				ClientSettings.ExtendedReachrange = (float) slider.getValue();

			}
		});
		return new Component[] { reachRangeSlider };
	}

	public ExtendedReach() {
		super("ExtendedReach", Keyboard.KEY_NONE, Category.COMBAT,
				"Enables you to place blocks and hit entites further away. Warning! Don't mine blocks more than 5 blocks away!");
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

}
