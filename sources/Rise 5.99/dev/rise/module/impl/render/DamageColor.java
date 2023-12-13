/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.render;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.NumberSetting;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "DamageColor", description = "Changes the color when people get damaged", category = Category.RENDER)
public final class DamageColor extends Module {
    private final NumberSetting red = new NumberSetting("Red", this, 255, 0, 255, 1);
    private final NumberSetting green = new NumberSetting("Green", this, 0, 0, 255, 1);
    private final NumberSetting blue = new NumberSetting("Blue", this, 0, 0, 255, 1);
    private final NumberSetting alpha = new NumberSetting("Alpha", this, 76, 0, 255, 1);

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        RendererLivingEntity.dmgRed = (float) red.getValue() / 255;
        RendererLivingEntity.dmgGreen = (float) green.getValue() / 255;
        RendererLivingEntity.dmgBlue = (float) blue.getValue() / 255;
        RendererLivingEntity.dmgAlpha = (float) alpha.getValue() / 255;
    }
}
