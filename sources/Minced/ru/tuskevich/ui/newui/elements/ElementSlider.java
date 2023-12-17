// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.newui.elements;

import ru.tuskevich.util.color.ColorUtility;
import ru.tuskevich.modules.impl.HUD.Hud;
import ru.tuskevich.util.render.RenderUtility;
import java.awt.Color;
import ru.tuskevich.util.math.MathUtility;
import org.lwjgl.input.Mouse;
import ru.tuskevich.util.animations.AnimationMath;
import ru.tuskevich.util.font.Fonts;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;

public class ElementSlider extends ExpandElement
{
    public ElementModule module;
    public SliderSetting setting;
    public float lerp;
    
    public ElementSlider(final ElementModule e, final SliderSetting setting) {
        this.module = e;
        this.setting = setting;
        this.setHeight(16.0);
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        Fonts.Nunito12.drawString(this.setting.getName(), this.x + 2.0, this.y + 2.0, -1);
        final float amount = (float)(this.setting.getFloatValue() - this.setting.getMinValue()) / (float)(this.setting.getMaxValue() - this.setting.getMinValue()) / 1.05f;
        this.lerp = AnimationMath.fast(this.lerp, amount, 15.0f);
        if (this.isHovered(mouseX, mouseY) && Mouse.isButtonDown(0)) {
            this.setting.setValueNumber((float)MathUtility.round((mouseX - 2 - this.x) / (this.module.width - 4.0) * (this.setting.getMaxValue() - this.setting.getMinValue()) + this.setting.getMinValue(), this.setting.increment));
        }
        this.setting.setValueNumber((float)MathUtility.clamp(this.setting.getFloatValue(), this.setting.getMinValue(), this.setting.getMaxValue()));
        RenderUtility.drawRect(this.x + 2.0, this.y + 8.0, this.module.width - 6.0, 1.0, new Color(99, 99, 99, 255).getRGB());
        RenderUtility.drawGradientRound((float)(this.x + 3.0), (float)(this.y + 8.0), (float)((this.module.width - 4.0) * this.lerp), 0.5f, 0.0f, ColorUtility.applyOpacity(Hud.getColor(270), 0.85f), Hud.getColor(0), Hud.getColor(180), Hud.getColor(90));
        RenderUtility.drawGradientRound((float)(this.x + 3.0 + (this.module.width - 4.0) * this.lerp - 1.0), (float)(this.y + 7.0), 1.0f, 2.0f, 0.0f, new Color(255, 255, 255), new Color(255, 255, 255), new Color(255, 255, 255), new Color(255, 255, 255));
        Fonts.Nunito12.drawString(this.setting.getFloatValue() + "", this.x + this.module.width - Fonts.Nunito12.getStringWidth(this.setting.getFloatValue() + "") - 5.0, this.y + 2.0, new Color(255, 255, 255, 255).getRGB());
    }
    
    @Override
    public boolean isShown() {
        return this.setting.isVisible();
    }
    
    @Override
    public boolean canExpand() {
        return false;
    }
    
    @Override
    public int getHeightWithExpand() {
        return 0;
    }
    
    @Override
    public void onPress(final int mouseX, final int mouseY, final int button) {
    }
}
