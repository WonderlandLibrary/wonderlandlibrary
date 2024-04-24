package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;

public class onPlayerTick extends Block {
    public onPlayerTick() {
        super( "onPlayerTick", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.PlayerTickEvent);
        this.description = "Called when the player ticks";
    }

}
