// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import java.util.Date;
import java.text.SimpleDateFormat;
import moonsense.features.SCDefaultRenderModule;

public class TimeDisplayModule extends SCDefaultRenderModule
{
    public TimeDisplayModule() {
        super("Time Display", "Display the real-world time on the HUD.", 14);
    }
    
    @Override
    public String getFormat() {
        String bracketType = this.brackets.getValue().get(this.brackets.getInt() + 1);
        if (bracketType.equalsIgnoreCase("NONE")) {
            bracketType = "  ";
        }
        return String.valueOf(bracketType.charAt(0)) + "%value%" + bracketType.charAt(1);
    }
    
    @Override
    public Object getValue() {
        return new SimpleDateFormat("hh:mm a").format(new Date());
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
