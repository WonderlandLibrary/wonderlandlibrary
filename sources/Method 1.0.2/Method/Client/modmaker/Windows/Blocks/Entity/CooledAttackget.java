package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.player.EntityPlayer;

public class CooledAttackget extends Block {

    public CooledAttackget() {
        super("StrengthGet", MainBlockType.Numbers, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns getCooledAttackStrength 0-1";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getCooledAttackStrength(0);
    }

}
