// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.render;

import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.utils.RainbowUtil;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.ColorSetting;
import net.augustus.modules.Module;

public class CrossHair extends Module
{
    public final ColorSetting color;
    public final DoubleValue length;
    public final DoubleValue width;
    public final DoubleValue margin;
    public final BooleanValue dot;
    public final BooleanValue tStyle;
    public final BooleanValue rainbow;
    public final DoubleValue rainbowSpeed;
    public final RainbowUtil rainbowUtil;
    
    public CrossHair() {
        super("Crosshair", new Color(157, 230, 106), Categorys.RENDER);
        this.color = new ColorSetting(4, "Color", this, new Color(94, 0, 255, 255));
        this.length = new DoubleValue(1, "Length", this, 4.0, 0.0, 20.0, 1);
        this.width = new DoubleValue(2, "Width", this, 1.3, 0.0, 10.0, 1);
        this.margin = new DoubleValue(3, "Margin", this, 1.3, 0.0, 20.0, 1);
        this.dot = new BooleanValue(5, "Dot", this, true);
        this.tStyle = new BooleanValue(6, "T-Style", this, false);
        this.rainbow = new BooleanValue(7, "Rainbow", this, false);
        this.rainbowSpeed = new DoubleValue(8, "RainbowSpeed", this, 55.0, 0.0, 1000.0, 0);
        this.rainbowUtil = new RainbowUtil();
    }
}
