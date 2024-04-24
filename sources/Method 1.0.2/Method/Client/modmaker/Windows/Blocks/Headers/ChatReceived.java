package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;

public class ChatReceived extends Block {
    public ChatReceived() {
        super( "ChatReceived", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.ClientChatReceivedEvent);
        this.description = "ClientChatReceivedEvent";
    }


}
