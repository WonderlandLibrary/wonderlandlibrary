// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.entity.EntityLiving;

public class EntitySenses
{
    EntityLiving entity;
    List<Entity> seenEntities;
    List<Entity> unseenEntities;
    
    public EntitySenses(final EntityLiving entityIn) {
        this.seenEntities = (List<Entity>)Lists.newArrayList();
        this.unseenEntities = (List<Entity>)Lists.newArrayList();
        this.entity = entityIn;
    }
    
    public void clearSensingCache() {
        this.seenEntities.clear();
        this.unseenEntities.clear();
    }
    
    public boolean canSee(final Entity entityIn) {
        if (this.seenEntities.contains(entityIn)) {
            return true;
        }
        if (this.unseenEntities.contains(entityIn)) {
            return false;
        }
        this.entity.world.profiler.startSection("canSee");
        final boolean flag = this.entity.canEntityBeSeen(entityIn);
        this.entity.world.profiler.endSection();
        if (flag) {
            this.seenEntities.add(entityIn);
        }
        else {
            this.unseenEntities.add(entityIn);
        }
        return flag;
    }
}
