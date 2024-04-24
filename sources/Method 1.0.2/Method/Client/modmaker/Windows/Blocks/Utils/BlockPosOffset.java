package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.util.math.BlockPos;

public class BlockPosOffset extends Block {

    public BlockPosOffset() {
        super("BlockPosOffset", MainBlockType.BlockPos, Tabs.Utils, BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words,
                BlockObjects.DropDown, BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.words[0] = "BlockPos";
        this.words[1] = "Direction";
        this.words[2] = "Offset";

        ddOptions.add("Up");
        ddOptions.add("Down");
        ddOptions.add("North");
        ddOptions.add("South");
        ddOptions.add("East");
        ddOptions.add("West");

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "Up":
                return ((BlockPos) (dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event))).up((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event));
            case "Down":
                return ((BlockPos) (dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event))).down((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event));
            case "North":
                return ((BlockPos) (dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event))).north((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event));
            case "South":
                return ((BlockPos) (dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event))).south((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event));
            case "East":
                return ((BlockPos) (dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event))).east((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event));
            case "West":
                return ((BlockPos) (dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event))).west((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event));
        }
        return null;
    }

}
