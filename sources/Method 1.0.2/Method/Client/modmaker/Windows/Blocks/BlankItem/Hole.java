package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.util.math.BlockPos;

public class Hole extends Block {

    public Hole() {
        super("Hole", MainBlockType.Hole, Tabs.NewItem, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter
                , BlockObjects.Words, BlockObjects.NumericalTextEnter
                , BlockObjects.Words, BlockObjects.BooleanTextEnter);
        this.words[0] = "BlockPos";
        this.words[1] = "HoleType";
        this.words[2] = "IsTall";
        this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        this.typesAccepted.add(typesCollapse(MainBlockType.HoleType));
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        BlockPos b = (BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
        Method.Client.managers.Hole.HoleType h = (Method.Client.managers.Hole.HoleType) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 1, event);
        boolean t = (boolean) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, event);
        return new Method.Client.managers.Hole(b, h, t);
    }


}
