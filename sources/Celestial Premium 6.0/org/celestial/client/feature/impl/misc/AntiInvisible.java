/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;

public class AntiInvisible
extends Feature {
    private final List<Entity> e = new ArrayList<Entity>();

    public AntiInvisible() {
        super("AntiInvisible", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0443\u0432\u0438\u0434\u0435\u0442\u044c \u043d\u0435\u0432\u0438\u0434\u0438\u043c\u044b\u0445 \u0441\u0443\u0449\u0435\u0441\u0442\u0432", Type.Misc);
    }

    @Override
    public void onEnable() {
        for (Entity entity : this.e) {
            entity.setInvisible(true);
        }
        this.e.clear();
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        for (Entity entity : AntiInvisible.mc.world.loadedEntityList) {
            if (!entity.isInvisible() || !(entity instanceof EntityPlayer)) continue;
            entity.setInvisible(false);
            this.e.add(entity);
        }
    }
}

