/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import java.util.Objects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.visuals.NightMode;
import org.celestial.client.helpers.render.AnimationHelper;
import org.celestial.client.settings.impl.ListSetting;

public class FullBright
extends Feature {
    public static ListSetting brightMode;
    private float gammaValue;

    public FullBright() {
        super("FullBright", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u0442\u0435\u043c\u043d\u043e\u0442\u0443 \u0432 \u0438\u0433\u0440\u0435", Type.Visuals);
        brightMode = new ListSetting("FullBright Mode", "Gamma", () -> true, "Gamma", "Potion");
        this.addSettings(brightMode);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (this.getState() && !Celestial.instance.featureManager.getFeatureByClass(NightMode.class).getState()) {
            String mode = brightMode.getOptions();
            if (mode.equalsIgnoreCase("Gamma")) {
                this.gammaValue = (float)AnimationHelper.animation((double)this.gammaValue, 1.0, 0.001);
                FullBright.mc.gameSettings.gammaSetting = this.gammaValue = MathHelper.clamp(this.gammaValue + 0.1f, 0.0f, 1.0f);
            }
            if (mode.equalsIgnoreCase("Potion")) {
                FullBright.mc.player.addPotionEffect(new PotionEffect(Objects.requireNonNull(Potion.getPotionById(16)), 817, 1));
            } else {
                FullBright.mc.player.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(16)));
            }
        }
    }

    @Override
    public void onEnable() {
        this.gammaValue = 0.0f;
        FullBright.mc.gameSettings.gammaSetting = 0.0f;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.gammaValue = 0.0f;
        FullBright.mc.gameSettings.gammaSetting = 0.0f;
        FullBright.mc.player.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(16)));
        super.onDisable();
    }
}

