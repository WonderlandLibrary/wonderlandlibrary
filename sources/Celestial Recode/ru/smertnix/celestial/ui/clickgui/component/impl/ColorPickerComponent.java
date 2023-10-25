package ru.smertnix.celestial.ui.clickgui.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.feature.impl.hud.ClickGUI;
import ru.smertnix.celestial.ui.clickgui.Panel;
import ru.smertnix.celestial.ui.clickgui.component.Component;
import ru.smertnix.celestial.ui.clickgui.component.ExpandableComponent;
import ru.smertnix.celestial.ui.clickgui.component.PropertyComponent;
import ru.smertnix.celestial.ui.settings.Setting;
import ru.smertnix.celestial.ui.settings.impl.ColorSetting;
import ru.smertnix.celestial.utils.render.RenderUtils;
import ru.smertnix.celestial.utils.render.RoundedUtil;
import ru.smertnix.celestial.utils.render.StencilUtil;

import org.lwjgl.opengl.GL11;

import javafx.animation.Interpolator;

import java.awt.*;


public class ColorPickerComponent extends ExpandableComponent implements PropertyComponent {

    private static final int COLOR_PICKER_HEIGHT = 70;
    public static Tessellator tessellator = Tessellator.getInstance();
    public static BufferBuilder buffer = tessellator.getBuffer();
    private final ColorSetting colorSetting;
    private float hue;
    private float saturation;
    private float brightness;
    private float alpha;

    private boolean colorSelectorDragging;

    private boolean hueSelectorDragging;
    public float a;
    private boolean alphaSelectorDragging;
    Minecraft mc = Minecraft.getMinecraft();
    public ColorPickerComponent(Component parent, ColorSetting colorSetting, int x, int y, int width, int height) {
        super(parent, colorSetting.getName(), x, y, width, height);

        this.colorSetting = colorSetting;

        int value = colorSetting.getColorValue();
        float[] hsb = getHSBFromColor(value);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];

        this.alpha = (value >> 24 & 0xFF) / 255.0F;
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
    	ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        GlStateManager.pushMatrix();
    	GL11.glEnable(GL11.GL_SCISSOR_TEST);
    	RenderUtils.scissorRect(getX() - 10, 25.5f, sr.getScaledWidth(), 239);
        super.drawComponent(scaledResolution, mouseX, mouseY);

        int x = getX() - 5;
        int y = getY();
        int width = getWidth();
        int height = getHeight() + 5;

        float left = x + width - 13;
        float top = y + height / 2.0F - 2;
        float right = x + width - 2;
        float bottom = y + height / 2.0F + 2;

        int textColor = new Color(222,222,222).getRGB();

        mc.mntsb_15.drawString(getName() + ":", x + 2, y + height / 2F - 3, Celestial.instance.featureManager.getFeature(parent.getName()).isEnabled() ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
      
        
        //RenderUtils.drawSmoothRect(left, top, right, bottom, colorSetting.getColorValue());
        
        a = (float) Interpolator.LINEAR.interpolate(a, isExpanded() ? 13 : 0, 0.1f);
        
        
        RenderUtils.drawShadow(8,1, () -> {
        	   RoundedUtil.drawRound(x + width - 7 + 1 - 1, y + 6/*+ a*/ + 1, 9, 2, 1, new Color(colorSetting.getColorValue()));
        });
        
        RoundedUtil.drawRound(x + width - 7, y + 6 /*+ a*/, 9, 4, 1, new Color(colorSetting.getColorValue()));
        
        if (isExpanded()) {
        	float cpLeft = x + 2;
            float cpTop = y + height + 2 - 4;
            float cpRight = x + COLOR_PICKER_HEIGHT - 2 - 4;
            float cpBottom = y + height + COLOR_PICKER_HEIGHT - 2 - 4 - 4;


    		GlStateManager.pushMatrix();
    	    
    	  	StencilUtil.initStencilToWrite();
            RoundedUtil.drawRound(cpLeft + 2, cpTop + 2.0F, cpRight - 2 - cpLeft - 2, cpBottom - 2 - cpTop - 2, 4, new Color(10, 10, 10, 180));
            StencilUtil.readStencilBuffer(1);
            
            
            // Draw Background


            // Draw Color Picker
            {
                // Box with gradient

                if (mouseX <= cpLeft || mouseY <= cpTop || mouseX >= cpRight || mouseY >= cpBottom)
                    colorSelectorDragging = false;

                float colorSelectorX = saturation * (cpRight - cpLeft);
                float colorSelectorY = (1 - brightness) * (cpBottom - cpTop);

                if (colorSelectorDragging) {
                    float wWidth = cpRight - cpLeft;
                    float xDif = mouseX - cpLeft;
                    this.saturation = xDif / wWidth;
                    colorSelectorX = xDif;

                    float hHeight = cpBottom - cpTop;
                    float yDif = mouseY - cpTop;
                    this.brightness = 1 - (yDif / hHeight);
                    colorSelectorY = yDif;

                    updateColor(Color.HSBtoRGB(hue, saturation, brightness), false);
                }

                drawColorPickerRect(cpLeft + 0.5F, cpTop + 0.5F, cpRight - 0.5F, cpBottom - 0.5F);
                // Selector
                float selectorWidth = 2;
                float outlineWidth = 0.5F;
                float half = selectorWidth / 2;

                float csLeft = cpLeft + colorSelectorX - half;
                float csTop = cpTop + colorSelectorY - half;
                float csRight = cpLeft + colorSelectorX + half;
                float csBottom = cpTop + colorSelectorY + half;
                
            	StencilUtil.uninitStencilBuffer();
            	GlStateManager.popMatrix();
                RoundedUtil.drawRound(csLeft - outlineWidth,
                        csTop - outlineWidth,
                        4 + outlineWidth + outlineWidth,
                        4 + outlineWidth + outlineWidth,
                        2.0f,
                        new Color(0));
                RoundedUtil.drawRound(csLeft,
                        csTop,
                        4,
                        4,
                        1.9f,
                        new Color(-1));
            }
        	
            // Hue Slider
            {
                float sLeft = x + COLOR_PICKER_HEIGHT - 1 - 4;
                float sTop = y + height + 2 - 4;
                float sRight = sLeft + 5 + 4;
                float sBottom = y + height + COLOR_PICKER_HEIGHT - 2 - 4;

                if (mouseX <= sLeft || mouseY <= sTop || mouseX >= sRight || mouseY >= sBottom)
                    hueSelectorDragging = false;

                float hueSelectorY = this.hue * (sBottom - sTop);

                if (hueSelectorDragging) {
                    float hsHeight = sBottom - sTop;
                    float yDif = mouseY - sTop;
                    this.hue = yDif / hsHeight;
                    hueSelectorY = yDif;

                    updateColor(Color.HSBtoRGB(hue, saturation, brightness), false);
                }

                // Outline
                float inc = 0.2F;
                float times = 1 / inc;
                float sHeight = sBottom - sTop;
                float sY = sTop + 0.5F + 1;
                float size = sHeight / times - 1.2f;
                
                
                RenderUtils.drawShadow(10,1, () -> {
                	RenderUtils.drawRect(sLeft + 1, sTop + 2, sRight - 1, sBottom - 6, -1);
                });
                
                // Color
                for (int i = 0; i < times; i++) {
                    boolean last = i == times - 1;
                    if (last)
                        size--;
                    final float y2;
                    y2 = sY;
                    final float size2;
                    size2 = size;
                    final int i2;
                    i2 = i;
                    RenderUtils.drawGradientRect(sLeft + 0.5F, sY, sRight - 0.5F,
                            sY + size,
                            Color.HSBtoRGB(inc * i, 1.0F, 1.0F),
                            Color.HSBtoRGB(inc * (i + 1), 1.0F, 1.0F));
                    if (!last)
                        sY += size;
                }

                float selectorHeight = 2;
                float outlineWidth = 0.5F;
                float half = selectorHeight / 2;

                float csTop = sTop + hueSelectorY - half;
                float csBottom = sTop + hueSelectorY + half;

                Gui.drawRect(sLeft - outlineWidth,
                        csTop - outlineWidth,
                        sRight + outlineWidth,
                        csBottom + outlineWidth,
                        0xFF000000);
                Gui.drawRect(sLeft,
                        csTop,
                        sRight,
                        csBottom,
                        Color.WHITE.getRGB());
            }
            
            RenderUtils.drawShadow(10,1, () -> {
                RoundedUtil.drawRound(cpLeft + 2, cpTop + 2, cpRight - cpLeft - 4, cpBottom- cpTop - 4, 4, new Color(10, 10, 10, 180));
            });
            
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
  		GlStateManager.popMatrix();
    }

    private void drawCheckeredBackground(float x, float y, float right, float bottom) {
        RenderUtils.drawRect(x, y, right, bottom, -1);

        for (boolean off = false; y < bottom; y++)
            for (float x1 = x + ((off = !off) ? 1 : 0); x1 < right; x1 += 2)
                RenderUtils.drawRect(x1, y, x1 + 1, y + 1, 0xFF808080);
    }

    private void updateColor(int hex, boolean hasAlpha) {
        if (hasAlpha)
            colorSetting.setColorValue(hex);
        else {
            colorSetting.setColorValue(new Color(
                    hex >> 16 & 0xFF,
                    hex >> 8 & 0xFF,
                    hex & 0xFF,
                    (int) (alpha * 255)).getRGB());
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        super.onMouseClick(mouseX, mouseY, button);

        if (isExpanded()) {
            if (button == 0) {
                int x = getX();
                int y = getY();
                // Color Picker Dimensions
                float cpLeft = x + 2;
                float cpTop = y + getHeight() + 2 - 4;
                float cpRight = x + COLOR_PICKER_HEIGHT - 2 - 4;
                float cpBottom = y + getHeight() + COLOR_PICKER_HEIGHT - 2 - 4 - 4;
                // Hue Slider Dimensions
                float sLeft = x + COLOR_PICKER_HEIGHT - 1 - 10;
                float sTop = y + getHeight() + 2 - 4;
                float sRight = sLeft + 5 + 4;
                float sBottom = y + getHeight() + COLOR_PICKER_HEIGHT - 2 - 4;
                // Alpha Slider Dimensions
                float asLeft = x + COLOR_PICKER_HEIGHT + 6;
                float asTop = y + getHeight() + 2;
                float asRight = asLeft + 5;
                float asBottom = y + getHeight() + COLOR_PICKER_HEIGHT - 2;
                // If hovered over color picker
                colorSelectorDragging = !colorSelectorDragging && mouseX > cpLeft && mouseY > cpTop && mouseX < cpRight && mouseY < cpBottom;
                // If hovered over hue slider
                hueSelectorDragging = !hueSelectorDragging && mouseX > sLeft && mouseY > sTop && mouseX < sRight && mouseY < sBottom;
                // If hovered over alpha slider
                alphaSelectorDragging = !alphaSelectorDragging && mouseX > asLeft && mouseY > asTop && mouseX < asRight && mouseY < asBottom;
            }
        }
    }

    @Override
    public void onMouseRelease(int button) {
        if (hueSelectorDragging)
            hueSelectorDragging = false;
        else if (colorSelectorDragging)
            colorSelectorDragging = false;
        else if (alphaSelectorDragging)
            alphaSelectorDragging = false;
    }

    private float[] getHSBFromColor(int hex) {
        int r = hex >> 16 & 0xFF;
        int g = hex >> 8 & 0xFF;
        int b = hex & 0xFF;
        return Color.RGBtoHSB(r, g, b, null);
    }

    public void drawColorPickerRect(float left, float top, float right, float bottom) {
        int hueBasedColor = Color.HSBtoRGB(hue, 1, 1);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GlStateManager.enableBlend();
        GL11.glShadeModel(GL11.GL_SMOOTH);
        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(right, top, 0).color(hueBasedColor).endVertex();
        buffer.pos(left, top, 0).color(-1).endVertex();
        buffer.pos(left, bottom, 0).color(-1).endVertex();
        buffer.pos(right, bottom, 0).color(hueBasedColor).endVertex();
        tessellator.draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(right, top, 0).color(0x18000000).endVertex();
        buffer.pos(left, top, 0).color(0x18000000).endVertex();
        buffer.pos(left, bottom, 0).color(-16777216).endVertex();
        buffer.pos(right, bottom, 0).color(-16777216).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    @Override
    public boolean canExpand() {
        return true;
    }

    @Override
    public int getHeightWithExpand() {
        return getHeight() + COLOR_PICKER_HEIGHT;
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {

    }

    @Override
    public Setting getSetting() {
        return colorSetting;
    }
}
