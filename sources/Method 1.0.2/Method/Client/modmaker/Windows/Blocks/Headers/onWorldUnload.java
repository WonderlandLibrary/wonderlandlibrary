package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.event.world.WorldEvent;

public class onWorldUnload extends Block {
    public onWorldUnload() {
        super( "onWorldUnload", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.WorldEventunload);
        this.description = "Called when the world is unloaded";
    }

    public static class getWorldoad extends Block {
        public getWorldoad() {
            super( "getWorldoad", MainBlockType.Worlds, Tabs.Sub, BlockObjects.Name, Headers.WorldEventunload);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((WorldEvent.Unload)event).getWorld();
        }
    }

}
