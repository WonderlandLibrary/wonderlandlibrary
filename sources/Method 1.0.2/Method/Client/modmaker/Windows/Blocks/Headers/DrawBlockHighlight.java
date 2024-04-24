package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;

public class DrawBlockHighlight extends Block {
    public DrawBlockHighlight() {
        super("DrawBlockHighlight", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.DrawBlockHighlightEvent);
        this.description = "DrawBlockHighlightEvent";
    }

    public static class getBlockPos extends Block {
        public getBlockPos() {
            super("getBlockPos", MainBlockType.BlockPos, Tabs.Sub, BlockObjects.Name, Headers.DrawBlockHighlightEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((DrawBlockHighlightEvent) event).getTarget().getBlockPos();
        }
    }
}
