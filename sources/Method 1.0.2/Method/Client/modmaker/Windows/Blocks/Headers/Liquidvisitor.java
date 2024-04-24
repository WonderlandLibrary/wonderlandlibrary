package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;

public class Liquidvisitor extends Block {
    public Liquidvisitor() {
        super( "Liquidvisitor", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.Liquidvisitor);
        this.description = "Custom liquidvisitor";
    }
}
