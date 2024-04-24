package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Null extends Block {

    public Null() {
        super("Null", MainBlockType.Wild, Tabs.NewItem, BlockObjects.Name, BlockObjects.Words);
        this.words[0] = "CAREFUL!";

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return null;
    }

}
