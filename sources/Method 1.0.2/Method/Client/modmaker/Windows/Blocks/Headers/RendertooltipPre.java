package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;

public class RendertooltipPre extends Block {
    public RendertooltipPre() {
        super( "RendertooltipPre", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.RenderTooltipEvent);
        this.description = "Called before the tooltip is rendered.";
    }

}
