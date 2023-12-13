package koks.manager.module.impl.render;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventOutline;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kroko
 * @created on 23.10.2020 : 02:03
 */

@ModuleInfo(name = "DormantESP", category = Module.Category.RENDER, description = "You the last position of a player before he doesn't render")
public class DormantESP extends Module {

    public ArrayList<Entity> entities = new ArrayList<>();
    public ConcurrentHashMap<Entity, Long> dormant = new ConcurrentHashMap<>();

    public Setting time = new Setting("Time", 3, 1, 10, false, this);

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventOutline) {
            ((EventOutline) event).setOutline(true);
        }

        if (event instanceof EventUpdate) {
            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (!entities.contains(entity))
                    entities.add(entity);
                else
                    entities.remove(entity);
            }

            for (Entity entity : entities) {
                if(entity instanceof EntityPlayer) {
                    if (!mc.theWorld.loadedEntityList.contains(entity)) {
                        if (!dormant.containsKey(entity))
                            dormant.put(entity, System.currentTimeMillis());
                    } else {
                        dormant.remove(entity);
                    }
                }
            }

            for (Entity entity : dormant.keySet()) {
                long time = System.currentTimeMillis() - dormant.get(entity);
                if (time >= 1000 * this.time.getCurrentValue()) {
                    dormant.remove(entity);
                    entities.remove(entity);
                }
            }

        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        dormant.clear();
        entities.clear();
    }
}
