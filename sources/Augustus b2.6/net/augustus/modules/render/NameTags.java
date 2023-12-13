// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.render;

import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.modules.Module;

public class NameTags extends Module
{
    public StringValue mode;
    public DoubleValue scale;
    public DoubleValue height;
    public BooleanValue armor;
    
    public NameTags() {
        super("NameTags", new Color(21, 35, 81), Categorys.RENDER);
        this.mode = new StringValue(1, "Mode", this, "Custom", new String[] { "None", "Vanilla", "Custom" });
        this.scale = new DoubleValue(2, "Scale", this, 1.2, 0.0, 3.0, 2);
        this.height = new DoubleValue(4, "Height", this, 0.0, -3.0, 3.0, 2);
        this.armor = new BooleanValue(3, "Armor", this, true);
    }
}
