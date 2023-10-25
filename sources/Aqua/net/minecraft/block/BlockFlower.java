package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/*
 * Exception performing whole class analysis ignored.
 */
public abstract class BlockFlower
extends BlockBush {
    protected PropertyEnum<EnumFlowerType> type;

    protected BlockFlower() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(this.getTypeProperty(), (Comparable)(this.getBlockType() == EnumFlowerColor.RED ? EnumFlowerType.POPPY : EnumFlowerType.DANDELION)));
    }

    public int damageDropped(IBlockState state) {
        return ((EnumFlowerType)state.getValue(this.getTypeProperty())).getMeta();
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (EnumFlowerType blockflower$enumflowertype : EnumFlowerType.getTypes((EnumFlowerColor)this.getBlockType())) {
            list.add((Object)new ItemStack(itemIn, 1, blockflower$enumflowertype.getMeta()));
        }
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(this.getTypeProperty(), (Comparable)EnumFlowerType.getType((EnumFlowerColor)this.getBlockType(), (int)meta));
    }

    public abstract EnumFlowerColor getBlockType();

    public IProperty<EnumFlowerType> getTypeProperty() {
        if (this.type == null) {
            this.type = PropertyEnum.create((String)"type", EnumFlowerType.class, (Predicate)new /* Unavailable Anonymous Inner Class!! */);
        }
        return this.type;
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumFlowerType)state.getValue(this.getTypeProperty())).getMeta();
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{this.getTypeProperty()});
    }

    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XZ;
    }
}
