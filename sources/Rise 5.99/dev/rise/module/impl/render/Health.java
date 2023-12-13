package dev.rise.module.impl.render;

import dev.rise.event.impl.render.Render2DEvent;
import dev.rise.font.CustomFont;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.util.math.MathUtil;
import net.minecraft.client.gui.ScaledResolution;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.awt.*;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "Health", description = "Shows your health under the crosshair", category = Category.RENDER)
public class Health extends Module {

    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        final ScaledResolution sr = new ScaledResolution(mc);

        final String health = String.valueOf(MathUtil.round(mc.thePlayer.getHealth(), 1));

        final float x = sr.getScaledWidth() / 2F + 10;
        final float y = sr.getScaledHeight() / 2F + 10;

        if (mc.thePlayer.getHealth() < 14 && mc.thePlayer.getHealth() >= 6) {
            CustomFont.drawString(health, x, y, Color.ORANGE.getRGB());
        } else if (mc.thePlayer.getHealth() < 6) {
            CustomFont.drawString(health, x, y, Color.RED.getRGB());
        } else {
            CustomFont.drawString(health, x, y, Color.GREEN.getRGB());
        }
    }
}
