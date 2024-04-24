package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.EntityLivingBase;

public class GetEntitySwing extends Block {

    public GetEntitySwing() {
        super("GetEntitySwing", MainBlockType.Numbers, Tabs.Entity, BlockObjects.Name, BlockObjects.DropDown, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        ddOptions.add("swingProgress");
        ddOptions.add("swingProgressInt");
        this.description = "Returns SwingProgress of EntityLiving" + "\n" + "(EntityLivingBase).swingProgress";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "swingProgress":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).swingProgress;
            case "swingProgressInt":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).swingProgressInt;
        }
        return 0;
    }

}
