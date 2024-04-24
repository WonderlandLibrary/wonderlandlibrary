package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class toString extends Block {

    public toString() {
        super("toString", MainBlockType.String, Tabs.NewItem, BlockObjects.Words, BlockObjects.DropDown, BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.words[0] = "Convert: ";
        this.words[1] = "to String";

        this.ddOptions.add("Object");
        this.ddOptions.add("Number");
        this.ddOptions.add("Boolean");
        this.typesAccepted.add(typesCollapse(MainBlockType.Wild));
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        if (dragableBlock.dropDowns.getSelected().equals("Object"))
            return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event).toString();
        else if (dragableBlock.dropDowns.getSelected().equals("Number"))
            return String.valueOf(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event));
        else if (dragableBlock.dropDowns.getSelected().equals("Boolean"))
            return String.valueOf(dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
        else return "";
    }


}
