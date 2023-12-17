// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.EntityCreature;

public class EntityAIRestrictSun extends EntityAIBase
{
    private final EntityCreature entity;
    
    public EntityAIRestrictSun(final EntityCreature creature) {
        this.entity = creature;
    }
    
    @Override
    public boolean shouldExecute() {
        return this.entity.world.isDaytime() && this.entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
    }
    
    @Override
    public void startExecuting() {
        ((PathNavigateGround)this.entity.getNavigator()).setAvoidSun(true);
    }
    
    @Override
    public void resetTask() {
        ((PathNavigateGround)this.entity.getNavigator()).setAvoidSun(false);
    }
}
