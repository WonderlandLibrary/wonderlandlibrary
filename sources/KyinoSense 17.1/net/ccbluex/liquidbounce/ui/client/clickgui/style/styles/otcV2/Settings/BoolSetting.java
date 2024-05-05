/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Settings;

import java.awt.Color;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Downward;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.ModuleRender;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.OtcClickGUi;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtilsFlux;
import net.ccbluex.liquidbounce.value.BoolValue;

public class BoolSetting
extends Downward<BoolValue> {
    private float modulex;
    private float moduley;
    private float booly;

    public BoolSetting(BoolValue s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int guiColor = ClickGUI.generateColor().getRGB();
        this.modulex = OtcClickGUi.getMainx();
        this.moduley = OtcClickGUi.getMainy();
        this.booly = this.pos.y + (float)this.getScrollY();
        RenderUtilsFlux.drawRoundedRect(this.modulex + 5.0f + this.pos.x + 4.0f, this.moduley + 17.0f + this.booly + 8.0f, 7.0f, 7.0f, 1.0f, (Boolean)((BoolValue)this.setting).get() != false ? new Color(86, 94, 115).getRGB() : new Color(50, 54, 65).getRGB(), 1.0f, (Boolean)((BoolValue)this.setting).get() != false ? new Color(86, 94, 115).getRGB() : new Color(85, 90, 96).getRGB());
        if (this.isHovered(mouseX, mouseY)) {
            RenderUtilsFlux.drawRoundedRect(this.modulex + 5.0f + this.pos.x + 4.0f, this.moduley + 17.0f + this.booly + 8.0f, 7.0f, 7.0f, 1.0f, new Color(0, 0, 0, 0).getRGB(), 1.0f, guiColor);
        }
        Fonts.fontTahoma.drawString(((BoolValue)this.setting).getName(), this.modulex + 5.0f + this.pos.x + 4.0f + 10.0f, this.moduley + 17.0f + this.booly + 8.0f + 3.0f, new Color(200, 200, 200).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isHovered(mouseX, mouseY)) {
            ((BoolValue)this.setting).toggle();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (float)mouseX >= this.modulex + 5.0f + this.pos.x + 4.0f && (float)mouseX <= this.modulex + 5.0f + this.pos.x + 4.0f + 135.0f - 128.0f && (float)mouseY >= this.moduley + 17.0f + this.booly + 8.0f && (float)mouseY <= this.moduley + 17.0f + this.booly + 8.0f + 7.0f;
    }
}

