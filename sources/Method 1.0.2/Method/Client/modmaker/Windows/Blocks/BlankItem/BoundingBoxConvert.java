package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.util.math.AxisAlignedBB;

public class BoundingBoxConvert extends Block {

    public BoundingBoxConvert() {
        super("BoundingBoxConvert", MainBlockType.Numbers, Tabs.NewItem, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.DropDown);
        this.ddOptions.add("X1");
        this.ddOptions.add("Y1");
        this.ddOptions.add("Z1");
        this.ddOptions.add("X2");
        this.ddOptions.add("Y2");
        this.ddOptions.add("Z2");
        this.words[0] = "BoundingBox:";
        this.words[1] = "Num:";
        this.typesAccepted.add(typesCollapse(MainBlockType.BoundingBox));
        this.description = "Returns Value's of a BoundingBox";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "X1":
                return ((AxisAlignedBB) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).minX;
            case "Y1":
                return ((AxisAlignedBB) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).minY;
            case "Z1":
                return ((AxisAlignedBB) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).minZ;
            case "X2":
                return ((AxisAlignedBB) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).maxX;
            case "Y2":
                return ((AxisAlignedBB) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).maxY;
            case "Z2":
                return ((AxisAlignedBB) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).maxZ;
        }
        return 0;
    }


}
