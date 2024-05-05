package net.minecraft.init;

import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.world.World;

static final class Bootstrap.2
extends BehaviorProjectileDispense {
    Bootstrap.2() {
    }

    protected IProjectile getProjectileEntity(World worldIn, IPosition position) {
        return new EntityEgg(worldIn, position.getX(), position.getY(), position.getZ());
    }
}
