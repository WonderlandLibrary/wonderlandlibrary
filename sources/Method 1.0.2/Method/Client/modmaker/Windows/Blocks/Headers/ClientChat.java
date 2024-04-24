package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;

public class ClientChat extends Block {
    public ClientChat() {
        super( "ClientChat", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.ClientChatEvent);
        this.description = "ClientChatEvent";
    }


}
