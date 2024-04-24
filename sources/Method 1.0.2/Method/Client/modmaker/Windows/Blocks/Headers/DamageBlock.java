package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.Patcher.Events.PlayerDamageBlockEvent;

public class DamageBlock extends Block {
    public DamageBlock() {
        super( "DamageBlock", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.PlayerDamageBlockEvent);
        this.description = "PlayerDamageBlockEvent";
    }

    public static class pos extends Block {
        public pos() {
            super( "pos", MainBlockType.BlockPos, Tabs.Sub, BlockObjects.Name, Headers.PlayerDamageBlockEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((PlayerDamageBlockEvent) event).getPos();
        }
    }

    public static class Facing extends Block {
        public Facing() {
            super( "Facing", MainBlockType.Facing, Tabs.Sub, BlockObjects.Name, Headers.PlayerDamageBlockEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((PlayerDamageBlockEvent) event).getFacing();
        }
    }

}
