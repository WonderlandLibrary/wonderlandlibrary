package Method.Client.modmaker.Windows.Blocks.Render;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.util.math.AxisAlignedBB;

public class BBExpand extends Block {

    public BBExpand() {
        super("BBExpand", MainBlockType.BoundingBox, Tabs.Render, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter );


        this.words[0] = "BoundingBox:";
        this.words[1] = "X:";
        this.words[2] = "Y:";
        this.words[3] = "Z:";
        this.words[4] = "X2:";
        this.words[5] = "Y2:";
        this.words[6] = "Z2:";

        this.typesAccepted.add(typesCollapse(MainBlockType.BoundingBox));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Expand BoundingBox";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        AxisAlignedBB boundingBox = (AxisAlignedBB) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
        double x = (double) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
        double y = (double) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 2, event);
        double z = (double) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 3, event);
        double x2 = (double) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 4, event);
        double y2 = (double) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 5, event);
        double z2 = (double) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 6, event);
        return new AxisAlignedBB(boundingBox.minX + x, boundingBox.minY + y, boundingBox.minZ + z, boundingBox.maxX + x2, boundingBox.maxY + y2, boundingBox.maxZ + z2);

    }


}
