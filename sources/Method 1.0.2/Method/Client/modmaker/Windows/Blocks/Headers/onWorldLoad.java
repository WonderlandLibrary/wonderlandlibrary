package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.event.world.WorldEvent;

public class onWorldLoad extends Block {
    public onWorldLoad() {
        super( "onWorldLoad", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.onWorldLoad);
        this.description = "Called when the world is loaded";
    }

    public static class getWorld extends Block {
        public getWorld() {
            super( "getWorld", MainBlockType.Worlds, Tabs.Sub, BlockObjects.Name, Headers.onWorldLoad);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((WorldEvent.Load)event).getWorld();
        }
    }

}

