// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.font;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventChangeGuiScale;
import net.lenni0451.eventapi.manager.EventManager;
import java.awt.Font;
import net.augustus.utils.interfaces.MC;

public class CustomFontUtil implements MC
{
    private UnicodeFontRenderer fontRenderer;
    private final String fontName;
    private float fontHeight;
    private Font font;
    private int size;
    
    public CustomFontUtil(final String FontName, final int size) {
        this.fontHeight = 0.0f;
        this.fontName = FontName;
        this.size = size;
        this.font = new Font(FontName, 0, size);
        this.fontRenderer = new UnicodeFontRenderer(this.font);
        this.fontHeight = (float)this.fontRenderer.getStringHeight("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqurstuvwxyz1234567890");
        EventManager.register((Object)this);
    }
    
    public CustomFontUtil(final String fontName, final Font font) {
        this.fontHeight = 0.0f;
        this.fontName = fontName;
        this.size = font.getSize();
        this.font = font;
        this.fontRenderer = new UnicodeFontRenderer(font);
        this.fontHeight = (float)this.fontRenderer.getStringHeight("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqurstuvwxyz1234567890");
        EventManager.register((Object)this);
    }
    
    @EventTarget
    public void onEventChangeGuiScale(final EventChangeGuiScale eventChangeGuiScale) {
        this.fontRenderer = new UnicodeFontRenderer(this.font);
        this.fontHeight = (float)this.fontRenderer.getStringHeight("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqurstuvwxyz1234567890");
    }
    
    public void drawString(final String text, final double x, final double y, final int color) {
        this.fontRenderer.drawString(text, (float)x, (float)y, color);
    }
    
    public void drawStringWithShadow(final String text, final double x, final double y, final int color) {
        this.fontRenderer.drawStringWithShadow(text, (float)x, (float)y, color);
    }
    
    public void drawStringXCenter(final String text, final double x, final double y, final double width, final int color) {
        this.fontRenderer.drawString(text, (float)(x + width / 2.0 - this.fontRenderer.getStringWidth(text) / 2.0f), (float)y, color);
    }
    
    public void drawStringYCenter(final String text, final double x, final double y, final double height, final int color) {
        this.fontRenderer.drawString(text, (float)x, (float)(y + height / 2.0 - this.fontRenderer.getStringHeight(text) / 2.0f), color);
    }
    
    public void drawStringYCenterMax(final String text, final double x, final double y, final double height, final int color) {
        this.fontRenderer.drawString(text, (float)x, (float)(y + height / 2.0 - this.fontRenderer.getStringHeight("Dj") / 2.0f), color);
    }
    
    public void drawStringTotalCenter(final String text, final double x, final double y, final double width, final double height, final int color) {
        this.fontRenderer.drawString(text, (float)(x + width / 2.0 - this.fontRenderer.getStringWidth(text) / 2.0f), (float)(y + height / 2.0 - this.fontRenderer.getStringHeight(text) / 2.0f), color);
    }
    
    public void drawStringTotalCenterWithShadow(final String text, final double x, final double y, final double width, final double height, final int color) {
        this.fontRenderer.drawStringWithShadow(text, (float)(x + width / 2.0 - this.fontRenderer.getStringWidth(text) / 2.0f), (float)(y + height / 2.0 - this.fontRenderer.getStringHeight(text) / 2.0f), color);
    }
    
    public float getStringWidth(final String text) {
        return (float)this.fontRenderer.getStringWidth(text);
    }
    
    public float getHeight() {
        return this.fontHeight;
    }
    
    public float getHeight(final String string) {
        return (float)this.fontRenderer.getStringHeight(string);
    }
    
    public String getFontName() {
        return this.fontName;
    }
}
