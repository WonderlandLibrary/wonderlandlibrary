// 
// Decompiled by Procyon v0.5.36
// 

package dev.xulu.newgui.elements.menu;

import java.io.IOException;
import org.lwjgl.input.Keyboard;
import dev.xulu.settings.Bind;
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

public class ElementKeyBind extends Element
{
    private boolean listening;
    
    public ElementKeyBind(final ModuleButton iparent, final Value iset) {
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
        final String s = this.listening ? "..." : Keyboard.getKeyName(this.set.getValue().getNum());
        if (NewGui.customfont.getValue()) {
            Xulu.cFontRenderer.drawStringWithShadow(s, (float)(this.x + 8.0 + Xulu.cFontRenderer.getStringWidth(this.setstrg)), (float)this.y + 2.0f, new Color(-1).darker().darker().getRGB());
        }
        else {
            FontUtil.drawStringWithShadow(s, this.x + 8.0 + FontUtil.getStringWidth(this.setstrg), this.y + 2.0, new Color(-1).darker().darker().getRGB());
        }
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.isButtonHovered(mouseX, mouseY)) {
            if (mouseButton == 0) {
                this.listening = true;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (this.listening) {
            if (keyCode != 1) {
                this.parent.mod.setKey(keyCode);
            }
            else {
                this.parent.mod.setKey(0);
            }
            this.listening = false;
            return true;
        }
        return super.keyTyped(typedChar, keyCode);
    }
    
    public boolean isButtonHovered(final int mouseX, final int mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
    }
}
