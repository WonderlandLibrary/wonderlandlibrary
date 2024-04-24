package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.item.ItemStack;

public class ItemStackAttibutes extends Block {

    public ItemStackAttibutes() {
        super("ItemStackAttibutes", MainBlockType.Numbers, Tabs.NewItem, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.DropDown);
        this.ddOptions.add("ItemDamage");
        this.ddOptions.add("getCount");
        this.ddOptions.add("getMaxStackSize");
        this.ddOptions.add("getMaxDamage");
        this.ddOptions.add("getRepairCost");
        this.words[0] = "Attribute ItemStack:";
        this.words[1] = "Value:";
        this.typesAccepted.add(typesCollapse(MainBlockType.ItemStack));
        this.description = "ItemStack Attibutes "  + " \n"+ "ItemDamage: " + " \n"+ "getCount: " + " \n"+ "getMaxStackSize: " + " \n"+ "getMaxDamage: " + " \n"+ "getRepairCost: " + " \n";


    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "ItemDamage":
                return ((ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getItemDamage();
            case "getCount":
                return ((ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getCount();
            case "getMaxStackSize":
                return ((ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaxStackSize();
            case "getMaxDamage":
                return ((ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaxDamage();
            case "getRepairCost":
                return ((ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getRepairCost();
        }
        return 0;
    }


}
