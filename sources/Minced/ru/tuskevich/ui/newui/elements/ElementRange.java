// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.newui.elements;

import ru.tuskevich.util.math.HoveringUtil;
import ru.tuskevich.util.color.ColorUtility;
import ru.tuskevich.modules.impl.HUD.ClickGui;
import ru.tuskevich.util.render.RenderUtility;
import java.awt.Color;
import ru.tuskevich.util.font.Fonts;
import ru.tuskevich.util.math.MathUtility;
import ru.tuskevich.util.animations.AnimationMath;
import ru.tuskevich.ui.dropui.setting.imp.RangeSetting;

public class ElementRange extends Element
{
    public RangeSetting setting;
    public ElementModule module;
    public boolean draggingFirst;
    public boolean draggingSecond;
    public float firstLerp;
    public float secondLerp;
    
    public ElementRange(final ElementModule module, final RangeSetting setting) {
        this.draggingFirst = false;
        this.draggingSecond = false;
        this.setting = setting;
        this.module = module;
        this.setHeight(16.0);
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        final float firstX = (this.setting.getFirst() - this.setting.min) / (this.setting.max - this.setting.min) / 1.005f;
        final float secondX = (this.setting.getSecond() - this.setting.min) / (this.setting.max - this.setting.min) / 1.005f;
        this.firstLerp = AnimationMath.fast(this.firstLerp, firstX, 15.0f);
        this.secondLerp = AnimationMath.fast(this.secondLerp, secondX, 15.0f);
        if (this.draggingFirst) {
            this.setting.first = (float)MathUtility.round((mouseX - 2 - this.x) / (this.module.width - 4.0) * (this.setting.max - this.setting.min) + this.setting.min, this.setting.increment);
        }
        if (this.draggingSecond) {
            this.setting.second = (float)MathUtility.round((mouseX - 2 - this.x) / (this.module.width - 4.0) * (this.setting.max - this.setting.min) + this.setting.min, this.setting.increment);
        }
        if (this.setting.first > this.setting.second - 0.5f) {
            this.setting.first = this.setting.second - 0.5f;
        }
        if (this.setting.second < this.setting.first) {
            this.setting.second = this.setting.first;
        }
        this.setting.first = MathUtility.clamp(this.setting.first, this.setting.min, this.setting.max);
        this.setting.second = MathUtility.clamp(this.setting.second, this.setting.min, this.setting.max);
        Fonts.Nunito12.drawString(this.setting.getName(), this.x + 2.0, this.y + 2.0, -1);
        RenderUtility.drawRect(this.x + 2.0, this.y + 8.0, this.module.width - 4.0, 1.0, new Color(15, 15, 15, 255).getRGB());
        RenderUtility.drawRect(this.x + 2.0 + (this.module.width - 4.0) * this.firstLerp - 1.0, this.y + 7.5, 2.0, 2.0, ColorUtility.setAlpha(ClickGui.getColor().getRGB(), 255));
        RenderUtility.drawRect(this.x + 2.0 + (this.module.width - 4.0) * this.secondLerp - 1.0, this.y + 7.5, 2.0, 2.0, ColorUtility.setAlpha(ClickGui.getColor().getRGB(), 255));
        Fonts.Nunito12.drawString(this.setting.getFirst() + " - " + this.setting.getSecond(), this.x + this.width - Fonts.Nunito12.getStringWidth(this.setting.getFirst() + " - " + this.setting.getSecond()) - 3.0, this.y + 2.0, -1);
        RenderUtility.drawRect(this.x + 2.0 + (this.module.width - 4.0) * this.firstLerp, this.y + 8.0, (this.module.width - 4.0) * (this.secondLerp - this.firstLerp), 1.0, ColorUtility.setAlpha(ClickGui.getColor().getRGB(), 255));
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int button) {
        super.mouseClicked(x, y, button);
        final float firstX = (this.setting.getFirst() - this.setting.min) / (this.setting.max - this.setting.min) / 1.005f;
        final float secondX = (this.setting.getSecond() - this.setting.min) / (this.setting.max - this.setting.min) / 1.005f;
        if (this.isHovered(x, y)) {
            if (HoveringUtil.isHovering((float)(this.x + 2.0 + (this.module.width - 4.0) * firstX - 2.0), (float)(this.y + 5.5), 7.0f, 7.0f, x, y)) {
                this.draggingFirst = true;
            }
            if (HoveringUtil.isHovering((float)(this.x + 2.0 + (this.module.width - 4.0) * secondX - 2.0), (float)(this.y + 5.5), 7.0f, 7.0f, x, y)) {
                this.draggingSecond = true;
            }
        }
    }
    
    @Override
    public void mouseReleased(final int x, final int y, final int button) {
        super.mouseReleased(x, y, button);
        this.draggingFirst = false;
        this.draggingSecond = false;
    }
}
