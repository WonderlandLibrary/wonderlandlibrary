// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.misc;

import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class Avoid extends Module
{
    public static Value<Boolean> cactus;
    public static Value<Boolean> fire;
    public static Value<Boolean> lava;
    public static Value<Boolean> webs;
    
    public Avoid() {
        super("Avoid", "Avoids interactions with certain things", 0, Category.MISC, true);
        Avoid.cactus = this.register(new Value<Boolean>("Cactus", this, true));
        Avoid.fire = this.register(new Value<Boolean>("Fire", this, true));
        Avoid.lava = this.register(new Value<Boolean>("Lava", this, true));
        Avoid.webs = this.register(new Value<Boolean>("Webs", this, true));
    }
}
