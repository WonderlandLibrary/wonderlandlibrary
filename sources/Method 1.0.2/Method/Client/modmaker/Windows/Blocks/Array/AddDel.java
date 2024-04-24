package Method.Client.modmaker.Windows.Blocks.Array;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.heldArray;

public class AddDel extends Block {

    public AddDel() {
        super("AddDel", MainBlockType.Default, Tabs.Arrays, BlockObjects.DropDown, BlockObjects.NumericalTextEnter);
        ddOptions.add("Add");
        ddOptions.add("Remove");
        this.typesAccepted.add(typesCollapse(MainBlockType.Wild));
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "Add":
                ((heldArray) dragableBlock.blockPointer).array.add(dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
                break;
            case "Remove":
                ((heldArray) dragableBlock.blockPointer).array.remove(dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
                break;
        }
        super.runCode(dragableBlock, event);
    }

}
