// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.render;

import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.BooleanValue;
import net.augustus.modules.Module;

public class Scoreboard extends Module
{
    public BooleanValue remove;
    public BooleanValue stick;
    public DoubleValue xCord;
    public DoubleValue yCord;
    
    public Scoreboard() {
        super("Scoreboard", new Color(34, 162, 162), Categorys.RENDER);
        this.remove = new BooleanValue(1, "Remove", this, false);
        this.stick = new BooleanValue(4, "Stick", this, false);
        this.xCord = new DoubleValue(2, "PositionX", this, 100.0, 0.0, 100.0, 1);
        this.yCord = new DoubleValue(3, "PositionY", this, 60.0, 0.0, 100.0, 1);
    }
}
