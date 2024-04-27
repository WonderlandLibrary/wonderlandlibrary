package me.AquaVit.liquidSense.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import net.minecraft.client.*;
import net.minecraft.util.*;

import java.awt.*;

@ModuleInfo(name = "Health", description = "Health", category = ModuleCategory.RENDER)
public class Health extends Module {
    private int width;

    @EventTarget
    private void renderHud(Render2DEvent event) {
        if (mc.thePlayer.getHealth() >= 0.0f) {
            if (mc.thePlayer.getHealth() < 10.0f) {
                this.width = 3;
            }
        }

        if (mc.thePlayer.getHealth() >= 10.0f) {
            if (mc.thePlayer.getHealth() < 100.0f) {
                this.width = 6;
            }
        }
        final FontRenderer fontRendererObj = Health.mc.fontRendererObj;
        final String string = new StringBuilder().append(MathHelper.ceiling_float_int(mc.thePlayer.getHealth())).toString();
        final float x = (float)(new ScaledResolution(Health.mc).getScaledWidth() / 2 - this.width);
        final float y = new ScaledResolution(Health.mc).getScaledHeight() / 2 - 17;
        fontRendererObj.drawStringWithShadow(string, x, y, (mc.thePlayer.getHealth() <= 10.0f) ? new Color(255, 0, 0).getRGB() : new Color(0, 255, 0).getRGB());
    }

}
