/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockOre
extends Block {
    @Override
    public Item getItemDropped(IBlockState iBlockState, Random random, int n) {
        return this == Blocks.coal_ore ? Items.coal : (this == Blocks.diamond_ore ? Items.diamond : (this == Blocks.lapis_ore ? Items.dye : (this == Blocks.emerald_ore ? Items.emerald : (this == Blocks.quartz_ore ? Items.quartz : Item.getItemFromBlock(this)))));
    }

    public BlockOre(MapColor mapColor) {
        super(Material.rock, mapColor);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public int quantityDropped(Random random) {
        return this == Blocks.lapis_ore ? 4 + random.nextInt(5) : 1;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos blockPos, IBlockState iBlockState, float f, int n) {
        super.dropBlockAsItemWithChance(world, blockPos, iBlockState, f, n);
        if (this.getItemDropped(iBlockState, world.rand, n) != Item.getItemFromBlock(this)) {
            int n2 = 0;
            if (this == Blocks.coal_ore) {
                n2 = MathHelper.getRandomIntegerInRange(world.rand, 0, 2);
            } else if (this == Blocks.diamond_ore) {
                n2 = MathHelper.getRandomIntegerInRange(world.rand, 3, 7);
            } else if (this == Blocks.emerald_ore) {
                n2 = MathHelper.getRandomIntegerInRange(world.rand, 3, 7);
            } else if (this == Blocks.lapis_ore) {
                n2 = MathHelper.getRandomIntegerInRange(world.rand, 2, 5);
            } else if (this == Blocks.quartz_ore) {
                n2 = MathHelper.getRandomIntegerInRange(world.rand, 2, 5);
            }
            this.dropXpOnBlockBreak(world, blockPos, n2);
        }
    }

    public BlockOre() {
        this(Material.rock.getMaterialMapColor());
    }

    @Override
    public int getDamageValue(World world, BlockPos blockPos) {
        return 0;
    }

    @Override
    public int quantityDroppedWithBonus(int n, Random random) {
        if (n > 0 && Item.getItemFromBlock(this) != this.getItemDropped((IBlockState)this.getBlockState().getValidStates().iterator().next(), random, n)) {
            int n2 = random.nextInt(n + 2) - 1;
            if (n2 < 0) {
                n2 = 0;
            }
            return this.quantityDropped(random) * (n2 + 1);
        }
        return this.quantityDropped(random);
    }

    @Override
    public int damageDropped(IBlockState iBlockState) {
        return this == Blocks.lapis_ore ? EnumDyeColor.BLUE.getDyeDamage() : 0;
    }
}

