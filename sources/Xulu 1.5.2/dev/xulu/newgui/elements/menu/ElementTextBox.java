// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.newgui.elements.menu;

import net.minecraft.util.ChatAllowedCharacters;
import dev.xulu.newgui.util.FontUtil;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.module.render.NewGui;
import net.minecraft.client.gui.Gui;
import com.elementars.eclient.util.ColorUtils;
import java.awt.Color;
import dev.xulu.newgui.util.ColorUtil;
import dev.xulu.settings.TextBox;
import dev.xulu.settings.Value;
import dev.xulu.newgui.elements.ModuleButton;
import com.elementars.eclient.util.Timer;
import dev.xulu.newgui.elements.Element;

public class ElementTextBox extends Element
{
    private boolean listening;
    private Timer timer;
    private boolean showCursor;
    private CurrentString currentString;
    
    public ElementTextBox(final ModuleButton iparent, final Value iset) {
        this.timer = new Timer();
        this.currentString = new CurrentString("");
        this.parent = iparent;
        this.set = iset;
        this.currentString = new CurrentString(iset.getValue().getText());
        super.setup();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final Color temp = ColorUtil.getClickGUIColor();
        final int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 150).getRGB();
        Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), ColorUtils.changeAlpha(ColorUtils.Colors.BLACK, 60));
        Gui.drawRect((int)this.x, (int)(this.y + this.height - 1.0), (int)(this.x + this.width), (int)(this.y + this.height), ColorUtils.changeAlpha(1996488704, 30));
        final String s = this.currentString.getString();
        if (this.listening) {
            if (this.timer.hasReached(500L)) {
                this.showCursor = !this.showCursor;
                this.timer.reset();
            }
            if (this.showCursor) {
                Gui.drawRect((int)this.x + (NewGui.customfont.getValue() ? Xulu.cFontRenderer.getStringWidth(s) : FontUtil.getStringWidth(s)) + 2, (int)this.y, (int)this.x + (NewGui.customfont.getValue() ? Xulu.cFontRenderer.getStringWidth(s) : FontUtil.getStringWidth(s)) + 3, (int)this.y + FontUtil.getFontHeight() + 2, -1);
            }
        }
        else {
            if (!s.equals(this.set.getValue().getText())) {
                this.currentString = new CurrentString(this.set.getValue().getText());
            }
            this.showCursor = false;
        }
        if (NewGui.customfont.getValue()) {
            Xulu.cFontRenderer.drawStringWithShadow(s, (float)(this.x + 2.0), (float)this.y + 2.0f, -1);
        }
        else {
            FontUtil.drawStringWithShadow(s, this.x + 2.0, this.y + 2.0, -1);
        }
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.isButtonHovered(mouseX, mouseY)) {
            if (mouseButton == 0) {
                this.currentString = new CurrentString(this.set.getValue().getText());
                this.listening = true;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean keyTyped(final char typedChar, final int keyCode) {
        if (!this.listening) {
            return false;
        }
        switch (keyCode) {
            case 1: {
                return true;
            }
            case 28: {
                this.enterString();
                return true;
            }
            case 14: {
                this.setString(removeLastChar(this.currentString.getString()));
                return true;
            }
            default: {
                if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                    this.setString(this.currentString.getString() + typedChar);
                    return true;
                }
                return false;
            }
        }
    }
    
    public static String removeLastChar(final String str) {
        String output = "";
        if (str != null && str.length() > 0) {
            output = str.substring(0, str.length() - 1);
        }
        return output;
    }
    
    private void enterString() {
        this.set.setValue(new TextBox(this.currentString.getString()));
        this.setString(this.set.getValue().getText());
        this.listening = false;
    }
    
    public void setString(final String newString) {
        this.currentString = new CurrentString(newString);
    }
    
    public boolean isButtonHovered(final int mouseX, final int mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
    }
    
    public static class CurrentString
    {
        private String string;
        
        public CurrentString(final String string) {
            this.string = string;
        }
        
        public String getString() {
            return this.string;
        }
    }
}
