package net.minecraft.village;

import net.minecraft.entity.EntityLivingBase;

class Village.VillageAggressor {
    public EntityLivingBase agressor;
    public int agressionTime;

    Village.VillageAggressor(EntityLivingBase p_i1674_2_, int p_i1674_3_) {
        this.agressor = p_i1674_2_;
        this.agressionTime = p_i1674_3_;
    }
}
