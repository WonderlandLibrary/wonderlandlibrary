// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.modules.render;

import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.StringValue;
import net.augustus.modules.Module;

public class Notifications extends Module
{
    public StringValue mode;
    
    public Notifications() {
        super("Notifications", Color.green, Categorys.RENDER);
        this.mode = new StringValue(1, "Mode", this, "Xenza", new String[] { "Xenza", "Rise5" });
    }
}
