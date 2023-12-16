// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.event.impl;

import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import moonsense.event.SCEvent;

public class SCRenderPlayerEvent extends SCEvent
{
    public final EntityPlayer player;
    public final RenderPlayer renderer;
    public final float partialRenderTick;
    public final double x;
    public final double y;
    public final double z;
    
    public SCRenderPlayerEvent(final EntityPlayer player, final RenderPlayer renderer, final float partialRenderTick, final double x, final double y, final double z) {
        this.player = player;
        this.renderer = renderer;
        this.partialRenderTick = partialRenderTick;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
