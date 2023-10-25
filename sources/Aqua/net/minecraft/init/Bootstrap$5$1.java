package net.minecraft.init;

import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

class Bootstrap.1
extends BehaviorProjectileDispense {
    final /* synthetic */ ItemStack val$stack;

    Bootstrap.1(ItemStack itemStack) {
        this.val$stack = itemStack;
    }

    protected IProjectile getProjectileEntity(World worldIn, IPosition position) {
        return new EntityPotion(worldIn, position.getX(), position.getY(), position.getZ(), this.val$stack.copy());
    }

    protected float func_82498_a() {
        return super.func_82498_a() * 0.5f;
    }

    protected float func_82500_b() {
        return super.func_82500_b() * 1.25f;
    }
}
