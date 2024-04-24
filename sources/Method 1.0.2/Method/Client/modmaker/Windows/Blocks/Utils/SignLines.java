package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.tileentity.TileEntitySign;

public class SignLines extends Block {

    public SignLines() {
        super("SignLines", MainBlockType.String, Tabs.Utils,BlockObjects.Name, BlockObjects.NumericalTextEnter,BlockObjects.DropDown);
        this.typesAccepted.add(typesCollapse(MainBlockType.TileEntity));
        this.description = "Returns the text of the sign at the given position";
        ddOptions.add("0");
        ddOptions.add("1");
        ddOptions.add("2");
        ddOptions.add("3");
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return ((TileEntitySign) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).signText[Integer.parseInt(dragableBlock.dropDowns.getSelected())].getUnformattedText();
    }

}
