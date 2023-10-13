/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.ai;

import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILookAtTradePlayer
extends EntityAIWatchClosest {
    private final EntityVillager theMerchant;
    private static final String __OBFID = "CL_00001593";

    public EntityAILookAtTradePlayer(EntityVillager p_i1633_1_) {
        super(p_i1633_1_, EntityPlayer.class, 8.0f);
        this.theMerchant = p_i1633_1_;
    }

    @Override
    public boolean shouldExecute() {
        if (this.theMerchant.isTrading()) {
            this.closestEntity = this.theMerchant.getCustomer();
            return true;
        }
        return false;
    }
}

