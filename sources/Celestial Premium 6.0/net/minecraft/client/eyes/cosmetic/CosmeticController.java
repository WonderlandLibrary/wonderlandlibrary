/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.eyes.cosmetic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.visuals.CustomModel;

public class CosmeticController {
    public static boolean shouldRenderCosmetic(AbstractClientPlayer player) {
        if (!(!CustomModel.onlyMe.getCurrentValue() || player == Minecraft.getMinecraft().player || Celestial.instance.friendManager.isFriend(player.getName()) && CustomModel.friends.getCurrentValue())) {
            return false;
        }
        return Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.googlyEyes.getCurrentValue() && CustomModel.modelMode.currentMode.equals("None");
    }

    public static float[] getCosmeticColor(AbstractClientPlayer player) {
        return new float[]{1.0f, 1.0f, 1.0f};
    }
}

