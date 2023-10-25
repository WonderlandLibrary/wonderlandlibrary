/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.stream;

import net.minecraft.client.stream.Metadata;
import net.minecraft.entity.EntityLivingBase;

public class MetadataCombat
extends Metadata {
    public MetadataCombat(EntityLivingBase entityLivingBase, EntityLivingBase entityLivingBase2) {
        super("player_combat");
        this.func_152808_a("player", entityLivingBase.getName());
        if (entityLivingBase2 != null) {
            this.func_152808_a("primary_opponent", entityLivingBase2.getName());
        }
        if (entityLivingBase2 != null) {
            this.func_152807_a("Combat between " + entityLivingBase.getName() + " and " + entityLivingBase2.getName());
        } else {
            this.func_152807_a("Combat between " + entityLivingBase.getName() + " and others");
        }
    }
}

