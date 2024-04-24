package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;

public class RenderBlockOverlay extends Block {
    public RenderBlockOverlay() {
        super( "RenderBlockOverlay", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.RenderBlockOverlayEvent);
        this.description = "Called when the block is rendered.";
    }

}
