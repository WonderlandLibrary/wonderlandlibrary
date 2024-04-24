package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.Utils;

public class PlaceLocationSet extends Block {

    public PlaceLocationSet() {
        super("PlaceLocationSet", MainBlockType.Default, Tabs.NewItem, BlockObjects.Name, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.DropDown,
                BlockObjects.Words,
                BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.PlaceLocation));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.ddOptions.add("X");
        this.ddOptions.add("Y");
        this.ddOptions.add("Z");
        this.ddOptions.add("Damage");
        this.ddOptions.add("Timeset");

        this.words[0] = "Set:";
        this.words[1] = "To:";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        double numb = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
        switch (dragableBlock.dropDowns.getSelected()) {
            case "X":
                ((Utils.PlaceLocation) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).x = (int) numb;
                break;
            case "Y":
                ((Utils.PlaceLocation) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).y = (int) numb;
                break;
            case "Z":
                ((Utils.PlaceLocation) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).z = (int) numb;
                break;
            case "Damage":
                ((Utils.PlaceLocation) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).damage = (int) numb;
                break;
            case "Timeset":
                ((Utils.PlaceLocation) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).Timeset = (int) numb;
                break;

        }
        super.runCode(dragableBlock, event);
    }

}
