package net.minecraft.init;

import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.world.World;

static final class Bootstrap.4
extends BehaviorProjectileDispense {
    Bootstrap.4() {
    }

    protected IProjectile getProjectileEntity(World worldIn, IPosition position) {
        return new EntityExpBottle(worldIn, position.getX(), position.getY(), position.getZ());
    }

    protected float func_82498_a() {
        return super.func_82498_a() * 0.5f;
    }

    protected float func_82500_b() {
        return super.func_82500_b() * 1.25f;
    }
}
