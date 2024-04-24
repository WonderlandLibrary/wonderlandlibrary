package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;

public class postBackgroundTooltipRender extends Block {
    public postBackgroundTooltipRender() {
        super( "postBackgroundTooltipRender", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.PostBackground);
        this.description = "Called after the tooltip is rendered.";
    }
}
