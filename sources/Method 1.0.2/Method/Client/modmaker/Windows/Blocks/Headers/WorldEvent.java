package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class WorldEvent extends Block {
    public WorldEvent() {
        super( "WorldEvent", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.WorldEvent);
        this.description = "Called when a world event occurs.";
    }

    public static class World extends Block {
        public World() {
            super( "World", MainBlockType.Worlds, Tabs.Sub, BlockObjects.Name, Headers.WorldEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((net.minecraftforge.event.world.WorldEvent)event).getWorld();
        }
    }

}
