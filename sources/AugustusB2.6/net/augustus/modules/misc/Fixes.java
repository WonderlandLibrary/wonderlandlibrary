// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.misc;

import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.utils.TimeHelper;
import net.augustus.settings.BooleanValue;
import net.augustus.modules.Module;

public class Fixes extends Module
{
    public final BooleanValue mouseDelayFix;
    public final BooleanValue hitDelayFix;
    public final TimeHelper timeHelper;
    
    public Fixes() {
        super("Fixes", new Color(169, 66, 237), Categorys.MISC);
        this.mouseDelayFix = new BooleanValue(1, "MouseDelayFix", this, true);
        this.hitDelayFix = new BooleanValue(2, "HitDelayFix", this, false);
        this.timeHelper = new TimeHelper();
    }
}
