package org.dreamcore.client.feature.impl.visual;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.settings.impl.ListSetting;

public class FullBright extends Feature {

    public static ListSetting brightMode;

    public FullBright() {
        super("FullBright", "Убирает темноту в игре", Type.Visuals);
        brightMode = new ListSetting("FullBright Mode", "Gamma", () -> true, "Gamma", "Potion");
        addSettings(brightMode);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (getState()) {
            String mode = brightMode.getOptions();
            if (mode.equalsIgnoreCase("Gamma")) {
                mc.gameSettings.gammaSetting = 1000F;
            }
            if (mode.equalsIgnoreCase("Potion")) {
                mc.player.addPotionEffect(new PotionEffect(Potion.getPotionById(16), 817, 1));
            } else {
                mc.player.removePotionEffect(Potion.getPotionById(16));
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.gammaSetting = 1F;
        mc.player.removePotionEffect(Potion.getPotionById(16));
    }
}
