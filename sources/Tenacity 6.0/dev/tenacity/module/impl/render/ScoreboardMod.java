// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.Module;

public class ScoreboardMod extends Module
{
    public static final NumberSetting yOffset;
    public static final BooleanSetting customFont;
    public static final BooleanSetting textShadow;
    public static final BooleanSetting redNumbers;
    
    public ScoreboardMod() {
        super("Scoreboard", "Scoreboard", Category.RENDER, "Scoreboard preferences");
        this.addSettings(ScoreboardMod.yOffset, ScoreboardMod.customFont, ScoreboardMod.textShadow, ScoreboardMod.redNumbers);
        this.setToggled(true);
    }
    
    static {
        yOffset = new NumberSetting("Y Offset", 0.0, 250.0, 1.0, 5.0);
        customFont = new BooleanSetting("Custom Font", false);
        textShadow = new BooleanSetting("Text Shadow", true);
        redNumbers = new BooleanSetting("Red Numbers", false);
    }
}
