package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.Entity;


public class GetEntityFacing extends Block {

    public GetEntityFacing() {
        super("GetEntityFacing", MainBlockType.Facing, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);
        ddOptions.add("getHorizontalFacing");
        ddOptions.add("AdjustedHorizontalFacing");
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "getHorizontalFacing":
                return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getHorizontalFacing();
            case "AdjustedHorizontalFacing":
                return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getAdjustedHorizontalFacing();
        }
        return 0;
    }

}
