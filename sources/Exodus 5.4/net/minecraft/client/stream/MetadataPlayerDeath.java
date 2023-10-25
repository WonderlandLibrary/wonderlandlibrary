/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.stream;

import net.minecraft.client.stream.Metadata;
import net.minecraft.entity.EntityLivingBase;

public class MetadataPlayerDeath
extends Metadata {
    public MetadataPlayerDeath(EntityLivingBase entityLivingBase, EntityLivingBase entityLivingBase2) {
        super("player_death");
        if (entityLivingBase != null) {
            this.func_152808_a("player", entityLivingBase.getName());
        }
        if (entityLivingBase2 != null) {
            this.func_152808_a("killer", entityLivingBase2.getName());
        }
    }
}

