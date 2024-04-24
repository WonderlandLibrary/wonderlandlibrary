package Method.Client.modmaker.Windows.Blocks.Logic;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class ForObject extends Block {

    public ForObject() {
        super("ForObject", MainBlockType.Wild, Tabs.Logic, BlockObjects.Name,BlockObjects.DropDown);
        this.description = "Returns the object of the for loop";
        this.ddOptions.add("1");
        this.ddOptions.add("2");
        this.ddOptions.add("3");
        this.ddOptions.add("4");
        this.ddOptions.add("5");
        this.ddOptions.add("6");
        this.ddOptions.add("7");
        this.ddOptions.add("8");

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return dragableBlock.local.codeExecuter.loopObj[Integer.parseInt(dragableBlock.dropDowns.getSelected()) ];
    }
}
