// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.newui.elements;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import ru.tuskevich.ui.dropui.setting.imp.TextSetting;

public class ElementText extends Element
{
    public TextSetting setting;
    public ElementModule module;
    public boolean typing;
    public float typingGlow;
    
    public ElementText(final ElementModule module, final TextSetting setting) {
        this.setting = setting;
        this.module = module;
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        this.setHeight(24.0);
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int button) {
        super.mouseClicked(x, y, button);
        if (this.isHovered(x, y)) {
            this.typing = !this.typing;
        }
    }
    
    @Override
    public void keypressed(final char c, final int key) {
        super.keypressed(c, key);
        if (this.typing) {
            Keyboard.enableRepeatEvents(true);
            if (ChatAllowedCharacters.isAllowedCharacter(c)) {
                final StringBuilder sb = new StringBuilder();
                final TextSetting setting = this.setting;
                setting.text = sb.append(setting.text).append(c).toString();
            }
            if (key == 14 && this.setting.text.length() > 0) {
                this.setting.text = this.setting.text.substring(0, this.setting.text.length() - 1);
            }
        }
        if (key == 28) {
            this.typing = false;
        }
        if (Keyboard.isKeyDown(29) && Keyboard.isKeyDown(47)) {
            final StringBuilder sb2 = new StringBuilder();
            final TextSetting setting2 = this.setting;
            setting2.text = sb2.append(setting2.text).append(GuiScreen.getClipboardString()).toString();
        }
    }
}
