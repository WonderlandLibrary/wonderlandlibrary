package net.minecraft.item;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemMinecart
extends Item {
    private static final IBehaviorDispenseItem dispenserMinecartBehavior = new /* Unavailable Anonymous Inner Class!! */;
    private final EntityMinecart.EnumMinecartType minecartType;

    public ItemMinecart(EntityMinecart.EnumMinecartType type) {
        this.maxStackSize = 1;
        this.minecartType = type;
        this.setCreativeTab(CreativeTabs.tabTransport);
        BlockDispenser.dispenseBehaviorRegistry.putObject((Object)this, (Object)dispenserMinecartBehavior);
    }

    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        if (BlockRailBase.isRailBlock((IBlockState)iblockstate)) {
            if (!worldIn.isRemote) {
                BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getBlock() instanceof BlockRailBase ? (BlockRailBase.EnumRailDirection)iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                double d0 = 0.0;
                if (blockrailbase$enumraildirection.isAscending()) {
                    d0 = 0.5;
                }
                EntityMinecart entityminecart = EntityMinecart.getMinecart((World)worldIn, (double)((double)pos.getX() + 0.5), (double)((double)pos.getY() + 0.0625 + d0), (double)((double)pos.getZ() + 0.5), (EntityMinecart.EnumMinecartType)this.minecartType);
                if (stack.hasDisplayName()) {
                    entityminecart.setCustomNameTag(stack.getDisplayName());
                }
                worldIn.spawnEntityInWorld((Entity)entityminecart);
            }
            --stack.stackSize;
            return true;
        }
        return false;
    }

    static /* synthetic */ EntityMinecart.EnumMinecartType access$000(ItemMinecart x0) {
        return x0.minecartType;
    }
}
