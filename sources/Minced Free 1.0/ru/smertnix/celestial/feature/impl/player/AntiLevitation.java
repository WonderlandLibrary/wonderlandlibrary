package ru.smertnix.celestial.feature.impl.player;

import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;

public class AntiLevitation extends Feature {

    public AntiLevitation() {
        super("Anti Levitation", "Позволяет не чувствовать эффект левитации", FeatureCategory.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
    	/*if (mc.player.ticksExisted % 10 == 0) {
    		mc.player.sendChatMessage("//wand");
    		mc.gameSettings.keyBindPickBlock.pressed = true;
    		mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 36, 1, ClickType.THROW, mc.player);
    	}*/
        if (mc.player.isPotionActive(MobEffects.LEVITATION)) {
            mc.player.removeActivePotionEffect(MobEffects.LEVITATION);
        }
    }
}