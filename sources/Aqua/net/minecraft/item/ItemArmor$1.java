package net.minecraft.item;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;

static final class ItemArmor.1
extends BehaviorDefaultDispenseItem {
    ItemArmor.1() {
    }

    protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing((int)source.getBlockMetadata()));
        int i = blockpos.getX();
        int j = blockpos.getY();
        int k = blockpos.getZ();
        AxisAlignedBB axisalignedbb = new AxisAlignedBB((double)i, (double)j, (double)k, (double)(i + 1), (double)(j + 1), (double)(k + 1));
        List list = source.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb, Predicates.and((Predicate)EntitySelectors.NOT_SPECTATING, (Predicate)new EntitySelectors.ArmoredMob(stack)));
        if (list.size() > 0) {
            EntityLivingBase entitylivingbase = (EntityLivingBase)list.get(0);
            int l = entitylivingbase instanceof EntityPlayer ? 1 : 0;
            int i1 = EntityLiving.getArmorPosition((ItemStack)stack);
            ItemStack itemstack = stack.copy();
            itemstack.stackSize = 1;
            entitylivingbase.setCurrentItemOrArmor(i1 - l, itemstack);
            if (entitylivingbase instanceof EntityLiving) {
                ((EntityLiving)entitylivingbase).setEquipmentDropChance(i1, 2.0f);
            }
            --stack.stackSize;
            return stack;
        }
        return super.dispenseStack(source, stack);
    }
}
