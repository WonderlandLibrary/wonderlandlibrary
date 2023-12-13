// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.world;

import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.BooleanValue;
import net.augustus.modules.Module;

public class FastBreak extends Module
{
    public BooleanValue instant;
    public DoubleValue multiplier;
    
    public FastBreak() {
        super("FastBreak", new Color(52, 152, 219), Categorys.WORLD);
        this.instant = new BooleanValue(1, "Instant", this, false);
        this.multiplier = new DoubleValue(2, "Multiplier", this, 2.0, 1.0, 2.0, 2);
    }
}
