package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;

public class OnClientTick extends Block {

    public OnClientTick() {
        super( "OnClientTick", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.OnClientTick);
        this.description = "Called when the client ticks";
    }

}
