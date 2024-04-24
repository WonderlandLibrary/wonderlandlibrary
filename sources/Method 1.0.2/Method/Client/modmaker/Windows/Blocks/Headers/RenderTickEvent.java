package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;

public class RenderTickEvent extends Block {
    public RenderTickEvent() {
        super( "RenderTickEvent", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.RenderTickEvent);
        this.description = "Called when the game is rendering.";
    }
}
