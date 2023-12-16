// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import net.minecraft.client.Minecraft;
import moonsense.features.SCDefaultRenderModule;

public class FPSModule extends SCDefaultRenderModule
{
    public FPSModule() {
        super("FPS", "Displays your FPS on the HUD.");
    }
    
    @Override
    public Object getValue() {
        return String.valueOf(Minecraft.func_175610_ah()) + " FPS";
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.TOP_CENTER;
    }
}
