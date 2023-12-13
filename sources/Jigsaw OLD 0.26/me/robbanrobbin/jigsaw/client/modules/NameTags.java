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

public class NameTags extends Module {
	@Override
	public Component[] getModSettings() {
		// nameTagsSlider

		Slider nameTagsSlider = new BasicSlider("Size", ClientSettings.Nametagssize, 2.0, 10.0, 0.0,
				ValueDisplay.INTEGER);

		nameTagsSlider.addSliderListener(new SliderListener() {

			@Override
			public void onSliderValueChanged(Slider slider) {

				ClientSettings.Nametagssize = (float) (slider.getValue());

			}
		});
		return new Component[] { nameTagsSlider };
	}

	public NameTags() {
		super("NameTags", Keyboard.KEY_NONE, Category.RENDER, "Changes nametags.");
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
	}

}
