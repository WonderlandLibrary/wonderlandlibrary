// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.cosmetics;

import moonsense.event.impl.SCWorldUnloadedEvent;
import moonsense.event.SubscribeEvent;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.Minecraft;
import moonsense.event.impl.SCClientTickEvent;
import moonsense.event.EventBus;
import moonsense.MoonsenseClient;
import moonsense.cosmetics.physics.EyePhysics;
import net.minecraft.client.entity.AbstractClientPlayer;
import java.util.WeakHashMap;

public class PhysicsManager
{
    private WeakHashMap<AbstractClientPlayer, EyePhysics> physicsList;
    private static PhysicsManager instance;
    
    static {
        PhysicsManager.instance = null;
    }
    
    public PhysicsManager() {
        this.physicsList = new WeakHashMap<AbstractClientPlayer, EyePhysics>();
    }
    
    public static PhysicsManager getInstance() {
        if (PhysicsManager.instance == null) {
            PhysicsManager.instance = new PhysicsManager();
            MoonsenseClient.INSTANCE.getEventManager();
            EventBus.register(PhysicsManager.instance);
        }
        return PhysicsManager.instance;
    }
    
    public EyePhysics getPhysics(final AbstractClientPlayer player) {
        if (!this.physicsList.containsKey(player)) {
            this.physicsList.put(player, new EyePhysics(player));
        }
        return this.physicsList.get(player);
    }
    
    @SubscribeEvent
    public void onTick(final SCClientTickEvent event) {
        if (Minecraft.getMinecraft().theWorld != null && !Minecraft.getMinecraft().isGamePaused()) {
            final Iterator<Map.Entry<AbstractClientPlayer, EyePhysics>> iterator = this.physicsList.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<AbstractClientPlayer, EyePhysics> e = iterator.next();
                final EyePhysics ep = e.getValue();
                if (ep.getPlayer().worldObj.getWorldTime() - ep.getLastUpdate() > 3L) {
                    iterator.remove();
                }
                else {
                    ep.update();
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onWorldUnload(final SCWorldUnloadedEvent event) {
        final Iterator<Map.Entry<AbstractClientPlayer, EyePhysics>> iterator = this.physicsList.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<AbstractClientPlayer, EyePhysics> e = iterator.next();
            final EyePhysics ep = e.getValue();
            if (ep.getPlayer().worldObj == event.worldObject) {
                iterator.remove();
            }
        }
    }
}
