// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.newui.elements;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import ru.tuskevich.util.animations.AnimationMath;
import org.lwjgl.input.Mouse;
import ru.tuskevich.util.math.HoveringUtil;
import ru.tuskevich.util.render.RenderUtility;
import ru.tuskevich.util.color.ColorUtility;
import ru.tuskevich.util.font.Fonts;
import java.awt.Color;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import ru.tuskevich.ui.dropui.setting.imp.ColorSetting;

public class ElementColor extends Element
{
    public ElementModule module;
    public ColorSetting setting;
    public static Tessellator tessellator;
    public static BufferBuilder buffer;
    public int prevX;
    public int prevY;
    public float hue;
    public boolean renderRGB;
    public float alpha;
    
    public ElementColor(final ElementModule e, final ColorSetting setting) {
        this.module = e;
        this.setting = setting;
        this.hue = Color.RGBtoHSB(setting.getColorValueColor().getRed(), setting.getColorValueColor().getGreen(), setting.getColorValueColor().getBlue(), null)[0];
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        final float[] hsb = Color.RGBtoHSB(this.setting.getColorValueColor().getRed(), this.setting.getColorValueColor().getGreen(), this.setting.getColorValueColor().getBlue(), null);
        final float xC = (float)(hsb[1] * (this.module.width - 10.0));
        final float yC = (1.0f - hsb[2]) * 51.0f;
        this.height = 67.0;
        Fonts.MONTSERRAT12.drawCenteredString(this.setting.getName(), this.x + this.width / 2.0, this.y + 3.0, -1);
        this.drawColorPickerRect((float)(this.x + 2.0), (float)(this.y + 8.0), (float)(this.module.width - 10.0), 52.0f);
        for (float i = 0.0f; i < 1.0f; i += (float)0.01) {
            RenderUtility.drawRect(this.x + this.width - 6.0, this.y + 8.0 + i * 51.0f, 4.0, 1.0, ColorUtility.setAlpha(Color.HSBtoRGB(i, 1.0f, 1.0f), 255));
        }
        RenderUtility.drawRect(this.x + this.width - 6.0, this.y + 8.0 + this.hue * 51.0f, 4.0, 1.0, -1);
        if (HoveringUtil.isHovering((float)(this.x + this.width - 6.0), (float)(this.y + 8.0), 4.0f, 51.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            this.hue = (float)((mouseY - this.y - 8.0) / 52.0);
        }
        this.renderRGB = (this.isHovered(mouseX, mouseY) && Mouse.isButtonDown(0));
        this.alpha = AnimationMath.fast(this.alpha, this.renderRGB ? 1.0f : 0.0f, 10.0f);
        if (HoveringUtil.isHovering((float)(this.x + 2.0), (float)(this.y + 8.0), (float)(this.module.width - 10.0), 51.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            final float saturation = (float)((mouseX - this.x - 2.0) / (this.module.width - 10.0));
            final float brightness = 1.0f - (float)((mouseY - this.y - 8.0) / 51.0);
            this.setting.setColorValue(Color.getHSBColor(this.hue, saturation, brightness).getRGB());
        }
        RenderUtility.drawRoundCircle((float)(this.x + xC + 3.0), (float)(this.y + yC + 9.0), 3.0f, Color.BLACK);
        RenderUtility.drawRoundCircle((float)(this.x + xC + 3.0), (float)(this.y + yC + 9.0), 2.0f, this.setting.getColorValueColor());
    }
    
    public void drawColorPickerRect(final float left, final float top, float right, float bottom) {
        right += left;
        bottom += top;
        final int hueBasedColor = Color.HSBtoRGB(this.hue, 1.0f, 1.0f);
        GL11.glDisable(3553);
        GlStateManager.enableBlend();
        GL11.glShadeModel(7425);
        ElementColor.buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        ElementColor.buffer.pos(right, top, 0.0).color(hueBasedColor).endVertex();
        ElementColor.buffer.pos(left, top, 0.0).color(-1).endVertex();
        ElementColor.buffer.pos(left, bottom, 0.0).color(-1).endVertex();
        ElementColor.buffer.pos(right, bottom, 0.0).color(hueBasedColor).endVertex();
        ElementColor.tessellator.draw();
        ElementColor.buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        ElementColor.buffer.pos(right, top, 0.0).color(402653184).endVertex();
        ElementColor.buffer.pos(left, top, 0.0).color(402653184).endVertex();
        ElementColor.buffer.pos(left, bottom, 0.0).color(-16777216).endVertex();
        ElementColor.buffer.pos(right, bottom, 0.0).color(-16777216).endVertex();
        ElementColor.tessellator.draw();
        GlStateManager.disableBlend();
        GL11.glShadeModel(7425);
        GL11.glEnable(3553);
    }
    
    @Override
    public boolean isShown() {
        return this.setting.isVisible();
    }
    
    static {
        ElementColor.tessellator = Tessellator.getInstance();
        ElementColor.buffer = ElementColor.tessellator.getBuffer();
    }
}
