// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.render;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventItemRenderer;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.DoubleValue;
import net.augustus.modules.Module;

public class CustomItemPos extends Module
{
    public DoubleValue posX;
    public DoubleValue posY;
    public DoubleValue posZ;
    public DoubleValue blockPosX;
    public DoubleValue blockPosY;
    public DoubleValue blockPosZ;
    public DoubleValue scale;
    
    public CustomItemPos() {
        super("CustomItemPos", new Color(42, 145, 95), Categorys.RENDER);
        this.posX = new DoubleValue(1, "X", this, 0.13, -1.0, 1.0, 2);
        this.posY = new DoubleValue(2, "Y", this, 0.13, -1.0, 1.0, 2);
        this.posZ = new DoubleValue(3, "Z", this, 0.04, -1.0, 1.0, 2);
        this.blockPosX = new DoubleValue(5, "BlockX", this, -0.27, -1.0, 1.0, 2);
        this.blockPosY = new DoubleValue(6, "BlockY", this, -0.04, -1.0, 1.0, 2);
        this.blockPosZ = new DoubleValue(7, "BlockZ", this, 0.0, -1.0, 1.0, 2);
        this.scale = new DoubleValue(4, "Scale", this, 0.2, 0.0, 2.0, 2);
    }
    
    @EventTarget
    public void onEventItemRenderer(final EventItemRenderer eventItemRenderer) {
        eventItemRenderer.setX(this.posX.getValue());
        eventItemRenderer.setY(this.posY.getValue());
        eventItemRenderer.setZ(this.posZ.getValue());
        eventItemRenderer.setBlockX(this.blockPosX.getValue());
        eventItemRenderer.setBlockY(this.blockPosY.getValue());
        eventItemRenderer.setBlockZ(this.blockPosZ.getValue());
        eventItemRenderer.setScale(this.scale.getValue());
    }
}
