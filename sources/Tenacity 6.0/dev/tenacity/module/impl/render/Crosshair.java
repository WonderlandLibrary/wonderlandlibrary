// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.Module;

public final class Crosshair extends Module
{
    private final BooleanSetting dynamic;
    private final NumberSetting gap;
    private final NumberSetting width;
    private final NumberSetting size;
    
    public Crosshair() {
        super("Crosshair", "Crosshair", Category.RENDER, "Personalizes your own crosshair.");
        this.dynamic = new BooleanSetting("Dynamic", false);
        this.gap = new NumberSetting("Crosshair Gap", 5.0, 15.0, 0.25, 0.25);
        this.width = new NumberSetting("Crosshair Width", 5.0, 10.0, 1.0, 0.25);
        this.size = new NumberSetting("Crosshair Size/Length", 5.0, 15.0, 1.0, 0.25);
    }
}
