// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockOre extends Block
{
    public BlockOre() {
        this(Material.ROCK.getMaterialMapColor());
    }
    
    public BlockOre(final MapColor color) {
        super(Material.ROCK, color);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        if (this == Blocks.COAL_ORE) {
            return Items.COAL;
        }
        if (this == Blocks.DIAMOND_ORE) {
            return Items.DIAMOND;
        }
        if (this == Blocks.LAPIS_ORE) {
            return Items.DYE;
        }
        if (this == Blocks.EMERALD_ORE) {
            return Items.EMERALD;
        }
        return (this == Blocks.QUARTZ_ORE) ? Items.QUARTZ : Item.getItemFromBlock(this);
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return (this == Blocks.LAPIS_ORE) ? (4 + random.nextInt(5)) : 1;
    }
    
    @Override
    public int quantityDroppedWithBonus(final int fortune, final Random random) {
        if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped((IBlockState)this.getBlockState().getValidStates().iterator().next(), random, fortune)) {
            int i = random.nextInt(fortune + 2) - 1;
            if (i < 0) {
                i = 0;
            }
            return this.quantityDropped(random) * (i + 1);
        }
        return this.quantityDropped(random);
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        if (this.getItemDropped(state, worldIn.rand, fortune) != Item.getItemFromBlock(this)) {
            int i = 0;
            if (this == Blocks.COAL_ORE) {
                i = MathHelper.getInt(worldIn.rand, 0, 2);
            }
            else if (this == Blocks.DIAMOND_ORE) {
                i = MathHelper.getInt(worldIn.rand, 3, 7);
            }
            else if (this == Blocks.EMERALD_ORE) {
                i = MathHelper.getInt(worldIn.rand, 3, 7);
            }
            else if (this == Blocks.LAPIS_ORE) {
                i = MathHelper.getInt(worldIn.rand, 2, 5);
            }
            else if (this == Blocks.QUARTZ_ORE) {
                i = MathHelper.getInt(worldIn.rand, 2, 5);
            }
            this.dropXpOnBlockBreak(worldIn, pos, i);
        }
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(this);
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return (this == Blocks.LAPIS_ORE) ? EnumDyeColor.BLUE.getDyeDamage() : 0;
    }
}
