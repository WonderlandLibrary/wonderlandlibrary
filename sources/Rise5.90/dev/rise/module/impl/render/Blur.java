/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.render;

import dev.rise.event.impl.other.WorldChangedEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.util.render.InGameBlurUtil;
import net.minecraft.client.gui.ScaledResolution;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "Blur", description = "Blurs interfaces", category = Category.RENDER)
public final class Blur extends Module {

    public static void renderBlur() {
        InGameBlurUtil.toBlurBuffer.bindFramebuffer(false);
        InGameBlurUtil.setupBuffers();
        //InGameBlurUtil.renderGaussianBlur((float) ((NumberSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("Blur", "Radius"))).getValue(), (float) ((NumberSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("Blur", "Compression"))).getValue(), true, false);
        InGameBlurUtil.renderGaussianBlur(5, 2, true, false);
        mc.getFramebuffer().bindFramebuffer(false);
    }

    @Override
    public void onWorldChanged(final WorldChangedEvent event) {
        InGameBlurUtil.sr = new ScaledResolution(mc);
    }
}
