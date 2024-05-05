package ru.smertnix.celestial.feature.impl.visual;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;

public class BabyBoy extends Feature {
	public BabyBoy() {
        super("Baby Boy", "Мой маленький", FeatureCategory.Render);
    }
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
}
