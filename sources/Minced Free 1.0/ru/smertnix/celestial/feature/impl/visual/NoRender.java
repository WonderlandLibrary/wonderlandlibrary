package ru.smertnix.celestial.feature.impl.visual;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.MobEffects;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.MultipleBoolSetting;

public class NoRender extends Feature {
    public static MultipleBoolSetting sel = new MultipleBoolSetting("Elements selection",
    		new BooleanSetting("Scoreboard", true),
    		new BooleanSetting("Title", true),
    		new BooleanSetting("Totem",true),
    		new BooleanSetting("Lag Machine",true),
    		new BooleanSetting("Record Playing", true),
    		new BooleanSetting("Boss Bar"));


    public NoRender() {
        super("No Render", "Убирает не нужные области рендеринга", FeatureCategory.Render);
        addSettings(sel);

    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
    	 mc.world.setRainStrength(0);
         mc.world.setThunderStrength(0);
         mc.player.removePotionEffect(MobEffects.NAUSEA);
         mc.player.removePotionEffect(MobEffects.BLINDNESS);
    }

}
