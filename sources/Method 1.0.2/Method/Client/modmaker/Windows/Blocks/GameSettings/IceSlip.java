package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.init.Blocks;

public class IceSlip extends Block {

    public IceSlip() {
        super("IceSlip", MainBlockType.Default, Tabs.Game, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Set slipperiness of blocks" + "\n" + "Blocks.ICE.slipperiness = " +
                "\n" + "Blocks.PACKED_ICE.slipperiness = " + "\n" + "Blocks.FROSTED_ICE.slipperiness = ";
        ddOptions.add("ICE");
        ddOptions.add("PACKED_ICE");
        ddOptions.add("FROSTED_ICE");
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "ICE":
                Blocks.ICE.setDefaultSlipperiness((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
                break;
            case "PACKED_ICE":
                Blocks.PACKED_ICE.setDefaultSlipperiness((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
                break;
            case "FROSTED_ICE":
                Blocks.FROSTED_ICE.setDefaultSlipperiness((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
                break;
        }
        super.runCode(dragableBlock, event);
    }

}
