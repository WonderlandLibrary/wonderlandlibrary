package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.Utils;

public class PlaceLocationConvert extends Block {

    public PlaceLocationConvert() {
        super("PlaceLocationConvert", MainBlockType.Numbers, Tabs.NewItem, BlockObjects.Name, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.DropDown);
        this.ddOptions.add("X");
        this.ddOptions.add("Y");
        this.ddOptions.add("Z");
        this.ddOptions.add("Damage");
        this.ddOptions.add("Timeset");

        this.words[0] = "Get:";
        this.typesAccepted.add(typesCollapse(MainBlockType.PlaceLocation));
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "X":
                return ((Utils.PlaceLocation) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).x;
            case "Y":
                return ((Utils.PlaceLocation) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).y;
            case "Z":
                return ((Utils.PlaceLocation) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).z;
            case "Damage":
                return ((Utils.PlaceLocation) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).damage;
            case "Timeset":
                return ((Utils.PlaceLocation) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).Timeset;

        }
        return 0;
    }


}
