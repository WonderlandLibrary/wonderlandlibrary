package Method.Client.modmaker.Windows.Blocks.VarHelper;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.util.math.AxisAlignedBB;

public class BoundingChange extends Block {

    public BoundingChange() {
        super("BoundingChange", MainBlockType.BoundingBox, Tabs.VarHelper, BlockObjects.Name,BlockObjects.NumericalTextEnter,
                BlockObjects.DropDown, BlockObjects.NumericalTextEnter);
        ddOptions.add("Grow");
        ddOptions.add("Shrink");
        this.typesAccepted.add(typesCollapse(MainBlockType.BoundingBox));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Shrink or Grow Bounding Box " + "\n" + "Boundingbox.shrink(Value) Boundingbox.grow(Value)";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "Grow":
                return ((AxisAlignedBB) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).grow(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event));
            case "Shrink":
                return ((AxisAlignedBB) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).shrink(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event));
        }
        return null;

    }

}
