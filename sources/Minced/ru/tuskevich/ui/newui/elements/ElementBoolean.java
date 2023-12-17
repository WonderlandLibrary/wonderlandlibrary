// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.newui.elements;

import ru.tuskevich.util.color.ColorUtility;
import ru.tuskevich.modules.impl.HUD.Hud;
import ru.tuskevich.util.render.RenderUtility;
import ru.tuskevich.util.animations.AnimationMath;
import java.awt.Color;
import ru.tuskevich.util.font.Fonts;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;

public class ElementBoolean extends Element
{
    public ElementModule module;
    public BooleanSetting setting;
    private double scale;
    private double alpha;
    public float lerp;
    
    public ElementBoolean(final ElementModule e, final BooleanSetting setting) {
        this.scale = 1.0;
        this.alpha = 1.0;
        this.module = e;
        this.setting = setting;
        this.setHeight(16.0);
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        final double centerY = this.getY() + 4.0;
        Fonts.Nunito12.drawString(this.setting.getName(), this.x + 4.0, this.y + 4.0, new Color(255, 255, 255, 255).getRGB());
        final int checkbox_width = 13;
        final int checkbox_height = 7;
        final double max = this.setting.state ? 1.0 : 0.0;
        this.scale = AnimationMath.animation((float)this.scale, (float)max, (float)AnimationMath.deltaTime());
        this.alpha = AnimationMath.animation((float)this.alpha, this.setting.state ? 255.0f : 0.0f, (float)AnimationMath.deltaTime());
        RenderUtility.drawRound((float)(this.x - 7.0 + this.width - checkbox_width) + 4.0f, (float)(centerY + this.height / 2.0 - checkbox_height) - 3.0f, 8.0f, 7.0f, 1.0f, new Color(98, 98, 98));
        Fonts.icon15.drawStringWithShadow("p", (float)(this.x - 7.0 + this.width - checkbox_width) + 5.0f, (float)(centerY + this.height / 2.0 - checkbox_height) - 0.5f, new Color(255, 255, 255).getRGB());
        RenderUtility.drawGradientRound((float)(this.x - 7.0 + this.width - checkbox_width) + 4.0f, (float)(centerY + this.height / 2.0 - checkbox_height) - 3.0f, (float)this.scale * 8.0f, (float)(this.scale * 7.0), (float)(this.scale * 1.0), ColorUtility.applyOpacity(Hud.getColor(270), 0.85f), Hud.getColor(0), Hud.getColor(180), Hud.getColor(90));
        if (this.setting.state) {
            Fonts.icon15.drawStringWithShadow("o", (float)(this.x - 7.0 + this.width - checkbox_width) + 4.5f, (float)(centerY + this.height / 2.0 - checkbox_height) - 1.0f, new Color(255, 255, 255, (int)this.alpha).getRGB());
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.isHovered(mouseX, mouseY)) {
            this.setting.setBoolValue(!this.setting.state);
        }
    }
    
    @Override
    public boolean isShown() {
        return this.setting.isVisible();
    }
}
