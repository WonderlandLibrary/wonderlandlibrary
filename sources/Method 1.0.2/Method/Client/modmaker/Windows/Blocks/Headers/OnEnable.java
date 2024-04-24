package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;

public class OnEnable extends Block {
    public OnEnable() {
        super( "OnEnable", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.OnEnable);
        this.description = "Called when the mod is enabled";
    }


}
