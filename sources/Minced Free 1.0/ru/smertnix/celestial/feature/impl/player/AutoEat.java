package ru.smertnix.celestial.feature.impl.player;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemFood;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;

public class AutoEat extends Feature {
    public AutoEat() {
        super("Auto Eat", "Автоматически кушает еду", FeatureCategory.Player);
    }
    
    public void onUpdate(EventUpdate event) {
    	if (mc.player.getHeldItemOffhand().getItem() instanceof ItemFood) {
    		if (mc.player.getFoodStats().getFoodLevel() < 18) {
                mc.gameSettings.keyBindUseItem.pressed = true;
        	} else {
                mc.gameSettings.keyBindUseItem.pressed = false;
        	}
    	}
    }
}
