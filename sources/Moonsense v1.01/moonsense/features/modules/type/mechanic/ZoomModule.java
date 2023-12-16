// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.mechanic;

import moonsense.enums.ModuleCategory;
import moonsense.animations.Easing;
import moonsense.settings.Setting;
import moonsense.animations.Animation;
import moonsense.features.SCModule;

public class ZoomModule extends SCModule
{
    public static ZoomModule INSTANCE;
    public final Animation zoomAnimation;
    public final Setting scrollZoom;
    public final Setting smoothZoom;
    public final Setting scrollMaximum;
    
    public ZoomModule() {
        super("Zoom", "Alter the OptiFine zoom feature.");
        this.zoomAnimation = new Animation(1L, 0.1f, 1.0f, Easing.EASE_IN_OUT_SINE);
        ZoomModule.INSTANCE = this;
        new Setting(this, "Zoom Options");
        this.scrollZoom = new Setting(this, "Scroll to Zoom").setDefault(true);
        this.smoothZoom = new Setting(this, "Smooth Zoom").setDefault(true);
        this.scrollMaximum = new Setting(this, "Scroll Maximum").setDefault(16).setRange(16, 48, 1);
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.MECHANIC;
    }
}
