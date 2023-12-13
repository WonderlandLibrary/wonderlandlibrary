// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.render;

import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.ColorSetting;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.modules.Module;

public class HUD extends Module
{
    public StringValue side;
    public StringValue mode;
    public DoubleValue size;
    public BooleanValue hotBar;
    public BooleanValue armor;
    public BooleanValue backGround;
    public ColorSetting backGroundColor;
    public ColorSetting color;
    
    public HUD() {
        super("HUD", new Color(75, 166, 91), Categorys.RENDER);
        this.side = new StringValue(1, "Side", this, "Left", new String[] { "Left", "Right" });
        this.mode = new StringValue(4, "Mode", this, "Basic", new String[] { "None", "Basic", "Other" });
        this.size = new DoubleValue(8, "Size", this, 0.6, 0.01, 2.0, 2);
        this.hotBar = new BooleanValue(5, "Hotbar", this, true);
        this.armor = new BooleanValue(7, "Armor", this, true);
        this.backGround = new BooleanValue(2, "BackGround", this, true);
        this.backGroundColor = new ColorSetting(3, "BackGroundColor", this, new Color(0, 0, 0, 100));
        this.color = new ColorSetting(6, "Color", this, Color.white);
    }
}
