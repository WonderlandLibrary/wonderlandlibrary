package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Vec3d extends Block {

    public Vec3d() {
        super("Vec3d", MainBlockType.Vec3d, Tabs.NewItem, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter
                , BlockObjects.Words, BlockObjects.NumericalTextEnter
                , BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.words[0] = "X:";
        this.words[1] = "Y:";
        this.words[2] = "Z:";
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return new net.minecraft.util.math.Vec3d(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event), dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event), dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,2, event));
    }


}
