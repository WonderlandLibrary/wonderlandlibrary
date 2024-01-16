// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.button;

import ru.fluger.client.helpers.render.rect.RectHelper;
import java.awt.Color;
import org.lwjgl.input.Mouse;

public class ConfigGuiButton extends bja
{
    private int fade;
    
    public ConfigGuiButton(final int buttonId, final int x, final int y, final String buttonText) {
        this(buttonId, x, y, 200, 35, buttonText);
    }
    
    public ConfigGuiButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.fade = 20;
    }
    
    public static int getMouseX() {
        return Mouse.getX() * ConfigGuiButton.sr.a() / bib.z().d;
    }
    
    public static int getMouseY() {
        return ConfigGuiButton.sr.b() - Mouse.getY() * ConfigGuiButton.sr.b() / bib.z().e - 1;
    }
    
    @Override
    public void drawButton(final bib mc, final int mouseX, final int mouseY, final float mouseButton) {
        if (this.m) {
            final int height = this.g - 31;
            this.n = (mouseX >= this.h + 93 && mouseY >= this.i - 41 && mouseX < this.h + this.f - 43 && mouseY < height + this.i - 30);
            Color text = new Color(155, 155, 155, 255);
            if (this.l) {
                if (this.n) {
                    if (this.fade < 100) {
                        this.fade += 8;
                    }
                    text = Color.white;
                }
                else if (this.fade > 20) {
                    this.fade -= 8;
                }
            }
            bus.m();
            bus.a(770, 771, 1, 0);
            bus.b(770, 771);
            RectHelper.drawSkeetButton((float)(this.h + 125), (float)(this.i + 2), (float)(this.f + this.h - 75), (float)(height + this.i));
            mc.robotoRegularFontRender.drawCenteredString(this.j, this.h + this.f / 2.0f + 25.0f, this.i + (this.g - 72.5f), text.getRGB());
            this.a(mc, mouseX, mouseY);
        }
    }
    
    @Override
    protected void a(final bib mc, final int mouseX, final int mouseY) {
    }
    
    @Override
    public void a(final int mouseX, final int mouseY) {
    }
    
    @Override
    public boolean b(final bib mc, final int mouseX, final int mouseY) {
        final int height = this.g - 31;
        return this.l && this.m && mouseX >= this.h + 93 && mouseY >= this.i - 41 && mouseX < this.h + this.f - 43 && mouseY < height + this.i - 30;
    }
    
    @Override
    public boolean a() {
        return this.n;
    }
    
    @Override
    public void b(final int mouseX, final int mouseY) {
    }
    
    @Override
    public void a(final cho soundHandlerIn) {
        soundHandlerIn.a(cgp.a(qf.ic, 1.0f));
    }
    
    @Override
    public int b() {
        return this.f;
    }
    
    @Override
    public void a(final int width) {
        this.f = width;
    }
}
