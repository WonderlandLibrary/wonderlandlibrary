// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.newgui.elements.menu;

import dev.xulu.newgui.util.FontUtil;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.module.render.NewGui;
import net.minecraft.client.gui.Gui;
import com.elementars.eclient.util.ColorUtils;
import java.awt.Color;
import dev.xulu.newgui.util.ColorUtil;
import dev.xulu.settings.Value;
import dev.xulu.newgui.elements.ModuleButton;
import dev.xulu.newgui.elements.Element;

public class ElementComboBox extends Element
{
    public ElementComboBox(final ModuleButton iparent, final Value iset) {
        this.parent = iparent;
        this.set = iset;
        super.setup();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final Color temp = ColorUtil.getClickGUIColor();
        final int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 150).getRGB();
        Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), ColorUtils.changeAlpha(ColorUtils.Colors.BLACK, 60));
        if (NewGui.customfont.getValue()) {
            Xulu.cFontRenderer.drawStringWithShadow(this.setstrg, (float)(this.x + 2.0), (float)this.y + 2.0f, -1);
        }
        else {
            FontUtil.drawStringWithShadow(this.setstrg, this.x + 2.0, this.y + 2.0, -1);
        }
        final int clr1 = color;
        final int clr2 = temp.getRGB();
        Gui.drawRect((int)this.x, (int)(this.y + this.height - 1.0), (int)(this.x + this.width), (int)(this.y + this.height), ColorUtils.changeAlpha(1996488704, 30));
        final String s = (this.set.getValue() instanceof String) ? this.set.getValue() : this.set.getValue().toString();
        if (NewGui.customfont.getValue()) {
            Xulu.cFontRenderer.drawStringWithShadow(s, (float)(this.x + 8.0 + Xulu.cFontRenderer.getStringWidth(this.setstrg)), (float)this.y + 2.0f, new Color(-1).darker().darker().getRGB());
        }
        else {
            FontUtil.drawStringWithShadow(s, this.x + 8.0 + FontUtil.getStringWidth(this.setstrg), this.y + 2.0, new Color(-1).darker().darker().getRGB());
        }
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        final String s = (this.set.getValue() instanceof String) ? this.set.getValue() : this.set.getValue().toString();
        if (mouseButton == 0 && this.isButtonHovered(mouseX, mouseY)) {
            try {
                if (!this.set.getCorrectString(s).equalsIgnoreCase(this.set.getOptions().get(this.set.getOptions().size() - 1).toString())) {
                    this.set.setValue(this.set.getOptions().get(this.set.getOptions().indexOf(this.set.getCorrectString(s)) + 1));
                }
                else {
                    this.set.setValue(this.set.getOptions().get(0));
                }
            }
            catch (Exception e) {
                System.err.println("Error with invalid combo");
                e.printStackTrace();
                this.set.setValue(this.set.getOptions().get(0));
            }
            return true;
        }
        if (mouseButton == 1 && this.isButtonHovered(mouseX, mouseY)) {
            try {
                if (this.set.getOptions().listIterator(this.set.getOptions().indexOf(this.set.getCorrectString(s))).hasPrevious()) {
                    this.set.setValue(this.set.getOptions().listIterator(this.set.getOptions().indexOf(this.set.getCorrectString(s))).previous());
                }
                else {
                    this.set.setValue(this.set.getOptions().get(this.set.getOptions().size() - 1));
                }
            }
            catch (Exception e) {
                System.err.println("Error with invalid combo");
                e.printStackTrace();
                this.set.setValue(this.set.getOptions().get(0));
            }
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    public boolean isButtonHovered(final int mouseX, final int mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
    }
}
