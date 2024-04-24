package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;

public class PostMotion extends Block {
    public PostMotion() {
        super( "PostMotion", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.PostMotionEvent);
        this.description = "Called after the player moves";
    }


}
