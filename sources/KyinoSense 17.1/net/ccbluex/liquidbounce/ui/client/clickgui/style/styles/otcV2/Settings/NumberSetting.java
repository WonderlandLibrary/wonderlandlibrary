/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.MathHelper
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Settings;

import java.awt.Color;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Downward;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.ModuleRender;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.OtcClickGUi;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.math.MathUtil;
import net.ccbluex.liquidbounce.utils.render.RenderUtilsFlux;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public class NumberSetting
extends Downward<IntegerValue> {
    private float modulex;
    private float moduley;
    private float numbery;
    public float percent = 0.0f;
    private boolean iloveyou;

    public NumberSetting(IntegerValue s, float x, float y, int width, int height, ModuleRender moduleRender) {
        super(s, x, y, width, height, moduleRender);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int guiColor = ClickGUI.generateColor().getRGB();
        this.modulex = OtcClickGUi.getMainx();
        this.moduley = OtcClickGUi.getMainy();
        this.numbery = this.pos.y + (float)this.getScrollY();
        Minecraft.func_71410_x();
        double clamp = MathHelper.func_151237_a((double)(Minecraft.func_175610_ah() / 30), (double)1.0, (double)9999.0);
        double percentBar = ((double)((Integer)((IntegerValue)this.setting).get()).intValue() - (double)((IntegerValue)this.setting).getMinimum()) / (double)(((IntegerValue)this.setting).getMaximum() - ((IntegerValue)this.setting).getMinimum());
        this.percent = Math.max(0.0f, Math.min(1.0f, (float)((double)this.percent + (Math.max(0.0, Math.min(percentBar, 1.0)) - (double)this.percent) * (0.2 / clamp))));
        RoundedUtil.drawRound(this.modulex + 5.0f + this.pos.x + 55.0f, this.moduley + 17.0f + this.numbery + 8.0f, 75.0f, 2.5f, 1.0f, new Color(34, 38, 48));
        RoundedUtil.drawRound(this.modulex + 5.0f + this.pos.x + 55.0f, this.moduley + 17.0f + this.numbery + 8.0f, 75.0f * this.percent, 2.5f, 1.0f, new Color(guiColor));
        Fonts.fontTahoma.drawString(((IntegerValue)this.setting).getName(), this.modulex + 5.0f + this.pos.x + 4.0f, this.moduley + 17.0f + this.numbery + 8.0f, new Color(200, 200, 200).getRGB());
        if (this.iloveyou) {
            float percentt = Math.min(1.0f, Math.max(0.0f, ((float)mouseX - (this.modulex + 5.0f + this.pos.x + 55.0f)) / 99.0f * 1.3f));
            double newValue = percentt * (float)(((IntegerValue)this.setting).getMaximum() - ((IntegerValue)this.setting).getMinimum()) + (float)((IntegerValue)this.setting).getMinimum();
            double set = MathUtil.incValue(newValue, 1.0);
            ((IntegerValue)this.setting).set(set);
        }
        ClickGUI cg = (ClickGUI)LiquidBounce.moduleManager.getModule(ClickGUI.class);
        if (this.iloveyou || this.isHovered(mouseX, mouseY) || ((Boolean)cg.disp.get()).booleanValue()) {
            RoundedUtil.drawRound(this.modulex + 5.0f + this.pos.x + 55.0f + 61.0f * this.percent, this.moduley + 17.0f + this.numbery + 8.0f + 6.0f, Fonts.fontTahoma.func_78256_a(this.setting.get() + "") + 2, 6.0f, 1.0f, new Color(32, 34, 39));
            Fonts.fontTahoma.drawString(this.setting.get() + "", this.modulex + 5.0f + this.pos.x + 55.0f + 62.0f * this.percent, this.moduley + 17.0f + this.numbery + 8.0f + 8.0f, new Color(250, 250, 250).getRGB());
        }
        if (this.isHovered(mouseX, mouseY)) {
            RenderUtilsFlux.drawRoundedRect(this.modulex + 5.0f + this.pos.x + 55.0f, this.moduley + 17.0f + this.numbery + 8.0f, 75.0f, 2.5f, 1.0f, new Color(0, 0, 0, 0).getRGB(), 1.0f, guiColor);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isHovered(mouseX, mouseY) && mouseButton == 0) {
            this.iloveyou = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            this.iloveyou = false;
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (float)mouseX >= this.modulex + 5.0f + this.pos.x + 55.0f && (float)mouseX <= this.modulex + 5.0f + this.pos.x + 55.0f + 75.0f && (float)mouseY >= this.moduley + 17.0f + this.numbery + 8.0f && (float)mouseY <= this.moduley + 17.0f + this.numbery + 8.0f + 2.5f;
    }
}

