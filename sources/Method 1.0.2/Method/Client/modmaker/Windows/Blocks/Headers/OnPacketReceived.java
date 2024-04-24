package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;

public class OnPacketReceived extends Block {
    public OnPacketReceived() {
        super( "OnPacketReceived", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.OnPacketReceived);
        this.description = "Called when any packet is received.";
    }


}
