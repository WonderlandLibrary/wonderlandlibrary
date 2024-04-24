package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class UUID extends Block {

    public UUID() {
        super("UUID", MainBlockType.UUID, Tabs.NewItem, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter
                , BlockObjects.Words, BlockObjects.NumericalTextEnter
        );
        this.words[0] = "MostBit:";
        this.words[1] = "LeastBit:";
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return new java.util.UUID((long) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event), (long) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event));
    }


}
