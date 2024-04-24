package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;

public class CreatePlayer extends Block {

    public CreatePlayer() {
        super("CreatePlayer", MainBlockType.Entity, Tabs.Utils, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.BooleanTextEnter);
        this.words[0] = "Player";
        this.words[1] = "Name";
        this.words[2] = "ShowPlayer";
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.description = "Creates a (Client-Side) player";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
         return Utils.createPlayer((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event), (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,2, event));
    }

}
