/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.misc;

import java.awt.Color;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.shader.shaders.GlowShader;

@ModuleInfo(name="Test", description="awa", category=ModuleCategory.MISC)
public final class Test
extends Module {
    @EventTarget
    public final void on2D(Render2DEvent render2DEvent) {
        GlowShader glowShader = GlowShader.GLOW_SHADER;
        glowShader.startDraw(render2DEvent.getPartialTicks());
        RenderUtils.drawRect(50, 50, 100, 100, new Color(Integer.MAX_VALUE).getRGB());
        float f = 2.5f;
        glowShader.stopDraw(new Color(Integer.MAX_VALUE), f, 1.0f);
    }
}

