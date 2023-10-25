package net.minecraft.entity.passive;

import net.minecraft.entity.IEntityLivingData;

public static class EntityRabbit.RabbitTypeData
implements IEntityLivingData {
    public int typeData;

    public EntityRabbit.RabbitTypeData(int type) {
        this.typeData = type;
    }
}
