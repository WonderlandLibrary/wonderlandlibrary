// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.button;

import ru.fluger.client.helpers.render.rect.RectHelper;
import java.awt.Color;
import org.lwjgl.input.Mouse;

public class GuiMainMenuButton extends bja
{
    private int fade;
    
    public GuiMainMenuButton(final int buttonId, final int x, final int y, final String buttonText) {
        this(buttonId, x, y, 200, 35, buttonText);
    }
    
    public GuiMainMenuButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.fade = 20;
    }
    
    public static int getMouseX() {
        return Mouse.getX() * GuiMainMenuButton.sr.a() / bib.z().d;
    }
    
    public static int getMouseY() {
        return GuiMainMenuButton.sr.b() - Mouse.getY() * GuiMainMenuButton.sr.b() / bib.z().e - 1;
    }
    
    @Override
    public void drawButton(final bib mc, final int mouseX, final int mouseY, final float mouseButton) {
        if (this.m) {
            this.n = (mouseX >= this.h && mouseY >= this.i && mouseX < this.h + this.f && mouseY < this.i + this.g + 10);
            Color text = new Color(155, 155, 155, 255);
            final Color color = new Color(this.fade + 14, this.fade + 14, this.fade + 14, 200);
            if (this.n) {
                if (this.fade < 100) {
                    this.fade += 8;
                }
                text = Color.white;
            }
            else if (this.fade > 20) {
                this.fade -= 8;
            }
            bus.m();
            bus.a(770, 771, 1, 0);
            bus.b(770, 771);
            final int height = this.g + 11;
            RectHelper.drawSmoothRect((float)this.h, (float)this.i, this.f + this.h, height + this.i, color.getRGB());
            mc.bigButtonFontRender.drawCenteredString(this.j, this.h + this.f / 2.0f, (float)(this.i + (this.g - 12)), text.getRGB());
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
        return this.l && this.m && mouseX >= this.h && mouseY >= this.i && mouseX < this.h + this.f && mouseY < this.i + this.g + 10;
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
