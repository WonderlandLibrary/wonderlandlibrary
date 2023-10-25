package wtf.evolution.click;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import wtf.evolution.helpers.StencilUtil;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.RoundedUtil;

import java.awt.*;

public class BotScreen extends GuiScreen {

    float width = 500 - 50;
    float height = 330 - 50;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        float finalWidth = width;
        float finalHeight = height;
        RenderUtil.blur(() -> {
            RoundedUtil.drawRound(sr.getScaledWidth() / 2f - finalWidth / 2f, sr.getScaledHeight() / 2f - finalHeight / 2f, finalWidth, finalHeight, 2, new Color(20, 20, 20, 150));
        }, 5);
        RoundedUtil.drawRound(sr.getScaledWidth() / 2f - width / 2f, sr.getScaledHeight() / 2f - height / 2f, width, height, 2, new Color(20, 20, 20, 150));
        float width = this.width - 2;
        float height = this.height - 2;
        StencilUtil.initStencilToWrite();
        RenderUtil.drawRectWH(sr.getScaledWidth() / 2f - width / 2f, sr.getScaledHeight() / 2f - height / 2f, width, height, new Color(20, 20, 20, 150).getRGB());
        StencilUtil.readStencilBuffer(0);
        RenderUtil.drawBlurredShadow(sr.getScaledWidth() / 2f - width / 2f, sr.getScaledHeight() / 2f - height / 2f, width, height, 15, new Color(0,0,0, 255));
        StencilUtil.uninitStencilBuffer();

        float x = sr.getScaledWidth() / 2f - width / 2f;
        float y = sr.getScaledHeight() / 2f - height / 2f;


        Fonts.RUB14.drawCenteredString("Evolution Web", x + width / 2, y - 10, new Color(255, 255, 255, 100).getRGB());



    }
}
