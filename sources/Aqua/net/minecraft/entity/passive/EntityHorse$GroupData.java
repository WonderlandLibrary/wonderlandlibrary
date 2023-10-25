package net.minecraft.entity.passive;

import net.minecraft.entity.IEntityLivingData;

public static class EntityHorse.GroupData
implements IEntityLivingData {
    public int horseType;
    public int horseVariant;

    public EntityHorse.GroupData(int type, int variant) {
        this.horseType = type;
        this.horseVariant = variant;
    }
}
