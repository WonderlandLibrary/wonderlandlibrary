package Method.Client.modmaker.Windows.Blocks.VarHelper;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Join extends Block {

    public Join() {
        super("Join", MainBlockType.String, Tabs.VarHelper, BlockObjects.NumericalTextEnter,BlockObjects.Words,BlockObjects.NumericalTextEnter);
        this.words[0] = " Join ";
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.description = "Joins the strings together";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) + (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event);
    }

}
