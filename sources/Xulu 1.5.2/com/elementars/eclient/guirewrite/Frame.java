// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite;

import net.minecraft.client.gui.Gui;
import java.awt.Color;
import com.elementars.eclient.Xulu;
import net.minecraft.client.gui.ScaledResolution;
import com.elementars.eclient.util.Wrapper;
import com.elementars.eclient.module.ModuleManager;

public class Frame
{
    public String title;
    public double x;
    public double y;
    private double x2;
    private double y2;
    public double width;
    public double height;
    public boolean dragging;
    public boolean pinned;
    public boolean visible;
    public HUD hud;
    
    public Frame(final String ititle, final double ix, final double iy, final double iwidth, final double iheight, final boolean ipinned, final HUD hud) {
        this.title = ititle;
        this.x = ix;
        this.y = iy;
        this.width = iwidth;
        this.height = iheight;
        this.pinned = ipinned;
        this.dragging = false;
        this.visible = true;
        this.hud = hud;
        this.setup();
    }
    
    public void setup() {
    }
    
    int changeAlpha(int origColor, final int userInputedAlpha) {
        origColor &= 0xFFFFFF;
        return userInputedAlpha << 24 | origColor;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (!(this.visible = ModuleManager.isModuleEnabled(this.title))) {
            return;
        }
        if (this.dragging) {
            this.x = this.x2 + mouseX;
            this.y = this.y2 + mouseY;
            final ScaledResolution sr = new ScaledResolution(Wrapper.getMinecraft());
            if (this.x < 0.0) {
                this.x = 0.0;
            }
            if (this.y < 0.0) {
                this.y = 0.0;
            }
            if (this.x > sr.getScaledWidth() - this.width) {
                this.x = sr.getScaledWidth() - this.width;
            }
            if (this.y > sr.getScaledHeight() - this.height) {
                this.y = sr.getScaledHeight() - this.height;
            }
        }
        if (Xulu.MODULE_MANAGER.getModuleByName(this.title) != null) {
            ((Element)Xulu.MODULE_MANAGER.getModuleByName(this.title)).x = this.x;
            ((Element)Xulu.MODULE_MANAGER.getModuleByName(this.title)).y = this.y;
        }
        if (this.dragging) {
            Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), this.changeAlpha(Color.lightGray.getRGB(), 100));
            Gui.drawRect((int)(this.x + 4.0), (int)(this.y + 2.0), (int)(this.x + 4.3), (int)(this.y + this.height - 2.0), this.changeAlpha(Color.lightGray.getRGB(), 100));
        }
        else {
            Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), this.changeAlpha(-15592942, 100));
            Gui.drawRect((int)(this.x + 4.0), (int)(this.y + 2.0), (int)(this.x + 4.3), (int)(this.y + this.height - 2.0), this.changeAlpha(-5592406, 100));
        }
        if (Xulu.MODULE_MANAGER.getModuleByName(this.title) != null) {
            Xulu.MODULE_MANAGER.getModuleByName(this.title).onRender();
        }
    }
    
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (!this.visible) {
            return false;
        }
        if (mouseButton == 0 && this.isHovered(mouseX, mouseY)) {
            this.x2 = this.x - mouseX;
            this.y2 = this.y - mouseY;
            return this.dragging = true;
        }
        if (mouseButton == 2 && this.isHovered(mouseX, mouseY)) {
            ((Element)Xulu.MODULE_MANAGER.getModuleByName(this.title)).onMiddleClick();
        }
        return false;
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (!this.visible) {
            return;
        }
        if (state == 0) {
            this.dragging = false;
        }
    }
    
    public boolean isHovered(final int mouseX, final int mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
    }
}
