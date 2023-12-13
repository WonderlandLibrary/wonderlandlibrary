// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.modules.render;

import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.modules.Module;

public class BlockAnimation extends Module
{
    public final StringValue animation;
    public final DoubleValue height;
    public final BooleanValue fakeBlock;
    public final DoubleValue speed;
    
    public BlockAnimation() {
        super("BlockAnimation", new Color(136, 29, 163), Categorys.RENDER);
        this.animation = new StringValue(69, "Animation", this, "1.7", new String[] { "1.7", "Exhibition", "Slide" });
        this.height = new DoubleValue(2, "Height", this, 0.0, -0.5, 1.5, 2);
        this.fakeBlock = new BooleanValue(3, "FakeBlock", this, true);
        this.speed = new DoubleValue(4, "Speed", this, 1.0, 0.1, 3.0, 1);
    }
}
