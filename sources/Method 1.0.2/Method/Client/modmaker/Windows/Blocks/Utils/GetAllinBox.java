package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class GetAllinBox extends Block {

    public GetAllinBox() {
        super("GetAllinBox", MainBlockType.Array, Tabs.Utils, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter
        );

        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.words[0] = "X1: ";
        this.words[1] = "Y1: ";
        this.words[2] = "Z1: ";
        this.words[3] = "X2: ";
        this.words[4] = "Y2: ";
        this.words[5] = "Z2: ";
        this.description = "Gets all the blocks (BlockPos) in a box";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        int x1 = Math.min((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event), (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,3, event));
        int x2 = Math.max((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event), (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,3, event));
        int y1 = Math.min((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event), (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,4, event));
        int y2 = Math.max((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event), (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,4, event));
        int z1 = Math.min((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,2, event), (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,5, event));
        int z2 = Math.max((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,2, event), (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,5, event));
        ArrayList<BlockPos> bloc = new ArrayList<>();
        for (BlockPos allInBox : BlockPos.getAllInBox(x1, y1, z1, x2, y2, z2)) {
            bloc.add(allInBox);
        }
        return bloc;
    }

}
