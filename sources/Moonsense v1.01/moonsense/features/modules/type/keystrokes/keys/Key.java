// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.keystrokes.keys;

import moonsense.utils.ColorObject;
import java.awt.Color;
import org.lwjgl.input.Mouse;
import net.minecraft.client.renderer.WorldRenderer;
import moonsense.ui.utils.GuiUtils;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import moonsense.features.modules.type.keystrokes.KeystrokesModule;
import net.minecraft.client.settings.KeyBinding;

public class Key
{
    private final int gapSize;
    protected final KeyBinding keyBinding;
    protected final KeystrokesModule keystrokesModule;
    protected long pressTime;
    private boolean wasPressed;
    private float faded;
    
    public void render(final boolean dummy) {
        final boolean pressed = !dummy && this.pressed();
        final float pressModifier = Math.min(1.0f, (float)((System.currentTimeMillis() - this.pressTime) / this.keystrokesModule.boxFade.getInt()));
        final float brightness = (pressed ? pressModifier : (1.0f - pressModifier)) * 0.8f;
        this.renderBackground(pressed, brightness);
        final String keyname = Keyboard.getKeyName(this.keyBinding.getKeyCode());
        final String keyType = this.keystrokesModule.keyMode.getValue().get(this.keystrokesModule.keyMode.getInt() + 1);
        if (keyType.equals("Letter")) {
            this.drawString(keyname, (this.getWidth() - Minecraft.getMinecraft().fontRendererObj.getStringWidth(keyname)) / 2.0f, (this.getHeight() - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT) / 2.0f + 1.0f, pressed);
        }
        else {
            if (keyname.equals("W")) {
                this.drawString("\u00e2\u2013²", (this.getWidth() - Minecraft.getMinecraft().fontRendererObj.getStringWidth(keyname)) / 2.0f + 1.5f, (this.getHeight() - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT) / 2.0f + 1.0f, pressed);
            }
            if (keyname.equals("A")) {
                this.drawString("\u00e2\u2014\u20ac", (this.getWidth() - Minecraft.getMinecraft().fontRendererObj.getStringWidth(keyname)) / 2.0f + 1.5f, (this.getHeight() - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT) / 2.0f + 1.0f, pressed);
            }
            if (keyname.equals("S")) {
                this.drawString("\u00e2\u2013¼", (this.getWidth() - Minecraft.getMinecraft().fontRendererObj.getStringWidth(keyname)) / 2.0f + 1.5f, (this.getHeight() - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT) / 2.0f + 1.0f, pressed);
            }
            if (keyname.equals("D")) {
                this.drawString("\u00e2\u2013¶", (this.getWidth() - Minecraft.getMinecraft().fontRendererObj.getStringWidth(keyname)) / 2.0f + 1.5f, (this.getHeight() - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT) / 2.0f + 1.0f, pressed);
            }
        }
    }
    
    public void renderBackground(final boolean pressed, final float brightness) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        this.setBackgroundColor(pressed, brightness);
        worldRenderer.startDrawing(7);
        worldRenderer.addVertex(0.0, this.getHeight(), 0.0);
        worldRenderer.addVertex(this.getWidth(), this.getHeight(), 0.0);
        worldRenderer.addVertex(this.getWidth(), 0.0, 0.0);
        worldRenderer.addVertex(0.0, 0.0, 0.0);
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        if (this.keystrokesModule.outline.getBoolean()) {
            final int color = this.getColor(this.keystrokesModule.outlineColor.getColorObject(), this.keystrokesModule.offset, pressed);
            final int color2 = this.getColor(this.keystrokesModule.outlineColor.getColorObject(), this.keystrokesModule.offset + this.getWidth(), pressed);
            GuiUtils.drawGradientRect(0.0f, 0.0f, this.getWidth(), 1.0f, color, color2, color, color2, 0);
            GuiUtils.drawGradientRect(this.getWidth() - 1.0f, 1.0f, this.getWidth(), this.getHeight() - 1.0f, color2, color2, color2, color2, 0);
            GuiUtils.drawGradientRect(this.getWidth(), this.getHeight(), 0.0f, this.getHeight() - 1.0f, color2, color, color2, color, 0);
            GuiUtils.drawGradientRect(1.0f, this.getHeight() - 1.0f, 0.0f, 1.0f, color, color, color, color, 0);
        }
    }
    
    public float getWidth() {
        return this.keystrokesModule.boxSize.getFloat();
    }
    
    public float getHeight() {
        return this.keystrokesModule.boxSize.getFloat();
    }
    
    protected boolean pressed() {
        final int keycode = this.keyBinding.getKeyCode();
        final boolean pressed = (keycode < 0) ? Mouse.isButtonDown(keycode + 100) : Keyboard.isKeyDown(keycode);
        if (this.wasPressed != pressed) {
            this.pressTime = System.currentTimeMillis();
        }
        if (this.keystrokesModule.doFade.getBoolean()) {
            this.faded = (float)((System.currentTimeMillis() - this.pressTime) / this.keystrokesModule.boxFade.getInt());
        }
        else {
            this.faded = (float)((System.currentTimeMillis() - this.pressTime) / 1L);
        }
        return this.wasPressed = pressed;
    }
    
    private void setBackgroundColor(final boolean pressed, final float brightness) {
        int bgColor = 0;
        int bgPressed = 0;
        final int converted = GuiUtils.convertPercentToValue(brightness);
        if (this.keystrokesModule.boxColor.getColor() != 0) {
            bgColor = this.keystrokesModule.boxColor.getColor();
        }
        if (this.keystrokesModule.boxPressedColor.getColor() != 0) {
            bgPressed = this.keystrokesModule.boxPressedColor.getColor();
        }
        if (bgColor == 0) {
            bgColor = new Color(converted, converted, converted, 102).getRGB();
        }
        if (bgPressed == 0) {
            bgPressed = new Color(converted, converted, converted, 102).getRGB();
        }
        final int bg = pressed ? bgPressed : bgColor;
        if (this.faded < 1.0f) {
            final int lastColor = pressed ? bgColor : bgPressed;
            GuiUtils.setGlColor(GuiUtils.getIntermediateColor(bg, lastColor, this.faded));
        }
        else {
            GuiUtils.setGlColor(bg);
        }
    }
    
    protected int getColor(final ColorObject colorObject, final float offset, final boolean invert) {
        if (!colorObject.isChroma()) {
            return colorObject.getColor();
        }
        if (invert) {
            return -16777216;
        }
        final float v = this.keystrokesModule.textColor.getColorObject().getActualChromaSpeed();
        final int i = new Color(Color.HSBtoRGB((float)((System.currentTimeMillis() - offset * 10.0 - this.keystrokesModule.offset * 10.0) % v) / v, 0.8f, 0.8f)).getRGB();
        return i;
    }
    
    protected void drawString(final String text, float x, final float y, final boolean invert) {
        ColorObject colorObject = this.keystrokesModule.textColor.getColorObject();
        if (this.pressed()) {
            colorObject = this.keystrokesModule.pressedTextColor.getColorObject();
        }
        if (colorObject.isChroma()) {
            if (invert) {
                GuiUtils.drawString(text, x, y, -16777216);
            }
            else {
                char[] charArray;
                for (int length = (charArray = text.toCharArray()).length, j = 0; j < length; ++j) {
                    final char c = charArray[j];
                    final float v = this.keystrokesModule.textColor.getColorObject().getActualChromaSpeed();
                    final int i = new Color(Color.HSBtoRGB((float)((System.currentTimeMillis() - x * 10.0 - this.keystrokesModule.offset * 10.0) % v) / v, 0.8f, 0.8f)).getRGB();
                    final String tmp = String.valueOf(c);
                    GuiUtils.drawString(tmp, x, y, i);
                    x += Minecraft.getMinecraft().fontRendererObj.getStringWidth(tmp);
                }
            }
        }
        else {
            GuiUtils.drawString(text, x, y, colorObject.getColor());
        }
    }
    
    public Key(final int gapSize, final KeyBinding keyBinding, final KeystrokesModule keystrokesModule) {
        this.gapSize = gapSize;
        this.keyBinding = keyBinding;
        this.keystrokesModule = keystrokesModule;
    }
}
