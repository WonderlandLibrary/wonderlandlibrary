package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.player.EntityPlayer;

public class GetEntityFood extends Block {

    public GetEntityFood() {
        super("GetEntityFood", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);
        ddOptions.add("foodLevel");
        ddOptions.add("foodSaturationLevel");
        ddOptions.add("foodExhaustionLevel");
        ddOptions.add("foodTimer");
        ddOptions.add("prevFoodLevel");

        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns Health of Entity" + "\n" + "(Entity).GetEntityFood()";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "foodLevel":
                return ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getFoodStats().foodLevel;
            case "foodSaturationLevel":
                return ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getFoodStats().foodSaturationLevel;
            case "foodExhaustionLevel":
                return ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getFoodStats().foodExhaustionLevel;
            case "foodTimer":
                return ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getFoodStats().foodTimer;
            case "prevFoodLevel":
                return ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getFoodStats().prevFoodLevel;
        }
        return 0;
    }

}
