// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.newgui;

import java.util.Iterator;
import dev.xulu.newgui.elements.Element;
import dev.xulu.newgui.util.FontUtil;
import com.elementars.eclient.util.XuluTessellator;
import net.minecraft.client.gui.Gui;
import com.elementars.eclient.util.ColorUtils;
import dev.xulu.newgui.util.ColorUtil;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.module.core.Global;
import com.elementars.eclient.module.render.NewGui;
import java.awt.Color;
import dev.xulu.newgui.elements.ModuleButton;
import java.util.ArrayList;

public class Panel
{
    public String title;
    public double x;
    public double y;
    private double x2;
    private double y2;
    public double width;
    public double height;
    public boolean dragging;
    public boolean extended;
    public boolean visible;
    public ArrayList<ModuleButton> Elements;
    public NewGUI clickgui;
    public int rgb;
    
    public Panel(final String ititle, final double ix, final double iy, final double iwidth, final double iheight, final boolean iextended, final NewGUI parent) {
        this.Elements = new ArrayList<ModuleButton>();
        this.title = ititle;
        this.x = ix;
        this.y = iy;
        this.width = iwidth;
        this.height = iheight;
        this.extended = iextended;
        this.dragging = false;
        this.visible = true;
        this.clickgui = parent;
        this.setup();
    }
    
    public void setup() {
    }
    
    public int updateRainbow(final int IN) {
        float hue2 = Color.RGBtoHSB(new Color(IN).getRed(), new Color(IN).getGreen(), new Color(IN).getBlue(), null)[0];
        hue2 += NewGui.rainbowspeed.getValue() / 1000.0f;
        if (hue2 > 1.0f) {
            --hue2;
        }
        return Color.HSBtoRGB(hue2, Global.rainbowSaturation.getValue() / 255.0f, Global.rainbowLightness.getValue() / 255.0f);
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (!this.visible) {
            return;
        }
        if (this.dragging) {
            this.x = this.x2 + mouseX;
            this.y = this.y2 + mouseY;
        }
        this.rgb = Xulu.rgb;
        final Color temp = ColorUtil.getClickGUIColor();
        int outlineColor = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 200).getRGB();
        if (NewGui.rainbowgui.getValue()) {
            outlineColor = ColorUtils.changeAlpha(this.rgb, 200);
        }
        int trueOutline = ColorUtils.changeAlpha(outlineColor, 225);
        Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), ColorUtils.changeAlpha(ColorUtils.Colors.BLACK, 225));
        Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), outlineColor);
        Gui.drawRect((int)(this.x + 4.0), (int)(this.y + 2.0), (int)(this.x + 4.3), (int)(this.y + this.height - 2.0), -5592406);
        if (this.extended) {
            Gui.drawRect((int)this.x, (int)(this.y + this.height - 1.0), (int)(this.x + this.width), (int)(this.y + this.height), ColorUtils.changeAlpha(ColorUtils.Colors.BLACK, 30));
        }
        if (NewGui.outline.getValue()) {
            if (this.extended) {
                XuluTessellator.drawRectOutline((int)this.x - 1, (int)this.y - 1, (int)(this.x + this.width) + 1, (int)(this.y + this.height), (int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), trueOutline);
            }
            else {
                XuluTessellator.drawRectOutline((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), 1.0, trueOutline);
            }
        }
        if (NewGui.customfont.getValue()) {
            Xulu.cFontRenderer.drawStringWithShadow(this.title, (float)(this.x + 4.0), (float)(this.y + this.height / 2.0 - 4.0), -1052689);
        }
        else {
            FontUtil.drawStringWithShadow(this.title, this.x + 4.0, this.y + this.height / 2.0 - 4.0, -1052689);
        }
        if (this.extended && !this.Elements.isEmpty()) {
            double startY = this.y + this.height;
            final int epanelcolor = ColorUtils.changeAlpha(ColorUtils.Colors.BLACK, NewGui.bgAlpha.getValue());
            final int mcolor = ColorUtils.changeAlpha(ColorUtil.getClickGUIColor().getRGB(), 30);
            for (final ModuleButton et : this.Elements) {
                if (NewGui.rainbowgui.getValue()) {
                    final int updateRainbow = this.updateRainbow(this.rgb);
                    this.rgb = updateRainbow;
                    trueOutline = ColorUtils.changeAlpha(updateRainbow, 225);
                }
                Gui.drawRect((int)this.x, (int)startY, (int)(this.x + this.width), (int)(startY + et.height + 1.0), epanelcolor);
                if (NewGui.outline.getValue()) {
                    if (this.Elements.indexOf(et) == this.Elements.size() - 1 && !et.extended) {
                        XuluTessellator.drawRectOutline((int)this.x - 1, (int)startY, (int)(this.x + this.width) + 1, (int)(startY + et.height + 1.0) + 1, (int)this.x, (int)startY, (int)(this.x + this.width), (int)(startY + et.height + 1.0), trueOutline);
                    }
                    else {
                        XuluTessellator.drawRectOutline((int)this.x - 1, (int)startY, (int)(this.x + this.width) + 1, (int)(startY + et.height + 1.0), (int)this.x, (int)startY, (int)(this.x + this.width), (int)(startY + et.height + 1.0), trueOutline);
                    }
                }
                if (NewGui.moduleSetting.getValue().equalsIgnoreCase("MiniButton")) {
                    Gui.drawRect((int)this.x + 2, (int)startY + 1, (int)(this.x + this.width) - 2, (int)(startY + et.height), mcolor);
                }
                et.x = this.x + 2.0;
                et.y = startY;
                et.width = this.width - 4.0;
                et.drawScreen(mouseX, mouseY, partialTicks);
                startY += et.height + 1.0;
                if (et.extended) {
                    for (final Element e : et.menuelements) {
                        if (!e.set.isVisible()) {
                            continue;
                        }
                        Gui.drawRect((int)this.x, (int)startY, (int)(this.x + this.width), (int)(startY + et.height + 1.0), epanelcolor);
                        if (NewGui.outline.getValue()) {
                            if (this.Elements.indexOf(et) == this.Elements.size() - 1 && et.menuelements.indexOf(e) == et.menuelements.size() - 1) {
                                XuluTessellator.drawRectOutline((int)this.x - 1, (int)startY, (int)(this.x + this.width) + 1, (int)(startY + et.height + 1.0) + 1, (int)this.x, (int)startY, (int)(this.x + this.width), (int)(startY + et.height + 1.0), trueOutline);
                            }
                            else {
                                XuluTessellator.drawRectOutline((int)this.x - 1, (int)startY, (int)(this.x + this.width) + 1, (int)(startY + et.height + 1.0), (int)this.x, (int)startY, (int)(this.x + this.width), (int)(startY + et.height + 1.0), trueOutline);
                            }
                        }
                        startY += et.height + 1.0;
                    }
                }
            }
            Gui.drawRect((int)this.x, (int)(startY + 1.0), (int)(this.x + this.width), (int)(startY + 1.0), epanelcolor);
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
        if (mouseButton == 1 && this.isHovered(mouseX, mouseY)) {
            this.extended = !this.extended;
            return true;
        }
        if (this.extended) {
            for (final ModuleButton et : this.Elements) {
                if (et.mouseClicked(mouseX, mouseY, mouseButton)) {
                    return true;
                }
            }
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
