package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class onRenderPre extends Block {
    public onRenderPre() {
        super("onRenderPre", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.onRenderPre);
        this.description = "Called before the render method is called.";
    }

    public static class getTypeOV extends Block {
        public getTypeOV() {
            super("getTypeOV", MainBlockType.ElementType, Tabs.Sub, BlockObjects.Name, Headers.onRenderPre);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((RenderGameOverlayEvent.Pre) event).getType();
        }
    }
}
