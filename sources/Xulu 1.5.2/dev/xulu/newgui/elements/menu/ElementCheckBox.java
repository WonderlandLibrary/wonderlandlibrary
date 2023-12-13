// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.newgui.elements.menu;

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

public class ElementCheckBox extends Element
{
    public ElementCheckBox(final ModuleButton iparent, final Value iset) {
        this.parent = iparent;
        this.set = iset;
        super.setup();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (NewGui.toggleSetting.getValue().equalsIgnoreCase("Checkbox")) {
            Color temp = ColorUtil.getClickGUIColor();
            if (NewGui.rainbowgui.getValue()) {
                temp = new Color(Xulu.rgb).darker();
            }
            final int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 225).getRGB();
            Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), ColorUtils.changeAlpha(ColorUtils.Colors.BLACK, 60));
            if (NewGui.customfont.getValue()) {
                Xulu.cFontRenderer.drawStringWithShadow(this.setstrg, (float)(this.x + this.width - Xulu.cFontRenderer.getStringWidth(this.setstrg)), (float)(this.y + this.height / 2.0 - 1.5) - 3.0f, -1);
            }
            else {
                FontUtil.drawString(this.setstrg, this.x + this.width - FontUtil.getStringWidth(this.setstrg), this.y + FontUtil.getFontHeight() / 2 - 1.5, -1);
            }
            Gui.drawRect((int)(this.x + 1.0), (int)(this.y + 1.0), (int)(this.x + 11.0), (int)(this.y + 11.0), ((boolean)this.set.getValue()) ? color : ColorUtils.changeAlpha(-16777216, 150));
            if (this.isCheckHovered(mouseX, mouseY)) {
                Gui.drawRect((int)(this.x + 1.0), (int)(this.y + 1.0), (int)(this.x + 11.0), (int)(this.y + 11.0), ColorUtils.changeAlpha(1427181841, 30));
            }
        }
        else if (NewGui.toggleSetting.getValue().equalsIgnoreCase("Full-box")) {
            Color temp = ColorUtil.getClickGUIColor().darker();
            if (NewGui.rainbowgui.getValue()) {
                temp = new Color(Xulu.rgb).darker();
            }
            final int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 200).getRGB();
            Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), ((boolean)this.set.getValue()) ? ColorUtils.changeAlpha(color, 225) : ColorUtils.changeAlpha(ColorUtils.Colors.BLACK, 60));
            if (NewGui.customfont.getValue()) {
                Xulu.cFontRenderer.drawStringWithShadow(this.setstrg, (float)(this.x + this.width / 2.0 - Xulu.cFontRenderer.getStringWidth(this.setstrg) / 2), (float)(this.y + Xulu.cFontRenderer.getHeight() / 2.0f - 1.5), -1);
            }
            else {
                FontUtil.drawStringWithShadow(this.setstrg, this.x + this.width / 2.0 - FontUtil.getStringWidth(this.setstrg) / 2, this.y + FontUtil.getFontHeight() / 2 - 1.5, -1);
            }
            Gui.drawRect((int)this.x, (int)(this.y + this.height - 1.0), (int)(this.x + this.width), (int)(this.y + this.height), ColorUtils.changeAlpha(ColorUtils.Colors.BLACK, 30));
        }
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton == 0) {
            if (NewGui.toggleSetting.getValue().equalsIgnoreCase("Checkbox")) {
                if (!this.isCheckHovered(mouseX, mouseY)) {
                    return super.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
            else if (!this.isHovered(mouseX, mouseY)) {
                return super.mouseClicked(mouseX, mouseY, mouseButton);
            }
            this.set.setValue(!this.set.getValue());
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    public boolean isCheckHovered(final int mouseX, final int mouseY) {
        return mouseX >= this.x + 1.0 && mouseX <= this.x + 11.0 && mouseY >= this.y + 1.0 && mouseY <= this.y + 11.0;
    }
}
