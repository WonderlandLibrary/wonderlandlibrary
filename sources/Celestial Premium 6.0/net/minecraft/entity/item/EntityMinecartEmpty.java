/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.item;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityMinecartEmpty
extends EntityMinecart {
    public EntityMinecartEmpty(World worldIn) {
        super(worldIn);
    }

    public EntityMinecartEmpty(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public static void registerFixesMinecartEmpty(DataFixer fixer) {
        EntityMinecart.registerFixesMinecart(fixer, EntityMinecartEmpty.class);
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand stack) {
        if (player.isSneaking()) {
            return false;
        }
        if (this.isBeingRidden()) {
            return true;
        }
        if (!this.world.isRemote) {
            player.startRiding(this);
        }
        return true;
    }

    @Override
    public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
        if (receivingPower) {
            if (this.isBeingRidden()) {
                this.removePassengers();
            }
            if (this.getRollingAmplitude() == 0) {
                this.setRollingDirection(-this.getRollingDirection());
                this.setRollingAmplitude(10);
                this.setDamage(50.0f);
                this.setBeenAttacked();
            }
        }
    }

    @Override
    public EntityMinecart.Type getType() {
        return EntityMinecart.Type.RIDEABLE;
    }
}

