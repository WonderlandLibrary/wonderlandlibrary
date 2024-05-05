package net.minecraft.init;

import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;

static final class Bootstrap.1
extends BehaviorProjectileDispense {
    Bootstrap.1() {
    }

    protected IProjectile getProjectileEntity(World worldIn, IPosition position) {
        EntityArrow entityarrow = new EntityArrow(worldIn, position.getX(), position.getY(), position.getZ());
        entityarrow.canBePickedUp = 1;
        return entityarrow;
    }
}
