package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.player.EntityPlayer;

public class GetSpeedinAir extends Block {

    public GetSpeedinAir() {
        super("AirSpeed", MainBlockType.Numbers, Tabs.Entity, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.words[0] = "PlayerOnly";
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Given a Player returns AirSpeed  " + "\n" + "(EntityPlayer).speedInAir";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).speedInAir;
    }

}
