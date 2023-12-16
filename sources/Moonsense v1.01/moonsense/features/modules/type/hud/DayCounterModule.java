// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import moonsense.features.SCDefaultRenderModule;

public class DayCounterModule extends SCDefaultRenderModule
{
    public DayCounterModule() {
        super("Day Counter", "Display the number of days you have played in a Minecraft world.");
    }
    
    @Override
    public Object getValue() {
        return this.mc.theWorld.getWorldTime() / 24000L;
    }
    
    @Override
    public String getFormat() {
        String bracketType = this.brackets.getValue().get(this.brackets.getInt() + 1);
        if (bracketType.equalsIgnoreCase("NONE")) {
            bracketType = "  ";
        }
        return String.valueOf(bracketType.charAt(0)) + "%value% Days" + bracketType.charAt(1);
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
