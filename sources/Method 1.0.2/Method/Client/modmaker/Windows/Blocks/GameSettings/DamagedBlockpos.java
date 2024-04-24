package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.renderer.DestroyBlockProgress;

public class DamagedBlockpos extends Block {

    public DamagedBlockpos() {
        super("DamagedBlockpos", MainBlockType.BlockPos, Tabs.Game, BlockObjects.Name,BlockObjects.NumericalTextEnter);
        this.description = "Use only with getDamagedBlocks";
        this.typesAccepted.add(typesCollapse(MainBlockType.Wild));
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event){
    return ((DestroyBlockProgress) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getPosition();
    }

}
