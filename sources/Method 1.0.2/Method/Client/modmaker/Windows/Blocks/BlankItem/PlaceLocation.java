package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.Utils;

public class PlaceLocation extends Block {

    public PlaceLocation() {
        super("PlaceLocation", MainBlockType.PlaceLocation, Tabs.NewItem, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter
                , BlockObjects.Words, BlockObjects.NumericalTextEnter
                , BlockObjects.Words, BlockObjects.NumericalTextEnter
                , BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.words[0] = "X:";
        this.words[1] = "Y:";
        this.words[2] = "Z:";
        this.words[3] = "Damage:";
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        // double xIn, double yIn, double zIn, double v, double v1
        return new Utils.PlaceLocation(
                dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event),
                dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event),
                dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,2, event),
                dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,3, event));
    }


}
