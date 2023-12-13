// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class Cape extends Module
{
    public static Cape INSTANCE;
    
    public Cape() {
        super("Capes", "Shows Xulu capes", 0, Category.CORE, true);
        Cape.INSTANCE = this;
    }
    
    public static boolean isEnabled() {
        return Cape.INSTANCE.isToggled();
    }
}
