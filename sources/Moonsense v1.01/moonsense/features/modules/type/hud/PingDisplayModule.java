// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import moonsense.features.SCDefaultRenderModule;

public class PingDisplayModule extends SCDefaultRenderModule
{
    public PingDisplayModule() {
        super("Ping Display", "Display your server latency on the HUD.", 18);
    }
    
    @Override
    public Object getValue() {
        return (this.mc.getNetHandler() != null && this.mc.getNetHandler().func_175102_a(this.mc.thePlayer.getUniqueID()) != null) ? this.mc.getNetHandler().func_175102_a(this.mc.thePlayer.getUniqueID()).getResponseTime() : -1;
    }
    
    @Override
    public String getFormat() {
        String bracketType = this.brackets.getValue().get(this.brackets.getInt() + 1);
        if (bracketType.equalsIgnoreCase("NONE")) {
            bracketType = "  ";
        }
        return String.valueOf(bracketType.charAt(0)) + "%value% ms" + bracketType.charAt(1);
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
