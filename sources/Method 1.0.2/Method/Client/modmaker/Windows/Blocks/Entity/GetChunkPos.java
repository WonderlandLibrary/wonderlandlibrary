package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;


public class GetChunkPos extends Block {

    public GetChunkPos() {
        super("GetChunkPos", MainBlockType.BlockPos, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);

        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns ChunkPos of entity" + "\n" + "(Entity).getChunk";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        Chunk chunk = Minecraft.getMinecraft().world.getChunk(((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getPosition());
        return new BlockPos(chunk.x, 0, chunk.z);
    }

}
