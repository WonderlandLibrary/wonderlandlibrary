// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.material.button.values;

import java.awt.Font;
import net.lenni0451.eventapi.events.IEvent;
import net.lenni0451.eventapi.manager.EventManager;
import net.augustus.events.EventClickGui;
import org.lwjgl.input.Mouse;
import net.augustus.material.Main;
import net.augustus.utils.skid.tomorrow.RenderUtil;
import java.awt.Color;
import net.augustus.settings.StringValue;
import net.augustus.material.Tab;
import net.augustus.settings.Setting;
import net.augustus.font.UnicodeFontRenderer;
import net.augustus.material.button.Button;

public class BMode extends Button
{
    private static UnicodeFontRenderer arial18;
    private static UnicodeFontRenderer arial16;
    
    public BMode(final float x, final float y, final Setting v, final Tab moduleTab) {
        super(x, y, v, moduleTab);
    }
    
    @Override
    public void draw(final float mouseX, final float mouseY) {
        BMode.arial16.drawString(this.v.getName(), this.x + 2.0f - this.animation / (((StringValue)this.v).getStringList().length * 20) * 5.0f, this.y - this.animation / (((StringValue)this.v).getStringList().length * 20) * 5.0f, new Color(255, 255, 255).getRGB());
        BMode.arial18.drawString(((StringValue)this.v).getSelected(), this.x + 5.0f, this.y + 5.0f, new Color(120, 120, 120).getRGB());
        RenderUtil.drawBorderedRect(this.x, this.y - 5.0f, this.x + 65.0f, this.y - 5.0f + this.animation, 0.5f, new Color(100, 100, 100).getRGB(), new Color(0, 0, 0, 0).getRGB());
        BMode.arial16.drawString("V", this.x + 55.0f, this.y + 4.0f, new Color(200, 200, 200).getRGB());
        float modY = this.y + 25.0f;
        for (final String e : ((StringValue)this.v).getStringList()) {
            if (!e.equals(((StringValue)this.v).getSelected())) {
                if (modY <= this.y - 5.0f + this.animation) {
                    BMode.arial18.drawString(e, this.x + 5.0f, modY, new Color(120, 120, 120).getRGB());
                }
                modY += 20.0f;
            }
        }
        if (this.drag) {
            this.animation = this.animationUtils.animate((float)(((StringValue)this.v).getStringList().length * 20), this.animation, 0.1f);
        }
        else {
            this.animation = this.animationUtils.animate(20.0f, this.animation, 0.1f);
        }
        super.draw(mouseX, mouseY);
    }
    
    @Override
    public void mouseClicked(final float mouseX, final float mouseY) {
        super.mouseClicked(mouseX, mouseY);
        if (Main.isHovered(this.x, this.y - 5.0f, this.x + 65.0f, this.y + 15.0f, mouseX, mouseY)) {
            this.drag = !this.drag;
        }
        float modY = this.y + 25.0f;
        for (final String e : ((StringValue)this.v).getStringList()) {
            if (!e.equals(((StringValue)this.v).getSelected())) {
                if (modY <= this.y - 5.0f + this.animation && Main.isHovered(this.x, modY, this.x + 65.0f, modY + 20.0f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    this.drag = false;
                    ((StringValue)this.v).setString(e);
                    EventManager.call(new EventClickGui());
                }
                modY += 20.0f;
            }
        }
    }
    
    static {
        try {
            BMode.arial18 = new UnicodeFontRenderer(Font.createFont(0, BMode.class.getResourceAsStream("/ressources/arial.ttf")).deriveFont(18.0f));
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        try {
            BMode.arial16 = new UnicodeFontRenderer(Font.createFont(0, BMode.class.getResourceAsStream("/ressources/arial.ttf")).deriveFont(16.0f));
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
