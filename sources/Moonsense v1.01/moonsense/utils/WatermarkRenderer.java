// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.utils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.Minecraft;
import moonsense.ui.utils.GuiUtils;
import moonsense.MoonsenseClient;
import net.minecraft.client.renderer.GlStateManager;

public class WatermarkRenderer
{
    public static void render(final int x, final int y) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        final int color = MoonsenseClient.getBrandingColor(255);
        GuiUtils.setGlColor(color);
        Minecraft.getMinecraft().getTextureManager().bindTexture(MoonsenseClient.CLIENT_LOGO);
        Gui.drawModalRectWithCustomSizedTexture(x - 223, y - 25, 0.0f, 0.0f, 24, 24, 24.0f, 24.0f);
        MoonsenseClient.watermarkRenderer1.drawString("MOONSENSE ", x - 194, y - 25, color);
        MoonsenseClient.watermarkRenderer2.drawString("CLIENT", x - 206 + MoonsenseClient.watermarkRenderer1.getWidth("STREAMLINED "), (float)(y - 25), color);
        GlStateManager.popMatrix();
    }
}
