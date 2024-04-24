package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.item.EntityItem;

public class ItemAttributesString extends Block {

    public ItemAttributesString() {
        super("ItemAttributesString", MainBlockType.String, Tabs.NewItem, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.DropDown);
        this.ddOptions.add("thrower");
        this.ddOptions.add("owner");

        this.words[0] = "Attribute Item:";
        this.words[1] = "Value:";
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Item Attibutes " + " \n"+ "thrower: " + " \n"+ "owner: " + " \n";


    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "thrower":
                return ((EntityItem) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getThrower();
            case "owner":
                return ((EntityItem) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getOwner();
        }
        return 0;
    }


}
