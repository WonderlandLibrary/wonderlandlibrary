// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.render;

import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.StringValue;
import net.augustus.modules.Module;

public class ItemEsp extends Module
{
    public StringValue mode;
    
    public ItemEsp() {
        super("ItemESP", Color.RED, Categorys.RENDER);
        this.mode = new StringValue(1, "Mode", this, "Vanilla", new String[] { "Vanilla" });
    }
}
