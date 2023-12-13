// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.movement;

import exhibition.event.impl.EventPacket;
import exhibition.event.RegisterEvent;
import net.minecraft.util.AxisAlignedBB;
import exhibition.event.impl.EventBlockBounds;
import net.minecraft.block.material.Material;
import exhibition.util.PlayerUtil;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class Jesus extends Module
{
    private boolean shouldOffsetPacket;
    
    public Jesus(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventMotion.class, EventPacket.class, EventBlockBounds.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion em = (EventMotion)event;
            if (PlayerUtil.isInLiquid() && Jesus.mc.thePlayer.isInsideOfMaterial(Material.air) && !Jesus.mc.thePlayer.isSneaking()) {
                Jesus.mc.thePlayer.motionY = 0.05;
                em.setGround(false);
            }
        }
        if (event instanceof EventBlockBounds) {
            final EventBlockBounds ebb = (EventBlockBounds)event;
            if (!this.shouldSetBoundingBox()) {
                ebb.setBounds(new AxisAlignedBB(ebb.getPos().getX(), ebb.getPos().getY(), ebb.getPos().getZ(), ebb.getPos().getX() + 1, ebb.getPos().getY() + 1, ebb.getPos().getZ() + 1));
            }
        }
    }
    
    private boolean shouldSetBoundingBox() {
        return !Jesus.mc.thePlayer.isSneaking() && Jesus.mc.thePlayer.fallDistance < 4.0f;
    }
}
