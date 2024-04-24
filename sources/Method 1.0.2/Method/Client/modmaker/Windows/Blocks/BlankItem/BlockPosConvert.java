package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.util.math.BlockPos;

public class BlockPosConvert extends Block {

    public BlockPosConvert() {
        super("BlockPosConvert", MainBlockType.Numbers, Tabs.NewItem, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.DropDown);
        this.ddOptions.add("X");
        this.ddOptions.add("Y");
        this.ddOptions.add("Z");
        this.description = "Pulls x, y, or z from a BlockPos";

        this.words[0] = "Blockpos:";
        this.words[1] = "Num:";
        this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));

    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "X":
                return ((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).x;
            case "Y":
                return ((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).y;
            case "Z":
                return ((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).z;

        }
        return 0;
    }


}
