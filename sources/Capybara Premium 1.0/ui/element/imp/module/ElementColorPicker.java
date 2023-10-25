package fun.expensive.client.ui.element.imp.module;


import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import ru.alone.setting.imp.SettingColor;
import ru.alone.ui.element.Element;
import ru.alone.utils.RenderUtils;
import ru.alone.utils.other.font.Fonts;
import ru.alone.ui.element.imp.panel.ElementSettings;

import java.awt.*;

public class ElementColorPicker extends Element {

    double viewColorWidth = 14;
    double viewColorHeight = 8;
    double viewColorx;
    public float selectedHue = 0.0F;
    public float selectionY = 0.0F;
    private final ElementSettings settings;
    private final SettingColor setting;
    private boolean extended;
    public boolean dragging;


    public ElementColorPicker(ElementSettings settings, SettingColor setting) {
        this.settings = settings;
        this.setting = setting;
        this.dragging = false;
    }

    @Override
    public void render(int width, int height, int x, int y, float ticks) {
        this.height = 16;
        this.setWidth(this.settings.getWidth());
        this.viewColorx = this.width - (viewColorWidth * 2) - viewColorWidth + 8;
        boolean collided = collided(x, y, this.x + viewColorx + viewColorWidth, this.y + viewColorHeight / 2, (float) viewColorWidth + 6, (float) viewColorWidth);

        String title = setting.getName() + ":";
        Fonts.Monstserrat.drawString(title, (int) this.x, (int) this.y + 5, new Color(0x7C7C7C).getRGB(), false);
        RenderUtils.drawBlurredShadow((float) (this.x + viewColorx + viewColorWidth), (float) (this.y + viewColorHeight / 2), 14, 8, 8, setting.color);
        Fonts.Monstserrat.drawString("1", (float) (this.x + viewColorx + viewColorWidth)+2, (float) (this.y + viewColorHeight / 2) + 0.5f, -1);

        RenderUtils.drawRoundedRect((int) (this.x + viewColorx + viewColorWidth), (float) (this.y + viewColorHeight / 2), (int) ((this.x + viewColorx + viewColorWidth) + viewColorWidth), (float) ((this.y + viewColorHeight / 2) + viewColorHeight), 1.5f, setting.color.getRGB());

        if (collided) {
            String hexcolor = "#" + Integer.toHexString(setting.color.getRGB()).substring(2);
            int hexcolor_width = Fonts.Monstserrat.getStringWidth(hexcolor);
            RenderUtils.drawRect(x + 6, y - 3, x + 6 + hexcolor_width + 1, y + 10, Integer.MIN_VALUE);
            Fonts.Monstserrat.drawString(hexcolor, x + 6, y, -1);
        }


        for (int pickerX = 0; (double) pickerX < this.width; ++pickerX) {

            for (int pickerY = 0; (double) pickerY < this.height / 2.5D; ++pickerY) {
                float[] floatcolor = HSVtoRGB(map((float) pickerX, 0.0F, (float) this.width, 0.0F, 360.0F), 0.9F, 1.0F);
                Color c = new Color(floatcolor[0], floatcolor[1], floatcolor[2]);
                if (extended) {
                    RenderUtils.drawRect(this.x + (float) pickerX, this.y + 57 + (float) pickerY, this.x + (float) pickerX + 1.0F, this.y + 57 + (float) pickerY + 1.0F, c.getRGB());
                }
            }
        }
        if (extended) {
            float pickerX = (float) (this.x );
            float pickerY = (float) (this.y + 15);
            float pickerWidth = (float) (this.width );
            float pickerHeight = 40;

            // GL shit pt 1
            GlStateManager.pushMatrix();
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.shadeModel(7425);

            BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();

            // Add positions
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(pickerX + pickerWidth, pickerY, 0).color(setting.color.getRed(), setting.color.getGreen(), setting.color.getBlue(), 255).endVertex();
            bufferbuilder.pos(pickerX, pickerY, 0).color(255, 255, 255, 255).endVertex();
            bufferbuilder.pos(pickerX, pickerY + pickerHeight, 0).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(pickerX + pickerWidth, pickerY + pickerHeight, 0).color(0, 0, 0, 255).endVertex();

            // Draw rect
            Tessellator.getInstance().draw();

            // GL shit pt 2
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
            GlStateManager.popMatrix();
        }
        if (extended) {
            this.setHeight(65);

        } else {
            this.setHeight(15);

        }
        super.render(width, height, x, y, ticks);
    }


    @Override
    public void mouseClicked(int x, int y, int button) {
        if (collided(x, y, this.x + viewColorx + viewColorWidth, this.y + viewColorHeight / 2, (float) viewColorWidth, (float) viewColorWidth)) {
            this.extended = !this.extended;
        }
        if (extended && collided(x, y, this.x, this.y + 57, (float) (this.width), (float) (5))) {
            x = (int) ((double) x - this.x);
            this.selectionY = (float) x;
            if (this.selectionY >= 140.0F) {
                this.selectionY = 140.0F;
                return;
            }

            if (this.selectionY <= -1.0F) {
                this.selectionY = 0.0F;
                return;
            }

            this.selectedHue = map((float) x, 0.0F, (float) this.width, 0.0F, 360.0F);
            float[] hsv = HSVtoRGB(this.getSelectedHue(), 1.0F, 1.0F);
            Color current = new Color(hsv[0], hsv[1], hsv[2]);
            setting.set(current);

        }
        super.mouseClicked(x, y, button);
    }

    public static float map(float val, float minVal, float maxVal, float minTarget, float maxTarget) {
        return (val - minVal) / (maxVal - minVal) * (maxTarget - minTarget) + minTarget;
    }

    public static float[] HSVtoRGB(float hue, float saturation, float value) {
        float chroma = value * saturation;
        float hue1 = hue / 60.0F;
        float x = chroma * (1.0F - Math.abs(hue1 % 2.0F - 1.0F));
        float r1 = 0.0F;
        float g1 = 0.0F;
        float b1 = 0.0F;
        if (hue1 >= 0.0F && hue1 <= 1.0F) {
            r1 = chroma;
            g1 = x;
            b1 = 0.0F;
        } else if (hue1 >= 1.0F && hue1 <= 2.0F) {
            r1 = x;
            g1 = chroma;
            b1 = 0.0F;
        } else if (hue1 >= 2.0F && hue1 <= 3.0F) {
            r1 = 0.0F;
            g1 = chroma;
            b1 = x;
        } else if (hue1 >= 3.0F && hue1 <= 4.0F) {
            r1 = 0.0F;
            g1 = x;
            b1 = chroma;
        } else if (hue1 >= 4.0F && hue1 <= 5.0F) {
            r1 = x;
            g1 = 0.0F;
            b1 = chroma;
        } else if (hue1 >= 5.0F && hue1 <= 6.0F) {
            r1 = chroma;
            g1 = 0.0F;
            b1 = x;
        }

        float m = value - chroma;
        float r = r1 + m;
        float g = g1 + m;
        float b = b1 + m;
        return new float[]{r, g, b};
    }

    public float getSelectedHue() {
        return this.selectedHue;
    }

    @Override
    public void mouseRealesed(int x, int y, int button) {
        super.mouseRealesed(x, y, button);
    }

}
