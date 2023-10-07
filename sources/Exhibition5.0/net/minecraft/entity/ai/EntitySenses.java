// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.EntityLiving;

public class EntitySenses
{
    EntityLiving entityObj;
    List seenEntities;
    List unseenEntities;
    private static final String __OBFID = "CL_00001628";
    
    public EntitySenses(final EntityLiving p_i1672_1_) {
        this.seenEntities = Lists.newArrayList();
        this.unseenEntities = Lists.newArrayList();
        this.entityObj = p_i1672_1_;
    }
    
    public void clearSensingCache() {
        this.seenEntities.clear();
        this.unseenEntities.clear();
    }
    
    public boolean canSee(final Entity p_75522_1_) {
        if (this.seenEntities.contains(p_75522_1_)) {
            return true;
        }
        if (this.unseenEntities.contains(p_75522_1_)) {
            return false;
        }
        this.entityObj.worldObj.theProfiler.startSection("canSee");
        final boolean var2 = this.entityObj.canEntityBeSeen(p_75522_1_);
        this.entityObj.worldObj.theProfiler.endSection();
        if (var2) {
            this.seenEntities.add(p_75522_1_);
        }
        else {
            this.unseenEntities.add(p_75522_1_);
        }
        return var2;
    }
}
