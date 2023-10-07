package dev.rise.ui.console;

import dev.rise.font.CustomFont;
import dev.rise.util.math.MathUtil;
import dev.rise.util.misc.RiseLogger;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class RiseConsole extends GuiScreen {
    private float size;

    @Override
    public void initGui() {
        size = 0;
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        size = (float) MathUtil.lerp(size, 1, 0.05);

        GlStateManager.pushMatrix();

        final ScaledResolution sr = new ScaledResolution(mc);

        GlStateManager.translate(sr.getScaledWidth() / 2F - 500 * size, sr.getScaledHeight() / 2F - 250 * size, 1);
        GlStateManager.scale(size, size, size);
//        GL11.glLineWidth(2);
//        RenderUtil.rect(-1, -1, 1002, 502, false, new Color(0xff000000));
        drawRect(0, 0, 1000, 500, 0xdd36393F);
        drawRect(0, -20, 1000, 0, 0xfa202225);

        for (int i = 0; i < 50; i++) {
            if (i >= RiseLogger.getMessages().size() || RiseLogger.getMessages().size() < 1)
                break;

            CustomFont.drawString(RiseLogger.getMessages().get(RiseLogger.getMessages().size() - 1 - i), 0, 500 - (i + 1) * (CustomFont.getHeight() + 1), Color.WHITE.getRGB());
        }
        CustomFont.drawStringMedium("Rise Console", 0, -(20 - CustomFont.getHeight() / 2F), Color.WHITE.getRGB());

        GlStateManager.popMatrix();
    }
}
