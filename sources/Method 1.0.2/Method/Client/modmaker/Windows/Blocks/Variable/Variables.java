package Method.Client.modmaker.Windows.Blocks.Variable;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;

public class Variables extends Block {

    public Variables(String name) {
        super(name, MainBlockType.Null, Tabs.Variables, BlockObjects.Name);
    }

}
