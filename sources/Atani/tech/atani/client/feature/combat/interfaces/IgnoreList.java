package tech.atani.client.feature.combat.interfaces;

import net.minecraft.entity.Entity;

public interface IgnoreList {

    boolean shouldSkipEntity(Entity entity);

}
