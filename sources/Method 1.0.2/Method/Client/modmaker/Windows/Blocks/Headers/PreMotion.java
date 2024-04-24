package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;

public class PreMotion extends Block {
    public PreMotion() {
        super( "PreMotion", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.PreMotionEvent);
        this.description = "Called before the player moves";
    }

}
