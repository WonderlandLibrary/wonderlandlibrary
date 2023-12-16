// 
// Decompiled by Procyon v0.6.0
// 

package fluid.client.mods;

import com.darkmagician6.eventapi.EventManager;

public class Mod
{
    public String name;
    public String description;
    
    public Mod(final String name, final String description) {
        this.name = name;
        this.description = description;
    }
    
    public void onEnable() {
        EventManager.register(this);
    }
    
    public void onDisable() {
        EventManager.unregister(this);
    }
}
