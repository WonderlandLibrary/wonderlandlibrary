package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.Patcher.Events.PlayerMoveEvent;

public class onPlayerMove extends Block {
    public onPlayerMove() {
        super( "onPlayerMove", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.PlayerMoveEvent);
        this.description = "Called when a player moves";
    }
    public static class Playerove extends Block {
        public Playerove() {
            super( "Playerove", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.PlayerMoveEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((PlayerMoveEvent)event).getPlayer();
        }
    }
}
