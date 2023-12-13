// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.newgui.elements.menu;

import net.minecraft.util.math.MathHelper;
import dev.xulu.newgui.util.FontUtil;
import net.minecraft.client.gui.Gui;
import com.elementars.eclient.util.ColorUtils;
import java.awt.Color;
import com.elementars.eclient.Xulu;
import dev.xulu.newgui.util.ColorUtil;
import com.elementars.eclient.module.render.NewGui;
import dev.xulu.settings.Value;
import dev.xulu.newgui.elements.ModuleButton;
import dev.xulu.newgui.elements.Element;

public class ElementSlider extends Element
{
    public boolean dragging;
    
    public ElementSlider(final ModuleButton iparent, final Value iset) {
        this.parent = iparent;
        this.set = iset;
        this.dragging = false;
        super.setup();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (NewGui.sliderSetting.getValue().equalsIgnoreCase("Line")) {
            String displayval;
            if (this.set.getValue() instanceof Integer) {
                displayval = "" + Math.round(this.set.getValue() * 100.0) / 100.0;
            }
            else if (this.set.getValue() instanceof Short) {
                displayval = "" + Math.round(this.set.getValue() * 100.0) / 100.0;
            }
            else if (this.set.getValue() instanceof Long) {
                displayval = "" + Math.round(this.set.getValue() * 100.0) / 100.0;
            }
            else if (this.set.getValue() instanceof Float) {
                displayval = "" + Math.round(this.set.getValue() * 100.0) / 100.0;
            }
            else if (this.set.getValue() instanceof Double) {
                displayval = "" + Math.round(this.set.getValue() * 100.0) / 100.0;
            }
            else {
                displayval = "";
            }
            final boolean hoveredORdragged = this.isSliderHovered(mouseX, mouseY) || this.dragging;
            Color temp = ColorUtil.getClickGUIColor();
            if (NewGui.rainbowgui.getValue()) {
                temp = new Color(Xulu.rgb).darker();
            }
            final int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), hoveredORdragged ? 225 : 225).getRGB();
            final int color2 = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), hoveredORdragged ? 225 : 225).getRGB();
            double percentBar;
            if (this.set.getValue() instanceof Integer) {
                final double value = this.set.getValue();
                percentBar = (value - this.set.getMin()) / (this.set.getMax() - this.set.getMin());
            }
            else if (this.set.getValue() instanceof Short) {
                final double value = this.set.getValue();
                percentBar = (value - this.set.getMin()) / ((short)this.set.getMax() - (short)this.set.getMin());
            }
            else if (this.set.getValue() instanceof Long) {
                final double value = this.set.getValue();
                percentBar = (value - this.set.getMin()) / (this.set.getMax() - this.set.getMin());
            }
            else if (this.set.getValue() instanceof Float) {
                percentBar = (this.set.getValue() - this.set.getMin()) / (this.set.getMax() - this.set.getMin());
            }
            else if (this.set.getValue() instanceof Double) {
                percentBar = (this.set.getValue() - this.set.getMin()) / (this.set.getMax() - this.set.getMin());
            }
            else {
                percentBar = 0.0;
            }
            Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), ColorUtils.changeAlpha(ColorUtils.Colors.BLACK, 30));
            if (NewGui.customfont.getValue()) {
                Xulu.cFontRenderer.drawString(this.setstrg, (float)(this.x + 2.0), (float)(this.y + 2.0), -1);
                Xulu.cFontRenderer.drawString(displayval, (float)(this.x + this.width - Xulu.cFontRenderer.getStringWidth(displayval)), (float)(this.y + 2.0), -1);
            }
            else {
                FontUtil.drawString(this.setstrg, this.x + 2.0, this.y + 2.0, -1);
                FontUtil.drawString(displayval, this.x + this.width - FontUtil.getStringWidth(displayval), this.y + 2.0, -1);
            }
            Gui.drawRect((int)this.x, (int)(this.y + 12.0), (int)(this.x + this.width), (int)(this.y + 13.5), ColorUtils.changeAlpha(-15724528, 30));
            Gui.drawRect((int)this.x, (int)(this.y + 12.0), (int)(this.x + percentBar * this.width), (int)(this.y + 13.5), color);
            if (percentBar > 0.0 && percentBar < 1.0) {
                Gui.drawRect((int)(this.x + percentBar * this.width - 1.0), (int)(this.y + 12.0), (int)(this.x + Math.min(percentBar * this.width, this.width)), (int)(this.y + 13.5), color2);
            }
            if (this.dragging) {
                Double val;
                if (this.set.getValue() instanceof Integer) {
                    final int diff = this.set.getMax() - this.set.getMin();
                    val = this.set.getMin() + MathHelper.clamp((mouseX - this.x) / this.width, 0.0, 1.0) * diff;
                }
                else if (this.set.getValue() instanceof Short) {
                    final short diff2 = (short)((short)this.set.getMax() - (short)this.set.getMin());
                    val = this.set.getMin() + MathHelper.clamp((mouseX - this.x) / this.width, 0.0, 1.0) * diff2;
                }
                else if (this.set.getValue() instanceof Long) {
                    final long diff3 = this.set.getMax() - this.set.getMin();
                    val = this.set.getMin() + MathHelper.clamp((mouseX - this.x) / this.width, 0.0, 1.0) * diff3;
                }
                else if (this.set.getValue() instanceof Float) {
                    final float diff4 = this.set.getMax() - this.set.getMin();
                    val = this.set.getMin() + MathHelper.clamp((mouseX - this.x) / this.width, 0.0, 1.0) * diff4;
                }
                else if (this.set.getValue() instanceof Double) {
                    final double diff5 = this.set.getMax() - this.set.getMin();
                    val = this.set.getMin() + MathHelper.clamp((mouseX - this.x) / this.width, 0.0, 1.0) * diff5;
                }
                else {
                    val = 0.0;
                }
                Number type;
                if (this.set.getValue() instanceof Integer) {
                    type = val.intValue();
                }
                else if (this.set.getValue() instanceof Short) {
                    type = val.shortValue();
                }
                else if (this.set.getValue() instanceof Long) {
                    type = val.longValue();
                }
                else if (this.set.getValue() instanceof Float) {
                    type = val.floatValue();
                }
                else if (this.set.getValue() instanceof Double) {
                    type = val;
                }
                else {
                    type = 0;
                }
                this.set.setValue(type);
            }
        }
        else if (NewGui.sliderSetting.getValue().equalsIgnoreCase("Box")) {
            String displayval;
            if (this.set.getValue() instanceof Integer) {
                displayval = "" + Math.round(this.set.getValue() * 100.0) / 100.0;
            }
            else if (this.set.getValue() instanceof Short) {
                displayval = "" + Math.round(this.set.getValue() * 100.0) / 100.0;
            }
            else if (this.set.getValue() instanceof Long) {
                displayval = "" + Math.round(this.set.getValue() * 100.0) / 100.0;
            }
            else if (this.set.getValue() instanceof Float) {
                displayval = "" + Math.round(this.set.getValue() * 100.0) / 100.0;
            }
            else if (this.set.getValue() instanceof Double) {
                displayval = "" + Math.round(this.set.getValue() * 100.0) / 100.0;
            }
            else {
                displayval = "";
            }
            boolean b = false;
            Label_2169: {
                Label_2164: {
                    if (NewGui.sliderSetting.getValue().equalsIgnoreCase("Line")) {
                        if (this.isSliderHovered(mouseX, mouseY)) {
                            break Label_2164;
                        }
                    }
                    else if (this.isHovered(mouseX, mouseY)) {
                        break Label_2164;
                    }
                    if (!this.dragging) {
                        b = false;
                        break Label_2169;
                    }
                }
                b = true;
            }
            final boolean hoveredORdragged = b;
            Color temp = ColorUtil.getClickGUIColor().darker();
            if (NewGui.rainbowgui.getValue()) {
                temp = new Color(Xulu.rgb).darker();
            }
            final int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), hoveredORdragged ? 50 : 30).getRGB();
            final int color2 = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), hoveredORdragged ? 255 : 230).getRGB();
            double percentBar;
            if (this.set.getValue() instanceof Integer) {
                final double value = this.set.getValue();
                percentBar = (value - this.set.getMin()) / (this.set.getMax() - this.set.getMin());
            }
            else if (this.set.getValue() instanceof Short) {
                final double value = this.set.getValue();
                percentBar = (value - this.set.getMin()) / ((short)this.set.getMax() - (short)this.set.getMin());
            }
            else if (this.set.getValue() instanceof Long) {
                final double value = this.set.getValue();
                percentBar = (value - this.set.getMin()) / (this.set.getMax() - this.set.getMin());
            }
            else if (this.set.getValue() instanceof Float) {
                percentBar = (this.set.getValue() - this.set.getMin()) / (this.set.getMax() - this.set.getMin());
            }
            else if (this.set.getValue() instanceof Double) {
                percentBar = (this.set.getValue() - this.set.getMin()) / (this.set.getMax() - this.set.getMin());
            }
            else {
                percentBar = 0.0;
            }
            Gui.drawRect((int)(this.x + percentBar * this.width), (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), ColorUtils.changeAlpha(ColorUtils.Colors.BLACK, 60));
            Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + percentBar * this.width), (int)(this.y + this.height), ColorUtils.changeAlpha(color, 225));
            if (NewGui.customfont.getValue()) {
                Xulu.cFontRenderer.drawStringWithShadow(this.setstrg, (float)(this.x + 2.0), (float)(this.y + 2.0), -1);
                Xulu.cFontRenderer.drawStringWithShadow(displayval, (float)(this.x + this.width - Xulu.cFontRenderer.getStringWidth(displayval)), (float)(this.y + 2.0), -1);
            }
            else {
                FontUtil.drawStringWithShadow(this.setstrg, this.x + 2.0, this.y + 2.0, -1);
                FontUtil.drawStringWithShadow(displayval, this.x + this.width - FontUtil.getStringWidth(displayval), this.y + 2.0, -1);
            }
            if (this.dragging) {
                Double val;
                if (this.set.getValue() instanceof Integer) {
                    final int diff = this.set.getMax() - this.set.getMin();
                    val = this.set.getMin() + MathHelper.clamp((mouseX - this.x) / this.width, 0.0, 1.0) * diff;
                }
                else if (this.set.getValue() instanceof Short) {
                    final short diff2 = (short)((short)this.set.getMax() - (short)this.set.getMin());
                    val = this.set.getMin() + MathHelper.clamp((mouseX - this.x) / this.width, 0.0, 1.0) * diff2;
                }
                else if (this.set.getValue() instanceof Long) {
                    final long diff3 = this.set.getMax() - this.set.getMin();
                    val = this.set.getMin() + MathHelper.clamp((mouseX - this.x) / this.width, 0.0, 1.0) * diff3;
                }
                else if (this.set.getValue() instanceof Float) {
                    final float diff4 = this.set.getMax() - this.set.getMin();
                    val = this.set.getMin() + MathHelper.clamp((mouseX - this.x) / this.width, 0.0, 1.0) * diff4;
                }
                else if (this.set.getValue() instanceof Double) {
                    final double diff5 = this.set.getMax() - this.set.getMin();
                    val = this.set.getMin() + MathHelper.clamp((mouseX - this.x) / this.width, 0.0, 1.0) * diff5;
                }
                else {
                    val = 0.0;
                }
                Number type;
                if (this.set.getValue() instanceof Integer) {
                    type = val.intValue();
                }
                else if (this.set.getValue() instanceof Short) {
                    type = val.shortValue();
                }
                else if (this.set.getValue() instanceof Long) {
                    type = val.longValue();
                }
                else if (this.set.getValue() instanceof Float) {
                    type = val.floatValue();
                }
                else if (this.set.getValue() instanceof Double) {
                    type = val;
                }
                else {
                    type = 0;
                }
                this.set.setValue(type);
            }
        }
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton == 0) {
            if (NewGui.sliderSetting.getValue().equalsIgnoreCase("Line")) {
                if (!this.isSliderHovered(mouseX, mouseY)) {
                    return super.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
            else if (!this.isHovered(mouseX, mouseY)) {
                return super.mouseClicked(mouseX, mouseY, mouseButton);
            }
            return this.dragging = true;
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.dragging = false;
    }
    
    public boolean isSliderHovered(final int mouseX, final int mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y + 11.0 && mouseY <= this.y + 14.0;
    }
}
