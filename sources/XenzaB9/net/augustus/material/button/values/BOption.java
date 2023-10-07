// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.material.button.values;

import java.awt.Font;
import net.lenni0451.eventapi.events.IEvent;
import net.lenni0451.eventapi.manager.EventManager;
import net.augustus.events.EventClickGui;
import net.augustus.utils.skid.tomorrow.RenderUtil;
import net.augustus.utils.skid.tomorrow.ColorUtils;
import net.augustus.material.Main;
import net.augustus.settings.BooleanValue;
import java.awt.Color;
import net.augustus.material.Tab;
import net.augustus.settings.Setting;
import net.augustus.utils.skid.tomorrow.AnimationUtils;
import net.augustus.font.UnicodeFontRenderer;
import net.augustus.material.button.Button;

public class BOption extends Button
{
    private static UnicodeFontRenderer arial18;
    AnimationUtils au;
    
    public BOption(final float x, final float y, final Setting v, final Tab moduleTab) {
        super(x, y, v, moduleTab);
        this.au = new AnimationUtils();
    }
    
    @Override
    public void draw(final float mouseX, final float mouseY) {
        super.draw(mouseX, mouseY);
        BOption.arial18.drawString(this.v.getName(), this.x + 30.0f, this.y + 2.0f, new Color(255, 255, 255).getRGB());
        if (((BooleanValue)this.v).getBoolean()) {
            RenderUtil.drawRoundedRect(this.x, this.y + 1.0f, this.x + 20.0f, this.y + 9.0f, 2.0f, ColorUtils.lighter(Main.clientColor, 0.30000001192092896));
            this.animation = this.au.animate(20.0f, this.animation, 0.05f);
            if (Main.isHovered(this.x, this.y + 1.0f, this.x + 20.0f, this.y + 9.0f, mouseX, mouseY)) {
                RenderUtil.drawCircle(this.x + this.animation - 3.5f, this.y + 5.0f, 9.0f, ColorUtils.reAlpha(Main.clientColor.getRGB(), 0.1f));
            }
            RenderUtil.drawCircle(this.x + this.animation - 3.0f, this.y + 5.0f, 6.0f, new Color(240, 240, 240).getRGB());
        }
        else {
            RenderUtil.drawRoundedRect(this.x, this.y + 1.0f, this.x + 20.0f, this.y + 9.0f, 2.0f, new Color(180, 180, 180));
            this.animation = this.au.animate(0.0f, this.animation, 0.05f);
            if (Main.isHovered(this.x, this.y, this.x + 20.0f, this.y + 10.0f, mouseX, mouseY)) {
                RenderUtil.drawCircle(this.x + this.animation - 1.5f, this.y + 5.0f, 9.0f, new Color(0, 0, 0, 30).getRGB());
            }
            RenderUtil.drawCircle(this.x + this.animation - 1.0f, this.y + 5.0f, 6.0f, new Color(240, 240, 240).getRGB());
        }
    }
    
    @Override
    public void mouseClicked(final float mouseX, final float mouseY) {
        super.mouseClicked(mouseX, mouseY);
        if (Main.isHovered(this.x, this.y, this.x + 20.0f, this.y + 10.0f, mouseX, mouseY)) {
            ((BooleanValue)this.v).setBoolean(!((BooleanValue)this.v).getBoolean());
            EventManager.call(new EventClickGui());
        }
    }
    
    static {
        try {
            BOption.arial18 = new UnicodeFontRenderer(Font.createFont(0, BOption.class.getResourceAsStream("/ressources/arial.ttf")).deriveFont(18.0f));
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
