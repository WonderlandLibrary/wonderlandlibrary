package net.minecraft.init;

import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.world.World;

static final class Bootstrap.3
extends BehaviorProjectileDispense {
    Bootstrap.3() {
    }

    protected IProjectile getProjectileEntity(World worldIn, IPosition position) {
        return new EntitySnowball(worldIn, position.getX(), position.getY(), position.getZ());
    }
}
