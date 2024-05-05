// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.button;

import ru.fluger.client.helpers.input.MouseHelper;
import ru.fluger.client.helpers.render.RenderHelper;
import java.awt.Color;
import ru.fluger.client.ui.clickgui.ClickGuiScreen;

public class ImageButton
{
    protected int height;
    protected String description;
    protected int width;
    protected bib mc;
    protected nf image;
    protected int target;
    protected int x;
    protected float ani;
    protected int y;
    public static boolean hoverShop;
    
    public ImageButton(final nf resourceLocation, final int x, final int y, final int width, final int height, final String description, final int target) {
        this.ani = 0.0f;
        this.image = resourceLocation;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.description = description;
        this.target = target;
        this.mc = bib.z();
    }
    
    protected void hoverAnimation(final int mouseX, final int mouseY) {
        if (this.isHovered(mouseX, mouseY)) {
            if (this.target == 228) {
                ImageButton.hoverShop = true;
            }
            if (this.ani < 5.0f) {
                this.ani += (float)(0.3 * bib.frameTime * 0.1);
            }
        }
        else if (this.ani > 0.0f) {
            this.ani -= (float)(0.2 * bib.frameTime * 0.1);
            ImageButton.hoverShop = false;
        }
    }
    
    public void onClick(final int mouseX, final int mouseY) {
        if (this.isHovered(mouseX, mouseY)) {
            if (this.target == 16) {
                this.mc.a(new ble(null, this.mc.t));
            }
            if (this.target == 15) {
                this.mc.a(new blc(null, this.mc.t, this.mc.Q()));
            }
            if (this.target == 14) {
                this.mc.n();
            }
            if (this.target == 19) {
                bib.z().a(ClickGuiScreen.oldScreen);
            }
            if (this.target == 32) {
                for (int i = 0; i < 46; ++i) {
                    if (this.mc.h.bx.a(i).e()) {
                        this.mc.c.a(this.mc.h.bx.d, i, 1, afw.e, this.mc.h);
                    }
                }
            }
        }
    }
    
    public void draw(final int mouseX, final int mouseY, final Color color) {
        bus.G();
        bus.l();
        this.hoverAnimation(mouseX, mouseY);
        if (this.ani > 0.0f) {
            RenderHelper.drawImage(this.image, this.x - this.ani, this.y - this.ani, this.width + 4 + this.ani * 2.0f, this.height + 4 + this.ani * 2.0f, Color.BLACK);
            RenderHelper.drawImage(this.image, this.x - this.ani, this.y - this.ani, this.width + this.ani * 2.0f, this.height + this.ani * 2.0f, this.isHovered(mouseX, mouseY) ? new Color(156, 156, 156, 255) : Color.WHITE);
            if (this.isHovered(mouseX, mouseY)) {
                this.mc.robotoRegularFontRender.drawStringWithShadow(this.description, this.x + this.width / 2.0f + this.mc.robotoRegularFontRender.getStringWidth(this.description) / 2.0f - ((this.target == 228) ? 35 : 6), this.y + this.height - 17, new Color(255, 255, 255, 255).getRGB());
            }
        }
        else {
            RenderHelper.drawImage(this.image, (float)this.x, (float)this.y, (float)(this.width + 3), (float)(this.height + 3), Color.BLACK);
            RenderHelper.drawImage(this.image, (float)this.x, (float)this.y, (float)this.width, (float)this.height, color);
        }
        bus.H();
    }
    
    protected boolean isHovered(final int mouseX, final int mouseY) {
        return MouseHelper.isHovered(this.x, this.y, this.x + this.width, this.y + this.height, mouseX, mouseY);
    }
}
