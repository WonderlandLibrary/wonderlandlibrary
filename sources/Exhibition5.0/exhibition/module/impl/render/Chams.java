// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.render;

import exhibition.event.RegisterEvent;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import exhibition.event.impl.EventRender3D;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class Chams extends Module
{
    public Chams(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventRender3D.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventRender3D) {
            final EventRender3D er = (EventRender3D)event;
            for (final Object o : Chams.mc.theWorld.loadedEntityList) {
                if (o instanceof EntityPlayer) {
                    final EntityPlayer entity = (EntityPlayer)o;
                    if (entity != Chams.mc.thePlayer) {}
                }
            }
        }
    }
}
