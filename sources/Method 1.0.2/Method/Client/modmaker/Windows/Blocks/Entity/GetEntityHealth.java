package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.EntityLivingBase;

public class GetEntityHealth extends Block {

    public GetEntityHealth() {
        super("GetEntityHealth", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);
        ddOptions.add("getHealth");
        ddOptions.add("getMaxHealth");
        ddOptions.add("getAbsorptionAmount");
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns Health of Entity" + "\n" + "(Entity).getHealth()";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "getHealth":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getHealth();
            case "getMaxHealth":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getMaxHealth();
            case "getAbsorptionAmount":
                return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getAbsorptionAmount();

        }
        return 0;
    }

}
