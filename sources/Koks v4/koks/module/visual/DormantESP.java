package koks.module.visual;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.manager.value.annotation.Value;
import koks.event.OutlineEvent;
import koks.event.UpdateEvent;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.optifine.reflect.Reflector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "DormantESP", description = "You can see the last position of a player", category = Module.Category.VISUAL)
public class DormantESP extends Module implements Module.Shader {

    private final ArrayList<Entity> entities = new ArrayList<>();
    private final ConcurrentHashMap<Entity, Long> dormant = new ConcurrentHashMap<>();

    @Value(name = "Time", minimum = 1, maximum = 10)
    double time = 3;

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof final OutlineEvent outlineEvent) {
            outlineEvent.setAllowOutline(true);
        }

        if (event instanceof UpdateEvent) {
            for (Entity entity : getWorld().loadedEntityList) {
                if (!entities.contains(entity))
                    entities.add(entity);
                else
                    entities.remove(entity);
            }

            for (Entity entity : entities) {
                if (entity instanceof EntityPlayer) {
                    if (!getWorld().loadedEntityList.contains(entity)) {
                        if (!dormant.containsKey(entity))
                            dormant.put(entity, System.currentTimeMillis());
                    } else {
                        dormant.remove(entity);
                    }
                }
            }

            for (Entity entity : dormant.keySet()) {
                long time = System.currentTimeMillis() - dormant.get(entity);
                if (time >= 1000 * this.time) {
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

    }

    @Override
    public void drawShaderESP(List list, boolean flag, RenderManager renderManager, float partialTicks, Entity renderViewEntity, ICamera camera, int i) {
        for (int k = 0; k < dormant.size(); ++k) {
            final Entity entity3 = (Entity) dormant.keySet().toArray()[k];
            if (!flag || Reflector.callBoolean(entity3, Reflector.ForgeEntity_shouldRenderInPass, i)) {
                renderManager.renderEntitySimple(entity3, partialTicks);
            }
        }
    }
}
