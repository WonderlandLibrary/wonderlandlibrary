package net.minecraft.entity.monster;

import net.minecraft.entity.IEntityLivingData;

class EntityZombie.GroupData
implements IEntityLivingData {
    public boolean isChild = false;
    public boolean isVillager = false;

    private EntityZombie.GroupData(boolean isBaby, boolean isVillagerZombie) {
        this.isChild = isBaby;
        this.isVillager = isVillagerZombie;
    }
}
