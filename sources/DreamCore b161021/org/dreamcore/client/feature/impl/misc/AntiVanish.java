package org.dreamcore.client.feature.impl.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;

import java.util.ArrayList;
import java.util.List;

public class AntiVanish extends Feature {

    private final List<Entity> e = new ArrayList<>();

    public AntiVanish() {
        super("Anti Vanish", "Позволяет увидеть невидимых существ", Type.Misc);
    }

    @Override
    public void onEnable() {
        for (Entity entity : e) {
            entity.setInvisible(true);
        }
        e.clear();
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity.isInvisible() && entity instanceof EntityPlayer) {
                entity.setInvisible(false);
                e.add(entity);
            }
        }
    }
}