package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.world.chunk.EmptyChunk;

public class IsEmptyChunk extends Block {

    public IsEmptyChunk() {
        super("IsEmptyChunk", MainBlockType.Boolean, Tabs.Utils, BlockObjects.Name,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.words[0] = "X";
        this.words[1] = "Y";
        this.description = "Returns true if the chunk at X, Y is empty (EmptyChunk)";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return Minecraft.getMinecraft().world.getChunk((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event), (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event)) instanceof EmptyChunk;
    }

}
