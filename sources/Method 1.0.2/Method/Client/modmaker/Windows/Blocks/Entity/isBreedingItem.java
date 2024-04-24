package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;

public class isBreedingItem extends Block {

    public isBreedingItem() {
        super("isBreedingItem", MainBlockType.Boolean, Tabs.Entity, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.ItemStack));
        this.words[0] = "Animal Only!";
        this.words[1] = "Item";
        this.description = "Returns true if Itemstack is a Breeding item for given Animal   " + "\n" + "EntityAnimal.isBreedingItem(ItemStack)";

    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return ((EntityAnimal) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isBreedingItem((ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
    }

}
