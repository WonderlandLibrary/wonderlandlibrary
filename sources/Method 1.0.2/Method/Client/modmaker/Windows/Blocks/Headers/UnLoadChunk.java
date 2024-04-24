package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.event.world.ChunkEvent;

public class UnLoadChunk extends Block {
    public UnLoadChunk() {
        super( "UnLoadChunk", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.UnLoadChunk);
        this.description = "Called when a chunk is unloaded";
    }

    public static class Chunkunk extends Block {
        public Chunkunk() {
            super( "Chunkunk", MainBlockType.Chunk, Tabs.Sub, BlockObjects.Name, Headers.UnLoadChunk);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((ChunkEvent.Unload)event).getChunk();
        }
    }


}
