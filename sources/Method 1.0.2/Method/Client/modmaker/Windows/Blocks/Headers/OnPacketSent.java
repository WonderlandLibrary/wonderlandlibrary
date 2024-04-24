package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;

public class OnPacketSent extends Block {
    public OnPacketSent() {
        super( "OnPacketSent", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.OnPacketSent);
        this.description = "Called when any packet is sent.";
    }


}
