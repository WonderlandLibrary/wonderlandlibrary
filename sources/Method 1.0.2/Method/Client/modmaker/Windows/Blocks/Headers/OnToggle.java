package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;

public class OnToggle extends Block {
    public OnToggle() {
        super( "OnToggle", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.OnToggle);
        this.description = "Called when the Module is toggled,either on or off.";
    }


}
