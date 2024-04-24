package Method.Client.modmaker.Windows.Blocks.Math;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class ObjectEquals extends Block {

    public ObjectEquals() {
        super("ObjectEquals", MainBlockType.Boolean, Tabs.Math, BlockObjects.NumericalTextEnter, BlockObjects.Name, BlockObjects.NumericalTextEnter);

        this.typesAccepted.add(typesCollapse(MainBlockType.Wild));
        this.typesAccepted.add(typesCollapse(MainBlockType.Wild));
        this.description = "Returns true if the two objects are equal";

    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event).equals(dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event));
    }
}
