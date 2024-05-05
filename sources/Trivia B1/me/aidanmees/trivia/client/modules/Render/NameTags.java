package me.aidanmees.trivia.client.modules.Render;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.client.settings.ClientSettings;
import me.aidanmees.trivia.gui.custom.clickgui.ModSetting;
import me.aidanmees.trivia.gui.custom.clickgui.SliderSetting;
import me.aidanmees.trivia.gui.custom.clickgui.ValueFormat;
import me.aidanmees.trivia.module.Module;

public class NameTags extends Module {
	@Override
	public ModSetting[] getModSettings() {
		// nameTagsSlider

//		Slider nameTagsSlider = new BasicSlider("Size", ClientSettings.Nametagssize, 2.0, 10.0, 0.0,
//				ValueDisplay.INTEGER);
//
//		nameTagsSlider.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				ClientSettings.Nametagssize = (float) (slider.getValue());
//
//			}
//		});
		SliderSetting<Number> nameTagsSlider = new SliderSetting<Number>("Tag Size", ClientSettings.Nametagssize, 2.0, 10.0, 0.0, ValueFormat.DECIMAL);
		return new ModSetting[] { nameTagsSlider };
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
