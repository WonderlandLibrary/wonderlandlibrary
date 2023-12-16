package wtf.expensive.ui.beta.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;
import wtf.expensive.ui.beta.component.impl.ColorComponent;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.animation.AnimationMath;

import java.awt.*;
import java.util.Arrays;

public class ColorWindow {

    private ColorComponent component;
    private float[] hsb = new float[2];
    private float alpha;
    private boolean dragging;
    private boolean draggingHue;
    private boolean draggingAlpha;

    public ColorWindow(ColorComponent component) {
        this.component = component;
        hsb = Color.RGBtoHSB(component.option.getColor().getRed(), component.option.getColor().getGreen(),component.option.getColor().getBlue(), null);
        alpha = component.option.getColor().getAlpha() / 255f;
    }

    public static float[] copied = new float[2];

    public void draw(MatrixStack stack, int mouseX, int mouseY) {
        float width = 228 / 2f;
        float x = component.x + component.width - 10 + 8 / 2f;
        float y = component.y + component.height / 2f + 8 / 2f;

        RenderUtil.Render2D.drawShadow(component.x + component.width - 10 + 8 / 2f, component.y + component.height / 2f + 8 / 2f, 228 / 2f, 321 / 2f, 9, new Color(0, 0, 0).getRGB());

        RenderUtil.Render2D.drawRoundedCorner(component.x + component.width - 10 + 8 / 2f, component.y + component.height / 2f + 8 / 2f, 228 / 2f, 321 / 2f, 3, new Color(17, 18, 21).getRGB());
        RenderUtil.Render2D.drawGradientRound(x + 4,y + 4,width - 8,width - 8, 2, Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.getHSBColor(hsb[0],1,1).getRGB(),  Color.BLACK.getRGB());


        if (dragging) {
            float saturation = MathHelper.clamp((mouseX - x - 4), 0, width - 8) / (width - 8);
            float brightness = MathHelper.clamp((mouseY - y - 4), 0, width - 8) / (width - 8);
            hsb[1] = saturation;
            hsb[2] = 1 - brightness;
        }

        float circleX = x + 4 + hsb[1] * (width - 8);
        float circleY = y + 4 + (1 - hsb[2]) * (width - 8);

        RenderUtil.Render2D.drawRoundCircle(circleX + 1,circleY + 1, 8, Color.BLACK.getRGB());
        RenderUtil.Render2D.drawRoundCircle(circleX + 1,circleY + 1, 6, RenderUtil.reAlphaInt(component.option.get(), 255));

        for (int i = 0; i < width - 12;i++) {
            float hue = i / (width - 12);
            RenderUtil.Render2D.drawRoundCircle(x + 6 + i,y + width + 6, 6, RenderUtil.reAlphaInt(Color.HSBtoRGB(hue,1,1),255));
        }
        for (int i = 0; i < width - 12;i++) {
            float hue = i / (width - 12);
            RenderUtil.Render2D.drawRoundCircle(x + 6 + i,y + width + 18, 6, ColorUtil.interpolateColor(RenderUtil.reAlphaInt(component.option.get(), 255), RenderUtil.IntColor.rgba(17, 18, 21,255), 1 - hue));
        }
        RenderUtil.Render2D.drawRoundCircle(x + 6 + alpha *(width - 12),y + width + 18, 8, Color.BLACK.getRGB());
        RenderUtil.Render2D.drawRoundCircle(x + 6 + alpha *(width - 12),y + width + 18, 6, ColorUtil.interpolateColor(RenderUtil.IntColor.rgba(17, 18, 21,255), RenderUtil.reAlphaInt(component.option.get(), 255), alpha));
        if (draggingHue) {
            float hue = MathHelper.clamp((mouseX - x - 6), 0, width - 12) / (width - 12);
            hsb[0] = hue;
        }
        if (draggingAlpha) {
            float hue = MathHelper.clamp((mouseX - x - 6), 0, width - 12) / (width - 12);
            alpha = hue;
        }

        RenderUtil.Render2D.drawRoundedCorner(x + 4, y + 321 / 2f - 36 / 2f - 4, 102 / 2f, 36 / 2f, 3,new Color(40, 45, 51).getRGB());
        RenderUtil.Render2D.drawRoundedCorner(x + 4 + 51 + 4, y + 321 / 2f - 36 / 2f - 4, 102 / 2f, 36 / 2f, 3,new Color(40, 45, 51).getRGB());
        Fonts.gilroy[14].drawCenteredString(stack, "Copy", x + 4 +51 / 2f, y + 321 / 2f - 14.5f, -1);
        Fonts.gilroy[14].drawCenteredString(stack, "Paste", x + 4 + 51 + 4 +51 / 2f, y + 321 / 2f - 14.5f, -1);
        RenderUtil.Render2D.drawRoundCircle(x + 6 + hsb[0] *(width - 12),y + width + 6, 8, Color.BLACK.getRGB());
        RenderUtil.Render2D.drawRoundCircle(x + 6 + hsb[0] *(width - 12),y + width + 6, 6, Color.HSBtoRGB(hsb[0],1,1));
        if (dragging || draggingAlpha || draggingHue)
            component.option.color = RenderUtil.reAlphaInt(Color.getHSBColor(hsb[0], hsb[1],hsb[2]).getRGB(), (int) (alpha * 255));
        else {
            hsb = Color.RGBtoHSB(component.option.getColor().getRed(), component.option.getColor().getGreen(),component.option.getColor().getBlue(), null);
            alpha = component.option.getColor().getAlpha() / 255f;
        }

    }

    public void onConfigUpdate() {
        hsb = Color.RGBtoHSB(component.option.getColor().getRed(), component.option.getColor().getGreen(),component.option.getColor().getBlue(), null);
        alpha = component.option.getColor().getAlpha() / 255f;
    }

    public boolean click(int mouseX, int mouseY) {

        float width = 228 / 2f;
        float x = component.x + component.width - 10 + 8 / 2f;
        float y = component.y + component.height / 2f + 8 / 2f;

        if (RenderUtil.isInRegion(mouseX,mouseY, x + 4,y + width + 1,width - 8,6)) {
            draggingHue = true;
            return false;
        }
        if (RenderUtil.isInRegion(mouseX,mouseY, x + 4, y + 321 / 2f - 36 / 2f - 4, 102 / 2f, 36 / 2f)) {
            ColorWindow.copied = Color.RGBtoHSB(component.option.getColor().getRed(), component.option.getColor().getGreen(),component.option.getColor().getBlue(), null);
            return false;
        }
        if (RenderUtil.isInRegion(mouseX,mouseY, x + 4 + 51 + 4, y + 321 / 2f - 36 / 2f - 4, 102 / 2f, 36 / 2f)) {
            component.option.color = Color.HSBtoRGB(ColorWindow.copied[0],ColorWindow.copied[1], ColorWindow.copied[2]);
            hsb = Color.RGBtoHSB(component.option.getColor().getRed(), component.option.getColor().getGreen(),component.option.getColor().getBlue(), null);
            return false;
        }
        if (RenderUtil.isInRegion(mouseX,mouseY, x + 4,y + width + 13,width - 8,6)) {
            draggingAlpha = true;
            return false;
        }
        if (RenderUtil.isInRegion(mouseX,mouseY, x + 4,y + 4,width - 8,width - 8)) {
            dragging = true;
            return false;
        }
        return true;
    }

    public void unclick(int mouseX, int mouseY) {
        dragging = false;
        draggingHue = false;
        draggingAlpha = false;
    }

}
