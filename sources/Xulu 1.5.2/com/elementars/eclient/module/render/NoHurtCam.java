// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class NoHurtCam extends Module
{
    private static NoHurtCam INSTANCE;
    
    public NoHurtCam() {
        super("NoHurtCam", "Disables the hurt cam", 0, Category.RENDER, true);
        NoHurtCam.INSTANCE = this;
    }
    
    public static boolean shouldDisable() {
        return NoHurtCam.INSTANCE != null && NoHurtCam.INSTANCE.isToggled();
    }
}
