package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.event.world.ChunkEvent;

public class LoadChunk extends Block {
    public LoadChunk() {
        super( "LoadChunk", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.LoadChunk);
        this.description = "Called when a chunk is loaded";
    }

    public static class Chunk extends Block {
        public Chunk() {
            super( "Chunk", MainBlockType.Chunk, Tabs.Sub, BlockObjects.Name, Headers.LoadChunk);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((ChunkEvent.Load)event).getChunk();
        }
    }


}
