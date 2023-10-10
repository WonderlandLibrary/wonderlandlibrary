// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.settings;

import net.augustus.savings.parts.SettingPart;
import net.augustus.modules.Module;
import java.awt.Color;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class ColorSetting extends Setting
{
    @Expose
    @SerializedName("Red")
    private int red;
    @Expose
    @SerializedName("Green")
    private int green;
    @Expose
    @SerializedName("Blue")
    private int blue;
    @Expose
    @SerializedName("Alpha")
    private int alpha;
    private Color color;
    
    public ColorSetting(final int id, final String name, final Module parent, final Color color) {
        super(id, name, parent);
        this.color = color;
        this.red = this.color.getRed();
        this.blue = this.color.getBlue();
        this.green = this.color.getGreen();
        this.alpha = this.color.getAlpha();
        ColorSetting.sm.newSetting(this);
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public void setColor(final Color color) {
        this.color = color;
        this.red = this.color.getRed();
        this.blue = this.color.getBlue();
        this.green = this.color.getGreen();
        this.alpha = this.color.getAlpha();
    }
    
    @Override
    public void readSetting(final SettingPart setting) {
        super.readSetting(setting);
    }
    
    @Override
    public void readConfigSetting(final SettingPart setting) {
        super.readConfigSetting(setting);
        this.setColor(new Color(setting.getRed(), setting.getGreen(), setting.getBlue(), setting.getAlpha()));
    }
}
