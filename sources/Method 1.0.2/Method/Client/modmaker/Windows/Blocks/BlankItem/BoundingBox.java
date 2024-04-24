package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.util.math.AxisAlignedBB;

public class BoundingBox extends Block {

    public BoundingBox() {
        super("BoundingBox", MainBlockType.BoundingBox, Tabs.NewItem, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter
                , BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);

        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.words[0] = "X:";
        this.words[1] = "Y:";
        this.words[2] = "Z:";

        this.words[3] = "X2:";
        this.words[4] = "Y2:";
        this.words[5] = "Z2:";

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return new AxisAlignedBB(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event), dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event), dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,2, event),
                dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,3, event),
                dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,4, event),
                dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,5, event));


    }


}
