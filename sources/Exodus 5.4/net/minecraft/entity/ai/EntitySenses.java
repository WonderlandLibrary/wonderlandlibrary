/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.entity.ai;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class EntitySenses {
    List<Entity> unseenEntities;
    EntityLiving entityObj;
    List<Entity> seenEntities = Lists.newArrayList();

    public EntitySenses(EntityLiving entityLiving) {
        this.unseenEntities = Lists.newArrayList();
        this.entityObj = entityLiving;
    }

    public void clearSensingCache() {
        this.seenEntities.clear();
        this.unseenEntities.clear();
    }

    public boolean canSee(Entity entity) {
        if (this.seenEntities.contains(entity)) {
            return true;
        }
        if (this.unseenEntities.contains(entity)) {
            return false;
        }
        this.entityObj.worldObj.theProfiler.startSection("canSee");
        boolean bl = this.entityObj.canEntityBeSeen(entity);
        this.entityObj.worldObj.theProfiler.endSection();
        if (bl) {
            this.seenEntities.add(entity);
        } else {
            this.unseenEntities.add(entity);
        }
        return bl;
    }
}

