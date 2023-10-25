/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.IAnimals;

public enum EnumCreatureType {
    MONSTER(IMob.class, 70, Material.air, false, false),
    CREATURE(EntityAnimal.class, 10, Material.air, true, true),
    AMBIENT(EntityAmbientCreature.class, 15, Material.air, true, false),
    WATER_CREATURE(EntityWaterMob.class, 5, Material.water, true, false);

    private final int maxNumberOfCreature;
    private final Class<? extends IAnimals> creatureClass;
    private final Material creatureMaterial;
    private final boolean isAnimal;
    private final boolean isPeacefulCreature;

    public Class<? extends IAnimals> getCreatureClass() {
        return this.creatureClass;
    }

    public int getMaxNumberOfCreature() {
        return this.maxNumberOfCreature;
    }

    public boolean getAnimal() {
        return this.isAnimal;
    }

    private EnumCreatureType(Class<? extends IAnimals> clazz, int n2, Material material, boolean bl, boolean bl2) {
        this.creatureClass = clazz;
        this.maxNumberOfCreature = n2;
        this.creatureMaterial = material;
        this.isPeacefulCreature = bl;
        this.isAnimal = bl2;
    }

    public boolean getPeacefulCreature() {
        return this.isPeacefulCreature;
    }
}

