// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAILookAtTradePlayer extends EntityAIWatchClosest
{
    private final EntityVillager villager;
    
    public EntityAILookAtTradePlayer(final EntityVillager villagerIn) {
        super(villagerIn, EntityPlayer.class, 8.0f);
        this.villager = villagerIn;
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.villager.isTrading()) {
            this.closestEntity = this.villager.getCustomer();
            return true;
        }
        return false;
    }
}
