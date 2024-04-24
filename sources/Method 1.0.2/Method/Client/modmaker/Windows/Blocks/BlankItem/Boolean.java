package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Boolean extends Block {

    public Boolean() {
        super("Boolean", MainBlockType.Boolean, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
        this.ddOptions.add("True");
        this.ddOptions.add("False");

    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return dragableBlock.dropDowns.getSelected().equals("True");
    }


}
