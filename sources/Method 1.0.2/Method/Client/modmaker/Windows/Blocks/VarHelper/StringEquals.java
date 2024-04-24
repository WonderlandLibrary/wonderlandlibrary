package Method.Client.modmaker.Windows.Blocks.VarHelper;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class StringEquals extends Block {

    public StringEquals() {
        super("StringEquals", MainBlockType.Boolean, Tabs.VarHelper, BlockObjects.NumericalTextEnter ,BlockObjects.Words,
                BlockObjects.NumericalTextEnter);
        this.words[0] = " StringEquals ";
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.description = "Returns true if the string Equals the word";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event){
            return ((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).equalsIgnoreCase((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event));
    }


}
