package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.Main;
import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.player.EntityPlayer;

public class GetPops extends Block {

    public GetPops() {
        super("GetPops", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.description = "Get totem pops per player";
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return Main.PlayerManager.getPops((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
    }

}
