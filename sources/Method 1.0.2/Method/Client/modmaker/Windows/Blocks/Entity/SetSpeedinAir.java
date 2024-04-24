package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.player.EntityPlayer;

public class SetSpeedinAir extends Block {

    public SetSpeedinAir() {
        super("SetSpeedinAir", MainBlockType.Default, Tabs.Entity, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.NumericalTextEnter);
        this.words[0] = "PlayerOnly";
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Set Entity Player Speed in air   " + "\n" + "EntityPlayer.speedInAir ";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        ((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).speedInAir = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event);
        super.runCode(dragableBlock, event);
    }

}
