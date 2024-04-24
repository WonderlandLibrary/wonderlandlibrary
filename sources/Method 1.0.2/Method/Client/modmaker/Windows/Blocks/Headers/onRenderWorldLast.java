package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;

public class onRenderWorldLast extends Block {

    public onRenderWorldLast() {
        super( "onRenderWorldLast", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.RenderWorldLastEvent);
        this.description = "Called when the world is rendered.";
    }

}
