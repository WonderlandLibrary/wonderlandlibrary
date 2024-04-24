package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;

public class OnDisable extends Block {
    public OnDisable() {
        super( "OnDisable", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.OnDisable);
        this.description = "Called when the mod is disabled";
    }


}
