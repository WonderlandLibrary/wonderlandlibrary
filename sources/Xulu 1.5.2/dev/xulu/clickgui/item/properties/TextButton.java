// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.clickgui.item.properties;

import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import com.elementars.eclient.module.render.ExeterGui;
import dev.xulu.settings.TextBox;
import net.minecraft.client.gui.Gui;
import dev.xulu.newgui.util.FontUtil;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.module.render.NewGui;
import com.elementars.eclient.util.XuluTessellator;
import com.elementars.eclient.util.ColorUtils;
import dev.xulu.newgui.util.ColorUtil;
import dev.xulu.clickgui.Panel;
import dev.xulu.settings.Value;
import dev.xulu.newgui.elements.menu.ElementTextBox;
import com.elementars.eclient.util.Timer;
import dev.xulu.clickgui.item.Button;

public class TextButton extends Button
{
    private boolean listening;
    private Timer timer;
    private boolean showCursor;
    private ElementTextBox.CurrentString currentString;
    
    public TextButton(final Value property) {
        super(property.getName(), null);
        this.timer = new Timer();
        this.currentString = new ElementTextBox.CurrentString("");
        this.setValue(property);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        XuluTessellator.drawRectGradient(this.x, this.y, this.x + this.width + 7.4f, this.y + this.height, this.getState() ? ColorUtils.changeAlpha(ColorUtil.getClickGUIColor().getRGB(), 200) : 290805077, -1);
        if (this.isHovering(mouseX, mouseY)) {
            if (this.getState()) {
                XuluTessellator.drawRectGradient(this.x, this.y, this.x + this.width, this.y + this.height, ColorUtils.changeAlpha(ColorUtils.Colors.BLACK, 25), -1);
            }
            else {
                XuluTessellator.drawRectGradient(this.x, this.y, this.x + this.width, this.y + this.height, ColorUtils.changeAlpha(ColorUtils.Colors.WHITE, 25), -1);
            }
        }
        final String s = this.currentString.getString();
        if (this.listening) {
            if (this.timer.hasReached(500L)) {
                this.showCursor = !this.showCursor;
                this.timer.reset();
            }
            if (this.showCursor) {
                Gui.drawRect((int)this.x + (NewGui.customfont.getValue() ? Xulu.cFontRenderer.getStringWidth(s) : FontUtil.getStringWidth(s)) + 3, (int)this.y + 1, (int)this.x + (NewGui.customfont.getValue() ? Xulu.cFontRenderer.getStringWidth(s) : FontUtil.getStringWidth(s)) + 4, (int)this.y + FontUtil.getFontHeight() + 5, -1);
            }
        }
        else {
            if (!s.equals(this.property.getValue().getText())) {
                this.currentString = new ElementTextBox.CurrentString(this.property.getValue().getText());
            }
            this.showCursor = false;
        }
        if (ExeterGui.getCF()) {
            Xulu.cFontRenderer.drawStringWithShadow(s, this.x + 2.3f, this.y + 3.0f, this.getState() ? -1 : -1);
        }
        else {
            TextButton.fontRenderer.drawStringWithShadow(s, this.x + 2.3f, this.y + 4.0f, this.getState() ? -1 : -1);
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            if (ExeterGui.getSound()) {
                TextButton.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }
            if (mouseButton == 0) {
                this.currentString = new ElementTextBox.CurrentString(this.property.getValue().getText());
                this.listening = true;
            }
        }
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
        this.property.setValue(new TextBox(this.currentString.getString()));
        this.setString(this.property.getValue().getText());
        this.listening = false;
    }
    
    public void setString(final String newString) {
        this.currentString = new ElementTextBox.CurrentString(newString);
    }
    
    @Override
    public int getHeight() {
        return 14;
    }
    
    @Override
    public void toggle() {
    }
    
    @Override
    public boolean getState() {
        return false;
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
