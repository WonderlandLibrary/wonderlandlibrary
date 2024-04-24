package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.Patcher.Events.EntityPlayerJumpEvent;

public class onPlayerJump extends Block {
    public onPlayerJump() {
        super( "onPlayerJump", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.EntityPlayerJumpEvent);
        this.description = "Called when a player jumps";
    }

    public static class Playerump extends Block {
        public Playerump() {
            super( "Playerump", MainBlockType.Entity, Tabs.Sub, BlockObjects.Name, Headers.EntityPlayerJumpEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((EntityPlayerJumpEvent)event).getPlayer();
        }
    }
}
