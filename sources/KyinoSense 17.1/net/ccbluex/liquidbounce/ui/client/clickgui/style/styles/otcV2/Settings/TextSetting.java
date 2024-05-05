/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Settings;

import java.awt.Color;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Downward;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.ModuleRender;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.OtcClickGUi;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtilsFlux;
import net.ccbluex.liquidbounce.value.TextValue;
import org.lwjgl.input.Keyboard;

public class TextSetting
extends Downward<TextValue> {
    public TextValue textValue;
    private float modulex;
    private float moduley;
    private float texty;

    public TextSetting(TextValue s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
        this.textValue = s;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int guiColor = ClickGUI.generateColor().getRGB();
        this.modulex = OtcClickGUi.getMainx();
        this.moduley = OtcClickGUi.getMainy();
        this.texty = this.pos.y + (float)this.getScrollY();
        RenderUtilsFlux.drawRoundedRect(this.modulex + 5.0f + this.pos.x + 55.0f, this.moduley + 17.0f + this.texty + 8.0f, 75.0f, 11.0f, 1.0f, new Color(59, 63, 72).getRGB(), 1.0f, this.textValue.getTextHovered() ? guiColor : new Color(85, 90, 96).getRGB());
        if (this.isHovered(mouseX, mouseY) && !this.textValue.getTextHovered()) {
            RenderUtilsFlux.drawRoundedRect(this.modulex + 5.0f + this.pos.x + 55.0f, this.moduley + 17.0f + this.texty + 8.0f, 75.0f, 11.0f, 1.0f, new Color(0, 0, 0, 0).getRGB(), 1.0f, guiColor);
        }
        Fonts.fontTahoma.drawString(this.textValue.getName(), this.modulex + 5.0f + this.pos.x + 4.0f, this.moduley + 17.0f + this.texty + 13.0f, new Color(200, 200, 200).getRGB());
        if (Fonts.fontTahoma.func_78256_a(this.textValue.getTextHovered() ? (String)this.textValue.get() + "_" : (String)this.textValue.get()) > 70) {
            Fonts.fontTahoma.drawString(Fonts.fontTahoma.func_78262_a(this.textValue.getTextHovered() ? (String)this.textValue.get() + "_" : (String)this.textValue.get(), 78, true), this.modulex + 5.0f + this.pos.x + 57.0f, this.moduley + 17.0f + this.texty + 13.0f, new Color(200, 200, 200).getRGB());
        } else if (((String)this.textValue.get()).isEmpty() && !this.textValue.getTextHovered()) {
            Fonts.fontTahoma.drawString("\u5728\u6b64\u8f93\u5165...", this.modulex + 5.0f + this.pos.x + 57.0f, this.moduley + 17.0f + this.texty + 13.0f, new Color(200, 200, 200).getRGB());
        } else {
            Fonts.fontTahoma.drawString(this.textValue.getTextHovered() ? (String)this.textValue.get() + "_" : (String)this.textValue.get(), this.modulex + 5.0f + this.pos.x + 57.0f, this.moduley + 17.0f + this.texty + 13.0f, new Color(200, 200, 200).getRGB());
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (float)mouseX >= this.modulex + 5.0f + this.pos.x + 55.0f && (float)mouseX <= this.modulex + 5.0f + this.pos.x + 55.0f + 75.0f && (float)mouseY >= this.moduley + 17.0f + this.texty + 8.0f && (float)mouseY <= this.moduley + 17.0f + this.texty + 8.0f + 11.0f;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isHovered(mouseX, mouseY)) {
            this.textValue.setTextHovered(!this.textValue.getTextHovered());
        } else if (this.textValue.getTextHovered()) {
            this.textValue.setTextHovered(false);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (this.textValue.getTextHovered()) {
            if (keyCode == 1) {
                this.textValue.setTextHovered(false);
            } else if (keyCode != 14 && keyCode != 157 && keyCode != 29 && keyCode != 54 && keyCode != 42 && keyCode != 15 && keyCode != 58 && keyCode != 211 && keyCode != 199 && keyCode != 210 && keyCode != 200 && keyCode != 208 && keyCode != 205 && keyCode != 203 && keyCode != 56 && keyCode != 184 && keyCode != 197 && keyCode != 70 && keyCode != 207 && keyCode != 201 && keyCode != 209 && keyCode != 221 && keyCode != 59 && keyCode != 60 && keyCode != 61 && keyCode != 62 && keyCode != 63 && keyCode != 64 && keyCode != 65 && keyCode != 66 && keyCode != 67 && keyCode != 68 && keyCode != 87 && keyCode != 88) {
                this.textValue.append(Character.valueOf(typedChar));
            }
            if (((TextValue)this.setting).getTextHovered() && Keyboard.isKeyDown((int)14) && ((String)this.textValue.get()).length() >= 1) {
                this.textValue.set(((String)this.textValue.get()).substring(0, ((String)this.textValue.get()).length() - 1));
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
    }
}

