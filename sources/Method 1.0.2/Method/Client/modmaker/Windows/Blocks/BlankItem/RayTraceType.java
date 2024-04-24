package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.util.math.RayTraceResult;

public class RayTraceType extends Block {
    public RayTraceType() {
        super("RayTraceType", MainBlockType.Boolean, Tabs.NewItem, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.DropDown);
        for (RayTraceResult.Type value : RayTraceResult.Type.values()) {
            this.ddOptions.add(value.toString());
        }
        this.typesAccepted.add(typesCollapse(MainBlockType.RayTraceResult));
        this.description = "RayTraceResult.Type typeOfHit";
    }


    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "MISS":
                return ((RayTraceResult) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).typeOfHit == RayTraceResult.Type.MISS;
            case "BLOCK":
                return ((RayTraceResult) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).typeOfHit == RayTraceResult.Type.BLOCK;
            case "ENTITY":
                return ((RayTraceResult) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).typeOfHit == RayTraceResult.Type.ENTITY;
        }
        return false;
    }
}
