// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.render;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventItemGlint;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.ColorSetting;
import net.augustus.settings.BooleanValue;
import net.augustus.modules.Module;

public class CustomGlint extends Module
{
    public BooleanValue customColor;
    public ColorSetting color;
    public BooleanValue removeGlint;
    public DoubleValue glintSpeed;
    
    public CustomGlint() {
        super("CustomGlint", new Color(128, 64, 204), Categorys.RENDER);
        this.customColor = new BooleanValue(4, "CustomColor", this, false);
        this.color = new ColorSetting(1, "Color", this, new Color(128, 64, 204));
        this.removeGlint = new BooleanValue(2, "Remove", this, false);
        this.glintSpeed = new DoubleValue(3, "Speed", this, 1.0, 0.01, 10.0, 2);
    }
    
    @EventTarget
    public void onEventItemGlint(final EventItemGlint eventItemGlint) {
        if (this.customColor.getBoolean()) {
            eventItemGlint.setColor(this.color.getColor().getRGB());
        }
        eventItemGlint.setCanceled(this.removeGlint.getBoolean());
        eventItemGlint.setSpeed(this.glintSpeed.getValue());
    }
}
