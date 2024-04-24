package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.item.EntityItem;

public class ItemAttributesNum extends Block {

    public ItemAttributesNum() {
        super("ItemAttributesNum", MainBlockType.Numbers, Tabs.NewItem, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.DropDown);
        this.ddOptions.add("age");
        this.ddOptions.add("pickupDelay");
        this.ddOptions.add("lifespan");
        this.ddOptions.add("getCount");
        this.ddOptions.add("getMaxDamage");
        this.ddOptions.add("getMaxStackSize");

        this.words[0] = "Attribute Item:";
        this.words[1] = "Value:";
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "ENTITY Item Attibutes  " + " \n"+ "age: " + " \n"+ "pickupDelay: " + " \n"+ "lifespan: " + " \n" + "getCount: " + " \n" + "getMaxDamage: " + " \n" + "getMaxStackSize: " + " \n";


    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "age":
                return ((EntityItem) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).age;
            case "pickupDelay":
                return ((EntityItem) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).pickupDelay;
            case "lifespan":
                return ((EntityItem) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).lifespan;
            case "getCount":
                return ((EntityItem) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getItem().getCount();
            case "getMaxDamage":
                return ((EntityItem) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getItem().getMaxDamage();
            case "getMaxStackSize":
                return ((EntityItem) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getItem().getMaxStackSize();


        }
        return 0;
    }


}
