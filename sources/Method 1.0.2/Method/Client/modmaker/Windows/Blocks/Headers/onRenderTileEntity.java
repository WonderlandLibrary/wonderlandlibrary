package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.Patcher.Events.RenderTileEntityEvent;

public class onRenderTileEntity extends Block {
    public onRenderTileEntity() {
        super( "onRenderTileEntity", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.PlayerRespawnEvent);
        this.description = "Called when a tile entity is rendered.";
    }

    public static class getTileEntity extends Block {
        public getTileEntity() {
            super( "getTileEntity", MainBlockType.TileEntity, Tabs.Sub, BlockObjects.Name, Headers.PlayerRespawnEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((RenderTileEntityEvent)event).getTileEntity();
        }
    }


}
