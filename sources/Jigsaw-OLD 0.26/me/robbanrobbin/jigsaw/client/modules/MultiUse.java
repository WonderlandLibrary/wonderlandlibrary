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

public class MultiUse extends Module {
	@Override
	public Component[] getModSettings() {
		BasicSlider slider1 = new BasicSlider("Amount", ClientSettings.MultiUseamount, 1, 1000, 0,
				ValueDisplay.INTEGER);
		slider1.addSliderListener(new SliderListener() {
			@Override
			public void onSliderValueChanged(Slider slider) {
				ClientSettings.MultiUseamount = (int) Math.round(slider.getValue());
			}
		});
		return new Component[] { slider1 };
	}

	public MultiUse() {
		super("MultiUse", Keyboard.KEY_NONE, Category.FUN, "When you use something, it uses it again many times.");
	}

	@Override
	public void onRightClick() {
		for (int i = 0; i < ClientSettings.MultiUseamount; i++) {
			mc.rightClickMouse();
		}
		super.onRightClick();
	}

}
