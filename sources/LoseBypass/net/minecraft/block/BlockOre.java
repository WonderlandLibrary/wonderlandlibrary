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
    public BlockOre() {
        this(Material.rock.getMaterialMapColor());
    }

    public BlockOre(MapColor p_i46390_1_) {
        super(Material.rock, p_i46390_1_);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        Item item;
        if (this == Blocks.coal_ore) {
            item = Items.coal;
            return item;
        }
        if (this == Blocks.diamond_ore) {
            item = Items.diamond;
            return item;
        }
        if (this == Blocks.lapis_ore) {
            item = Items.dye;
            return item;
        }
        if (this == Blocks.emerald_ore) {
            item = Items.emerald;
            return item;
        }
        if (this == Blocks.quartz_ore) {
            item = Items.quartz;
            return item;
        }
        item = Item.getItemFromBlock(this);
        return item;
    }

    @Override
    public int quantityDropped(Random random) {
        if (this != Blocks.lapis_ore) return 1;
        int n = 4 + random.nextInt(5);
        return n;
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        if (fortune <= 0) return this.quantityDropped(random);
        if (Item.getItemFromBlock(this) == this.getItemDropped((IBlockState)this.getBlockState().getValidStates().iterator().next(), random, fortune)) return this.quantityDropped(random);
        int i = random.nextInt(fortune + 2) - 1;
        if (i >= 0) return this.quantityDropped(random) * (i + 1);
        i = 0;
        return this.quantityDropped(random) * (i + 1);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        if (this.getItemDropped(state, worldIn.rand, fortune) == Item.getItemFromBlock(this)) return;
        int i = 0;
        if (this == Blocks.coal_ore) {
            i = MathHelper.getRandomIntegerInRange(worldIn.rand, 0, 2);
        } else if (this == Blocks.diamond_ore) {
            i = MathHelper.getRandomIntegerInRange(worldIn.rand, 3, 7);
        } else if (this == Blocks.emerald_ore) {
            i = MathHelper.getRandomIntegerInRange(worldIn.rand, 3, 7);
        } else if (this == Blocks.lapis_ore) {
            i = MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
        } else if (this == Blocks.quartz_ore) {
            i = MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
        }
        this.dropXpOnBlockBreak(worldIn, pos, i);
    }

    @Override
    public int getDamageValue(World worldIn, BlockPos pos) {
        return 0;
    }

    @Override
    public int damageDropped(IBlockState state) {
        if (this != Blocks.lapis_ore) return 0;
        int n = EnumDyeColor.BLUE.getDyeDamage();
        return n;
    }
}

