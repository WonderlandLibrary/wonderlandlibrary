package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemMultiTexture
extends ItemBlock {
    protected final Block theBlock;
    protected final Function<ItemStack, String> nameFunction;

    public ItemMultiTexture(Block block, Block block2, Function<ItemStack, String> nameFunction) {
        super(block);
        this.theBlock = block2;
        this.nameFunction = nameFunction;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public ItemMultiTexture(Block block, Block block2, String[] namesByMeta) {
        this(block, block2, (Function<ItemStack, String>)new /* Unavailable Anonymous Inner Class!! */);
    }

    public int getMetadata(int damage) {
        return damage;
    }

    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + (String)this.nameFunction.apply((Object)stack);
    }
}
