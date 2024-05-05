package net.minecraft.init;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.properties.IProperty;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

static final class Bootstrap.15
extends BehaviorDefaultDispenseItem {
    private boolean field_179240_b = true;

    Bootstrap.15() {
    }

    protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        World world = source.getWorld();
        EnumFacing enumfacing = BlockDispenser.getFacing((int)source.getBlockMetadata());
        BlockPos blockpos = source.getBlockPos().offset(enumfacing);
        BlockSkull blockskull = Blocks.skull;
        if (world.isAirBlock(blockpos) && blockskull.canDispenserPlace(world, blockpos, stack)) {
            if (!world.isRemote) {
                world.setBlockState(blockpos, blockskull.getDefaultState().withProperty((IProperty)BlockSkull.FACING, (Comparable)EnumFacing.UP), 3);
                TileEntity tileentity = world.getTileEntity(blockpos);
                if (tileentity instanceof TileEntitySkull) {
                    if (stack.getMetadata() == 3) {
                        GameProfile gameprofile = null;
                        if (stack.hasTagCompound()) {
                            String s;
                            NBTTagCompound nbttagcompound = stack.getTagCompound();
                            if (nbttagcompound.hasKey("SkullOwner", 10)) {
                                gameprofile = NBTUtil.readGameProfileFromNBT((NBTTagCompound)nbttagcompound.getCompoundTag("SkullOwner"));
                            } else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isNullOrEmpty((String)(s = nbttagcompound.getString("SkullOwner")))) {
                                gameprofile = new GameProfile((UUID)null, s);
                            }
                        }
                        ((TileEntitySkull)tileentity).setPlayerProfile(gameprofile);
                    } else {
                        ((TileEntitySkull)tileentity).setType(stack.getMetadata());
                    }
                    ((TileEntitySkull)tileentity).setSkullRotation(enumfacing.getOpposite().getHorizontalIndex() * 4);
                    Blocks.skull.checkWitherSpawn(world, blockpos, (TileEntitySkull)tileentity);
                }
                --stack.stackSize;
            }
        } else {
            this.field_179240_b = false;
        }
        return stack;
    }

    protected void playDispenseSound(IBlockSource source) {
        if (this.field_179240_b) {
            source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
        } else {
            source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
        }
    }
}
