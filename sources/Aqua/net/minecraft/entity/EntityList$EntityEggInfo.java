package net.minecraft.entity;

import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;

public static class EntityList.EntityEggInfo {
    public final int spawnedID;
    public final int primaryColor;
    public final int secondaryColor;
    public final StatBase field_151512_d;
    public final StatBase field_151513_e;

    public EntityList.EntityEggInfo(int id, int baseColor, int spotColor) {
        this.spawnedID = id;
        this.primaryColor = baseColor;
        this.secondaryColor = spotColor;
        this.field_151512_d = StatList.getStatKillEntity((EntityList.EntityEggInfo)this);
        this.field_151513_e = StatList.getStatEntityKilledBy((EntityList.EntityEggInfo)this);
    }
}
