/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.eyes;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.eyes.EyePhysics;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.game.EventUnloadWorld;
import org.celestial.client.event.events.impl.player.EventUpdate;

public class PhysicsManager {
    private WeakHashMap<AbstractClientPlayer, EyePhysics> physicsList = new WeakHashMap();
    private static PhysicsManager instance = null;

    public static PhysicsManager getInstance() {
        if (instance == null) {
            instance = new PhysicsManager();
            EventManager.register(instance);
        }
        return instance;
    }

    public EyePhysics getPhysics(AbstractClientPlayer player) {
        if (!this.physicsList.containsKey(player)) {
            this.physicsList.put(player, new EyePhysics(player));
        }
        return this.physicsList.get(player);
    }

    @EventTarget
    public void onTick(EventUpdate event) {
        if (Minecraft.getMinecraft().world != null && !Minecraft.getMinecraft().isGamePaused()) {
            Iterator<Map.Entry<AbstractClientPlayer, EyePhysics>> iterator = this.physicsList.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<AbstractClientPlayer, EyePhysics> e = iterator.next();
                EyePhysics ep = e.getValue();
                if (ep.getPlayer().world.getWorldTime() - ep.getLastUpdate() > 3L) {
                    iterator.remove();
                    continue;
                }
                ep.update();
            }
        }
    }

    @EventTarget
    public void onWorldUload(EventUnloadWorld event) {
        Iterator<Map.Entry<AbstractClientPlayer, EyePhysics>> iterator = this.physicsList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<AbstractClientPlayer, EyePhysics> e = iterator.next();
            EyePhysics ep = e.getValue();
            if (ep.getPlayer().world != event.getWorld()) continue;
            iterator.remove();
        }
    }
}

